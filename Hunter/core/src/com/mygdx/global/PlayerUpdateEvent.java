package com.mygdx.global;

import com.mygdx.game.supers.PlayerState;
import com.mygdx.game.supers.Skill;

public class PlayerUpdateEvent {

    public String username;

    public float x;
    public float y;
    public int state;

    public int status;
    public double health;
    public long lastHit;
    public double dmgMultiplier;

    public String skillname;
    public String skillDescription;
    public long skillCD;

    public PlayerUpdateEvent(){

    }
}
