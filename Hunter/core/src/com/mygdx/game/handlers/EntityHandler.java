package com.mygdx.game.handlers;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface EntityHandler {
    void render(final Batch batch);

    void update(final float delta);
}
