import java.util.Random;

public class Enemy extends AbstractGameObject {

    private Random rnd = new Random();
    private int timer = 0;
    private Direction direction;

    public Enemy(final double x, final double y, final Boolean indestructible, final Type type, Direction direction) {
        super(x, y, indestructible, type);
        this.direction = direction;
    }

    public void update() {
        if (!GamePanel.ENEMIESGAMEFIELD.contains(rectangle)) {
            GamePanel.addGameObjectIdToRemove(this.getId());
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
            if (timer <= 0) {
                Sound.play("src/sounds/enemyBlaster.wav");
                shoot(Type.BULLET, Direction.DOWN);
                timer += getEnemyShootDelay();
            }
            if (timer > 0) {
                timer--;
            }
            /*
            timer++;
            int timeToShoot = rnd.nextInt(3);  // It's not always time to shoot
            if (timeToShoot == 0 && ((timer % 100) == 0)) { // Maximum one shot every second
                Sound.play("src/sounds/enemyBlaster.wav");
                shoot(Type.BULLET, Direction.DOWN);
                timer = 0;
            }*/
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
