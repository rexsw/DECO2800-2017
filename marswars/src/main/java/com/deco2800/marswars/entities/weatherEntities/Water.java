package com.deco2800.marswars.entities.weatherEntities;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.HasAction;
import com.deco2800.marswars.entities.HasHealth;
import com.deco2800.marswars.entities.Tickable;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;
import com.deco2800.marswars.managers.WeatherManager;
import com.deco2800.marswars.worlds.AbstractWorld;

import java.util.Optional;

/**
 * Use Resource Class instead
 * Created by timhadwen on 29/7/17.
 */
public class Water extends BaseEntity implements HasHealth, Tickable, HasAction {

    private WeatherManager weatherManager = (WeatherManager)
            GameManager.get().getManager(WeatherManager.class);
    private Optional<DecoAction> currentAction = Optional.empty();

    /**
     * Constructor for the Water
     * @param parent
     * @param posX
     * @param posY
     * @param posZ
     */
    public Water(AbstractWorld parent, float posX, float posY, float posZ) {
        super(posX, posY, posZ, 1, 1, 1f);
        this.setTexture("water_draft");
        this.canWalkOver = true;
    }

    /**
     * Gets the current health of an entity
     * @return
     */
    @Override
    public int getHealth() {
        return 0;
    }

    /**
     * Sets the current health of an entity
     * @param health
     */
    @Override
    public void setHealth(int health) {

    }

    /**
     * Set an current action for this Water
     * @param action
     */
    @Override
    public void setAction(DecoAction action) {
        currentAction = Optional.of(action);
    }

    /**
     * Returns the current action of the entity
     * @return current action
     */
    @Override
    public Optional<DecoAction> getCurrentAction() {
        return currentAction;
    }

    /**
     * Causes effects to come into play each game tick.
     * @param tick Current game tick
     */
    @Override
    public void onTick(int tick) {
        weatherManager.setWeatherEvent();
    }
}
