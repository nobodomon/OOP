package com.mygdx.game.supers;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface Entity {
    public void render(final Batch batch);

    public void update(final float delta);
}
