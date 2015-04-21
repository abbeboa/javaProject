import java.util.List;

public class Projectile extends AbstractGameObject {

    private Direction direction;
    private int ownerID;

    public Projectile(final double x, final double y, final Boolean indestructible,
                      final Type type, final Direction direction, final int ownerID) {
        super(x, y, indestructible, type);
        this.direction = direction;
        this.setOwnerID(ownerID);
    }

    public List<Integer> updateProjectile(List<Integer> gameObjectIdsToRemove) {
        this.move(direction, speed);
        if (!GamePanel.GAMEFIELD.contains(rectangle)) {
            gameObjectIdsToRemove.add(this.getId());
        }
        return gameObjectIdsToRemove;
    }

    public Direction getDirection() {
        return direction;
    }
}
