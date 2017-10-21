package com.deco2800.marswars.hud;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.deco2800.marswars.entities.items.*;
import com.deco2800.marswars.managers.*;

import com.deco2800.marswars.technology.*;
import org.lwjgl.Sys;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;


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

		button(String.format("Extra Padding \n %d R, %d C, %d B", this.techMan.getTech(1).getCost()[0], this.techMan.getTech(1).getCost()[1], this.techMan.getTech(1).getCost()[2]), 1);
		button(String.format("Hard Shell \n %d R, %d C, %d B", this.techMan.getTech(2).getCost()[0], this.techMan.getTech(2).getCost()[1], this.techMan.getTech(2).getCost()[2]), 2);
		button(String.format("Spiky Armour \n %d R, %d C, %d B", this.techMan.getTech(3).getCost()[0], this.techMan.getTech(3).getCost()[1], this.techMan.getTech(3).getCost()[2]), 3);
		button(String.format("Cloister Mode \n %d R, %d C, %d B", this.techMan.getTech(4).getCost()[0], this.techMan.getTech(4).getCost()[1], this.techMan.getTech(4).getCost()[2]), 4);

		getButtonTable().row();

		//Attack Damage Upgrades
		getButtonTable().add(new Label("Attack Damage Tech", skin));

		button(String.format("Anger \n %d R, %d C, %d B", this.techMan.getTech(5).getCost()[0], this.techMan.getTech(5).getCost()[1], this.techMan.getTech(5).getCost()[2]), 5);
		button(String.format("Rage \n %d R, %d C, %d B", this.techMan.getTech(6).getCost()[0], this.techMan.getTech(6).getCost()[1], this.techMan.getTech(6).getCost()[2]), 6);
		button(String.format("Sleight of Hand \n %d R, %d C, %d B", this.techMan.getTech(7).getCost()[0], this.techMan.getTech(7).getCost()[1], this.techMan.getTech(7).getCost()[2]), 7);
		button(String.format("Sleight of Hand \n %d R, %d C, %d B", this.techMan.getTech(8).getCost()[0], this.techMan.getTech(8).getCost()[1], this.techMan.getTech(8).getCost()[2]), 8);

		getButtonTable().row();

		//Attack Speed Upgrades

		getButtonTable().add(new Label("Attack Speed Tech", skin));

		button(String.format("Sleight of Hand \n %d R, %d C, %d B", this.techMan.getTech(9).getCost()[0], this.techMan.getTech(9).getCost()[1], this.techMan.getTech(9).getCost()[2]), 9);
		button(String.format("Unnatural Dexterity \n %d R, %d C, %d B", this.techMan.getTech(10).getCost()[0], this.techMan.getTech(10).getCost()[1], this.techMan.getTech(10).getCost()[2]), 10);
		button(String.format("120 WPM \n %d R, %d C, %d B", this.techMan.getTech(11).getCost()[0], this.techMan.getTech(11).getCost()[1], this.techMan.getTech(11).getCost()[2]), 11);
		button(String.format("Korean Starcraft Pro \n %d R, %d C, %d B", this.techMan.getTech(12).getCost()[0], this.techMan.getTech(12).getCost()[1], this.techMan.getTech(12).getCost()[2]), 12);

		getButtonTable().row();

		//Health Upgrades

		getButtonTable().add(new Label("Health Tech", skin));

		button(String.format("Swole \n %d R, %d C, %d B", this.techMan.getTech(13).getCost()[0], this.techMan.getTech(13).getCost()[1], this.techMan.getTech(13).getCost()[2]), 13);
		button(String.format("Jacked \n %d R, %d C, %d B", this.techMan.getTech(14).getCost()[0], this.techMan.getTech(14).getCost()[1], this.techMan.getTech(14).getCost()[2]), 14);
		button(String.format("Ripped \n %d R, %d C, %d B", this.techMan.getTech(15).getCost()[0], this.techMan.getTech(15).getCost()[1], this.techMan.getTech(15).getCost()[2]), 15);
		button(String.format("Beast Mode \n %d R, %d C, %d B", this.techMan.getTech(16).getCost()[0], this.techMan.getTech(16).getCost()[1], this.techMan.getTech(16).getCost()[2]), 16);

		getButtonTable().row();

		//Special techs

		getButtonTable().add(new Label("Special Technology", skin));

		button(String.format("Nootropics \n %d R, %d C, %d B", this.techMan.getTech(17).getCost()[0], this.techMan.getTech(17).getCost()[1], this.techMan.getTech(17).getCost()[2]), 17);
		button(String.format("Steroids \n %d R, %d C, %d B", this.techMan.getTech(18).getCost()[0], this.techMan.getTech(18).getCost()[1], this.techMan.getTech(18).getCost()[2]), 18);
		button(String.format("Unlock cow level \n %d R, %d C, %d B", this.techMan.getTech(19).getCost()[0], this.techMan.getTech(19).getCost()[1], this.techMan.getTech(19).getCost()[2]), 19);
		button(String.format("Vampirism \n %d R, %d C, %d B", this.techMan.getTech(20).getCost()[0], this.techMan.getTech(20).getCost()[1], this.techMan.getTech(20).getCost()[2]), 20);

		getButtonTable().row();

		//Weapon Item Level Upgrades
		getButtonTable().add(new Label("Armour Levels", skin));

		button(String.format("Unlock Hero Factory \n %d R, %d C, %d B", this.techMan.getTech(21).getCost()[0], this.techMan.getTech(21).getCost()[1], this.techMan.getTech(21).getCost()[2]), 21);
		button(String.format("Level 1 Weapons \n %d R, %d C, %d B", this.techMan.getTech(22).getCost()[0], this.techMan.getTech(22).getCost()[1], this.techMan.getTech(22).getCost()[2]), 22);
		button(String.format("Level 2 Weapons \n %d R, %d C, %d B", this.techMan.getTech(23).getCost()[0], this.techMan.getTech(23).getCost()[1], this.techMan.getTech(23).getCost()[2]), 23);
		button(String.format("Level 3  Weapons \n %d R, %d C, %d B", this.techMan.getTech(24).getCost()[0], this.techMan.getTech(24).getCost()[1], this.techMan.getTech(24).getCost()[2]), 24);

		getButtonTable().row();

		//Armour Item Level Upgrades
		getButtonTable().add(new Label("Weapon Levels", skin));

		button(String.format("Unlock Special Items \n %d R, %d C, %d B", this.techMan.getTech(25).getCost()[0], this.techMan.getTech(25).getCost()[1],this.techMan.getTech(25).getCost()[2]), 25);
		button(String.format("Level 1 Armour \n %d R, %d C, %d B", this.techMan.getTech(26).getCost()[0], this.techMan.getTech(26).getCost()[1], this.techMan.getTech(26).getCost()[2]), 26);
		button(String.format("Level 2 Armour \n %d R, %d C, %d B", this.techMan.getTech(27).getCost()[0], this.techMan.getTech(27).getCost()[1], this.techMan.getTech(27).getCost()[2]), 27);
		button(String.format("Level 3  Armour \n %d R, %d C, %d B", this.techMan.getTech(28).getCost()[0], this.techMan.getTech(28).getCost()[1], this.techMan.getTech(28).getCost()[2]), 28);


		timeManager.pause();

		//exit button
//		getButtonTable().row();
//		button("EXIT TECHNOLOGY TREE", 29);
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

		button(String.format("Extra Padding \n %d R, %d C, %d B", this.techMan.getTech(1).getCost()[0], this.techMan.getTech(1).getCost()[1], this.techMan.getTech(1).getCost()[2]), 1);
		button(String.format("Hard Shell \n %d R, %d C, %d B", this.techMan.getTech(2).getCost()[0], this.techMan.getTech(2).getCost()[1], this.techMan.getTech(2).getCost()[2]), 2);
		button(String.format("Spiky Armour \n %d R, %d C, %d B", this.techMan.getTech(3).getCost()[0], this.techMan.getTech(3).getCost()[1], this.techMan.getTech(3).getCost()[2]), 3);
		button(String.format("Cloister Mode \n %d R, %d C, %d B", this.techMan.getTech(4).getCost()[0], this.techMan.getTech(4).getCost()[1], this.techMan.getTech(4).getCost()[2]), 4);

		getButtonTable().row();

		//Attack Damage Upgrades
		getButtonTable().add(new Label("Attack Damage Tech", skin));

		button(String.format("Anger \n %d R, %d C, %d B", this.techMan.getTech(5).getCost()[0], this.techMan.getTech(5).getCost()[1], this.techMan.getTech(5).getCost()[2]), 5);
		button(String.format("Rage \n %d R, %d C, %d B", this.techMan.getTech(6).getCost()[0], this.techMan.getTech(6).getCost()[1], this.techMan.getTech(6).getCost()[2]), 6);
		button(String.format("Sleight of Hand \n %d R, %d C, %d B", this.techMan.getTech(7).getCost()[0], this.techMan.getTech(7).getCost()[1], this.techMan.getTech(7).getCost()[2]), 7);
		button(String.format("Sleight of Hand \n %d R, %d C, %d B", this.techMan.getTech(8).getCost()[0], this.techMan.getTech(8).getCost()[1], this.techMan.getTech(8).getCost()[2]), 8);

		getButtonTable().row();

		//Attack Speed Upgrades

		getButtonTable().add(new Label("Attack Speed Tech", skin));

		button(String.format("Sleight of Hand \n %d R, %d C, %d B", this.techMan.getTech(9).getCost()[0], this.techMan.getTech(9).getCost()[1], this.techMan.getTech(9).getCost()[2]), 9);
		button(String.format("Unnatural Dexterity \n %d R, %d C, %d B", this.techMan.getTech(10).getCost()[0], this.techMan.getTech(10).getCost()[1], this.techMan.getTech(10).getCost()[2]), 10);
		button(String.format("120 WPM \n %d R, %d C, %d B", this.techMan.getTech(11).getCost()[0], this.techMan.getTech(11).getCost()[1], this.techMan.getTech(11).getCost()[2]), 11);
		button(String.format("Korean Starcraft Pro \n %d R, %d C, %d B", this.techMan.getTech(12).getCost()[0], this.techMan.getTech(12).getCost()[1], this.techMan.getTech(12).getCost()[2]), 12);

		getButtonTable().row();

		//Health Upgrades

		getButtonTable().add(new Label("Health Tech", skin));

		button(String.format("Swole \n %d R, %d C, %d B", this.techMan.getTech(13).getCost()[0], this.techMan.getTech(13).getCost()[1], this.techMan.getTech(13).getCost()[2]), 13);
		button(String.format("Jacked \n %d R, %d C, %d B", this.techMan.getTech(14).getCost()[0], this.techMan.getTech(14).getCost()[1], this.techMan.getTech(14).getCost()[2]), 14);
		button(String.format("Ripped \n %d R, %d C, %d B", this.techMan.getTech(15).getCost()[0], this.techMan.getTech(15).getCost()[1], this.techMan.getTech(15).getCost()[2]), 15);
		button(String.format("Beast Mode \n %d R, %d C, %d B", this.techMan.getTech(16).getCost()[0], this.techMan.getTech(16).getCost()[1], this.techMan.getTech(16).getCost()[2]), 16);

		getButtonTable().row();

		//Special techs

		getButtonTable().add(new Label("Special Technology", skin));

		button(String.format("Nootropics \n %d R, %d C, %d B", this.techMan.getTech(17).getCost()[0], this.techMan.getTech(17).getCost()[1], this.techMan.getTech(17).getCost()[2]), 17);
		button(String.format("Steroids \n %d R, %d C, %d B", this.techMan.getTech(18).getCost()[0], this.techMan.getTech(18).getCost()[1], this.techMan.getTech(18).getCost()[2]), 18);
		button(String.format("Unlock cow level \n %d R, %d C, %d B", this.techMan.getTech(19).getCost()[0], this.techMan.getTech(19).getCost()[1], this.techMan.getTech(19).getCost()[2]), 19);
		button(String.format("Vampirism \n %d R, %d C, %d B", this.techMan.getTech(20).getCost()[0], this.techMan.getTech(20).getCost()[1], this.techMan.getTech(20).getCost()[2]), 20);

		getButtonTable().row();

		//Weapon Item Level Upgrades
		getButtonTable().add(new Label("Armour Levels", skin));

		button(String.format("Unlock Hero Factory \n %d R, %d C, %d B", this.techMan.getTech(21).getCost()[0], this.techMan.getTech(21).getCost()[1], this.techMan.getTech(21).getCost()[2]), 21);
		button(String.format("Level 1 Weapons \n %d R, %d C, %d B", this.techMan.getTech(22).getCost()[0], this.techMan.getTech(22).getCost()[1], this.techMan.getTech(22).getCost()[2]), 22);
		button(String.format("Level 2 Weapons \n %d R, %d C, %d B", this.techMan.getTech(23).getCost()[0], this.techMan.getTech(23).getCost()[1], this.techMan.getTech(23).getCost()[2]), 23);
		button(String.format("Level 3  Weapons \n %d R, %d C, %d B", this.techMan.getTech(24).getCost()[0], this.techMan.getTech(24).getCost()[1], this.techMan.getTech(24).getCost()[2]), 24);

		getButtonTable().row();

		//Armour Item Level Upgrades
		getButtonTable().add(new Label("Weapon Levels", skin));

		button(String.format("Unlock Special Items \n %d R, %d C, %d B", this.techMan.getTech(25).getCost()[0], this.techMan.getTech(25).getCost()[1],this.techMan.getTech(25).getCost()[2]), 25);
		button(String.format("Level 1 Armour \n %d R, %d C, %d B", this.techMan.getTech(26).getCost()[0], this.techMan.getTech(26).getCost()[1], this.techMan.getTech(26).getCost()[2]), 26);
		button(String.format("Level 2 Armour \n %d R, %d C, %d B", this.techMan.getTech(27).getCost()[0], this.techMan.getTech(27).getCost()[1], this.techMan.getTech(27).getCost()[2]), 27);
		button(String.format("Level 3  Armour \n %d R, %d C, %d B", this.techMan.getTech(28).getCost()[0], this.techMan.getTech(28).getCost()[1], this.techMan.getTech(28).getCost()[2]), 28);

		//exit button
//		getButtonTable().row();
//		button("EXIT TECHNOLOGY TREE", 29);

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
		timeManager.pause();
		this.hud.setTechCheck(1);
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					techtree.hide();
					this.hud.updateShop();
					timeManager.unPause();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.techMan.addActiveTech(this.techMan.getTech(27));
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
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
					this.hud.setTechCheck(0);
					timeManager.unPause();
					this.hud.updateShop();
					techtree.hide();
				}
				break;
		}
	}
}
