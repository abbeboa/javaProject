import java.util.Random;

/**
 * PowerUp object class, subclass to AbstractGameObject. Main functions are to change certain variables of the Player object
 * that it collides with, and then change them back when time runs out.
 */
public class PowerUp extends AbstractGameObject {
    private static final int IDLETIME = 10000;
    private static final int EFFECTIVETIME = 5000;
    private static final int DEFAULTEXTRAHEALTH = 50;
    private boolean firstUpdate = true;

    private long expireTime;
    private PowerUpType powerUpType;
    private boolean hasSetValues = false;

    public PowerUp(double x, double y, Type type) {
        super(x, y, type);
        expireTime = System.currentTimeMillis() + IDLETIME;
        setPowerUpType();
    }

    private void setPowerUpType() {
        int randomPowerUpType = new Random().nextInt(PowerUpType.values().length);
        this.powerUpType = PowerUpType.values()[randomPowerUpType];
    }

    public void update(GamePanel gamePanel) {
        if (expireTime > System.currentTimeMillis()) {
            if (!hasSetValues && isPickedUp()) {
                setValues(gamePanel);
                expireTime = System.currentTimeMillis() + EFFECTIVETIME;
                hasSetValues = true;
            }
        } else {
            if (hasSetValues) {
                restoreValues(gamePanel);
            }
            gamePanel.addGameObjectIdToRemove(id);
        }
        if (firstUpdate) {
            Position randomPos = randomPosition(gamePanel);
            x = randomPos.getX();
            y = randomPos.getY();
            updateRectangle((int) x, (int) y);
            firstUpdate = false;
        }
    }

    private void setValues(GamePanel gamePanel) {
        AbstractGameObject owner = gamePanel.getGameObject(ownerID);
        switch (powerUpType) {
            case DOUBLESPEED:
                Sound.playSoundDoubleSpeed(gamePanel.isSoundEnabled());
                owner.speed *= 2;
                break;
            case INDESTRUCTIBLE:
                Sound.playSoundIndestructible(gamePanel.isSoundEnabled());
                owner.indestructible = true;
                owner.image = GamePanel.getImgPlayerIndestructible();
                break;
            case DOUBLEFIRERATE:
                Sound.playSoundDoubleFirerate(gamePanel.isSoundEnabled());
                owner.setShootingDelay(owner.getShootingDelay() / 2);
                break;
            case EXTRAHEALTH:
                Sound.playSoundExtraHealth(gamePanel.isSoundEnabled());
                owner.hp += DEFAULTEXTRAHEALTH;
                if (owner.hp > PLAYERMAXIMUMHEALTH) {
                    owner.setHp(PLAYERMAXIMUMHEALTH);
                }
                break;
            default:
                System.out.println("PowerUp setValues fault!");
        }
    }

    private void restoreValues(GamePanel gamePanel) {
        AbstractGameObject owner = gamePanel.getGameObject(ownerID);
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

    private Position randomPosition(GamePanel gamePanel) {
        Random random = new Random();
        int x = random.nextInt(gamePanel.getJpwidth() - image.getWidth());
        int y = random.nextInt(GamePanel.getJpheight() - image.getHeight());
        return new Position(x, y);
    }
}
