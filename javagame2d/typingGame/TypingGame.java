import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.List;
import java.util.*;
import javax.swing.Timer;
import java.util.concurrent.ThreadLocalRandom;

public class TypingGame extends JFrame {
    // --- UI ---
    private JLabel lblTarget;
    private JTextField txtInput;
    private JButton btnStartStop;
    private JComboBox<String> cmbDuration;
    private JComboBox<String> cmbDifficulty;
    private JCheckBox chkEndless;
    private JProgressBar timeBar;

    private JLabel lblScore;
    private JLabel lblWpm;
    private JLabel lblAccuracy;
    private JLabel lblMistakes;
    private JLabel lblTimeLeft;
    private JLabel lblBestScore;

    // --- 状態 ---
    private boolean running = false;
    private String currentWord = "";
    private int score = 0;
    private int typedChars = 0;
    private int correctChars = 0;
    private int mistakes = 0;
    private int timeLimitSec = 60; // デフォルト60秒
    private int timeLeftSec = 60;
    private long startNano = 0L;
    private javax.swing.Timer swingTimer; // 1秒タイマー

    private double bestScore = 0.0; // ベスト WPM を保存
    private static final Path BEST_FILE = Path.of("typing_highscore.txt");
    private final DecimalFormat df1 = new DecimalFormat("0.0");

    // 語彙
    private final List<String> easyWords = Arrays.asList(
            "cat","dog","book","blue","green","tree","bird","milk","water","apple",
            "house","happy","music","sun","star","game","light","mouse","phone","paper"
    );
    private final List<String> normalWords = Arrays.asList(
            "orange","window","school","family","summer","winter","travel","answer","simple","coffee",
            "letter","minute","future","planet","flower","silver","little","bridge","engine","guitar",
            "camera","potato","butter","castle","rocket","forest","matter","thirty","laptop","planet"
    );
    private final List<String> hardWords = Arrays.asList(
            "architecture","responsibility","sophisticated","extraordinary","consequence",
            "psychology","miscellaneous","characteristic","acknowledgment","infrastructure",
            "approximate","communication","opportunity","circumstance","congratulations",
            "identification","recommendation","unbelievable","neighborhood","multiplication"
    );

    public TypingGame() {
        super("Typing Game (Java 17 + Swing)");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(760, 520);
        setLocationRelativeTo(null);

        // ルック&フィール（任意）
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        // ---- 上部：コントロール ----
        JPanel top = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4,4,4,4);
        gc.gridy = 0;

        cmbDuration = new JComboBox<>(new String[]{"60 sec","120 sec","180 sec"});
        cmbDifficulty = new JComboBox<>(new String[]{"Easy","Normal","Hard"});
        chkEndless = new JCheckBox("Endless");
        btnStartStop = new JButton("Start");

        gc.gridx = 0; top.add(new JLabel("Duration:"), gc);
        gc.gridx = 1; top.add(cmbDuration, gc);
        gc.gridx = 2; top.add(new JLabel("Difficulty:"), gc);
        gc.gridx = 3; top.add(cmbDifficulty, gc);
        gc.gridx = 4; top.add(chkEndless, gc);
        gc.gridx = 5; top.add(btnStartStop, gc);

        timeBar = new JProgressBar(0, 100);
        timeBar.setStringPainted(true);
        timeBar.setPreferredSize(new Dimension(220, 20));
        gc.gridx = 6; top.add(timeBar, gc);

        root.add(top, BorderLayout.NORTH);

        // ---- 中央：ターゲット単語＋入力 ----
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        lblTarget = new JLabel(htmlWord("", ""));
        lblTarget.setFont(lblTarget.getFont().deriveFont(Font.BOLD, 36f));
        lblTarget.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtInput = new JTextField();
        txtInput.setFont(txtInput.getFont().deriveFont(Font.PLAIN, 24f));
        txtInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        center.add(Box.createVerticalStrut(20));
        center.add(lblTarget);
        center.add(Box.createVerticalStrut(16));
        center.add(txtInput);
        center.add(Box.createVerticalGlue());

        root.add(center, BorderLayout.CENTER);

        // ---- 下部：ステータス ----
        JPanel bottom = new JPanel(new GridLayout(2, 3, 8, 8));
        lblScore = new JLabel("Score: 0");
        lblWpm = new JLabel("WPM: 0.0");
        lblAccuracy = new JLabel("Accuracy: 100.0%");
        lblMistakes = new JLabel("Mistakes: 0");
        lblTimeLeft = new JLabel("Time: 60s");
        lblBestScore = new JLabel("Best WPM: loading...");

        bottom.add(lblScore);
        bottom.add(lblWpm);
        bottom.add(lblAccuracy);
        bottom.add(lblMistakes);
        bottom.add(lblTimeLeft);
        bottom.add(lblBestScore);

        root.add(bottom, BorderLayout.SOUTH);

        // ---- イベント ----
        btnStartStop.addActionListener(e -> {
            if (!running) startGame(); else stopGame(false);
        });

        cmbDuration.addActionListener(e -> {
            if (!running && !chkEndless.isSelected()) {
                timeLimitSec = selectionToSeconds((String) cmbDuration.getSelectedItem());
                timeLeftSec = timeLimitSec;
                updateTimeUI();
            }
        });

        chkEndless.addActionListener(e -> {
            boolean endless = chkEndless.isSelected();
            cmbDuration.setEnabled(!endless);
            if (!running) {
                if (endless) {
                    timeLimitSec = Integer.MAX_VALUE / 4; // 実質無制限
                    timeLeftSec = timeLimitSec;
                } else {
                    timeLimitSec = selectionToSeconds((String) cmbDuration.getSelectedItem());
                    timeLeftSec = timeLimitSec;
                }
                updateTimeUI();
            }
        });

        txtInput.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                if (!running) return;
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    checkAndNext();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    stopGame(false);
                } else {
                    // リアルタイム色分け
                    lblTarget.setText(colorize(currentWord, txtInput.getText()));
                    // 統計（入力中のタイプは typedChars に含めない。Enterで集計）
                }
            }
        });

        // タイマー
        swingTimer = new javax.swing.Timer(1000, e -> onTick());

        // ベストスコアの読み込み
        loadBest();

        // 初期状態
        timeLimitSec = selectionToSeconds((String) cmbDuration.getSelectedItem());
        timeLeftSec = timeLimitSec;
        updateTimeUI();
        setWord(randomWord());
        setRunning(false);
    }

    private static String htmlWord(String pre, String post) {
        // 適当な空表示
        return "<html><span style='color:#888'>" + escapeHtml(pre) + "</span>" +
               "<span>" + escapeHtml(post) + "</span></html>";
    }

    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<","&lt;").replace(">","&gt;");
    }

    private String colorize(String target, String input) {
        StringBuilder sb = new StringBuilder("<html>");
        int n = Math.min(target.length(), input.length());
        for (int i = 0; i < n; i++) {
            char tc = target.charAt(i);
            char ic = input.charAt(i);
            if (tc == ic) {
                sb.append("<span style='color:#2e7d32;'>").append(escapeHtml(String.valueOf(tc))).append("</span>");
            } else {
                sb.append("<span style='color:#c62828;'>").append(escapeHtml(String.valueOf(tc))).append("</span>");
            }
        }
        if (target.length() > n) {
            sb.append("<span>").append(escapeHtml(target.substring(n))).append("</span>");
        }
        sb.append("</html>");
        return sb.toString();
    }

    private void startGame() {
        resetStats();
        setRunning(true);
        startNano = System.nanoTime();

        if (chkEndless.isSelected()) {
            timeLimitSec = Integer.MAX_VALUE / 4;
        } else {
            timeLimitSec = selectionToSeconds((String) cmbDuration.getSelectedItem());
        }
        timeLeftSec = timeLimitSec;
        updateTimeUI();
        setWord(randomWord());
        txtInput.requestFocusInWindow();

        swingTimer.start();
    }

    private void stopGame(boolean timeUp) {
        if (!running) return;
        swingTimer.stop();
        setRunning(false);
        double wpm = calcWpm();
        lblWpm.setText("WPM: " + df1.format(wpm));
        maybeSaveBest(wpm);

        String title = timeUp ? "Time Up!" : "Stopped";
        JOptionPane.showMessageDialog(
                this,
                "Score: " + score + "\nWPM: " + df1.format(wpm) +
                        "\nAccuracy: " + df1.format(calcAccuracy()) + "%" +
                        "\nMistakes: " + mistakes,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void setRunning(boolean run) {
        running = run;
        btnStartStop.setText(run ? "Stop" : "Start");
        cmbDuration.setEnabled(!run && !chkEndless.isSelected());
        cmbDifficulty.setEnabled(!run);
        chkEndless.setEnabled(!run);
        txtInput.setEnabled(run);
        if (!run) {
            // 入力フィールド残さない
            txtInput.setText("");
            lblTarget.setText(htmlWord("", ""));
        }
    }

    private void resetStats() {
        score = 0;
        typedChars = 0;
        correctChars = 0;
        mistakes = 0;
        lblScore.setText("Score: 0");
        lblWpm.setText("WPM: 0.0");
        lblAccuracy.setText("Accuracy: 100.0%");
        lblMistakes.setText("Mistakes: 0");
    }

    private void onTick() {
        if (!running) return;
        if (!chkEndless.isSelected()) {
            timeLeftSec--;
            updateTimeUI();
            if (timeLeftSec <= 0) {
                stopGame(true);
            }
        } else {
            // 無制限：WPMだけ更新
            lblWpm.setText("WPM: " + df1.format(calcWpm()));
            lblAccuracy.setText("Accuracy: " + df1.format(calcAccuracy()) + "%");
        }
    }

    private void updateTimeUI() {
        if (timeLimitSec <= 0) {
            timeBar.setValue(0);
            timeBar.setString("0%");
            lblTimeLeft.setText("Time: 0s");
            return;
        }
        int percent = (int)Math.max(0, Math.round((timeLeftSec * 100.0) / timeLimitSec));
        timeBar.setValue(percent);
        timeBar.setString(percent + "%");
        lblTimeLeft.setText("Time: " + Math.max(0, timeLeftSec) + "s");
    }

    private void setWord(String w) {
        currentWord = w;
        lblTarget.setText(colorize(currentWord, txtInput.getText()));
    }

    private int selectionToSeconds(String sel) {
        if (sel == null) return 60;
        if (sel.startsWith("120")) return 120;
        if (sel.startsWith("180")) return 180;
        return 60;
    }

    private List<String> getWordList() {
        String diff = (String) cmbDifficulty.getSelectedItem();
        if ("Hard".equals(diff)) return hardWords;
        if ("Normal".equals(diff)) return normalWords;
        return easyWords;
    }

    private String randomWord() {
        List<String> list = getWordList();
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    private void checkAndNext() {
        String input = txtInput.getText();
        // 統計更新（Enter時に加算）
        typedChars += input.length();

        // 正誤判定（完全一致のみ加点）
        if (input.equals(currentWord)) {
            score++;
            correctChars += input.length(); // 全文字正解
            lblScore.setText("Score: " + score);
        } else {
            // 部分一致で正しく打てた文字数を計算（参考用）
            int n = Math.min(currentWord.length(), input.length());
            for (int i = 0; i < n; i++) {
                if (input.charAt(i) == currentWord.charAt(i)) correctChars++;
                else mistakes++;
            }
            // 余り分はミス扱い（打ち過ぎ／足りない）
            mistakes += Math.abs(currentWord.length() - input.length());
            lblMistakes.setText("Mistakes: " + mistakes);
        }

        // WPM, Accuracy 更新
        lblWpm.setText("WPM: " + df1.format(calcWpm()));
        lblAccuracy.setText("Accuracy: " + df1.format(calcAccuracy()) + "%");

        // 次へ
        txtInput.setText("");
        setWord(randomWord());
        txtInput.requestFocusInWindow();
    }

    private double calcWpm() {
        double minutes = Math.max( (System.nanoTime() - startNano) / 1_000_000_000.0 / 60.0, 1e-6 );
        // 一般的なWPM: 正しく打てた文字数/5 / 分
        return (correctChars / 5.0) / minutes;
    }

    private double calcAccuracy() {
        if (typedChars + mistakes == 0) return 100.0;
        // 正確度 = 正しい文字 / (タイプ文字 + ミス文字)
        double denom = Math.max(1, (double)(typedChars + mistakes));
        return (correctChars * 100.0) / denom;
    }

    private void loadBest() {
        try {
            if (Files.exists(BEST_FILE)) {
                String s = Files.readString(BEST_FILE, StandardCharsets.UTF_8).trim();
                bestScore = Double.parseDouble(s);
            }
        } catch (Exception ignored) {}
        lblBestScore.setText("Best WPM: " + df1.format(bestScore));
    }

    private void maybeSaveBest(double wpm) {
        if (wpm > bestScore) {
            bestScore = wpm;
            lblBestScore.setText("Best WPM: " + df1.format(bestScore));
            try (BufferedWriter bw = Files.newBufferedWriter(BEST_FILE, StandardCharsets.UTF_8)) {
                bw.write(df1.format(bestScore));
            } catch (IOException ignored) {}
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TypingGame().setVisible(true));
    }
}
