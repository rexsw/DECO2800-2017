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
			button("Upgrade Spacman Cost", 1);
			button("Upgrade Attack", 2);
			button("Upgrade Defense", 3);
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
