package com.deco2800.marswars.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.TechnologyManager;
import com.deco2800.marswars.technology.*;


public class TechTreeView extends Dialog{

	public TechTreeView(String title, Skin skin) {
		super(title, skin);		
		{
			text("This will display the technology tree");
			button("Upgrade Tech", 1);
			button("OK");

		}
	}
	
	@Override 
	protected void result(final Object object){

		TechnologyManager techMan = (TechnologyManager) GameManager.get().getManager(TechnologyManager.class);
		if (object == (Object) 1) {
			Technology tech2 = techMan.getTech(2);
			ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
			if (!techMan.getActive().contains(tech2)) {
				if (resourceManager.getRocks() > tech2.getCost()[0]) {
					resourceManager.setRocks(resourceManager.getRocks() - tech2.getCost()[0]);
					techMan.addActiveTech(tech2);
					System.out.println("YOu just upgraded the tech");
					tech2.costUpgrade();
				}
			} else {
				//You already have tech 2, do something else
				System.out.println("You already have it");
			}
		}
	}
}


