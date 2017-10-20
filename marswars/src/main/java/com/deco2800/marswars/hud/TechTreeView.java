package com.deco2800.marswars.hud;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.deco2800.marswars.entities.items.*;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.TechnologyManager;
import com.deco2800.marswars.managers.TimeManager;

import com.deco2800.marswars.technology.*;


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
	private String sleightOfHandString = "Sleight of Hand \n %d R, %d C, %d B";
	
	public TechTreeView(String title, Skin skin, HUDView hud) {
		super(title, skin);
		this.hud = hud;

		this.getContentTable().debugCell();
		this.getButtonTable().debugCell().center();
		getButtonTable().defaults().height(60).width(200).padTop(10).padBottom(10);


		getButtonText(skin);

		timeManager.pause();

		//exit button
		getButtonTable().row();
		button("EXIT TECHNOLOGY TREE", 29);
	}

	// second constructor with additional message parameter
	public TechTreeView(String title, Skin skin, HUDView hud, String message) {
		super(title, skin);
		this.hud = hud;

		this.getContentTable().debugCell();
		this.getButtonTable().debugCell().center();
		getButtonTable().defaults().height(60).width(200).padTop(10).padBottom(10);

		getButtonText(skin);
		
		//exit button
		getButtonTable().row();
		button("EXIT TECHNOLOGY TREE", 29);

		text(message);
		timeManager.pause();
	}

	private void getButtonText(Skin skin) {
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
		button(String.format(sleightOfHandString, this.techMan.getTech(7).getCost()[0], this.techMan.getTech(7).getCost()[1], this.techMan.getTech(7).getCost()[2]), 7);
		button(String.format(sleightOfHandString, this.techMan.getTech(8).getCost()[0], this.techMan.getTech(8).getCost()[1], this.techMan.getTech(8).getCost()[2]), 8);

		getButtonTable().row();

		//Attack Speed Upgrades

		getButtonTable().add(new Label("Attack Speed Tech", skin));

		button(String.format(sleightOfHandString, this.techMan.getTech(9).getCost()[0], this.techMan.getTech(9).getCost()[1], this.techMan.getTech(9).getCost()[2]), 9);
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
		final String techTree = "TechTree";
		final String activateTech = "Activating Technology!";
		if (techID < 29) {
			int x = (int) object;
			message = this.techMan.checkPrereqs(techMan, this.techMan.getTech(x), x, 1);
			techtree = new TechTreeView(techTree, this.getSkin(), this.hud,message).show(this.getStage());
			if(message == activateTech) {
				this.techMan.activateTech(techMan, this.techMan.getTech(1), resourceManager, x, 1);
				this.techMan.addActiveTech(this.techMan.getTech(x));
			}
		} else {
			this.hud.setTechCheck(1);
			techtree.hide();
			this.timeManager.unPause();
		}

		if (techID == 0) {
			return;
		}
		Technology tech = this.techMan.getTech(techID);
		String message = this.techMan.checkPrereqs(this.techMan, tech, techID, -1);
		//Need to find a way to print this to the dialogue box
		System.out.println(message);
	}
}
