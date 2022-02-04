package com.mygdx.server.supers;

import com.esotericsoftware.kryonet.Connection;
import com.mygdx.game.supers.PlayerStatus;
import com.mygdx.game.supers.skills.BlinkToPoint;
import com.mygdx.game.supers.skills.DashSkill;
import com.mygdx.game.supers.Player;
import com.mygdx.game.supers.PlayerState;
import com.mygdx.game.supers.PlayerType;
import com.mygdx.game.supers.Skill;
import com.mygdx.game.supers.Skills;
import com.mygdx.game.supers.skills.MassStun;
import com.mygdx.game.supers.skills.SpeedBoostSkill;
import com.mygdx.game.supers.skills.dmgUp;

public class ServerPlayer {

    private final String username;
    private double health;
    private long lastHit;

    private final Connection connection;

    private Skill skill;

    private PlayerType playerType;
    private PlayerState serverState;
    private PlayerStatus status;
    private boolean ready;

    public boolean attack;
    public boolean hit;
    public boolean dead;

    public boolean moveUp;
    public boolean moveDown;
    public boolean moveLeft;
    public boolean moveRight;
    public boolean shift;

    private float speed;
    private double dmgMultiplier;

    private float x;
    private float y;

    public ServerPlayer(String username, Connection connection) {
        this.username = username;
        this.connection = connection;
        this.status = PlayerStatus.NONE;
        this.serverState = PlayerState.IDLE;
        this.ready = false;
        this.playerType = PlayerType.GHOST_ONE;
        this.health = 25.0;
        this.speed = 4.5F;
        this.dmgMultiplier = 1;
        this.skill = new DashSkill(this);
    }


    public void update() {
        if(System.currentTimeMillis() > skill.getSkillEndDuration()){
            skill.revertSkill();
        }
        if (health <= 0.0){
            this.serverState = PlayerState.DEAD;
            return;
        }
        if(status == PlayerStatus.STUNNED){
            this.serverState = PlayerState.HIT;
            return;
        }
        if(hit == true){
            this.serverState = PlayerState.HIT;
            hit = false;
            return;
        }
        if(attack == true){
            this.serverState = PlayerState.ATTACKING;
            return;
        }
        if(shift == true){
            skill.useSkill();
            return;
        }
        if(this.moveLeft || this.moveRight || this.moveUp || this.moveDown){
            if (this.moveLeft) {

                if (this.x - this.speed < 0) {

                } else {
                    this.x -= this.speed;
                }
                this.serverState = PlayerState.MOVING_LEFT;
            } else if (this.moveRight) {
                if (this.x + this.speed > 1150) {

                } else {
                    this.x += this.speed;
                }
                this.serverState = PlayerState.MOVING_RIGHT;
            }

            if (this.moveUp) {
                if (this.y + this.speed > 720) {
                    this.y = 720;
                } else {
                    this.y += this.speed;
                }
                this.serverState = PlayerState.MOVING_UP;
            } else if (this.moveDown) {
                if (this.y - this.speed < 0) {
                    this.y = 0;
                } else {
                    this.y -= this.speed;
                }
                this.serverState = PlayerState.MOVING_DOWN;
            }
        }else{
            this.serverState = PlayerState.IDLE;
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
        switch (playerType){
            case GHOST_ONE:
                this.speed = 4F;
                this.status = PlayerStatus.NONE;
                this.skill = new BlinkToPoint(this);
                break;
            case GHOST_THREE:
            case GHOST_TWO:
                this.speed = 4F;
                this.status = PlayerStatus.NONE;
                this.skill = new SpeedBoostSkill(this);
                break;
            case MINOTAUR_ONE:
                this.speed = 4.5F;
                this.status = PlayerStatus.NONE;
                this.skill = new MassStun(this);
                break;
            case MINOTAUR_TWO:
                this.speed = 4.5F;
                this.dmgMultiplier = 1;
                this.status = PlayerStatus.NONE;
                this.skill = new dmgUp(this);
                break;
            case MINOTAUR_THREE:
                this.speed = 4.5F;
                this.status = PlayerStatus.NONE;
                this.skill = new DashSkill(this);
                break;
            default:
                this.speed = 4F;
                this.status = PlayerStatus.NONE;
                this.skill =  new DashSkill(this);
        }
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public long getLastHit() {
        return lastHit;
    }

    public void setLastHit(long lastHit) {
        this.lastHit = lastHit;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public double getDmgMultiplier() {return dmgMultiplier;}

    public void setDmgMultiplier(double dmgMultiplier) {this.dmgMultiplier = dmgMultiplier;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }
}
