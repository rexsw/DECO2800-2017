package com.deco2800.marswars.entities.units;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.actions.AttackAction;
import com.deco2800.marswars.entities.Clickable;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.TechnologyManager;
import com.deco2800.marswars.managers.AiManager.State;



public class AmbientAnimal extends AttackableEntity{
	protected static final Logger LOGGER = LoggerFactory.getLogger(AttackableEntity.class);
	private AmbientState state;
	private int maxTravelTime;
	private int traveledTime;
	private String name;
	
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
		this.name = "Ambient";
		setDefaultAttributes();
		
	}
	
	public void setDefaultAttributes() {
		TechnologyManager t = (TechnologyManager) GameManager.get().getManager(TechnologyManager.class);
		this.setMaxHealth(t.getUnitAttribute(this.name, 1));
		this.setHealth(t.getUnitAttribute(this.name, 1));
		this.setDamage(t.getUnitAttribute(this.name, 2));
		this.setArmor(t.getUnitAttribute(this.name, 3));
		this.setMaxArmor(t.getUnitAttribute(this.name, 3));
		this.setArmorDamage(t.getUnitAttribute(this.name, 4));
		this.setAttackRange(t.getUnitAttribute(this.name, 5));
		this.setAttackSpeed(t.getUnitAttribute(this.name, 6));
		
		this.setSpeed(0.01f);
	}

	/**
	 * attack the unit who attacked it
	 */
	public void attack() {
		
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

	@Override
	public String toString(){
		return "Ambient Animal";
	}
	
	
	

}
