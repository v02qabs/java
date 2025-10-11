import javax.swing.JFrame;

public class Game {
    public static void main(String[] args) {
        // ウィンドウを作成
        JFrame frame = new JFrame("2D レースゲーム");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ウィンドウを閉じたらプログラムも終了
        frame.setResizable(false); // ウィンドウサイズを固定

        // ゲーム画面（描画エリア）を追加
        GamePanel panel = new GamePanel();
        frame.add(panel);

        frame.pack(); // パネルのサイズに合わせてウィンドウサイズを自動調整
        frame.setLocationRelativeTo(null); // ウィンドウを画面中央に表示
        frame.setVisible(true); // ウィンドウを表示

        // ゲームを開始
        panel.startGame();
    }
}
