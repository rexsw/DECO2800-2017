package com.deco2800.marswars.entities;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.actions.DamageAction;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.Selectable.EntityType;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.PlayerManager;
import com.deco2800.marswars.managers.SoundManager;
import com.deco2800.marswars.util.Point;
import com.deco2800.marswars.worlds.BaseWorld;

/**
 * A combat unit.
 * @author Tze Thong Khor
 *
 */
public class Soldier extends AttackableEntity implements Tickable, Clickable{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Soldier.class);
	
	private Optional<DecoAction> currentAction = Optional.empty();
	
	protected String selectedTextureName = "soldierSelected";
	protected String defaultTextureName = "soldier";
	protected String movementSound = "endturn.wav";

	public Soldier(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1, 1, 1);
		// Everything is just testing
		this.setTexture(defaultTextureName); // just for testing
		this.setCost(10);
		this.setEntityType(EntityType.UNIT);
		this.initActions();
		this.addNewAction(ActionType.MOVE);
		this.addNewAction(ActionType.DAMAGE);
		// set all the attack attributes
		this.setMaxHealth(500);
		this.setHealth(500);
		this.setDamage(50);
		this.setArmor(250);
		this.setArmorDamage(50);
		this.setAttackRange(8);
		this.setAttackSpeed(30);
	}
	
	private void attack(AttackableEntity target){
		int x = (int) target.getPosX();
		int y = (int) target.getPosY();
		if (	//!this.sameOwner(target)&&//(belongs to another player, currently always true)`
				 this!= target //prevent soldier suicide when owner is not set
				) {
			
			currentAction = Optional.of(new DamageAction(this, target));
			LOGGER.error("Assigned action attack target at " + x + " " + y);
		} 
		else 
		{
			currentAction = Optional.of(new MoveAction((int) x, (int) y, this));
			LOGGER.error("Same owner");
		}
	}

	@Override
	public void onClick(MouseHandler handler) {
		if(this.getOwner() instanceof PlayerManager) {
			handler.registerForRightClickNotification(this);
			SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
			this.setTexture(selectedTextureName);
			LOGGER.error("Clicked on soldier");
			this.makeSelected();
		} else {
			LOGGER.error("Clicked on ai soldier");
		}
	}

	@Override
	public void onRightClick(float x, float y) {
		List<BaseEntity> entities;
		try {
			entities = ((BaseWorld) GameManager.get().getWorld()).getEntities((int) x, (int) y);

		} catch (IndexOutOfBoundsException e) {
			// if the right click occurs outside of the game world, nothing will happen
			LOGGER.info("Right click occurred outside game world.");
			this.setTexture(defaultTextureName);
			return;
		}
		if (!entities.isEmpty() && entities.get(0) instanceof AttackableEntity) {
			// we cant assign different owner yet
			AttackableEntity target = (AttackableEntity) entities.get(0);
			attack(target);
			
		} else {
			currentAction = Optional.of(new MoveAction((int) x, (int) y, this));
			LOGGER.error("Assigned action move to" + x + " " + y);
		}
		this.setTexture(defaultTextureName);
		SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
		sound.playSound(movementSound);
	}

	@Override
	public void onTick(int tick) {
		if (!currentAction.isPresent()) {
			// make stances here.
			int xPosition =(int)this.getPosX();
			int yPosition = (int) this.getPosY();
			boolean moveAway = GameManager.get().getWorld().getEntities(xPosition, yPosition).size() > 2;
			if (moveAway) {
				List<BaseEntity> entities = GameManager.get().getWorld().getEntities(xPosition, yPosition);
				
				BaseWorld world = GameManager.get().getWorld();

				/* We are stuck on a tile with another entity
				 * therefore randomize a close by position and see if its a good
				 * place to move to
				 */
				Random r = new Random();
				Point p = new Point(xPosition + r.nextInt(2) - 1, yPosition + r.nextInt(2) - 1);

				/* Ensure new position is on the map */
				if (p.getX() < 0 || p.getY() < 0 || p.getX() > world.getWidth() || p.getY() > world.getLength()) {
					return;
				}
				/* Check that the new position is free */
				if (world.getEntities((int)p.getX(), (int)p.getY()).size() > 1) {
					// No good
					return;
				}

				LOGGER.info("Spacman is on a tile with another entity, move out of the way");

				/* Finally move to that position using a move action */
				currentAction = Optional.of(new MoveAction((int)p.getX(), (int)p.getY(), this));
			}
			return;
		}
		
		if (!currentAction.get().completed()) {
			currentAction.get().doAction();
		} else {
			LOGGER.info("Action is completed. Deleting");
			currentAction = Optional.empty();
		}
		
	}
}