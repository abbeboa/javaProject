import java.awt.*;

/**
 * This is where the menu is created
 */
public class Menu {
    public static int buttonTop = 270;
    public static int buttonBottom = 220;
    public static int buttonLeft = 768;
    public static int buttonRight = 1024;

    public void render(Graphics2D g) {
        g.drawImage(GamePanel.imgMenuBackground, 0, 0, null);
        Font fnt0 = new Font("arial", Font.BOLD, 50);
        g.setFont(fnt0);
        g.setColor(Color.WHITE);
        g.drawString("Space Duck Shooter", GamePanel.JPWIDTH / 3, 100);
        g.setColor(Color.WHITE);
       if (GamePanel.isResumeGame()) {
            g.drawString("Resume", buttonLeft, buttonTop);
       }
        g.drawString("New Game", buttonLeft, buttonTop  + 100);
        g.drawString("Quit Game", buttonLeft, buttonTop + 200);


    }
}
