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
	 * Enumerate values to indicate the intended target of the effect.
	 */
	enum Target {
		SELF, //the Commander themselves or simply entities on your team in the area selected (for aoe purposes).
		SELF_TEAM, //player's whole entire team globally
		ENEMY, //other players' (if not allied)/AI's entities in area selected
		ENEMY_TEAM, //all other players' (if not allied)/AI's entities globally.
		GLOBAL //every players' and AI's entities gets targetted globally.
	}
	
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
	 * Method to return the Target enumerate to represent the intended target of the effect.
	 * @return
	 */
	Target getTarget();
	
	/**
	 * Method to generate a helpful string that describes what the effect does. 
	 * @return string that described what the effect does.
	 */
	String generateDescription();
}
