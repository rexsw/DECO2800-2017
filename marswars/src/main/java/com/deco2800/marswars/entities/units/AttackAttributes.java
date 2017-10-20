package com.deco2800.marswars.entities.units;

/**
 * Attack attributes handling interface for entities
 * @author Tze Thong Khor 27/8/17
 *
 */
public interface AttackAttributes {
	/**
	 * Returns the armor of an entity
	 * @return
	 */
	int getArmor();
	
	/**
	 * Sets the armor of an entity
	 * @param armor
	 */
	void setArmor(int armor);
	
	/**
	 * Set the maximum armor of the entity
	 * @param maxArmor the maximum armor of the entity
	 */
	public void setMaxArmor(int maxArmor);
	
	/**
	 * Return the maximum armor of the entity
	 * @return the maximum armor of the entity
	 */
	public int getMaxArmor();
	
	/**
	 * Returns the attack range of an entity.
	 * @return
	 */
	int getAttackRange();
	
	/**
	 * Sets the attack range of an entity.
	 * @param attackRange
	 */
	void setAttackRange(int attackRange);
	
	/**
	 * Set the attack speed of the entity
	 * @param attackSpeed the attack speed of the entity
	 */
	public void setAttackSpeed(int attackSpeed);
	
	/**
	 * Return the attack speed of the entity
	 * @return attackspeed the attack speed of the entity
	 */
	public int getAttackSpeed();
	
	/**
	 * Returns the damage of an entity
	 * @return
	 */
	int getDamageDeal();
	
	/**
	 * Set the damage of an entity
	 * @param damage
	 */
	void setDamage(int damage);
	
	/**
	 * Set the armor damage of an entity
	 * @param armorDamage
	 */
	void setArmorDamage(int armorDamage);
	
	/**
	 * Return the armor damage of an entity
	 * @return the armor damage.
	 */
	int getArmorDamage();
	
	/**
	 * Returns the health of an entity
	 * @return
	 */
	int getHealth();

	/**
	 * Sets the health of an entity
	 * @param health
	 */
	void setHealth(int health);
	
	/**
	 * Set the maximum health of the entity
	 * @param maxHealth the maximum health of the entity
	 */
	public void setMaxHealth(int maxHealth);
	
	/**
	 * Return the maximum health of the entity
	 * @return the maximum health of the entity
	 */
	public int getMaxHealth();
	
	/**
	 * Returns the loyalty of an entity
	 * @return loyalty of an entity
	 */
	public int getLoyalty();
	
	/**
	 * Set the loyalty of an entity
	 */
	public void setLoyalty(int loyalty);
	
	/**
	 * Set the max loyalty of an entity
	 */
	public void setMaxLoyalty(int maxLoyalty);
	
	/**
	 * Returns the loyalty damage of an entity
	 * @return the loyalty damage of an entity
	 */
	public int getLoyaltyDamage();
	
	/**
	 * Set the loyalty damage of an entity
	 * @param loyaltyDamage damage of an entity
	 */
	public void setLoyaltyDamage(int loyaltyDamage);
	
	public void setSpeed(float speed);
	
	public float getSpeed();
	
	/**
	 * Returns whether the entity is loaded onto a vehicle or not
	 * @return 1 if loaded, 0 otherwise
	 */
	public int getLoadStatus();
	
	/**
	 * Sets the entity to be loaded into a vehicle
	 */
	public void setLoaded();
	
	/**
	 * Sets the entity to be unloaded from a vehicle
	 */
	public void setUnloaded();
}
