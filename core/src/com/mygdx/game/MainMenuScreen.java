package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final FlappyBird game;

    OrthographicCamera camera;

    public MainMenuScreen(FlappyBird Game) {
        this.game = Game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }
    @Override
    public void show() {

    }

    /**
     * Main screen settings. Colors and coordinates for start screen.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 50, 1);     //changing background image

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Flappy birb", 380, 220);
        game.font.draw(game.batch, "Press ENTER to play game", 380, 160);
        game.batch.end();

        /**
         * Press enter to play game, space starting game direct
         */
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            game.setScreen((new GameScreen(game)));
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
