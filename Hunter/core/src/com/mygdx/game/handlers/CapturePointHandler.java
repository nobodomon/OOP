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
    private boolean isLastDecaySet = false;

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
        LinkedList<Player> players = PlayerHandler.INSTANCE.getPlayers();
        //System.out.printf("There are %d players", players.size());
        double now = System.currentTimeMillis();
        for(int i = 0; i < capturePoints.size();i++){
            CapturePoint currPoint = capturePoints.get(i);
            for(int j = 0; j < players.size(); j++){
                if(Player.getIntByType(players.get(j).getPlayerType()) > 2){
                    if(players.get(j).getPlayerHitBox().overlaps(currPoint.getCapturePointHitBox()) && players.get(j).getCurrentState() == PlayerState.ATTACKING){
                        capturePoints.get(i).attemptDisrupt();
                        break;
                    }
                }else{
                    if(players.get(j).getPlayerHitBox().overlaps(currPoint.getCapturePointHitBox()) && players.get(j).getCurrentState() == PlayerState.ATTACKING){
                        capturePoints.get(i).attemptCapture();
                        break;
                    }
                }
            }
        }

        while(now - decayDelay > lastDecay){
            for(int i = 0; i < capturePoints.size(); i++) {
                CapturePoint currPoint = capturePoints.get(i);
                if ((currPoint.getProgress() != 0.0) && currPoint.getProgress() != 50.0 && currPoint.getProgress() != 100.0) {
                    capturePoints.get(i).captureDecay();
                }
            }
            lastDecay = System.currentTimeMillis();
        }


        for(int i = 0; i < capturePoints.size(); i++) {
            capturePointUpdate(capturePoints.get(i));
            capturePoints.get(i).render(batch);
        }
    }

    public void update(final float delta){
        for(int i = 0; i < this.capturePoints.size(); i++){
            this.capturePoints.get(i).update(delta);
        }
    }

    public void resetCapturePoints(){
        for(int i = 0; i < this.capturePoints.size(); i++){
            CapturePoint capturePoint = capturePoints.get(i);
            capturePoint.setProgress(0);
            capturePointUpdate(capturePoint);
        }
    }

    public void capturePointUpdate(final CapturePoint capturePoint){
        CapturePointUpdateEvent capturePointUpdateEvent = new CapturePointUpdateEvent();
        capturePointUpdateEvent.x = capturePoint.getPosition().x;
        capturePointUpdateEvent.y = capturePoint.getPosition().y;
        capturePointUpdateEvent.progress = capturePoint.getProgress();
        MyGdxGame.getInstance().getClient().sendTCP(capturePointUpdateEvent);
    }

    public boolean isAllCapturePointsCaptured(){
        boolean allCaptured = true;
        for(int i = 0; i < this.capturePoints.size(); i++){
            if(this.capturePoints.get(i).getProgress() != 100){
                allCaptured = false;
            }
        }
        return allCaptured;
    }
}
