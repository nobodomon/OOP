package com.mygdx.global;

public class PlayerReadyEvent {
    public String username;
    public boolean ready;

    public PlayerReadyEvent(){
        this.ready = false;
    }

    public void toggleReady(){
        if(this.ready == true){
            this.ready = false;
        }else{
            this.ready = true;
        }
    }
}

