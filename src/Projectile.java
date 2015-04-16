import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Projectile extends AbstractGameObject {

    private int ownerID;
    private Direction direction;

    public Projectile(final double x, final double y, final Boolean indestructible,
                      final Type type, final Direction direction, final int ownerID) {
        super(x, y, indestructible, type);
        this.direction = direction;
        this.ownerID = ownerID;
    }

    public void updateProjectile(List<AbstractGameObject> gameObjects, Rectangle gameField) {
        List<Integer> outsideProjectiles = new ArrayList<Integer>();
        for (int i = 0; i < gameObjects.size(); i++) {
            if (gameObjects.get(i).getClass() == Projectile.class) {
                AbstractGameObject p = gameObjects.get(i);
                p.move(p.getDirection(), p.getSpeed());
                if (!gameField.contains(p.getRectangle())) {
                    outsideProjectiles.add(gameObjects.indexOf(p));
                }
            }
        }
        removeProjectiles(outsideProjectiles, gameObjects);
    }

    private void removeProjectiles(List<Integer> outsideProjectiles, List<AbstractGameObject> gameObjects) {
        for (int i = 0; i < outsideProjectiles.size(); i++) {
            try {
                gameObjects.remove(i);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("removeProjectiles: " + e);
            }

        }
    }

    public int getOwnerID() {
        return ownerID;
    }
}
