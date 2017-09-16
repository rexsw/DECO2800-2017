package com.deco2800.marswars.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ExitGame extends Dialog{
	public ExitGame(String title, Skin skin) {
		super(title, skin);
		
			{
				text("Are you sure you want to quit? "); //$NON-NLS-1$
				button("Yes", 1); //$NON-NLS-1$
				button("No, keep playing", 2); //$NON-NLS-1$
			}
	}
			@Override
			protected void result(final Object object){
				if(object == (Object) 1){
					System.exit(0);
				}
			}	
}