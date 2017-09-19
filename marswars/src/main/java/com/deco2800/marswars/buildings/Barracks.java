package com.deco2800.marswars.buildings;

import com.deco2800.marswars.entities.Clickable;
import com.deco2800.marswars.entities.HasOwner;
import com.deco2800.marswars.entities.HasProgress;
import com.deco2800.marswars.entities.Tickable;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.AbstractWorld;

/**
 * Created by JudahBennett on 25/8/17.
 *
 * A barracks to build an army
 */
public class Barracks extends BuildingEntity implements Clickable, Tickable, HasProgress, HasOwner {

	/**
	 * Constructor for the barracks.
	 * @param world The world that will hold the base.
	 * @param posX its x position on the world.
	 * @param posY its y position on the world.
	 * @param posZ its z position on the world.
	 */
	public Barracks(AbstractWorld world, float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, BuildingType.BARRACKS, owner);
	}

	@Override
	public void makeSelected() {
		super.makeSelected();
		GameManager.get().getGui().showEntitiesPicker(true, true);
		GameManager.get().getGui().addUnitsPickerMenu(true);
	}

	@Override
	public void deselect() {
		super.deselect();
		GameManager.get().getGui().showEntitiesPicker(false, true);
	}
}
