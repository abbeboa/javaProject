import java.util.ArrayList;
import java.util.Collection;
import java.awt.event.*;
import java.util.List;

/**
 * This class handles keyevents, in other words input from players. Used in GamePanel class.
 */
public class KeyEventHandler {
    private int shootingDelayCounter = 0;
    private GamePanel gamePanel;
    private int shottingDelayCounter2 = 0;

    public KeyEventHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void handleKeyEvents(List<Integer> pressedKeys) {
        AbstractGameObject player1 = GamePanel.getGameObject(0);
        Collection<Integer> keysToHandle = new ArrayList<>();
        for (int i = 0; i < pressedKeys.size(); i++) {
            keysToHandle.add(pressedKeys.get(i));
        }
        for (int keyCode : keysToHandle) {
            if (keyCode == KeyEvent.VK_ESCAPE) {
                gamePanel.setGameRunning(false);
            }
            if (GamePanel.checkStateEqualsGame()) {
                if (keyCode == KeyEvent.VK_LEFT) {
                    if (GamePanel.GAMEFIELD.contains(player1.getRectangle())) {
                        player1.move(Direction.LEFT, player1.getSpeed());
                    }
                }
                if (keyCode == KeyEvent.VK_RIGHT) {
                    if (GamePanel.GAMEFIELD.contains(player1.getRectangle())) {
                        player1.move(Direction.RIGHT, player1.getSpeed());
                    }
                }
                if (keyCode == KeyEvent.VK_UP) {
                    if (GamePanel.GAMEFIELD.contains(player1.getRectangle())) {
                        player1.move(Direction.UP, player1.getSpeed());
                    }
                }
                if (keyCode == KeyEvent.VK_DOWN) {
                    if (GamePanel.GAMEFIELD.contains(player1.getRectangle())) {
                        player1.move(Direction.DOWN, player1.getSpeed());
                    }
                }
                if (keyCode == KeyEvent.VK_SPACE) {
                    if (shootingDelayCounter <= 0) {
                        Sound.playSoundBlaster();
                        player1.shoot(Type.BULLET, Direction.UP);
                        int currentShootingDelay = player1.getShootingDelay();
                        shootingDelayCounter += currentShootingDelay;
                    }
                }
		if (GamePanel.getPlayerCount() > 1) {
		AbstractGameObject player2 = GamePanel.getGameObject(1);

		if (keyCode == KeyEvent.VK_A) {
		    if (GamePanel.GAMEFIELD.contains(player2.getRectangle())) {
			player2.move(Direction.LEFT, player2.getSpeed());
		    }
		}
		if (keyCode == KeyEvent.VK_D) {
		    if (GamePanel.GAMEFIELD.contains(player2.getRectangle())) {
			player2.move(Direction.RIGHT, player2.getSpeed());
		    }
		}
		if (keyCode == KeyEvent.VK_W) {
		    if (GamePanel.GAMEFIELD.contains(player2.getRectangle())) {
			player2.move(Direction.UP, player2.getSpeed());
		    }
		}
		if (keyCode == KeyEvent.VK_S) {
		    if (GamePanel.GAMEFIELD.contains(player2.getRectangle())) {
			player2.move(Direction.DOWN, player2.getSpeed());
		    }
		}
		if (keyCode == KeyEvent.VK_CONTROL) {
		    if (shootingDelayCounter <= 0) {
			GamePanel.playSoundBlaster();
			player2.shoot(Type.BULLET, Direction.UP);
			int currentShootingDelay2 = player2.getShootingDelay();
			shootingDelayCounter += currentShootingDelay2;
		    }
		}
	    }
                if (keyCode == KeyEvent.VK_P) {
                    GamePanel.setStateMenu();
                }
            } else if (GamePanel.checkStateEqualsMenu()) {
                if (keyCode == KeyEvent.VK_P) {
                    GamePanel.setStateGame();
                }
            }
        }
        if (shootingDelayCounter >= 0) {
            shootingDelayCounter -= 1;
        }
    }
}
