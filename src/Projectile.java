import java.util.ArrayList;
import java.util.List;

public class Projectile extends AbstractGameObject {

    private int ownerID;

    public Projectile(final double x, final double y, final Boolean indestructible,
                      final Type type, final Direction direction, final int ownerID) {
        super(x, y, indestructible, type);
        this.direction = direction;
        this.ownerID = ownerID;
    }

    public void updateProjectile(List<AbstractGameObject> gameObjects, List<Projectile> projectileList, int pListIndex) {
        List<Integer> outsideProjectiles = new ArrayList<Integer>();
        List<Integer> outsideProjectilesIds = new ArrayList<Integer>();
        this.move(direction, speed);
        if (!GamePanel.gameField.contains(rectangle)) {
            outsideProjectiles.add(projectileList.indexOf(pListIndex));
            outsideProjectilesIds.add(this.getId());
        }
        removeProjectiles(outsideProjectiles, gameObjects, projectileList, outsideProjectilesIds);
    }

    private void removeProjectiles(List<Integer> outsideProjectiles, List<AbstractGameObject> gameObjects, List<Projectile> projectileList, List<Integer> outsideProjectilesIds) {
        for (int i = 0; i < outsideProjectiles.size(); i++) {
            try {
                projectileList.remove(i);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("removeProjectiles: " + e);
            }
        }
        for (int i = 0; i < gameObjects.size(); i++) {
            if (outsideProjectilesIds.contains(gameObjects.get(i).getId())) {
                gameObjects.remove(i);
            }
        }
    }

    public int getOwnerID() {
        return ownerID;
    }
}
