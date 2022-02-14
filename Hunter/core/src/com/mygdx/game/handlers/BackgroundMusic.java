package com.mygdx.game.handlers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class BackgroundMusic {
    String fileName;
    Music music;
    boolean played = false;

    public final Music gameplayMusic = Gdx.audio.newMusic(Gdx.files.internal("bgm_game.ogg"));
    public final Music lobbyMusic = Gdx.audio.newMusic(Gdx.files.internal("bgm_lobby.ogg"));
    public final Music endMusic = Gdx.audio.newMusic(Gdx.files.internal("end.mp3"));

    HashMap<String, Music> musicMap = new HashMap<String, Music>();
    {
        musicMap.put("gameplayMusic", gameplayMusic);
        musicMap.put("lobbyMusic", lobbyMusic);
        musicMap.put("endMusic", endMusic);
    }

    public BackgroundMusic(String name){
        this.fileName = name;
        this.music = musicMap.get(name);
    }

    public void playMusic(){
        this.music.setLooping(true);
        this.music.play();
    }

    public void playOnce(){
        if (played == false){
            this.music.setLooping(false);
            this.music.play();
            this.played = true;
        }
    }

    public void resetPlayOnce(){
        if (played == true){
            this.played = false;
        }
    }

    public void stopMusic(){
        this.music.dispose();
    }
}
