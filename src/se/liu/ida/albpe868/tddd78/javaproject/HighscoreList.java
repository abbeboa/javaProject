package se.liu.ida.albpe868.tddd78.javaproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class HighscoreList {
    private static final int MAXIMUMNUMBEROFHIGHSCORES = 10;
    private static final HighscoreList INSTANCE = new HighscoreList();
    private final List<Highscore> highScorelist = new ArrayList<>();

    private HighscoreList() {
    }

    public static void addHighscore(Highscore hs) {
        HighscoreList list = INSTANCE;
        list.highScorelist.add(hs);
        Collections.sort(list.highScorelist, new ScoreComparator());
    }

    public static String getAllHighScores() {
        HighscoreList list = INSTANCE;
        StringBuilder highScoreString = new StringBuilder();
        for (int i = 0; i < getNumberOfHighScores(); i++) {
            highScoreString.append(list.highScorelist.get(i).getPlayer()).append("||")
                    .append(list.highScorelist.get(i).getScore()).append("\r\n");
        }
        return highScoreString.toString();
    }

    public static void printAllHighScores() {
        HighscoreList list = INSTANCE;
        System.out.println("*** HIGHSCORES TOP 10 ***");
        for (int i = 0; i < getNumberOfHighScores(); i++) {
            System.out.println(list.highScorelist.get(i).getPlayer()
                    + ": --- Score: " + list.highScorelist.get(i).getScore());
        }
    }

    private static int getNumberOfHighScores() {
        if (getListLength() < MAXIMUMNUMBEROFHIGHSCORES) {
            return getListLength();
        } else {
            return MAXIMUMNUMBEROFHIGHSCORES;
        }
    }

    private static int getListLength() {
        HighscoreList list = INSTANCE;
        return list.highScorelist.size();
    }
}

