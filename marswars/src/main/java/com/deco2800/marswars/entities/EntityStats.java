package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;

import java.util.Optional;

/**
 * This class is used to represent the stats an entity might have and to standardize them so the HUD can read it.
 * Created by Hayden Bird on 23/08/2017.
 */

public class EntityStats {

    private int health;
    private float posX;
    private float posY;
    private float posZ;
    private GatheredResource resourceCarried;
    private Optional<DecoAction> currentAction;
    private BaseEntity entity;

    public EntityStats(int health, float posX, float posY, float posZ, GatheredResource resourceCarried, Optional<DecoAction> currentAction, BaseEntity entity) {
        this.health = health;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.resourceCarried = resourceCarried;
        this.currentAction = currentAction;
        this.entity = entity;
    }

    /**
     * This function returns the health of the unit
     * @return
     */
    public int getHealth() {
        return health;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public float getPosZ() {
        return posZ;
    }

    public GatheredResource getResourceCarried() {
        return resourceCarried;
    }

    public Optional<DecoAction> getCurrentAction() {
        return currentAction;
    }
}
