package com.mygdx.game.supers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.handlers.ResourceHandler;

public class Player {
    private final Vector2 position;
    private final Vector2 serverPosition;
    private final Vector2 distance;

    private final Color color;

    private final String username;

    private float pastTime;

    public Player(final String username, final Color color){
        this.position = new Vector2();
        this.serverPosition = new Vector2();

        this.distance = new Vector2();

        this.username = username;
        this.color = color;
    }

    public void render(final Batch batch){
        //To color the player
        batch.setColor(this.color);

//        // Left
//        if(this.distance.x < -2){
//            TextureRegion frame = ResourceHandler.INSTANCE.playerWalk.getKeyFrame(this.pastTime, true);
//            batch.draw(frame, this.position.x + frame.getRegionWidth() / 2F, this.position.y, -frame.getRegionWidth(), frame.getRegionHeight());
//        }
//        // Right
//        else if (this.distance.x > +2){
//            TextureRegion frame = ResourceHandler.INSTANCE.playerWalk.getKeyFrame(this.pastTime, true);
//            batch.draw(frame, this.position.x, this.position.y, frame.getRegionWidth(), frame.getRegionHeight());
//        }
//        // Idle
//        else {
            TextureRegion frame = ResourceHandler.INSTANCE.playerIdle.getKeyFrame(this.pastTime,true);
            batch.draw(frame, this.position.x - frame.getRegionWidth(), this.position.y, frame.getRegionWidth(),frame.getRegionHeight());
//        }
        batch.setColor(Color.WHITE);
    }

    public void update(final float delta){
        this.pastTime += delta;

        final Vector2 interpolate = this.position.interpolate(this.serverPosition, 0.2F, Interpolation.linear);

        this.distance.x = interpolate.x - serverPosition.x;
        this.distance.y = interpolate.y - serverPosition.y;
    }

    public String getUsername(){
        return username;
    }

    public Vector2 getServerPosition(){
        return serverPosition;
    }

    public Vector2 getPosition(){
        return position;
    }
}
