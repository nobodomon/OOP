package com.mygdx.game.supers.skills;

import com.mygdx.game.supers.Skill;
import com.mygdx.game.supers.Skills;
import com.mygdx.server.supers.ServerPlayer;

import java.util.concurrent.TimeUnit;

public class SpeedBoostSkill extends Skill {
    float prevSpeed;

    public SpeedBoostSkill(Skills skillName, int skillCooldown, int skillDuration, ServerPlayer skillUser) {
        super(skillName, skillCooldown, skillDuration, skillUser);
        this.prevSpeed = skillUser.getSpeed();

        setSkillDescription("Grants the player a speed boost of 2.5x for " + skillDuration + " seconds.");
    }

    public SpeedBoostSkill(ServerPlayer skillUser) {
        super(Skills.SPEEDBOOST, 15, 3, skillUser);
        this.prevSpeed = skillUser.getSpeed();
        setSkillDescription("Grants the player a speed boost of 2.5x for " + 3 + " seconds.");
    }

    @Override
    public void useSkill() {
        if (System.currentTimeMillis() > getNextAvailableUsage()) {
            skillUser.setSpeed(skillUser.getSpeed() * 2.5f);
            setSkillEndDuration(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(getSkillDuration()));
            setNextAvailableUsage(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(getSkillDuration()) + TimeUnit.SECONDS.toMillis(getSkillCooldown()));
            super.playSkillSound();
        }
    }

    @Override
    public void revertSkill() {
        skillUser.setSpeed(prevSpeed);
    }
}
