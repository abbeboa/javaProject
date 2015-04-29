import java.util.Random;

/**
 * Enemy object class, subclass to AbstractGameObject. Movements and shooting are either random or programmed,
 * not controlled by a human/player.
 */
public class Enemy extends AbstractGameObject {

    private Random rnd = new Random();
    private int timer = 0;
    private Direction direction;

    public Enemy(final double x, final double y, final Type type, Direction direction) {
        super(x, y, type);
        this.direction = direction;
    }

    public void update(GamePanel gamePanel) {
        if (!gamePanel.getEnemiesgamefield().contains(rectangle)) {
            gamePanel.addGameObjectIdToRemove(id);
            gamePanel.addScorePlayer1(-100);
            if (gamePanel.getPlayerCount() > 1) {
                gamePanel.addScorePlayer2(-100);
            }

        }
        if (x < 0 || x > (gamePanel.getJpwidth() - image.getWidth())) {
            changeDirection();
        }
        if (direction == Direction.RIGHT) {
            move(Direction.RIGHT, (speed * 2), gamePanel);
        } else {
            move(Direction.LEFT, (speed * 2), gamePanel);
        }

        if (type == Type.BASICENEMY) {
            move(Direction.DOWN, (speed / 2.0), gamePanel);
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

    }

    public void changeDirection() {
        if (direction == Direction.LEFT) {
            direction = Direction.RIGHT;
        } else {
            direction = Direction.LEFT;
        }
    }
}
