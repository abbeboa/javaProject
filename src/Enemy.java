import java.util.Random;

/**
 * Enemy object class, subclass to AbstractGameObject. Movements and shooting are either random or programmed, not controlled by
 * a human/player.
 */
public class Enemy extends AbstractGameObject {

    private Random rnd = new Random();
    private int timer = 0;
    private Direction direction;

    public Enemy(final double x, final double y, final Type type, Direction direction) {
        super(x, y, type);
        this.direction = direction;
    }

    public void update() {
        if (!GamePanel.ENEMIESGAMEFIELD.contains(rectangle)) {
            GamePanel.addGameObjectIdToRemove(this.getId());
            GamePanel.addScorePlayer1(-100);
            if (GamePanel.getPlayerCount() > 1) {
                GamePanel.addScorePlayer2(-100);
            }

        }
        if (x < 0 || x > (GamePanel.JPWIDTH - image.getWidth())) {
            changeDirection();
        }
        if (direction == Direction.RIGHT) {
            move(Direction.RIGHT, (speed * 2));
        } else {
            move(Direction.LEFT, (speed * 2));
        }

        if (type == Type.BASICENEMY) {
            move(Direction.DOWN, (speed / 2.0));
            int timeToShoot = rnd.nextInt(3);
            if (timer <= 0 && timeToShoot == 0) {
                Sound.playSoundEnemyBlaster();
                shoot(Type.BULLET, Direction.DOWN);
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
