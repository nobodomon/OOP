package com.mygdx.server.handlers;

import com.mygdx.game.handlers.CapturePointHandler;
import com.mygdx.global.GameRestartEvent;
import com.mygdx.global.GameStartEvent;
import com.mygdx.server.ServerFoundation;
import com.mygdx.server.supers.ServerPlayer;

public class GameHandler {
    public static final GameHandler INSTANCE = new GameHandler();

    public void startGame(){
        for(ServerPlayer all : PlayerHandler.INSTANCE.getPlayers()){
            final GameStartEvent gameStartEvent = new GameStartEvent();
            all.getConnection().sendTCP(gameStartEvent);
        }
    }

    public void restartGame(){
        for(ServerPlayer all : PlayerHandler.INSTANCE.getPlayers()){
            final GameRestartEvent gameRestartEvent = new GameRestartEvent();
            all.getConnection().sendTCP(gameRestartEvent);
        }
    }

}
