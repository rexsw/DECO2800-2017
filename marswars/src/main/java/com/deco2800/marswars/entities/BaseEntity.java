package com.deco2800.marswars.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.managers.FogOfWarManager;
import com.deco2800.marswars.worlds.BaseWorld;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.Manager;
import com.deco2800.marswars.util.Box3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by timhadwen on 2/8/17.
 */
public class BaseEntity extends AbstractEntity implements Selectable {
	private int cost = 0;
	private float buildSpeed = 1;
	private EntityType entityType = EntityType.NOT_SET;
	private  List<ActionType> validActions;
	private boolean selected = false;
	protected float speed = 0.05f;

	/**
	 * Constructor for the base entity
	 * @param posX
	 * @param posY
	 * @param posZ
	 * @param xLength
	 * @param yLength
	 * @param zLength
	 */
	public BaseEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength) {
		super(posX, posY, posZ, xLength, yLength, zLength);
		this.modifyCollisionMap(true);
	}

	/**
	 * Full blown constructor for the base entity
	 * @param posX
	 * @param posY
	 * @param posZ
	 * @param xLength
	 * @param yLength
	 * @param zLength
	 * @param xRenderLength
	 * @param yRenderLength
	 * @param centered
	 */
	public BaseEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength, float xRenderLength, float yRenderLength, boolean centered) {
		super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, centered);
	}

	/**
	 * Outdated constructor for the base entity
	 * @deprecated
	 * @param position
	 * @param xRenderLength
	 * @param yRenderLength
	 * @param centered
	 */
	@Deprecated
	public BaseEntity(Box3D position, float xRenderLength, float yRenderLength, boolean centered) {
		super(position, xRenderLength, yRenderLength, centered);
	}

	/**
	 * Gets the path finding cost of this entity
	 * @return
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * Sets the pathfinding cost for this entity
	 * @param cost
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	/**
	 * Gets the build speed modifier for this entity
	 * @return
	 */
	public float getSpeed() {
		return buildSpeed;
	}

	/**
	 * Sets the build speed modifier for this entity
	 * @param speed
	 */
	public void setSpeed(float speed) {
		this.buildSpeed = speed;
	}

	/**
	 * Checks if the entity is collidable
	 * @return
	 */
	public boolean isCollidable() {
		return (!super.canWalkOver);
	}

	/**
	 * Sets the current position of the Base Entity and also updates its position in the collision map
	 * @param x
	 * @param y
	 * @param z
	 */
	@Override
	public void setPosition(float x, float y, float z) {
		modifyCollisionMap(false);
		super.setPosition(x, y, z);
		modifyCollisionMap(true);
	}
	
	/**
	 * Workaround for making position line up with rendered object rendered over multiple tiles
	 * @param xPos
	 * @param yPos
	 * @param zPos
	 */
	public void fixPosition(int xPos, int yPos, int zPos) {
		modifyCollisionMap(false);
		if (GameManager.get().getWorld() instanceof BaseWorld) {
			BaseWorld baseWorld = (BaseWorld) GameManager.get().getWorld();
			int left = (int) xPos;
			int right = (int) Math.ceil(xPos + getXLength());
			int bottom = (int) yPos;
			int top = (int) Math.ceil(yPos + getYLength());
			for (int x = left; x < right; x++) {
				for (int y = bottom; y < top; y++) {
						baseWorld.getCollisionMap().get(x, y).add(this);
				}
			}	
		}
	}


	/**
	 * Sets the Position X
	 * @param x
	 */
	@Override
	public void setPosX(float x) {
		modifyCollisionMap(false);
		super.setPosX(x);
		modifyCollisionMap(true);
	}

	/**
	 * Sets the position Y
	 * @param y
	 */
	@Override
	public void setPosY(float y) {
		modifyCollisionMap(false);
		super.setPosY(y);
		modifyCollisionMap(true);
	}

	/**
	 * Sets the position Z
	 * @param z
	 */
	@Override
	public void setPosZ(float z) {
		modifyCollisionMap(false);
		super.setPosZ(z);
		modifyCollisionMap(true);
	}

	/**
	 * Returns true if currently selected
	 * @return
	 */
	public boolean isSelected() {
		return this.selected;
	}

	public void makeSelected() {
		this.selected = true;

	}

	/**
	 * Deselects this object
	 */
	public void deselect() {
		this.selected = false;
	}

	/**
	 *
	 * @return the list of actions the entity is allowed to take
	 */
	@Override
	public List<ActionType> getValidActions() {
		return this.validActions;
	}

	/**
	 * Instantiates the list of actions
	 * @deprecated addNewActions will now automatically instantiate the list if it does not exist
	 */
	@Deprecated
	public void initActions() {
		this.validActions = new ArrayList<ActionType>();
	}

	/**
	 *Adds a new valid action to the entity
	 * @param newAction The new action that is valid for the unit to perform
	 * @return True if successful, false if the action was not added or if it was already in the list
	 */

	@Override
	public boolean addNewAction(ActionType newAction) {
		if (this.validActions == null) {
			this.validActions = new ArrayList<ActionType>();
		}
		for (ActionType d: this.validActions) {
			if (d == newAction) {
				return false;
			}
		}
		this.validActions.add(newAction);
		return true;
	}

	/**
	 *Removes a valid action from the entity
	 * @param actionToRemove The new action that is valid for the unit to perform
	 * @return True if successful, false if the action failed to remove or did not exist in the list
	 */
	@Override
	public boolean removeActions(ActionType actionToRemove) {
		if (this.validActions == null){
			return false;
		}
		for (ActionType d: this.validActions) {
			if (d == actionToRemove) {
				this.validActions.remove(d);
				return true;
			}
		}
		return false;
	}

	/**
	 * This method returns a value denoting the type of entity it is
	 * 0 = Unset
	 * 1 = Resource
	 * 2 = Building
	 * 3 = Unit
	 * 4 = Hero
	 * @return the entity type
	 */
	@Override
	public EntityType getEntityType() {
		return this.entityType;
	}

	/**
	 * This sets a value denoting the type of entity it is
	 * 0 = Unset
	 * 1 = Resource
	 * 2 = Building
	 * 3 = Unit
	 * 4 = Hero
	 * @return the new entity type
	 */
	@Override
	public EntityType setEntityType(EntityType newType) {
		this.entityType = newType;
		return  this.entityType;
	}

	@Override
	public Button getButton() {
		return null;
	}

	@Override
	public void buttonWasPressed() {return;}
	
	/**
	 * Returns a label to display into 'Actions' of the HUD 
	 */
	@Override
	public Label getHelpText() {
		String message = "";
		switch (this.entityType){
			case NOT_SET:
				 message ="This entity has not had its type set";
				break;
			case BUILDING:
				message ="This is a building";
				break;
			case UNIT:
				message ="This is a unit";
				break;
			case HERO:
				message ="This is a hero";
				break;
			case RESOURCE:
				message ="This is a resource";
				break;
			case AISPACMAN:
				message = "This is an AI spacman";
				break;
			case TECHTREE:
				message = "You have clicked on the base";
				break;
			default:
				break;

		}
		return new Label(message, new Skin(Gdx.files.internal("uiskin.json")));
	}

	/**
	 * Updates the collision map
	 * @param add
	 */
	protected void modifyCollisionMap(boolean add) {
		if (GameManager.get().getWorld() == null) {
			return;
		}

		BaseWorld baseWorld = GameManager.get().getWorld();
		int left = (int) getPosX();
		int right = (int) Math.ceil(getPosX() + getXLength());
		int bottom = (int) getPosY();
		int top = (int) Math.ceil(getPosY() + getYLength());

		for (int x = left; x < right; x++) {
			for (int y = bottom; y < top; y++) {
				if (add) {
					baseWorld.getCollisionMap().get(x, y).add(this);
				} else {
					baseWorld.getCollisionMap().get(x, y).remove(this);
				}
			}
		}
	}

	protected void modifyFogOfWarMap(boolean add) {

		int left = (int) getPosX();
		int right = (int) Math.ceil(getPosX() + getXLength());
		int bottom = (int) getPosY();
		int top = (int) Math.ceil(getPosY() + getYLength());

		for (int x = left; x < right; x++) {
			for (int y = bottom; y < top; y++) {
				if (add) {
					FogOfWarManager.sightRange(x,y,2,add);
				} else {
					FogOfWarManager.sightRange(x,y,2,add);
				}
			}
		}
	}

	/**
	 * @return The stats of the entity
	 */
	public EntityStats getStats() {
		return new EntityStats("UNNAMED",0, null, Optional.empty(), this);
	}

	/**
	 * Forces the unit to only try the chosen action on the next rightclick
	 * @param nextAction the action to be forced
	 */
	public void setNextAction(ActionType nextAction) {
		return;
	}

	/**
	 * Causes the entity to perform the action
	 * @param action the action to perform
	 */
	public void setAction(DecoAction action) {
		return;
	}

	public float getMoveSpeed() {
		return speed;
	}

	public void setMoveSpeed(float speed) {
		this.speed = speed;
	}
}
