package com.mygdx.game.supers;

import com.mygdx.server.supers.ServerPlayer;

public class Skill {
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

    public Skills getSkillName() {
        return skillName;
    }

    public void useSkill(){

    }

    public void revertSkill(){

    }

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
}
