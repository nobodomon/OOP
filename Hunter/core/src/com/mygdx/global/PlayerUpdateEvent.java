package com.mygdx.global;

import com.mygdx.game.supers.PlayerState;

public class PlayerUpdateEvent {

    public String username;

    public float x;
    public float y;
    public int state;

    public int status;
    public double health;
    public long lastHit;

    public String skillname;
    public long skillCD;

    public PlayerUpdateEvent(){

    }
}
