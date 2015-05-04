package se.liu.ida.albpe868.tddd78.javaproject;

import java.util.Random;

/**
 * Enemy object class, subclass to AbstractGameObject. Movements and shooting are either random or programmed,
 * not controlled by a human/player.
 */
public class Enemy extends AbstractGameObject {

    private static final double DIVIDEBYTWODOUBLE = 2.0;
    private static final double MOVEMENTPATTERN1CONSTANT = 5000;
    private Random rnd = new Random();
    private int timer = 0;
    private Direction direction;
    private int movementPattern;

    public Enemy(final double x, final double y, final Type type, final Direction direction, final int movementPattern) {
        super(x, y, type);
        this.direction = direction;
        this.movementPattern = movementPattern;
    }

    public void update(GamePanel gamePanel) {
        if (!GamePanel.getEnemiesgamefield().contains(rectangle)) {
            gamePanel.addGameObjectIdToRemove(id);
            gamePanel.addScorePlayer1(-100);
            if (gamePanel.getPlayerCount() > 1) {
                gamePanel.addScorePlayer2(-100);
            }
        }
        if (type == Type.BASICENEMY) {
            int timeToShoot = rnd.nextInt(3);
            if (timer <= 0 && timeToShoot == 0) {
                Sound.playSoundEnemyBlaster(gamePanel.isSoundEnabled());
                shoot(Type.BULLET, Direction.DOWN, gamePanel);
                timer += getShootingDelay();
            }
            if (timer > 0) {
                timer--;
            }
        }
        if (movementPattern == 0) {
            movementPattern0();
        } else if (movementPattern == 1) {
            movementPattern1();
        } else {
            move(Direction.DOWN, (speed / DIVIDEBYTWODOUBLE));
        }
    }

    private void movementPattern0() {
        if (x < 0 || x > (GamePanel.getJpwidth() - image.getWidth())) {
            changeDirection();
        }
        if (direction == Direction.RIGHT) {
            move(Direction.RIGHT, (speed * 2));
        } else {
            move(Direction.LEFT, (speed * 2));
        }
        move(Direction.DOWN, (speed / DIVIDEBYTWODOUBLE));
    }

    private void movementPattern1() {
        x -= speed;
        y += Math.sin(Math.toDegrees(x / MOVEMENTPATTERN1CONSTANT));
        updateRectangle((int) x, (int) y);
    }

    public void changeDirection() {
        if (direction == Direction.LEFT) {
            direction = Direction.RIGHT;
        } else {
            direction = Direction.LEFT;
        }
    }
}
