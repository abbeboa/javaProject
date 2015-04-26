import java.util.List;

/**
 * Created by Christian on 2015-04-26.
 */
public class CollisionHandler {
    public CollisionHandler() {
    }

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
                break;
            case ENEMY:
                enemyCollisionActions(objectA, objectB);
                break;
            case PROJECTILE:
                projectileCollisionActions(objectA, objectB);
                break;
            default:
                System.out.println("handleCollision fault!");
        }
    }

    private void enemyCollisionActions(AbstractGameObject objectA, AbstractGameObject objectB) {
        switch (objectB.getGameObjectType()) {
            case PLAYER:
                GamePanel.addGameObjectIdToRemove(objectA.getId());
                GamePanel.playSoundTakenHit();
                changeStats(objectA, objectB);
                break;
            case ENEMY:
                break;
            case PROJECTILE:
                break;
            default:
        }
    }

    private void projectileCollisionActions(AbstractGameObject objectA, AbstractGameObject objectB) {
        switch (objectB.getGameObjectType()) {
            case PLAYER:
                GamePanel.addGameObjectIdToRemove(objectA.getId());
                changeStats(objectA, objectB);
                if (objectA.getOwnerID() != objectB.getId()) { // You can not hurt yourself
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
            default:
                System.out.println("handleCollision fault!");
        }
    }

    private void changeStats(AbstractGameObject objectA, AbstractGameObject objectB) {
        if (objectA.getOwnerID() != objectB.getId()) { // You can not hurt yourself
            objectA.hp--;
            objectB.hp--;
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
            }
        }
    }

}
