package com.mygdx.game.supers.skills;

import com.mygdx.game.supers.Skill;
import com.mygdx.game.supers.Skills;
import com.mygdx.server.handlers.CapturePointHandler;
import com.mygdx.server.supers.ServerCapturePoint;
import com.mygdx.server.supers.ServerPlayer;

import java.util.concurrent.TimeUnit;

public class BlinkToPoint extends Skill {
    public BlinkToPoint(Skills skillName, int skillCooldown, int skillDuration, ServerPlayer serverPlayer) {
        super(skillName, skillCooldown, skillDuration, serverPlayer);
        setSkillDescription("Allows the player to blink to the nearest capture point.");
    }

    public BlinkToPoint(ServerPlayer skillUser) {
        super(Skills.BLINKTOPOINT, 15, 1, skillUser);
        setSkillDescription("Allows the player to blink to the nearest capture point.");
    }

    @Override
    public void useSkill() {
        super.useSkill();
        if (getNextAvailableUsage() < System.currentTimeMillis()) {

            double nearestPoint;
            float x;
            float y;
            ServerCapturePoint capturePoint = CapturePointHandler.INSTANCE.getCapturePoints().get(0);
            //Point x^2 + point y^2 = diagonal distance ^2
            nearestPoint = Math.sqrt(Math.pow(capturePoint.getX() - skillUser.getX(), 2) + Math.pow(capturePoint.getY() - skillUser.getY(), 2));
            x = capturePoint.getX();
            y = capturePoint.getY();
            for (int i = 1; i < CapturePointHandler.INSTANCE.getCapturePoints().size(); i++) {
                ServerCapturePoint currPoint = CapturePointHandler.INSTANCE.getCapturePoints().get(i);
                double nextPoint = Math.sqrt(Math.pow(currPoint.getX() - skillUser.getX(), 2) + Math.pow(currPoint.getY() - skillUser.getY(), 2));
                if (nextPoint < nearestPoint) {
                    nearestPoint = nextPoint;
                    x = currPoint.getX();
                    y = currPoint.getY();
                } else {

                }
            }
            skillUser.setX(x);
            skillUser.setY(y);
            setNextAvailableUsage(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(getSkillDuration()) + TimeUnit.SECONDS.toMillis(getSkillCooldown()));
        }

    }
}
