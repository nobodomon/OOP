package com.mygdx.game.supers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.handlers.FontSizeHandler;
import com.mygdx.game.handlers.ResourceHandler;
import com.mygdx.game.handlers.SoundEffect;

import java.util.concurrent.TimeUnit;


public class Player implements Entity {
    private final Vector2 position;
    private final Vector2 serverPosition;
    private final Vector2 distance;
    private PlayerType playerType;
    private boolean lookingLeft;

    private boolean alive;


    private double dmgMultiplier;
    private final String username;
    private double health;
    private boolean ready;
    private long lastHit;

    private long skillCD;
    private Skills skillName;
    private String skillDescription;

    private Animation<TextureAtlas.AtlasRegion> generalFrame;
    private PlayerStatus status;
    private PlayerState currentState;
    private PlayerState lockedState;
    private float animationTime;
    private boolean animLock;
    private final GlyphLayout layout;
    private final BitmapFont font;

    private Rectangle playerHitBox;

    private SoundEffect attackSound = new SoundEffect("attackSound");
    private SoundEffect hurtSound = new SoundEffect("hurtSound");


    public Player(final String username) {
        this.position = new Vector2();
        this.serverPosition = new Vector2();
        this.distance = new Vector2();

        this.lastHit = 0;
        this.skillCD = 0;
        this.skillName = Skills.DASH;
        this.lookingLeft = false;
        this.username = username;
        this.playerType = PlayerType.GHOST_ONE;
        this.currentState = PlayerState.IDLE;
        this.status = PlayerStatus.NONE;
        this.health = 25;
        this.alive = true;
        this.ready = false;
        this.playerHitBox = new Rectangle();
        this.animLock = false;
        this.animationTime = 0;
        this.dmgMultiplier = 1;
        this.layout = new GlyphLayout();
        this.font = FontSizeHandler.INSTANCE.getFont(18, Color.BLACK);

    }

    @Override
    public void render(final Batch batch) {
        //To color the player
        //batch.setColor(this.color)
        TextureRegion frame;
        float positionX;
        float regionWidth;
        String name = this.username + " " + this.health;

        // If player is dead set currentState to permanently dead
        if (health <= 0.0) {
            currentState = PlayerState.DEAD;
        }

        if (currentState == PlayerState.ATTACKING) {
            animationTime = 0;
            if (animLock) {
                if (!generalFrame.isAnimationFinished(animationTime)) {
                } else {
                    animLock = false;
                }
            } else {
                animLock = true;
                this.lockedState = this.currentState;
                if (this.playerType == PlayerType.MINOTAUR_ONE || this.playerType == PlayerType.MINOTAUR_TWO || this.playerType == PlayerType.MINOTAUR_THREE ){
                    this.attackSound.soundPlay();
                }
            }
        } else if (currentState == PlayerState.HIT) {
            animationTime = 0;
            if (animLock) {
                if (!generalFrame.isAnimationFinished(animationTime)) {
                } else {
                    animLock = false;
                }
            } else {
                animLock = true;
                this.lockedState = this.currentState;
            }
        } else if (currentState == PlayerState.DEAD) {
            if (animLock) {
                if (!generalFrame.isAnimationFinished(animationTime)) {
                } else {
                    if (lockedState != PlayerState.DEAD) {
                        animationTime = 0;
                    }
                    //animLock = false;
                }
            } else {
                animLock = true;
                this.lockedState = this.currentState;
            }
        } else if (currentState == PlayerState.MOVING_UP || currentState == PlayerState.MOVING_DOWN || currentState == PlayerState.MOVING_LEFT || currentState == PlayerState.MOVING_RIGHT) {
            PlayerState lookingDirection = PlayerState.MOVING_LEFT;
            if (!animLock) {
                if (currentState == PlayerState.MOVING_LEFT) {
                    this.lockedState = PlayerState.MOVING_LEFT;
                    lookingLeft = true;
                } else if (currentState == PlayerState.MOVING_RIGHT) {
                    this.lockedState = PlayerState.MOVING_RIGHT;
                    lookingLeft = false;
                }
                if (currentState == PlayerState.MOVING_UP) {
                    this.lockedState = PlayerState.MOVING_UP;
                }
                if (currentState == PlayerState.MOVING_DOWN) {
                    this.lockedState = PlayerState.MOVING_DOWN;
                }
            } else {
                animLock = !generalFrame.isAnimationFinished(animationTime);
            }
        } else if (currentState == PlayerState.IDLE) {
            if (!animLock) {
                lockedState = PlayerState.IDLE;
            } else {
                if (!generalFrame.isAnimationFinished(animationTime)) {
                    animLock = true;
                } else {
                    animLock = false;
                    lockedState = PlayerState.IDLE;
                }
            }
        }
        setFrames(this.lockedState);

        //If non looping animation disable loop
        if (this.lockedState == PlayerState.ATTACKING || this.lockedState == PlayerState.HIT || this.lockedState == PlayerState.DEAD) {
            frame = generalFrame.getKeyFrame(animationTime);
        } else {
            frame = generalFrame.getKeyFrame(animationTime, true);
        }

        //Make character look in render in left or right direction
        if (lookingLeft) {
            positionX = this.position.x + frame.getRegionWidth();
            regionWidth = -frame.getRegionWidth();
        } else {
            positionX = this.position.x;
            regionWidth = frame.getRegionWidth();
        }

        //Generate player hitbox
        this.playerHitBox = new Rectangle(this.position.x, this.position.y, frame.getRegionWidth(), frame.getRegionHeight());

        //Make player blink on hit
        if (this.lastHit - System.currentTimeMillis() > 0 && this.lastHit != 0) {
            if ((this.lastHit - System.currentTimeMillis()) % 2 > 0) {
                batch.setColor(Color.GRAY);
            } else {

            }
        }

        batch.draw(frame, positionX, this.position.y, regionWidth, frame.getRegionHeight());
        batch.setColor(Color.WHITE);
        this.layout.setText(this.font, name);

        this.font.draw(batch, name, this.position.x + frame.getRegionWidth() / 2F - this.layout.width / 2, this.position.y + frame.getRegionHeight() + 10);

    }

    @Override
    public void update(final float delta) {
        this.animationTime += delta;
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

    public PlayerState getCurrentState() {
        return currentState;
    }

    public static PlayerStatus getStatusByInt(int i) {
        switch (i) {
            case 0:
                return PlayerStatus.NONE;
            case 1:
                return PlayerStatus.STUNNED;
            default:
                return PlayerStatus.NONE;
        }
    }

    public static int getIntByStatus(PlayerStatus status) {
        switch (status) {
            case NONE:
                return 0;
            case STUNNED:
                return 1;
            default:
                return 0;
        }
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
            case 5:
                state = PlayerState.DEAD;
                break;
            case 6:
                state = PlayerState.HIT;
                break;
            case 7:
                state = PlayerState.IDLE;
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
            case DEAD:
                state = 5;
                break;
            case HIT:
                state = 6;
                break;
            case IDLE:
                state = 7;
                break;
            default:
                state = 7;
        }
        return state;
    }

    public static PlayerType getTypeByInt(int i) {
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

    public static int getIntByType(PlayerType type) {
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

    public static Texture getPlayerIcon(PlayerType type) {
        switch (type) {
            case GHOST_ONE:
                return new Texture(Gdx.files.internal("ghost_1_face.png"));
            case GHOST_TWO:
                return new Texture(Gdx.files.internal("ghost_2_face.png"));
            case GHOST_THREE:
                return new Texture(Gdx.files.internal("ghost_3_face.png"));
            case MINOTAUR_ONE:
                return new Texture(Gdx.files.internal("minotaur_1_face.png"));
            case MINOTAUR_TWO:
                return new Texture(Gdx.files.internal("minotaur_2_face.png"));
            case MINOTAUR_THREE:
                return new Texture(Gdx.files.internal("minotaur_3_face.png"));
            default:
                return new Texture(Gdx.files.internal("ghost_1_face.png"));
        }
    }


    public void setFrames(PlayerState state) {
        switch (this.playerType) {
            case GHOST_ONE:
                switch (state) {
                    case IDLE:
                        this.generalFrame = ResourceHandler.INSTANCE.ghost_one_idle;
                        break;
                    case MOVING_DOWN:
                    case MOVING_RIGHT:
                    case MOVING_LEFT:
                    case MOVING_UP:
                        this.generalFrame = ResourceHandler.INSTANCE.ghost_one_walk;
                        break;
                    case ATTACKING:
                        this.generalFrame = ResourceHandler.INSTANCE.ghost_one_attack;
                        break;
                    case DEAD:
                        this.generalFrame = ResourceHandler.INSTANCE.ghost_one_dead;
                        break;
                    case HIT:
                        this.generalFrame = ResourceHandler.INSTANCE.ghost_one_hit;
                        break;
                    default:
                        this.generalFrame = ResourceHandler.INSTANCE.ghost_one_idle;
                        break;
                }
                break;
            case GHOST_TWO:
                switch (state) {
                    case IDLE:
                        this.generalFrame = ResourceHandler.INSTANCE.ghost_two_idle;
                        break;
                    case MOVING_DOWN:
                    case MOVING_RIGHT:
                    case MOVING_LEFT:
                    case MOVING_UP:
                        this.generalFrame = ResourceHandler.INSTANCE.ghost_two_walk;
                        break;
                    case ATTACKING:
                        this.generalFrame = ResourceHandler.INSTANCE.ghost_two_attack;
                        break;
                    case DEAD:
                        this.generalFrame = ResourceHandler.INSTANCE.ghost_two_dead;
                        break;
                    case HIT:
                        this.generalFrame = ResourceHandler.INSTANCE.ghost_two_hit;
                        break;
                    default:
                        this.generalFrame = ResourceHandler.INSTANCE.ghost_two_idle;
                        break;
                }
                break;
            case GHOST_THREE:
                switch (state) {
                    case IDLE:
                        this.generalFrame = ResourceHandler.INSTANCE.ghost_three_idle;
                        break;
                    case MOVING_DOWN:
                    case MOVING_RIGHT:
                    case MOVING_LEFT:
                    case MOVING_UP:
                        this.generalFrame = ResourceHandler.INSTANCE.ghost_three_walk;
                        break;
                    case ATTACKING:
                        this.generalFrame = ResourceHandler.INSTANCE.ghost_three_attack;
                        break;
                    case DEAD:
                        this.generalFrame = ResourceHandler.INSTANCE.ghost_three_dead;
                        break;
                    case HIT:
                        this.generalFrame = ResourceHandler.INSTANCE.ghost_three_hit;
                        break;
                    default:
                        this.generalFrame = ResourceHandler.INSTANCE.ghost_three_idle;
                        break;
                }
                break;
            case MINOTAUR_ONE:
                switch (state) {
                    case IDLE:
                        this.generalFrame = ResourceHandler.INSTANCE.minotaur_one_idle;
                        break;
                    case MOVING_DOWN:
                    case MOVING_RIGHT:
                    case MOVING_LEFT:
                    case MOVING_UP:
                        this.generalFrame = ResourceHandler.INSTANCE.minotaur_one_walk;
                        break;
                    case ATTACKING:
                        this.generalFrame = ResourceHandler.INSTANCE.minotaur_one_attack;
                        break;
                    default:
                        this.generalFrame = ResourceHandler.INSTANCE.minotaur_one_idle;
                        break;
                }
                break;
            case MINOTAUR_TWO:
                switch (state) {
                    case IDLE:
                        this.generalFrame = ResourceHandler.INSTANCE.minotaur_two_idle;
                        break;
                    case MOVING_DOWN:
                    case MOVING_RIGHT:
                    case MOVING_LEFT:
                    case MOVING_UP:
                        this.generalFrame = ResourceHandler.INSTANCE.minotaur_two_walk;
                        break;
                    case ATTACKING:
                        this.generalFrame = ResourceHandler.INSTANCE.minotaur_two_attack;
                        break;
                    default:
                        this.generalFrame = ResourceHandler.INSTANCE.minotaur_two_idle;
                        break;
                }
                break;
            case MINOTAUR_THREE:
                switch (state) {
                    case IDLE:
                        this.generalFrame = ResourceHandler.INSTANCE.minotaur_three_idle;
                        break;
                    case MOVING_DOWN:
                    case MOVING_RIGHT:
                    case MOVING_LEFT:
                    case MOVING_UP:
                        this.generalFrame = ResourceHandler.INSTANCE.minotaur_three_walk;
                        break;
                    case ATTACKING:
                        this.generalFrame = ResourceHandler.INSTANCE.minotaur_three_attack;
                        break;
                    default:
                        this.generalFrame = ResourceHandler.INSTANCE.minotaur_three_idle;
                        break;
                }
                break;
        }
    }

    public void skillUsed() {
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
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

    public void hit(double dmgMultiplier) {
        System.out.println(dmgMultiplier + "dmg multiplier");
        if (health > 0.0) {
            this.lastHit = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(3);
            this.health -= 5 * dmgMultiplier;
            this.hurtSound.soundPlay();
        } else {
            this.alive = false;
            this.currentState = PlayerState.DEAD;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Rectangle getPlayerHitBox() {
        return playerHitBox;
    }

    public long getSkillCD() {
        return skillCD;
    }

    public void setSkillCD(long blinkCD) {
        this.skillCD = blinkCD;
    }

    public Skills getSkill() {
        return skillName;
    }

    public void setSkill(Skills skill) {
        this.skillName = skill;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public String getSkillDescription() {
        return skillDescription;
    }

    public void setSkillDescription(String skillDescription) {
        this.skillDescription = skillDescription;
    }

    public double getDmgMultiplier() { return dmgMultiplier;
    }

    public void setDmgMultiplier(double dmgMultiplier) {
        this.dmgMultiplier = dmgMultiplier;
    }
}
