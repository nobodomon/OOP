package com.mygdx.server.handlers;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.mygdx.game.supers.Player;
import com.mygdx.global.CapturePointCreateEvent;
import com.mygdx.global.CapturePointDeleteEvent;
import com.mygdx.global.PlayerAddEvent;
import com.mygdx.global.PlayerRemoveEvent;
import com.mygdx.server.ServerFoundation;
import com.mygdx.server.supers.ServerCapturePoint;

import java.util.LinkedList;

public class CapturePointHandler {
    public static final CapturePointHandler INSTANCE = new CapturePointHandler();
    private LinkedList<ServerCapturePoint> capturePoints;

    public CapturePointHandler(){
        this.capturePoints = new LinkedList<>();
    }

    public ServerCapturePoint getCapturePointByConnection(final Connection connection){
        for(final ServerCapturePoint serverCapturePoint: this.capturePoints){
            if(serverCapturePoint.getConnection() == connection){
                return serverCapturePoint;
            }
        }
        return null;
    }

    public ServerCapturePoint getCapturePointByVector(final float x, final float y){
        for(final ServerCapturePoint serverCapturePoint: this.capturePoints){
            if(serverCapturePoint.getX() == x && serverCapturePoint.getY() == y){
                return serverCapturePoint;
            }
        }
        return null;
    }

    public void addCapturePoint(ServerCapturePoint capturePoint){
        for(ServerCapturePoint all : capturePoints){
            final CapturePointCreateEvent capturePointCreateEvent = new CapturePointCreateEvent();
            capturePointCreateEvent.progress = all.getProgress();
            capturePointCreateEvent.x = all.getX();
            capturePointCreateEvent.y = all.getY();

            capturePoint.getConnection().sendTCP(capturePointCreateEvent);
        }
        final CapturePointCreateEvent capturePointCreateEvent = new CapturePointCreateEvent();
        capturePointCreateEvent.progress = capturePoint.getProgress();
        capturePointCreateEvent.x = capturePoint.getX();
        capturePointCreateEvent.y = capturePoint.getY();

        ServerFoundation.instance.getServer().sendToAllTCP(capturePointCreateEvent);
        this.capturePoints.add(capturePoint);
    }

    public void removeCapturePoint(ServerCapturePoint capturePoint){
        this.capturePoints.remove(capturePoint);

        final CapturePointDeleteEvent capturePointDeleteEvent = new CapturePointDeleteEvent();
        capturePointDeleteEvent.x = capturePoint.getX();
        capturePointDeleteEvent.y = capturePoint.getY();

        ServerFoundation.instance.getServer().sendToAllTCP(capturePointDeleteEvent);
    }

    public void clearCapturePoints(){
        this.capturePoints = new LinkedList<>();
    }

    public LinkedList<ServerCapturePoint> getCapturePoints() {
        return capturePoints;
    }

    public void update(){
        for(int i = 0; i < this.capturePoints.size(); i++){
            this.capturePoints.get(i).update();
        }
    }
}
