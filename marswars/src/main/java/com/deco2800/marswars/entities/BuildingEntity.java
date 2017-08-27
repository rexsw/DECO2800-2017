package com.deco2800.marswars.entities;

import com.deco2800.marswars.entities.Selectable.EntityType;
import com.deco2800.marswars.managers.MouseHandler;

/**
 * Created by grumpygandalf on 27/8/17.
 *
 * The base building class, will update later and reduce repeated code.
 */
public class BuildingEntity extends BaseEntity implements HasHealth {
	
	private int cost;
	private int buildingSpeed;
	private int xLength;
	private int yLength;
	private int health = 350;
	
	/**
	 * Constructor for the BuildingEntity
	 * @param posX
	 * @param posY
	 * @param posZ
	 * @param xLength
	 * @param yLength
	 * @param zLength
	 */
	public BuildingEntity(float posX, float posY, float posZ, BuildingType building) {
		super(posX, posY, posZ, 3f, 3f, 0f, 3f, 3f, false);
	}
	
	/**
	 * returns building health
	 * @return health
	 */
	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public void setHealth(int health) {
		this.health = health;
		
	}
	

}
