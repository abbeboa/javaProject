import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/**
 * Superclass for all gameobjects. Subclasses: Player, Enemy, PowerUp, Projectile.
 */
public abstract class AbstractGameObject {
    //constants
    protected static final int PLAYERMAXIMUMHEALTH = 150;
    private static final double PLAYERDEFAULTSPEED = 3.0;
    private static final int PLAYERDEFAULTHEALTH = 100;
    private static final int PLAYERDEFAULTSHOOTINGDELAY = 30;
    private static final double BULLETDEFAULTSPEED = 5.0;
    private static final int PROJECTILEDEFAULTHEALTH = 0;
    private static final int BULLETDEFAULTDAMAGE = 20;
    private static final double MISSILEDEFAULTSPEED = 5.0;
    private static final double BASICENEMYDEFAULTSPEED = 1.0;
    private static final int BASICENEMYDEFAULTHP = 1;
    private static final int BASICENEMYDEFAULTDAMAGE = 40;
    private static final int BASICENEMYDEFAULTSHOOTINGDELAY = 60;
    private static final double DIVIDEBYTWODOUBLE = 2.0;

    protected double x, y, speed;
    protected int hp;
    protected Boolean indestructible = false;
    protected Type type;
    protected BufferedImage image = null;
    protected Rectangle rectangle;
    protected int damage = 0;
    protected GameObjectType gameObjectType = null;
    protected int id;
    protected int ownerID;
    protected int shootingDelay;
    protected boolean pickedUp;
    protected boolean invisible = false;
    private static int counter = 0;

    protected AbstractGameObject(final double x, final double y, final Type type) {
        this.x = x;
        this.y = y;
        setInitialValues(type);
        this.type = type;
        this.rectangle = new Rectangle((int) x, (int) y, image.getWidth(), image.getHeight());
        this.id = counter;
        increaseCounter();
    }

    private static void increaseCounter() {
        counter++;
    }

    public void drawGameObject(Graphics dbg, ImageObserver panel) {
        if (!invisible) {
            dbg.drawImage(image, (int) x, (int) y, panel);
        }
    }

    public abstract void update(GamePanel gamePanel);

    private void setInitialValues(Type type) {
        switch (type) {
            case PLAYER1:
                this.speed = PLAYERDEFAULTSPEED;
                this.image = GamePanel.getImgPlayer1();
                this.hp = PLAYERDEFAULTHEALTH;
                this.gameObjectType = GameObjectType.PLAYER;
                this.shootingDelay = PLAYERDEFAULTSHOOTINGDELAY;
                break;
            case PLAYER2:
                this.speed = PLAYERDEFAULTSPEED;
                this.image = GamePanel.getImgPlayer2();
                this.hp = PLAYERDEFAULTHEALTH;
                this.gameObjectType = GameObjectType.PLAYER;
                this.shootingDelay = PLAYERDEFAULTSHOOTINGDELAY;
                break;
            case BULLET:
                this.speed = BULLETDEFAULTSPEED;
                this.image = GamePanel.getImgBullet();
                this.hp = PROJECTILEDEFAULTHEALTH;
                this.damage = BULLETDEFAULTDAMAGE;
                this.gameObjectType = GameObjectType.PROJECTILE;
                break;
            case MISSILE:
                this.speed = MISSILEDEFAULTSPEED;
                this.image = GamePanel.getImgBullet();
                this.hp = PROJECTILEDEFAULTHEALTH;
                this.gameObjectType = GameObjectType.PROJECTILE;
                break;
            case BASICENEMY:
                this.speed = BASICENEMYDEFAULTSPEED;
                this.image = GamePanel.getImgBasicEnemy();
                this.hp = BASICENEMYDEFAULTHP;
                this.gameObjectType = GameObjectType.ENEMY;
                this.damage = BASICENEMYDEFAULTDAMAGE;
                this.shootingDelay = BASICENEMYDEFAULTSHOOTINGDELAY;
                break;
            case POWERUP:
                this.image = GamePanel.getImgBasicEnemy();
                this.indestructible = true;
                this.gameObjectType = GameObjectType.POWERUP;
                this.pickedUp = false;
                break;
            default:
                System.out.println("getInitialValues fault");
        }
    }

    public void move(Direction direction, double speed) {
        double newX = x;
        double newY = y;
        switch (direction) {
            case UP:
                newY -= speed;
                break;
            case DOWN:
                newY += speed;
                break;
            case LEFT:
                newX -= speed;
                break;
            case RIGHT:
                newX += speed;
                break;
            default:
                break;
        }
        if (this.gameObjectType == gameObjectType.PLAYER) {
            Rectangle testRectangle = new Rectangle((int) newX, (int) newY, image.getWidth(), image.getHeight());
            if (GamePanel.getGamefield().contains(testRectangle)) {
                x = newX;
                y = newY;
            }
        } else {
            x = newX;
            y = newY;
        }
        updateRectangle((int) x, (int) y);
    }

    public void updateRectangle(int newX, int newY) {
        this.rectangle = new Rectangle(newX, newY, image.getWidth(), image.getHeight());
    }

    public void shoot(Type type, Direction direction, GamePanel gamePanel) {
        Position pos = calculateShootPos(direction);
        Projectile newProjectile = new Projectile(pos.getX(), pos.getY(), type, direction, this.id);
        gamePanel.addToGameObjectsList(newProjectile);
    }

    private Position calculateShootPos(Direction direction) {
        double posX;
        double posY;
        int playerWidth = image.getWidth();
        int playerHeight = image.getHeight();
        double halfPlayerWidth = playerWidth / DIVIDEBYTWODOUBLE;
        double halfPlayerHeight = playerHeight / DIVIDEBYTWODOUBLE;
        switch (direction) {
            case UP:
                posX = x + halfPlayerWidth;
                posY = y;
                return new Position(posX, posY);
            case DOWN:
                posX = x + halfPlayerWidth;
                posY = y + playerHeight;
                return new Position(posX, posY);
            case LEFT:
                posX = x;
                posY = y + halfPlayerHeight;
                return new Position(posX, posY);
            case RIGHT:
                posX = x + playerWidth;
                posY = y + halfPlayerHeight;
                return new Position(posX, posY);
            default:
                posX = x;
                posY = y;
                return new Position(posX, posY);
        }
    }

    public int getOwnerID() {
        return ownerID;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSpeed() {
        return speed;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public int getId() {
        return id;
    }

    public GameObjectType getGameObjectType() {
        return gameObjectType;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public int getHp() {
        return hp;
    }

    public int getDamage() {
        return damage;
    }

    public static void setCounter(int counter) {
        AbstractGameObject.counter = counter;
    }

    public int getShootingDelay() {
        return shootingDelay;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public boolean isPickedUp() {
        return pickedUp;
    }

    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;
    }

    public void setShootingDelay(int shootingDelay) {
        this.shootingDelay = shootingDelay;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
