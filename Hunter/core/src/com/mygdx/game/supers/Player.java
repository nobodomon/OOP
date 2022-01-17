package com.mygdx.game.supers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.handlers.FontSizeHandler;
import com.mygdx.game.handlers.ResourceHandler;


public class Player {
    private final Vector2 position;
    private final Vector2 serverPosition;
    private final Vector2 distance;
    private PlayerState currentState;
    private PlayerType playerType;
    private boolean lookingLeft;

    private final String username;

    private final GlyphLayout layout;
    private final BitmapFont font;

    private TextureRegion idleFrames;
    private TextureRegion walkingFrames;
    private TextureRegion attackFrames;

    private float pastTime;

    private float attackDuration;

    public Player(final String username, final PlayerType playerType) {
        this.position = new Vector2();
        this.serverPosition = new Vector2();
        this.distance = new Vector2();

        this.lookingLeft = false;
        this.username = username;
        this.playerType = PlayerType.GHOST_ONE;

        this.layout = new GlyphLayout();
        this.font = FontSizeHandler.INSTANCE.getFont(18, Color.BLACK);
    }

    public void render(final Batch batch) {
        //To color the player
        //batch.setColor(this.color);
        setFrames(pastTime);
        TextureRegion frame;
        float positionX;
        float regionWidth;
        if (currentState == PlayerState.ATTACKING) {
            frame = this.attackFrames;
        } else {
            if(currentState == PlayerState.MOVING_UP || currentState == PlayerState.MOVING_DOWN || currentState == PlayerState.MOVING_LEFT || currentState == PlayerState.MOVING_RIGHT){
                frame = this.walkingFrames;
                if (currentState == PlayerState.MOVING_LEFT) {
                    lookingLeft = true;
                } else if (currentState == PlayerState.MOVING_RIGHT) {
                    lookingLeft = false;
                }
            }else if (currentState == PlayerState.IDLE) {
                frame = this.idleFrames;
            } else {
                frame = this.idleFrames;
            }
        }
        if(lookingLeft){
            positionX = this.position.x + frame.getRegionWidth();
            regionWidth = -frame.getRegionWidth();
        }else{
            positionX = this.position.x;
            regionWidth = frame.getRegionWidth();
        }
        batch.draw(frame, positionX, this.position.y, regionWidth, frame.getRegionHeight());

        this.layout.setText(this.font, this.username);

        this.font.draw(batch, this.username, this.position.x + frame.getRegionWidth() / 2F - this.layout.width / 2, this.position.y + frame.getRegionHeight() + 10);

    }

    public void update(final float delta) {
        this.pastTime += delta;

        final Vector2 interpolate = this.position.interpolate(this.serverPosition, 0.2F, Interpolation.smooth);

        this.distance.x = interpolate.x - serverPosition.x;
        this.distance.y = interpolate.y - serverPosition.y;
    }

    public String getUsername() {
        return username;
    }

    public Vector2 getServerPosition() {
        return serverPosition;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setCurrentState(PlayerState state) {
        currentState = state;
    }

    public static PlayerState getStateByInt(int i) {
        PlayerState state;
        switch (i) {
            case 0:
                state = PlayerState.ATTACKING;
                break;
            case 1:
                state = PlayerState.MOVING_LEFT;
                break;
            case 2:
                state = PlayerState.MOVING_RIGHT;
                break;
            case 3:
                state = PlayerState.MOVING_UP;
                break;
            case 4:
                state = PlayerState.MOVING_DOWN;
                break;
            default:
                state = PlayerState.IDLE;
                break;
        }
        return state;
    }

    public static int getIntByState(PlayerState playerState) {
        int state;
        switch (playerState) {
            case ATTACKING:
                state = 0;
                break;
            case MOVING_LEFT:
                state = 1;
                break;
            case MOVING_RIGHT:
                state = 2;
                break;
            case MOVING_UP:
                state = 3;
                break;
            case MOVING_DOWN:
                state = 4;
                break;
            default:
                state = 5;
                break;
        }
        return state;
    }

    public static PlayerType getTypeByInt(int i){
        PlayerType type;
        switch (i) {
            case 0:
                type = PlayerType.GHOST_ONE;
                break;
            case 1:
                type = PlayerType.GHOST_TWO;
                break;
            case 2:
                type = PlayerType.GHOST_THREE;
                break;
            case 3:
                type = PlayerType.MINOTAUR_ONE;
                break;
            case 4:
                type = PlayerType.MINOTAUR_TWO;
                break;
            case 5:
                type = PlayerType.MINOTAUR_THREE;
                break;
            default:
                type = PlayerType.GHOST_ONE;
                break;
        }
        return type;
    }

    public static int getIntByType(PlayerType type){
        int playerTypeInt;
        switch (type) {
            case GHOST_ONE:
                playerTypeInt = 0;
                break;
            case GHOST_TWO:
                playerTypeInt = 1;
                break;
            case GHOST_THREE:
                playerTypeInt = 2;
                break;
            case MINOTAUR_ONE:
                playerTypeInt = 3;
                break;
            case MINOTAUR_TWO:
                playerTypeInt = 4;
                break;
            case MINOTAUR_THREE:
                playerTypeInt = 5;
                break;
            default:
                playerTypeInt = 0;
                break;
        }
        return playerTypeInt;
    }

    public void setFrames(float pastTime){
        switch(this.playerType){
            case GHOST_ONE:
                idleFrames = ResourceHandler.INSTANCE.ghost_one_idle.getKeyFrame(pastTime,true);
                walkingFrames = ResourceHandler.INSTANCE.ghost_one_walk.getKeyFrame(pastTime,true);
                attackFrames = ResourceHandler.INSTANCE.ghost_one_attack.getKeyFrame(pastTime,true);
                break;
            case GHOST_TWO:
                idleFrames = ResourceHandler.INSTANCE.ghost_two_idle.getKeyFrame(pastTime,true);
                walkingFrames = ResourceHandler.INSTANCE.ghost_two_walk.getKeyFrame(pastTime,true);
                attackFrames = ResourceHandler.INSTANCE.ghost_two_attack.getKeyFrame(pastTime,true);
                break;
            case GHOST_THREE:
                idleFrames = ResourceHandler.INSTANCE.ghost_three_idle.getKeyFrame(pastTime,true);
                walkingFrames = ResourceHandler.INSTANCE.ghost_three_walk.getKeyFrame(pastTime,true);
                attackFrames = ResourceHandler.INSTANCE.ghost_three_attack.getKeyFrame(pastTime,true);
                break;
            case MINOTAUR_ONE:
                idleFrames = ResourceHandler.INSTANCE.minotaur_one_idle.getKeyFrame(pastTime,true);
                walkingFrames = ResourceHandler.INSTANCE.minotaur_one_walk.getKeyFrame(pastTime,true);
                attackFrames = ResourceHandler.INSTANCE.minotaur_one_attack.getKeyFrame(pastTime,true);
                break;
            case MINOTAUR_TWO:
                idleFrames = ResourceHandler.INSTANCE.minotaur_two_idle.getKeyFrame(pastTime,true);
                walkingFrames = ResourceHandler.INSTANCE.minotaur_two_walk.getKeyFrame(pastTime,true);
                attackFrames = ResourceHandler.INSTANCE.minotaur_two_attack.getKeyFrame(pastTime,true);
                break;
            case MINOTAUR_THREE:
                idleFrames = ResourceHandler.INSTANCE.minotaur_three_idle.getKeyFrame(pastTime,true);
                walkingFrames = ResourceHandler.INSTANCE.minotaur_three_walk.getKeyFrame(pastTime,true);
                attackFrames = ResourceHandler.INSTANCE.minotaur_three_attack.getKeyFrame(pastTime,true);
                break;
        }
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }
}
