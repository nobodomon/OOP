package com.mygdx.server.handlers;

import com.mygdx.game.supers.Player;
import com.mygdx.game.supers.PlayerState;
import com.mygdx.global.PlayerCharacterChangeEvent;
import com.mygdx.global.PlayerUpdateEvent;
import com.mygdx.server.ServerFoundation;
import com.mygdx.server.supers.ServerPlayer;

public class PlayerUpdateHandler implements Runnable{

    public static final PlayerUpdateHandler INSTANCE = new PlayerUpdateHandler();

    private boolean running;

    public synchronized void start(){
        this.running = true;

        final Thread thread = new Thread(this);
        thread.start();
    }

    public void tick(){
        for(int i = 0; i < PlayerHandler.INSTANCE.getPlayers().size(); i++){
            // Update server player
            final ServerPlayer serverPlayer = PlayerHandler.INSTANCE.getPlayers().get(i);
            serverPlayer.update();

            // Send update to all clients
            final PlayerUpdateEvent playerUpdateEvent = new PlayerUpdateEvent();
            playerUpdateEvent.username = serverPlayer.getUsername();
            int state = Player.getIntByState(serverPlayer.getServerState());
            playerUpdateEvent.state = state;
            playerUpdateEvent.x = serverPlayer.getX();
            playerUpdateEvent.y = serverPlayer.getY();

            ServerFoundation.instance.getServer().sendToAllUDP(playerUpdateEvent);

            final PlayerCharacterChangeEvent characterChangeEvent = new PlayerCharacterChangeEvent();
            characterChangeEvent.username = serverPlayer.getUsername();
            int playerType = Player.getIntByType(serverPlayer.getPlayerType());
            characterChangeEvent.characterType = playerType;

            ServerFoundation.instance.getServer().sendToAllTCP(characterChangeEvent);
        }
    }

    @Override
    public void run() {
        long pastTime = System.nanoTime();
        double amountOfTicks = 60;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        while(this.running){

            try {
                Thread.sleep((long) (60F / amountOfTicks));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long now = System.nanoTime();
            delta += (now - pastTime) / ns;
            pastTime = now;

            while(delta > 0){
                tick();
                delta--;
            }
        }
    }
}
