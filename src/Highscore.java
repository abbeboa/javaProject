
public class Highscore {

    private final int score;
    private final String player;

    Highscore(int score, String player) {
        this.score = score;
        this.player = player;
    }

    public int getScore() {
        return score;
    }

    public String getPlayer() {
        return player;
    }
}
