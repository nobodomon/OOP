package com.mygdx.server.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.global.PlayerAddEvent;
import com.mygdx.global.PlayerRemoveEvent;
import com.mygdx.server.ServerFoundation;
import com.mygdx.server.supers.ServerPlayer;

import java.util.LinkedList;

public class PlayerHandler {
    public static final PlayerHandler INSTANCE = new PlayerHandler();

    private final LinkedList<ServerPlayer> players;

    public PlayerHandler(){
        this.players = new LinkedList<>();
    }

    public void update(){
        for(int i = 0; i < this.players.size(); i++){
            this.players.get(i).update();
        }
    }

    public void addPlayer(final ServerPlayer serverPlayer){

        final PlayerAddEvent playerAddEvent = new PlayerAddEvent();
        playerAddEvent.username = serverPlayer.getUsername();
        playerAddEvent.color = Color.WHITE;
        playerAddEvent.x = serverPlayer.getX();
        playerAddEvent.y = serverPlayer.getY();


        ServerFoundation.instance.getServer().sendToAllTCP(playerAddEvent);

        this.players.add(serverPlayer);
        Gdx.app.log("Info","Added Player: " + serverPlayer.getUsername());
    }

    public void removePlayer(final ServerPlayer serverPlayer){
        this.players.remove(serverPlayer);

        final PlayerRemoveEvent playerRemoveEvent = new PlayerRemoveEvent();
        playerRemoveEvent.username = serverPlayer.getUsername();

        ServerFoundation.instance.getServer().sendToAllTCP(playerRemoveEvent);
    }



    public LinkedList<ServerPlayer> getPlayers() {
        return players;
    }
}
