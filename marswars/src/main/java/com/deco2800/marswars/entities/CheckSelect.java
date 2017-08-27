package com.deco2800.marswars.entities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.actions.GenerateAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.Selectable.EntityType;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.SoundManager;
import com.deco2800.marswars.worlds.AbstractWorld;
import com.deco2800.marswars.worlds.BaseWorld;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by grumpygandalf on 26/8/17.
 *
 * A select area which will turn red when build area is impossible and green when valid
 */
public class CheckSelect extends BaseEntity{
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckSelect.class);
	/**
	 * Constructor for the GreenSelect
	 * @param posX
	 * @param posY
	 * @param posZ
	 * @param lengthX
	 * @param lengthY
	 * @param LengthZ
	 */
	public CheckSelect(float posX, float posY, float posZ, float lengthX, float lengthY, float lengthZ) {
		super(posX, posY, posZ, lengthX, lengthY, lengthZ, lengthX, lengthY, false);
		this.setTexture("grass");
		super.canWalkOver = true;
	}
	
	/**
	 *Sets the build area to be green (valid)
	 */
	public void setGreen() {
		this.setTexture("greenSelect");
	}
	
	/**
	 *Sets the build area to be red (invalid)
	 */
	public void setRed() {
		this.setTexture("redSelect");
	}

}