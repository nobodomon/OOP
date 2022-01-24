package com.mygdx.server.supers;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.mygdx.game.handlers.CapturePointHandler;
import com.mygdx.game.supers.PlayerState;

import java.util.Random;

public class ServerCapturePoint {
    private Connection connection;
    float[] randomX = new float[]{112.5f,212.5f,312.5f,412.5f,512.5f,612.5f,712.5f,812.5f,912.5f,1012.5f,1112.f};
    float[] randomY = new float[]{112.5f,212.5f,312.5f,412.5f,512.5f,612.5f,712.5f};
    private float x;
    private float y;
    private float progress;

    public boolean beingCaptured;

    public ServerCapturePoint(Connection connection){
        this.connection = connection;
        this.beingCaptured = false;
        do {
            x = randomX[new Random().nextInt(randomX.length)];
            y = randomY[new Random().nextInt(randomY.length)];
        }while(CapturePointHandler.instance.doesNewCapturePointOverlap(x ,y));
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Connection getConnection() {
        return connection;
    }

    public void update() {
        this.progress = progress;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }
}
