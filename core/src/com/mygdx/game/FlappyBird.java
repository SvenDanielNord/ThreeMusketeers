package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class FlappyBird extends Game {
	SpriteBatch batch;
	public BitmapFont fireFont;

	/**
	 *Creating new Sprite batch and font to make it possible to control this screen
	 */
	@Override
	public void create () {
		batch = new SpriteBatch();
		fireFont = new BitmapFont(Gdx.files.internal("bitfont.fnt"));
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		fireFont.dispose();
	}
}
