package com.mygdx.game.handlers;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface EntityHandler {
    public void render(final Batch batch);

    public void update(final float delta);
}
