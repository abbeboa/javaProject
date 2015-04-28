import java.util.Random;

/**
 * PowerUp object class, subclass to AbstractGameObject.
 * Main functions are to change certain variables of the Player object that it collides with,
 * and then change them back when time runs out.
 */
public class PowerUp extends AbstractGameObject {
    private static final int IDLETIME = 10000;
    private static final int EFFECTIVETIME = 5000;
    private static final int DEFAULTEXTRAHEALTH = 50;

    private long expireTime;
    private PowerUpType powerUpType;
    private boolean hasSetValues = false;

    public PowerUp(double x, double y, Type type) {
        super(x, y, type);
        expireTime = System.currentTimeMillis() + IDLETIME;
        setPowerUpType();
        this.updateRectangle((int) this.x, (int) this.y);
    }

    private void setPowerUpType() {
        int randomPowerUpType = new Random().nextInt(PowerUpType.values().length);
        this.powerUpType = PowerUpType.values()[randomPowerUpType];
    }

    public void update() {
        if (expireTime > System.currentTimeMillis()) {
            if (!hasSetValues && isPickedUp()) {
                setValues();
                expireTime = System.currentTimeMillis() + EFFECTIVETIME;
                hasSetValues = true;
            }
        } else {
            if (hasSetValues) {
                restoreValues();
            }
            GamePanel.addGameObjectIdToRemove(this.getId());
        }
    }

    private void setValues() {
        AbstractGameObject owner = GamePanel.getGameObject(this.getOwnerID());
        switch (powerUpType) {
            case DOUBLESPEED:
                Sound.playSoundDoubleSpeed();
                owner.speed *= 2;
                break;
            case INDESTRUCTIBLE:
                Sound.playSoundIndestructible();
                owner.indestructible = true;
                owner.image = GamePanel.getImgPlayerIndestructible();
                break;
            case DOUBLEFIRERATE:
                Sound.playSoundDoubleFirerate();
                owner.setShootingDelay(owner.getShootingDelay() / 2);
                break;
            case EXTRAHEALTH:
                Sound.playSoundExtraHealth();
                owner.hp += DEFAULTEXTRAHEALTH;
                if (owner.hp > PLAYERMAXIMUMHEALTH) {
                    owner.setHp(PLAYERMAXIMUMHEALTH);
                }
                break;
            default:
                System.out.println("PowerUp setValues fault!");
        }
    }

    private void restoreValues() {
        AbstractGameObject owner = GamePanel.getGameObject(this.getOwnerID());
        switch (powerUpType) {
            case DOUBLESPEED:
                owner.speed /= 2;
                break;
            case INDESTRUCTIBLE:
                owner.indestructible = false;
                setOriginalImage(owner);
                break;
            case DOUBLEFIRERATE:
                owner.setShootingDelay(owner.getShootingDelay() * 2);
                break;
            case EXTRAHEALTH:
                break;
            default:
                System.out.println("PowerUp setValues fault!");
        }
    }

    private void setOriginalImage(AbstractGameObject owner) {
        if (owner.type == Type.PLAYER1) {
            owner.image = GamePanel.getImgPlayer1();
        } else {
            owner.image = GamePanel.getImgPlayer2();
        }

    }
}
