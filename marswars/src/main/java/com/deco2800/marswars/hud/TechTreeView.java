package com.deco2800.marswars.hud;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.deco2800.marswars.entities.items.*;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.TechnologyManager;
import com.deco2800.marswars.managers.TimeManager;

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

		button(String.format("Extra Padding \n %d R, %d C, %d W, %d B", this.techMan.getTech(1).getCost()[0], this.techMan.getTech(1).getCost()[1], this.techMan.getTech(1).getCost()[2], this.techMan.getTech(1).getCost()[3]), 1);
		button(String.format("Hard Shell \n %d R, %d C, %d W, %d B", this.techMan.getTech(2).getCost()[0], this.techMan.getTech(2).getCost()[1], this.techMan.getTech(2).getCost()[2], this.techMan.getTech(2).getCost()[3]), 2);
		button(String.format("Spiky Armour \n %d R, %d C, %d W, %d B", this.techMan.getTech(3).getCost()[0], this.techMan.getTech(3).getCost()[1], this.techMan.getTech(3).getCost()[2], this.techMan.getTech(3).getCost()[3]), 3);
		button(String.format("Cloister Mode \n %d R, %d C, %d W, %d B", this.techMan.getTech(4).getCost()[0], this.techMan.getTech(4).getCost()[1], this.techMan.getTech(4).getCost()[2], this.techMan.getTech(4).getCost()[3]), 4);

		getButtonTable().row();

		//Attack Damage Upgrades
		getButtonTable().add(new Label("Attack Damage Tech", skin));

		button(String.format("Anger \n %d R, %d C, %d W, %d B", this.techMan.getTech(5).getCost()[0], this.techMan.getTech(5).getCost()[1], this.techMan.getTech(5).getCost()[2], this.techMan.getTech(5).getCost()[3]), 5);
		button(String.format("Rage \n %d R, %d C, %d W, %d B", this.techMan.getTech(6).getCost()[0], this.techMan.getTech(6).getCost()[1], this.techMan.getTech(6).getCost()[2], this.techMan.getTech(6).getCost()[3]), 6);
		button(String.format("Sleight of Hand \n %d R, %d C, %d W, %d B", this.techMan.getTech(7).getCost()[0], this.techMan.getTech(7).getCost()[1], this.techMan.getTech(7).getCost()[2], this.techMan.getTech(7).getCost()[3]), 7);
		button(String.format("Sleight of Hand \n %d R, %d C, %d W, %d B", this.techMan.getTech(8).getCost()[0], this.techMan.getTech(8).getCost()[1], this.techMan.getTech(8).getCost()[2], this.techMan.getTech(8).getCost()[3]), 8);

		getButtonTable().row();

		//Attack Speed Upgrades

		getButtonTable().add(new Label("Attack Speed Tech", skin));

		button(String.format("Sleight of Hand \n %d R, %d C, %d W, %d B", this.techMan.getTech(9).getCost()[0], this.techMan.getTech(9).getCost()[1], this.techMan.getTech(9).getCost()[2], this.techMan.getTech(9).getCost()[3]), 9);
		button(String.format("Unnatural Dexterity \n %d R, %d C, %d W, %d B", this.techMan.getTech(10).getCost()[0], this.techMan.getTech(10).getCost()[1], this.techMan.getTech(10).getCost()[2], this.techMan.getTech(10).getCost()[3]), 10);
		button(String.format("120 WPM \n %d R, %d C, %d W, %d B", this.techMan.getTech(11).getCost()[0], this.techMan.getTech(11).getCost()[1], this.techMan.getTech(11).getCost()[2], this.techMan.getTech(11).getCost()[3]), 11);
		button(String.format("Korean Starcraft Pro \n %d R, %d C, %d W, %d B", this.techMan.getTech(12).getCost()[0], this.techMan.getTech(12).getCost()[1], this.techMan.getTech(12).getCost()[2], this.techMan.getTech(12).getCost()[3]), 12);

		getButtonTable().row();

		//Health Upgrades

		getButtonTable().add(new Label("Health Tech", skin));

		button(String.format("Swole \n %d R, %d C, %d W, %d B", this.techMan.getTech(13).getCost()[0], this.techMan.getTech(13).getCost()[1], this.techMan.getTech(13).getCost()[2], this.techMan.getTech(13).getCost()[3]), 13);
		button(String.format("Jacked \n %d R, %d C, %d W, %d B", this.techMan.getTech(14).getCost()[0], this.techMan.getTech(14).getCost()[1], this.techMan.getTech(14).getCost()[2], this.techMan.getTech(14).getCost()[3]), 14);
		button(String.format("Ripped \n %d R, %d C, %d W, %d B", this.techMan.getTech(15).getCost()[0], this.techMan.getTech(15).getCost()[1], this.techMan.getTech(15).getCost()[2], this.techMan.getTech(15).getCost()[3]), 15);
		button(String.format("Beast Mode \n %d R, %d C, %d W, %d B", this.techMan.getTech(16).getCost()[0], this.techMan.getTech(16).getCost()[1], this.techMan.getTech(16).getCost()[2], this.techMan.getTech(16).getCost()[3]), 16);

		getButtonTable().row();

		//Special techs

		getButtonTable().add(new Label("Special Technology", skin));

		button(String.format("Nootropics \n %d R, %d C, %d W, %d B", this.techMan.getTech(17).getCost()[0], this.techMan.getTech(17).getCost()[1], this.techMan.getTech(17).getCost()[2], this.techMan.getTech(17).getCost()[3]), 17);
		button(String.format("Steroids \n %d R, %d C, %d W, %d B", this.techMan.getTech(18).getCost()[0], this.techMan.getTech(18).getCost()[1], this.techMan.getTech(18).getCost()[2], this.techMan.getTech(18).getCost()[3]), 18);
		button(String.format("Unlock cow level \n %d R, %d C, %d W, %d B", this.techMan.getTech(19).getCost()[0], this.techMan.getTech(19).getCost()[1], this.techMan.getTech(19).getCost()[2], this.techMan.getTech(19).getCost()[3]), 19);
		button(String.format("Vampirism \n %d R, %d C, %d W, %d B", this.techMan.getTech(20).getCost()[0], this.techMan.getTech(20).getCost()[1], this.techMan.getTech(20).getCost()[2], this.techMan.getTech(20).getCost()[3]), 20);

		getButtonTable().row();

		//Weapon Item Level Upgrades
		getButtonTable().add(new Label("Armour Levels", skin));

		button(String.format("Unlock Hero Factory \n %d R, %d C, %d W, %d B", this.techMan.getTech(21).getCost()[0], this.techMan.getTech(21).getCost()[1], this.techMan.getTech(21).getCost()[2], this.techMan.getTech(21).getCost()[3]), 21);
		button(String.format("Level 1 Weapons \n %d R, %d C, %d W, %d B", this.techMan.getTech(22).getCost()[0], this.techMan.getTech(22).getCost()[1], this.techMan.getTech(22).getCost()[2], this.techMan.getTech(22).getCost()[3]), 22);
		button(String.format("Level 2 Weapons \n %d R, %d C, %d W, %d B", this.techMan.getTech(23).getCost()[0], this.techMan.getTech(23).getCost()[1], this.techMan.getTech(23).getCost()[2], this.techMan.getTech(23).getCost()[3]), 23);
		button(String.format("Level 3  Weapons \n %d R, %d C, %d W, %d B", this.techMan.getTech(24).getCost()[0], this.techMan.getTech(24).getCost()[1], this.techMan.getTech(24).getCost()[2], this.techMan.getTech(24).getCost()[3]), 24);

		getButtonTable().row();

		//Armour Item Level Upgrades
		getButtonTable().add(new Label("Weapon Levels", skin));

		button(String.format("Unlock Special Items \n %d R, %d C, %d W, %d B", this.techMan.getTech(25).getCost()[0], this.techMan.getTech(25).getCost()[1],this.techMan.getTech(25).getCost()[2], this.techMan.getTech(25).getCost()[3]), 25);
		button(String.format("Level 1 Armour \n %d R, %d C, %d W, %d B", this.techMan.getTech(26).getCost()[0], this.techMan.getTech(26).getCost()[1], this.techMan.getTech(26).getCost()[2], this.techMan.getTech(26).getCost()[3]), 26);
		button(String.format("Level 2 Armour \n %d R, %d C, %d W, %d B", this.techMan.getTech(27).getCost()[0], this.techMan.getTech(27).getCost()[1], this.techMan.getTech(27).getCost()[2], this.techMan.getTech(27).getCost()[3]), 27);
		button(String.format("Level 3  Armour \n %d R, %d C, %d W, %d B", this.techMan.getTech(28).getCost()[0], this.techMan.getTech(28).getCost()[1], this.techMan.getTech(28).getCost()[2], this.techMan.getTech(28).getCost()[3]), 28);


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

		button(String.format("Extra Padding \n %d R, %d C, %d W, %d B", this.techMan.getTech(1).getCost()[0], this.techMan.getTech(1).getCost()[1], this.techMan.getTech(1).getCost()[2], this.techMan.getTech(1).getCost()[3]), 1);
		button(String.format("Hard Shell \n %d R, %d C, %d W, %d B", this.techMan.getTech(2).getCost()[0], this.techMan.getTech(2).getCost()[1], this.techMan.getTech(2).getCost()[2], this.techMan.getTech(2).getCost()[3]), 2);
		button(String.format("Spiky Armour \n %d R, %d C, %d W, %d B", this.techMan.getTech(3).getCost()[0], this.techMan.getTech(3).getCost()[1], this.techMan.getTech(3).getCost()[2], this.techMan.getTech(3).getCost()[3]), 3);
		button(String.format("Cloister Mode \n %d R, %d C, %d W, %d B", this.techMan.getTech(4).getCost()[0], this.techMan.getTech(4).getCost()[1], this.techMan.getTech(4).getCost()[2], this.techMan.getTech(4).getCost()[3]), 4);

		getButtonTable().row();

		//Attack Damage Upgrades
		getButtonTable().add(new Label("Attack Damage Tech", skin));

		button(String.format("Anger \n %d R, %d C, %d W, %d B", this.techMan.getTech(5).getCost()[0], this.techMan.getTech(5).getCost()[1], this.techMan.getTech(5).getCost()[2], this.techMan.getTech(5).getCost()[3]), 5);
		button(String.format("Rage \n %d R, %d C, %d W, %d B", this.techMan.getTech(6).getCost()[0], this.techMan.getTech(6).getCost()[1], this.techMan.getTech(6).getCost()[2], this.techMan.getTech(6).getCost()[3]), 6);
		button(String.format("Sleight of Hand \n %d R, %d C, %d W, %d B", this.techMan.getTech(7).getCost()[0], this.techMan.getTech(7).getCost()[1], this.techMan.getTech(7).getCost()[2], this.techMan.getTech(7).getCost()[3]), 7);
		button(String.format("Sleight of Hand \n %d R, %d C, %d W, %d B", this.techMan.getTech(8).getCost()[0], this.techMan.getTech(8).getCost()[1], this.techMan.getTech(8).getCost()[2], this.techMan.getTech(8).getCost()[3]), 8);

		getButtonTable().row();

		//Attack Speed Upgrades

		getButtonTable().add(new Label("Attack Speed Tech", skin));

		button(String.format("Sleight of Hand \n %d R, %d C, %d W, %d B", this.techMan.getTech(9).getCost()[0], this.techMan.getTech(9).getCost()[1], this.techMan.getTech(9).getCost()[2], this.techMan.getTech(9).getCost()[3]), 9);
		button(String.format("Unnatural Dexterity \n %d R, %d C, %d W, %d B", this.techMan.getTech(10).getCost()[0], this.techMan.getTech(10).getCost()[1], this.techMan.getTech(10).getCost()[2], this.techMan.getTech(10).getCost()[3]), 10);
		button(String.format("120 WPM \n %d R, %d C, %d W, %d B", this.techMan.getTech(11).getCost()[0], this.techMan.getTech(11).getCost()[1], this.techMan.getTech(11).getCost()[2], this.techMan.getTech(11).getCost()[3]), 11);
		button(String.format("Korean Starcraft Pro \n %d R, %d C, %d W, %d B", this.techMan.getTech(12).getCost()[0], this.techMan.getTech(12).getCost()[1], this.techMan.getTech(12).getCost()[2], this.techMan.getTech(12).getCost()[3]), 12);

		getButtonTable().row();

		//Health Upgrades

		getButtonTable().add(new Label("Health Tech", skin));

		button(String.format("Swole \n %d R, %d C, %d W, %d B", this.techMan.getTech(13).getCost()[0], this.techMan.getTech(13).getCost()[1], this.techMan.getTech(13).getCost()[2], this.techMan.getTech(13).getCost()[3]), 13);
		button(String.format("Jacked \n %d R, %d C, %d W, %d B", this.techMan.getTech(14).getCost()[0], this.techMan.getTech(14).getCost()[1], this.techMan.getTech(14).getCost()[2], this.techMan.getTech(14).getCost()[3]), 14);
		button(String.format("Ripped \n %d R, %d C, %d W, %d B", this.techMan.getTech(15).getCost()[0], this.techMan.getTech(15).getCost()[1], this.techMan.getTech(15).getCost()[2], this.techMan.getTech(15).getCost()[3]), 15);
		button(String.format("Beast Mode \n %d R, %d C, %d W, %d B", this.techMan.getTech(16).getCost()[0], this.techMan.getTech(16).getCost()[1], this.techMan.getTech(16).getCost()[2], this.techMan.getTech(16).getCost()[3]), 16);

		getButtonTable().row();

		//Special techs

		getButtonTable().add(new Label("Special Technology", skin));

		button(String.format("Nootropics \n %d R, %d C, %d W, %d B", this.techMan.getTech(17).getCost()[0], this.techMan.getTech(17).getCost()[1], this.techMan.getTech(17).getCost()[2], this.techMan.getTech(17).getCost()[3]), 17);
		button(String.format("Steroids \n %d R, %d C, %d W, %d B", this.techMan.getTech(18).getCost()[0], this.techMan.getTech(18).getCost()[1], this.techMan.getTech(18).getCost()[2], this.techMan.getTech(18).getCost()[3]), 18);
		button(String.format("Unlock cow level \n %d R, %d C, %d W, %d B", this.techMan.getTech(19).getCost()[0], this.techMan.getTech(19).getCost()[1], this.techMan.getTech(19).getCost()[2], this.techMan.getTech(19).getCost()[3]), 19);
		button(String.format("Vampirism \n %d R, %d C, %d W, %d B", this.techMan.getTech(20).getCost()[0], this.techMan.getTech(20).getCost()[1], this.techMan.getTech(20).getCost()[2], this.techMan.getTech(20).getCost()[3]), 20);

		getButtonTable().row();

		//Weapon Item Level Upgrades
		getButtonTable().add(new Label("Armour Levels", skin));

		button(String.format("Unlock Hero Factory \n %d R, %d C, %d W, %d B", this.techMan.getTech(21).getCost()[0], this.techMan.getTech(21).getCost()[1], this.techMan.getTech(21).getCost()[2], this.techMan.getTech(21).getCost()[3]), 21);
		button(String.format("Level 1 Weapons \n %d R, %d C, %d W, %d B", this.techMan.getTech(22).getCost()[0], this.techMan.getTech(22).getCost()[1], this.techMan.getTech(22).getCost()[2], this.techMan.getTech(22).getCost()[3]), 22);
		button(String.format("Level 2 Weapons \n %d R, %d C, %d W, %d B", this.techMan.getTech(23).getCost()[0], this.techMan.getTech(23).getCost()[1], this.techMan.getTech(23).getCost()[2], this.techMan.getTech(23).getCost()[3]), 23);
		button(String.format("Level 3  Weapons \n %d R, %d C, %d W, %d B", this.techMan.getTech(24).getCost()[0], this.techMan.getTech(24).getCost()[1], this.techMan.getTech(24).getCost()[2], this.techMan.getTech(24).getCost()[3]), 24);

		getButtonTable().row();

		//Armour Item Level Upgrades
		getButtonTable().add(new Label("Weapon Levels", skin));

		button(String.format("Unlock Special Items \n %d R, %d C, %d W, %d B", this.techMan.getTech(25).getCost()[0], this.techMan.getTech(25).getCost()[1],this.techMan.getTech(25).getCost()[2], this.techMan.getTech(25).getCost()[3]), 25);
		button(String.format("Level 1 Armour \n %d R, %d C, %d W, %d B", this.techMan.getTech(26).getCost()[0], this.techMan.getTech(26).getCost()[1], this.techMan.getTech(26).getCost()[2], this.techMan.getTech(26).getCost()[3]), 26);
		button(String.format("Level 2 Armour \n %d R, %d C, %d W, %d B", this.techMan.getTech(27).getCost()[0], this.techMan.getTech(27).getCost()[1], this.techMan.getTech(27).getCost()[2], this.techMan.getTech(27).getCost()[3]), 27);
		button(String.format("Level 3  Armour \n %d R, %d C, %d W, %d B", this.techMan.getTech(28).getCost()[0], this.techMan.getTech(28).getCost()[1], this.techMan.getTech(28).getCost()[2], this.techMan.getTech(28).getCost()[3]), 28);



//		getButtonTable().row();
//		button("EXIT TECHNOLOGY TREE", 29);

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
