package com.mygdx.game.supers;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface Entity {
    void render(final Batch batch);

    void update(final float delta);
}
