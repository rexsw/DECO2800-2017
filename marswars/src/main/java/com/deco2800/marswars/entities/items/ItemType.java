package com.deco2800.marswars.entities.items;

import com.badlogic.gdx.graphics.Texture;
import com.deco2800.marswars.managers.TextureManager;

/**
 * Interface for Item type enumerate classes which specify the meta data for specific items (regardless of item types).
 * These enumerate classes that will implement this interface will store data such as the item's name, the item's
 * texture string
 * @author Mason
 *
 */
public interface ItemType {
	String getName();
	String getTextureString();
	int[] getCost();
	String getDescription();
	String getCostString();
}
