package com.deco2800.marswars.entities.items;

import com.badlogic.gdx.graphics.Texture;
import com.deco2800.marswars.managers.TextureManager;

public interface ItemType {
	String getName();
	String getTextureString();
	int[] getCost();
	String getDescription();
	String getCostString();
}
