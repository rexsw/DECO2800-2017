package com.deco2800.marswars.entities;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.ImpactAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.managers.GameManager;

/**
 * A missile.
 * @author Tze Thong Khor
 *
 */
public class Bullet extends MissileEntity implements Tickable {
	
	private Optional<DecoAction> currentAction = Optional.empty();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Bullet.class);
	private int damage;
	private int armorDamage;
	private float speed;
	private AttackableEntity target;

    public Bullet(float posX, float posY, float posZ, AttackableEntity target, int damage, int armorDamage) {
        super(posX, posY, posZ, 1, 1, 1, target, damage, armorDamage);
        this.setTexture("bullet");
        this.initActions();
        //this.addNewAction(MoveAction.class);
        this.addNewAction(ImpactAction.class);
        this.setDamage(damage);
        this.setArmorDamage(armorDamage);
        this.setSpeed(0.04f); //Unused
        currentAction = Optional.of(new ImpactAction(this, target));
    }

    @Override
    public void onTick(int tick) {
    	/* If the action is completed, remove it otherwise keep doing that action */
    	try {
			if (!currentAction.get().completed()) {
				currentAction.get().doAction();
			} else {
				LOGGER.info("Action is completed. Deleting");
				currentAction = Optional.empty();
			}
    	} catch (Exception e) {
    		//Bullets are freezing for an unknown reason fix needed
    		GameManager.get().getWorld().removeEntity(this);
    		return;
    	} 
    }
    
    /**
     * Return the movement speed of the bullet.
     * @return speed of the bullet
     */
    public float getSpeed() { return speed; }
    
    /**
     * Set the speed of the bullet.
     */
    public void setSpeed(float speed) { this.speed = speed; }
    
}