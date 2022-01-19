package com.mygdx.server;
import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.supers.PlayerState;
import com.mygdx.global.*;
import com.mygdx.server.handlers.CapturePointUpdateHandler;
import com.mygdx.server.handlers.PlayerUpdateHandler;
import com.mygdx.server.listeners.EventListener;
import com.mygdx.server.listeners.JoinListener;
import com.mygdx.server.listeners.LeaveListener;

import java.io.IOException;


public class ServerFoundation {

    public static ServerFoundation instance;

    private Server server;

    public static void main(String[] args){
        ServerFoundation.instance = new ServerFoundation();
        System.out.println("Server Initializing");
    }

    public ServerFoundation(){
        this.server = new Server();

        this.server.getKryo().register(JoinRequestEvent.class);
        this.server.getKryo().register(JoinResponseEvent.class);
        this.server.getKryo().register(PlayerAddEvent.class);
        this.server.getKryo().register(CapturePointCreateEvent.class);
        this.server.getKryo().register(CapturePointDeleteEvent.class);
        this.server.getKryo().register(PlayerRemoveEvent.class);
        this.server.getKryo().register(PlayerUpdateEvent.class);
        this.server.getKryo().register(PlayerTransferEvent.class);
        this.server.getKryo().register(PlayerCapturingEvent.class);
        this.server.getKryo().register(CapturePointUpdateEvent.class);
        this.server.getKryo().register(MoveUpdateEvent.class);
        this.server.getKryo().register(PlayerCharacterChangeEvent.class);
        this.server.getKryo().register(PlayerReadyEvent.class);
        this.server.getKryo().register(PlayerHitEvent.class);
        this.server.getKryo().register(PlayerKilledEvent.class);
        this.server.getKryo().register(String.class);
        this.server.getKryo().register(Color.class);

        this.server.addListener(new JoinListener());
        this.server.addListener(new LeaveListener());
        this.server.addListener(new EventListener());

        PlayerUpdateHandler.INSTANCE.start();
        CapturePointUpdateHandler.INSTANCE.start();

        this.bindServer(6334,6334);
    }

    public void bindServer(final int tcpPort, final int udpPort){
        this.server.start();
        System.out.println("Server started");
        try{
            this.server.bind(tcpPort,udpPort);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public Server getServer() {
        return server;
    }

}
