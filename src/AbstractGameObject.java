import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class AbstractGameObject {
    protected double x, y, speed;
    protected int hp;
    protected Boolean indestructible;
    protected Type type;
    protected BufferedImage image;
    protected Rectangle rectangle;
    private int counter = 0;
    private int id;

    public AbstractGameObject(final double x, final double y, final Boolean indestructible, final Type type) {
        this.x = x;
        this.y = y;
        setInitialValues(type);
        this.indestructible = indestructible;
        this.type = type;
        this.rectangle = new Rectangle((int)x, (int)y, image.getWidth(), image.getHeight());
        this.id = counter;
        counter++;
    }

    private void setInitialValues(Type type) {
        switch (type) {
            case PLAYER1:
                this.speed = 3.0;
                this.image = GamePanel.imgPlayer1;
                this.hp = 100;
                break;
            case PLAYER2:
                this.speed = 1.0;
                this.image = GamePanel.imgPlayer1;
                this.hp = 100;
                break;
            case BULLET:
                this.speed = 5.0;
                this.image = GamePanel.imgBullet;
                this.hp = 0;
                break;
            case MISSILE:
                this.speed = 5.0;
                this.image = GamePanel.imgBullet;
                this.hp = 0;
                break;
            case BASICENEMY:
                this.speed = 1.0;
                this.image = GamePanel.imgPlayer1;
                this.hp = 2;
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
            Rectangle testRectangle = new Rectangle((int)newX, (int)newY, image.getWidth(), image.getHeight());
            if (GamePanel.gameField.contains(testRectangle)) {
                x = newX;
                y = newY;
            }
        } else {
            x = newX;
            y = newY;
        }
        updateRectangle((int)x, (int)y);
    }

    private void updateRectangle(int newX, int newY) {
        this.rectangle = new Rectangle(newX, newY, image.getWidth(), image.getHeight());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getHP() {
        return hp;
    }

    public double getSpeed() {
        return speed;
    }

    public void shoot(Type type, Direction direction) {
        Position pos = calculateShootPos(direction, type);
        Projectile projectile = new Projectile(pos.getX(), pos.getY(), true, type, direction, this.id);
        GamePanel.projectileList.add(projectile);
    }

    private Position calculateShootPos(Direction direction, Type bulletType) {
        double posX;
        double posY;
        int playerWidth = this.getImage().getWidth();
        int playerHeight = this.getImage().getHeight();
        //int bulletWidth = getInitialImage(bulletType).getWidth();
        //int bulletHeight = getInitialImage(bulletType).getHeight();
        switch (direction) {
            case UP:
                posX = this.getX() + (playerWidth / 2);
                posY = this.getY();
                return new Position(posX, posY);
            case DOWN:
                posX = this.getX() + (playerWidth / 2);
                posY = this.getY() + playerHeight;
                return new Position(posX, posY);
            case LEFT:
                posX = this.getX();
                posY = this.getY() + (playerHeight / 2);
                return new Position(posX, posY);
            case RIGHT:
                posX = this.getX() + playerWidth;
                posY = this.getY() + (playerHeight / 2);
                return new Position(posX, posY);
            case UP_LEFT:
                posX = this.getX();
                posY = this.getY();
                return new Position(posX, posY);
            case UP_RIGHT:
                posX = this.getX();
                posY = this.getY();
                return new Position(posX, posY);
            case DOWN_LEFT:
                posX = this.getX();
                posY = this.getY();
                return new Position(posX, posY);
            case DOWN_RIGHT:
                posX = this.getX();
                posY = this.getY();
                return new Position(posX, posY);
            default:
                posX = this.getX();
                posY = this.getY();
                return new Position(posX, posY);
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
