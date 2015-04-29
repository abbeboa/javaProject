/**
 * Player object class, subclass to AbstractGameObject.
 */
public class Player extends AbstractGameObject {

    public Player(final double x, final double y, final Type type) {
        super(x, y, type);
    }

    public int getOwnerID() {
        super.getOwnerID();
        if (type == Type.PLAYER1) {
            return 0;
        }
        return 1;
    }

    public void update(GamePanel gamePanel) {
    }
}