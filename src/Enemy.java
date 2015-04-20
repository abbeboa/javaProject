import java.util.Random;

public class Enemy extends AbstractGameObject {

    Random rnd = new Random();
    int timer = 0;

    public Enemy(final double x, final double y, final Boolean indestructible, final Type type) {
        super(x, y, indestructible, type);
    }

    public void update() {
        if (type == Type.BASICENEMY) {
            timer++;
            this.move(Direction.DOWN, speed);
            int timeToShoot = rnd.nextInt(20);  // It's not always time to shoot
            if (timeToShoot == 0 && (timer&300) == 0) { // Maximum one shot every third second
                Sound.play("src/sounds/enemyBlaster.wav");
                shoot(Type.BULLET, Direction.DOWN, GamePanel.getGameObjects(), GamePanel.getProjectileList());
                }
            }
        }
    }