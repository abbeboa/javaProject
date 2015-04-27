public class Player extends AbstractGameObject {
    /**
     * Here is the constructor for the player
     */
    public Player(final double x, final double y, final Type type) {
        super(x, y, type);
    }

    @Override
    public int getOwnerID() {
        return 0;
    }
}