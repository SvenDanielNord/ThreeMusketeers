package com.mygdx.game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


    public class HighScore {
    static int highScore;
    static int allTimeScore;

    static int setHighScore(int score) {
        //saving high score if it's bigger than score
        if (score > highScore) {
            highScore = score;
        }

        readAllTimeScore(highScore);
        //writing all-time high score to a file


        try {
            FileWriter writer = new FileWriter("highscore.txt");
            writer.write(Integer.toString(allTimeScore));
            writer.close();
        } catch (IOException ex) {
            System.out.println("Something went wrong: " + ex.getMessage());
        }
        return highScore;
    }

    static void readAllTimeScore(int highScore) {


        //writing and reading all-time high score to/from a file
        try {

            Scanner scanner = new Scanner(new File("highscore.txt"));
            while (scanner.hasNextLine()) {
                allTimeScore = Integer.parseInt(scanner.nextLine());

            }

        } catch (IOException ex) {
            System.out.println("Something went wrong: " + ex.getMessage());
        }
        if (highScore > allTimeScore) {
            allTimeScore = highScore;
        }

    }

}
