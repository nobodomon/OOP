package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.esotericsoftware.kryonet.Client;
import com.mygdx.game.screens.ConnectScreen;

public class MyGdxGame extends Game {

	private OrthographicCamera camera;

	private Client client;

	@Override
	public void create () {
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false,1200,800);

		super.setScreen(ConnectScreen.INSTANCE);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();
	}
	
	@Override
	public void dispose () {
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Client getClient() {
		return client;
	}

	public static MyGdxGame getInstance(){
		return (MyGdxGame) Gdx.app.getApplicationListener();
	}
}
