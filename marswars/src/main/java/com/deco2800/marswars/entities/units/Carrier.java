package com.deco2800.marswars.entities.units;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.LoadAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.managers.AbstractPlayerManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.SoundManager;
import com.deco2800.marswars.worlds.BaseWorld;

public class Carrier extends Soldier {

    private static final Logger LOGGER = LoggerFactory.getLogger(Carrier.class);

    private Optional<DecoAction> currentAction = Optional.empty();

    private AttackableEntity[] loadedUnits;

    public Carrier(float posX, float posY, float posZ,
	    AbstractPlayerManager owner) {
	super(posX, posY, posZ, owner);

	// set all the attack attributes
	this.setMaxHealth(1000);
	this.setHealth(1000);
	this.setDamage(0);
	this.setArmor(500);
	this.setArmorDamage(0);
	this.setAttackRange(0);
	this.setAttackSpeed(0);
	loadedUnits = (AttackableEntity[]) new Object[10];
    }

    @Override
    public void onRightClick(float x, float y) {
	List<BaseEntity> entities;
	try {
	    entities = ((BaseWorld) GameManager.get().getWorld())
		    .getEntities((int) x, (int) y);

	} catch (IndexOutOfBoundsException e) {
	    // if the right click occurs outside of the game world, nothing will
	    // happen
	    LOGGER.info("Right click occurred outside game world.");
	    this.setTexture(defaultTextureName);
	    return;
	}
	if (!entities.isEmpty()
		&& entities.get(0) instanceof AttackableEntity) {
	    AttackableEntity target = (AttackableEntity) entities.get(0);
	    load(target);

	} else {
	    currentAction = Optional.of(new MoveAction((int) x, (int) y, this));
	    for (int i = 10; i == 0; i--) {
		    if(!(loadedUnits[i] == null)) {
			loadedUnits[i].setPosX(x);
			loadedUnits[i].setPosY(y);
		    }
	    }
	    LOGGER.error("Assigned action move to" + x + " " + y);
	}
	this.setTexture(defaultTextureName);
	SoundManager sound = (SoundManager) GameManager.get()
		.getManager(SoundManager.class);
	sound.playSound(movementSound);
    }

    public void load(AttackableEntity target) {
	int x = (int) target.getPosX();
	int y = (int) target.getPosY();
	if (this.sameOwner(target) && this != target) { // prevent carrier
							// loading itself
	    currentAction = Optional.of(new LoadAction(this, target));
	    LOGGER.error("Assigned action attack target at " + x + " " + y);
	} else {
	    currentAction = Optional.of(new MoveAction((int) x, (int) y, this));
	    LOGGER.error("Same owner");
	}
    }
    
    public boolean loadPassengers(AttackableEntity target) {
	//Make passengers unrendered
	for (int i = 0; i< 10; i++) {
	    if(loadedUnits[i] == null) {
		loadedUnits[i] = target;
		return true;
	    }
	}
	return false;
    }
    
    /**
     * 
     * 
     * @return passenger thats loaded last
     */
    public AttackableEntity unloadPassenger() {
	AttackableEntity output;
	for (int i = 10; i == 0; i--) {
	    if(!(loadedUnits[i] == null)) {
		output = loadedUnits[i];
		loadedUnits[i] = null;
		return output;
	    }
	}
	return null;
    }
}
