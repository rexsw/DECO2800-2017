package com.deco2800.marswars.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.GenerateAction;
import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.entities.Clickable;
import com.deco2800.marswars.entities.HasOwner;
import com.deco2800.marswars.entities.HasProgress;
import com.deco2800.marswars.entities.Spacman;
import com.deco2800.marswars.entities.Tickable;
import com.deco2800.marswars.managers.AbstractPlayerManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.Manager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.PlayerManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.worlds.AbstractWorld;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by JudahBennett on 25/8/17.
 *
 * A barracks to build an army
 */
public class Barracks extends BuildingEntity implements Clickable, Tickable, HasProgress, HasOwner {

	/* A single action for this building */
	Optional<DecoAction> currentAction = Optional.empty();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Barracks.class);
	
	private Manager onwer = null;

	boolean selected = false;

	/**
	 * Constructor for the barracks.
	 * @param world The world that will hold the base.
	 * @param posX its x position on the world.
	 * @param posY its y position on the world.
	 * @param posZ its z position on the world.
	 */
	public Barracks(AbstractWorld world, float posX, float posY, float posZ, AbstractPlayerManager owner) {
		super(posX, posY, posZ, BuildingType.BARRACKS, owner);
	}
}
