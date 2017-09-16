package com.deco2800.marswars.entities.weatherEntities;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.HasHealth;
import com.deco2800.marswars.worlds.AbstractWorld;

/**
 * Use Resource Class instead
 * Created by timhadwen on 29/7/17.
 */
public class Water extends BaseEntity implements HasHealth {
    /**
     * Constructor for the Water
     * @param parent
     * @param posX
     * @param posY
     * @param posZ
     * @param height
     * @param width
     */
    public Water(AbstractWorld parent, float posX, float posY, float posZ,
                 float height, float width) {
        super(posX, posY, posZ, height, width, 1f);
        // WRONG TEXTURE
        this.setTexture("medium_water");
        this.setCost(10);
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
}
