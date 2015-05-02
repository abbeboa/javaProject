package se.liu.ida.albpe868.tddd78.javaproject;

/**
 * Position object that contains one x variable and one y variable.
 */
public final class Position {
    private double x;
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}

