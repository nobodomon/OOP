package com.mygdx.game.network;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.handlers.PlayerHandler;
import com.mygdx.game.supers.Player;
import com.mygdx.global.PlayerAddEvent;
import com.mygdx.global.PlayerRemoveEvent;
import com.mygdx.global.PlayerUpdateEvent;

public class EventListener extends Listener {
    @Override
    public void received(Connection connection, Object object) {
        if(object instanceof PlayerAddEvent){
            PlayerAddEvent playerAddEvent = (PlayerAddEvent) object;
            final Player player = new Player(playerAddEvent.username,playerAddEvent.color);
            player.getPosition().x = playerAddEvent.x;
            player.getPosition().y = playerAddEvent.y;
            player.getServerPosition().x = playerAddEvent.x;
            player.getServerPosition().y = playerAddEvent.y;

            PlayerHandler.INSTANCE.addPlayer(player);
            Gdx.app.log("Info", "added");

        }else if(object instanceof PlayerRemoveEvent){

        }else if(object instanceof PlayerUpdateEvent){

        }

        super.received(connection, object);
    }
}
