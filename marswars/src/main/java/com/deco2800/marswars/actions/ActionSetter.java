package com.deco2800.marswars.actions;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.Resource;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * This class is used to handle all of the common actions between entities
 * Created by Hayden on 26/08/2017.
 */

public final class ActionSetter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionSetter.class);
    private ActionSetter() {

    }

    public static boolean setAction(BaseEntity performer, float x, float y, ActionType designatedAction) {
        List<BaseEntity> entities;
        try {
            entities = ((BaseWorld) GameManager.get().getWorld()).getEntities((int) x, (int) y);
        } catch (IndexOutOfBoundsException e) {
            LOGGER.error("Attempted to click off map", e);
            return false;
        }
        //Clear Entities Selection
        for (BaseEntity e : GameManager.get().getWorld().getEntities()) {
                e.deselect();
        }
        //Check entities found
        switch(designatedAction) { //breaks not necessary here because of the returns.
        	case GATHER:
        		return (entities.isEmpty())? false : doGather(performer, entities.get(0));
        	case MOVE:
        		return doMove(performer, x, y);
        	default: //other actions were not implemented yet.
        		return false;
        }
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
            default:
                return "PLEASE SET IN ACTIONS/ACTIONSETTER.JAVA";
        }
    }
}
