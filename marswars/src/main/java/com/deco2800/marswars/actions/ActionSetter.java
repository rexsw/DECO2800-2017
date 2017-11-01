package com.deco2800.marswars.actions;

import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.buildings.Turret;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.EntityID;
import com.deco2800.marswars.entities.terrainelements.Resource;
import com.deco2800.marswars.entities.units.*;
import com.deco2800.marswars.managers.AiManager;
import com.deco2800.marswars.managers.AiManager.Difficulty;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
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
                return doUnload((AttackableEntity) performer);
            case UNLOADINDIVIDUAL:
                return doUnloadIndividual((Soldier) performer);
            case ATTACKMOVE:
            	return doAttackMove(performer, x, y);
            case BUILDGATE:
                return doBuildGate((BuildingEntity)performer);
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
    private static boolean doUnload(AttackableEntity performer) {
        LOGGER.info("Try to unload");
        if (performer instanceof Carrier) {
            Carrier carrier =  (Carrier)performer;
            carrier.unload();
            return true;
        }
        else if (performer instanceof Turret) {
        	 Turret carrier =  (Turret)performer;
        	 carrier.setAction(new UnloadAction(carrier));
        	 return true;
        }
        else {
        	return false;
        }
    }
    
    /**
     * Assigns the unload individual action to the entity
     * @param performer the entity to be assigned the action
     * @return true
     */
    private static boolean doUnloadIndividual(Soldier performer) {
        LOGGER.info("Try to unload last loaded unit");
        Carrier carrier =  (Carrier)performer;
        carrier.unloadIndividual();
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
    private static boolean doBuildGate(BuildingEntity performer) {
        performer.setAction(new BuildGateAction(performer));
        return true;
    }
    
    /**
     * Assigns the buildgate action to the entity
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
     * Assigns the attack move action to the entity
     * @param performer the entity to be assigned the action
     * @param x the x co-ordinates of the action
     * @param y the y co-ordinates of the action
     * @return true
     */
    private static boolean doAttackMove(BaseEntity performer, float x, float y) {
        performer.setAction(new AttackMoveAction((int)x, (int)y, (AttackableEntity) performer));
        return true;
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
            case UNLOADINDIVIDUAL:
            	return "Unload Individual";
            case ATTACKMOVE:
            	return "Attack Move";
            default:
                return "PLEASE SET IN ACTIONS/ACTIONSETTER.JAVA";
        }
    }

    private static double setDifficultyMultiplier(Difficulty difficulty) {
		double difficultyMultiplier;
    	switch(difficulty) {
		case EASY:
			difficultyMultiplier = 2.0;
			break;
		case HARD:
			difficultyMultiplier = 0.8;
			break;
		default: //NORMAL
			difficultyMultiplier = 1.0;
			break;
		}
		return difficultyMultiplier;
	}
    
    /**
     * checks if there are enough resources to pay for the selected entity (and pop limit)
     * @param owner
     * @param c
     * @param resourceManager
     * @return
     */
    public static boolean canAfford(int owner, boolean isAi, EntityID c, ResourceManager resourceManager) {
    	if (GameManager.get().areCostsFree() && !isAi) {
    		return true;
    	}
    	if (resourceManager.getPopulation(owner) > resourceManager.getMaxPopulation(owner)) {
    		return false;
    	}
    	if (!isAi) {
	    	if (resourceManager.getRocks(owner) >= c.getCostRocks()
	    			&& resourceManager.getCrystal(owner) >= c.getCostCrystals()
	    			&& resourceManager.getBiomass(owner) >= c.getCostBiomass()) {
	    		return true;
	    	}
    	} else {
    		AiManager am = (AiManager) GameManager.get().getManager(AiManager.class);
			double difficultyMultiplier = setDifficultyMultiplier(am.getDifficulty());
			if (resourceManager.getRocks(owner) >= (int)(c.getCostRocks()*difficultyMultiplier)
	    			&& resourceManager.getCrystal(owner) >= (int)(c.getCostCrystals()*difficultyMultiplier)
	    			&& resourceManager.getBiomass(owner) >= (int)(c.getCostBiomass()*difficultyMultiplier)) {
	    		return true;
	    	}
		}
    	return false;
    }
    
    /**
     * checks if there are enough resources to pay for the selected entity
     * @param owner
     * @param c
     * @param resourceManager
     * @return
     */
    public static void payForEntity(int owner, boolean isAi, EntityID c, ResourceManager resourceManager) {
    	if (GameManager.get().areCostsFree() && !isAi) {
    		// no payment
    	} else if (!isAi) {
			resourceManager.setRocks(resourceManager.getRocks(owner) - c.getCostRocks(), owner);
			resourceManager.setCrystal(resourceManager.getCrystal(owner) - c.getCostCrystals(), owner);
			resourceManager.setBiomass(resourceManager.getBiomass(owner) - c.getCostBiomass(), owner);
		} else {
			AiManager am = (AiManager) GameManager.get().getManager(AiManager.class);
			double difficultyMultiplier = setDifficultyMultiplier(am.getDifficulty());
			
			resourceManager.setRocks(resourceManager.getRocks(owner) - (int)(c.getCostRocks()*difficultyMultiplier), owner);
			resourceManager.setCrystal(resourceManager.getCrystal(owner) - (int)(c.getCostCrystals()*difficultyMultiplier), owner);
			resourceManager.setBiomass(resourceManager.getBiomass(owner) - (int)(c.getCostBiomass()*difficultyMultiplier), owner);
		}
    }
    
    public static void setGenerate(BaseEntity target, EntityID c) {
        // entity costs
        ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
        if (canAfford(target.getOwner(), target.isAi(), c, resourceManager)) {
        	LOGGER.info("Building entity " + c.name());
			payForEntity(target.getOwner(), target.isAi(), c, resourceManager);
        } else {
        	LOGGER.info("CAN'T AFFORD ENTITY!");
        	LOGGER.info("Biomass: " + resourceManager.getBiomass(target.getOwner()) + " | Cost: " + (int)(c.getCostBiomass()*1.0));
        	return;
        }
        switch (c) {
            case ASTRONAUT:
                target.setAction(new GenerateAction(new Astronaut(target.getPosX(), target.getPosY(), 0, target.getOwner())));
                break;
            case CARRIER:
                target.setAction(new GenerateAction(new Carrier(target.getPosX(), target.getPosY(), 0, target.getOwner())));
                break;
            case COMMANDER:
                target.setAction(new GenerateAction(new Commander(target
                        .getPosX(), target.getPosY(), 0, target.getOwner())));
                break;
            case MEDIC:
                target.setAction(new GenerateAction(new Medic(target.getPosX(), target.getPosY(), 0, target.getOwner())));
                break;
            case SOLDIER:
                target.setAction(new GenerateAction(new Soldier(target.getPosX(), target.getPosY(), 0, target.getOwner())));
                break;
            case TANK:
                target.setAction(new GenerateAction(new Tank(target.getPosX(), target.getPosY(), 0, target.getOwner())));
                break;
            case HACKER:
            	target.setAction(new GenerateAction(new Hacker(target.getPosX(), target.getPosY(), 0, target.getOwner())));
                break;
            case SNIPER:
            	target.setAction(new GenerateAction(new Sniper(target.getPosX(), target.getPosY(), 0, target.getOwner())));
                break;
            case TANKDESTROYER:
            	target.setAction(new GenerateAction(new TankDestroyer(target.getPosX(), target.getPosY(), 0, target.getOwner())));
                break;
            case SPATMAN:
            	target.setAction(new GenerateAction(new Spatman(target.getPosX(), target.getPosY(), 0, target.getOwner())));
                break;
            default:
                break;

        }
        return;
    }
}
