package com.deco2800.marswars;

import com.deco2800.marswars.managers.BackgroundManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BackgroundManagerTest {
    private TimeManager timeManager = (TimeManager) GameManager.get()
            .getManager(TimeManager.class);
    private BackgroundManager bgManager = (BackgroundManager) GameManager.get()
            .getManager(BackgroundManager.class);

    @Test
    public void correctBackgrounds() {
        assertTrue(bgManager.getBackground() == "night_Bg");
        timeManager.addTime(21600);
        assertTrue(bgManager.getBackground() == "dawn_Bg");
        timeManager.addTime(21600);
        assertTrue(bgManager.getBackground() == "day_Bg");
        timeManager.addTime(21600);
        assertTrue(bgManager.getBackground() == "dusk_Bg");
    }
}
