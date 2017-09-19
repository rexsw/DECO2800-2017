package com.deco2800.marswars.entities.units;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.HasAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.Tickable;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;

/**
 * A missile.
 * @author Tze Thong Khor
 *
 */
public class Bullet extends MissileEntity implements Tickable, HasAction {
	
	private Optional<DecoAction> currentAction = Optional.empty();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Bullet.class);

    public Bullet(float posX, float posY, float posZ, AttackableEntity target, int damage, int armorDamage, String missileTexture,
    			int areaDamage, int owner, AttackableEntity ownerEntity) {
        super(posX, posY, posZ, 1, 1, 1, target, damage, armorDamage, missileTexture, areaDamage, owner, ownerEntity);
        this.setTexture(missileTexture);
        this.setSpeed(0.05f); 
        this.setTarget(target);
        this.setDamage(damage);
        this.setArmorDamage(armorDamage);
        this.setMissileTexture(missileTexture);
        this.setareaDamage(areaDamage);
        this.setOwner(owner);
        this.setOwnerEntity(ownerEntity);
        this.addNewAction(ActionType.MOVE);
        currentAction = Optional.of(new MoveAction((int) target.getPosX(), (int) target.getPosY(), this));
    }

    @Override
    public void onTick(int tick) {
    	//If there is no action delete
    	if (!currentAction.isPresent() || currentAction.get().completed()) {
    		GameManager.get().getWorld().removeEntity(this);
    	}
    	/* If the action is completed, remove it otherwise keep doing that action */
    	try {
    		// check if the target still exists in the world
    		float posX = this.getPosX(); float posY = this.getPosY();
    		boolean find = GameManager.get().getWorld().getEntities().contains(this.getTarget());
			if (find && currentAction.get().completed()) {
				// check for the positions
				if (this.getTarget().getPosX() == posX && this.getTarget().getPosY() == posY) {
					impact();
					GameManager.get().getWorld().removeEntity(this);
					//LOGGER.info("target health " + this.getTarget().getHealth());
				} else {
					GameManager.get().getWorld().removeEntity(this);
				}
			} else if (find == true) { // if target is still existing then continue the action
				currentAction.get().doAction();
			} else { // either the target is not existing anymore or the action is completed
				//LOGGER.info("Action is completed. Deleting");
				currentAction = Optional.empty();
				GameManager.get().getWorld().removeEntity(this);
			}
    	} catch (Exception e) {
    		//Bullets are freezing for an unknown reason fix needed
    		GameManager.get().getWorld().removeEntity(this);
    	} 
    }
    
    /**
     * Impact on the target and damage enemy depends on this areaDamage.
     */
    public void impact() {
    	if (this.getareaDamage() == 0) {
	    	causeDamage(this.getTarget(), this.getDamageDeal(), this.getArmorDamage());
    	} else {
    		ArrayList<AttackableEntity> listOfEntity = new ArrayList<AttackableEntity>();
    		
    		addEnemyEntity(this.getareaDamage(), listOfEntity);
    		for (AttackableEntity entity : listOfEntity) {
    			causeDamage(entity, this.getDamageDeal(), this.getArmorDamage());
    			
    		}
    	}
    }
    
 
    /**
     * Helper function. Reduce the health and armor of the enemy.
     * @param target
     * 			Target entity
     * @param damage
     * 			Normal damage
     * @param armorDamage
     * 			Armor damage
     */
    public void causeDamage(AttackableEntity target, int damage, int armorDamage) {
    	if (target.getArmor() > 0) {
    		
    		target.setHealth(target.getHealth() - damage/2);
    		target.setArmor(target.getArmor() - armorDamage);
    	} else {
    		target.setHealth(target.getHealth() - damage);
    	}
    	LOGGER.info("Enemy health " + target.getHealth());
    }

    /**
     * Loop through the area of the square(length * length) and add an attackableEntity enemy to listOfEntity
     * @param length 
     * 			the length of the square
     * @param listOfEntity
     * 			the list to add enemy entity
     */
    public void addEnemyEntity(int areaDamage, ArrayList<AttackableEntity> listOfEntity) {
    	int length = 1 + 2 * areaDamage;
    	BaseWorld world = GameManager.get().getWorld();
    	int posX = (int) this.getTarget().getPosX();
		int posY = (int) this.getTarget().getPosY();
		// find all the position of the square
		for (int i = 0; i < length * 2; i++) {
			for (int y = 0; y < length; y++) {
				int x =  length - i + length;
				int currentX = x + posX + areaDamage;
				int currentY = y + posY + areaDamage;
				// invalid position
				//LOGGER.info("Checking valid position");
				if (currentX < 0 || currentX > world.getWidth() || currentY > world.getLength()) {
					continue;
				}
				// check for attackablentity enemy and add them to listOfEntity
				List<BaseEntity> entitiesList = GameManager.get().getWorld().getEntities(currentX, currentY);
				if (entitiesList.size() > 0) {
					for (BaseEntity entity: entitiesList) {
						// need to change the condition at some point
						if (entity instanceof AttackableEntity && !this.sameOwner(entity)) {
							listOfEntity.add((AttackableEntity) entity);
						}
					}
				}
			}
		}		
    }
    
	@Override
	public Optional<DecoAction> getCurrentAction() {
		return currentAction;
	}
    
}
