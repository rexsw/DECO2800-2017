package com.deco2800.marswars.entities;

import com.deco2800.marswars.World;
import com.deco2800.moos.entities.AbstractEntity;
import com.deco2800.moos.managers.GameManager;
import com.deco2800.moos.util.Box3D;

/**
 * Created by timhadwen on 2/8/17.
 */
public class BaseEntity extends AbstractEntity {

	private int cost = 0;

	public BaseEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength) {
		super(posX, posY, posZ, xLength, yLength, zLength);
	}

	public BaseEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength, float xRenderLength, float yRenderLength, boolean centered) {
		super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, centered);
	}

	public BaseEntity(Box3D position, float xRenderLength, float yRenderLength, boolean centered) {
		super(position, xRenderLength, yRenderLength, centered);
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public boolean isCollidable() {
		return true;
	}

	@Override
	public void setPosition(float x, float y, float z) {
		modifyCollisionMap(false);
		super.setPosition(x, y, z);
		modifyCollisionMap(true);
	}

	@Override
	public void setPosX(float x) {
		modifyCollisionMap(false);
		super.setPosX(x);
		modifyCollisionMap(true);
	}

	@Override
	public void setPosY(float y) {
		modifyCollisionMap(false);
		super.setPosY(y);
		modifyCollisionMap(true);
	}

	@Override
	public void setPosZ(float z) {
		modifyCollisionMap(false);
		super.setPosZ(z);
		modifyCollisionMap(true);
	}

	private void modifyCollisionMap(boolean add) {
		if (GameManager.get().getWorld() instanceof World) {
			World world = (World) GameManager.get().getWorld();
			int left = (int) getPosX();
			int right = (int) Math.ceil(getPosX() + getXLength());
			int bottom = (int) getPosY();
			int top = (int) Math.ceil(getPosY() + getYLength());
			for (int x = left; x < right; x++) {
				for (int y = bottom; y < top; y++) {
					if (add)
						world.getCollisionMap().get(x, y).add(this);
					else
						world.getCollisionMap().get(x, y).remove(this);
				}
			}
		}
	}
}
