package com.deco2800.marswars.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TechnologyManager;
import com.deco2800.marswars.technology.*;


public class TechTreeView extends Dialog{
	TechnologyManager techMan = (TechnologyManager) GameManager.get().getManager(TechnologyManager.class);

	public TechTreeView(String title, Skin skin) {
		super(title, skin);
		{
			//text("This will display the technology tree");
			// note that object numbers must correspond with tech tree tech ids
			button("Unlock Hero Factory", 1);
			button("Unlock Armour Level 1", 2);
			button("Unlock Armour Level 2", 3);
			button("Unlock Armour Level 3", 4);
			button("Unlock Weapons Level 1", 5);
			button("Unlock Weapons Level 2", 6);
			button("Unlock Weapons Level 3", 7);
			button("Unlock Special Items", 8);

			button("OK", 0);
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
		int techID = (int) object;
		if (techID == 0) {return;}
		Technology tech = techMan.getTech(techID);
		String message = techMan.checkPrereqs(techMan, tech, techID, -1);
		//Need to find a way to print this to the dialogue box
		//System.out.println(message);
	}
}
