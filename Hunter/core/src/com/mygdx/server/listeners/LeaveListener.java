package com.mygdx.server.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.server.handlers.CapturePointHandler;
import com.mygdx.server.handlers.PlayerHandler;
import com.mygdx.server.supers.ServerCapturePoint;
import com.mygdx.server.supers.ServerPlayer;

import java.util.LinkedList;

public class LeaveListener extends Listener {
    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);

        final ServerPlayer leavePlayer = PlayerHandler.INSTANCE.getPlayerByConnection(connection);
        final ServerCapturePoint removedPoint = CapturePointHandler.INSTANCE.getCapturePointByConnection(connection);
        if(leavePlayer == null) return;

        if(removedPoint == null) return;

        PlayerHandler.INSTANCE.removePlayer(leavePlayer);
        CapturePointHandler.INSTANCE.removeCapturePoint(removedPoint);
    }
}
