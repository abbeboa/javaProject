import java.awt.*;

/**
 * This is where the menu is created
 */
public class Menu {
    private static final int RESUMEBUTTONLEFT = 768;
    private static final int RESUMEBUTTONTOP = 170;

    private static final int NEWBUTTONLEFT = 768;
    private static final int NEWBUTTOTOP = 270;

    private static final int PLAYERBUTTONLEFT = 768;
    private static final int PLAYERBUTTONTOP = 370;

    private static final int QUITBUTTONLEFT = 768;
    private static final int QUITBUTTONTOP = 470;

    private static final int SOUNDBUTTONLEFT = 1200;
    private static final int SOUNDBUTTONRIGHT = 1240;
    private static final int SOUNDBUTTONTOP = 650;
    private static final int SOUNDBUTTONBOTTOM = 690;

    private static final int PLAYER1SCOREPOSX = 50;
    private static final int PLAYER1SCOREPOSY = 570;
    private static final int PLAYER2SCOREPOSX = 50;
    private static final int PLAYER2SCOREPOSY = 670;

    private static final int HEADLINEYPOS = 70;

    private GamePanel gamePanel;

    public Menu(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void render(Graphics2D g, int s1, int s2) {
        g.drawImage(GamePanel.getImgMenuBackground(), 0, 0, null);
        Font headline = gamePanel.getHeadline();
        g.setFont(headline);
        g.setColor(Color.WHITE);
        g.drawString("[Space Shooter]", GamePanel.JPWIDTH / 4, HEADLINEYPOS);
        Font text = gamePanel.getMenuText();
        g.setFont(text);
        if (s1 > 0) {
            g.drawString("Player1 score: " + s1, PLAYER1SCOREPOSX, PLAYER1SCOREPOSY);
        }

        if (s2 > 0) {
            g.drawString("Player2 score: " + s2, PLAYER2SCOREPOSX, PLAYER2SCOREPOSY);
        }

        if (gamePanel.isResumeGame()) {
            g.drawString("Resume", RESUMEBUTTONLEFT, RESUMEBUTTONTOP);
        }

        g.drawString("New Game", NEWBUTTONLEFT, NEWBUTTOTOP);
        g.drawString("Players: " + gamePanel.getPlayerCount(), PLAYERBUTTONLEFT, PLAYERBUTTONTOP);
        g.drawString("Quit Game", QUITBUTTONLEFT, QUITBUTTONTOP);

        if (!gamePanel.isSoundEnabled()) {
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(3));
            g.drawLine(SOUNDBUTTONRIGHT, SOUNDBUTTONBOTTOM, SOUNDBUTTONLEFT, SOUNDBUTTONTOP);
        }
    }

    public static int getResumebuttonleft() {
        return RESUMEBUTTONLEFT;
    }

    public static int getResumebuttontop() {
        return RESUMEBUTTONTOP;
    }

    public static int getNewbuttonleft() {
        return NEWBUTTONLEFT;
    }

    public static int getNewbuttotop() {
        return NEWBUTTOTOP;
    }

    public static int getPlayerbuttonleft() {
        return PLAYERBUTTONLEFT;
    }

    public static int getPlayerbuttontop() {
        return PLAYERBUTTONTOP;
    }

    public static int getQuitbuttonleft() {
        return QUITBUTTONLEFT;
    }

    public static int getQuitbuttontop() {
        return QUITBUTTONTOP;
    }

    public static int getSoundbuttonleft() {
        return SOUNDBUTTONLEFT;
    }

    public static int getSoundbuttonright() {
        return SOUNDBUTTONRIGHT;
    }

    public static int getSoundbuttontop() {
        return SOUNDBUTTONTOP;
    }

    public static int getSoundbuttonbottom() {
        return SOUNDBUTTONBOTTOM;
    }
}