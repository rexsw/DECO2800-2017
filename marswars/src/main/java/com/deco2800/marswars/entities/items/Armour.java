package com.deco2800.marswars.entities.items;

import java.util.ArrayList;
import java.util.List;

import com.deco2800.marswars.entities.items.effects.DefenceEffect;
import com.deco2800.marswars.entities.items.effects.Effect;

public class Armour extends Item {

    private ArmourType type;
    private int lvl;
    private List<Effect> effects;
    //private AttackEffect effect;

    public Armour(ArmourType type, int lvl) {
    	this.effects = new ArrayList<>();
        this.type = type;
        this.lvl = lvl;
        this.effects.add(new DefenceEffect(getArmourValue(), getArmourHealth(), getMoveSpeed()));
    }

    public int getArmourValue() {
        return type.getArmourValue(lvl);
    }
    
    public int getArmourHealth() {
    	return type.getArmourHealth(lvl);
    }
    
    public int getMoveSpeed() {
    	return type.getMoveSpeed(lvl);
    }

    public int getLevel() {
    	return this.lvl;
    }
    
    public List<Effect> getEffect() {
    	return new ArrayList<Effect>(effects);
    }
    
    @Override
    public Type getItemType() {
        return Type.ARMOUR;
    }

    @Override
    public String getName() {
        return type.getName();
    }
    
    @Override
    public String getDescription() {
		return this.getName() + "\nArmour: " + this.getArmourValue() + "\nHealth: " + this.getArmourHealth() + "\nMovementSpeed: " + this.getMoveSpeed();
    	
    }
}