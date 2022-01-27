package com.mygdx.game.supers;

import com.mygdx.server.supers.ServerPlayer;

public class Skill {
    private Skills skillName;
    private int skillCooldown;
    private int skillDuration;
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

    public Skills getSkillName() {
        return skillName;
    }

    public void useSkill(){

    }

    public void revertSkill(){

    }


    public void setSkillName(Skills skillName) {
        this.skillName = skillName;
    }

    public int getSkillCooldown() {
        return skillCooldown;
    }

    public void setSkillCooldown(int skillCooldown) {
        this.skillCooldown = skillCooldown;
    }

    public int getSkillDuration() {
        return skillDuration;
    }

    public void setSkillDuration(int skillDuration) {
        this.skillDuration = skillDuration;
    }

    public long getSkillEndDuration() {
        return skillEndDuration;
    }

    public void setSkillEndDuration(long skillEndDuration) {
        this.skillEndDuration = skillEndDuration;
    }
}
