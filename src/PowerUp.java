import java.util.Random;

/**
 * Created by Christian on 2015-04-27.
 */
public class PowerUp extends AbstractGameObject {
    private long expireTime;
    private PowerUpType powerUpType;
    private boolean hasSetValues = false;
    private int idleTime = 10000;
    private int effectiveTime = 5000;

    public PowerUp(double x, double y, Type type) {
        super(x, y, type);
        expireTime = System.currentTimeMillis() + idleTime;
        setPowerUpType();
        Position randomPos = randomPosition();
        this.x = randomPos.getX();
        this.y = randomPos.getY();
        this.updateRectangle((int) randomPos.getX(), (int) randomPos.getY());
    }

    private Position randomPosition() {
        Random random = new Random();
        int x = random.nextInt(GamePanel.JPWIDTH - image.getWidth());
        int y = random.nextInt(GamePanel.JPHEIGHT - image.getHeight());
        return new Position(x, y);
    }

    private void setPowerUpType() {
        int randomPowerUpType = new Random().nextInt(PowerUpType.values().length);
        this.powerUpType = PowerUpType.values()[randomPowerUpType];
    }

    public void update() {
        if (expireTime > System.currentTimeMillis()) {
            if (hasSetValues == false && isPickedUp()) {
                setValues();
                expireTime = System.currentTimeMillis() + effectiveTime;
                hasSetValues = true;
            }
        } else {
            if (hasSetValues == true) {
                restoreValues();
            }
            GamePanel.addGameObjectIdToRemove(this.getId());
        }
    }

    private void setValues() {
        AbstractGameObject owner = GamePanel.getGameObject(this.getOwnerID());
        switch (powerUpType) {
            case DOUBLESPEED:
                GamePanel.playSoundDoubleSpeed();
                owner.speed = owner.speed * 2;
                break;
            case INDESTRUCTIBLE:
                GamePanel.playSoundIndestructible();
                owner.indestructible = true;
                owner.image = GamePanel.getImgPlayerIndestructible();
                break;
            case DOUBLEFIRERATE:
                GamePanel.playSoundDoubleFirerate();
                owner.setShootingDelay(owner.getShootingDelay() / 2);
                break;
            case EXTRAHEALTH:
                GamePanel.playSoundExtraHealth();
                owner.hp += 50;
                if (owner.hp > owner.getMaxHp()) {
                    owner.setHp(owner.getMaxHp());
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
                owner.speed = owner.speed / 2;
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
