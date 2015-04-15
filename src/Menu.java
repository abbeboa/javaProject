import java.awt.*;

public class Menu {
    public static int height = 230;
    public static int width = GamePanel.JPWIDTH;

    public void render(Graphics2D g){
        g.drawImage(GamePanel.imgMenuBackground, 0, 0, null);
        Font fnt0 = new Font("arial", Font.BOLD, 50);
        g.setFont(fnt0);
        g.setColor(Color.WHITE);
        g.drawString("Space Duck Shooter", width / 3, 100);
        g.setColor(Color.WHITE);
        if (GamePanel.isResumeGame()) {g.drawString("Resume", (width / 2 + width / 10), (height + width / 20 - 20));}
        g.drawString("New Game", (width/2+width/10), (height+width/20+80));
        g.drawString("Quit Game", (width/2+width/10), (height+width/20+180));


    }
}
