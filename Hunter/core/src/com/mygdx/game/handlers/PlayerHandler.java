package com.mygdx.game.handlers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.supers.Player;

import java.util.LinkedList;

public class PlayerHandler {

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
        for(int i = 0; i < this.players.size(); i++){
            this.players.get(i).render(batch);
        }
    }

    public void update(final float delta){
        for(int i = 0; i < this.players.size(); i++){
            this.players.get(i).update(delta);
        }
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
}
