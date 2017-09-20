package com.deco2800.marswars.actions;

import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.entities.EntityID;
import com.deco2800.marswars.net.Action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This class is so that there can be a list of actions that can
 * contain both Action types and Entity IDs but prevent any other type
 * from being added, unlike using a normal ArrayList<Object>
 * Created by Hayden on 15/09/2017.
 */

public class ActionList extends ArrayList<Object> {

    private ArrayList<BuildingType> buildingsAvailable = new ArrayList<BuildingType>(Arrays.asList(
            BuildingType.BASE, BuildingType.BUNKER, BuildingType.TURRET, BuildingType.BARRACKS));

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
            super.add(o);
            ArrayList<Object> sorted = this.getSorted();
            super.clear();
            for (Object o2 : sorted) super.add(o2);
        }
        return false;
    }

    private ArrayList<Object> getSorted() { //TODO: implement a batter sorting algorithm
        ArrayList<Object> sorted = this.stream().filter(o -> o instanceof ActionType).collect(Collectors.toCollection(ArrayList::new));
        sorted.addAll(this.getEntities().stream().filter(o -> ActionSetter.getBuildingType(o) == null).collect(Collectors.toList()));
        sorted.addAll(this.getEntities().stream().filter(o -> ActionSetter.getBuildingType(o) != null).collect(Collectors.toList()));
        return sorted;
    }

    public ArrayList<ActionType> getActions() {
        ArrayList<ActionType> a = new ArrayList<>();
        for (Object o : this) {
            if (o instanceof ActionType) a.add((ActionType) o);
        }
        return a;
    }

    public ArrayList<EntityID> getEntities() {
        ArrayList<EntityID> a = new ArrayList<>();
        for (Object o : this) {
            if (o instanceof EntityID) a.add((EntityID) o);
        }
        return a;
    }

    public ArrayList<BuildingType> getBuildings() {
        ArrayList<BuildingType> a = new ArrayList<>();
        for (Object o : this) {
            if (!(o instanceof EntityID)) continue;
            EntityID e = (EntityID) o;
            if (buildingsAvailable.contains(ActionSetter.getBuildingType(e))) {
                BuildingType b = ActionSetter.getBuildingType(e);
                if (b!= null) a.add(b);
            }
        }
        return a;
    }

    public ArrayList<EntityID> getUnits() {
        ArrayList<EntityID> a = new ArrayList<>();
        for (Object o : this) {
            if (!(o instanceof EntityID)) continue;
            EntityID e = (EntityID) o;
            if (!buildingsAvailable.contains(ActionSetter.getBuildingType(e))) {
                a.add((EntityID) o);
            }
        }
        return a;
    }
}
