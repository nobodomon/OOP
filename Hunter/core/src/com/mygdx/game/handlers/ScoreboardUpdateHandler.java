package com.mygdx.game.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.screens.GameInProgressScreen;
import com.mygdx.global.MoveUpdateEvent;

public class ScoreboardUpdateHandler implements  Runnable{

    public static final ScoreboardUpdateHandler INSTANCE = new ScoreboardUpdateHandler();

    private boolean running;

    public synchronized void start() {
        this.running = true;

        final Thread thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        this.running = false;
    }

    public void tick() {

        boolean tab;

        tab = Gdx.input.isKeyPressed(Input.Keys.TAB);

        if(tab){
            GameInProgressScreen.INSTANCE.showScoreBoard();
        }else{
            GameInProgressScreen.INSTANCE.hideScoreBoard();
        }
    }

    @Override
    public void run() {
        long pastTime = System.nanoTime();
        double amountOfTicks = 30;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        while (this.running) {
            try {
                Thread.sleep((long) (60F / amountOfTicks));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long now = System.nanoTime();
            delta += (now - pastTime) / ns;
            pastTime = now;

            while (delta > 0) {
                tick();
                delta--;
            }
        }
    }
}
