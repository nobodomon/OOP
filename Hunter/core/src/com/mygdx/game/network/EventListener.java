package com.mygdx.game.network;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.screens.IngameScreen;
import com.mygdx.game.supers.PlayerState;
import com.mygdx.game.supers.PlayerType;
import com.mygdx.global.JoinResponseEvent;
import com.mygdx.global.PlayerAddEvent;
import com.mygdx.global.PlayerCharacterChangeEvent;
import com.mygdx.global.PlayerHitEvent;
import com.mygdx.global.PlayerKilledEvent;
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
                    MyGdxGame.getInstance().setScreen(IngameScreen.INSTANCE);
                }
            });
        } else if (object instanceof PlayerAddEvent) {

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    PlayerAddEvent playerAddEvent = (PlayerAddEvent) object;

                    final Player player = new Player(playerAddEvent.username, PlayerType.GHOST_ONE);
                    player.getPosition().x = playerAddEvent.x;
                    player.getPosition().y = playerAddEvent.y;
                    player.getServerPosition().x = playerAddEvent.x;
                    player.getServerPosition().y = playerAddEvent.y;

                    PlayerHandler.INSTANCE.addPlayer(player);
                }
            });
        } else if (object instanceof PlayerRemoveEvent) {

            PlayerHandler.INSTANCE.removePlayer(PlayerHandler.INSTANCE.getPlayerByUsername(((PlayerRemoveEvent) object).username));

        } else if (object instanceof PlayerUpdateEvent) {
            final PlayerUpdateEvent playerUpdateEvent = (PlayerUpdateEvent) object;

            final Player player = PlayerHandler.INSTANCE.getPlayerByUsername(playerUpdateEvent.username);
            if (player == null) return;

            PlayerState state = Player.getStateByInt(playerUpdateEvent.state);
            player.setCurrentState(state);
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
        /*else if (object instanceof PlayerHitEvent) {
            final PlayerHitEvent playerHitEvent = (PlayerHitEvent) object;

            final Player player = PlayerHandler.INSTANCE.getPlayerByUsername(playerHitEvent.username);
            if (player == null) return;

            player.setCurrentState(PlayerState.HIT);
            System.out.println(player.getUsername() + " is hit");

        } else if (object instanceof PlayerKilledEvent) {
            final PlayerKilledEvent playerKilledEvent = (PlayerKilledEvent) object;

            final Player player = PlayerHandler.INSTANCE.getPlayerByUsername(playerKilledEvent.username);
            if (player == null) return;

            player.setCurrentState(PlayerState.DEAD);
            System.out.println(player.getUsername() + " is dead");
        }*/

        super.received(connection, object);
    }
}
