import java.awt.*;

public class Menu {
    int buttonY = 230;
    int width = GamePanel.JPWIDTH;

    //public Rectangle newGamebutton    = new Rectangle(width/2 + width/10, buttonY,     width/5, width/20);
    //public Rectangle resumeGameButton = new Rectangle(width/2 + width/10, buttonY+100, width/5, width/20);
    //public Rectangle quitButton       = new Rectangle(width/2 + width/10, buttonY+200, width/5, width/20);

    public void render(Graphics2D g){
        g.drawImage(GamePanel.imgMenuBackground, 0, 0, null);
        Font fnt0 = new Font("arial", Font.BOLD, 50);
        g.setFont(fnt0);
        g.setColor(Color.WHITE);
        g.drawString("Space Duck invader", width / 3, 100);
        //g.setColor(Color.black);
        //g.draw(newGamebutton);
        //g.draw(resumeGameButton);
        //g.draw(quitButton);
        g.setColor(Color.WHITE);
        g.drawString("Resume", (width/2+width/10), (buttonY+width/20-20));
        g.drawString("New Game", (width/2+width/10), (buttonY+width/20+80));
        g.drawString("Quit Game", (width/2+width/10), (buttonY+width/20+180));


    }
}
