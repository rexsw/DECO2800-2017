package com.deco2800.marswars.hud;


import com.deco2800.marswars.entities.units.AttackableEntity;

public class MiniMapEntity {
    private int x; // x coordinate of the entity in pixels: 0 <= x < minimap width
    private int y; // y coordinate of the entity in pixels: 0 <= y < minimap height
    public AttackableEntity entity;


    /**
     * Constructor for MiniMapEntity
     * @param entity the game world entity this object represents
     * @param x x position of the entity in mimimap coordinates
     * @param y y position
     */
    public MiniMapEntity(AttackableEntity entity, int x, int y) {
        this.x = x;
        this.y = y;
        this.entity = entity;
    }

    public boolean samePosition(int x, int y) {
        return x == this.x && y == this.y;
    }

    /**
     * Gets the texture to be displayed
     *
     * @return The texture to be rendered onto the screen
     */
    public String getTexture() {
        //TODO this needs updating when the AI is more finalised
        if (entity.getOwner() < 0) {
            return "friendly_unit";
        } else {
            return "AI_unit";
        }
    }

    /**
     *
     * Only frienly units, and enemys not obscured by the fog of war should be displayed on the minimap
     *
     * @return true if the entity is to be displayed on the minimap, false otherwise
     */
    public boolean toBeDisplayed() {
        return !entity.concealedByFog();
    }
}
