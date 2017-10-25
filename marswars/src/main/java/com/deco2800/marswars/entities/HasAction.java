package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;

import java.util.Optional;

/**
 * Implemented by entities that have an action. Used in TimeManager to
 * facilitate pausing of sets of entities.
 * @FunctionalInterface
 *
 * @author Isaac Doidge
 */
public interface HasAction {

    /**
     * Returns the current action of the entity
     * @return current action of entity
     */
    Optional<DecoAction> getCurrentAction();
}