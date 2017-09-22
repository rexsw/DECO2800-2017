package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.actions.*;
import com.deco2800.marswars.entities.EntityStats;
import com.deco2800.marswars.entities.Inventory;
import com.deco2800.marswars.entities.items.*;
import com.deco2800.marswars.managers.GameManager;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A hero for the game previously called as HeroSpacman.
 * Class for the hero character that the player chooses at the start of the game. This unit will be different to other 
 * in that it will have skills and can equip items.
 * 
 * inventory = instance of the Inventory class to store items that the Commander has equipped.
 * currentAction = the Commander's action that it is currently taking.
 * 
 * Created by timhadwen on 19/7/17.
 * Edited by Zeid Ismail on 8/09
 */
public class Commander extends Soldier {

	private Inventory inventory;
	private static final Logger LOGGER = LoggerFactory.getLogger(Commander.class);
	//master merging testing
	Optional<DecoAction> currentAction = Optional.empty();

	/**
	 * Constructor for the Commander in the specified location and with the specified owner (i.e. who controls the 
	 * Commander or which team it belongs to).
	 * @param posX  the Commander's X position in the world when spawned
	 * @param posY the Commander's Y position in the world when spawned
	 * @param posZ  the Commander's Z position in the world when spawned
	 * @param owner  the team id indicating which team the Commander belongs to a.k.a who controls this Commander.
	 */
	public Commander(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, owner);
		
		this.name = "Commander";
		setAttributes();
		this.inventory = new Inventory(this);
		//GameManager.get().getGui().
	}

//	/**
//	 * Method to check, execute and update the Commander's actions for the Commander to actually do and appear to do 
//	 * the assigned current action.
//	 * @param i  The current game tick.
//	 */
//	@Override
//	public void onTick(int i) {
//		if (!currentAction.isPresent()) { //no need to update or do anything if there already is no assigned action
//			return;
//		}
//		//Do the assigned action if it's not completed already.
//		if (!currentAction.get().completed()) {
//			currentAction.get().doAction();
//		}
//	}


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
		return new EntityStats("Commander", this.getHealth(), this.getMaxHealth(), null, this.getCurrentAction(), this);
	}
	
	@Override
	public String toString(){
		return this.name;
	}
	
	@Override
	public void setLoyalty(int loyalty) {
		return;
	}
	
}