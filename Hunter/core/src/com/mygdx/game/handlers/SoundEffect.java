package com.mygdx.game.handlers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundEffect {
    String soundName;
    Sound soundFile;
    long soundID;

    public final Sound attackSound = Gdx.audio.newSound(Gdx.files.internal("attack.mp3"));
    public final Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurt.mp3"));
    public final Sound endSound = Gdx.audio.newSound(Gdx.files.internal("end.mp3"));
    public final Sound skillDash = Gdx.audio.newSound(Gdx.files.internal("skill_dash.mp3"));
    public final Sound skillStun = Gdx.audio.newSound(Gdx.files.internal("skill_stun.mp3"));
    public final Sound skillBlink = Gdx.audio.newSound(Gdx.files.internal("skill_blink.mp3"));
    public final Sound skillSpeed = Gdx.audio.newSound(Gdx.files.internal("skill_speedboost.mp3"));
    public final Sound skillAttack = Gdx.audio.newSound(Gdx.files.internal("skill_attack.mp3"));
    public final Sound fixSound = Gdx.audio.newSound(Gdx.files.internal("fix.mp3"));
    public final Sound fixDoneSound = Gdx.audio.newSound(Gdx.files.internal("fix_done.mp3"));

    HashMap<String, Sound> soundMap = new HashMap<String, Sound>();
    {
        soundMap.put("attackSound", attackSound);
        soundMap.put("hurtSound", hurtSound);
        soundMap.put("skillDash", skillDash);
        soundMap.put("skillStun", skillStun);
        soundMap.put("skillBlink", skillBlink);
        soundMap.put("skillSpeed", skillSpeed);
        soundMap.put("skillAttack", skillAttack);
        soundMap.put("endSound", endSound);
        soundMap.put("fixSound", fixSound);
        soundMap.put("fixDoneSound", fixDoneSound);
    }

    public SoundEffect(String name){
        this.soundName = name;
        this.soundFile = soundMap.get(name);
        this.soundID = 0;
    }

    public void soundPlay(){
        this.soundID = soundFile.play();
    }

    public void soundDispose(){
        this.soundFile.dispose();
        this.soundID = 0;
    }

    public void soundPause(){
        this.soundFile.pause();
    }

    public void soundResume(){
        this.soundFile.resume(this.soundID);
    }

    public void soundPlayLoop(){
        // this.soundID = this.soundFile.play();
        this.soundID = this.soundFile.loop();
    }

    public long getSoundID(){
        return this.soundID;
    }
}
