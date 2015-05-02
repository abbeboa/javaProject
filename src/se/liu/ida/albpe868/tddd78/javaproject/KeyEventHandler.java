package se.liu.ida.albpe868.tddd78.javaproject;

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
    private int shootingDelayCounterPlayer2 = 0;

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
        if (shootingDelayCounterPlayer2 >= 0) {
            shootingDelayCounterPlayer2 -= 1;
        }
    }

    private void checkPlayerOneKeyCodes(int keyCode, AbstractGameObject player1) {
        if (keyCode == KeyEvent.VK_LEFT) {
            if (GamePanel.getGamefield().contains(player1.getRectangle())) {
                player1.move(Direction.LEFT, player1.getSpeed());
            }
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            if (GamePanel.getGamefield().contains(player1.getRectangle())) {
                player1.move(Direction.RIGHT, player1.getSpeed());
            }
        }
        if (keyCode == KeyEvent.VK_UP) {
            if (GamePanel.getGamefield().contains(player1.getRectangle())) {
                player1.move(Direction.UP, player1.getSpeed());
            }
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            if (GamePanel.getGamefield().contains(player1.getRectangle())) {
                player1.move(Direction.DOWN, player1.getSpeed());
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
            if (GamePanel.getGamefield().contains(player2.getRectangle())) {
                player2.move(Direction.LEFT, player2.getSpeed());
            }
        }
        if (keyCode == KeyEvent.VK_D) {
            if (GamePanel.getGamefield().contains(player2.getRectangle())) {
                player2.move(Direction.RIGHT, player2.getSpeed());
            }
        }
        if (keyCode == KeyEvent.VK_W) {
            if (GamePanel.getGamefield().contains(player2.getRectangle())) {
                player2.move(Direction.UP, player2.getSpeed());
            }
        }
        if (keyCode == KeyEvent.VK_S) {
            if (GamePanel.getGamefield().contains(player2.getRectangle())) {
                player2.move(Direction.DOWN, player2.getSpeed());
            }
        }
        if (keyCode == KeyEvent.VK_CONTROL) {
            if (shootingDelayCounterPlayer2 <= 0) {
                Sound.playSoundBlaster(gamePanel.isSoundEnabled());
                player2.shoot(Type.BULLET, Direction.UP, gamePanel);
                int currentShootingDelay = player2.getShootingDelay();
                shootingDelayCounterPlayer2 += currentShootingDelay;
            }
        }
    }
}
