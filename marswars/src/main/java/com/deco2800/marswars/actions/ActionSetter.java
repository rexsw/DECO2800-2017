package com.deco2800.marswars.actions;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.Resource;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;

import java.util.List;

/**
 * This class is used to handle all of the common actions between entities
 * Created by Hayden on 26/08/2017.
 */

public final class ActionSetter {

    private GameManager manager;

    private ActionSetter() {

    }

    public static boolean setAction(BaseEntity performer, float x, float y, ActionType designatedAction) {
        List<BaseEntity> entities;
        try {
            entities = ((BaseWorld) GameManager.get().getWorld()).getEntities((int) x, (int) y);
        } catch (IndexOutOfBoundsException e) {
            // if the right click occurs outside of the game world, nothing will happen
            return false;
        }
        //Clear Entities Selection
        for (BaseEntity e : GameManager.get().getWorld().getEntities()) {
                e.deselect();
        }
        //Check entities found
        if (entities.size() > 0) {
            switch (designatedAction) {
                case GATHER:
                    return doGather(performer, entities.get(0));
                case DAMAGE:
                    return false;
                case GENERATE:
                    return false;
            }
        }
        if (designatedAction == ActionType.MOVE) {
            return doMove(performer, x, y);
        }
        return false;
    }

    private static boolean doGather(BaseEntity performer, BaseEntity target) {
        if (target instanceof Resource) {
            performer.setAction(new GatherAction(performer, target));
            return true;
        } else {
            return false;
        }
    }

    private static boolean doMove(BaseEntity performer, float x, float y) {
        performer.setAction(new MoveAction(x, y, performer));
        return true;
    }


    public static String getActionName(ActionType a) {
        switch (a) {
            case MOVE:
                return "Move";
            case GATHER:
                return  "Gather";
            case DAMAGE:
                return "Attack";
            case GENERATE:
                return "Create";
        }
        return "";
    }
}
