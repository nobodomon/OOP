package com.mygdx.server;
import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.global.JoinRequestEvent;
import com.mygdx.global.PlayerAddEvent;
import com.mygdx.global.PlayerRemoveEvent;
import com.mygdx.global.PlayerUpdateEvent;

import java.io.IOException;


public class ServerFoundation {
    public static ServerFoundation instance;

    private Server server;

    public static void main(String[] args){
        ServerFoundation.instance = new ServerFoundation();
    }

    public ServerFoundation(){
        this.server = new Server(1_000_000, 1_000_000);

        this.server.getKryo().register(JoinRequestEvent.class);
        this.server.getKryo().register(PlayerAddEvent.class);
        this.server.getKryo().register(PlayerRemoveEvent.class);
        this.server.getKryo().register(PlayerUpdateEvent.class);
        this.server.getKryo().register(String.class);
        this.server.getKryo().register(Color.class);

        this.bindServer(6334,6334);
    }

    public void bindServer(final int tcpPort, final int udpPort){
        this.server.start();

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
