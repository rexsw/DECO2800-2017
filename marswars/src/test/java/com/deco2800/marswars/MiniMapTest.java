package com.deco2800.marswars;

import com.deco2800.marswars.hud.MiniMap;
import com.deco2800.marswars.hud.MiniMapEntity;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;
import org.junit.Before;
import org.junit.Test;


import java.util.List;

import static org.junit.Assert.assertTrue;

public class MiniMapTest extends BaseTest {
    /*
    Simple tests for the minimap
     */
    private MiniMap miniMap;
    private MiniMapEntity miniMapEntity;

    @Before
    public void createMiniMap() {
        GameManager t = GameManager.get();
        TextureManager text = new TextureManager();
        t.addManager(text);
        TextureManager reg = (TextureManager) GameManager.get().getManager(TextureManager.class);
        reg.saveTexture("minimap", "resources/HUDAssets/minimap.png");
        miniMap = new MiniMap("minimap", 220, 220);
        miniMapEntity = new MiniMapEntity(0, 4, 3);
    }

    @Test
    public void dimensions() {
        // Tests that the dimensions given are the dimensions being used
        assertTrue(miniMap.getWidth() == 220);
        assertTrue(miniMap.getHeight() == 220);
    }

    @Test
    public void insertingData() {
        assertTrue(miniMapEntity.x == 4);
        assertTrue(miniMapEntity.y ==3);
        assertTrue(miniMapEntity.getTeam() == 0);
        assertTrue(miniMapEntity.getTexture().equals("friendly_unit"));
    }

    @Test
    public void addingEntities() {
        miniMap.addEntity(0, 4, 4);
        miniMap.addEntity(1, 3, 3);
        List<MiniMapEntity> l = miniMap.getEntitiesOnMap();
        assertTrue(l.get(0).getTeam() == 0);
        assertTrue(l.get(1).getTeam() == 1);
        assertTrue(l.get(0).getTexture().equals("friendly_unit"));
        assertTrue(l.get(0).x == 4);
        assertTrue(l.get(1).y == 3);
    }
}









