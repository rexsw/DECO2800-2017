package com.deco2800.marswars.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.worlds.BaseWorld;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.util.Box3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by timhadwen on 2/8/17.
 */
public class BaseEntity extends AbstractEntity implements Selectable {

	private int cost = 0;
	private EntityType entityType = EntityType.NOT_SET;
	private  List<Class> validActions;
	private boolean selected = false;

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
	 * @param
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
	 * Checks if the entity is collidable
	 * @return
	 */
	public boolean isCollidable() {
		return true;
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

	@Override
	public List<Class> getValidActions() {
		return this.validActions;
	}

	public void initActions() {
		this.validActions = new ArrayList<Class>();
	}

	/**
	 *Adds a new valid action to the entity
	 * @param newAction The new action that is valid for the unit to perform
	 * @return True if successful, false if the action was not added or if it was already in the list
	 */

	@Override
	public boolean addNewAction(Class newAction) {
		for (Class d: this.validActions) {
			if (d.equals(newAction)) {
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
	public boolean removeActions(Class actionToRemove) {
		for (Class d: this.validActions) {
			if (d.equals(actionToRemove)) {
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

		}
		return new Label(message, new Skin(Gdx.files.internal("uiskin.json")));
	}

	/**
	 * Updates the collision map
	 * @param add
	 */
	private void modifyCollisionMap(boolean add) {
		if (GameManager.get().getWorld() instanceof BaseWorld) {
			BaseWorld baseWorld = (BaseWorld) GameManager.get().getWorld();
			int left = (int) getPosX();
			int right = (int) Math.ceil(getPosX() + getXLength());
			int bottom = (int) getPosY();
			int top = (int) Math.ceil(getPosY() + getYLength());
			for (int x = left; x < right; x++) {
				for (int y = bottom; y < top; y++) {
					if (add)
						baseWorld.getCollisionMap().get(x, y).add(this);
					else
						baseWorld.getCollisionMap().get(x, y).remove(this);
				}
			}
		}
	}
}