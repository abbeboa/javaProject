package se.liu.ida.albpe868.tddd78.javaproject;

import java.io.*;

/**
 * This class reads/writes highscores from/to a textfile.
 */
public class HighscoreHandler {
    private final static String FILENAME = "src/gamefiles/highscores.txt";

    public void readFile() {
        try (FileReader fileReader = new FileReader(FILENAME);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line = bufferedReader.readLine();
            while (line != null) {
                String player = line.replaceAll("[^a-zA-Z]+", "");
                String scoreString = line.replaceAll("[^0-9]+", "");
                int score = Integer.parseInt(scoreString);
                Highscore highscore = new Highscore(score, player);
                HighscoreList.addHighscore(highscore);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void writeToFile() {
        try (FileWriter fileWriter = new FileWriter(FILENAME);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(HighscoreList.getAllHighScores());
            bufferedWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
