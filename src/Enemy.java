import java.util.Random;

public class Enemy extends AbstractGameObject {

    private int timeToMove = 0;
    Random rnd = new Random();


    public Enemy(final double x, final double y, final Boolean indestructible, final Type type) {
        super(x, y, indestructible, type);
    }

    public void update() {
        if (type == Type.BASICENEMY) {
            if (timeToMove % 100 == 0) {// For every second (since it's 100 fps)
                int changeDirection = rnd.nextInt(3)-1;
                x += changeDirection;
            }
            timeToMove++;
            y += speed;
        }
    }
}
