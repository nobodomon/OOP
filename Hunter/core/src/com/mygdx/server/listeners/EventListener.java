package com.mygdx.server.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.supers.Player;
import com.mygdx.global.MoveUpdateEvent;
import com.mygdx.global.PlayerCharacterChangeEvent;
import com.mygdx.server.handlers.PlayerHandler;
import com.mygdx.server.supers.ServerPlayer;

public class EventListener extends Listener {

    @Override
    public void received(Connection connection, Object object) {
        super.received(connection, object);

        if(object instanceof MoveUpdateEvent){
            final ServerPlayer serverPlayer = PlayerHandler.INSTANCE.getPlayerByConnection(connection);

            final MoveUpdateEvent moveUpdateEvent = (MoveUpdateEvent) object;

            serverPlayer.moveUp = moveUpdateEvent.moveUp;
            serverPlayer.moveDown = moveUpdateEvent.moveDown;
            serverPlayer.moveLeft = moveUpdateEvent.moveLeft;
            serverPlayer.moveRight = moveUpdateEvent.moveRight;
            serverPlayer.attack = moveUpdateEvent.attack;
        }else if(object instanceof PlayerCharacterChangeEvent){
            System.out.println("Attempting to change skin");
            final ServerPlayer serverPlayer = PlayerHandler.INSTANCE.getPlayerByConnection(connection);

            final PlayerCharacterChangeEvent playerCharacterChangeEvent = (PlayerCharacterChangeEvent) object;
            serverPlayer.setPlayerType(Player.getTypeByInt(playerCharacterChangeEvent.characterType));

            final Player player = com.mygdx.game.handlers.PlayerHandler.INSTANCE.getPlayerByUsername(serverPlayer.getUsername());
            player.setPlayerType(Player.getTypeByInt(playerCharacterChangeEvent.characterType));
        }
    }
}
