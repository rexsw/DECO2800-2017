package com.deco2800.marswars.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.marswars.entities.items.*;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TechnologyManager;
import com.deco2800.marswars.managers.TimeManager;

import com.deco2800.marswars.technology.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.deco2800.marswars.hud.ShopDialog;

import java.util.ArrayList;


/**
 * Creates the tech tree dialog view to be used by the player
 *
 */
public class TechTreeView extends Dialog{
	private TechnologyManager techMan = (TechnologyManager) GameManager.get().getManager(TechnologyManager.class);
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	HUDView hud;
	
	TechTreeView(String title, Skin skin, HUDView hud) {
		super(title, skin);
		this.hud = hud;

		this.getContentTable().debugCell();
		this.getButtonTable().debugCell().center();
		getButtonTable().defaults().height(60).width(200).padTop(10).padBottom(10);


		//Armour Upgrades
		getButtonTable().add(new Label("Armour Tech", skin));
		button("Extra Padding \n R: 10", 1); //$NON-NLS-1$
		button("Hard Shells", 2); //$NON-NLS-1$
		button("Spiky Armour", 3); //$NON-NLS-1$
		button("Cloyster Mode", 4); //$NON-NLS-1$

		getButtonTable().row();

		//Attack Damage Upgrades
		getButtonTable().add(new Label("Attack Damage Tech", skin));
		button("Anger", 5); //$NON-NLS-1$
		button("Rabid", 6); //$NON-NLS-1$
		button("Ferocity", 7); //$NON-NLS-1$
		button("Demonic Rage", 8); //$NON-NLS-1$

		getButtonTable().row();

		//Attack Speed Upgrades

		getButtonTable().add(new Label("Attack Speed Tech", skin));
		button("Sleight of Hand", 9); //$NON-NLS-1$
		button("Unnatural Dexterity", 10); //$NON-NLS-1$
		button("120 wpm", 11); //$NON-NLS-1$
		button("Korean Starcraft pro", 12); //$NON-NLS-1$

		getButtonTable().row();

		//Health Upgrades

		getButtonTable().add(new Label("Health Tech", skin));
		button("Swole", 13); //$NON-NLS-1$
		button("Jacked", 14); //$NON-NLS-1$
		button("Ripped", 15); //$NON-NLS-1$
		button("Beast Mode", 16); //$NON-NLS-1$

		getButtonTable().row();

		//Special techs

		getButtonTable().add(new Label("Special Technology", skin));

		button("Unlock Hero Factory", 17); //$NON-NLS-1$
		button("Steroids", 18); //$NON-NLS-1$
		button("Unlock Cow level", 19); //$NON-NLS-1$
		button("Vampirism", 20); //$NON-NLS-1$

		getButtonTable().row();

		//Armour Item Level Upgrades
		getButtonTable().add(new Label("Armour Levels", skin));

		// cost is 0 for now
		button("Unlock Hero Factory \n R: 0", 21); //$NON-NLS-1$
		button("Level 1 Armour", 22); //$NON-NLS-1$
		button("Level 2 Armour", 23); //$NON-NLS-1$
		button("Level 3 Armour", 24); //$NON-NLS-1$

		getButtonTable().row();

		//Weapon Item Level Upgrades
		getButtonTable().add(new Label("Weapon Levels", skin));

		// cost is 0 for now

		button("Unlock Special Items \n R: 0", 21); //$NON-NLS-1$
		button("Level 1 Weapons", 22); //$NON-NLS-1$
		button("Level 2 Weapons", 23); //$NON-NLS-1$
		button("Level 3 Weapons", 24); //$NON-NLS-1$


		timeManager.pause();
	}

	/**
	 * Checks the result of the Dialogue box and then if a Upgrade was selected
	 * executes code to the requirements of the specified Technology and then activates it if it can be done, otherwise
	 * displays a message indicating why not
	 * @param object
	 */
	@Override
	protected void result(final Object object){
		timeManager.unPause();
		this.hud.setTechCheck(0);
		int techID = (int) object;
		switch((int)object) {
			case 1:

				break;
			case 2:
				// do something
				break;
			case 3:
				// do something
				break;
			case 4:
				// do something
				break;
			case 5:
				// do something
				break;
			case 6:
				// do something
				break;
			case 7:
				// do something
				break;
			case 8:
				// do something
				break;
			case 9:
				// do something
				break;
			case 10:
				// do something
				break;
			case 11:
				// do something
				break;
			case 12:
				// do something
				break;
			case 13:
				// do something
				break;
			case 14:
				// do something
				break;
			case 15:
				// do something
				break;
			case 16:
				// do something
				break;
			case 17:
				// do something
				break;
			case 18:
				// do something
				break;
			case 19:
				// do something
				break;
			case 20:
				// do something
				break;
			case 21:
				this.techMan.checkPrereqs(techMan, this.techMan.getTech(21),
						21, 0);
				this.techMan.unlockHeroFactory();
				break;
			case 22:
				this.techMan.unlockArmourLevelOne();
				break;
			case 23:
				this.techMan.unlockArmourLevelTwo();
				break;
			case 24:
				this.techMan.unlockArmourLevelThree();
				break;
			case 25:
				this.techMan.unlockSpecial();
				break;
			case 26:
				this.techMan.unlockWeaponLevelOne();
				break;
			case 27:
				this.techMan.unlockWeaponLevelTwo();
				break;
			case 28:
				this.techMan.unlockWeaponLevelThree();
				break;
		}






		if (techID == 0) {return;}
		Technology tech = this.techMan.getTech(techID);
		String message = this.techMan.checkPrereqs(this.techMan, tech, techID, -1);
		//Need to find a way to print this to the dialogue box
		System.out.println(message);
	}
}
