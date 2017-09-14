package com.deco2800.marswars.managers;

import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.worlds.CustomizedWorld;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.Clickable;
import com.deco2800.marswars.entities.HasOwner;
import com.deco2800.marswars.worlds.AbstractWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Really crappy mouse handler for the game
 * Created by timhadwen on 23/7/17.
 */
public class MouseHandler extends Manager {

	private static final Logger LOGGER = LoggerFactory.getLogger(MouseHandler.class);

	private List<Clickable> listeners = new ArrayList<>();

	/**
	 * Currently only handles objects on height 0
	 * @param x
	 * @param y
	 */
	public void handleMouseClick(float x, float y, int button) {
		float tileWidth = (float) GameManager.get().getWorld().getMap().getProperties().get("tilewidth", Integer.class);
		float tileHeight = (float) GameManager.get().getWorld().getMap().getProperties().get("tileheight", Integer.class);

		float projX;
		float projY;

		if (button == 0) {
			// Left click
			AbstractWorld world = GameManager.get().getWorld();

			// If we get another left click ignore the previous listeners
//				listeners.clear(); // Remove this to allow multiselect

			projX = x/tileWidth;
			projY = -(y - tileHeight / 2f) / tileHeight + projX;
			projX -= projY - projX;

			if (projX < 0 || projX > world.getWidth() || projY < 0 || projY > world.getLength()) {
				return;
			}

			LOGGER.info(String.format("Clicked on tile x:%f y:%f", projX,projY));

			List<BaseEntity> entities = GameManager.get().getWorld().getEntities((int)projX, (int)projY);
			


			if (entities.isEmpty()) {
				LOGGER.info(String.format("No selectable enities found at x:%f y:%f", projX,projY));
				for (Clickable c : listeners) {
					if (c instanceof Soldier) ((Soldier)c).resetTexture();
				}
				listeners.clear();//Deselect all the entities selected before
				return;
			}

/*			for (BaseEntity e: entities) {
				if (e instanceof Clickable) {
					LOGGER.info(String.format("Clicked on %s", entities.get(entities.size() - 1).toString()));
					((Clickable) entities.get(entities.size() - 1)).onClick(this);
					break;
				}
			}
			((CustomizedWorld)world).deSelectAll();*/
			
			
			List<BaseEntity> staticEntities = new ArrayList<BaseEntity>(entities);
			boolean isClickable=false;
			BaseEntity chosen = null;
			for (BaseEntity e: staticEntities) {
				if (e instanceof Clickable) {
					if (e instanceof HasOwner) {
						//giving preference to Player's own entities.
						if (! ((HasOwner) e).isAi() ) {
							chosen = e;
							isClickable = true;
							break;
						}
					}
					if (chosen == null) {
						//taking the first clickable entity found that is not the Player's. May not have an owner.
						chosen = e;
						isClickable = true;
					}
				}
			}
			if (chosen != null) {
				LOGGER.info(String.format("Clicked on %s", chosen).toString());
				((Clickable) chosen).onClick(this);
			}
			
			if(isClickable){
				((CustomizedWorld)world).deSelectAll();
			}
			
			/*if (entities.get(entities.size() - 1) instanceof Clickable) {
				LOGGER.info(String.format("Clicked on %s", entities.get(entities.size() - 1).toString()));
				((Clickable) entities.get(entities.size() - 1)).onClick(this);
			} else {
				
				((CustomizedWorld)world).deSelectAll();*/
			//}
		} else if (button == 1) {
			// Right click
			projX = x/tileWidth;
			projY = -(y - tileHeight / 2f) / tileHeight + projX;
			projX -= projY - projX;

			for (Clickable c : listeners) {
				c.onRightClick(projX, projY);
			}
			listeners.clear();
		}
	}

	public void registerForRightClickNotification(Clickable thing) {
		listeners.add(thing);
	}
}