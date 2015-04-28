import java.awt.*;

/**
 * This is where the menu is created
 */
public class Menu {
    private static int buttonTop = 270;
    private static int buttonBottom = 220;
    private static int buttonLeft = 768;
    private static int buttonRight = 1024;

    public void render(Graphics2D g, int s1, int s2) {
        g.drawImage(GamePanel.getImgMenuBackground(), 0, 0, null);
        Font headline = GamePanel.getHeadline();
        g.setFont(headline);
        g.setColor(Color.WHITE);
        g.drawString("[Space Shooter]", GamePanel.JPWIDTH / 4, 70);
        Font text = GamePanel.getText();
        g.setFont(text);
        if (s1 > 0) {
            g.drawString("Player1 score " + s1, 50, buttonTop + 300);
        }

        if (s2 > 0) {
            g.drawString("Player2 score " + s2, 50, buttonTop + 400);
        }

        if (GamePanel.isResumeGame()) {
            g.drawString("Resume", buttonLeft, buttonTop -100);
        }

        g.drawString("New Game", buttonLeft, buttonTop);
        g.drawString("Players: "+GamePanel.getPlayerCount(), buttonLeft, buttonTop + 100);
        g.drawString("Quit Game", buttonLeft, buttonTop + 200);

        if (!GamePanel.isSoundEnabled()) {
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(3));
            g.drawLine(1240, 690, 1200, 650);
        }
    }

    public static int getButtonTop() {return buttonTop;}

    public static int getButtonBottom() {
        return buttonBottom;
    }

    public static int getButtonLeft() {
        return buttonLeft;
    }

    public static int getButtonRight() {
        return buttonRight;
    }
}