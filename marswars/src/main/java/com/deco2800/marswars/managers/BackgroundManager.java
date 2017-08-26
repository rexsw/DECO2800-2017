package com.deco2800.marswars.managers;

import com.badlogic.gdx.graphics.Texture;
import com.deco2800.marswars.hud.HUDView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackgroundManager extends Manager {

    private TimeManager timeManager = (TimeManager) GameManager.get().getManager(TimeManager.class);
    private TextureManager textureManager = (TextureManager) GameManager.get().getManager(TextureManager.class);


    public Texture getBackground() {
        Texture currentTexture;
        if (timeManager.getHours() >= 6 && timeManager.getHours() < 9) {
            currentTexture = textureManager.getTexture("dawn_Bg");
        } else if (timeManager.getHours() >= 9 && timeManager.getHours() < 17) {
            currentTexture = textureManager.getTexture("day_Bg");
        } else if (timeManager.getHours() >= 17 && timeManager.getHours() < 20) {
            currentTexture = textureManager.getTexture("dusk_Bg");
        } else {
            currentTexture = textureManager.getTexture("night_Bg");
        }
        return currentTexture;
    }
}
