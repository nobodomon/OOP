package com.mygdx.server.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.supers.CapturePoint;
import com.mygdx.game.supers.Player;
import com.mygdx.game.supers.PlayerState;
import com.mygdx.global.CapturePointUpdateEvent;
import com.mygdx.global.GameRestartEvent;
import com.mygdx.global.GameStartEvent;
import com.mygdx.global.MoveUpdateEvent;
import com.mygdx.global.PlayerCharacterChangeEvent;
import com.mygdx.global.PlayerHitEvent;
import com.mygdx.global.PlayerKilledEvent;
import com.mygdx.global.PlayerReadyEvent;
import com.mygdx.global.PlayerUpdateEvent;
import com.mygdx.server.handlers.CapturePointHandler;
import com.mygdx.server.handlers.GameHandler;
import com.mygdx.server.handlers.PlayerHandler;
import com.mygdx.server.supers.ServerCapturePoint;
import com.mygdx.server.supers.ServerPlayer;

public class EventListener extends Listener {

    @Override
    public void received(Connection connection, Object object) {
        super.received(connection, object);
        if (object instanceof GameStartEvent) {
            GameHandler.INSTANCE.startGame();
        }else if(object instanceof GameRestartEvent){
            GameHandler.INSTANCE.restartGame();
        }else if (object instanceof MoveUpdateEvent) {
            final ServerPlayer serverPlayer = PlayerHandler.INSTANCE.getPlayerByConnection(connection);

            final MoveUpdateEvent moveUpdateEvent = (MoveUpdateEvent) object;

            serverPlayer.moveUp = moveUpdateEvent.moveUp;
            serverPlayer.moveDown = moveUpdateEvent.moveDown;
            serverPlayer.moveLeft = moveUpdateEvent.moveLeft;
            serverPlayer.moveRight = moveUpdateEvent.moveRight;
            serverPlayer.attack = moveUpdateEvent.attack;
            serverPlayer.shift = moveUpdateEvent.shift;

        } else if (object instanceof PlayerCharacterChangeEvent) {
            final ServerPlayer serverPlayer = PlayerHandler.INSTANCE.getPlayerByConnection(connection);

            final PlayerCharacterChangeEvent playerCharacterChangeEvent = (PlayerCharacterChangeEvent) object;
            serverPlayer.setPlayerType(Player.getTypeByInt(playerCharacterChangeEvent.characterType));

            final Player player = com.mygdx.game.handlers.PlayerHandler.INSTANCE.getPlayerByUsername(serverPlayer.getUsername());
            player.setPlayerType(Player.getTypeByInt(playerCharacterChangeEvent.characterType));
        } else if (object instanceof PlayerReadyEvent) {
            final ServerPlayer serverPlayer = PlayerHandler.INSTANCE.getPlayerByConnection(connection);

            final PlayerReadyEvent playerReadyEvent = (PlayerReadyEvent) object;
            serverPlayer.setReady(playerReadyEvent.ready);

            final Player player = com.mygdx.game.handlers.PlayerHandler.INSTANCE.getPlayerByUsername(serverPlayer.getUsername());
            player.setReady(playerReadyEvent.ready);

        } else if (object instanceof PlayerHitEvent) {

            final PlayerHitEvent playerHitEvent = (PlayerHitEvent) object;
            final ServerPlayer serverPlayer = PlayerHandler.INSTANCE.getPlayerByUsername(playerHitEvent.username);
            serverPlayer.hit = playerHitEvent.hit;
            serverPlayer.setServerState(PlayerState.HIT);


            final Player player = com.mygdx.game.handlers.PlayerHandler.INSTANCE.getPlayerByUsername(serverPlayer.getUsername());
            player.setCurrentState(PlayerState.HIT);

        } else if (object instanceof PlayerKilledEvent) {

            final PlayerKilledEvent playerKilledEvent = (PlayerKilledEvent) object;
            final ServerPlayer serverPlayer = PlayerHandler.INSTANCE.getPlayerByUsername(playerKilledEvent.username);
            serverPlayer.dead = playerKilledEvent.dead;
            serverPlayer.setServerState(PlayerState.DEAD);

            final Player player = com.mygdx.game.handlers.PlayerHandler.INSTANCE.getPlayerByUsername(serverPlayer.getUsername());
            player.setCurrentState(PlayerState.DEAD);
            player.setAlive(false);

        } else if (object instanceof PlayerUpdateEvent) {

            final PlayerUpdateEvent playerUpdateEvent = (PlayerUpdateEvent) object;
            final ServerPlayer serverPlayer = PlayerHandler.INSTANCE.getPlayerByUsername(playerUpdateEvent.username);
            serverPlayer.setHealth(playerUpdateEvent.health);
            serverPlayer.setLastHit(playerUpdateEvent.lastHit);
            serverPlayer.setX(playerUpdateEvent.x);
            serverPlayer.setY(playerUpdateEvent.y);
            serverPlayer.setServerState(Player.getStateByInt(playerUpdateEvent.state));

            final Player player = com.mygdx.game.handlers.PlayerHandler.INSTANCE.getPlayerByUsername(serverPlayer.getUsername());
            player.setHealth(serverPlayer.getHealth());
            player.setLastHit(serverPlayer.getLastHit());
            player.getServerPosition().x = serverPlayer.getX();
            player.getServerPosition().y = serverPlayer.getY();
            player.setCurrentState(serverPlayer.getServerState());

        } else if (object instanceof CapturePointUpdateEvent) {
            final CapturePointUpdateEvent capturePointUpdateEvent = (CapturePointUpdateEvent) object;
            final ServerCapturePoint serverCapturePoint = CapturePointHandler.INSTANCE.getCapturePointByVector(capturePointUpdateEvent.x, capturePointUpdateEvent.y);
            serverCapturePoint.setProgress(capturePointUpdateEvent.progress);

            final CapturePoint capturePoint = com.mygdx.game.handlers.CapturePointHandler.instance.getCapturePointByVector(serverCapturePoint.getX(), serverCapturePoint.getY());
            capturePoint.setProgress(serverCapturePoint.getProgress());
        }
    }
}
