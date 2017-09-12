package com.deco2800.marswars.entities.items.ml_ver;

public enum EffectStats {
	DMG, // damage of attacks
	ARMOUR_DMG, // attacks' damage against armour
	ATK_SPD, // attack speed (i.e. in terms of interval between attacks)
	ATK_RNGE, // range of attack
	ARMOUR, // current armour, i.e. used to change the current amount of armour
	MAX_HP, // maximum health
	SPD, // movement speed
	LOYALTY, // loyalty
	FOV, // field of vision i.e. radius range of unit to uncover the fog of war
	HP, // current health i.e. used to increase health for purposes of healing items.
	DURATION, // duration of effect i.e. used for specifying how long a temporary effect would last.
	AOE_RNGE // range/radius of an "area of effect" effect
}
