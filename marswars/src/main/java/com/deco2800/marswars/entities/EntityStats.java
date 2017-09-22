package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;

import java.util.Optional;

/**
 * This class is used to represent the stats an entity might have and to standardize them so the HUD can read it.
 * Created by Hayden Bird on 23/08/2017.
 */

public class EntityStats implements HasAction {



    private String name;
    private int health;
    private int maxHealth;
    private float posX;
    private float posY;
    private float posZ;
    private GatheredResource resourceCarried;
    private Optional<DecoAction> currentAction;
    private Selectable.EntityType type;


    public EntityStats(String name, int health, int maxHealth, GatheredResource resourceCarried,
                       Optional<DecoAction> currentAction, BaseEntity entity) {
        this.name = name;
        this.health = health;
        this.maxHealth = maxHealth;
        this.posX = entity.getPosX();
        this.posY = entity.getPosY();
        this.posZ = entity.getPosZ();
        this.resourceCarried = resourceCarried;
        this.currentAction = currentAction;
        this.type = entity.getEntityType();
    }

    public String getName() {
        return name;
    }
    /**
     * This function returns the health of the unit
     * @return
     */
    public int getHealth() {
        return health;
    }
    
    public int getMaxHealth() {
        return maxHealth;
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

    /**
     * Returns the current action of the entity
     * @return current action
     */
    @Override
    public Optional<DecoAction> getCurrentAction() {
        return currentAction;
    }

    public Selectable.EntityType getType() {
        return type;
    }
}
