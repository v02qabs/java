import java.awt.*;


public class Main extends Frame
{


    public static void main(String [] args) {
        new Main();
    }
    Main() {
        super("ButtonTest");
        setSize(200, 100);
        setLayout(new FlowLayout());
        Button b1 = new Button("OK");
        add(b1);
        setVisible(true);
    }
}
