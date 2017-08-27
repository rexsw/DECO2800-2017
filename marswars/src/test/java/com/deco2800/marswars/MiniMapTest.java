package com.deco2800.marswars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.deco2800.marswars.hud.MiniMap;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.graphics.Texture;



import static org.junit.Assert.assertTrue;

public class MiniMapTest extends BaseTest {

    private MiniMap miniMap;

    @Before
    public void createMiniMap() {
        GameManager t = GameManager.get();
        TextureManager text = new TextureManager();
        t.addManager(text);
        TextureManager reg = (TextureManager) GameManager.get().getManager(TextureManager.class);
        reg.saveTexture("minimap", "resources/HUDAssets/minimap.png");
        miniMap = new MiniMap("minimap", 220, 220);
    }

    @Test
    public void dimensions() {
        assertTrue(miniMap.getWidth() == 220);
        assertTrue(miniMap.getHeight() == 220);
    }
}









