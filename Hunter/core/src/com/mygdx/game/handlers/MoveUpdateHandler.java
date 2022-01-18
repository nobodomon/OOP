package com.mygdx.game.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.MyGdxGame;
import com.mygdx.global.MoveUpdateEvent;

public class MoveUpdateHandler implements Runnable{

    public static final MoveUpdateHandler INSTANCE = new MoveUpdateHandler();

    private boolean attack,moveUp, moveDown, moveLeft, moveRight;

    //attack cool down
    private double lastTime = 0;
    private boolean attackAnim = false;

    private boolean running;

    public synchronized void start(){
        this.running = true;

        final Thread thread = new Thread(this);
        thread.start();
    }

    public void stop(){
        this.running = false;
    }

    public void tick(){
        double lastTime = 0;
        boolean w, s, a, d, e;

//        if(attackAnim == true) {
//            double now = System.currentTimeMillis();
//            float frameTime = ResourceHandler.INSTANCE.ghost_one_attack.getAnimationDuration();
//            if (lastTime - now > frameTime) {
//                e = true;
//            } else {
//                e = false;
//                attackAnim = false;
//            }
//        }
        if(Gdx.input.isKeyPressed(Input.Keys.E) || Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.D)){
            if(Gdx.input.isKeyPressed(Input.Keys.W)){
                w = true;
            }else{
                w = false;
            }

            if(Gdx.input.isKeyPressed(Input.Keys.S)){
                s = true;
            }else{
                s = false;
            }

            if(Gdx.input.isKeyPressed(Input.Keys.A)){
                a = true;
            }else{
                a = false;
            }

            if(Gdx.input.isKeyPressed(Input.Keys.D)){
                d = true;
            }else{
                d = false;
            }

            if(Gdx.input.isKeyPressed(Input.Keys.E)){
                e = true;
            }else{
                e = false;
            }

//            if(lastTime - now > coolDownTimer && Gdx.input.isKeyPressed(Input.Keys.E)){
//                lastTime = System.currentTimeMillis();
//                e = true;
//            }else{
//                e = false;
//            }
        }else{
            w = false;
            s = false;
            a = false;
            d = false;
            if(attackAnim == true){
                e = true;
            }else{
                e = false;
            }
        }



        final boolean changed = this.movementChanged(e,w,s,a,d);

        if(!changed){
            this.moveUp = w;
            this.moveDown = s;
            this.moveLeft = a;
            this.moveRight = d;
            this.attack = e;

            final MoveUpdateEvent moveUpdateEvent = new MoveUpdateEvent();
            moveUpdateEvent.moveUp = this.moveUp;
            moveUpdateEvent.moveDown = this.moveDown;
            moveUpdateEvent.moveLeft = this.moveLeft;
            moveUpdateEvent.moveRight = this.moveRight;
            moveUpdateEvent.attack = this.attack;
            MyGdxGame.getInstance().getClient().sendTCP(moveUpdateEvent);
        }else{
        }
    }

    public boolean movementChanged(boolean isAttacking, final boolean moveUp, final boolean moveDown, final boolean moveLeft, final boolean moveRight){
        return isAttacking == this.attack && moveUp == this.moveUp && moveDown == this.moveDown && moveLeft == this.moveLeft && moveRight == this.moveRight;
    }

    @Override
    public void run() {
        long pastTime = System.nanoTime();
        double amountOfTicks = 30;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        while(this.running){
            try {
                if(attack){
                    Thread.sleep((long) (ResourceHandler.INSTANCE.getHunterAttackDuration() * 100F));
                }else{
                    Thread.sleep((long) (60F / amountOfTicks));
                }
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
