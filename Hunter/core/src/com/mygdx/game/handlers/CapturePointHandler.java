package com.mygdx.game.handlers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.supers.CapturePoint;
import com.mygdx.game.supers.Player;
import com.mygdx.game.supers.PlayerState;
import com.mygdx.global.CapturePointUpdateEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

public class CapturePointHandler implements EntityHandler {
    private double decayDelay = 1200;
    private double lastDecay = 0;

    public static final CapturePointHandler instance = new CapturePointHandler();
    public LinkedList<CapturePoint> capturePoints = new LinkedList<>();

    public CapturePoint getCapturePointByVector(final float x, final float y) {
        for (int i = 0; i < this.capturePoints.size(); i++) {
            final CapturePoint capturePoint = this.capturePoints.get(i);
            if (capturePoint.getPosition().x == x && capturePoint.getPosition().y == y) {
                return capturePoint;
            }
        }
        return null;
    }

    public void addCapturePoint(CapturePoint capturePoint) {
        this.capturePoints.add(capturePoint);
    }

    public void removeCapturePoint(CapturePoint capturePoint) {
        this.capturePoints.remove(capturePoint);
    }

    @Override
    public void render(final Batch batch) {
        LinkedList<Player> players = PlayerHandler.INSTANCE.getPlayers();
        double now = System.currentTimeMillis();
        for (int i = 0; i < capturePoints.size(); i++) {
            CapturePoint currPoint = capturePoints.get(i);
            for (int j = 0; j < players.size(); j++) {
                if (Player.getIntByType(players.get(j).getPlayerType()) > 2) {
                    if (players.get(j).getPlayerHitBox().overlaps(currPoint.getCapturePointHitBox()) && players.get(j).getCurrentState() == PlayerState.ATTACKING) {
                        capturePoints.get(i).attemptDisrupt();
                        break;
                    }
                } else {
                    if (players.get(j).getPlayerHitBox().overlaps(currPoint.getCapturePointHitBox()) && players.get(j).getCurrentState() == PlayerState.ATTACKING) {
                        if (this.isLastCapturePoint()) {
                            capturePoints.get(i).attemptCapture(1.5f);
                        } else {
                            capturePoints.get(i).attemptCapture();
                        }
                    }
                }
            }
        }

        while (now - decayDelay > lastDecay) {
            for (int i = 0; i < capturePoints.size(); i++) {
                CapturePoint currPoint = capturePoints.get(i);
                if ((currPoint.getProgress() != 0.0) && currPoint.getProgress() != 50.0 && currPoint.getProgress() != 100.0) {
                    capturePoints.get(i).captureDecay();
                }
            }
            lastDecay = System.currentTimeMillis();
        }


        for (int i = 0; i < capturePoints.size(); i++) {
            capturePointUpdate(capturePoints.get(i));
            capturePoints.get(i).render(batch);
        }
    }

    @Override
    public void update(final float delta) {
        for (int i = 0; i < this.capturePoints.size(); i++) {
            this.capturePoints.get(i).update(delta);
        }
    }

    public void resetCapturePoints() {
        for (int i = 0; i < this.capturePoints.size(); i++) {
            CapturePoint capturePoint = capturePoints.get(i);
            capturePoint.setProgress(0.0f);
            capturePointUpdate(capturePoint);
        }
    }

    public void clearCapturePoints() {
        this.capturePoints = new LinkedList<>();
    }

    public void capturePointUpdate(final CapturePoint capturePoint) {
        CapturePointUpdateEvent capturePointUpdateEvent = new CapturePointUpdateEvent();
        capturePointUpdateEvent.x = capturePoint.getPosition().x;
        capturePointUpdateEvent.y = capturePoint.getPosition().y;
        capturePointUpdateEvent.progress = capturePoint.getProgress();
        MyGdxGame.getInstance().getClient().sendTCP(capturePointUpdateEvent);
    }

    public boolean isAllCapturePointsCaptured() {
        boolean allCaptured = true;
        for (int i = 0; i < this.capturePoints.size(); i++) {
            if (this.capturePoints.get(i).getProgress() != 100) {
                allCaptured = false;
            }
        }
        return allCaptured;
    }

    public boolean isLastCapturePoint() {
        int fullyCappedPoints = 0;
        for (int i = 0; i < this.capturePoints.size(); i++) {
            if (this.capturePoints.get(i).getProgress() == 100) {
                fullyCappedPoints++;
            }
        }
        if (capturePoints.size() - fullyCappedPoints == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean doesNewCapturePointOverlap(float x, float y) {
        for (int i = 0; i < this.capturePoints.size(); i++) {
            if (capturePoints.get(i).getPosition().x == x && capturePoints.get(i).getPosition().y == y) {
                return true;
            }
        }
        return false;
    }
}
