package com.deco2800.marswars.managers;

public class BackgroundManager extends Manager {

    private TimeManager timeManager = (TimeManager) GameManager.get().getManager(TimeManager.class);

    public String getBackground() {
        String background;
        
        if (timeManager.getHours() >= 6 && timeManager.getHours() < 9) {
            background = "dawn_Bg";
        } else if (timeManager.getHours() >= 9 && timeManager.getHours() < 17) {
            background = "day_Bg";
        } else if (timeManager.getHours() >= 17 && timeManager.getHours() < 20) {
            background = "dusk_Bg";
        } else {
            background = "night_Bg";
        }
        return background;
    }
}
