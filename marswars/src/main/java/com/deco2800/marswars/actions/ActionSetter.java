package com.deco2800.marswars.actions;

import com.deco2800.marswars.entities.EntityID;
import com.deco2800.marswars.entities.Spacman;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Carrier;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.TerrainElements.Resource;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * This class is used to assign actions to entities.
 * Created by Hayden on 26/08/2017.
 */

public final class ActionSetter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionSetter.class);
    private ActionSetter() {}

    /**
     * This method is used to give an entity an action to perform.
     * @param performer The entity that will have the action assigned to them to perform
     * @param x the x co-ordinates that the user rightclicked
     * @param y the y co-ordinates that the user rightclicked
     * @param designatedAction The action to try an assign to the performer
     * @return The function will return if it could not give the performer the action, and true otherwise
     */
    public static boolean setAction(BaseEntity performer, float x, float y, ActionType designatedAction) {
        //Check that this is a valid action for the performer
        if (!performer.getValidActions().contains(designatedAction)) {
            return false;
        }
        //Get the list of entities at the clicked location
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
        //Only check for actions that require a target if there is a potential target
        if (!entities.isEmpty()) {
            switch (designatedAction) {
                case GATHER:
                    return doGather(performer, entities.get(0));
                case DAMAGE:
                    return doDamage(performer, entities.get(0));
                case LOAD:
                    return doLoad(performer, entities.get(0));
                default:
                    break;
            }
        }
        //Check actions that don't require a target
        switch (designatedAction) {
            case MOVE:
                return doMove(performer, x, y);
            case UNLOAD:
                return doUnload((Soldier) performer);
            default:
                return false;
        }
    }
    
    /**
     * This method is used to initialise build action
     * @param performer The entity that will have the action assigned to them to perform
     * @param building The building type to be made
     * @return The function will return if it could not give the performer the action, and true otherwise
     */
    public static BuildAction setAction(BaseEntity performer, BuildingType building) {
        //Check that this is a valid action for the performer
        if (!performer.getValidActions().contains(ActionType.BUILD)) {
            return null;
        }
        return doBuild(performer, building);
    }
    

    /**
     *Assigns the performer to attempt to load the target
     * @param performer The entity that will load
     * @param target The entity that will be loaded
     * @return True if both entities are instances of Soldier, false otherwise
     */
    private static boolean doLoad(BaseEntity performer, BaseEntity target) {
        if (target instanceof Soldier && performer instanceof Carrier) {
            LOGGER.info("Try to load");
            Carrier carrier =  (Carrier)performer;
            carrier.load((Soldier) target);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Assigns the unload action to the entity
     * @param performer the entity to be assigned the action
     * @return true
     */
    private static boolean doUnload(Soldier performer) {
        LOGGER.info("Try to unload");
        performer.setAction(new UnloadAction(performer));
        return true;
    }
    
   
    /**
     *Assigns the performer to attempt to attack the target
     * @param performer The entity that will attack
     * @param target The entity that will be attacked
     * @return True if both entities are instances of attackableentity, false otherwise
     */
    private static boolean doDamage(BaseEntity performer, BaseEntity target) {
        if (target instanceof AttackableEntity && performer instanceof AttackableEntity) {
            performer.setAction(new AttackAction((AttackableEntity) performer, (AttackableEntity) target));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Assigns the performer to try and gather from the target if it is a resource
     * @param performer the entity that will try to gather
     * @param target the thing that will be attempted to be gathered
     * @return True if target is a resource, false otherwise
     */
    private static boolean doGather(BaseEntity performer, BaseEntity target) {
        if (target instanceof Resource) {
            performer.setAction(new GatherAction(performer, target));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Assigns the move action to the entity
     * @param performer the entity to be assigned the action
     * @param x the x co-ordinates of the action
     * @param y the y co-ordinates of the action
     * @return true
     */
    private static boolean doMove(BaseEntity performer, float x, float y) {
        performer.setAction(new MoveAction((int)x, (int)y, performer));
        return true;
    }
    
    /**
     * Assigns the build action to the entity
     * @param performer the entity to be assigned the action
     * @param building The building type to be made
     * @return true
     */
    private static BuildAction doBuild(BaseEntity performer, BuildingType building) {
        BuildAction processBuild = new BuildAction(performer, building);
        performer.setAction(processBuild);
        return processBuild;
    }

    /**
     * This function is used to get the string for the name of the action denoted by the enumerated type
     * @param a the action
     * @return the string containing the actions name
     */
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
            case BUILD:
                return "Construct";
            case LOAD:
        	return "Load";
            case UNLOAD:
        	return "Unload";
            default:
                return "PLEASE SET IN ACTIONS/ACTIONSETTER.JAVA";
        }
    }

    public static void setBuild(BaseEntity target, EntityID c) {
        switch (c) {
            default:
                target.setAction(new GenerateAction(new Spacman(target.getPosX() - 10, target.getPosY() - 10, 0)));
                break;

        }
        return;
    }

    public static BuildingType getBuildingType(EntityID e) {
        switch (e) {
            case BARRACKS:
                return BuildingType.BARRACKS;
            case BASE:
                return BuildingType.BASE;
            case TURRET:
                return BuildingType.TURRET;
            case BUNKER:
                return BuildingType.BUNKER;
            case HEROFACTORY:
                return BuildingType.HEROFACTORY;
            default:
                return null;
        }
    }
}
