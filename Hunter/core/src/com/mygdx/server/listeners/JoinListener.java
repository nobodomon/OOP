package com.mygdx.server.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.global.JoinRequestEvent;
import com.mygdx.server.handlers.PlayerHandler;
import com.mygdx.server.supers.ServerPlayer;

public class JoinListener extends Listener {

    @Override
    public void received(Connection connection, Object object) {

        // Join Request
        if(object instanceof JoinRequestEvent){
            final JoinRequestEvent joinRequestEvent = (JoinRequestEvent) object;

            final ServerPlayer serverPlayer = new ServerPlayer(joinRequestEvent.username, connection);
            serverPlayer.setX(500);
            serverPlayer.setY(500);

            PlayerHandler.INSTANCE.addPlayer(serverPlayer);
        }

        super.received(connection, object);
    }
}
