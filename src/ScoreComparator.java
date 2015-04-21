import java.util.Comparator;

class ScoreComparator implements Comparator<Highscore> {

    public int compare(Highscore o1, Highscore o2) {
        return o1.getScore() < o2.getScore() ? 1 : o1.getScore() == o2.getScore() ? 0 : -1;
    }
}
