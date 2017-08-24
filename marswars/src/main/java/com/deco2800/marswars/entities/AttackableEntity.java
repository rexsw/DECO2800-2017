package com.deco2800.marswars.entities;

import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.util.Box3D;
import com.deco2800.marswars.worlds.BaseWorld;

/**
 * @author Tze Thong Khor on 25/8/17
 *
 */
public class AttackableEntity extends BaseEntity implements HasHealth, HasDamage, HasArmor,
	HasAttackRange{
	
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

	private int maxHealth; // maximum health of the entity
	private int health; // current health of the entity
	private int maxArmor; // maximum armor of the entity
	private int armor; // current armor of the entity
	private int attackRange; // attackrange of the entity
	private int damage; // the damage of the entity


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
		this.armor = armor;
	}
	
	/**
	 * Set the maximum armor of the entity
	 * @param maxarmor the maximum armor of the entity
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
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	/**
	 * Return the maximum health of the entity
	 * @return the maximum health of the entity
	 */
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
		this.health  = health;
	}

	/**
	 * Updates the collision map
	 * @param add
	 */
	private void modifyCollisionMap(boolean add) {
		if (GameManager.get().getWorld() instanceof BaseWorld) {
			BaseWorld baseWorld = (BaseWorld) GameManager.get().getWorld();
			int left = (int) getPosX();
			int right = (int) Math.ceil(getPosX() + getXLength());
			int bottom = (int) getPosY();
			int top = (int) Math.ceil(getPosY() + getYLength());
			for (int x = left; x < right; x++) {
				for (int y = bottom; y < top; y++) {
					if (add)
						baseWorld.getCollisionMap().get(x, y).add(this);
					else
						baseWorld.getCollisionMap().get(x, y).remove(this);
				}
			}
		}
	}
	
}
