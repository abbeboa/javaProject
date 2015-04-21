import javax.swing.*;
import java.awt.*;

public class TestGame extends JFrame {

    public TestGame() {
        super("TestGame");
        createGUI();
        pack();
        setResizable(false);
        setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void createGUI() {
        Container container = getContentPane();
        GamePanel gamePanel = new GamePanel();
        container.add(gamePanel, "Center");
    }

    public static void main(String[] args) {
        new TestGame();
    }
}
