package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.utils.Levels;

@SuppressWarnings("java:S1186")
public class MainMenuScreen implements Screen {
    final int buttonWidth = 100;
    final int buttonHeight = 40;

    final int buttonX = 250;

    final FlappyBird game;
    Levels level;
    OrthographicCamera camera;
    long delay;
    /**
     * welcome screen
     */
    //Texture welcome;
    Texture welcomeBird;
    Texture spaceToPlay;
    Texture xToExit;

    /**
     * background pic
     */
    Texture backgroundWelcome;
    Texture chooseLevel;
    Texture medium;
    Texture easy;
    Texture hard;
    Texture activatedMedium;
    Texture activatedEasy;
    Texture activatedHard;
    Rectangle boxHard;
    Rectangle boxMedium;
    Rectangle boxEasy;
    private long id;
    private Sound music;


    public MainMenuScreen(FlappyBird Game, Levels level) {
        this.game = Game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        delay = TimeUtils.nanoTime();
        /**
         * welcome screen
         */
        backgroundWelcome = new Texture(Gdx.files.internal("bgtest.png"));
       // welcome = new Texture(Gdx.files.internal("welcome.png"));
        welcomeBird = new Texture(Gdx.files.internal("jumpybird.png"));
        chooseLevel = new Texture(Gdx.files.internal("chooselevel.png"));
        spaceToPlay = new Texture(Gdx.files.internal("pressspace.png"));
        xToExit = new Texture(Gdx.files.internal("pressx.png"));

        medium = new Texture(Gdx.files.internal("medium.png"));
        easy = new Texture(Gdx.files.internal("easy.png"));
        hard = new Texture(Gdx.files.internal("hard.png"));
        activatedHard = new Texture(Gdx.files.internal("activatedHard.png"));
        activatedMedium = new Texture(Gdx.files.internal("activatedMedium.png"));
        activatedEasy = new Texture(Gdx.files.internal("activatedEasy.png"));
        boxHard = new Rectangle(250, 240, 100, 40);
        boxMedium = new Rectangle(250, 190, 100, 40);
        boxEasy = new Rectangle(250, 140, 100, 40);
        this.level = level;
        if (level == null) {
            this.level = Levels.MEDIUM;

        }
        music = Gdx.audio.newSound(Gdx.files.internal("start_music.mp3"));
        id = music.play(0.1f);
        music.setLooping(id, true);
    }

    @Override
    public void show() {

    }

    /**
     * Main screen settings. Colors and coordinates for start screen.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);     //changing background image

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        //game.fireFont.draw(game.batch, "Press SPACE to play game", 380, 160);
        //game.fireFont.draw(game.batch, "Press X to exit", 380, 100);

        /**
         * Draw welcome text
         */
        game.batch.draw(backgroundWelcome, (buttonX-300), 0);
        //game.batch.draw(welcome, (buttonX - 50), 410);
        game.batch.draw(welcomeBird, (buttonX - 110), 350);
        game.batch.draw(chooseLevel, (buttonX - 110), 285);
        game.batch.draw(spaceToPlay, (buttonX - 110), 35);
        game.batch.draw(xToExit, (buttonX - 110), 0);

        int x = 800 / 2 - buttonWidth / 2;

        if (level == Levels.MEDIUM) {
            game.batch.draw(activatedMedium, buttonX, 190);
        } else {
            game.batch.draw(medium, buttonX, 190);
        }
        if (level == Levels.HARD) {
            game.batch.draw(activatedHard, buttonX, 240);
        } else {
            game.batch.draw(hard, buttonX, 240);
        }
        if (level == Levels.EASY) {
            game.batch.draw(activatedEasy, buttonX, 140);
        } else {
            game.batch.draw(easy, buttonX, 140);
        }

        if (Gdx.input.getX() < x + buttonWidth && Gdx.input.getX() > x && 480 - Gdx.input.getY() < 220 - buttonHeight && 480 - Gdx.input.getY() > 120) {
            if (Gdx.input.isTouched())
                level = Levels.HARD;
        }

        if (Gdx.input.getX() < x + buttonWidth && Gdx.input.getX() > x && 480 - Gdx.input.getY() < 150 - buttonHeight && 480 - Gdx.input.getY() > 50) {
            if (Gdx.input.isTouched())
                level = Levels.MEDIUM;
        }

        if (Gdx.input.getX() < x + buttonWidth && Gdx.input.getX() > x && 480 - Gdx.input.getY() < 70 - buttonHeight && 480 - Gdx.input.getY() > -30) {
            if (Gdx.input.isTouched())
                level = Levels.EASY;
        }


        game.batch.end();


        /**
         * Press space to play game
         */
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (TimeUtils.nanoTime() - delay > 500000000) {
                music.stop(id);
                game.setScreen((new GameScreen(game, level)));
                dispose();
            }
        }

        /**
         * Press x to exit
         */
        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            Gdx.app.exit();
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
        music.dispose();
        backgroundWelcome.dispose();
        welcomeBird.dispose();
        activatedEasy.dispose();
        activatedMedium.dispose();
        activatedHard.dispose();
        hard.dispose();
        medium.dispose();
        easy.dispose();
        chooseLevel.dispose();
        xToExit.dispose();
        spaceToPlay.dispose();
    }
}
