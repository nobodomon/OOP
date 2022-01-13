package com.mygdx.game.network;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.screens.ConnectScreen;
import com.mygdx.game.screens.IngameScreen;

public class ConnectionStateListener extends Listener {
    @Override
    public void connected(Connection connection){
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                MyGdxGame.getInstance().setScreen(IngameScreen.INSTANCE);
            }
        });
        super.connected(connection);
    }

    @Override
    public void disconnected(Connection connection){
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                MyGdxGame.getInstance().setScreen(ConnectScreen.INSTANCE);
                ConnectScreen.INSTANCE.getErrorLabel().setText("Connection lost!");
            }
        });
        super.disconnected(connection);
    }
}
