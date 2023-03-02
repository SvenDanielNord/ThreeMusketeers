package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import static com.mygdx.game.GameScreen.getHighscore;

public class DeathScreen implements Screen {

    final FlappyBird game;

    OrthographicCamera camera;

    Texture background;
    TextureRegion region;

    long screenStart;
    BitmapFont endFont;
    int score;
    int highscore = 0;

    public DeathScreen(FlappyBird game, int score) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1080, 720);
        background = new Texture(Gdx.files.internal("bgtest.png"));
        region = new TextureRegion(background, 1080,720);
        screenStart = TimeUtils.nanoTime();
        endFont = new BitmapFont();
        this.score = score;
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if(score > highscore) {
            highscore = score;
        }

        ScreenUtils.clear(0,0,0,1);
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(region,0,0);
        game.font.draw(game.batch, "You Died!", 500, 380);
        game.font.draw(game.batch, "Your score was: " + score, 500, 380);

        //writing highscore for this playing round on screen
        game.font.draw(game.batch, "Your highscore is: " + getHighscore(), 500, 300);
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && TimeUtils.nanoTime() - screenStart > 500000000) {
            game.setScreen((new MainMenuScreen(game)));
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
