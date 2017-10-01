package com.deco2800.marswars.entities.items.effects;

import com.deco2800.marswars.entities.items.effects.Effect.Target;
import com.deco2800.marswars.entities.units.AttackableEntity;

/**
 * NOT IMPLEMENTED YET. PLACEHOLDER
 * At the moment, this class doesn't do anything. However, there are stats about vision and loyalty in the game
 * and at the time when item class gets implemented, those stats are still under testing. 
 * This class will be implemented later, with relative effects affected!
 * 
 * target = Target enumerate value indicating the intended target of this effect (see Effect interface for more detail).
 * @author Mason
 *
 */
public class otherEffect implements Effect{
	private int loyalty;
	//private int vision;  need further development
	private Target target;
	
	// don't do anything to these two yet, need clarification
	public otherEffect(int loyalty, int vision, Target target) {
		this.loyalty = loyalty; 
		//this.vision = vision;
		this.target = target;
	}
	
	/**
	 * NOT IMPLEMENTED YET. PLACEHOLDER
	 * Method to activate the effect on the specified attackable entity.
	 * @param entity target of the effect to  be applied on.
	 */
	@Override
	public void applyEffect(AttackableEntity entity) {
		//entity.setLoyalty(entity.getLoyalty()+loyalty);
		return;
	}

	/**
	 * NOT IMPLEMENTED YET. PLACEHOLDER
	 * Method to deactivate the effect on the specified attackable entity. Should only be used on attackable entities 
	 * which have effect already activated on them.
	 * @param entity target with the effect to remove it.
	 */
	@Override
	public void removeEffect(AttackableEntity entity) {
		return;
	}
	
	/**
	 * Returns the intended target of this effect as a Target enumerate value.
	 * @return Target enumerate value corresponding to the intended target of this effect.
	 */
	@Override
	public Target getTarget() {
		return this.target;
	}
		
	/**
	 * NOT IMPLEMENTED YET. PLACEHOLDER
	 * Method to generate a helpful string that describes what the effect does. 
	 * @return string that described what the effect does.
	 */
	@Override
	public String generateDescription() {
		return null;
	}

}
