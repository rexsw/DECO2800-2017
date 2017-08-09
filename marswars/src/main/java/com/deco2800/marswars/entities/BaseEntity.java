package com.deco2800.marswars.entities;

import com.deco2800.marswars.worlds.BaseWorld;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.util.Box3D;

/**
 * Created by timhadwen on 2/8/17.
 */
public class BaseEntity extends AbstractEntity {

	private int cost = 0;

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
