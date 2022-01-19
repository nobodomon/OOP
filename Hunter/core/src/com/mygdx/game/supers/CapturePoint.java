package com.mygdx.game.supers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.handlers.FontSizeHandler;
import com.mygdx.game.handlers.ResourceHandler;

public class CapturePoint {
    private Vector2 position;
    private final SpriteBatch batch;

    private float progress;
    private Rectangle capturePointHitBox;
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
        this.layout = new GlyphLayout();
        this.font = FontSizeHandler.INSTANCE.getFont(18, Color.BLACK);
    }

    public void render(final Batch batch){

        this.batch.begin();
        float width = frame.getWidth()/2;
        float height = frame.getHeight()/2;
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

        this.font.draw(batch, this.progress + "%", this.position.x + width / 2F - this.layout.width / 2, this.position.y + height + 10);
        this.batch.end();
    }
    public void update(final float delta) {
        //this.animationTime += delta;
    }

    public Rectangle getCapturePointHitBox() {
        return capturePointHitBox;
    }

    public void attemptCapture(){
        if(this.progress < 100.0){

            this.progress += 0.125;
        }
        //System.out.println("Capturing!");
    }

    public void captureDecay(){
        if(this.progress < 50.0 && this.progress > 0.0){
            this.progress -= 0.125;
        }else if(this.progress >= 50.0 && this.progress < 100.0){
            if(this.progress == 50.0){

            }else{
                this.progress -= 0.25;
            }
        }else{

        }
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }
}
