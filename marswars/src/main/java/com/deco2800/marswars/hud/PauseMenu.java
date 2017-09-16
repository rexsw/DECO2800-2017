package com.deco2800.marswars.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class PauseMenu extends Dialog{
	public PauseMenu(String title, Skin skin) {
		super(title, skin);
		
		{
			text("Game Paused");  //$NON-NLS-1$
			
			button("Resume", 0); //$NON-NLS-1$
			button("Game Statistics", 1);
			button("Settings", 2); //$NON-NLS-1$
			button("Quit to Main Menu", 3); //$NON-NLS-1$
			button("Exit Game", 4); //$NON-NLS-1$
		}	
	}
	
		protected void result(final Object object) {
			if (object == (Object) 1) {
				
			} else if (object == (Object) 2) {
				
			} else if (object == (Object) 3) {
				
			} else if (object == (Object) 4) {
				System.exit(0);
			}
		}
}