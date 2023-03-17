package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.utils.Levels;


import java.io.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class GameScreen implements Screen {

    final FlappyBird game;
    Animation<TextureRegion> flapAnimation;



    SpriteBatch batch;
    /**
     * bird image
     */
    Texture flapSheet;
    Texture background;
    Texture background2;
    Texture phoenixImage;
    Texture blockImage;
    Texture longImage;
    Texture imageShort;
    OrthographicCamera camera;
    Rectangle phoenix;
    Rectangle block;
    Rectangle block2;
    Levels level;
    Array<Rectangle> blockBank;
    Array<Texture> textureBank;
    long lastBlock;
    long speed;
    long spawnTime;
    final int phoenixCols = 2;
    final int phoenixRows = 1;
    float backgroundMove;
    float backgroundMove2;

    int score;
    int flap;
    float stateTime;



    //highscore for this playing round
//    static int highScore;
//
//    static int allTimeScore;


    public GameScreen(FlappyBird game, Levels level) {
        this.game = game;
        this.level = level;


        setLevel(level);

        createSheet();
        setTextures();

        /**
         * Loading image (64*64) for customer
         */

        backgroundMove = 0;
        backgroundMove2 = 1080;


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
        phoenix.y = 220L;
        phoenix.width = 60;
        phoenix.height = 60;




        blockBank = new Array<Rectangle>();

        score = 0;


    }

    private void setTextures(){
        phoenixImage = new Texture(Gdx.files.internal("Phoenix.gif"));
        longImage = new Texture(Gdx.files.internal("rectangleorange.png"));
        background2 = new Texture(Gdx.files.internal("bgtest.png"));
        background = new Texture(Gdx.files.internal("bgreverse.png"));

    }
    private void setLevel(Levels level){
        if (level == Levels.EASY){
            speed = 100L;
            spawnTime = 4000000000L;
        }else if (level == Levels.HARD){
            speed = 350L;
            spawnTime = 1000000000L;
        }else {
            speed = 200L;
            spawnTime = 1500000000L;
        }
    }

    private void createSheet(){
        flapSheet = new Texture(Gdx.files.internal("Jumpy_Birb.png"));
        TextureRegion[][] tmp = TextureRegion.split(flapSheet,flapSheet.getWidth()/ phoenixCols, flapSheet.getHeight()/phoenixRows);
        TextureRegion[] flapFrames = new TextureRegion[phoenixCols* phoenixRows];
        int index = 0;
        for (int i = 0; i < phoenixRows; i++) {
            for (int j = 0; j < phoenixCols; j++) {
                flapFrames[index++] = tmp[i][j];
            }
        }
        flapAnimation = new Animation<TextureRegion>(0.25f, flapFrames);

        stateTime = 0f;

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
            block.y = 0 - 155;
            block2.x = 1000;
            block2.y = 480 - 155;
        } else if (random == 2) {
            block.x = 1000;
            block.y = 480 - 260;
            block2.x = 1000;
            block2.y = 0 - 251 ;

        } else {
            block.x = 1000;
            block.y = 0 - 91;
            block2.x = 1000;
            block2.y = 480 - 91;

        }
    }


    @Override
    public void show() {

    }

//    public int getHighScoreAllTime() {
//        return allTimeScore;
//    }

    @Override
    public void render(float delta) {
        /**
         * Setting new color for background
         */

        ScreenUtils.clear(0, 50, 0, 1);
        stateTime += Gdx.graphics.getDeltaTime();

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        /**
         * Start new batch with instruction message to customer and star position for box
         */
        TextureRegion currentFrame = flapAnimation.getKeyFrame(stateTime,true);
        game.batch.begin();
        game.batch.draw(background,backgroundMove,0);
        game.batch.draw(background2,backgroundMove2,0);
        if (backgroundMove < -1080){
            backgroundMove = 0;
            backgroundMove2 = 1080;
        }


        game.batch.draw(currentFrame,phoenix.x + 4, phoenix.y + 4,phoenix.getWidth(),phoenix.getHeight());
        //game.batch.draw(phoenixImage, phoenix.x, phoenix.y, phoenix.width, phoenix.height);

        for (Rectangle block : blockBank) {
            game.batch.draw(longImage, block.x, block.y);
        }

        game.fireFont.draw(game.batch, "Score: " + score, 380, 480);
        game.batch.end();
        /**
         * Input to start the game
         */

        if (Gdx.input.isTouched()) {
            Vector3 startPas = new Vector3();
            startPas.set(540, 220  , 0);
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
        backgroundMove -= 100 * Gdx.graphics.getDeltaTime();
        backgroundMove2 -= 100 * Gdx.graphics.getDeltaTime();

        if (TimeUtils.nanoTime() - lastBlock > spawnTime) {
            score = score + 100;
            spawnBlocks();
        }

        Iterator<Rectangle> iter = blockBank.iterator();
        while (iter.hasNext()) {
            Rectangle block = iter.next();
            block.x -= (speed * Gdx.graphics.getDeltaTime());

            if (block.overlaps(phoenix)) {

                game.setScreen((new DeathScreen(game, score)));
            }
            if (block.getX() < -150) {
                iter.remove();
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
        background.dispose();
    }



}
