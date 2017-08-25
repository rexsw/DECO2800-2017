package com.deco2800.marswars.entities;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.ImpactAction;
import com.deco2800.marswars.actions.MoveAction;

public class Bullet extends MissileEntity implements Tickable {
	
	private Optional<DecoAction> currentAction = Optional.empty();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Bullet.class);
	private int damage;
	private int armorDamage;
	private AttackableEntity target;

    public Bullet(float posX, float posY, float posZ, AttackableEntity target, int damage, int armorDamage) {
        super(posX, posY, posZ, 1, 1, 1, target, armorDamage, armorDamage);
        this.setTexture("spacman_blue"); //Placeholder texture
        this.initActions();
        this.addNewAction(MoveAction.class);
        this.addNewAction(ImpactAction.class);
        this.setDamage(damage);
        this.setArmorDamage(armorDamage);
        currentAction = Optional.of(new ImpactAction(this, target));
    }

    @Override
    public void onTick(int tick) {
		/* If the action is completed, remove it otherwise keep doing that action */
		if (!currentAction.get().completed()) {
			currentAction.get().doAction();
		} else {
			LOGGER.info("Action is completed. Deleting");
			currentAction = Optional.empty();
		}
    }
}