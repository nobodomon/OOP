package com.mygdx.game.supers.skills;

import com.mygdx.game.supers.PlayerState;
import com.mygdx.game.supers.Skill;
import com.mygdx.game.supers.Skills;
import com.mygdx.server.supers.ServerPlayer;

import java.util.concurrent.TimeUnit;

public class DashSkill extends Skill {

    public DashSkill(Skills skillName, int skillCooldown, int skillDuration, ServerPlayer skillUser) {
        super(skillName, skillCooldown, skillDuration, skillUser);
    }

    @Override
    public void useSkill() {
        super.useSkill();
        float blinkSpeed = skillUser.getSpeed() * 50;
        long now = System.currentTimeMillis();
        if (skillUser.moveLeft) {
            if (now > getNextAvailableUsage()) {
                if (skillUser.getX() - blinkSpeed < 0) {
                    skillUser.setX(0);
                } else {

                    skillUser.setX(skillUser.getX() - blinkSpeed);
                }
                setNextAvailableUsage(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(getSkillCooldown()));
            }
            skillUser.setServerState(PlayerState.MOVING_LEFT);
        } else if (skillUser.moveRight) {

            if (now > getNextAvailableUsage()) {
                if (skillUser.getX() + blinkSpeed > 1150) {
                    skillUser.setX(1150);
                } else {
                    skillUser.setX(skillUser.getX() + blinkSpeed);
                }
                setNextAvailableUsage(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(getSkillCooldown()));
            }
            skillUser.setServerState(PlayerState.MOVING_RIGHT);
        }
        if (skillUser.moveUp) {
            if (now > getNextAvailableUsage()) {
                if (skillUser.getY() + blinkSpeed > 720) {
                    skillUser.setY(720);
                } else {
                    skillUser.setY(skillUser.getY() + blinkSpeed);
                }
                setNextAvailableUsage(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(getSkillCooldown()));
            }
            skillUser.setServerState(PlayerState.MOVING_UP);
        } else if (skillUser.moveDown) {

            if (now > getNextAvailableUsage()) {
                if (skillUser.getY() - blinkSpeed < 0) {
                    skillUser.setY(0);
                } else {
                    skillUser.setY(skillUser.getY() -  blinkSpeed);
                }
                setNextAvailableUsage(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(getSkillCooldown()));
            }
            skillUser.setServerState(PlayerState.MOVING_DOWN);
        }
    }
}
