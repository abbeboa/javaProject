import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Collection;

public abstract class AbstractGameObject {
    protected double x, y, speed;
    protected int hp;
    protected Boolean indestructible;
    protected Type type;
    protected BufferedImage image;
    protected Rectangle rectangle;
    protected int damage;
    protected GameObjectType gameObjectType;
    private static int counter = 0;
    private int id;

    public AbstractGameObject(final double x, final double y, final Boolean indestructible, final Type type) {
        this.x = x;
        this.y = y;
        setInitialValues(type);
        this.indestructible = indestructible;
        this.type = type;
        this.rectangle = new Rectangle((int) x, (int) y, image.getWidth(), image.getHeight());
        this.id = counter;
        increaseCounter();
    }

    private static void increaseCounter() {
        counter++;
    }

    public void drawGameObject(Graphics dbg, ImageObserver panel) {
        dbg.drawImage(image, (int) x, (int) y, panel);
    }

    public void setInitialValues(Type type) {
        switch (type) {
            case PLAYER1:
                this.speed = 3.0;
                this.image = GamePanel.imgPlayer1;
                this.hp = 3;
                this.gameObjectType = GameObjectType.PLAYER;
                break;
            case PLAYER2:
                this.speed = 1.0;
                this.image = GamePanel.imgPlayer1;
                this.hp = 100;
                this.gameObjectType = GameObjectType.PLAYER;
                break;
            case BULLET:
                this.speed = 5.0;
                this.image = GamePanel.imgBullet;
                this.hp = 0;
                this.damage = 1;
                this.gameObjectType = GameObjectType.PROJECTILE;
                break;
            case MISSILE:
                this.speed = 5.0;
                this.image = GamePanel.imgBullet;
                this.hp = 0;
                this.gameObjectType = GameObjectType.PROJECTILE;
                break;
            case BASICENEMY:
                this.speed = 1.0;
                this.image = GamePanel.imgBasicEnemy;
                this.hp = 1;
                this.gameObjectType = GameObjectType.ENEMY;
                break;
            default:
                System.out.println("getInitialValues fault");
                this.speed = 1.0;
                this.image = GamePanel.imgBullet;
                this.hp = 1;
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
            case UP_LEFT:
                break;
            case UP_RIGHT:
                break;
            case DOWN_LEFT:
                break;
            case DOWN_RIGHT:
                break;
            default:
                break;
        }
        if (this.type == Type.PLAYER1) {
            Rectangle testRectangle = new Rectangle((int) newX, (int) newY, image.getWidth(), image.getHeight());
            if (GamePanel.gameField.contains(testRectangle)) {
                x = newX;
                y = newY;
            }
        } else {
            x = newX;
            y = newY;
        }
        updateRectangle((int) x, (int) y);
    }

    private void updateRectangle(int newX, int newY) {
        this.rectangle = new Rectangle(newX, newY, image.getWidth(), image.getHeight());
    }

    public void shoot(Type type, Direction direction, Collection<AbstractGameObject> gameObjects, Collection<Projectile> projectileList) {
        Position pos = calculateShootPos(direction, type);
        Projectile newProjectile = new Projectile(pos.getX(), pos.getY(), true, type, direction, this.id);
        gameObjects.add(newProjectile);
        projectileList.add(newProjectile);
    }

    private Position calculateShootPos(Direction direction, Type bulletType) {
        double posX;
        double posY;
        int playerWidth = image.getWidth();
        int playerHeight = image.getHeight();
        //int bulletWidth = getInitialImage(bulletType).getWidth();
        //int bulletHeight = getInitialImage(bulletType).getHeight();
        switch (direction) {
            case UP:
                posX = x + (playerWidth / 2);
                posY = y;
                return new Position(posX, posY);
            case DOWN:
                posX = x + (playerWidth / 2);
                posY = y + playerHeight;
                return new Position(posX, posY);
            case LEFT:
                posX = x;
                posY = y + (playerHeight / 2);
                return new Position(posX, posY);
            case RIGHT:
                posX = x + playerWidth;
                posY = y + (playerHeight / 2);
                return new Position(posX, posY);
            case UP_LEFT:
                posX = x;
                posY = y;
                return new Position(posX, posY);
            case UP_RIGHT:
                posX = x;
                posY = y;
                return new Position(posX, posY);
            case DOWN_LEFT:
                posX = x;
                posY = y;
                return new Position(posX, posY);
            case DOWN_RIGHT:
                posX = x;
                posY = y;
                return new Position(posX, posY);
            default:
                posX = x;
                posY = y;
                return new Position(posX, posY);
        }
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

    public BufferedImage getImage() {
        return image;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public Boolean getIndestructible() {
        return indestructible;
    }

    public int getHp() {
        return hp;
    }

    public int getDamage() {
        return damage;
    }

    public GameObjectType getGameObjectType() {
        return gameObjectType;
    }
}
