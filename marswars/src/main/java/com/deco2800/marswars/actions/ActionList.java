package com.deco2800.marswars.actions;

import com.deco2800.marswars.entities.EntityID;

import java.util.ArrayList;

/**
 * This class is so that there can be a list of actions that can
 * contain both Action types and Entity IDs but prevent any other type
 * from being added, unlike using a normal ArrayList<Object>
 * Created by Hayden on 15/09/2017.
 */

public class ActionList extends ArrayList<Object> {

    public ActionList() {
        super();
    }

    /**
     * @param o the object to add to the list
     * @return true if the object is an ActionType or EntityID and it was
     * successfully added to the list, false otherwise
     */
    @Override
    public boolean add(Object o) {
        if (o instanceof ActionType || o instanceof EntityID) {
            return super.add(o);
        }
        return false;
    }


}
