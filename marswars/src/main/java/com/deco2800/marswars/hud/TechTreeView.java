package com.deco2800.marswars.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TechnologyManager;
import com.deco2800.marswars.managers.TimeManager;
import com.deco2800.marswars.technology.Technology;


public class TechTreeView extends Dialog{
	TechnologyManager techMan = (TechnologyManager) GameManager.get().getManager(TechnologyManager.class);
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);
	HUDView hud;

	public TechTreeView(String title, Skin skin, HUDView hud) {
		super(title, skin);
		this.hud = hud;
		{
			//text("This will display the technology tree");
			// note that object numbers must correspond with tech tree tech ids
			button("Unlock Hero Factory", 1);
			button("Unlock Armour Level 1", 2);
			button("Unlock Armour Level 2", 3);
			button("Unlock Armour Level 3", 4);

			button("OK", 0);
			
			timeManager.pause();
		}
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
		if (techID == 0) {return;}
		Technology tech = this.techMan.getTech(techID);
		String message = this.techMan.checkPrereqs(this.techMan, tech, techID, -1);
		//Need to find a way to print this to the dialogue box
		System.out.println(message);
	}
}
