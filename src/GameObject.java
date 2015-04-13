import java.awt.image.BufferedImage;

public interface GameObject {
    public abstract double getX();

    public abstract double getY();

    public abstract double getSpeed();

    public abstract int getHP();

    public abstract BufferedImage getImage();

    public void move(Direction direction, double speed);

    public void shoot(Type type, Direction direction);

    public BufferedImage getInitialImage(Type type);

}