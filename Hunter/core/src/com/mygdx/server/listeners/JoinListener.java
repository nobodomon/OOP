package com.mygdx.server.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.screens.ConnectScreen;
import com.mygdx.game.supers.PlayerType;
import com.mygdx.global.JoinRequestEvent;
import com.mygdx.global.JoinResponseEvent;
import com.mygdx.server.handlers.PlayerHandler;
import com.mygdx.server.handlers.CapturePointHandler;
import com.mygdx.server.supers.ServerCapturePoint;
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

            serverPlayer.setX(random.nextInt(1100));
            serverPlayer.setY(random.nextInt(700));

            final ServerCapturePoint serverCapturePoint = new ServerCapturePoint(connection);

            System.out.println("Capture Point created");
            // Name already exist
            if(PlayerHandler.INSTANCE.getPlayerByUsername(((JoinRequestEvent) object).username) != null){
                ConnectScreen.INSTANCE.getErrorLabel().setText("Username is taken!");
                return;
            }

                PlayerHandler.INSTANCE.addPlayer(serverPlayer);
                CapturePointHandler.INSTANCE.addCapturePoint(serverCapturePoint);

                final JoinResponseEvent joinResponseEvent = new JoinResponseEvent();
                joinResponseEvent.username = joinRequestEvent.username;
                connection.sendTCP(joinResponseEvent);


        }
        super.received(connection, object);

    }
}
