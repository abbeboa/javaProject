/**
 * This enum class is used in the AbstractGameObject constructor to determine which variables to set to that object. (Not to be
 * confused with the GameObjectType class which is only used for collisions.)
 */
public enum Type {
    /**
     * First player
     */
    PLAYER1,
    /**
     * Second player
     */
    PLAYER2,
    /**
     * Default type of projectile
     */
    BULLET,
    /**
     * Another type of projectile
     */
    MISSILE,
    /**
     * Basic enemy is basic
     */
    BASICENEMY,
    /**
     * Player gets extra powers when colliding with this type of object
     */
    POWERUP
}
