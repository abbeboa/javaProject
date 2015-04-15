import javax.swing.*;
import java.awt.*;

public class TestGame extends JFrame {
    private GamePanel gamePanel;

    public TestGame() {
        super("TestGame");
        createGUI();
        pack();
        setResizable(false);
        setVisible(true);
    }

    private void createGUI() {
        Container c = getContentPane();
        gamePanel = new GamePanel();
        c.add(gamePanel, "Center");
    }

    public static void main(String[] args) {
        new TestGame();
    }
}
