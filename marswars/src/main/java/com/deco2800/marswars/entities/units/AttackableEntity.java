package com.deco2800.marswars.entities.units;


import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.HasAction;
import com.deco2800.marswars.entities.HasOwner;
import com.deco2800.marswars.entities.HasProgress;
import com.deco2800.marswars.managers.GameBlackBoard;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.util.Box3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * A super class of a combat unit.
 * @author Tze Thong Khor on 25/8/17
 *
 */

public class AttackableEntity extends BaseEntity implements AttackAttributes, HasOwner, HasProgress, HasAction{
	private int maxHealth; // maximum health of the entity
	protected int health; // current health of the entity
	private int maxArmor; // maximum armor of the entity
	private int armor; // current armor of the entity
	private int armorDamage; // armorDamage of the entity
	private int attackRange; // attackRange of the entity
	private int damage; // the damage of the entity
	private int loyalty = 100; // the loyalty of the entity
	private int loyaltyDamage; // the loyalty damage of the entity
	private int maxLoyalty = 100; // the max loyalty of the entity
	private float speed; // the movement speed of the entity
	private float maxSpeed = 0.05f; // the maximum speed of the entity
	private int attackSpeed; // attack speed of the entity
	private int maxAttackSpeed = 20; // maximum attack speed of the entity
	private int loadStatus; //whether the target is loaded
	private int areaDamage = 0; // the area of damage 
	private boolean gotHit; // if the unit is hit, it will be true
	private int maxGotHitInterval = 1000; // the maximum value of gotHitInterval
	private int gotHitInterval = maxGotHitInterval; // the interval determine if the entity get hit
	private int loyaltyRegenInterval = 10000;
	private int enemyHackerOwner; // the owner of the last enemy who deal loyalty damage to it
	private boolean ownerChanged = false;
	private AttackableEntity enemy; // the last enemy who hit/damage the entity
	private int stance = 0; // the behavior of the unit responding to enemies
	private int fogRange = 3; //fog range of the entities
	protected String defaultMissileName;
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(AttackableEntity.class);
	
	public AttackableEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength) {
		super(posX, posY, posZ, xLength, yLength, zLength);
		this.setAreaDamage(0);
	}
	
	public AttackableEntity(Box3D position, float xRenderLength, float yRenderLength, boolean centered) {
		super(position, xRenderLength, yRenderLength, centered);
	      this.setAreaDamage(0);
	}

    /**
     * Return the fog range
     */
	public int getFogRange(){
        return fogRange;
    }

    /**
     * Set the fog range
     * @param range
     */
    public void setFogRange(int range){
        this.fogRange = range;
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
		} else if (armor > getMaxArmor()) {
			this.armor = getMaxArmor();
		} else {
			this.armor = armor;
		}
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
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	/**
	 * Return the maximum health of the entity
	 * @return the maximum health of the entity
	 */
	public int getMaxHealth() {
		return this.maxHealth;
	}
	
	/**
	 * Return the current health of the entity
	 * @return current health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Set the health of the entity. When the health is dropped, the entity gotHit status is set to true
	 * @param the health of the entity
	 */
	public void setHealth(int health) {
		if (this.health > health) {
			this.setGotHit(true);
		}
		if (health <= 0) {
			if (this instanceof Carrier) {
				((Carrier)this).unloadPassenger();
			}
			GameBlackBoard black = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
			black.updateDead(this);

			GameManager.get().getWorld().removeEntity(this);
			if (this.getHealthBar() != null) {
				GameManager.get().getWorld().removeEntity(this.getHealthBar());
			}

			LOGGER.info("DEAD");

		}
		if (health >= this.getMaxHealth()) {
			this.health = this.getMaxHealth();
			return;
		}
		if(this instanceof Soldier && ((Soldier) this).getLoadStatus() == 1) {
			return;
		} else {
			this.health = health;
		}
	}

	/**
	 * Get the current action of the entity
	 * @return the current action
	 */
	@Override
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
	 * Set a new action for an entity
	 * @param an action for the entity to take
	 */
	@Override
	public void setAction(DecoAction action) {
		if (currentAction.isPresent()) {
			if (currentAction.get() instanceof MoveAction) {
				if (((MoveAction)currentAction.get()).isCentered()){
					((MoveAction)currentAction.get()).doAction();
					return;
				}
			}
		}
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

	public void setMaxAttackSpeed(int maxAttackSpeed) {
		this.maxAttackSpeed = maxAttackSpeed;
	}
	
	public int getMaxAttackSpeed() {
		return this.maxAttackSpeed;
	}
	
	/**
	 * Set the attack speed of the entity
	 * @param the new attack speed of the entity
	 */
	@Override
	public void setAttackSpeed(int attackSpeed) {
		if (attackSpeed <= 1) {
			this.attackSpeed = 1;
		} else if (attackSpeed >= getMaxAttackSpeed()) {
			this.attackSpeed = this.getMaxAttackSpeed();
		} else {
			this.attackSpeed = attackSpeed;
		}
	}

	/**
	 * Set the attack speed of the entity
	 * @return the attack speed of the entity
	 */
	@Override
	public int getAttackSpeed() {
		return attackSpeed;
	}

	/**
	 * Get the loyalty attribute of the entity
	 * @return the loyalty value of the entity
	 */
	@Override
	public int getLoyalty() {
		return loyalty;
	}

	/**
	 * Set the loyalty attribute of the entity
	 * @param loyalty
	 * 		The loyalty attribute value
	 */
	@Override
	public void setLoyalty(int loyalty) {
		if (loyalty <= 0) {
			if(this instanceof Carrier) {
			    ((Carrier)this).unloadPassenger();
			}
			this.loyalty = this.getMaxLoyalty();
			this.setOwner(this.getEnemyHackerOwner());
			this.ownerChanged = true;
		} else if (loyalty > getMaxLoyalty()) {
			this.loyalty = getMaxLoyalty();
		} else {
			if (((Soldier)this).getLoadStatus()!=1) {
				this.loyalty = loyalty;
			}
		}
	}

	/**
	 * Get the loyalty damage attribute of the entity
	 * @return the loyalty damage of the entity
	 */
	@Override
	public int getLoyaltyDamage() {
		return loyaltyDamage;
	}

	/**
	 * Set the loyalty damage of the entity
	 */
	@Override
	public void setLoyaltyDamage(int loyaltyDamage) {
		this.loyaltyDamage = loyaltyDamage;
	}

	@Override
	public void setMaxLoyalty(int maxLoyalty) {
		this.maxLoyalty = maxLoyalty;
	}
	
	/**
	 * Get the maximum loyalty value of the unit
	 * @return the maximum loyalty of the entity
	 */
	public int getMaxLoyalty() {
		return maxLoyalty;
	}
	
	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	public float getMaxSpeed() {
		return this.maxSpeed;
	}
	
	/**
	 * Set the movement speed of the entity
	 * @param the new speed of the unit
	 */
	public void setSpeed(float speed) {
		if (speed < 0.01f) {
			this.speed = 0.01f;
		} else if (speed > this.getMaxSpeed()) {
			speed = this.getMaxSpeed();
		} else {
			this.speed = speed;
		}
	}
	
	/**
	 * Get the movement speed of the entity
	 * @return the movement speed
	 */
	public float getSpeed() {
		return speed;
	}
	
	public int getLoadStatus() {
	    	return loadStatus;
	}
	
	public void setLoaded() {
	    loadStatus = 1;
	}
	
	public void setUnloaded() {
	    loadStatus = 0;
	}
	
	public void isCarrier() {
	    loadStatus = 2;
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
	
	/**
	 * Get the area damage of the entity.
	 * @return
	 */
	public int getAreaDamage() {
		return areaDamage;
	}
	
	/**
	 * Set the area damage of the entity
	 * @param areaDamage
	 */
	public void setAreaDamage(int areaDamage) {
		this.areaDamage = areaDamage;
	}
	
	public void setNextAction(ActionType a) {
		this.nextAction = a;
	}

	/**
	 * Set the gotHitInterval of the entity. 
	 * @param interval
	 */
	public void setGotHitInterval(int interval) {
		this.gotHitInterval = interval;
	}
	
	/**
	 * Get the value of gotHitInterval.
	 * @return
	 */
	public int getGotHitInterval() {
		return this.gotHitInterval;
	}
	
	/**
	 * Get the initial value of the gotHitInterval
	 * @return
	 */
	public int getMaxgotHitInterval() {
		return this.maxGotHitInterval;
	}
	
	/**
	 * Set the gotHit status. If the entity got hit, the status should be set true and the 
	 * gotHitInterval will be set to the max. Else set false.
	 * @param status
	 */
	public void setGotHit(boolean status) {
		this.gotHit = status;
		if (status) {
			this.setGotHitInterval(this.getMaxgotHitInterval());
		}
	}
	
	/**
	 * Return the status of got hit by any enemy.
	 * @return
	 */
	public boolean gotHit() {
		return gotHit;
	}
	
	/**
	 * Set the enemy of the entity. Should set the enemy who hit the entity.
	 * @param enemy
	 */
	public void setEnemy(AttackableEntity enemy) {
		this.enemy = enemy;
	}
	
	/**
	 * Get the enemy who hit the entity.
	 * @return
	 * 	
	 */
	public AttackableEntity getEnemy() {
		return enemy;
	}
	
	/**
	 * If the entity gotHit status is true, we need a way to set it to be false after it is not getting hit from anyone. The gotHitInterval is used to 
	 * determine the cool down period of the gotHit status from true to false. After the period, the gotHit status will be set false and the goHitInterval
	 * will be set to its initial value.
	 */
	public void gotHitIntervalCoolDown() {
		if (this.gotHit()) {
			this.setGotHitInterval(getGotHitInterval() - 10);
			if (this.getGotHitInterval() <= 0) {
				this.setGotHit(false);
				this.setGotHitInterval(this.getMaxgotHitInterval());
			}
		}
	}
	
	public int getRegenInterval() {
		return this.loyaltyRegenInterval;
	}
	
	public void setRegenInterval(int interval) {
		this.loyaltyRegenInterval = interval;
	}
	
	public void resetRegenInterval() {
		this.loyaltyRegenInterval = 10000;
	}
	
	/**
	 * This method returns a value denoting the stance of the unit.
	 * 0 = Passive - Default unit behavior no reaction to enemies.
	 * 1 = Defensive - Unit will attack enemies within their range but not move.
	 * 2 = Aggressive - Unit will attack enemies within range and follow if they move away.
	 * 3 = Skirmishing - Unit will run to the edge of their range and attack.
	 * 4 = Timid - Unit will move away if attacked.
	 * @return the entity stance
	 */
	public int getStance() {
		return stance;
	}
	
	/**
	 * Changes the stance of the unit.
	 * The new stance is chosen according to the list.
	 * 0 = Passive - Default unit behavior no reaction to enemies. Possible building behavior.
	 * 1 = Defensive - Unit will attack enemies within their range but not move. Possible building behavior.
	 * 2 = Aggressive - Unit will attack enemies within range and follow if they move away.
	 * 3 = Skirmishing - Unit will run to the edge of their range and attack.
	 * 4 = Timid - Unit will move away if attacked.
	 * @param the integer corresponding with the stance
	 */
	public void setStance(int stance) {
		this.stance = stance;
	}
	
	public int getEnemyHackerOwner() {
		return this.enemyHackerOwner;
	}
	
	public void setEnemyHackerOwner(int owner) {
		this.enemyHackerOwner = owner;
	}
	
	public boolean getOwnerChangedStatus() {
		return this.ownerChanged;
	}
	
	public void setOwnerChangedStatus(boolean status) {
		this.ownerChanged = status;
	}
	
	public String getMissileTexture() {
		return defaultMissileName;
	}
	
}
