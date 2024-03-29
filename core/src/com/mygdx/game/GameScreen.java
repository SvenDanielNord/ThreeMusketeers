package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.utils.Levels;


import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("java:S1186")
public class GameScreen implements Screen {

    final FlappyBird game;
    Animation<TextureRegion> flapAnimation;


    SpriteBatch batch;
    /**
     * bird image
     */
    Texture flapSheet;
    Texture flap;
    Texture glide;
    Texture background;
    Texture background2;
    Texture phoenixImage;
    Texture blockImage;
    Texture longImage;
    Texture imageShort;
    Texture cloud;
    Texture cars;
    Texture cars2;
    OrthographicCamera camera;
    Rectangle phoenix;
    Rectangle block;
    Rectangle block2;
    Levels level;
    Array<Rectangle> blockBank;
    long lastBlock;
    long speed;
    long spawnTime;
    final int phoenixCols = 2;
    final int phoenixRows = 1;
    float backgroundMove;
    float backgroundMove2;
    int gravityDownY = 200;
    int score;
    int frames = 0;
    long pause;
    float stateTime;

    boolean shouldFlap = false;
    private Sound jumpSound;
    private Sound death;


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

        backgroundMove = 800;
        backgroundMove2 = 1400;


        /**
         * Creating camera and start batch
         */
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        /**
         * Creating player box and setting coordinates for it
         */
        phoenix = new Rectangle();
        phoenix.x = (float) 800 / 2 - (float) 64 / 2;
        phoenix.y = 220;
        phoenix.width = 50;
        phoenix.height = 40;


        blockBank = new Array<Rectangle>();

        score = 0;

        /**
         * Creating jumping sound
         */
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("wingFlap.ogg"));
        death = Gdx.audio.newSound(Gdx.files.internal("death.mp3"));

    }

    private void setTextures() {

        longImage = new Texture(Gdx.files.internal("rectangleblue.png"));

        background = new Texture(Gdx.files.internal("bgreverse.png"));
        flap = new Texture(Gdx.files.internal("up.png"));
        glide = new Texture(Gdx.files.internal("down.png"));
        cars = new Texture(Gdx.files.internal("cars_1.png"));
        cars2 = new Texture(Gdx.files.internal("cars_2.png"));
//        box = new Texture(Gdx.files.internal("hitbox.png"));
    }

    /**
     * easy level - bird speed is slower
     * @param level
     */
    private void setLevel(Levels level) {
        if (level == Levels.EASY) {
            speed = 100L;
            spawnTime = 4000000000L;
            /**
             * medium level - bird speed is fastest
             */
        } else if (level == Levels.HARD) {
            speed = 350L;
            spawnTime = 1000000000L;
            /**
             * hard level - bird speed is faster
             */
        } else {
            speed = 200L;
            spawnTime = 1500000000L;
        }
    }

    private void createSheet() {
        flapSheet = new Texture(Gdx.files.internal("Jumpy_Birb.png"));
        TextureRegion[][] tmp = TextureRegion.split(flapSheet, flapSheet.getWidth() / phoenixCols, flapSheet.getHeight() / phoenixRows);
        TextureRegion[] flapFrames = new TextureRegion[phoenixCols * phoenixRows];
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
        block.width = 90;
        block.height = 290;

        block2 = new Rectangle();
        block2.width = 90;
        block2.height = 280;

        setBlockPosition();
        blockBank.add(block);
        blockBank.add(block2);
        lastBlock = TimeUtils.nanoTime();
    }

    private void setBlockPosition() {
        int random = ThreadLocalRandom.current().nextInt(3) + 1;

        if (random == 1) {
            block.x = 1000;
            block.y = (float)0 - 155;
            block2.x = 1000;
            block2.y = (float)480 - 155;
        } else if (random == 2) {
            block.x = 1000;
            block.y = (float)480 - 260;
            block2.x = 1000;
            block2.y = (float)0 - 251;

        } else {
            block.x = 1000;
            block.y = (float)0 - 91;
            block2.x = 1000;
            block2.y = (float)480 - 91;

        }
    }
    private void rectangleIterator() {
        Iterator<Rectangle> iter = blockBank.iterator();
        while (iter.hasNext()) {
            Rectangle block = iter.next();
            block.x -= (speed * Gdx.graphics.getDeltaTime());

            if (block.overlaps(phoenix) || phoenix.y < -64 || phoenix.y > 480) {
                death.play(0.1F);
                HighScore.separateHighscores(level);
                pause();
                game.setScreen((new DeathScreen(game, score, level)));

            }
            if (block.getX() < -150) {
                iter.remove();
            }
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
        TextureRegion currentFrame = flapAnimation.getKeyFrame(stateTime, true);
        game.batch.begin();
        game.batch.draw(background, 0, 0);
        game.batch.draw(cars ,backgroundMove ,0);
        game.batch.draw(cars2 ,backgroundMove2 ,0);


        if (backgroundMove < -460) {
            backgroundMove = 800;

        }
        if (backgroundMove2 < -480){
            backgroundMove2 = 800;
        }
       if (shouldFlap){
            game.batch.draw(flap, phoenix.x - 7, phoenix.y - 12, phoenix.width + 14, phoenix.height + 24);
       }else{
            game.batch.draw(glide, phoenix.x - 7, phoenix.y - 12, phoenix.width + 14, phoenix.height + 24);
        }
//        game.batch.draw(box, phoenix.x, phoenix.y, phoenix.width, phoenix.height);


        //game.batch.draw(currentFrame, phoenix.x + 4, phoenix.y + 4, phoenix.getWidth(), phoenix.getHeight());


        for (Rectangle block : blockBank) {
            game.batch.draw(longImage, block.x - 10, block.y);
        }

        game.fireFont.draw(game.batch, "Score: " + score, 380, 480);
        game.batch.end();
        /**
         * Input to start the game
         */

        if (Gdx.input.isTouched()) {
            Vector3 startPas = new Vector3();
            startPas.set(540, 220, 0);
            camera.unproject(startPas);
            phoenix.x = (float) (startPas.x - (double)64 / 2);
        }
        /**
         * Input to jump, press space key, Playing jump sound also
         */
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) ) {
            frames = 23;
            jumpSound.play(1.0f);
        }
        if (frames > 0 && frames < 7){
            phoenix.y += 110 * Gdx.graphics.getDeltaTime();
            shouldFlap = true;
            frames--;
        }
        else if (frames > 7) {
            phoenix.y += 300 * Gdx.graphics.getDeltaTime();
            shouldFlap = true;
            frames--;
        }else{
            shouldFlap = false;
            phoenix.y -= gravityDownY * Gdx.graphics.getDeltaTime();
        }
        /**
         * Bird sinking time
         */



        backgroundMove -= 300 * Gdx.graphics.getDeltaTime();
        backgroundMove2 -= 300 * Gdx.graphics.getDeltaTime();

        if (TimeUtils.nanoTime() - lastBlock > spawnTime) {
            score = score + 100;
            spawnBlocks();
        }

        rectangleIterator();


    }



    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        pause = TimeUtils.nanoTime();
        while(TimeUtils.nanoTime() - pause < 500000000);

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
