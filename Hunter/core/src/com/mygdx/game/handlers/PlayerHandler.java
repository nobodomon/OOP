package com.mygdx.game.handlers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.supers.Player;
import com.mygdx.game.supers.PlayerState;
import com.mygdx.global.PlayerHitEvent;
import com.mygdx.global.PlayerKilledEvent;
import com.mygdx.global.PlayerUpdateEvent;

import java.util.LinkedList;

public class PlayerHandler {


    public double attackCooldown;
    public double lastAttack = 0;

    public static final PlayerHandler INSTANCE = new PlayerHandler();

    private final LinkedList<Player> players = new LinkedList<>();

    public Player getPlayerByUsername(final String username){
        for(int i = 0; i < this.players.size(); i++){
            final Player player = this.players.get(i);

            if(player.getUsername().equals(username))
                return player;
        }
        return null;
    }



    public void render(final Batch batch){
        Player hunter = null;
        Rectangle hunterHitBox = null;
        double now = System.currentTimeMillis();
        this.attackCooldown = ResourceHandler.INSTANCE.getHunterAttackDuration() * 10000F;

        for(int i = 0; i < this.players.size(); i++){
            if(Player.getIntByType(this.players.get(i).getPlayerType()) > 2){
                hunter = this.players.get(i);
                hunterHitBox = this.players.get(i).getPlayerHitBox();
                break;
            }else{
                hunter = null;
                hunterHitBox = null;
            }

        }
        if(hunter != null && hunterHitBox != null && hunter.getCurrentState() == PlayerState.ATTACKING){
            for(int i = 0; i< this.players.size(); i++){
                if(this.players.get(i).getUsername() != hunter.getUsername()){
                    if(hunterHitBox.overlaps(this.players.get(i).getPlayerHitBox())){
                        if(this.players.get(i).getHealth() > 0){
                            if (now - attackCooldown > lastAttack) {
                                this.lastAttack = System.currentTimeMillis();
                                this.players.get(i).hit();
                                playerHit(this.players.get(i));
                            }
                        }else if(this.players.get(i).getHealth() == 0){
                            playerKilled(this.players.get(i));
                        }
                    }
                }else{
                    continue;
                }
            }
        }

        for(int i = 0; i < this.players.size(); i++){
            this.players.get(i).render(batch);
        }
    }

    public void update(final float delta){
        for(int i = 0; i < this.players.size(); i++){
            this.players.get(i).update(delta);
        }
    }

    public void playerHit(final Player player){
        PlayerUpdateEvent playerUpdateEvent = new PlayerUpdateEvent();
        playerUpdateEvent.username = player.getUsername();
        playerUpdateEvent.state = Player.getIntByState(PlayerState.HIT);
        playerUpdateEvent.x = player.getServerPosition().x;
        playerUpdateEvent.y = player.getServerPosition().y;
        MyGdxGame.getInstance().getClient().sendTCP(playerUpdateEvent);

        PlayerHitEvent playerHitEvent = new PlayerHitEvent();
        playerHitEvent.username = player.getUsername();
        playerHitEvent.hit = true;
        MyGdxGame.getInstance().getClient().sendTCP(playerHitEvent);
    }

    public void playerKilled(final Player player){
        PlayerUpdateEvent playerUpdateEvent = new PlayerUpdateEvent();
        playerUpdateEvent.username = player.getUsername();
        playerUpdateEvent.state = Player.getIntByState(PlayerState.DEAD);
        playerUpdateEvent.x = player.getServerPosition().x;
        playerUpdateEvent.y = player.getServerPosition().y;
        MyGdxGame.getInstance().getClient().sendTCP(playerUpdateEvent);

        PlayerKilledEvent playerKilledEvent = new PlayerKilledEvent();
        playerKilledEvent.username = player.getUsername();
        playerKilledEvent.dead = true;
        MyGdxGame.getInstance().getClient().sendTCP(playerKilledEvent);
    }

    public void addPlayer(final Player player){
        this.players.add(player);
    }

    public void removePlayer(final Player player){
        this.players.remove(player);
    }

    public int[] getPlayerCount(){
        int hunters = 0;
        int ghosts = 0;
        for(int i = 0; i < this.players.size(); i++){
            if(Player.getIntByType(this.players.get(i).getPlayerType()) > 2){
                hunters++;
            }else{
                ghosts++;
            }
        }
        int[] players = {hunters,ghosts};
        return players;
    }

    public int[] getReadyCount(){
        int ready = 0;
        int total = 0;
        for(int i = 0; i < this.players.size(); i++){
            total++;
            if(this.players.get(i).isReady()){
                ready++;
            }
        }
        int[] players = {ready,total};
        return players;
    }
}
