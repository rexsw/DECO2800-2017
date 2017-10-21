package com.deco2800.marswars;

import com.deco2800.marswars.initiategame.SoundTrackPlayer;
import com.deco2800.marswars.managers.TimeManager;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by Treenhan on 10/21/17.
 */

public class SoundTrackPlayerTest {
    @Test
    public void testNormalTracks(){
        SoundTrackPlayer player = new SoundTrackPlayer();
        player.updateNormalSoundTrack();


    }

    @Test
    public void testBattleTracks(){
        SoundTrackPlayer player = new SoundTrackPlayer();
        player.playBattleSoundTrack();
    }

    @Test
    public void testStopTracks(){
        SoundTrackPlayer player = new SoundTrackPlayer();
        player.updateNormalSoundTrack();
        player.stopSoundTrack();
        player.playBattleSoundTrack();
        player.stopSoundTrack();
    }

}
