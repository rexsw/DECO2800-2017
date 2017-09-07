package com.deco2800.marswars.hud;



public class MiniMapEntity {
    private int team; // 0, player's team, 1 allied, 2 enemy
    public float x; // x coordinate of the entity in pixels: 0 <= x < width
    public float y; // y coordinate of the entity in pixels: window height - height <= y < window height
    public int rendered = 0; // set to 1 once it has been rendered

    /**
     * Constructor for MiniMapEntity
     * @param team  0: friendly, 1: allied, 2: enemy.
     * @param x x position on screen. 0 < x < minimap width
     * @param y y position on screen.
     */
    public MiniMapEntity(int team, float x, float y) {
        this.team = team;
        this.x = x;
        this.y = y;
    }

    /**
     * An int representing which team it is on
     * @return  0: friendly, 1: allied, 2: enmy
     */
    public int getTeam() {
        return team;
    }

    /**
     * Gets the texture to be displayed
     *
     * @return The texture to be rendered onto the screen
     */
    public String getTexture() {
        if (team == 0) {
            return "friendly_unit";
        } else if (team == 1) {

        } else if (team == 2) {

        }
        return "this shouldnt happen ever";
    }
}
