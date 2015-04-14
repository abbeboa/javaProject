import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;

public class Menu {

    public void render(Graphics g){
        Font fnt0 = new Font("arial", Font.BOLD, 50);
        g.setFont(fnt0);
        g.setColor(Color.WHITE);
        g.drawString("Space Duck", GamePanel.getJpwidth()/2, GamePanel.getJpheight()/3);
    }
}
