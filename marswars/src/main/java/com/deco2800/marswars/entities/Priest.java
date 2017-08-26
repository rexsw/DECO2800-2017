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

public class Priest extends AttackableEntity implements Clickable, Tickable {

	private static final Logger LOGGER = LoggerFactory.getLogger(Priest.class);
	private int maxHealth; // maximum health of the entity
	private int health; // current health of the entity
	private boolean leftClick = false;
	private MissileEntity missile;

	public Priest(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1, 1, 1);
		this.setTexture("spatman_yellow"); // just for testing
		this.setCost(40);
		this.setEntityType(EntityType.UNIT);
		this.initActions();
		this.addNewAction(ActionType.MOVE);//MoveAction.class);
		// set all the attack attributes
		this.setMaxHealth(200);
		this.setHealth(200);
		this.setDamage(-20);
		this.setArmor(50);
		this.setArmorDamage(0);
		this.setAttackRange(2);
		this.setAttackSpeed(75);
		this.missile = new Bullet(this.getPosX(), this.getPosY(), this.getPosZ(), null,
				this.getDamageDeal(), this.getArmorDamage());
	}

	@Override
	public void onClick(MouseHandler handler) {
		leftClick = true;
		if(this.getOwner() instanceof PlayerManager) {
			handler.registerForRightClickNotification(this);
			SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
			this.setTexture("spatman_red");
			LOGGER.error("Clicked on priest");
			this.makeSelected();
		} else {
			LOGGER.error("Clicked on ai priest");
		}
	}

	@Override
	public void onRightClick(float x, float y) {
		List<BaseEntity> entities;
		try {
			entities = ((BaseWorld) GameManager.get().getWorld()).getEntities((int) x, (int) y);

		} catch (IndexOutOfBoundsException e) {
			// if the right click occurs outside of the game world, nothing will happen
			this.setTexture("spatman_yellow");
			return;
		}
		if (entities.size() > 0 && entities.get(0) instanceof AttackableEntity && leftClick == true) {
			// we cant assign different owner yet
			if (!this.sameOwner(entities.get(0))) {
				AttackableEntity target = (AttackableEntity) entities.get(0);
				this.setAction(new DamageAction(this, target));
				LOGGER.error("Healing friendly");
			} else {
				this.setAction(new MoveAction((int) x, (int) y, this));
				LOGGER.error("Moving to unfriendly position");
			}
			leftClick = false;
		} else {
			this.setAction(new MoveAction((int) x, (int) y, this));
			LOGGER.error("Assigned action move to" + x + " " + y);
		}
		this.setTexture("spatman_yellow");
		SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
		sound.playSound("endturn.wav");
		
	}
	
	@Override
	public void onTick(int tick) {
		if (!this.getCurrentAction().isPresent()) {
			// make stances here.
			if (GameManager.get().getWorld().getEntities((int)this.getPosX(), (int)this.getPosY()).size() > 2) {
				BaseWorld world = GameManager.get().getWorld();

				/* We are stuck on a tile with another entity
				 * therefore randomize a close by position and see if its a good
				 * place to move to
				 */
				Random r = new Random();
				Point p = new Point(this.getPosX() + r.nextInt(2) - 1, this.getPosY() + r.nextInt(2) - 1);

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
				this.setAction(new MoveAction((int)p.getX(), (int)p.getY(), this));
			}
			return;
		}
		
		if (!this.getCurrentAction().get().completed()) {
			this.getCurrentAction().get().doAction();
		} else {
			LOGGER.info("Action is completed. Deleting");
			this.setEmptyAction();
		}
	}
	
	/**
	 * Set the health of the entity
	 * @param the health of the entity
	 */
	@Override
	public void setHealth(int health) {
		if (health < 0) {
			GameManager.get().getWorld().removeEntity(this);
			LOGGER.info("DEAD");
		}
		this.health  = health;
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
	
	public MissileEntity getMissile() {
		return missile;
	}
	
	public void setMissile(MissileEntity missile) {
		this.missile = missile;
	}
	
}