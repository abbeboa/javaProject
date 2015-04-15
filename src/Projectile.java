public class Projectile extends AbstractGameObject {

    private int ownerID;
    private Direction direction;

    public Projectile(final double x, final double y, final Boolean indestructible,
                      final Type type, final Direction direction, final int ownerID) {
        super(x, y, indestructible, type);
        this.direction = direction;
        this.ownerID = ownerID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public Direction getDirection() {
        return direction;
    }
}
