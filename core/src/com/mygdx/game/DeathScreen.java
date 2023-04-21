package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.utils.Levels;

@SuppressWarnings("java:S1186")
public class DeathScreen implements Screen {

    final FlappyBird game;

    OrthographicCamera camera;

    Texture background;
    TextureRegion region;

    long screenStart;
    BitmapFont endFont;
    int score;
    Levels level;


    public DeathScreen(FlappyBird game, int score, Levels level) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1080, 720);
        background = new Texture(Gdx.files.internal("bgtest.png"));
        region = new TextureRegion(background, 1080, 720);
        screenStart = TimeUtils.nanoTime();
        endFont = new BitmapFont();
        this.score = score;
        this.level = level;
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {


        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(region, 0, 0);
        game.fireFont.draw(game.batch, "You Died!", 400, 500);
        game.fireFont.draw(game.batch, "Your score was: " + score, 500, 380);

        //writing high score for this playing round on screen
        game.fireFont.draw(game.batch, "Your highscore is: " + HighScore.setHighScore(score, level), 500, 300);
        //writing best score ever
        game.fireFont.draw(game.batch, "Your best score ever is: " + HighScore.allTimeScore, 500, 220);
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && TimeUtils.nanoTime() - screenStart > 500000000) {
            game.setScreen((new MainMenuScreen(game, level)));
            dispose();
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

    }
}
