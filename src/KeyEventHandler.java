import java.util.ArrayList;
import java.util.Collection;
import java.awt.event.*;
import java.util.List;

/**
 * Created by Christian on 2015-04-26.
 */
public class KeyEventHandler {
    private int shootingDelayCounter = 0;

    public void handleKeyEvents(List<Integer> pressedKeys) {
        AbstractGameObject player1 = GamePanel.getGameObject(0);
        Collection<Integer> keysToHandle = new ArrayList<>();
        for (int i = 0; i < pressedKeys.size(); i++) {
            keysToHandle.add(pressedKeys.get(i));
        }
        for (int keyCode : keysToHandle) {
            if (keyCode == KeyEvent.VK_ESCAPE) {
                GamePanel.setGameRunning(false);
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
                        GamePanel.playSoundBlaster();
                        player1.shoot(Type.BULLET, Direction.UP);
                        int currentShootingDelay = player1.getShootingDelay();
                        shootingDelayCounter += currentShootingDelay;
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
