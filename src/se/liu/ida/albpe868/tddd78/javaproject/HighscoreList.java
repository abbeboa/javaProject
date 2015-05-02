package se.liu.ida.albpe868.tddd78.javaproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class HighscoreList {
    private static final HighscoreList INSTANCE = new HighscoreList();
    private final List<Highscore> highScorelist = new ArrayList<>();

    public static void addHighscore(Highscore hs) {
        HighscoreList list = INSTANCE;
        list.highScorelist.add(hs);
        Collections.sort(list.highScorelist, new ScoreComparator());
        for (Highscore highscore : list.highScorelist) {
            System.out.println(highscore.getPlayer() + ": score: " + highscore.getScore());
        }
    }

    private HighscoreList() {
    }
}

