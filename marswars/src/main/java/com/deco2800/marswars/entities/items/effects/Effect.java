package com.deco2800.marswars.entities.items.effects;

import com.deco2800.marswars.entities.units.AttackableEntity;

/**
 *  Interface to specify common methods Effect classes should have. All Effect classes should implement this interface.
 *  
 * @author Mason
 *
 */
public interface Effect {
	/**
	 * Method to activate the effect on the specified attackable entity.
	 * @param entity target of the effect to  be applied on.
	 */
	void applyEffect(AttackableEntity entity);
	
	/**
	 * Method to deactivate the effect on the specified attackable entity. Should only be used on attackable entities 
	 * which have effect already activated on them.
	 * @param entity target with the effect to remove it.
	 */
	void removeEffect(AttackableEntity entity);
	
	/**
	 * Method to generate a helpful string that describes what the effect does. 
	 * @return string that described what the effect does.
	 */
	String generateDescription();
}
