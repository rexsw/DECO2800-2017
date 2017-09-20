package com.deco2800.marswars.buildings;

import com.deco2800.marswars.entities.BaseEntity;

/**
 * Created by grumpygandalf on 26/8/17.
 *
 * A select area which will turn red when build area is impossible and green when valid
 */
public class CheckSelect extends BaseEntity{
	private BuildingType buildingType;
	private String validSelect;
	private String invalidSelect;
	/**
	 * Constructor for the GreenSelect
	 * @param posX
	 * @param posY
	 * @param posZ
	 * @param lengthX
	 * @param lengthY
	 * @param LengthZ
	 */
	public CheckSelect(float posX, float posY, float posZ, float lengthX, float lengthY, float lengthZ, BuildingType building) {
		super(posX, posY, posZ, lengthX+.25f, lengthY+.25f, lengthZ, lengthX, lengthY, false);
		super.canWalkOver = true;
		buildingType = building;
	}
	
	/**
	 *Sets the build area to be green (valid)
	 */
	public void setGreen() {
		switch(buildingType) {
		case TURRET:
			validSelect = "greenSelect1";
			break;
		case BASE:
			validSelect = "greenSelect2";
			break;
		case BARRACKS:
			validSelect = "greenSelect3";
			break;
		case BUNKER:
			validSelect = "greenSelect1";
			break;
		case HEROFACTORY:
			validSelect = "greenSelect1";
			break;
		default:
			validSelect = "greenSelect1";
			break;
		}
		this.setTexture(validSelect);
	}
	
	/**
	 *Sets the build area to be red (invalid)
	 */
	public void setRed() {
		switch(buildingType) {
		case TURRET:
			invalidSelect = "redSelect1";
			break;
		case BASE:
			invalidSelect = "redSelect2";
			break;
		case BARRACKS:
			invalidSelect = "redSelect3";
			break;
		case BUNKER:
			invalidSelect = "redSelect1";
			break;
		case HEROFACTORY:
			invalidSelect = "redSelect1";
			break;
		default:
			validSelect = "greenSelect1";
			break;
		}
		this.setTexture(invalidSelect);
	}

}