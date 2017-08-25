package com.deco2800.marswars.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.actions.DamageAction;
import com.deco2800.marswars.actions.ImpactAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.managers.GameManager;

public class Bullet extends MissileEntity implements Tickable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Bullet.class);
	private int damage;
	private int armorDamage;
	private AttackableEntity target;

    public Bullet(float posX, float posY, float posZ, AttackableEntity target, int damage, int armorDamage) {
        super(posX, posY, posZ, 1, 1, 1, target, armorDamage, armorDamage);
        this.setTexture("spacman_blue"); //Placeholder texture
        this.initActions();
        this.addNewAction(ImpactAction.class);
        this.setDamage(damage);
        this.setArmorDamage(armorDamage);
    }

    @Override
    public void onTick(int tick) {
        //If target still exists move towards and hit
        //Remove the bullet and lower the health of the target
    	//Move towards target, if not at target move again
    	if (!isWorking()) { //Bullet not currently moving
    		this.setAction(new ImpactAction(this, target));
    	}
    	//Once at target impact action
    }
}