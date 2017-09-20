package com.deco2800.marswars.buildings;

/**
 * An interface that dictates whether or not an entity is paused by the flooding
 * effect.
 *
 * @author Isaac Doidge
 */
public interface Floodable {

    /**
     * Sets the boolean describing whether or not the BuildingEntity is
     * currently under the flooding effect.
     * @param state
     */
    void setFlooded(boolean state);

    /**
     * Returns the boolean describing whether or not the BuildingEntity is
     * currently under the flooding effect.
     * @return state
     */
    boolean isFlooded();
}
