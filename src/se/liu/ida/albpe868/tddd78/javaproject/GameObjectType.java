package se.liu.ida.albpe868.tddd78.javaproject;

/**
 * GameObjectType enum class. Variable gameObjectType set in AbstractGameObject class and used in CollisionHandler.
 */
public enum GameObjectType {
    /**
     * Includes player1 and player2 (possibly more players in the future)
     */
    PLAYER,
    /**
     * All types of enemies
     */
    ENEMY,
    /**
     * All types of projectiles
     */
    PROJECTILE,
    /**
     * All types of powerups
     */
    POWERUP
}

