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

    public Music battleTheme = Gdx.audio.newMusic(Gdx.files.internal("OriginalSoundTracks/SpacWarBattle.mp3"));
    public Music dayTheme = Gdx.audio.newMusic(Gdx.files.internal("OriginalSoundTracks/Day_Soundtrack.mp3"));

    public Music nightTheme = Gdx.audio.newMusic(Gdx.files.internal("OriginalSoundTracks/Night_Soundtrack.mp3"));


    private static TimeManager timeManager =
            (TimeManager) GameManager.get().getManager(TimeManager.class);

    public  void updateNormalSoundTrack(){
        if(timeManager.getHours() >= 6 && timeManager.getHours() < 17){
            if(!dayTheme.isPlaying()) {
                stopSoundTrack();
                dayTheme.setVolume(0.0f);
                fadeIn(dayTheme);
                dayTheme.setLooping(true);
                dayTheme.play();
            }

        }
        else{
            if(!nightTheme.isPlaying()){
                stopSoundTrack();
                nightTheme.setVolume(0.0f);
                fadeIn(nightTheme);
                nightTheme.setLooping(true);
                nightTheme.play();
            }
        }
    }

    public  void playBattleSoundTrack(){
        if(!battleTheme.isPlaying()){
            stopSoundTrack();
            battleTheme.setVolume(0.0f);
            fadeIn(battleTheme);
            battleTheme.play();
            Render3D.battleFlag = 0;
        }


        battleTheme.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                if(Render3D.battleFlag==0){
                    stopSoundTrack();
                }
            }
        });

    }

    public  void stopSoundTrack(){
        if(nightTheme.isPlaying()){
            fadeOut(nightTheme);
        }
        if(dayTheme.isPlaying()){
            fadeOut(dayTheme);
        }
        if(battleTheme.isPlaying()){
            fadeOut(battleTheme);
        }
    }


    public static void fadeIn(Music music){
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {

                if (music.getVolume() <= 0.9)
                    music.setVolume(music.getVolume() + 0.01f);
                else {
                    this.cancel();
                }
            }
        }, 0f, 0.01f);
    }

    public static void fadeOut(Music music){
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {

                if (music.getVolume() >= 0.01)
                    music.setVolume(music.getVolume() - 0.01f);
                else {
                    music.stop();
                    this.cancel();
                }
            }
        }, 0f, 0.01f);
    }

}
