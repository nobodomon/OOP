package com.mygdx.server.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.screens.ConnectScreen;
import com.mygdx.game.screens.IngameScreen;
import com.mygdx.global.JoinRequestEvent;
import com.mygdx.global.JoinResponseEvent;
import com.mygdx.server.handlers.PlayerHandler;
import com.mygdx.server.supers.ServerPlayer;

import java.util.Random;

public class JoinListener extends Listener {

    @Override
    public void received(Connection connection, Object object) {

        // Join Request
        if(object instanceof JoinRequestEvent){
            final JoinRequestEvent joinRequestEvent = (JoinRequestEvent) object;

            final ServerPlayer serverPlayer = new ServerPlayer(joinRequestEvent.username, connection);

            final Random random = new Random();

            serverPlayer.setX(random.nextInt(700));
            serverPlayer.setY(random.nextInt(500));

            // Name already exist
            if(PlayerHandler.INSTANCE.getPlayerByUsername(((JoinRequestEvent) object).username) != null){
                return;
            }else{

                PlayerHandler.INSTANCE.addPlayer(serverPlayer);

                final JoinResponseEvent joinResponseEvent = new JoinResponseEvent();
                connection.sendTCP(joinResponseEvent);
                //IngameScreen.INSTANCE.incrementPlayerCount();
            }

            super.received(connection, object);
        }

    }
}
