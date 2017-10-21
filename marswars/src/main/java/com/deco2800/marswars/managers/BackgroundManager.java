package com.deco2800.marswars.managers;

import com.deco2800.marswars.mainmenu.MainMenu;

/**
 * BackgroundManager: A class for managing the progression of backgrounds
 * for the game world. Changes the background according to the current In-Game
 * Time.
 */
public class BackgroundManager extends Manager {



    private TimeManager timeManager =
            (TimeManager) GameManager.get().getManager(TimeManager.class);

    /**
     * Chooses the appropriate background for the game according to the current
     * In-Game Time.
     *
     * @return String specifying which texture to load from Texture Manager
     * in MarsWars
     */
    public String getBackground() {
        if(MainMenu.isGameStarted() && !MainMenu.player.battleTheme.isPlaying()) {
            MainMenu.player.updateNormalSoundTrack();
        }

        String background;
        if (timeManager.getHours() >= 6 && timeManager.getHours() < 9) {
            background = "dawn_Bg";
        } else if (timeManager.getHours() >= 9 && timeManager.getHours() < 17) {
            background = "day_Bg";
        } else if (timeManager.getHours() >= 17 &&
                timeManager.getHours() < 20) {
            background = "dusk_Bg";
        } else {
            if (timeManager.getPlaySeconds() % 2 == 0){
            	background = "night_Bg1";
            } else {
            	background = "night_Bg2";
            }
        }
        return background;
    }
}
