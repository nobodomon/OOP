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
    }

    @Override
    public void useSkill() {
        super.useSkill();
        if(System.currentTimeMillis() > getNextAvailableUsage()) {
            for (int i = 0; i < PlayerHandler.INSTANCE.getPlayers().size(); i++) {
                ServerPlayer player = PlayerHandler.INSTANCE.getPlayers().get(i);
                if (Player.getIntByType(player.getPlayerType()) < 3) {
                    player.setStatus(PlayerStatus.STUNNED);
                }
            }
            setSkillEndDuration(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(getSkillDuration()));
            setNextAvailableUsage(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(getSkillDuration()) + TimeUnit.SECONDS.toMillis(getSkillCooldown()));
        }
        skillUser.setServerState(PlayerState.ATTACKING);
    }

    @Override
    public void revertSkill() {
        super.revertSkill();
        for (int i = 0; i < PlayerHandler.INSTANCE.getPlayers().size(); i++) {
            ServerPlayer player = PlayerHandler.INSTANCE.getPlayers().get(i);
            if (Player.getIntByType(player.getPlayerType()) < 3) {
                player.setStatus(PlayerStatus.NONE);
            }
        }
    }
}
