package com.deco2800.marswars.entities.weatherEntities;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.Tickable;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.WeatherManager;
import com.deco2800.marswars.worlds.AbstractWorld;

public class SpecialTileEntity extends BaseEntity implements Tickable {

    private WeatherManager weatherManager = (WeatherManager)
            GameManager.get().getManager(WeatherManager.class);
    private SpecialTileEntityType entityType;

    /**
     * Constructor for the Water
     * @param parent
     * @param posX
     * @param posY
     * @param posZ
     */
    public SpecialTileEntity(AbstractWorld parent, float posX, float posY,
                             float posZ, SpecialTileEntityType type) {
        super(posX, posY, posZ, 1, 1, 1f);
        this.entityType = type;
        this.canWalkOver = true;
        this.setTexture();
    }

    private void setTexture() {
        if (this.entityType.equals(SpecialTileEntityType.WATER)) {
            this.setTexture("water_draft");
        }
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
