package com.mygdx.server.supers;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Connection;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.screens.IngameScreen;
import com.mygdx.game.supers.Player;
import com.mygdx.game.supers.PlayerState;
import com.mygdx.game.supers.PlayerType;

public class ServerPlayer {

    private final String username;
    private final Connection connection;

    private PlayerType playerType;
    private PlayerState serverState;
    private PlayerState tempHoldState;
    private boolean ready;

    public boolean attack;
    public boolean hit;
    public boolean dead;
    public boolean moveUp;
    public boolean moveDown;
    public boolean moveLeft;
    public boolean moveRight;
    private float speed;

    private float x;
    private float y;

    public ServerPlayer(String username, Connection connection) {
        this.username = username;
        this.connection = connection;

        this.ready = false;
        this.playerType = PlayerType.GHOST_ONE;

        this.speed = 5.0F;
    }


    public void update() {
        if(dead == false){
            if (this.attack || this.hit || this.moveLeft || this.moveRight || this.moveUp || this.moveDown) {
                if (this.attack) {
                    this.serverState = PlayerState.ATTACKING;
                }else if(this.hit){
                    this.serverState = PlayerState.HIT;
                    this.hit = false;
                } else if (this.moveLeft || this.moveRight || this.moveUp || this.moveDown) {
                    if (this.moveLeft) {
                        this.x -= this.speed;
                        this.serverState = PlayerState.MOVING_LEFT;
                    } else if (this.moveRight) {
                        this.x += this.speed;
                        this.serverState = PlayerState.MOVING_RIGHT;
                    }
                    if (this.moveUp) {
                        this.y += this.speed;
                        this.serverState = PlayerState.MOVING_UP;
                    } else if (this.moveDown) {
                        this.y -= this.speed;
                        this.serverState = PlayerState.MOVING_DOWN;
                    }
                } else {
                    this.serverState = PlayerState.IDLE;
                }
            } else {
                this.serverState = PlayerState.IDLE;
            }
        }else{
            this.serverState = PlayerState.DEAD;
        }
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

    public PlayerType getPlayerType() {
        return playerType;
    }

    public PlayerState getServerState() {
        return serverState;
    }

    public void setServerState(PlayerState serverState) {
        this.serverState = serverState;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setPlayerType(PlayerType playerType) {

        this.playerType = playerType;
        if(Player.getIntByType(this.playerType) > 2){
            this.speed = 3.5F;
        }else{
            this.speed = 5F;
        }
    }
}
