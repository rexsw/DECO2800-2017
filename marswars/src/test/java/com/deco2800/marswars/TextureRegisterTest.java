package com.deco2800.marswars;

import com.badlogic.gdx.graphics.Texture;
import com.deco2800.marswars.managers.TextureManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextureRegisterTest extends BaseTest {
    @Test
    public void ConstructorTest() {
        TextureManager t = new TextureManager();
        t.saveTexture("thing", "resources/placeholderassets/tree.png");

        // This wont catch every issue but should catch most
        // Fall though case is where thing has the same width and height as duck.png
        assertEquals(t.getTexture("thing").getWidth(), new Texture("resources/placeholderassets/tree.png").getWidth());
        assertEquals(t.getTexture("thing").getHeight(), new Texture("resources/placeholderassets/tree.png").getHeight());
        assertEquals(t.getTexture("ajhdhsfjkaldsf").getWidth(), new Texture("resources/placeholderassets/spacman_ded.png").getWidth());
        assertEquals(t.getTexture("ajhdhsfjkaldsf").getHeight(), new Texture("resources/placeholderassets/spacman_ded.png").getHeight());
    }
}