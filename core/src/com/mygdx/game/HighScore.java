package com.mygdx.game;

import com.mygdx.game.utils.Levels;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class HighScore {

    private static String highScorePath;
    static int highScoreEasy;
    static int highScoreHard;
    static int highScoreMedium;
    static int highScore;
    static int allTimeScore;

    static int setHighScore(int score, Levels level) {

        if (level == Levels.EASY){
            if (score > highScoreEasy) {
                highScoreEasy = score;
                highScore = highScoreEasy;
                readAllTimeScore(highScoreEasy);
            }
            highScore = highScoreEasy;
        }else if (level == Levels.HARD){
            if (score > highScoreHard) {
                highScoreHard = score;
                highScore = highScoreHard;
                readAllTimeScore(highScoreHard);
            }
            highScore = highScoreHard;
        }else {
            if (score > highScoreMedium) {
                highScoreMedium = score;
                highScore = highScoreMedium;
                readAllTimeScore(highScoreMedium);
            }
            highScore = highScoreMedium;
        }
        //saving high score if it's bigger than score




        //writing all-time high score to a file



        return highScore;
    }




    static void separateHighscores(Levels level) {
        if (level == Levels.EASY) {
            highScorePath = "easy.txt";
        } else if (level == Levels.HARD) {
            highScorePath = "hard.txt";
        } else {
            highScorePath = "highscore.txt";
        }
    }

    static void readAllTimeScore(int highScore) {


        //writing and reading all-time high score to/from a file
        try {

            Scanner scanner = new Scanner(new File(highScorePath));
            while (scanner.hasNextLine()) {
                allTimeScore = Integer.parseInt(scanner.nextLine());

            }

        } catch (IOException ex) {
            System.out.println("Something went wrong: " + ex.getMessage());
        }
        if (highScore > allTimeScore) {
            allTimeScore = highScore;
            writeAlltimeHighScore();
        }

    }
    private static void writeAlltimeHighScore() {
        try {
            FileWriter writer = new FileWriter(highScorePath);
            writer.write(Integer.toString(allTimeScore));
            writer.close();
        } catch (IOException ex) {
            System.out.println("Something went wrong: " + ex.getMessage());
        }
    }

}
