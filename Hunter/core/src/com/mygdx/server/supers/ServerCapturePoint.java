package com.mygdx.server.supers;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.mygdx.game.supers.PlayerState;

public class ServerCapturePoint {
    private Connection connection;

    private float x;
    private float y;
    private float progress;

    public boolean beingCaptured;

    public ServerCapturePoint(Connection connection){
        this.progress = 0;
        this.connection = connection;
        this.beingCaptured = false;
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
