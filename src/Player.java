public class Player extends AbstractGameObject {
    /**
     * Here is the constructor for the player
     */

    public Player(final double x, final double y, final Type type) {
        super(x, y, type);
    }

    @Override
    public int getOwnerID() {
        if (type == Type.PLAYER1) {
            return 0;
        }
        return 1;
    }
}