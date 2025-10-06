import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.input.Key;

public class HelloWorld {
    public static void main(String[] args) {
        Terminal terminal = TerminalFacade.createTerminal();
        if (terminal == null) {
            System.err.println("Could not create terminal!");
            return;
        }

        try {
            terminal.enterPrivateMode();
            terminal.clearScreen();
            terminal.setCursorVisible(false);

            terminal.moveCursor(10, 5);
            String hello = "Hello";
            for (char c : hello.toCharArray()) {
                terminal.putCharacter(c);
            }
            terminal.flush();

            while (true) {
                Key key = terminal.readInput();

                // A key was pressed
                if (key != null) {
                    if (key.getKind() == Key.Kind.NormalKey && key.getCharacter() == 'q') {
                        break;
                    }
                }

                // Prevent high CPU usage while waiting for input
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        } finally {
            terminal.exitPrivateMode();
        }
    }
}