package com.deco2800.marswars.initiategame;

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

    public static final Music battleTheme = Gdx.audio.newMusic(Gdx.files.internal("OriginalSoundTracks/SpacWarBattle.mp3"));
    public static final Music dayTheme = Gdx.audio.newMusic(Gdx.files.internal("OriginalSoundTracks/Day_Soundtrack.mp3"));
    public static final Music nightTheme = Gdx.audio.newMusic(Gdx.files.internal("OriginalSoundTracks/Night_Soundtrack.mp3"));

     static boolean fadeDone = true;


    private static TimeManager timeManager =
            (TimeManager) GameManager.get().getManager(TimeManager.class);

    public  void updateNormalSoundTrack(){
        if(timeManager.getHours() >= 6 && timeManager.getHours() < 18){
            if(!dayTheme.isPlaying() && fadeDone && Render3D.getBattleFlag()==0) {
                stopSoundTrack();
                dayTheme.setVolume(0.0f);
                dayTheme.setLooping(true);
                dayTheme.play();
                fadeIn(dayTheme);
            }

        }
        else{
            if(!nightTheme.isPlaying() && fadeDone && Render3D.getBattleFlag()==0){
                stopSoundTrack();
                nightTheme.setVolume(0.0f);
                nightTheme.setLooping(true);
                nightTheme.play();
                fadeIn(nightTheme);
            }
        }
    }

    public  void playBattleSoundTrack(){
        if(!battleTheme.isPlaying() && fadeDone){
            stopSoundTrack();
            battleTheme.setVolume(0.0f);
            battleTheme.play();
            fadeIn(battleTheme);
            Render3D.setBattleFlag(0);
        }


        battleTheme.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                if(Render3D.getBattleFlag()==1){
                    playBattleSoundTrack();
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
        fadeDone = false;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {

                if (music.getVolume() <= 0.9)
                    music.setVolume(music.getVolume() + 0.01f);
                else {
                    fadeDone=true;
                    this.cancel();
                }
            }
        }, 0f, 0.01f);
    }

    public static void fadeOut(Music music){
        fadeDone=false;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {

                if (music.getVolume() >= 0.01)
                    music.setVolume(music.getVolume() - 0.01f);
                else {

                    music.stop();
                    fadeDone=true;
                    this.cancel();
                }
            }
        }, 0f, 0.01f);
    }

}
