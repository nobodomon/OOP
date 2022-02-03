package com.mygdx.game.supers.skills;

import com.mygdx.game.supers.Skill;
import com.mygdx.game.supers.Skills;
import com.mygdx.server.supers.ServerPlayer;

import java.util.concurrent.TimeUnit;

public class dmgUp extends Skill {

    public dmgUp(Skills skillName, int skillCooldown, int skillDuration, ServerPlayer skillUser) {
        super(skillName, skillCooldown, skillDuration, skillUser);
        setSkillDescription("Grants the player a damage boost of 2x for " + skillDuration + " seconds.");
    }

    public dmgUp(ServerPlayer skillUser) {
        super(Skills.DMGUP, 20, 3, skillUser);
        setSkillDescription("Grants the hunter a damage boost of 2x for " + 3 + " seconds.");
    }

    @Override
    public void useSkill() {
        super.useSkill();
        if (System.currentTimeMillis() > getNextAvailableUsage()) {
            skillUser.setDmgMultiplier(2);
            setSkillEndDuration(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(getSkillDuration()));
            setNextAvailableUsage(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(getSkillDuration()) + TimeUnit.SECONDS.toMillis(getSkillCooldown()));
        }
    }

    @Override
    public void revertSkill() {
        super.revertSkill();
        skillUser.setDmgMultiplier(1);
    }
}
