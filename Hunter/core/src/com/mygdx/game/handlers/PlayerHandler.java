package com.mygdx.game.handlers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.screens.ConnectScreen;
import com.mygdx.game.supers.Player;
import com.mygdx.game.supers.PlayerState;
import com.mygdx.global.PlayerHitEvent;
import com.mygdx.global.PlayerKilledEvent;
import com.mygdx.global.PlayerReadyEvent;
import com.mygdx.global.PlayerUpdateEvent;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class PlayerHandler implements EntityHandler {


    public long attackCooldown;

    public static final PlayerHandler INSTANCE = new PlayerHandler();

    private LinkedList<Player> players = new LinkedList<>();

    @Override
    public void render(final Batch batch) {
        Player hunter = null;
        Rectangle hunterHitBox = null;
        this.attackCooldown = TimeUnit.SECONDS.toSeconds((long) ResourceHandler.INSTANCE.getHunterAttackDuration());

        for (int i = 0; i < this.players.size(); i++) {
            if (Player.getIntByType(this.players.get(i).getPlayerType()) > 2) {
                hunter = this.players.get(i);
                hunterHitBox = hunter.getPlayerHitBox();
                break;
            } else {
            }
        }
        if (hunter != null && hunterHitBox != null && hunter.getCurrentState() == PlayerState.ATTACKING) {
            for (int i = 0; i < this.players.size(); i++) {
                if (this.players.get(i).getUsername() != hunter.getUsername()) {
                    if (hunterHitBox.overlaps(this.players.get(i).getPlayerHitBox())) {
                        if (this.players.get(i).getHealth() > 0) {
                            if (this.players.get(i).getLastHit() - System.currentTimeMillis() < 0) {
                                System.out.println(this.players.get(i).getLastHit());
                                this.players.get(i).hit(hunter.getDmgMultiplier());
                                /*System.out.println(hunter.getDmgMultiplier());*/
                                playerHit(this.players.get(i));
                            }
                        } else if (this.players.get(i).getHealth() == 0) {
                            playerKilled(this.players.get(i));
                        }
                    }
                } else {
                    continue;
                }
            }
        }

        for (int i = 0; i < this.players.size(); i++) {
            this.players.get(i).render(batch);
        }
    }


    @Override
    public void update(final float delta) {
        for (int i = 0; i < this.players.size(); i++) {
            this.players.get(i).update(delta);
        }
    }

    public Player getPlayerByUsername(final String username) {
        for (int i = 0; i < this.players.size(); i++) {
            final Player player = this.players.get(i);

            if (player.getUsername().equals(username))
                return player;
        }
        return null;
    }

    public void playerHit(final Player player) {
        PlayerUpdateEvent playerUpdateEvent = new PlayerUpdateEvent();
        playerUpdateEvent.username = player.getUsername();
        playerUpdateEvent.state = Player.getIntByState(PlayerState.HIT);
        playerUpdateEvent.health = player.getHealth();
        playerUpdateEvent.lastHit = player.getLastHit();
        playerUpdateEvent.x = player.getServerPosition().x;
        playerUpdateEvent.y = player.getServerPosition().y;
        MyGdxGame.getInstance().getClient().sendTCP(playerUpdateEvent);

        PlayerHitEvent playerHitEvent = new PlayerHitEvent();
        playerHitEvent.username = player.getUsername();
        playerHitEvent.hit = true;
        MyGdxGame.getInstance().getClient().sendTCP(playerHitEvent);
    }

    public void playerKilled(final Player player) {
        PlayerUpdateEvent playerUpdateEvent = new PlayerUpdateEvent();
        playerUpdateEvent.username = player.getUsername();
        playerUpdateEvent.state = Player.getIntByState(PlayerState.DEAD);
        playerUpdateEvent.health = player.getHealth();
        playerUpdateEvent.lastHit = player.getLastHit();
        playerUpdateEvent.x = player.getServerPosition().x;
        playerUpdateEvent.y = player.getServerPosition().y;
        MyGdxGame.getInstance().getClient().sendTCP(playerUpdateEvent);

        PlayerKilledEvent playerKilledEvent = new PlayerKilledEvent();
        playerKilledEvent.username = player.getUsername();
        playerKilledEvent.dead = true;
        MyGdxGame.getInstance().getClient().sendTCP(playerKilledEvent);
    }

    public void resetPlayerHP() {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            player.setHealth(25);
            player.setLastHit(0);
            player.setAlive(true);
            playerReset(player);
        }
    }

    public void playerReset(final Player player) {
        PlayerUpdateEvent playerUpdateEvent = new PlayerUpdateEvent();
        playerUpdateEvent.username = player.getUsername();
        playerUpdateEvent.health = player.getHealth();
        playerUpdateEvent.lastHit = 0;
        playerUpdateEvent.x = player.getServerPosition().x;
        playerUpdateEvent.y = player.getServerPosition().y;
        playerUpdateEvent.state = Player.getIntByState(player.getCurrentState());
        MyGdxGame.getInstance().getClient().sendTCP(playerUpdateEvent);
    }

    public void clearPlayers() {
        this.players = new LinkedList<>();
    }

    public void addPlayer(final Player player) {
        if (getPlayerByUsername(player.getUsername()) != null) {
            ConnectScreen.INSTANCE.getErrorLabel().setText("Username is taken!");
            return;
        } else {

            this.players.add(player);
        }
    }

    public void removePlayer(final Player player) {
        this.players.remove(player);
    }

    public int[] getPlayerCount() {
        int hunters = 0;
        int ghosts = 0;
        for (int i = 0; i < this.players.size(); i++) {
            if (Player.getIntByType(this.players.get(i).getPlayerType()) > 2) {
                hunters++;
            } else {
                ghosts++;
            }
        }
        int[] players = {hunters, ghosts};
        return players;
    }

    public int[] getReadyCount() {
        int ready = 0;
        int total = 0;
        for (int i = 0; i < this.players.size(); i++) {
            total++;
            if (this.players.get(i).isReady()) {
                ready++;
            }
        }
        int[] players = {ready, total};
        return players;
    }

    public boolean areAllSurvivorsDead() {
        for (int i = 0; i < this.players.size(); i++) {
            if (Player.getIntByType(this.players.get(i).getPlayerType()) < 3) {
                if (this.players.get(i).getHealth() > 0.0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void unreadyAll() {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setReady(false);
            PlayerReadyEvent playerReadyEvent = new PlayerReadyEvent();
            playerReadyEvent.username = players.get(i).getUsername();
            playerReadyEvent.ready = players.get(i).isReady();
            MyGdxGame.getInstance().getClient().sendTCP(playerReadyEvent);
        }
    }

    public LinkedList<Player> getPlayers() {
        return players;
    }
}
