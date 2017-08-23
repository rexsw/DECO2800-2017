package com.deco2800.marswars.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class TechTreeView extends Dialog{

	public TechTreeView(String title, Skin skin) {
		super(title, skin);		
		{
			text("This will display the technology tree");
			button("OK");
		}
	}
	
	@Override 
	protected void result(final Object object){
		
	}
	
}


