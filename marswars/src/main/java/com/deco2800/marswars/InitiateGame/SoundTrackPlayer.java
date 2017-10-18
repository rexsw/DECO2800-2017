package com.deco2800.marswars.InitiateGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Timer;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.renderers.Render3D;

/**
 * Created by Treenhan on 10/17/17.
 */
public class SoundTrackPlayer {
    public  boolean isBattlePlaying = false;

    Music battleTheme = Gdx.audio.newMusic(Gdx.files.internal("OriginalSoundTracks/SpacWarBattle.mp3"));

    public boolean change

    public  Music defaultTheme = null;

    public boolean transitionCompleted=false;

    boolean first = true;
    boolean firsthaha=true;

    private static TimeManager timeManager =
            (TimeManager) GameManager.get().getManager(TimeManager.class);

    public  void updateNormalSoundTrack(){
        isBattlePlaying = false;

        if(first) {
            defaultTheme = Gdx.audio.newMusic(Gdx.files.internal("OriginalSoundTracks/Night_Soundtrack.mp3"));
            defaultTheme.setVolume(0.9f);
            defaultTheme.setLooping(true);
            defaultTheme.play();
first=false;
        }

        if(timeManager.getHours() >= 1 && timeManager.getHours() < 2){
            if(firsthaha) {
                trackTransition("OriginalSoundTracks/Day_Soundtrack.mp3");
firsthaha=false;
            }
            //defaultTheme =  Gdx.audio.newMusic(Gdx.files.internal("OriginalSoundTracks/Day_Soundtrack.mp3"));
        }
        else{
            //defaultTheme =  Gdx.audio.newMusic(Gdx.files.internal("OriginalSoundTracks/Night_Soundtrack.mp3"));
        }

//        defaultTheme.setVolume(0.0f);
//        defaultTheme.setLooping(true);
//        defaultTheme.play();
    }

    public  void playBattleSoundTrack(){
        defaultTheme =  Gdx.audio.newMusic(Gdx.files.internal("OriginalSoundTracks/SpacWarBattle.mp3"));
        defaultTheme.setVolume(0.0f);
        defaultTheme.play();
        isBattlePlaying = true;
        Render3D.battleFlag = 0;

        defaultTheme.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                updateNormalSoundTrack();
            }
        });

    }

    public  void stopSoundTrack(){
        if( defaultTheme != null)
            defaultTheme.dispose();
    }


    public void trackTransition(String next){
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (defaultTheme.getVolume() >= 0.01)
                    defaultTheme.setVolume(defaultTheme.getVolume() - 0.01f);
                else {
                    stopSoundTrack();
                    defaultTheme = Gdx.audio.newMusic(Gdx.files.internal(next));
                    defaultTheme.setVolume(0.0f);
                    defaultTheme.play();
                    fadeIn(defaultTheme);
                    this.cancel();
                }
            }
        }, 0f, 0.01f);

    }

    public void fadeIn(Music music){
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {

                if (music.getVolume() <= 0.9)
                    music.setVolume(music.getVolume() + 0.01f);
                else {
                    transitionCompleted = true;
                    this.cancel();
                }
            }
        }, 0f, 0.01f);
    }

}
