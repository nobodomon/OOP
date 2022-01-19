package com.mygdx.game.handlers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.supers.CapturePoint;
import com.mygdx.game.supers.Player;
import com.mygdx.game.supers.PlayerState;
import com.mygdx.global.CapturePointUpdateEvent;
import com.mygdx.global.PlayerCapturingEvent;
import com.mygdx.global.PlayerHitEvent;
import com.mygdx.global.PlayerUpdateEvent;
import com.mygdx.server.supers.ServerCapturePoint;

import java.util.LinkedList;

public class CapturePointHandler {
    private double decayDelay = 1200;
    private double lastDecay = 0;
    public static final CapturePointHandler instance = new CapturePointHandler();
    public LinkedList<CapturePoint> capturePoints = new LinkedList<>();

    public CapturePoint getCapturePointByVector(final float x, final float y) {
        //System.out.printf("There are %d capture points", this.capturePoints.size());
        for(int i = 0; i < this.capturePoints.size(); i++){
            final CapturePoint capturePoint = this.capturePoints.get(i);
            if(capturePoint.getPosition().x == x && capturePoint.getPosition().y == y){
                return capturePoint;
            }
        }
        return null;
    }
    public void addCapturePoint(CapturePoint capturePoint){
        this.capturePoints.add(capturePoint);
    }

    public void removeCapturePoint(CapturePoint capturePoint){
        this.capturePoints.remove(capturePoint);
    }

    public void render(final Batch batch){
        double now = System.currentTimeMillis();
        LinkedList<Player> players = PlayerHandler.INSTANCE.getPlayers();
        //System.out.printf("There are %d players", players.size());
        for(int i = 0; i < capturePoints.size();i++){
            for(int j = 0; j < players.size(); j++){
                if(players.get(j).getPlayerHitBox().overlaps(capturePoints.get(i).getCapturePointHitBox()) && players.get(j).getCurrentState() == PlayerState.ATTACKING){
                    capturePoints.get(i).attemptCapture();
                    playerCapturing(capturePoints.get(i));
                    break;
                }else{
                    if(now - decayDelay > lastDecay){
                        capturePoints.get(i).captureDecay();
                        lastDecay = System.currentTimeMillis();
                        playerNotCapturingDecay(capturePoints.get(i));
                    }
                }
            }

            capturePoints.get(i).render(batch);
        }

    }

    public void update(final float delta){
        for(int i = 0; i < this.capturePoints.size(); i++){
            this.capturePoints.get(i).update(delta);
        }
    }


    public void playerCapturing(final CapturePoint capturePoint){
        CapturePointUpdateEvent capturePointUpdateEvent = new CapturePointUpdateEvent();
        capturePointUpdateEvent.x = capturePoint.getPosition().x;
        capturePointUpdateEvent.y = capturePoint.getPosition().y;
        capturePointUpdateEvent.progress = capturePoint.getProgress();
        MyGdxGame.getInstance().getClient().sendTCP(capturePointUpdateEvent);
    }

    public void playerNotCapturingDecay(final CapturePoint capturePoint){
        CapturePointUpdateEvent capturePointUpdateEvent = new CapturePointUpdateEvent();
        capturePointUpdateEvent.x = capturePoint.getPosition().x;
        capturePointUpdateEvent.y = capturePoint.getPosition().y;
        capturePointUpdateEvent.progress = capturePoint.getProgress();
        MyGdxGame.getInstance().getClient().sendTCP(capturePointUpdateEvent);
    }

}
