package com.mygdx.game.supers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.server.supers.ServerPlayer;

public abstract class Skill {
    private Skills skillName;
    private int skillCooldown;
    private int skillDuration;
    private String skillDescription;
    public ServerPlayer skillUser;

    public long getNextAvailableUsage() {
        return nextAvailableUsage;
    }

    public void setNextAvailableUsage(long nextAvailableUsage) {
        this.nextAvailableUsage = nextAvailableUsage;
    }

    private long nextAvailableUsage;
    private long skillEndDuration;

    public Skill(Skills skillName, int skillCooldown, int skillDuration, ServerPlayer serverPlayer) {
        this.skillName = skillName;
        this.skillCooldown = skillCooldown;
        this.skillDuration = skillDuration;
        this.skillUser = serverPlayer;
        this.skillEndDuration = 0;
        this.nextAvailableUsage = 0;
    }

    public Skill() {

    }

    public Skills getSkillName() {
        return skillName;
    }

    public void setSkillName(Skills skillName) {
        this.skillName = skillName;
    }

    public abstract void useSkill();

    public abstract void revertSkill();

    public int getSkillCooldown() {
        return skillCooldown;
    }


    public int getSkillDuration() {
        return skillDuration;
    }

    public long getSkillEndDuration() {
        return skillEndDuration;
    }

    public void setSkillEndDuration(long skillEndDuration) {
        this.skillEndDuration = skillEndDuration;
    }

    public String getSkillDescription() {
        return skillDescription;
    }

    public void setSkillDescription(String skillDescription) {
        this.skillDescription = skillDescription;
    }

    public static Texture getSkillIcon(Skills skillName, boolean cooldown) {
        if (cooldown == false) {
            switch (skillName) {
                case DASH:
                    return new Texture(Gdx.files.internal("dash.png"));
                case BLINKTOPOINT:
                    return new Texture(Gdx.files.internal("blink.png"));
                case SPEEDBOOST:
                    return new Texture(Gdx.files.internal("speedboost.png"));
                case MASSSTUN:
                    return new Texture(Gdx.files.internal("massstun.png"));
                case DMGUP:
                    return new Texture(Gdx.files.internal("dmgUp.png"));
                default:
                    return new Texture(Gdx.files.internal("dash.png"));
            }
        } else {
            switch (skillName) {
                case DASH:
                    return new Texture(Gdx.files.internal("dash-cd.png"));
                case BLINKTOPOINT:
                    return new Texture(Gdx.files.internal("blink-cd.png"));
                case SPEEDBOOST:
                    return new Texture(Gdx.files.internal("speedboost-cd.png"));
                case MASSSTUN:
                    return new Texture(Gdx.files.internal("massstun-cd.png"));
                case DMGUP:
                    return new Texture(Gdx.files.internal("dmgUp-cd.png"));
                default:
                    return new Texture(Gdx.files.internal("dash-cd.png"));
            }
        }
    }
}
