package com.deco2800.marswars.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;

import java.util.ArrayList;
import java.util.List;

/**This class is used to create a small portrait of a unit to display on the hud.
 * Created by Hayden Bird on 11/10/2017.
 */

public class EntityPortrait extends ImageButton {

    private EntityPortrait parent;
    private List<EntityPortrait> children = new ArrayList<>();
    private BaseEntity unit;
    private Image healthBarSprite;
    private int internalState;
    private TextureManager textureManager;

    /**
     * Creates the portrait using just a unit. This is the general use case
     * @param skin The HUD skin to use
     * @param unit The entity that will be represented with the portrait
     */
    public EntityPortrait(Skin skin, BaseEntity unit, float width, float height) {
        super(skin);
        this.unit = unit;
        initiateButton(width, height);
        createListener();
    }

    /**
     * Creates the portrait. This is the use case for when we want the portrait to be
     * @param skin The HUD skin to use
     * @param unit The entity that will be represented with the portrait
     * @param parent The parent entity of this portrait
     */
    public EntityPortrait(Skin skin, BaseEntity unit, EntityPortrait parent, float width, float height) {
        super(skin);
        this.parent = parent;
        this.unit = unit;
        initiateButton(width, height);
        createListener();
    }

    /**
     * Adds a portrait to the children list so it can be displayed in hierarchical order (e.g be able to show units loaded in a carrier)
     * @param child the portrait to add
     */

    public void addChild(EntityPortrait child) {
        children.add(child);
    }

    /**
     *Removes a child from the Portrait
     * @param child the child to remove
     */
    public void removeChild(EntityPortrait child) {
        children.remove(child);
    }

    /**
     *Returns the child portraits.
     * @return the list of children
     */
    public List<EntityPortrait> getChildrenPortraits() {
        return new ArrayList<>(children);
    }

    /**
     * Sets up the portrait visually
     * @param width the width of the button
     * @param height the height of the button
     */
    private void initiateButton(float width, float height) {
        super.setWidth(width);
        super.setHeight(height);
        this.textureManager = (TextureManager) GameManager.get().getManager(TextureManager.class);
        Texture unitTexture = textureManager.getTexture(unit.getTexture());
        Texture healthBar = textureManager.getTexture("Health" + unit.getHealthBar().getState());
        Image unitSprite = new Image(unitTexture);
        this.healthBarSprite = new Image(healthBar);
        this.add(unitSprite).align(Align.top);
        this.add(healthBarSprite).align(Align.top).bottom();
    }

    /**
     * This method updates the health bar on the portrait.
     */
    public void updateHealth() {
        int currentState = unit.getHealthBar().getState();
        if (currentState != internalState) {
            healthBarSprite.setDrawable(new SpriteDrawable(new Sprite(textureManager.getTexture("Health"+currentState))));
            internalState = currentState;
        }
    }


    /**
     *
     */
    private void createListener() {

    }


}
