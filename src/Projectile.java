/**
 * Projectile object class, subclass to AbstractGameObject.
 */
public class Projectile extends AbstractGameObject {

    private Direction direction;

    public Projectile(final double x, final double y, final Type type, final Direction direction, final int ownerID) {
        super(x, y, type);
        this.direction = direction;
        this.setOwnerID(ownerID);
    }

    public void updateProjectile() {
        this.move(direction, speed);
        if (!GamePanel.GAMEFIELD.contains(rectangle)) {
            GamePanel.addGameObjectIdToRemove(this.getId());
        }
    }
}
