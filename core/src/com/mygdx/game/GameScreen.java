package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
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

import java.awt.*;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;

public class GameScreen implements Screen {

    final FlappyBird game;
    SpriteBatch batch;
    /**
     * bird image
     */
    Texture customerImage;
    Texture blockImage;
    Texture longImage;
    Texture imageShort;
    OrthographicCamera camera;
    Rectangle customer;
    Rectangle block;
    Rectangle block2;
    Rectangle blockLong;
    Rectangle block88;

    Array<Rectangle> blockBank;
    long lastBlock;



    public GameScreen(FlappyBird game) {
        this.game = game;

        /**
         * Loading image (64*64) for customer
         */
        customerImage = new Texture(Gdx.files.internal("kund.png"));
        blockImage = new Texture(Gdx.files.internal("block.png"));
        longImage = new Texture(Gdx.files.internal("blockLong.png"));
        imageShort = new Texture(Gdx.files.internal("block88.png"));



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

        blockBank = new Array<Rectangle>();
        createBlocks();
        spawnBlocks();
    }
        private void spawnBlocks() {
            int random = ThreadLocalRandom.current().nextInt(4) + 1;
            if (random == 1) {
            block = new Rectangle();
            block.x = 1000;
            block.y = 0;
            block.width = 70;
            block.height = 144;
            blockBank.add(block);
            lastBlock = TimeUtils.nanoTime();

            block2 = new Rectangle();
            block2.x = 1000;
            block2.y = 480 - 144;
            block2.width = 70;
            block2.height = 144;
            blockBank.add(block2);
            lastBlock = TimeUtils.nanoTime();
            }

            else if (random == 2) {
            blockLong = new Rectangle();
            blockLong.x = 1000;
            blockLong.y = 0;
            blockLong.width = 70;
            blockLong.height = 200;
            blockBank.add(blockLong);
            lastBlock = TimeUtils.nanoTime();

            block88 = new Rectangle();
            block88.x = 1000;
            block88.y = 480 - 88;
            block88.width = 70;
            block88.height = 88;
            blockBank.add(block88);
            lastBlock = TimeUtils.nanoTime();
            }
            else {
                blockLong = new Rectangle();
                blockLong.x = 1000;
                blockLong.y = 0;
                blockLong.width = 70;
                blockLong.height = 88;
                blockBank.add(blockLong);
                lastBlock = TimeUtils.nanoTime();

                block88 = new Rectangle();
                block88.x = 1000;
                block88.y = 480 - 200;
                block88.width = 88;
                block88.height = 200;
                blockBank.add(block88);
                lastBlock = TimeUtils.nanoTime();
            }
        }

        public void createBlocks() {
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

        for (Rectangle block : blockBank) {
            if (block.getHeight() == 200) {
                game.batch.draw(longImage, block.x, block.y);
            } else if (block.getHeight() == 88) {
                game.batch.draw(imageShort, block.x, block.y);
            }else {
                game.batch.draw(blockImage, block.x, block.y);
            }
        }

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
         * Input to jump, press space key
         */
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            customer.y += 800 * Gdx.graphics.getDeltaTime();

            if (customer.y < 0)
                customer.y = 0;
            if (customer.y > 480 - 64)
                customer.y = 480 - 64;
        }
        /**
         * Drop time for the bird
         */
        customer.y -= 10 * Gdx.graphics.getDeltaTime();

        if (TimeUtils.nanoTime() - lastBlock > 1000000000) {
            spawnBlocks();
        }

        Iterator<Rectangle> iter = blockBank.iterator();
        while (iter.hasNext()) {
            Rectangle block = iter.next();
            block.x -= (200 * Gdx.graphics.getDeltaTime());

            if (block.intersects(customer)) {
                game.setScreen((new MainMenuScreen(game)));
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
        customerImage.dispose();
        blockImage.dispose();
        imageShort.dispose();
        longImage.dispose();
    }
}
