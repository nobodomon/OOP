package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.handlers.MoveUpdateHandler;
import com.mygdx.game.handlers.PlayerHandler;
import com.mygdx.game.handlers.ResourceHandler;
import com.mygdx.game.supers.Player;
import com.mygdx.global.MoveUpdateEvent;

public class WaitingScreen implements Screen {

    public static final WaitingScreen INSTANCE = new WaitingScreen();

    private final SpriteBatch batch;

    public WaitingScreen(){
        this.batch = new SpriteBatch();
        this.batch.setProjectionMatrix(MyGdxGame.getInstance().getCamera().combined);
    }

    @Override
    public void show() {
        MoveUpdateHandler.INSTANCE.start();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        this.batch.begin();
        // x axis render
        for(int x = 0; x< Gdx.graphics.getWidth() / ResourceHandler.INSTANCE.grass.getWidth(); x++){
            // y axis render
            for(int y = 0; y< Gdx.graphics.getHeight() / ResourceHandler.INSTANCE.grass.getHeight(); y++){
                this.batch.draw(ResourceHandler.INSTANCE.grass, ResourceHandler.INSTANCE.grass.getWidth() * x,  ResourceHandler.INSTANCE.grass.getHeight() * y);
            }
        }

        PlayerHandler.INSTANCE.render(this.batch);
        PlayerHandler.INSTANCE.update(delta);

        this.batch.end();
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
        MoveUpdateHandler.INSTANCE.stop();
    }

    @Override
    public void dispose() {

    }
}
