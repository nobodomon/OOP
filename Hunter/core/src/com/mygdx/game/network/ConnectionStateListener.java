package com.mygdx.game.network;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.handlers.PlayerHandler;
import com.mygdx.game.screens.ConnectScreen;

public class ConnectionStateListener extends Listener {

    @Override
    public void disconnected(Connection connection){
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                MyGdxGame.getInstance().setScreen(ConnectScreen.INSTANCE);
                ConnectScreen.INSTANCE.getErrorLabel().setText("Connection lost!");
                PlayerHandler.INSTANCE.clearPlayers();
                com.mygdx.server.handlers.PlayerHandler.INSTANCE.clearPlayers();
            }
        });
        super.disconnected(connection);
    }
}
