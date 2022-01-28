package com.mygdx.game.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.handlers.CapturePointHandler;
import com.mygdx.game.screens.GameInProgressScreen;
import com.mygdx.game.screens.LobbyScreen;
import com.mygdx.game.supers.CapturePoint;
import com.mygdx.game.supers.PlayerState;
import com.mygdx.game.supers.PlayerStatus;
import com.mygdx.game.supers.PlayerType;
import com.mygdx.global.CapturePointCreateEvent;
import com.mygdx.global.CapturePointDeleteEvent;
import com.mygdx.global.GameRestartEvent;
import com.mygdx.global.GameStartEvent;
import com.mygdx.global.JoinResponseEvent;
import com.mygdx.global.PlayerAddEvent;
import com.mygdx.global.PlayerCharacterChangeEvent;
import com.mygdx.global.PlayerReadyEvent;
import com.mygdx.global.PlayerRemoveEvent;
import com.mygdx.global.PlayerUpdateEvent;
import com.mygdx.game.handlers.PlayerHandler;
import com.mygdx.game.supers.Player;

public class EventListener extends Listener {
    @Override
    public void received(Connection connection, final Object object) {
        if (object instanceof JoinResponseEvent) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    LobbyScreen.INSTANCE.setPlayingPlayer(((JoinResponseEvent) object).username);
                    GameInProgressScreen.INSTANCE.setPlayingPlayer(((JoinResponseEvent) object).username);
                    MyGdxGame.getInstance().setScreen(LobbyScreen.INSTANCE);
                }
            });
        }
        else if(object instanceof GameStartEvent){
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    GameInProgressScreen.INSTANCE.resetGame();
                    MyGdxGame.getInstance().setScreen(GameInProgressScreen.INSTANCE);
                }
            });
        }else if (object instanceof GameRestartEvent){
            PlayerHandler.INSTANCE.unreadyAll();
            LobbyScreen.INSTANCE.reset();
            CapturePointHandler.instance.resetCapturePoints();
            PlayerHandler.INSTANCE.resetPlayerHP();
            GameInProgressScreen.INSTANCE.resetGame();
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    MyGdxGame.getInstance().setScreen(LobbyScreen.INSTANCE);
                }
            });
        }
        else if (object instanceof PlayerAddEvent) {

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    PlayerAddEvent playerAddEvent = (PlayerAddEvent) object;

                    final Player player = new Player(playerAddEvent.username);
                    player.getPosition().x = playerAddEvent.x;
                    player.getPosition().y = playerAddEvent.y;
                    player.setHealth(playerAddEvent.health);
                    player.setLastHit(playerAddEvent.lastHit);
                    player.getServerPosition().x = playerAddEvent.x;
                    player.getServerPosition().y = playerAddEvent.y;

                    PlayerHandler.INSTANCE.addPlayer(player);
                }
            });
        }else if(object instanceof CapturePointCreateEvent){
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    CapturePointCreateEvent capturePointCreateEvent = (CapturePointCreateEvent) object;
                    Vector2 position = new Vector2();
                    position.set(capturePointCreateEvent.x,capturePointCreateEvent.y);
                    final CapturePoint capturePoint = new CapturePoint(position);

                    CapturePointHandler.instance.addCapturePoint(capturePoint);
                }
            });
        }else if(object instanceof CapturePointDeleteEvent){
            CapturePointDeleteEvent capturePointDeleteEvent = (CapturePointDeleteEvent) object;
            CapturePointHandler.instance.removeCapturePoint(CapturePointHandler.instance.getCapturePointByVector(capturePointDeleteEvent.x,capturePointDeleteEvent.y));

        } else if (object instanceof PlayerRemoveEvent) {

            PlayerHandler.INSTANCE.removePlayer(PlayerHandler.INSTANCE.getPlayerByUsername(((PlayerRemoveEvent) object).username));

        }else if (object instanceof PlayerUpdateEvent) {
            final PlayerUpdateEvent playerUpdateEvent = (PlayerUpdateEvent) object;

            final Player player = PlayerHandler.INSTANCE.getPlayerByUsername(playerUpdateEvent.username);
            if (player == null) return;

            PlayerState state = Player.getStateByInt(playerUpdateEvent.state);
            player.setCurrentState(state);
            PlayerStatus status = Player.getStatusByInt(playerUpdateEvent.status);
            player.setStatus(status);
            player.setHealth(playerUpdateEvent.health);
            player.setLastHit(playerUpdateEvent.lastHit);
            player.setSkillCD(playerUpdateEvent.skillCD);
            player.setSkill(playerUpdateEvent.skillname);
            player.getServerPosition().set(playerUpdateEvent.x, playerUpdateEvent.y);
        } else if (object instanceof PlayerCharacterChangeEvent) {
            final PlayerCharacterChangeEvent playerCharacterChangeEvent = (PlayerCharacterChangeEvent) object;

            final Player player = PlayerHandler.INSTANCE.getPlayerByUsername(playerCharacterChangeEvent.username);
            if (player == null) return;

            PlayerType type = Player.getTypeByInt(playerCharacterChangeEvent.characterType);
            player.setPlayerType(type);
        } else if (object instanceof PlayerReadyEvent) {
            final PlayerReadyEvent playerReadyEvent = (PlayerReadyEvent) object;

            final Player player = PlayerHandler.INSTANCE.getPlayerByUsername(playerReadyEvent.username);
            if (player == null) return;

            player.setReady(playerReadyEvent.ready);
        }

        super.received(connection, object);
    }
}
