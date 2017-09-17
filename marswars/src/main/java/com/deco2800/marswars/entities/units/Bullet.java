package com.deco2800.marswars.entities.units;

import java.util.Optional;

import com.deco2800.marswars.entities.HasAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.Tickable;
import com.deco2800.marswars.managers.GameManager;

/**
 * A missile.
 * @author Tze Thong Khor
 *
 */
public class Bullet extends MissileEntity implements Tickable, HasAction {
	
	private Optional<DecoAction> currentAction = Optional.empty();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Bullet.class);

    public Bullet(float posX, float posY, float posZ, AttackableEntity target, int damage, int armorDamage, String missileTexture,
    			int area) {
        super(posX, posY, posZ, 1, 1, 1, target, damage, armorDamage, missileTexture, area);
        this.setTexture(missileTexture);
        this.setSpeed(0.05f); 
        this.setTarget(target);
        this.setDamage(damage);
        this.setArmorDamage(armorDamage);
        this.setMissileTexture(missileTexture);
        this.setArea(area);
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
			}
    	} catch (Exception e) {
    		//Bullets are freezing for an unknown reason fix needed
    		GameManager.get().getWorld().removeEntity(this);
    	} 
    }
    
    /**
     * Impact on the target and damage enemy depends on this radius.
     */
    public void impact() {
    	if (this.getArea() == 0) {
	    	if (this.getTarget().getArmor() > 0) {
	    		this.getTarget().setHealth(this.getTarget().getHealth() - this.getDamageDeal() / 2);
	    		this.getTarget().setArmor(this.getTarget().getArmor() - this.getArmorDamage());
			} else {
				this.getTarget().setHealth(this.getTarget().getHealth() - this.getDamageDeal());
			}
    	}
    }

	@Override
	public Optional<DecoAction> getCurrentAction() {
		return currentAction;
	}
    
}
