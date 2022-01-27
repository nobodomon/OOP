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
    }

    @Override
    public void useSkill() {
        super.useSkill();
        if(System.currentTimeMillis() > getNextAvailableUsage()){
            skillUser.setSpeed(skillUser.getSpeed() * 2.5f);
            setSkillEndDuration(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(getSkillDuration()));
            setNextAvailableUsage(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(getSkillDuration()) +TimeUnit.SECONDS.toMillis(getSkillCooldown()));
        }
    }

    @Override
    public void revertSkill() {
        super.revertSkill();
        skillUser.setSpeed(prevSpeed);
    }
}
