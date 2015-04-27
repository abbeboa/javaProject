import java.util.Random;

/**
 * Created by Christian on 2015-04-27.
 */
public class PowerUp extends AbstractGameObject {
    private long expireTime;
    private PowerUpType powerUpType;
    private boolean hasSetValues = false;

    public PowerUp(double x, double y, Type type) {
        super(x, y, type);
        expireTime = System.currentTimeMillis() + 10000;
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
            if (hasSetValues == false) {
                setValues();
                hasSetValues = true;
            }
        } else {
            restoreValues();
            GamePanel.addGameObjectIdToRemove(this.getId());
        }
    }

    private void setValues() {
        switch (powerUpType) {
            case DOUBLESPEED:
                break;
            case INDESTRUCTIBLE:
                break;
            default:
                System.out.println("PowerUp setValues fault!");
        }
    }

    private void restoreValues() {
        switch (powerUpType) {
            case DOUBLESPEED:
                break;
            case INDESTRUCTIBLE:
                break;
            default:
                System.out.println("PowerUp setValues fault!");
        }
    }
}
