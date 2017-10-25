package com.deco2800.marswars.entities.weatherentities;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.HasHealth;
import com.deco2800.marswars.entities.Tickable;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.WeatherManager;

/**
 * A Water object. Used for terrain elements and WeatherManager's flooding
 * effect. Extends BaseEntity.
 *
 * Created by timhadwen on 29/7/17, re-purposed by Isaac Doidge.
 *
 * @author Isaac Doidge
 */
public class Water extends BaseEntity implements HasHealth, Tickable {

    private WeatherManager weatherManager = (WeatherManager)
            GameManager.get().getManager(WeatherManager.class);
    private boolean surrounded = false;
    private int health;

    /**
     * Constructor for the Water Tiles
     * @param posX
     * @param posY
     * @param posZ
     */
    public Water(float posX, float posY, float posZ) {
        super(posX, posY, posZ, 1, 1, 1f);
        this.setTexture("water_final");
        this.setHealth(10);
        this.canWalkOver = true;
    }

    /**
     * Sets the boolean that indicates whether or not this Water entity is
     * surrounded on all sides by other Water entities.
     */
    public void setSurrounded() {
        this.surrounded = true;
    }

    /**
     * Returns the boolean that indicates whether or not this Water entity is
     * surrounded on all sides by other Water entities.
     * @return boolean indicating whether or not this Water is surrounded on all
     *         sides.
     */
    public boolean isSurrounded() {
        return this.surrounded;
    }

    /**
     * Gets the current health of an entity
     * @return current health of this Water entity
     */
    @Override
    public int getHealth() {
        return this.health;
    }

    /**
     * Sets the current health of an entity
     * @param health
     */
    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Causes effects to come into play each game tick.
     * @param tick Current game tick
     */
    @Override
    public void onTick(int tick) {
        //weatherManager.setWeatherEvent();
    }
}
