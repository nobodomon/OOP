package com.mygdx.server.supers;

import com.esotericsoftware.kryonet.Connection;
import com.mygdx.game.supers.Player;
import com.mygdx.game.supers.PlayerState;
import com.mygdx.game.supers.PlayerType;

import java.util.concurrent.TimeUnit;

public class ServerPlayer {

    private final String username;
    private double health;
    private long lastHit;
    private long lastBlink;
    private long blinkCD;
    private final Connection connection;

    private PlayerType playerType;
    private PlayerState serverState;
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
    private float blinkSpeed;

    private float x;
    private float y;

    public ServerPlayer(String username, Connection connection) {
        this.username = username;
        this.connection = connection;

        this.ready = false;
        this.playerType = PlayerType.GHOST_ONE;
        this.health = 25.0;
        this.speed = 5.0F;
        this.blinkSpeed = this.speed * 50;
        this.blinkCD = 0;
        this.lastBlink = 0;
    }


    public void update() {
        if (health > 0.0) {
            if (this.attack || this.hit || this.moveLeft || this.moveRight || this.moveUp || this.moveDown || this.shift) {
                if (this.attack) {
                    this.serverState = PlayerState.ATTACKING;
                } else if (this.hit) {
                    this.serverState = PlayerState.HIT;
                    this.hit = false;
                } else if (this.moveLeft || this.moveRight || this.moveUp || this.moveDown || this.shift) {
                    if (this.shift && (lastBlink > blinkCD)) {
                        if (this.moveLeft) {

                            if (lastBlink > blinkCD) {
                                if (this.x - blinkSpeed < 0) {
                                    this.x = 0;
                                } else {
                                    this.x -= blinkSpeed;
                                }
                                this.blinkCD = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5);
                            }
                            this.serverState = PlayerState.MOVING_LEFT;
                        } else if (this.moveRight) {

                            if (lastBlink > blinkCD) {
                                if (this.x + blinkSpeed > 1150) {
                                    this.x = 1150;
                                } else {
                                    this.x += blinkSpeed;
                                }
                                this.blinkCD = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5);
                            }
                            this.serverState = PlayerState.MOVING_RIGHT;
                        }
                        if (this.moveUp) {
                            if (lastBlink > blinkCD) {
                                if (this.y + blinkSpeed > 720) {
                                    this.y = 720;
                                } else {
                                    this.y += blinkSpeed;
                                }
                                this.blinkCD = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5);
                            }

                            this.serverState = PlayerState.MOVING_UP;
                        } else if (this.moveDown) {

                            if (lastBlink > blinkCD) {
                                if (this.y - blinkSpeed < 0) {
                                    this.y = 0;
                                } else {
                                    this.y -= blinkSpeed;
                                }
                                this.blinkCD = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5);
                            }

                            this.serverState = PlayerState.MOVING_UP;
                        }
                    } else {
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
                    }


                } else {
                    this.serverState = PlayerState.IDLE;
                }
            } else {
                this.serverState = PlayerState.IDLE;
            }
        } else {
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
        if (Player.getIntByType(this.playerType) > 2) {
            this.speed = 3.5F;
        } else {
            this.speed = 5F;
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

    public long getLastBlink() {
        return lastBlink;
    }

    public void setLastBlink(long lastBlink) {
        this.lastBlink = lastBlink;
    }

    public long getBlinkCD() {
        return blinkCD;
    }
}
