import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class AbstractGameObject implements GameObject {
    protected double x, y, speed;
    protected int hp;
    protected Boolean indestructible;
    protected Type type;
    protected BufferedImage image;
    protected Rectangle rectangle;
    private int counter = 0;
    private int id;

    public AbstractGameObject(final double x, final double y, final double speed, final int hp, final Boolean indestructible, final Type type) {
        this.x = x;
        this.y = y;
        this.speed = getInitialSpeed(type);
        this.hp = hp;
        this.indestructible = indestructible;
        this.type = type;
        this.image = getInitialImage(type);
        this.rectangle = new Rectangle((int)x, (int)y, image.getWidth(), image.getHeight());
        this.id = counter;
        counter++;

        int rectangleWidth = image.getWidth();
    }

    @Override
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

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public int getHP() {
        return hp;
    }

    @Override
    public double getSpeed() {
        return speed;
    }

    @Override
    public void shoot(Type type, Direction direction) {
        Position pos = calculateShootPos(direction, type);
        Projectile projectile = new Projectile(pos.getX(), pos.getY(), getInitialSpeed(type), 0, true, type, direction, this.id);
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

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public BufferedImage getInitialImage(Type type) {
        switch (type) {
            case PLAYER1:
                return GamePanel.imgPlayer1;
            case PLAYER2:
                return GamePanel.imgPlayer1;
            case BULLET:
                return GamePanel.imgBullet;
            case MISSILE:
                return GamePanel.imgBullet;
            default:
                System.out.println("getInitialImage fault");
                return GamePanel.imgBullet;
        }
    }

    public double getInitialSpeed(Type type) {
        switch (type) {
            case PLAYER1:
                return 3.0;
            case PLAYER2:
                return 1.0;
            case BULLET:
                return 5.0;
            case MISSILE:
                return 5.0;
            case BASICENEMY:
                return 1.0;
            default:
                System.out.println("getInitialSpeed fault");
                return 1.0;
        }
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
