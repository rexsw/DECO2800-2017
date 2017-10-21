package com.deco2800.marswars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.deco2800.marswars.mainmenu.MainMenu;
import com.deco2800.marswars.managers.BackgroundManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BackgroundManagerTest {
    private TimeManager timeManager = (TimeManager) GameManager.get()
            .getManager(TimeManager.class);
    private BackgroundManager bgManager = (BackgroundManager) GameManager.get()
            .getManager(BackgroundManager.class);

    @Before
    public void init(){
        MainMenu menu = new MainMenu(new Skin(Gdx.files.internal("uiskin.json")),new Stage(new ScreenViewport()));
        GameManager.get().setMainMenu(menu);
    }

    @Ignore
    @Test
    public void correctBackgrounds() {
        timeManager.resetInGameTime();
        assertTrue(bgManager.getBackground() == "night_Bg1" || 
        		bgManager.getBackground() == "night_Bg2");
        timeManager.addTime(21600);
        assertTrue(bgManager.getBackground() == "dawn_Bg");
        timeManager.addTime(21600);
        assertTrue(bgManager.getBackground() == "day_Bg");
        timeManager.addTime(21600);
        assertTrue(bgManager.getBackground() == "dusk_Bg");
        timeManager.resetInGameTime();
    }
}
