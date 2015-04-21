import java.awt.*;
import java.io.File;

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
        Font fnt0 = new Font("arial", Font.BOLD, 50);
        g.setFont(fnt0);
        g.setColor(Color.WHITE);
        g.drawString("Space Duck Shooter", GamePanel.JPWIDTH / 3, 100);
        Font fnt1 = new Font("arial", Font.BOLD, 30);
        g.setFont(fnt1);
        if (s1 > 0) {
            g.drawString("Player1 score: " + s1, 50, buttonTop + 300);
        }
        if (s2 > 0) {
            g.drawString("Player2 score: " + s2, 50, buttonTop + 400);
        }
        g.setFont(fnt0);
       if (GamePanel.isResumeGame()) {
            g.drawString("Resume", buttonLeft, buttonTop);
       }
        g.drawString("New Game", buttonLeft, buttonTop  + 100);
        g.drawString("Quit Game", buttonLeft, buttonTop + 200);
    }
}
