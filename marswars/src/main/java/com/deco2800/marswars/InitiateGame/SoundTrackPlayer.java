package com.deco2800.marswars.InitiateGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.deco2800.marswars.mainMenu.MainMenu;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.renderers.Render3D;

/**
 * Created by Treenhan on 10/17/17.
 */
public class SoundTrackPlayer {
    public  boolean isBattlePlaying = false;
    public  Music defaultTheme = null;

    private static TimeManager timeManager =
            (TimeManager) GameManager.get().getManager(TimeManager.class);

    public  void updateNormalSoundTrack(){
        isBattlePlaying = false;
        stopSoundTrack();

        if(timeManager.getHours() >= 6 && timeManager.getHours() < 17){
            defaultTheme =  Gdx.audio.newMusic(Gdx.files.internal("OriginalSoundTracks/Day_Soundtrack.mp3"));
        }
        else{
            defaultTheme =  Gdx.audio.newMusic(Gdx.files.internal("OriginalSoundTracks/Night_Soundtrack.mp3"));
        }

        defaultTheme.setVolume(0.9f);
        defaultTheme.setLooping(true);
        defaultTheme.play();
    }

    public  void playBattleSoundTrack(){
        stopSoundTrack();
        defaultTheme =  Gdx.audio.newMusic(Gdx.files.internal("OriginalSoundTracks/SpacWarBattle.mp3"));
        defaultTheme.setVolume(0.9f);
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


}
