package com.deco2800.marswars.entities;

/**
 * Created by grumpygandalf on 26/8/17.
 *
 * A select area which will turn red when build area is impossible and green when valid
 */
public class CheckSelect extends BaseEntity{
	/**
	 * Constructor for the GreenSelect
	 * @param posX
	 * @param posY
	 * @param posZ
	 * @param lengthX
	 * @param lengthY
	 * @param LengthZ
	 */
	public CheckSelect(float posX, float posY, float posZ, float lengthX, float lengthY, float lengthZ) {
		super(posX, posY, posZ, lengthX, lengthY, lengthZ, lengthX, lengthY, false);
		this.setTexture("grass");
		super.canWalkOver = true;
	}
	
	/**
	 *Sets the build area to be green (valid)
	 */
	public void setGreen() {
		this.setTexture("greenSelect");
	}
	
	/**
	 *Sets the build area to be red (invalid)
	 */
	public void setRed() {
		this.setTexture("redSelect");
	}

}