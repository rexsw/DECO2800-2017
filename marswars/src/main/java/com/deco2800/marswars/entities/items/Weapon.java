package com.deco2800.marswars.entities.items;

import java.util.ArrayList;
import java.util.List;

import com.deco2800.marswars.entities.items.effects.AttackEffect;
import com.deco2800.marswars.entities.items.effects.Effect;

public class Weapon extends Item {
    private WeaponType type;
    private int lvl;
    private List<Effect> effects;
    //private AttackEffect effect;

    public Weapon(WeaponType type, int lvl) {
    	this.effects = new ArrayList<>();
        this.type = type;
        this.lvl = lvl;
        this.effects.add(new AttackEffect(getWeaponDamage(), getWeaponSpeed(), getWeaponRange()));
    }

    public int getWeaponDamage() {
        return type.getWeaponDamage(lvl);
    }
    
    public int getWeaponRange() {
    	return type.getWeaponRange(lvl);
    }
    
    public int getWeaponSpeed() {
    	return type.getWeaponSpeed(lvl);
    }

    public int getLevel() {
    	return this.lvl;
    }
    
    public List<Effect> getEffect() {
    	return new ArrayList<Effect>(effects);
    }
    
    @Override
    public Type getItemType() {
        return Type.WEAPON;
    }

    @Override
    public String getName() {
        return type.getName();
    }
    
    @Override
    public String getDescription() {
		return this.getName() + "\nDamage: " + this.getWeaponDamage() + "\nSpeed: " + this.getWeaponSpeed() + "\nRange: " + this.getWeaponRange();
    	
    }
}

