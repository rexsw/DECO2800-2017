package com.deco2800.marswars.entities.items.effects;

import com.deco2800.marswars.entities.units.AttackableEntity;

/**
 * At the moment, this class doesn't do anything. However, there are stats about vision and loyalty in the game
 * and at the time when item class gets implemented, those stats are still under testing. 
 * This class will be implemented later, with relative effects affected!
 * @author Mason
 *
 */
public class otherEffect implements Effect{
	private int loyalty;
	//private int vision;  need further development
	
	// don't do anything to these two yet, need clarification
	public otherEffect(int loyalty, int vision) {
		this.loyalty = loyalty; 
		//this.vision = vision;
	}
	
	/**
	 * Method to activate the effect on the specified attackable entity.
	 * @param entity target of the effect to  be applied on.
	 */
	@Override
	public void applyEffect(AttackableEntity entity) {
		entity.setLoyalty(entity.getLoyalty()+loyalty);
		return;
	}

	/**
	 * Method to deactivate the effect on the specified attackable entity. Should only be used on attackable entities 
	 * which have effect already activated on them.
	 * @param entity target with the effect to remove it.
	 */
	@Override
	public void removeEffect(AttackableEntity entity) {
		return;
	}
	
	/**
	 * Method to generate a helpful string that describes what the effect does. 
	 * @return string that described what the effect does.
	 */
	@Override
	public String generateDescription() {
		return null;
	}

}
