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

import java.awt.*;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class GameScreen implements Screen {

    final FlappyBird game;
    SpriteBatch batch;
    /**
     * bird image
     */
    Texture background;
    Texture customerImage;
    Texture blockImage;
    Texture longImage;
    Texture imageShort;
    OrthographicCamera camera;
    Rectangle customer;
    Rectangle block;
    Rectangle block2;


    Array<Rectangle> blockBank;
    long lastBlock;
    int score;




    public GameScreen(FlappyBird game) {
        this.game = game;

        /**
         * Loading image (64*64) for customer
         */
        background = new Texture(Gdx.files.internal("bgtest.png"));
        customerImage = new Texture(Gdx.files.internal("Phoenix.gif"));
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
        customer = new Rectangle();
        customer.x = 800 / 2 - 64 / 2;
        customer.y = 220;
        customer.width = 64;
        customer.height = 64;

        blockBank = new Array<Rectangle>();

        score = 0;


    }
        private void spawnBlocks() {
            block = new Rectangle();
            block.x = 1000;
            block.y = 0;

            block2 = new Rectangle();
            block2.x = 1000;
            block2.y = 480 - 144;

            int random = ThreadLocalRandom.current().nextInt(4) + 1;
            if (random == 1) {
                block.width = 70;
                block.height = 144;
                block2.width = 70;
                block2.height = 144;
            }

            else if (random == 2) {
            block.x = 1000;
            block.y = 480 - 211;
            block.width = 104;
            block.height = 311;

            block2.x = 1000;
            block2.y = 0 - 211;
            block2.width = 104;
            block2.height = 311;

            }
            else {
                block.x = 1000;
                block.y = 0 - 111;
                block.width = 104;
                block.height = 311;

                block2.x = 1000;
                block2.y = 480 - 111;
                block2.width = 104;
                block2.height = 311;
            }
            blockBank.add(block);
            blockBank.add(block2);
            lastBlock = TimeUtils.nanoTime();
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
         * Start new batch with instruction message to customer and star position for box
         */
        game.batch.begin();
        game.batch.draw(background,0,0);

        game.batch.draw(customerImage, customer.x, customer.y, customer.width, customer.height);

        for (Rectangle block : blockBank) {
            if (block.getHeight() == 311) {
                game.batch.draw(longImage, block.x, block.y);
            }else {
                game.batch.draw(blockImage, block.x, block.y);
            }
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
            customer.x = (int) (startPas.x - 64 / 2);
        }
        /**
         * Input to jump, press space key
         */
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isTouched(Input.Buttons.LEFT)) {
            customer.y += 800 * Gdx.graphics.getDeltaTime();

            if (customer.y < 0)
                customer.y = 0;
            if (customer.y > 480 - 64)
                customer.y = 480 - 64;
        }
        /**
         * Bird sinking time
         */
        customer.y -= 150 * Gdx.graphics.getDeltaTime();

        if (TimeUtils.nanoTime() - lastBlock > 1000000000) {
            score = score + 100;
            spawnBlocks();
        }

        Iterator<Rectangle> iter = blockBank.iterator();
        while (iter.hasNext()) {
            Rectangle block = iter.next();
            block.x -= (200 * Gdx.graphics.getDeltaTime());

            if (block.intersects(customer)) {
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
        customerImage.dispose();
        blockImage.dispose();
        imageShort.dispose();
        longImage.dispose();
    }
}
