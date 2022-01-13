package com.mygdx.server.supers;

import com.esotericsoftware.kryonet.Connection;

public class ServerPlayer {

    private final String username;
    private final Connection connection;

    private boolean moveUp, moveDown, moveLeft, moveRight;

    private float x;
    private float y;

    public ServerPlayer(String username, Connection connection){
        this.username = username;
        this.connection = connection;
    }

    public void update(){

    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getUsername() {
        return username;
    }

}
