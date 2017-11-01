package com.deco2800.marswars.entities.units;


import com.badlogic.gdx.audio.Sound;
import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.actions.BuildAction;
import com.deco2800.marswars.actions.BuildWallAction;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.actions.LoadAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.buildings.Turret;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.EntityStats;
import com.deco2800.marswars.entities.GatheredResource;
import com.deco2800.marswars.entities.terrainelements.Resource;
import com.deco2800.marswars.managers.GameBlackBoard;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.SoundManager;
import com.deco2800.marswars.worlds.BaseWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;


/**
 * A combat unit that can gather resources
 * @author Tze Thong Khor
 *
 */
public class Astronaut extends Soldier {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Astronaut.class);
	private GatheredResource gatheredResource = null;
	private DecoAction build;

	public Astronaut(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, owner);
		this.name = "Astronaut";
		this.removeActions(ActionType.DAMAGE);
		setAttributes();
		setStance(0); // Default stance for astronaut is passive
	}
	
	
	/**
	 * Overrides Left click and checks if build action is in progress and handles appropriately
	 * param handler the mouse handler
	 * @author grumpygandalf
	 *
	 */
	@Override
	public void onClick(MouseHandler handler) {
		//check if this belongs to a* player (need to change for multiplayer):
		if (this.getCurrentAction().isPresent() && (this.getCurrentAction().get() instanceof BuildAction || this.getCurrentAction().get() instanceof BuildWallAction)) {
			return;
		}
		if(!this.isAi() & this.getLoadStatus() != 1) {
			handler.registerForRightClickNotification(this);
			this.setTexture(selectedTextureName);
			LOGGER.info("Clicked on soldier");
			this.makeSelected();
		} else {
			LOGGER.info("Clicked on ai soldier");
			 this.makeSelected();
		}
	}
	
	@Override
	public void onRightClick(float x, float y) {
		if (!this.isSelected()) {
			return;
		}
		List<BaseEntity> entities;
		try {
			entities = ((BaseWorld) GameManager.get().getWorld()).getEntities((int) x, (int) y);

		} catch (IndexOutOfBoundsException e) {
			// if the right click occurs outside of the game world, nothing will happen
			LOGGER.info("Right click occurred outside game world.",e);
			this.setTexture(defaultTextureName);
			this.deselect();
			return;
		}
		if (this.getCurrentAction().isPresent() && this.getCurrentAction().get()
				instanceof BuildAction) {
			((BuildAction)build).finaliseBuild();
			this.setTexture(defaultTextureName);
			this.deselect();
			return;

		}else if(this.getCurrentAction().isPresent() && this.getCurrentAction().get()
				instanceof BuildWallAction) {
				((BuildWallAction)build).beginWall(x, y);
				return;
		}
		boolean attack = !entities.isEmpty() && entities.get(0) instanceof AttackableEntity;
		boolean gatherResource = !entities.isEmpty() && entities.get(0) instanceof Resource;
		Turret loadTurret = null;
		for (BaseEntity b : entities) {
			if (b instanceof Turret && b.sameOwner(this)) {
				loadTurret = (Turret)b;
			}
		}
		if (loadTurret != null){ 
			LOGGER.debug("LOADING INTO TURRET");
			this.setCurrentAction(Optional.of(new LoadAction(this, loadTurret)));
			this.setTexture(defaultTextureName);
			SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
			Sound loadedSound = sound.loadSound(movementSound);
			sound.playSound(loadedSound);
			return;
		}
		if (attack) {
			// we cant assign different owner yet
			AttackableEntity target = (AttackableEntity) entities.get(0);
			attack(target);
		} else if (gatherResource) { 
			LOGGER.info("Gather resources");
			this.setCurrentAction(Optional.of(new GatherAction(this, entities.get(0))));
		} else {
			this.setAction((new MoveAction((int) x, (int) y, this)));
			LOGGER.error("Assigned action move to" + x + " " + y);
		}
		this.setTexture(defaultTextureName);
		SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
		Sound loadedSound = sound.loadSound(movementSound);
		sound.playSound(loadedSound);
	}

	/**
	 * Add gathered resource to unit's backpack
	 * @param resource
	 */
	public void addGatheredResource(GatheredResource resource) {
		this.gatheredResource = resource;

	}
	
	/**
	 * check if the unit has resource in it's backpack
	 * @return true if this unit carries something
	 */
	public boolean checkBackpack() {
		return gatheredResource != null;
	}
	
	/**
	 * remove the resource from unit's backpack
	 * @return gatheredResource
	 */
	public GatheredResource removeGatheredResource() {
		GatheredResource resource = new GatheredResource (gatheredResource.getType(), gatheredResource.getAmount());
		gatheredResource = null;
		return resource;
	}
	
	/**
	 * Returns Astronaut as the name
	 * @return String name of Astronaut
	 */
	@Override
	public String toString(){
		return this.name;
	}
	
	/**
	 * Causes the entity to perform the action
	 * @param action the action to perform
	 */
	@Override
	public void setAction(DecoAction action) {
		currentAction = Optional.of(action);
		if (action instanceof BuildAction) {
			build = (BuildAction)action;
		}else if (action instanceof BuildWallAction) {
			build = (BuildWallAction)action;
		}}
	
	/**
	 * Gets the current build action of this entity
	 * @return current BuildAction, null if not currently building
	 */
	public DecoAction getBuild() {
		if (currentAction.isPresent()) {
			return build;
		}
		return null;
	}

	/**
	 * @return The stats of the entity
	 */
	public EntityStats getStats() {
		return new EntityStats(this.name, this.getHealth(),this.getMaxHealth(), null, this.getCurrentAction(), this);
	}
	
	/**
	 * Set the health of the entity. When the health is dropped, the entity
	 * gotHit status is set to true. This method overrides the basic method to
	 * also remove building if astronaut is killed during build process
	 *
	 * @param health the health of the entity
	 */
	@Override
	public void setHealth(int health) {
		LOGGER.info("SETTING HEALTH");
		if (this.health > health) {
			this.setGotHit(true);
		}
		if (health <= 0) {
			if (this.getAction().isPresent() && (this.getAction().get() instanceof BuildAction || this.getAction().get() instanceof BuildWallAction)) {
					if (this.getAction().get() instanceof BuildAction) {
						BuildAction destroyBuild = (BuildAction)this.getAction().get();
						destroyBuild.cancelBuild();
						destroyBuild.doAction();
					}else {
						BuildWallAction destroyBuild = (BuildWallAction)this.getAction().get();
						destroyBuild.cancelBuild();
						destroyBuild.doAction();
					}
			}
			GameBlackBoard black = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
			black.updateDead(this);
			GameManager.get().getWorld().removeEntity(this);
			GameManager.get().getWorld().removeEntity(this.getHealthBar());
			LOGGER.info("DEAD");
		}
		if (health >= this.getMaxHealth()) {
			this.health = this.getMaxHealth();
			return;
		}
		this.health  = health;
	}
}
