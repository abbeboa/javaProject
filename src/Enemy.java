public class Enemy extends AbstractGameObject {

    public Enemy(final double x, final double y, final Boolean indestructible, final Type type) {
        super(x, y, indestructible, type);
    }

    public void update() {
        if (type == Type.BASICENEMY) {
            this.move(Direction.DOWN, speed);
        }
    }
}
