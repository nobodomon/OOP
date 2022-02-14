package com.mygdx.game.supers.skills;

import com.mygdx.game.supers.Player;
import com.mygdx.game.supers.PlayerState;
import com.mygdx.game.supers.PlayerStatus;
import com.mygdx.game.supers.Skill;
import com.mygdx.game.supers.Skills;
import com.mygdx.server.handlers.PlayerHandler;
import com.mygdx.server.supers.ServerPlayer;

import java.util.concurrent.TimeUnit;

public class MassStun extends Skill {
    public MassStun(Skills skillName, int skillCooldown, int skillDuration, ServerPlayer serverPlayer) {
        super(skillName, skillCooldown, skillDuration, serverPlayer);
        setSkillDescription("Hunter only skill that stuns all ghosts for " + skillDuration + " seconds.");
    }

    public MassStun(ServerPlayer skillUser) {
        super(Skills.MASSSTUN, 30, 3, skillUser);
        setSkillDescription("Hunter only skill that stuns all ghosts for " + 3 + " seconds.");
    }

    @Override
    public void useSkill() {
        if (System.currentTimeMillis() > getNextAvailableUsage()) {
            for (int i = 0; i < PlayerHandler.INSTANCE.getPlayers().size(); i++) {
                ServerPlayer player = PlayerHandler.INSTANCE.getPlayers().get(i);
                if (Player.getIntByType(player.getPlayerType()) < 3) {
                    player.setStatus(PlayerStatus.STUNNED);
                }
            }
            setSkillEndDuration(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(getSkillDuration()));
            setNextAvailableUsage(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(getSkillDuration()) + TimeUnit.SECONDS.toMillis(getSkillCooldown()));
            super.playSkillSound();
        }
        skillUser.setServerState(PlayerState.ATTACKING);
    }

    @Override
    public void revertSkill() {
        for (int i = 0; i < PlayerHandler.INSTANCE.getPlayers().size(); i++) {
            ServerPlayer player = PlayerHandler.INSTANCE.getPlayers().get(i);
            if (Player.getIntByType(player.getPlayerType()) < 3) {
                player.setStatus(PlayerStatus.NONE);
            }
        }
    }
}
