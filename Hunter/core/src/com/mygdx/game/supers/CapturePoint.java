package com.mygdx.game.supers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.handlers.FontSizeHandler;
import com.mygdx.game.handlers.ResourceHandler;

public class CapturePoint implements Entity {
    private Vector2 position;
    private final SpriteBatch batch;

    private float progress;

    private float captureRate;
    private float decayRate;
    private float disruptRate;

    private Rectangle capturePointHitBox;

    private ShapeRenderer hitboxRenderer;

    private Texture frame;
    private GlyphLayout layout;
    private BitmapFont font;

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public CapturePoint(final Vector2 position){
        this.batch = new SpriteBatch();
        this.frame = ResourceHandler.INSTANCE.capturePoint_zero;
        this.position = position;
        capturePointHitBox = new Rectangle();
        hitboxRenderer = new ShapeRenderer();
        this.layout = new GlyphLayout();
        this.font = FontSizeHandler.INSTANCE.getFont(18, Color.BLACK);

        this.decayRate = 0.1f;
        this.captureRate = 0.2f;
        this.disruptRate = 0.1f;
    }

    @Override
    public void render(final Batch batch){

        this.batch.begin();
        float width = frame.getWidth();
        float height = frame.getHeight();
        this.capturePointHitBox = new Rectangle(this.position.x, this.position.y, width, height);
        if(progress < 50){
            this.frame = ResourceHandler.INSTANCE.capturePoint_zero;
        }else if(progress >= 50 && progress < 100){
            this.frame = ResourceHandler.INSTANCE.capturePoint_half;
        }else if(progress == 100){
            this.frame = ResourceHandler.INSTANCE.capturePoint_complete;
        }
        batch.draw(frame, this.position.x, this.position.y, width, height);
        this.layout.setText(this.font, this.progress + "%");
        this.font.draw(batch, this.progress + "%", this.position.x + width / 2F - this.layout.width / 2, this.position.y + height + 15);
        this.batch.end();
    }

    @Override
    public void update(final float delta) {
    }

    public Rectangle getCapturePointHitBox() {
        return capturePointHitBox;
    }

    public void attemptCapture(){
        if(this.progress < 100.0f){
            if(this.progress + captureRate > 100.0f){
                this.progress = 100.0f;
            }else{
                this.progress += captureRate;
            }
        }
        this.progress = Math.round(this.progress * 10.0f) / 10.0f;
    }

    public void attemptCapture(float captureRateMultiplier){
        if(this.progress < 100.0f){
            if(this.progress + (captureRate * captureRateMultiplier) > 100.0f){
                this.progress = 100.0f;
            }else{
                this.progress += (captureRate * captureRateMultiplier);
            }
        }
        this.progress = Math.round(this.progress * 10.0f) / 10.0f;
    }
    public void attemptDisrupt(){
        if(this.progress > 0.0 && this.progress < 100.0){
            if(this.progress - disruptRate < 0.0f){
                this.progress = 0.0f;
            }else{
                this.progress -= 0.1f;
            }
        }
        this.progress = Math.round(this.progress * 10.0f) / 10.0f;
    }

    public void captureDecay(){

        if(this.progress < 50.0f && this.progress > 0.0f){
            this.progress -= decayRate;
        }else if(this.progress >= 50.0f && this.progress < 100.0f){
            if(this.progress == 50.0f){

            }else if(this.progress - (decayRate * 2f) < 50.0f){
                this.progress = 50.0f;
            }else{

                this.progress -= decayRate * 2f;
            }
        }else{

        }

        this.progress = Math.round(this.progress * 10.0f) / 10.0f;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

}
