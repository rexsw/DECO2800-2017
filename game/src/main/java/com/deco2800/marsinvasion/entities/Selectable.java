package com.deco2800.marsinvasion.entities;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created by timhadwen on 26/7/17.
 */
public interface Selectable {
	boolean isSelected();
	void deselect();
	Button getButton();
	void buttonWasPressed();
	Label getHelpText();
}
