package com.deco2800.marswars.entities.units;

import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.actions.AttackAction;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.entities.Clickable;
import com.deco2800.marswars.entities.HasAction;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.MouseHandler;


public class AmbientAnimal extends Soldier{
	
	
	public AmbientAnimal(float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, 0);
		this.setMaxHealth(1000);
		this.setHealth(1000);
		this.setDamage(75);
		this.setArmorDamage(150);
		this.setAttackRange(10);
		this.setAttackSpeed(20);
		//setAttributes();
		this.setAreaDamage(1);
	}

	private Optional<DecoAction> currentAction = Optional.empty();
	protected static final Logger LOGGER = LoggerFactory.getLogger(AttackableEntity.class);
	
	
	@Override
	public void onTick(int tick){
		//do nothing
	}
	@Override
	public void onClick(MouseHandler handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRightClick(float x, float y) {
		// TODO Auto-generated method stub
		
	}
	
	public void attack(AttackableEntity target) {
		currentAction = Optional.of(new AttackAction(this, target));
	}
	
	
	

}
