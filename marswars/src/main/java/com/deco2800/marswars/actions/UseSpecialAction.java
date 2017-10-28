package com.deco2800.marswars.actions;

import com.deco2800.marswars.buildings.CheckSelect;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.items.Special;
import com.deco2800.marswars.entities.items.effects.Effect;
import com.deco2800.marswars.entities.items.effects.Effect.Target;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Commander;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.util.WorldUtil;

import java.util.List;

/**
 * General class to implement the action of using a Special item. Would consider both cases of when the item effect is
 * area of effect (aoe), i.e. requires the player to select an area for the effect, and non-aoe (i.e. something global 
 * or simply something that has a fixed target).
 * 
 *  state = the stage of the action i.e. whether the player is selecting an area or simply applying the effect(s) of the
 *  		item. Items with non-aoe effects would simply skip to the latter.
 *  pointX = the X coordinate of the point that is the center of the aoe effect (which the player selects)
 *  pointY = the Y coordinate of the point that is the center of the aoe effect (which the player selects)
 *  temp = the texture BaseEntity for the overlay image for the player to visualise and select where the aoe effect will
 *  		be
 *  radius = the aoe radius in terms of tiles
 *  fixPos = value to help fix the centre location spawn point for rounding issues.
 *  user = The Commander that carries the item that is being used.
 *  item = The Special item being used.
 *  completed = Boolean for indicating if the action is complete or not.
 *  actionPaused = boolean indicating whether the action is paused (not needed for instantaneous effects).
 *  timeManager = The game's time manager to allow pausing etc. (may not be needed for instantaneous effects)
 *  
 * @author Michael
 *
 */
public class UseSpecialAction implements DecoAction {
	enum State {
		SELECT, //state where player chooses a space on the map to use the item there
		EXECUTE //activating the effect(s) of the item
	}
	private State state;
	private float pointX;
	private float pointY;
	private CheckSelect temp;
	/*
	 * may need to change definition of this, since I'm using building team's code but they might not have used the same
	 * definition as used for radius here.
	 */
	private float radius;  
	private float fixPos = 0f;
	private Commander user;
	private Special item;
	private boolean completed = false;
	private boolean actionPaused = false;
	private TimeManager timeManager = (TimeManager)	GameManager.get().getManager(TimeManager.class);
	
	/**
	 * Constructor for the action that implements the activation and execution of a Special item from the Inventory bar.
	 * @param item  The Special item that is being used
	 * @param actor  The Commander unit that carries the Special item being used.
	 */
	public UseSpecialAction(Special item, Commander actor) {
		this.item = item;
		this.user = actor;
		this.radius = item.getRadius();
		boolean radiusZero = Math.abs(radius - 0) < 0.01;
		state = radiusZero ? State.EXECUTE : State.SELECT; //0 aoe radius means does not need player to select an area
		if (state == State.SELECT) {
			actor.setItemInUse(true);
		}
	}
	
	/**
	 * Method to display the green overlay for the player to select where the aoe effect is to be used (if applicable).
	 * Also implements the functionality of applying the Special item's effect(s) on entities in the area selected or
	 * globally (dependent on the effect's definition). Does nothing if the game is paused.
	 */
	@Override
	public void doAction() {
		if (timeManager.isPaused() || actionPaused || completed) { //check if paused or completed.
			return;
		}
		if (state == State.SELECT) { //need to get player's input of where to use the item (if needed)
			if (temp != null) {
				GameManager.get().getWorld().removeEntity(temp);
			}
			float[] parse = new float[]{pointX, pointY, fixPos};
			temp = WorldUtil.selectionStage(temp, radius, parse, null);
			/*
			 * updating coordinates and parsed values into selectionStage because java passes by values unless 
			 * it's an object.
			 */
			pointX = parse[0];
			pointY = parse[1];
			fixPos = parse[2];
		} else { //should only get to here for EXECUTE.
			for (Effect e : item.getEffect()) {
				boolean radiusZero = Math.abs(radius - 0) < 0.01;
				if ((e.getTarget() == Target.SELF) && radiusZero) {//affect only the Commander that owns the item.
					e.applyEffect(user);
				} else if (e.getTarget() == Target.SELF_TEAM) { //affect only player's team
					executeEffectOnTargets(e, WorldUtil.getEntitiesOfClassAndOwner(
							GameManager.get().getWorld().getEntities(),	AttackableEntity.class, user.getOwner()));
				} else if (e.getTarget() == Target.ENEMY_TEAM) { //affect only enemies
					executeEffectOnTargets(e, WorldUtil.getEntitiesOfClassAndNotOwner(
							GameManager.get().getWorld().getEntities(),	AttackableEntity.class, user.getOwner()));
				} else if (e.getTarget() == Target.GLOBAL) { //affect everyone
					executeEffectOnTargets(e, WorldUtil.getEntitiesOfLikeClass(GameManager.get().getWorld().getEntities(),
							AttackableEntity.class));
				} else if (e.getTarget() == Target.ENEMY){ //only affect e within selected area (Target.ENEMY)
					List<BaseEntity> targets = WorldUtil.getEntitiesAroundWithClass(AttackableEntity.class, pointX, 
							pointY,	radius, radius);
					executeEffectOnTargets(e, WorldUtil.getEntitiesOfClassAndNotOwner(targets, AttackableEntity.class, 
							user.getOwner()));
					WorldUtil.removeOverlay();
				} else {
					List<BaseEntity> targets = WorldUtil.getEntitiesAroundWithClass(AttackableEntity.class, pointX, 
							pointY,	radius, radius);
					executeEffectOnTargets(e, WorldUtil.getEntitiesOfClassAndOwner(targets, AttackableEntity.class, 
							user.getOwner()));
					WorldUtil.removeOverlay();
				}
			}
    		if(!item.useItem()) {
    			// no use limit left
    			this.user.getInventory().getSpecials().remove(item);
    		}
        	this.user.setStatsChange(true);
			completed = true; //action completed once all effects of the Special item have been considered/applied.
		}
	}

	/**
	 * Helper method to apply an effect to a given list of targets which have already been filtered out (by class i.e.
	 * AttackableEntity, and owner where applicable).
	 * 
	 * @param e  Effect to be applied.
	 * @param targets  List of BaseEntities (which are in fact all instances of AttackableEntities) to apply the effect
	 * 			onto.
	 */
	private void executeEffectOnTargets(Effect e, List<BaseEntity> targets) {
		for (BaseEntity ent : targets) {
			AttackableEntity mark = (AttackableEntity) ent;
			e.applyEffect(mark);
			
		}	
		if(item.getDuration() > 0) {
			new java.util.Timer().schedule( 
			        new java.util.TimerTask() {
			            @Override
			            public void run() {
			            	for (BaseEntity ent : targets) {
			        			AttackableEntity mark = (AttackableEntity) ent;
			        			e.removeEffect(mark);
			        		}	
			            }
			        }, 
			        item.getDuration() * 1000
			);
		}
	}
	
	public void execute() {
		GameManager.get().getWorld().removeEntity(temp);
		state = State.EXECUTE;
	}
	
	public void cancel() {
		GameManager.get().getWorld().removeEntity(temp);
		this.completed = true;
	}
	
	/**
	 * Method to indicate whether the action has been completed or not.
	 * @return boolean indicating if the action is finished (true if so, false otherwise).
	 */
	@Override
	public boolean completed() {
		return completed;
	}

	/**
	 * Have no use for this method at the moment. Could use it for effects that are like DoT or HoT maybe?
	 */
	@Override
	public int actionProgress() {
		return 0;
	}

	/**
	 * Have no use for this method at the moment. Could use it for effects that are like DoT or HoT maybe?
	 */
	@Override
	public void pauseAction() {
		this.actionPaused = true;
	}

	/**
	 * Have no use for this method at the moment. Could use it for effects that are like DoT or HoT maybe?
	 */
	@Override
	public void resumeAction() {
		this.actionPaused = false;
	}
}
