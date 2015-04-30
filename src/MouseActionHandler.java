/**
 * Handles mousepresses in the menu and performs actions accordingly
 */
public class MouseActionHandler {
    private GamePanel gamePanel;
    private static final int RESUMEBUTTONRIGHT = 1024;
    private static final int RESUMEBUTTONBOTTOM = 120;
    private static final int NEWBUTTONRIGHT = 1024;
    private static final int NEWBUTTONBOTTOM = 220;
    private static final int PLAYERBUTTONRIGHT = 1024;
    private static final int PLAYERBUTTONBOTTOM = 320;
    private static final int QUITBUTTONRIGHT = 1024;
    private static final int QUITBUTTONBOTTOM = 420;

    public MouseActionHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void mouseAction(int x, int y) {
        // x and y used to check where user has clicked
        if (!gamePanel.isGameOver()) {
            // actions only performed when game is not over
            if (gamePanel.checkStateEqualsMenu()) {
                if (x > Menu.getResumebuttonleft() && x < RESUMEBUTTONRIGHT && y > RESUMEBUTTONBOTTOM &&
                        y < Menu.getResumebuttontop()) { // resume button
                    if (gamePanel.isResumeGame()) {
                        gamePanel.setStateGame();
                    }
                } else if (x > Menu.getNewbuttonleft() && x < NEWBUTTONRIGHT && y > NEWBUTTONBOTTOM &&
                        y < Menu.getNewbuttontop()) { // new game button
                    gamePanel.resetGame();
                    gamePanel.setNewGame(true);
                    gamePanel.setStateGame();
                } else if (x > Menu.getPlayerbuttonleft() && x < PLAYERBUTTONRIGHT && y > PLAYERBUTTONBOTTOM &&
                        y < Menu.getPlayerbuttontop()) { // Player count button
                    if (gamePanel.getPlayerCount() == 1) {
                        gamePanel.setPlayerCount(2);
                    } else {
                        gamePanel.setPlayerCount(1);
                    }
                } else if (x > Menu.getQuitbuttonleft() && x < QUITBUTTONRIGHT && y > QUITBUTTONBOTTOM &&
                        y < Menu.getQuitbuttontop()) { // quit game button

                    gamePanel.stopGame();
                } else if ((x > Menu.getSoundbuttonleft()) && (x < Menu.getSoundbuttonright()) &&
                        (y > Menu.getSoundbuttontop()) && (y < Menu.getSoundbuttonbottom())) {
                    if (gamePanel.isSoundEnabled()) {
                        gamePanel.setSoundEnabled(false);
                    } else {
                        gamePanel.setSoundEnabled(true);
                    }
                }
            }
        }
    }

}
