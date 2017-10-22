package com.deco2800.marswars.actions;

import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.EntityID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is so that there can be a list of actions that can
 * contain both Action types and Entity IDs but prevent any other type
 * from being added, unlike using a normal ArrayList<Object>
 * Created by Hayden on 15/09/2017.
 */

public class ActionList extends ArrayList<Object> {
	BaseEntity actor;

    public ActionList(BaseEntity actor) {
        super();
        this.actor = actor;
    }

    /**
     * @param o the object to add to the list
     * @return true if the object is an ActionType or EntityID and it was
     * successfully added to the list, false otherwise
     */
    @Override
    public boolean add(Object o) {
        if (o instanceof ActionType || o instanceof EntityID || o instanceof BuildingType) {
            super.add(o);
            Collections.sort(this, (Comparator<Object>) (lhs, rhs) -> {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                if (lhs instanceof ActionType && !(rhs instanceof ActionType)) return -1;
                else if (lhs instanceof ActionType && (rhs instanceof ActionType)) return 0;
                else if (!(lhs instanceof ActionType) && (rhs instanceof ActionType)) return 1;
                else if ((lhs instanceof EntityID) && !(rhs instanceof EntityID)) return -1;
                else if ((lhs instanceof EntityID) && (rhs instanceof EntityID)) return 0;
                else if ((lhs instanceof BuildingType) && (rhs instanceof EntityID)) return 1;
                else if ((lhs instanceof BuildingType) && (rhs instanceof BuildingType)) return 0;
                else return 0;
            });
            return true;
        }
        return false;
    }


    public List<ActionType> getActions() {
        return this.stream().filter(o -> o instanceof ActionType).map(o -> (ActionType) o).collect(Collectors.toCollection(ArrayList::new));
    }

    public List<BuildingType> getBuildings() {
        return this.stream().filter(o -> o instanceof BuildingType).map(o -> (BuildingType) o).collect(Collectors.toCollection(ArrayList::new));
    }

    public List<EntityID> getUnits() {
        return this.stream().filter(o -> o instanceof EntityID).map(o -> (EntityID) o).collect(Collectors.toCollection(ArrayList::new));
    }
    
    public List<Object> getallActions() {
    	return this;
    }
    
    /**
     * Gets the Base Entity that is the actor of these actions.
     * @return BaseEntity that owns these actions.
     */
    public BaseEntity getActor() {
    	return this.actor;
    }
}
