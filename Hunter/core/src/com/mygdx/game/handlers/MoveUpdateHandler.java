package com.mygdx.game.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.screens.GameInProgressScreen;
import com.mygdx.global.MoveUpdateEvent;

public class MoveUpdateHandler implements Runnable {

    public static final MoveUpdateHandler INSTANCE = new MoveUpdateHandler();

    private boolean attack, moveUp, moveDown, moveLeft, moveRight, shift;

    //attack cool down

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


        boolean w, s, a, d, e, shift;

        w = Gdx.input.isKeyPressed(Input.Keys.W);

        s = Gdx.input.isKeyPressed(Input.Keys.S);

        a = Gdx.input.isKeyPressed(Input.Keys.A);

        d = Gdx.input.isKeyPressed(Input.Keys.D);

        e = Gdx.input.isKeyPressed(Input.Keys.SPACE);

        shift = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);

        final boolean changed = this.movementChanged(e, w, s, a, d, shift);

        if (!changed) {
            this.moveUp = w;
            this.moveDown = s;
            this.moveLeft = a;
            this.moveRight = d;
            this.attack = e;
            this.shift = shift;

            final MoveUpdateEvent moveUpdateEvent = new MoveUpdateEvent();
            moveUpdateEvent.moveUp = this.moveUp;
            moveUpdateEvent.moveDown = this.moveDown;
            moveUpdateEvent.moveLeft = this.moveLeft;
            moveUpdateEvent.moveRight = this.moveRight;
            moveUpdateEvent.attack = this.attack;
            moveUpdateEvent.shift = this.shift;
            MyGdxGame.getInstance().getClient().sendTCP(moveUpdateEvent);
        } else {
        }
    }

    public boolean movementChanged(boolean isAttacking, final boolean moveUp, final boolean moveDown, final boolean moveLeft, final boolean moveRight, final boolean dash) {
        return isAttacking == this.attack && moveUp == this.moveUp && moveDown == this.moveDown && moveLeft == this.moveLeft && moveRight == this.moveRight && dash == this.shift;
    }

    @Override
    public void run() {
        long pastTime = System.nanoTime();
        double amountOfTicks = 30;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        while (this.running) {
            try {
                if (attack) {
                    Thread.sleep((long) (ResourceHandler.INSTANCE.getHunterAttackDuration() * 100F));
                } else {
                    Thread.sleep((long) (60F / amountOfTicks));
                }
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
