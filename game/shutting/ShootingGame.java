import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class ShootingGame extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private int playerX = 200, playerY = 350;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private boolean left, right, space;
    private Random rand = new Random();
    private int frameCount = 0;

    public ShootingGame() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.BLACK);
        timer = new Timer(16, this); // ~60 FPS
        timer.start();
        addKeyListener(this);
        setFocusable(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Player movement
        if (left) playerX -= 5;
        if (right) playerX += 5;
        if (space && frameCount % 10 == 0) { // limit fire rate
            bullets.add(new Bullet(playerX + 8, playerY));
        }

        // Move bullets
        bullets.forEach(Bullet::move);
        bullets.removeIf(b -> b.y < 0);

        // Spawn enemies every ~2 seconds
        if (frameCount % 120 == 0) {
            int ex = rand.nextInt(380);
            enemies.add(new Enemy(ex, 0));
        }

        // Move enemies
        enemies.forEach(Enemy::move);
        enemies.removeIf(en -> en.y > 400); // remove off-screen

        // Collision check: bullet vs enemy
        Iterator<Bullet> bulletIt = bullets.iterator();
        while (bulletIt.hasNext()) {
            Bullet b = bulletIt.next();
            Iterator<Enemy> enemyIt = enemies.iterator();
            while (enemyIt.hasNext()) {
                Enemy en = enemyIt.next();
                if (b.getBounds().intersects(en.getBounds())) {
                    bulletIt.remove();
                    enemyIt.remove();
                    break;
                }
            }
        }

        frameCount++;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Player
        g.setColor(Color.GREEN);
        g.fillRect(playerX, playerY, 20, 20);

        // Bullets
        g.setColor(Color.RED);
        for (Bullet b : bullets) {
            g.fillRect(b.x, b.y, 5, 10);
        }

        // Enemies
        g.setColor(Color.CYAN);
        for (Enemy en : enemies) {
            g.fillRect(en.x, en.y, 20, 20);
        }
    }

    // Key handling
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) left = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) right = true;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) space = true;
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) left = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) right = false;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) space = false;
    }
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame f = new JFrame("Shooting Game with Enemies");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new ShootingGame());
        f.pack();
        f.setVisible(true);
    }

    // Bullet class
    static class Bullet {
        int x, y;
        Bullet(int x, int y) { this.x = x; this.y = y; }
        void move() { y -= 10; }
        Rectangle getBounds() { return new Rectangle(x, y, 5, 10); }
    }

    // Enemy class
    static class Enemy {
        int x, y;
        Enemy(int x, int y) { this.x = x; this.y = y; }
        void move() { y += 2; }
        Rectangle getBounds() { return new Rectangle(x, y, 20, 20); }
    }
}
