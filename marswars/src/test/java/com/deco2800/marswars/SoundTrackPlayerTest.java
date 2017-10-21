package com.deco2800.marswars;

import com.deco2800.marswars.InitiateGame.SoundTrackPlayer;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by Treenhan on 10/21/17.
 */
@Ignore
public class SoundTrackPlayerTest {
    @Test
    public void testNormalTracks(){
        TimeManager timeManager =
                (TimeManager) GameManager.get().getManager(TimeManager.class);
        SoundTrackPlayer player = new SoundTrackPlayer();

        player.updateNormalSoundTrack();

    }

    @Test
    public void testBattleTracks(){
        TimeManager timeManager =
                (TimeManager) GameManager.get().getManager(TimeManager.class);
        SoundTrackPlayer player = new SoundTrackPlayer();

        player.playBattleSoundTrack();
    }

    @Test
    public void testStopTracks(){
        TimeManager timeManager =
                (TimeManager) GameManager.get().getManager(TimeManager.class);
        SoundTrackPlayer player = new SoundTrackPlayer();
        player.updateNormalSoundTrack();
        player.stopSoundTrack();
        player.playBattleSoundTrack();
        player.stopSoundTrack();
    }

}
