package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.DecoAction;

import java.util.Optional;

public interface HasAction {

    /**
     * Returns the current action of the entity
     * @return current action of entity
     */
    Optional<DecoAction> getCurrentAction();
}
