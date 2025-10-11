import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.io.File;
import java.io.IOException;
import java.awt.geom.*;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    // 画面サイズ
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    private Thread gameThread;
    private boolean running = false;

    // 画像リソース
    private BufferedImage carImage;
    private BufferedImage courseImage;

    // 車のプロパティ
    private double carX, carY;
    private double carAngle = 0.0;
    private double carSpeed = 0.0;

    // --- 改良点：物理演算に関する定数 ---
    private final double ACCELERATION = 0.2;  // 加速量
    private final double FRICTION = 0.08;     // 摩擦による減速量
    private final double MAX_SPEED = 8.0;     // 最高速度
    private final double TURN_SPEED = 3.5;    // 回転速度

    // キー入力状態を保持するフラグ
    private boolean upPressed, downPressed, leftPressed, rightPressed;

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        // 画像の読み込み
        loadImages();

        // 車の初期位置
        carX = 350;
        carY = 450;
    }

    private void loadImages() {
        try {
            carImage = ImageIO.read(new File("car.png"));
            courseImage = ImageIO.read(new File("course.png"));
        } catch (IOException e) {
            System.err.println("画像の読み込みに失敗しました。");
            e.printStackTrace();
        }
    }

    public void startGame() {
        if (gameThread == null) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {
        // --- 改良点：物理演算 ---
        // 1. キー入力に応じた加減速
        if (upPressed) {
            carSpeed += ACCELERATION;
        } else if (downPressed) {
            carSpeed -= ACCELERATION;
        }

        // 2. 摩擦による自然減速
        if (carSpeed > 0) {
            carSpeed -= FRICTION;
        } else if (carSpeed < 0) {
            carSpeed += FRICTION;
        }
        // 非常に遅い速度になったら停止
        if (Math.abs(carSpeed) < FRICTION) {
            carSpeed = 0;
        }

        // 3. 最高速度の制限
        if (carSpeed > MAX_SPEED) {
            carSpeed = MAX_SPEED;
        }
        if (carSpeed < -MAX_SPEED / 2) { // バックは最高速度の半分
            carSpeed = -MAX_SPEED / 2;
        }

        // 4. 回転（速度が出ているときほど曲がりにくくしても良い）
        if (carSpeed != 0) {
            if (leftPressed) {
                carAngle -= TURN_SPEED;
            }
            if (rightPressed) {
                carAngle += TURN_SPEED;
            }
        }
        
        // 前の座標を保存
        double prevX = carX;
        double prevY = carY;

        // 座標の更新
        carX += Math.sin(Math.toRadians(carAngle)) * carSpeed;
        carY -= Math.cos(Math.toRadians(carAngle)) * carSpeed;

        // 当たり判定
        handleCollision(prevX, prevY);
    }
    
    private void handleCollision(double prevX, double prevY) {
        if (carImage == null || courseImage == null) return;
        
        int centerX = (int) (carX + carImage.getWidth() / 2);
        int centerY = (int) (carY + carImage.getHeight() / 2);

        // 画面外チェック
        if (centerX < 0 || centerX >= SCREEN_WIDTH || centerY < 0 || centerY >= SCREEN_HEIGHT) {
            carX = prevX;
            carY = prevY;
            carSpeed = 0; // 壁に当たったら停止
            return;
        }

        // コースアウト判定 (コース外の色を緑系統と仮定)
        int rgb = courseImage.getRGB(centerX, centerY);
        Color roadColor = new Color(rgb);
        if (roadColor.getGreen() > 150 && roadColor.getRed() < 100 && roadColor.getBlue() < 100) {
            carX = prevX;
            carY = prevY;
            carSpeed *= 0.5; // コースアウトしたら減速
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // コースを描画
        if (courseImage != null) {
            g2d.drawImage(courseImage, 0, 0, null);
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            g.setColor(Color.RED);
            g.drawString("course.png が見つかりません", 50, 50);
        }

        // 車を描画
        if (carImage != null) {
            // Graphics2Dの状態を保存
            AffineTransform oldTransform = g2d.getTransform();
            // 車の中心を軸に回転
            g2d.rotate(Math.toRadians(carAngle), carX + carImage.getWidth() / 2.0, carY + carImage.getHeight() / 2.0);
            g2d.drawImage(carImage, (int) carX, (int) carY, null);
            // Graphics2Dの状態を元に戻す
            g2d.setTransform(oldTransform);
        }

        // --- 改良点：UI（速度メーター）の描画 ---
        drawUI(g2d);
    }
    
    private void drawUI(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.setColor(Color.WHITE);
        String speedText = String.format("Speed: %.1f", carSpeed * 10); // 表示上の速度
        g2d.drawString(speedText, 20, 30);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:    upPressed = true; break;
            case KeyEvent.VK_DOWN:  downPressed = true; break;
            case KeyEvent.VK_LEFT:  leftPressed = true; break;
            case KeyEvent.VK_RIGHT: rightPressed = true; break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:    upPressed = false; break;
            case KeyEvent.VK_DOWN:  downPressed = false; break;
            case KeyEvent.VK_LEFT:  leftPressed = false; break;
            case KeyEvent.VK_RIGHT: rightPressed = false; break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // 使用しない
    }
}
