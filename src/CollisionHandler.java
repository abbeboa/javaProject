import java.util.List;

/**
 * Collisionhandler is used in the GamePanel class. Collisionhandler iterates the gameObjects-list and checks for collisions
 * using the rectangle.intersects-method. It also performs actions depending on the gameObject types.
 */
public class CollisionHandler
{
    private GamePanel gamePanel;

    public CollisionHandler(GamePanel gamePanel) {
	this.gamePanel = gamePanel;
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
		Sound.playSoundTakenHit();
		Sound.playSoundExplosion();
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
		    Sound.playSoundTakenHit();
		}
		break;
	    case ENEMY:
		if (objectA.getOwnerID() == 0 || objectA.getOwnerID() == 1) { // only players need score
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
	checkIfDead(objectA, objectB);
	checkIfDead(objectB, objectA);
    }

    private void checkIfDead(AbstractGameObject objectA, AbstractGameObject objectB) {
	if (objectA.hp <= 0) {
	    switch (objectA.getGameObjectType()) {
		case ENEMY:
		    if (objectB.getOwnerID() == 0 || objectB.getId() == 0) {
			GamePanel.addScorePlayer1(100);
		    } else if (objectB.getOwnerID() == 1 || objectA.getId() == 1) {
			GamePanel.addScorePlayer2(100);
		    }
		    Sound.playSoundExplosion();
		    GamePanel.addGameObjectIdToRemove(objectA.getId());
		    break;
		case PROJECTILE:
		    GamePanel.addGameObjectIdToRemove(objectA.getId());
		    break;
		case PLAYER:
		    Sound.playSoundExplosion();
		    gamePanel.setGameOver(true);
		    break;
		case POWERUP:
		    break;
		default:
		    System.out.println("CollisionHandler CHECKIFDEAD fault");
	    }
	}
    }

}
