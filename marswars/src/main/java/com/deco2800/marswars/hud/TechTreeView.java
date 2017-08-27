package com.deco2800.marswars.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.marswars.actions.GenerateAction;
import com.deco2800.marswars.entities.Spacman;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.TechnologyManager;
import com.deco2800.marswars.technology.*;

import java.util.Optional;

public class TechTreeView extends Dialog{
	TechnologyManager techMan = (TechnologyManager) GameManager.get().getManager(TechnologyManager.class);

	public TechTreeView(String title, Skin skin) {
		super(title, skin);
		{
			//text("This will display the technology tree");
			button("Upgrade Spacman Cost", 1);
			button("Upgrade Attack", 2);
			button("Upgrade Defense", 3);
			button("OK");
		}
	}

	@Override
	protected void result(final Object object){
		int techID = (int) object;
		Technology tech = techMan.getTech(techID);
		String message = techMan.checkPrereqs(techMan, tech);
		//Need to find a way to print this to the dialogue box
		System.out.println(message);
	}

}
