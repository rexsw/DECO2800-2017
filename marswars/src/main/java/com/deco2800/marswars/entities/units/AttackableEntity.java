package com.deco2800.marswars.entities.units;


import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.HasOwner;
import com.deco2800.marswars.entities.HasProgress;
import com.deco2800.marswars.managers.AiManagerTest;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.Manager;
import com.deco2800.marswars.util.Box3D;

/**
 * A super class of a combat unit.
 * @author Tze Thong Khor on 25/8/17
 *
 */
public class AttackableEntity extends BaseEntity implements AttackAttributes, HasOwner, HasProgress{
	
	private int maxHealth; // maximum health of the entity
	private int health; // current health of the entity
	private int maxArmor; // maximum armor of the entity
	private int armor; // current armor of the entity
	private int armorDamage; // armorDamage of the entity
	private int attackRange; // attackrange of the entity
	private int damage; // the damage of the entity
	private int loyalty; // the loyalty of the entity
	private int loyaltyDamage; // the loyalty damage of the entity
	private int maxLoyalty; // the max loyalty of the entity
	private Manager owner = null; // the owner of the player
	private Optional<DecoAction> currentAction = Optional.empty(); // current action
	private int attackSpeed; // attack speed of the entity
	private MissileEntity missile; // the type of missile
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AttackableEntity.class);
	
	public AttackableEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength) {
		super(posX, posY, posZ, xLength, yLength, zLength);
		this.modifyCollisionMap(true);
	}
	
	public AttackableEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength, float xRenderLength, float yRenderLength, boolean centered) {
		super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, centered);
	}
	
	@SuppressWarnings("deprecation")
	public AttackableEntity(Box3D position, float xRenderLength, float yRenderLength, boolean centered) {
		super(position, xRenderLength, yRenderLength, centered);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Return the attack range of the entity
	 * @return attack range
	 */
	@Override
	public int getAttackRange() {
		return attackRange;
	}

	/**
	 * Set the attack range of the entity
	 * @param attackRange the attack range of the entity
	 */
	@Override
	public void setAttackRange(int attackRange) {
		this.attackRange = attackRange;		
	}

	/**
	 * Return the current armor of the entity
	 * @return current armor
	 */
	@Override
	public int getArmor() {
		return armor;
	}
	
	/**
	 * Set the armor of the entity
	 * @param armor the armor of the entity
	 */
	@Override
	public void setArmor(int armor) {
		if (armor < 0) {
			this.armor = 0;
			return;
		}
		this.armor = armor;
	}
	
	/**
	 * Set the maximum armor of the entity
	 * @param maxArmor the maximum armor of the entity
	 */
	public void setMaxArmor(int maxArmor) {
		this.maxArmor = maxArmor;
	}
	
	/**
	 * Return the maximum armor of the entity
	 * @return the maximum armor of the entity
	 */
	public int getMaxArmor() {
		return maxArmor;
	}
	
	/**
	 * Return the damage of the entity
	 * @return the damage of the entity
	 */
	@Override
	public int getDamageDeal() {
		return damage;
	}

	/**
	 * Set the damage of the entity
	 * @param the damage of the entity
	 */
	@Override
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	/**
	 * Set the maximum health of the entity
	 * @param maxHealth the maximum health of the entity
	 */
	@Override
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	/**
	 * Return the maximum health of the entity
	 * @return the maximum health of the entity
	 */
	@Override
	public int getMaxHealth() {
		return maxHealth;
	}
	
	/**
	 * Return the current health of the entity
	 * @return current health
	 */
	@Override
	public int getHealth() {
		return this.health;
	}

	/**
	 * Set the health of the entity
	 * @param the health of the entity
	 */
	@Override
	public void setHealth(int health) {
		if (health <= 0) {
			GameManager.get().getWorld().removeEntity(this);
			if(owner instanceof AiManagerTest) {
				((AiManagerTest) owner).isKill();
			}
			LOGGER.info("DEAD");
		}
		this.health  = health;
	}
	
	public Optional<DecoAction> getCurrentAction() {
		return currentAction;
	}
	
	/**
	 * Removes any current action and set action to empty
	 */
	public void setEmptyAction() {
		currentAction = Optional.empty();
	}

	/**
	 * Set the owner of the entity
	 * @param the owner of the entity
	 */
	@Override
	public void setOwner(Manager owner) {
		this.owner = owner;
	}

	/**
	 * Return the owner of the entity
	 * @return the owner of the entity
	 */
	@Override
	public Manager getOwner() {
		return this.owner;
	}

	/**
	 * Check if an entity shares the same owner
	 * @param an entity
	 * @return true if the parameter shares the same owner, false otherwise
	 */
	@Override
	public boolean sameOwner(AbstractEntity entity) {
		boolean isInstance = entity instanceof HasOwner;
		return isInstance && this.owner == ((HasOwner) entity).getOwner();
	}

	/**
	 * Set a new action for an entity
	 * @param an action for the entity to take
	 */
	@Override
	public void setAction(DecoAction action) {
		currentAction = Optional.of(action);
	}

	/**
	 * Set the armor damage of the entity
	 * @param the new armor damage of the entity
	 */
	@Override
	public void setArmorDamage(int armorDamage) {
		this.armorDamage = armorDamage;
	}

	/**
	 * Return the armor damage of the entity
	 * @return the armor damage of the entity
	 */
	@Override
	public int getArmorDamage() {
		return armorDamage;
	}

	/**
	 * Set the attack speed of the entity
	 * @param the new attack speed of the entity
	 */
	@Override
	public void setAttackSpeed(int attackSpeed) {
		this.attackSpeed = attackSpeed;
	}

	/**
	 * Set the attack speed of the entity
	 * @return the attack speed of the entity
	 */
	@Override
	public int getAttackSpeed() {
		return attackSpeed;
	}

	@Override
	public int getLoyalty() {
		return loyalty;
	}

	@Override
	public void setLoyalty(int loyalty) {
		this.loyalty = loyalty;
	}

	@Override
	public int getLoyaltyDamage() {
		return loyaltyDamage;
	}

	@Override
	public void setLoyaltyDamage(int loyaltyDamage) {
		this.loyaltyDamage = loyaltyDamage;
	}

	@Override
	public void setMaxLoyalty(int maxLoyalty) {
		this.maxLoyalty = maxLoyalty;
	}

	@Override
	/**
	 * Get the progress of current action
	 * @return int
	 */
	public int getProgress() {
		if (currentAction.isPresent()) {
			return currentAction.get().actionProgress();
		}
		return 0;
	}

	@Override
	/**
	 * Returns true if there is a current action, false if not
	 * @return boolean
	 */
	public boolean showProgress() {
		return currentAction.isPresent();
	}

}
