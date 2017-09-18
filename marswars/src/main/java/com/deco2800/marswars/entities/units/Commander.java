package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.actions.*;
import com.deco2800.marswars.entities.EntityStats;
import com.deco2800.marswars.entities.Inventory;
import com.deco2800.marswars.entities.items.*;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A hero for the game
 * Created by timhadwen on 19/7/17.
 * Edited by Zeid Ismail on 8/09
 */
public class Commander extends Soldier {

	private Inventory inventory;
	private static final Logger LOGGER = LoggerFactory.getLogger(Commander.class);

	Optional<DecoAction> currentAction = Optional.empty();

	public Commander(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, owner);
		
		this.name = "Commander";
		setAttributes();
		this.inventory = new Inventory(this);
	}

//	@Override
//	public void onTick(int i) {
//		if (!currentAction.isPresent()) {
//			return;
//		}
//
//		if (!currentAction.get().completed()) {
//			currentAction.get().doAction();
//		}
//	}
//

//	@Override
//	public void onRightClick(float x, float y) {
//		List<BaseEntity> entities;
//		try {
//			entities = ((BaseWorld) GameManager.get().getWorld()).getEntities((int) x, (int) y);
//
//		} catch (IndexOutOfBoundsException e) {
//			// if the right click occurs outside of the game world, nothing will happen
//			LOGGER.info("Right click occurred outside game world.");
//			this.setTexture(defaultTextureName);
//			return;
//		}
//		
//		boolean attack = !entities.isEmpty() && entities.get(0) instanceof AttackableEntity;
//				
//		if (attack) {
//			// we cant assign different owner yet
//			AttackableEntity target = (AttackableEntity) entities.get(0);
//			attack(target);
//			
//		} else {
//			this.setCurrentAction(Optional.of(new MoveAction((int) x, (int) y, this)));
//			LOGGER.error("Assigned action move to" + x + " " + y);
//		}
//		this.setTexture(defaultTextureName);
//		SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
//		sound.playSound(movementSound);
//	}
//
//	@Override
//	public boolean isSelected() {
//		return false;
//	}
//
//	@Override
//	public void deselect() {
//	}

	public boolean addItemToInventory(Item item) {
		return inventory.addToInventory(item);
	}

	public boolean removeItemFromInventory(Item item) {
		return inventory.removeFromInventory(item);
	}

	/** not a to string, returns the inventory object itself
	 *
	 * @return inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

//	@Override
//	public boolean equals(Object other) { // need more compare later
//		if (other instanceof Commander) {
//			return this.toString().equals(((Commander)other).toString());
//		}
//		return false;
//	}
	
	@Override
	public int hashCode() { // need more hash later
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + getOwner();
	    return result;
	}
	
	/**
	 * @return The stats of the entity
	 */
	public EntityStats getStats() {
		return new EntityStats("Commander", this.getHealth(), null, this.getCurrentAction(), this);
	}
	
	@Override
	public String toString(){
		return this.name;
	}
	
}