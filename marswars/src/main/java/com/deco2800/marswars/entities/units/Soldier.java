package com.deco2800.marswars.entities.units;

import com.badlogic.gdx.audio.Sound;
import com.deco2800.marswars.actions.*;
import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.buildings.Turret;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.entities.weatherentities.Water;
import com.deco2800.marswars.managers.*;
import com.deco2800.marswars.util.Point;
import com.deco2800.marswars.worlds.BaseWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * A combat unit.
 * @author Tze Thong Khor
 *
 */
public class Soldier extends AttackableEntity implements Tickable, Clickable, HasAction {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Soldier.class); 

	
	protected String selectedTextureName;
	protected String defaultTextureName;
	protected String upleftTextureName;
	protected String uprightTextureName;
	protected String downleftTextureName;
	protected String downrightTextureName;
	protected String movementSound;
	protected String name;
	private ActionType nextAction;

	/**
	 * This function is overiden to allow modification of Fog of war
	 * Sets the position X
	 * @param x
	 */
	@Override
	public void setPosX(float x) {
		//remove the old line of sight
		if(this.getOwner()==-1)
			modifyFogOfWarMap(false,getFogRange());

		super.setPosX(x);

		//set the new line of sight
		if(this.getOwner()==-1)
			modifyFogOfWarMap(true,getFogRange());

	}

	/**
	 * This function is overiden to allow modification of Fog of war
	 * Sets the position Y
	 * @param y
	 */
	@Override
	public void setPosY(float y) {
		//remove the old line of sight
		if(this.getOwner()==-1)
			modifyFogOfWarMap(false,getFogRange());


		super.setPosY(y);

		//set the new line of sight
		if(this.getOwner()==-1)
			modifyFogOfWarMap(true,getFogRange());



	}

	public Soldier(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, 1, 1, 1);
		super.setOwner(owner);
		this.name = "Soldier";
		this.modifyCollisionMap(false);
		//Accessing the technology manager which contains unit Attributes

		
		// Everything is just testing
		this.setAllTextture();
		this.setTexture(defaultTextureName); // just for testing
		this.addNewAction(ActionType.MOVE);
		this.addNewAction(ActionType.DAMAGE);
		this.setEntityType(EntityType.UNIT);
		setAttributes();
		setStance(2); // Default stance for soldier is aggressive		
	}

	//sets all attack attributes
	public void setAttributes(){
		TechnologyManager t = (TechnologyManager) GameManager.get().getManager(TechnologyManager.class);
		this.setMaxHealth(t.getUnitAttribute(this.name, 1));
		this.setHealth(t.getUnitAttribute(this.name, 1));
		this.setDamage(t.getUnitAttribute(this.name, 2));
		this.setMaxArmor(t.getUnitAttribute(this.name, 3));
		this.setArmor(t.getUnitAttribute(this.name, 3));
		this.setArmorDamage(t.getUnitAttribute(this.name, 4));
		this.setAttackRange(t.getUnitAttribute(this.name, 5));
		this.setMaxAttackSpeed(t.getUnitAttribute(this.name, 6));
		this.setAttackSpeed(t.getUnitAttribute(this.name, 6));
		this.setFogRange(t.getUnitAttribute(this.name, 5));
		/*
		 * was changed to make units moveable in game. need to test other values to make this work well in conjunction
		 * with the nano second threshold in setThread method in MarsWars.java
		 */
		this.setMaxSpeed(0.05f);
		this.setSpeed(0.05f); 
		this.setUnloaded(); //default load status = 0
	}
	
	public void attack(AttackableEntity target){
		int x = (int) target.getPosX();
		int y = (int) target.getPosY();
		if (setTargetType(target)) {
			this.setAction(new AttackAction(this, target));
		} 
		else {
			this.setAction(new MoveAction((int) x, (int) y, this));
		}
	}
	
	public void attackDefensively(AttackableEntity target){
		if (setTargetType(target)) {
			currentAction = Optional.of(new ShootAction(this, target));
		} 
	}
	
	/**
	 * Helper function for attack. Override if the entity has different types of target.
	 * Set the target type to be enemy or friend or etc.. and check if the target is valid
	 */
	public boolean setTargetType(AttackableEntity target) {
		if (!this.sameOwner(target) //(belongs to another player, currently always true)
				&& this!= target) { //prevent soldier suicide when owner is not set
			return true;
		}
		return false;
	}

	/**
	 * this is used to reset the texture to deselect entities
	 */
	public void resetTexture(){
		this.setTexture(defaultTextureName);
	}

	@Override
	public void onClick(MouseHandler handler) {
		//check if this belongs to a* player (need to change for multiplayer):
		if(!this.isAi() && this.getLoadStatus() != 1) {
			handler.registerForRightClickNotification(this);
			this.setTexture(selectedTextureName);
			LOGGER.info("Clicked on soldier");
			   this.makeSelected();
		} else {
			LOGGER.info("Clicked on ai soldier");
			this.makeSelected();
		}
	}

	/**
	 * Changes the texture to reflect the direction that the soldier is moving in
	 */
	public void faceTowards(float x, float y) {
		if(this.getPosX()>=x && this.getPosY()>=y) {
			if(this.getPosX()-x >= this.getPosY()-y) {
			this.setTexture(downleftTextureName);}
			else if(this.getPosX()-x < this.getPosY()-y)
			{this.setTexture(upleftTextureName);}
		}
		else if(this.getPosX()>=x && this.getPosY()<y) {
			if(y-this.getPosY() >= this.getPosX()-x) {
			this.setTexture(downrightTextureName);}
			else if(y-this.getPosY() < this.getPosX()-x) {
			this.setTexture(downleftTextureName);}			
		}
		else if(this.getPosX()<x && this.getPosY()>=y) {
			if(x-this.getPosX() >= this.getPosY()-y) {				
			this.setTexture(uprightTextureName);}
			else if (x-this.getPosX() < this.getPosY()-y) {
		    this.setTexture(upleftTextureName);}
		}
		else if(this.getPosX()<x && this.getPosY()<y) {
			if(x-this.getPosX() >= y-this.getPosY()) {
			this.setTexture(uprightTextureName);}
			else if(x-this.getPosX() < y-this.getPosY()) {
				this.setTexture(downrightTextureName);}
		}
		else {
			this.setTexture(defaultTextureName);
		}
	}
	
	@Override
	public void onRightClick(float x, float y) {
		List<BaseEntity> entities;
		try {
			entities = ((BaseWorld) GameManager.get().getWorld()).getEntities((int) x, (int) y);

		} catch (IndexOutOfBoundsException e) {
			// if the right click occurs outside of the game world, nothing will happen
			LOGGER.info("Right click occurred outside game world.",e);
			this.setTexture(defaultTextureName);
			return;
		}
		if (nextAction != null) {
			ActionSetter.setAction(this, x, y, nextAction);
			nextAction = null;
		}else {
			moveOrAttack(entities, x, y);
			this.setTexture(defaultTextureName);
		}
		SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
		Sound loadedSound = sound.loadSound(movementSound);
		sound.playSound(loadedSound);
	}
	
	/**
	 * Helper to be inherited and changed for different right click options (mainly for commander). here simply assigns
	 * the action when right click occurs.
	 * @param entities  List of entities found
	 * @param x coordinate along x axis of the mouse right click input
	 * @param y coordinate along y axis of the mouse right click input
	 */
	protected void moveOrAttack(List<BaseEntity> entities, float x, float y) {
		if (!entities.isEmpty()) {
			// we cant assign different owner yet
			AttackableEntity target = null;
			for (BaseEntity e : entities) {
				if (e instanceof AttackableEntity && !e.sameOwner(this)) {
					 target = (AttackableEntity)e;
					 attack(target);
					 return;
				}
			}

		}
		this.setAction(new MoveAction((int) x, (int) y, this));//, this.getSpeed()));
	}
	
	public void setCurrentAction(Optional<DecoAction> currentAction) {
		this.currentAction = currentAction;
	}

	@Override
	public void onTick(int tick) {
		//update fog of war for owner's entity on every tick
		if (this.getOwner() == -1)  {
			modifyFogOfWarMap(true,getFogRange());
		}

		regeneration();
		checkOwnerChange();
		if (!currentAction.isPresent()) {
			
			//this will disable collision check for the entities inside the carrier
			boolean isTheEntityLoaded=false;


			if(getHealth()<=0)
				modifyFogOfWarMap(false,getFogRange());
			// make stances here.
			int xPosition = (int) this.getPosX();
			int yPosition = (int) this.getPosY();
			List<BaseEntity> entities = GameManager.get().getWorld().getEntities(xPosition, yPosition);
			int entitiesSize = entities.size();
			boolean waterPresent = false;
			for (BaseEntity e: entities) {
				if (e instanceof Soldier && ((Soldier)e).getLoadStatus() == 1) {
					isTheEntityLoaded = true;
				}
				if (e instanceof MissileEntity) {
					entitiesSize--;
				}
				if (e instanceof Water) {
					waterPresent = true;
				}
				if (e instanceof BuildingEntity) {
					entitiesSize++;
				}
			}
			boolean moveAway = (entitiesSize > 1 && ! waterPresent) ||
					(entitiesSize > 2 && waterPresent);
			if (moveAway && !isTheEntityLoaded) {
					BaseWorld world = GameManager.get().getWorld();
				/* We are stuck on a tile with another entity
				 * therefore randomize a close by position and see if its a good
				 * place to move to
				 */
				Random r = new Random();
				Point p = new Point(xPosition + r.nextInt(2) - 1, yPosition + r.nextInt(2) - 1);
				/* Ensure new position is on the map */
				if (p.getX() < 0 || p.getY() < 0 || p.getX() >= world.getWidth() || p.getY() >= world.getLength()) {
					return;
				}
				/* Check that the new position is free
				with the exception of Water entities */
				List<BaseEntity> tileEntities =
						world.getEntities((int)p.getX(), (int)p.getY());

				if (this.moveAway(tileEntities)) {
					// No good
					return;
				}
				/* Finally move to that position using a move action */
				this.setAction(new MoveAction((int)p.getX(), (int)p.getY(), this));
				return;
			}
			// Stances are considered after this point
			
			//Enemies within attack range are found
			List<BaseEntity> entityList = GameManager.get().getWorld().getEntities();
			List<AttackableEntity> enemy = new ArrayList<AttackableEntity>();
			//For each entity in the world
			for (BaseEntity e: entityList) {
				//If an attackable entity
				if (e instanceof AttackableEntity) {
					//Not owned by the same player
					AttackableEntity attackable = (AttackableEntity) e;
					if (!this.sameOwner(attackable)) {
						//Within attacking distance
						float diffX = attackable.getPosX() - this.getPosX();
						float diffY = attackable.getPosY() - this.getPosY();
						if (Math.abs(diffX) + Math.abs(diffY) <= this.getAttackRange()) {
							enemy.add((AttackableEntity) e);
						}
					}
				}
			}
			
			getStances(enemy);
			
			return;
		}
		if (!currentAction.get().completed()) {
			currentAction.get().doAction();
		} else {
			currentAction = Optional.empty();

		}


	}
	
	/**
	 * Get the stance of the entity and react.
	 * @param enemy
	 */
	public void getStances(List<AttackableEntity> enemy) {
		if(this.getLoadStatus()!=1) {
			switch (getStance()) {
				//Passive
				case 0:
					break;
				//Defensive
				case 1:
					if (!enemy.isEmpty()) {
						defensiveBehaviour(enemy);
					}
					break;
				//Aggressive
				case 2:
					if (!enemy.isEmpty()) {
						aggressiveBehaviour(enemy);
					}
					break;
				//Skirmishing
				case 3:
					if (!enemy.isEmpty()) {
						skirmishingBehaviour(enemy);
					}
					break;
				//Timid
				case 4:
					if (!enemy.isEmpty()) {
						timidBehaviour(enemy);
					}
					break;
				default:
					break;
			}
		}
	}
	
	/**
	 * Will attack any enemy in its attack range but not chase.
	 * @param enemy
	 */
	public void defensiveBehaviour(List<AttackableEntity> enemy) {
		//Attack closest enemy
		for (int i=1; i<=getAttackRange(); i++) {
			for (AttackableEntity a: enemy) {
				float xDistance = a.getPosX() - this.getPosX();
				float yDistance = a.getPosY() - this.getPosY();
				boolean distanceEquality = Math.abs(Math.abs(yDistance) + Math.abs(xDistance) - i) < 0.01;
				if (distanceEquality) {
					attackDefensively(a);
					return;
				}
			}
		}
	}
	
	/**
	 * Will attack any enemy in its attack range.
	 * @param enemy
	 */
	public void aggressiveBehaviour(List<AttackableEntity> enemy) {
		//Attack closest enemy
		for (int i=1; i<=getAttackRange(); i++) {
			for (AttackableEntity a: enemy) {
				float xDistance = a.getPosX() - this.getPosX();
				float yDistance = a.getPosY() - this.getPosY();
				boolean distanceEquality = Math.abs(Math.abs(yDistance) + Math.abs(xDistance) - i) < 0.01;
				if (distanceEquality) {
					attack(a);
					return;
				}
			}
		}
	}
	
	/**
	 * Will move to just within attack range of enemy units and then attack.
	 * @param enemy
	 */
	public void skirmishingBehaviour(List<AttackableEntity> enemy) {
		//Run away from closest enemy until just within attack range
		for (int i = 1; i < getAttackRange(); i++) { 
			for (AttackableEntity a: enemy) {
				float xDistance = a.getPosX() - this.getPosX();
				float yDistance = a.getPosY() - this.getPosY();
                boolean distanceEquality = Math.abs(Math.abs(yDistance) + Math.abs(xDistance) - i) < 0.01;
                performSkirmishingbehaviour(distanceEquality, xDistance, yDistance, a);
			}
		}
		//Consider attacking at maximum range
		for (AttackableEntity a: enemy) {
			float xDistance = a.getPosX() - this.getPosX();
			float yDistance = a.getPosY() - this.getPosY();
			boolean distanceEquality = Math.abs(Math.abs(yDistance) +
					Math.abs(xDistance) - this.getAttackRange()) < 0.01;
			if (distanceEquality) {
				attack(a);
				return;
			}
		}
	}
	
	/**
	 * Helper method for skirmishing behavior.
	 * 
	 */
	private void performSkirmishingbehaviour(boolean distanceEquality, float xDistance, float yDistance, AttackableEntity a) {
        if (distanceEquality) {
			float xLocation;
			float yLocation;
			float xDist = xDistance;
			float yDist = yDistance;
			//If xDistance and yDistance is the same increment one by random
			boolean distanceEquality2 = Math.abs(xDist - yDist) < 0.01 ;
			if (distanceEquality2) {
				Random random = new Random();
				if (random.nextBoolean()) {
					xDist += 1;
				} else {
					yDist += 1;
				}
			}
			if (xDist > yDist) {
				if (a.getPosX() - this.getPosX() > 0) {
					xLocation = this.getPosX() - 1;
				} else {
					xLocation = this.getPosX() + 1;
				} 
				yLocation = this.getPosY();
			} else {
				if (a.getPosY() - this.getPosY() > 0) {
					yLocation = this.getPosY() - 1;
				} else {
					yLocation = this.getPosY() + 1;
				}
				xLocation = this.getPosX();
			}
			this.setAction(new MoveAction((int)xLocation , (int)yLocation, this));
			return;
		}
	}
	
	/**
	 * Will run away from enemy units.
	 * @param enemy
	 */
	public void timidBehaviour(List<AttackableEntity> enemy) {
		//Run away from closest enemy
		for (int i=1; i<=getAttackRange(); i++) { //Distance is based on attack range at the moment
			for (AttackableEntity a: enemy) {
				float xDistance = a.getPosX() - this.getPosX();
				float yDistance = a.getPosY() - this.getPosY();
				boolean distanceEquality = Math.abs(Math.abs(yDistance) + Math.abs(xDistance) - i) < 0.01;
				if (distanceEquality) {
					float xLocation = this.getPosX() + (this.getPosX() - a.getPosX());
					float yLocation = this.getPosY() + (this.getPosY() - a.getPosY());
					this.setAction(new MoveAction((int)xLocation , (int)yLocation, this));
					return;
				}
			}
		}
	}
	
	@Override
	public String toString(){
		return this.name;
	}
	
	/**
	 * Gets default texture
	 * @return returns string representing default texture
	 */
	public String getDefaultTexture(){
		return defaultTextureName;
	}
	
	public void setAllTextture() {
		TextureManager tm = (TextureManager) GameManager.get().getManager(TextureManager.class);
		try {
			this.selectedTextureName = tm.loadUnitSprite(this, "selected");
			this.defaultTextureName = tm.loadUnitSprite(this, "default") ;
			this.upleftTextureName = tm.loadUnitSprite(this, "upleft") ;
			this.uprightTextureName = tm.loadUnitSprite(this, "upright") ;
			this.downleftTextureName = tm.loadUnitSprite(this, "downleft") ;
			this.downrightTextureName = tm.loadUnitSprite(this, "downright") ;
			this.defaultMissileName = tm.loadUnitSprite(this, "missile");
			this.movementSound = "endturn.wav";
		}
		catch(NullPointerException n){
			LOGGER.error("setAlltexture has error");
			return;
		}
	}

	/**
	 * Returns the current action of the entity
	 * @return current action
	 */
	@Override
	public Optional<DecoAction> getCurrentAction() {
		return currentAction;
	}
	
	/**
	 * @return The stats of the entity
	 */
	public EntityStats getStats() {
		return new EntityStats(this.name, this.getHealth(),this.getMaxHealth(), null, this.getCurrentAction(), this);
	}

	@Override
	public void setNextAction(ActionType action) {
		this.nextAction = action;
	}
	
	@Override
	public String getMissileTexture() {
		return defaultMissileName;
	}
	
	/**
	 * Increase the loyalty of the entity in a certain period. Please include this to your unit if you override ontick
	 */
	public void regeneration() {
		this.setRegenInterval(this.getRegenInterval() - 10);
		if ((this.getRegenInterval()) <= 0) {
			this.setLoyalty(this.getLoyalty() + 15);
			this.setSpeed(this.getSpeed() + 0.01f);
			this.setAttackSpeed(this.getAttackSpeed() + 2);
			this.resetRegenInterval();
			return;
		}
	}
	
	/**
	 * If the owner status change, the texture should be reloaded.
	 * Please include this to your unit if you override ontick
	 */
	public void checkOwnerChange() {
		if (this.getOwnerChangedStatus()) {
			//turn of the fog of war for this entity when it switches side
			modifyFogOfWarMap(false,getFogRange());

			this.setAllTextture();
			this.setOwnerChangedStatus(false);
			this.setTexture(defaultTextureName);
		}
	}
	
}