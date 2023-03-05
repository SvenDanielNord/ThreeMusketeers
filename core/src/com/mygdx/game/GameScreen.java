package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.Rectangle;

import java.io.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class GameScreen implements Screen {

    final FlappyBird game;
    SpriteBatch batch;
    /**
     * bird image
     */
    Texture background;
    Texture phoenixImage;
    Texture blockImage;
    Texture longImage;
    Texture imageShort;
    OrthographicCamera camera;
    Rectangle phoenix;
    Rectangle block;
    Rectangle block2;


    Array<Rectangle> blockBank;
    long lastBlock;
    int score;

    //highscore for this playing round
    static int highScore;

    static int allTimeScore;


    public GameScreen(FlappyBird game) {
        this.game = game;

        /**
         * Loading image (64*64) for customer
         */
        background = new Texture(Gdx.files.internal("bgtest.png"));
        phoenixImage = new Texture(Gdx.files.internal("Phoenix.gif"));
        blockImage = new Texture(Gdx.files.internal("block.png"));
        longImage = new Texture(Gdx.files.internal("Building.png"));
        imageShort = new Texture(Gdx.files.internal("block88.png"));


        /**
         * Creating camera and start batch
         */
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        /**
         * Creating player box and setting coordinates for it
         */
        phoenix = new Rectangle();
        phoenix.x = 800 / 2 - 64 / 2;
        phoenix.y = 220;
        phoenix.width = 64;
        phoenix.height = 64;

        blockBank = new Array<Rectangle>();

        score = 0;


    }

    private void spawnBlocks() {
        block = new Rectangle();
        block.width = 104;
        block.height = 311;

        block2 = new Rectangle();
        block2.width = 104;
        block2.height = 311;

        setBlockPosition();
        blockBank.add(block);
        blockBank.add(block2);
        lastBlock = TimeUtils.nanoTime();
    }

    private void setBlockPosition() {
        int random = ThreadLocalRandom.current().nextInt(3) + 1;

        if (random == 1) {
            block.x = 1000;
            block.y = 0 - 150;
            block2.x = 1000;
            block2.y = 480 - 144;
        } else if (random == 2) {
            block.x = 1000;
            block.y = 480 - 211;
            block2.x = 1000;
            block2.y = 0 - 211;

        } else {
            block.x = 1000;
            block.y = 0 - 111;
            block2.x = 1000;
            block2.y = 480 - 111;

        }
    }


    public void createBlocks() {
    }

    @Override
    public void show() {

    }

    public int getHighScoreAllTime() {
        return allTimeScore;
    }

    @Override
    public void render(float delta) {
        /**
         * Setting new color for background
         */
        ScreenUtils.clear(0, 50, 0, 1);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        /**
         * Start new batch with instruction message to customer and star position for box
         */
        game.batch.begin();
        game.batch.draw(background, 0, 0);

        game.batch.draw(phoenixImage, phoenix.x, phoenix.y, phoenix.width, phoenix.height);

        for (Rectangle block : blockBank) {
            game.batch.draw(longImage, block.x, block.y);
        }

        game.font.draw(game.batch, "Score: " + score, 380, 480);
        game.batch.end();
        /**
         * Input to start the game
         */

        if (Gdx.input.isTouched()) {
            Vector3 startPas = new Vector3();
            startPas.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(startPas);
            phoenix.x = (int) (startPas.x - 64 / 2);
        }
        /**
         * Input to jump, press space key
         */
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isTouched(Input.Buttons.LEFT)) {
            phoenix.y += 800 * Gdx.graphics.getDeltaTime();

            if (phoenix.y < 0)
                phoenix.y = 0;
            if (phoenix.y > 480 - 64)
                phoenix.y = 480 - 64;
        }
        /**
         * Bird sinking time
         */
        phoenix.y -= 150 * Gdx.graphics.getDeltaTime();

        if (TimeUtils.nanoTime() - lastBlock > 1000000000) {
            score = score + 100;
            spawnBlocks();
        }

        Iterator<Rectangle> iter = blockBank.iterator();
        while (iter.hasNext()) {
            Rectangle block = iter.next();
            block.x -= (200 * Gdx.graphics.getDeltaTime());

            if (block.overlaps(phoenix)) {

                game.setScreen((new DeathScreen(game, score)));
            }
            if (block.getX() < -150) {
                iter.remove();
            }
        }

        //saving high score if it's bigger than score
        if (score > highScore) {
            highScore = score;
        }
        //writing and reading all-time high score to/from a file
        try {
            Scanner scanner = new Scanner(new File("highscore.txt"));
            while (scanner.hasNextLine()) {
                allTimeScore = Integer.parseInt(scanner.nextLine());
            }
        }
        catch (IOException ex) {
            System.out.println("Something went wrong: " + ex.getMessage());
        }

        //writing all-time high score to a file
        if(highScore > allTimeScore) {
            allTimeScore = highScore;

            try {
                FileWriter writer = new FileWriter("highscore.txt");
                writer.write(Integer.toString(allTimeScore));
                writer.close();
            }
            catch (IOException ex) {
                System.out.println("Something went wrong: " + ex.getMessage());
            }
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        phoenixImage.dispose();
        blockImage.dispose();
        imageShort.dispose();
        longImage.dispose();
    }

    public static int getHighScore() {
        return highScore;
    }

}
