package com.mygdx.game.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.supers.PlayerType;

public class ResourceHandler {

    public final static ResourceHandler INSTANCE = new ResourceHandler();

    public final Texture mud_one = new Texture(Gdx.files.internal("Ground_Tile_01_C.png"));
    public final Texture mud_two = new Texture(Gdx.files.internal("Ground_Tile_02_C.png"));
    public final Texture gravel_one = new Texture(Gdx.files.internal("Ground_Tile_01_A.png"));
    public final Texture gravel_two = new Texture(Gdx.files.internal("Ground_Tile_02_A.png"));
    public final Texture grass_one = new Texture(Gdx.files.internal("Ground_Tile_01_B.png"));
    public final Texture grass_two = new Texture(Gdx.files.internal("Ground_Tile_02_B.png"));

    public final Texture capturePoint_zero = new Texture(Gdx.files.internal("Dot_C.png"));
    public final Texture capturePoint_half = new Texture(Gdx.files.internal("Dot_B.png"));
    public final Texture capturePoint_complete = new Texture(Gdx.files.internal("Artifact.png"));

    public final Animation<TextureAtlas.AtlasRegion> ghost_one_idle =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("ghost_1_idle.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> ghost_one_walk =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("ghost_1_walking.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> ghost_one_attack =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("ghost_1_attack.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> ghost_one_hit =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("ghost_1_hit.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> ghost_one_dead =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("ghost_1_dead.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> ghost_two_idle =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("ghost_2_idle.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> ghost_two_walk =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("ghost_2_walking.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> ghost_two_attack =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("ghost_2_attack.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> ghost_two_hit =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("ghost_2_hit.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> ghost_two_dead =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("ghost_2_dead.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> ghost_three_idle =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("ghost_3_idle.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> ghost_three_walk =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("ghost_3_walking.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> ghost_three_attack =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("ghost_3_attack.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> ghost_three_hit =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("ghost_3_hit.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> ghost_three_dead =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("ghost_3_dead.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> minotaur_one_idle =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("minotaur_1_idle.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> minotaur_one_walk =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("minotaur_1_walking.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> minotaur_one_attack =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("minotaur_1_attack.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> minotaur_two_idle =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("minotaur_2_idle.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> minotaur_two_walk =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("minotaur_2_walking.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> minotaur_two_attack =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("minotaur_2_attack.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> minotaur_three_idle =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("minotaur_3_idle.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> minotaur_three_walk =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("minotaur_3_walking.atlas")).getRegions());

    public final Animation<TextureAtlas.AtlasRegion> minotaur_three_attack =
            new Animation<>(1 / 30F, new TextureAtlas(Gdx.files.internal("minotaur_3_attack.atlas")).getRegions());

    public final float getHunterAttackDuration(){
        return (minotaur_one_attack.getAnimationDuration() + minotaur_two_attack.getAnimationDuration() + minotaur_three_attack.getAnimationDuration())/3;
    }


    public Texture getRandomMapTexture(int i){

        switch(i){
            case 0:
                return grass_one;
            case 1:
                return grass_two;
            case 2:
                return mud_one;
            case 3:
                return mud_two;
            case 4:
                return gravel_one;
            case 5:
                return gravel_two;
            default:
                return grass_one;
        }
    }
}


