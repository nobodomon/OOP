package com.mygdx.server.handlers;

import com.mygdx.game.supers.Player;
import com.mygdx.game.supers.PlayerState;
import com.mygdx.global.CapturePointUpdateEvent;
import com.mygdx.global.PlayerCapturingEvent;
import com.mygdx.global.PlayerCharacterChangeEvent;
import com.mygdx.global.PlayerHitEvent;
import com.mygdx.global.PlayerReadyEvent;
import com.mygdx.global.PlayerUpdateEvent;
import com.mygdx.server.ServerFoundation;
import com.mygdx.server.supers.ServerCapturePoint;
import com.mygdx.server.supers.ServerPlayer;

public class CapturePointUpdateHandler implements Runnable{

    public static final CapturePointUpdateHandler INSTANCE = new CapturePointUpdateHandler();

    private boolean running;

    public synchronized void start(){
        this.running = true;

        final Thread thread = new Thread(this);
        thread.start();
    }

    public void tick(){
        for(int i = 0; i < CapturePointHandler.INSTANCE.getCapturePoints().size(); i++){
            // Update server capture point
            final ServerCapturePoint serverCapturePoint = CapturePointHandler.INSTANCE.getCapturePoints().get(i);
            serverCapturePoint.update();

            // Send update to all clients

            final CapturePointUpdateEvent capturePointUpdateEvent = new CapturePointUpdateEvent();
            capturePointUpdateEvent.x = serverCapturePoint.getX();
            capturePointUpdateEvent.y = serverCapturePoint.getX();
            capturePointUpdateEvent.progress = serverCapturePoint.getProgress();

            ServerFoundation.instance.getServer().sendToAllTCP(capturePointUpdateEvent);
        }
    }

    @Override
    public void run() {
        long pastTime = System.nanoTime();
        double amountOfTicks = 60;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        while(this.running){

            try {
                Thread.sleep((long) (60F / amountOfTicks));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long now = System.nanoTime();
            delta += (now - pastTime) / ns;
            pastTime = now;

            while(delta > 0){
                tick();
                delta--;
            }
        }
    }
}
