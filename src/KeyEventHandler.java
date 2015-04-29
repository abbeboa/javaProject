import java.util.ArrayList;
import java.util.Collection;
import java.awt.event.*;
import java.util.List;

/**
 * This class handles keyevents, in other words input from players. Used in GamePanel class.
 */
public class KeyEventHandler {
    private GamePanel gamePanel;
    private int shootingDelayCounter = 0;

    public KeyEventHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void handleKeyEvents(List<Integer> pressedKeys) {
        AbstractGameObject player1 = gamePanel.getGameObject(0);
        Collection<Integer> keysToHandle = new ArrayList<>();
        for (int i = 0; i < pressedKeys.size(); i++) {
            keysToHandle.add(pressedKeys.get(i));
        }
        for (int keyCode : keysToHandle) {
            if (keyCode == KeyEvent.VK_ESCAPE) {
                gamePanel.setGameRunning(false);
            }
            if (keyCode == KeyEvent.VK_P) {
                gamePanel.setStateMenu();
            }
            if (gamePanel.checkStateEqualsGame()) {
                checkPlayerOneKeyCodes(keyCode, player1);
                if (gamePanel.getPlayerCount() > 1) {
                    checkPlayerTwoKeyCodes(keyCode);
                } else if (gamePanel.checkStateEqualsMenu()) {
                    if (keyCode == KeyEvent.VK_P) {
                        gamePanel.setStateGame();
                    }
                }
            }
        }
        if (shootingDelayCounter >= 0) {
            shootingDelayCounter -= 1;
        }
    }

    private void checkPlayerOneKeyCodes(int keyCode, AbstractGameObject player1) {
        if (keyCode == KeyEvent.VK_LEFT) {
            if (gamePanel.getGamefield().contains(player1.getRectangle())) {
                player1.move(Direction.LEFT, player1.getSpeed(), gamePanel);
            }
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            if (GamePanel.getGamefield().contains(player1.getRectangle())) {
                player1.move(Direction.RIGHT, player1.getSpeed(), gamePanel);
            }
        }
        if (keyCode == KeyEvent.VK_UP) {
            if (GamePanel.getGamefield().contains(player1.getRectangle())) {
                player1.move(Direction.UP, player1.getSpeed(), gamePanel);
            }
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            if (GamePanel.getGamefield().contains(player1.getRectangle())) {
                player1.move(Direction.DOWN, player1.getSpeed(), gamePanel);
            }
        }
        if (keyCode == KeyEvent.VK_SPACE) {
            if (shootingDelayCounter <= 0) {
                Sound.playSoundBlaster(gamePanel.isSoundEnabled());
                player1.shoot(Type.BULLET, Direction.UP, gamePanel);
                int currentShootingDelay = player1.getShootingDelay();
                shootingDelayCounter += currentShootingDelay;
            }
        }
    }

    private void checkPlayerTwoKeyCodes(int keyCode) {
        AbstractGameObject player2 = gamePanel.getGameObject(1);
        if (keyCode == KeyEvent.VK_A) {
            if (gamePanel.getGamefield().contains(player2.getRectangle())) {
                player2.move(Direction.LEFT, player2.getSpeed(), gamePanel);
            }
        }
        if (keyCode == KeyEvent.VK_D) {
            if (gamePanel.getGamefield().contains(player2.getRectangle())) {
                player2.move(Direction.RIGHT, player2.getSpeed(), gamePanel);
            }
        }
        if (keyCode == KeyEvent.VK_W) {
            if (gamePanel.getGamefield().contains(player2.getRectangle())) {
                player2.move(Direction.UP, player2.getSpeed(), gamePanel);
            }
        }
        if (keyCode == KeyEvent.VK_S) {
            if (gamePanel.getGamefield().contains(player2.getRectangle())) {
                player2.move(Direction.DOWN, player2.getSpeed(), gamePanel);
            }
        }
        if (keyCode == KeyEvent.VK_CONTROL) {
            if (shootingDelayCounter <= 0) {
                Sound.playSoundBlaster(gamePanel.isSoundEnabled());
                player2.shoot(Type.BULLET, Direction.UP, gamePanel);
                int currentShootingDelay2 = player2.getShootingDelay();
                shootingDelayCounter += currentShootingDelay2;
            }
        }
    }
}