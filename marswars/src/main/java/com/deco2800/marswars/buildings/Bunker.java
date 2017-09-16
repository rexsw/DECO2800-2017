package com.deco2800.marswars.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.entities.Clickable;
import com.deco2800.marswars.entities.HasOwner;
import com.deco2800.marswars.entities.HasProgress;
import com.deco2800.marswars.entities.Tickable;
import com.deco2800.marswars.managers.AbstractPlayerManager;
import com.deco2800.marswars.managers.Manager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.PlayerManager;
import com.deco2800.marswars.worlds.AbstractWorld;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by judahbennett on 25/8/17.
 *
 * A bunker that can be used to increase population
 */

public class Bunker extends BuildingEntity implements Clickable, Tickable, HasProgress, HasOwner {

	/**
	 * Constructor for the bunker.
	 * @param world The world that will hold the bunker.
	 * @param posX its x position on the world.
	 * @param posY its y position on the world.
	 * @param posZ its z position on the world.
	 */
	public Bunker(AbstractWorld world, float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, BuildingType.BUNKER, owner);
	}
}
