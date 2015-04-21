import java.util.Random;

public class Enemy extends AbstractGameObject {

    private Random rnd = new Random();
    private int timer = 0;

    public Enemy(final double x, final double y, final Boolean indestructible, final Type type) {
        super(x, y, indestructible, type);
    }

    public void update() {
        if (type == Type.BASICENEMY) {
            timer++;
            this.move(Direction.DOWN, speed);
            int timeToShoot = rnd.nextInt(3);  // It's not always time to shoot
            if (timeToShoot == 0 && ((timer % 100) == 0)) { // Maximum one shot every second
                Sound.play("src/sounds/enemyBlaster.wav");
                shoot(Type.BULLET, Direction.DOWN, GamePanel.getGameObjects(), GamePanel.getProjectileList());
                timer = 0;
            }
        }
    }
}
