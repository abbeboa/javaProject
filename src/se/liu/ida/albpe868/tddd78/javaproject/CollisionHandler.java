package se.liu.ida.albpe868.tddd78.javaproject;

import java.util.List;

/**
 * CollisionHandler is used in the GamePanel class. CollisionHandler iterates the gameObjects-list and checks for collisions
 * using the rectangle.intersects-method. It also performs actions depending on the gameObject types.
 */
public class CollisionHandler {
    private GamePanel gamePanel;

    public CollisionHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void checkForCollisions(List<AbstractGameObject> gameObjects) {
        for (int i = 0; i < gameObjects.size(); i++) {
            for (int j = i + 1; j < gameObjects.size(); j++) {
                if (gameObjects.get(i).rectangle.intersects(gameObjects.get(j).rectangle)) {
                    handleCollision(gameObjects.get(i), gameObjects.get(j));
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
        }
    }

    private void playerCollisionActions(AbstractGameObject player, AbstractGameObject objectB) {
        switch (objectB.getGameObjectType()) {
            case PLAYER:
                break;
            case ENEMY:
                playerEnemyCollisionAction(player, objectB);
                break;
            case PROJECTILE:
                playerProjectileCollisionAction(player, objectB);
                break;
            case POWERUP:
                playerPowerUpAction(player, objectB);
                break;
            default:
        }
    }

    private void playerEnemyCollisionAction(AbstractGameObject player, AbstractGameObject enemy) {
        gamePanel.addGameObjectIdToRemove(enemy.getId());
        Sound.playSoundTakenHit(gamePanel.isSoundEnabled());
        Sound.playSoundExplosion(gamePanel.isSoundEnabled());
        changeStats(enemy, player);
    }

    private void playerProjectileCollisionAction(AbstractGameObject player, AbstractGameObject projectile) {
        if (projectile.getOwnerID() != player.getId()) { // You can not hurt yourself
            gamePanel.addGameObjectIdToRemove(projectile.getId());
            changeStats(projectile, player);
            Sound.playSoundTakenHit(gamePanel.isSoundEnabled());
        }
    }

    private void playerPowerUpAction(AbstractGameObject player, AbstractGameObject powerUp) {
        if (!powerUp.isPickedUp()) {
            powerUp.setInvisible(true);
            powerUp.setPickedUp(true);
            powerUp.setOwnerID(player.getId());
        }
    }

    private void enemyCollisionActions(AbstractGameObject enemy, AbstractGameObject objectB) {
        switch (objectB.getGameObjectType()) {
            case PLAYER:
                break;
            case ENEMY:
                break;
            case PROJECTILE:
                projectileEnemyCollisionAction(objectB, enemy);
                break;
            case POWERUP:
                break;
            default:
        }
    }

    private void projectileCollisionActions(AbstractGameObject projectile, AbstractGameObject objectB) {
        switch (objectB.getGameObjectType()) {
            case PLAYER:
                break;
            case ENEMY:
                projectileEnemyCollisionAction(projectile, objectB);
                break;
            case PROJECTILE:
                gamePanel.addGameObjectIdToRemove(projectile.getId());
                gamePanel.addGameObjectIdToRemove(objectB.getId());
                break;
            case POWERUP:
                break;
            default:
        }
    }

    private void projectileEnemyCollisionAction(AbstractGameObject projectile, AbstractGameObject enemy) {
        if (projectile.getOwnerID() == 0 || projectile.getOwnerID() == 1) { // only players need score
            changeStats(projectile, enemy);
        }
        gamePanel.addGameObjectIdToRemove(projectile.getId());
    }

    private void changeStats(AbstractGameObject objectGivingDamage, AbstractGameObject objectReceivingDamage) {
        if (!objectReceivingDamage.indestructible) {
            objectReceivingDamage.hp -= objectGivingDamage.getDamage();
        }
        checkIfDead(objectGivingDamage, objectReceivingDamage);
    }

    private void checkIfDead(AbstractGameObject objectGivingDamage, AbstractGameObject objectReceivingDamage) {
        if (objectReceivingDamage.hp <= 0) {
            switch (objectReceivingDamage.getGameObjectType()) {
                case ENEMY:
                    createExplosion(objectReceivingDamage.getX(), objectReceivingDamage.getY());
                    if (objectGivingDamage.getOwnerID() == 0 || objectGivingDamage.getId() == 0) {
                        gamePanel.addScorePlayer1(100);
                    } else if (objectGivingDamage.getOwnerID() == 1 || objectReceivingDamage.getId() == 1) {
                        gamePanel.addScorePlayer2(100);
                    }
                    Sound.playSoundExplosion(gamePanel.isSoundEnabled());
                    gamePanel.addGameObjectIdToRemove(objectReceivingDamage.getId());
                    break;
                case PROJECTILE:
                    gamePanel.addGameObjectIdToRemove(objectReceivingDamage.getId());
                    break;
                case PLAYER:
                    Sound.playSoundExplosion(gamePanel.isSoundEnabled());
                    gamePanel.setGameOver(true);
                    break;
                case POWERUP:
                    break;
                default:
            }
        }
    }

    private void createExplosion(double x, double y) {
        VisualEffect newExplosion = new VisualEffect(x, y, VisualEffectType.EXPLOSION);
        gamePanel.addToVisualEffects(newExplosion);

    }
}

