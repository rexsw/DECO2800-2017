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
import com.deco2800.marswars.managers.ResourceManager;
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

	ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
	TechnologyManager techMan = (TechnologyManager) GameManager.get().getManager(TechnologyManager.class);
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	HUDView hud;
	String message;
	Dialog techtree;

	public TechTreeView(String title, Skin skin, HUDView hud) {
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

	// second constructor with additional message parameter
	public TechTreeView(String title, Skin skin, HUDView hud, String message) {
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

		button("Unlock Special Items \n R: 0", 25); //$NON-NLS-1$
		button("Level 1 Weapons", 26); //$NON-NLS-1$
		button("Level 2 Weapons", 27); //$NON-NLS-1$
		button("Level 3 Weapons", 28); //$NON-NLS-1$



//		getButtonTable().row();
//		button("EXIT TECHNOLOGY TREE", 29); //$NON-NLS-1$

		//exit button


		text(message);
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
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(1),
						1, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(1), resourceManager, 1, 1);
					this.techMan.addActiveTech(this.techMan.getTech(1));
				}
				break;
			case 2:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(2),
						2, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(1), resourceManager, 2, 1);
					this.techMan.addActiveTech(this.techMan.getTech(2));
				}
				break;
			case 3:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(3),
						3, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(3), resourceManager, 3, 1);
					this.techMan.addActiveTech(this.techMan.getTech(1));
				}
				break;
			case 4:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(4),
						4, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(4), resourceManager, 4, 1);
					this.techMan.addActiveTech(this.techMan.getTech(4));
				}
				break;
			case 5:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(5),
						5, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(5), resourceManager, 5, 1);
					this.techMan.addActiveTech(this.techMan.getTech(5));
				}
				break;
			case 6:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(6),
						6, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(6), resourceManager, 6, 1);
					this.techMan.addActiveTech(this.techMan.getTech(6));
				}
				break;
			case 7:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(7),
						7, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(7), resourceManager, 7, 1);
					this.techMan.addActiveTech(this.techMan.getTech(7));
				}
				break;
			case 8:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(8),
						8, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(8), resourceManager, 8, 1);
					this.techMan.addActiveTech(this.techMan.getTech(8));
				}
				break;
			case 9:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(9),
						9, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(9), resourceManager, 9, 1);
					this.techMan.addActiveTech(this.techMan.getTech(9));
				}
				break;
			case 10:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(10),
						10, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(10), resourceManager, 10, 1);
					this.techMan.addActiveTech(this.techMan.getTech(10));
				}
				break;
			case 11:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(11),
						11, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(11), resourceManager, 11, 1);
					this.techMan.addActiveTech(this.techMan.getTech(11));
				}
				break;
			case 12:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(12),
						12, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(12), resourceManager, 12, 1);
					this.techMan.addActiveTech(this.techMan.getTech(12));
				}
				break;
			case 13:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(13),
						13, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(13), resourceManager, 13, 1);
					this.techMan.addActiveTech(this.techMan.getTech(13));
				}
				break;
			case 14:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(14),
						14, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(14), resourceManager, 14, 1);
					this.techMan.addActiveTech(this.techMan.getTech(14));
				}
				break;
			case 15:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(15),
						15, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(15), resourceManager, 15, 1);
					this.techMan.addActiveTech(this.techMan.getTech(15));
				}
				break;
			case 16:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(16),
						16, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(16), resourceManager, 16, 1);
					this.techMan.addActiveTech(this.techMan.getTech(16));
				}
				break;
			case 17:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(17),
						17, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(17), resourceManager, 17, 1);
					this.techMan.addActiveTech(this.techMan.getTech(17));
				}
				break;
			case 18:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(18),
						18, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(18), resourceManager, 18, 1);
					this.techMan.addActiveTech(this.techMan.getTech(18));
				}
				break;
			case 19:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(19),
						19, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(19), resourceManager, 19, 1);
					this.techMan.addActiveTech(this.techMan.getTech(19));
				}
				break;
			case 20:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(20),
						20, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(20), resourceManager, 20, 1);
					this.techMan.addActiveTech(this.techMan.getTech(20));
				}
				break;
			case 21:
				message = this.techMan.checkPrereqs(techMan, this.techMan
							.getTech
								(21),
						21, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
						.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(21), resourceManager, 21, 1);
					this.techMan.addActiveTech(this.techMan.getTech(21));
				}
				break;
			case 22:
				 message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(22),
						22, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(22), resourceManager, 22, 1);
					this.techMan.addActiveTech(this.techMan.getTech(22));
				}
				break;
			case 23:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(23),
						23, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(23), resourceManager, 23, 1);
					this.techMan.addActiveTech(this.techMan.getTech(23));
				}
				break;
			case 24:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(24),
						24, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(24), resourceManager, 24, 1);
					this.techMan.addActiveTech(this.techMan.getTech(24));
				}
				break;
			case 25:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(25),
						25, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(25), resourceManager, 25, 1);
					this.techMan.addActiveTech(this.techMan.getTech(25));
				}
				break;
			case 26:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(26),
						26, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(26), resourceManager, 26, 1);
					this.techMan.addActiveTech(this.techMan.getTech(26));
				}
				break;
			case 27:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(27),
						27, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(27), resourceManager, 27, 1);
					this.techMan.addActiveTech(this.techMan.getTech(24));
				}
				break;
			case 28:
				message = this.techMan.checkPrereqs(techMan, this.techMan
								.getTech
										(28),
						28, 1);
				techtree = new TechTreeView("TechTree", this.getSkin(), this.hud,
						message).show
						(this
								.getStage());
				if(message == "Activating Technology!") {
					this.techMan.activateTech(techMan, this.techMan.getTech(28), resourceManager, 28, 1);
					this.techMan.addActiveTech(this.techMan.getTech(28));
				}
				break;

			// needs to be fixed so it closes dialog box instead of crashing game
//			case 29:
//				this.hud.setTechCheck(0);
//				techtree.hide();
//				this.timeManager.unPause();
		}


		if (techID == 0) {return;}
		Technology tech = this.techMan.getTech(techID);
		String message = this.techMan.checkPrereqs(this.techMan, tech, techID, -1);
		//Need to find a way to print this to the dialogue box
		System.out.println(message);
	}
}
