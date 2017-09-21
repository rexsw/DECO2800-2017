package com.deco2800.marswars.entities.units;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.actions.AttackAction;
import com.deco2800.marswars.entities.Selectable.EntityType;
import com.deco2800.marswars.managers.AiManager.State;



public class AmbientAnimal extends AttackableEntity{
	protected static final Logger LOGGER = LoggerFactory.getLogger(AttackableEntity.class);
	private AmbientState state;
	private int maxTravelTime;
	private int traveledTime;
	
	public static enum AmbientState {
		DEFAULT, TRAVEL, ATTACKBACK
	}
	
	public AmbientAnimal(float posX, float posY, float posZ, float xLength, float yLength, float zLength) {
		super(posX, posY, posZ, 1, 1, 1);
		maxTravelTime = 5;
		traveledTime = 0;
		state = AmbientState.DEFAULT;
		this.setOwner(0);
		this.setEntityType(EntityType.UNIT);
		
	}
	/**
	 * attack the unit who attacked it
	 */
	public void attack() {
		Optional.of(new AttackAction(this, this.getEnemy()));
	}
	
	public void setState(AmbientState newState){
		state = newState;
	}
	
	public AmbientState getState(){
		return state;
	}
	
	public int getMaxTravelTime(){
		return maxTravelTime;
	}
	public int getTravelTime(){
		return traveledTime;
	}
	
	
	
	public void setTravelTime(int time){
		this.traveledTime = time;
	}
	
	
	

}
