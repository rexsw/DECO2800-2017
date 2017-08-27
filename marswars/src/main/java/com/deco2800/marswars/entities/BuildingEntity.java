package com.deco2800.marswars.entities;

import com.deco2800.marswars.entities.Selectable.EntityType;
import com.deco2800.marswars.managers.MouseHandler;

/**
 * Created by grumpygandalf on 27/8/17.
 *
 * The base building class.
 */
public class BuildingEntity extends BaseEntity {
	
	private int cost;
	private int buildingSpeed;
	private int xLength;
	private int yLength;
	
	/**
	 * Constructor for the BuildingEntity
	 * @param posX
	 * @param posY
	 * @param posZ
	 * @param xLength
	 * @param yLength
	 * @param zLength
	 */
	public BuildingEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength, BuildingType building) {
		super(posX, posY, posZ, xLength, yLength, zLength);
		this.setEntityType(EntityType.BUILDING);
	}




}
