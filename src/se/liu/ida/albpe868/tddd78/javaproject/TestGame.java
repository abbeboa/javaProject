package se.liu.ida.albpe868.tddd78.javaproject;

import javax.swing.*;
import java.awt.*;

/**
 * This class runs the game and creates the GUI.
 */
public class TestGame extends JFrame {

    public TestGame() {
        super("Space Shooter");
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
        new TestGame(); // Gives probable bugs inspection error, though this is correct
    }
}
