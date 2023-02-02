package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.*;
import java.util.Vector;

public class GameScreen implements Screen {

    final FlappyBird game;
    SpriteBatch batch;
    /**
     * bird image
     */
    Texture customerImage;
    OrthographicCamera camera;
    Rectangle customer;


    public GameScreen(FlappyBird game) {
        this.game = game;

        /**
         * Loading image (64*64) for customer
         */
        customerImage = new Texture(Gdx.files.internal("kund.png"));

        /**
         * Creating camera and start batch
         */
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        /**
         * Creating player box and setting cooridates for it
         */
        customer = new Rectangle();
        customer.x = 800 / 2 - 64 / 2;
        customer.y = 220;
        customer.width = 64;
        customer.height = 64;
    }

    @Override
    public void show() {

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
         * Start new batch with instruction message to customer and star postion for box
         */
        game.batch.begin();
        game.font.draw(game.batch, "Hoppa Hampus", 380, 480);
        game.batch.draw(customerImage, customer.x, customer.y, customer.width, customer.height);
        game.batch.end();
        /**
         * Input to start the game
         */
        if (Gdx.input.isTouched()) {
            Vector3 startPas = new Vector3();
            startPas.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(startPas);
            customer.x = (int) (startPas.x - 64 / 2);
        }
        /**
         * Input to jump
         */
        if (Gdx.input.isKeyPressed(62)) {
            customer.y += 400 * Gdx.graphics.getDeltaTime();

            if (customer.y < 0)
                customer.y = 0;
            if (customer.y > 480 - 64)
                customer.y = 480 - 64;
        }
        /**
         * Drop time for the bird
         */
        customer.y -= 10 * Gdx.graphics.getDeltaTime();
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
        customerImage.dispose();
    }
}
