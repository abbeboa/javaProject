import java.util.List;

/**
 * Created by Christian on 2015-04-26.
 */
public class CollisionHandler {

    public void checkForCollisions(List<AbstractGameObject> gameObjects) {
        for (int i = 0; i < gameObjects.size(); i++) {
            for (int j = 0; j < gameObjects.size(); j++) {
                //do not check collision with oneself
                if (i != j) {
                    if (gameObjects.get(i).rectangle.intersects(gameObjects.get(j).rectangle)) {
                        handleCollision(gameObjects.get(i), gameObjects.get(j));
                    }
                }
            }
        }
    }

    private void handleCollision(AbstractGameObject objectA, AbstractGameObject objectB) {
        switch (objectA.getGameObjectType()) {
            case PLAYER:
                playerCollisionActions(objectA, objectB);
                break;
            case ENEMY:
                enemyCollisionActions(objectA, objectB);
                break;
            case PROJECTILE:
                projectileCollisionActions(objectA, objectB);
                break;
            case POWERUP:
                break;
            default:
                System.out.println("handleCollision fault! 2");
        }
    }

    private void playerCollisionActions(AbstractGameObject objectA, AbstractGameObject objectB) {
        switch (objectB.getGameObjectType()) {
            case PLAYER:
                break;
            case ENEMY:
                break;
            case PROJECTILE:
                break;
            case POWERUP:
                if (!objectB.isPickedUp()) {
                    objectB.setInvisible(true);
                    objectB.setPickedUp(true);
                    objectB.setOwnerID(objectA.getId());
                }
                break;
            default:
                System.out.println("handleCollision fault! 1");
        }
    }

    private void enemyCollisionActions(AbstractGameObject objectA, AbstractGameObject objectB) {
        switch (objectB.getGameObjectType()) {
            case PLAYER:
                GamePanel.addGameObjectIdToRemove(objectA.getId());
                GamePanel.playSoundTakenHit();
                GamePanel.playSoundExplosion();
                changeStats(objectA, objectB);
                break;
            case ENEMY:
                break;
            case PROJECTILE:
                break;
            case POWERUP:
                break;
            default:
        }
    }

    private void projectileCollisionActions(AbstractGameObject objectA, AbstractGameObject objectB) {
        switch (objectB.getGameObjectType()) {
            case PLAYER:
                if (objectA.getOwnerID() != objectB.getId()) { // You can not hurt yourself
                    GamePanel.addGameObjectIdToRemove(objectA.getId());
                    changeStats(objectA, objectB);
                    GamePanel.playSoundTakenHit();
                }
                break;
            case ENEMY:
                if (objectA.getOwnerID() == 0) { // only players need score
                    changeStats(objectA, objectB);
                }
                GamePanel.addGameObjectIdToRemove(objectA.getId());
                break;
            case PROJECTILE:
                GamePanel.addGameObjectIdToRemove(objectA.getId());
                break;
            case POWERUP:
                break;
            default:
                System.out.println("handleCollision fault! 3");
        }
    }

    private void changeStats(AbstractGameObject objectA, AbstractGameObject objectB) {
        if (!objectB.indestructible) {
            objectB.hp -= objectA.getDamage();
        }
        checkIfDead(objectA);
        checkIfDead(objectB);
    }

    private void checkIfDead(AbstractGameObject objectX) {
        if (objectX.hp <= 0) {
            switch (objectX.getGameObjectType()) {
                case ENEMY:
                    GamePanel.addScore(100);
                    GamePanel.playSoundExplosion();
                    GamePanel.addGameObjectIdToRemove(objectX.getId());
                    break;
                case PROJECTILE:
                    GamePanel.addGameObjectIdToRemove(objectX.getId());
                    break;
                case PLAYER:
                    GamePanel.playSoundExplosion();
                    System.out.println("Game over");
                    GamePanel.setGameOver(true);
                    break;
                case POWERUP:
                    break;
                default:
                    System.out.println("CollisionHandler CHECKIFDEAD fault");
            }
        }
    }

}
