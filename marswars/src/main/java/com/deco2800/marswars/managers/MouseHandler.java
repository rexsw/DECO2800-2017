package com.deco2800.marswars.managers;

import com.deco2800.marswars.actions.BuildAction;
import com.deco2800.marswars.actions.BuildWallAction;
import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.Clickable;
import com.deco2800.marswars.entities.HasOwner;
import com.deco2800.marswars.entities.units.Astronaut;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.worlds.AbstractWorld;
import com.deco2800.marswars.worlds.CustomizedWorld;
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
	
	private boolean ignoreLeftClick = false;
	
	private BaseEntity unitSelected = null;
	
	private boolean multiselect = false;

	/**
	 * Currently only handles objects on height 0
	 * @param x
	 * @param y
	 */
	public void handleMouseClick(float x, float y, int button,boolean skipChecking) {
		if (GameManager.get().getWorld() != null){
			float tileWidth = (float) GameManager.get().getWorld().getMap().getProperties().get("tilewidth", Integer.class);
			float tileHeight = (float) GameManager.get().getWorld().getMap().getProperties().get("tileheight", Integer.class);

			float projX;
			float projY;
			if (button == 0 && !ignoreLeftClick) {
				// Left click
				if (!multiselect) {
					listeners.clear();
				}
				if (unitSelected != null && unitSelected instanceof BuildingEntity) {
					unregisterForRightClickNotification((Clickable) unitSelected);
					unitSelected.deselect();
				}
				// Left click cancels building selection confirmation
				if (unitSelected !=null && unitSelected instanceof Astronaut) {
					Astronaut castAstro = (Astronaut)unitSelected;
					if (castAstro.getBuild() !=null && castAstro.getBuild() instanceof BuildAction) {
						if (((BuildAction)castAstro.getBuild()).selectMode()) {
							LOGGER.info("cancel");
							((BuildAction)castAstro.getBuild()).cancelBuild();
							unitSelected.setTexture(((Soldier) unitSelected).getDefaultTexture());
							castAstro.deselect();
							return;
						}
					} else if (castAstro.getBuild() !=null && castAstro.getBuild() instanceof BuildWallAction) {
						if (((BuildWallAction)castAstro.getBuild()).selectMode()) {
							LOGGER.info("cancel");
							((BuildWallAction)castAstro.getBuild()).cancelBuild();
							unitSelected.setTexture(((Soldier) unitSelected).getDefaultTexture());
							castAstro.deselect();
							return;
						}
					}

				}
				AbstractWorld world = GameManager.get().getWorld();

				// If we get another left click ignore the previous listeners
//					listeners.clear(); // Remove this to allow multiselect
			if(!skipChecking) {
				projX = x / tileWidth;
				projY = -(y - tileHeight / 2f) / tileHeight + projX;
				projX -= projY - projX;
			}else{
				projX=x;
				projY=y;
			}

				if (projX < 0 || projX > world.getWidth() || projY < 0 || projY > world.getLength()) {
					return;
				}

				LOGGER.info(String.format("Clicked on tile x:%f y:%f", projX,projY));

				List<BaseEntity> entities = GameManager.get().getWorld().getEntities((int)projX, (int)projY);
				
				if (entities.isEmpty()) {
					if(skipChecking) 
					    return;//this line is for multiselection
					LOGGER.info(String.format("No selectable enities found at x:%f y:%f", projX,projY));
					for (Clickable c : listeners) {
						if (c instanceof Soldier) 
						    ((Soldier)c).resetTexture();
					}
					((CustomizedWorld)world).deSelectAll();
					listeners.clear();//Deselect all the entities selected before
					return;
				}
				
				List<BaseEntity> staticEntities = new ArrayList<BaseEntity>(entities);
				boolean isClickable=false;
				BaseEntity chosen = null;
				for (BaseEntity e: staticEntities) {
					if (e instanceof Clickable) {
						if (e instanceof HasOwner) {
							//giving preference to Player's own entities.
							if (! ((HasOwner) e).isAi()) {
								chosen = e;
								isClickable = true;
								if (e instanceof Soldier && ((Soldier)e).getLoadStatus()!=1) { //preference for player's non-building entities.
									break;
								}
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
					((Clickable) chosen).onClick(this);
					//Checks if last clicked entity was unit and deselect unit if current selection is building
					if (chosen instanceof BuildingEntity && (unitSelected instanceof Soldier) && !unitSelected.getAction().isPresent()) {
						unregisterForRightClickNotification((Clickable) unitSelected);
						unitSelected.deselect();
						unitSelected.setTexture(((Soldier) unitSelected).getDefaultTexture());
						unitSelected = (BuildingEntity)chosen;
					}
					unitSelected = (BaseEntity)chosen;

				}
				
			} else if (button == 1) {
				if (unitSelected instanceof Astronaut && unitSelected.getAction().isPresent()) {
					if (unitSelected.getAction().get() instanceof BuildWallAction) {
						BuildWallAction action = (BuildWallAction)unitSelected.getAction().get();
						if (action.selectMode()) {
							action.projectWall();
						}
						
					}
				}
				// Right click
				projX = x/tileWidth;
				projY = -(y - tileHeight / 2f) / tileHeight + projX;
				projX -= projY - projX;

				for (Clickable c : listeners) {
					c.onRightClick(projX, projY);
					((SoundManager) GameManager.get().getManager(SoundManager.class)).blockSound();
				}
				((SoundManager) GameManager.get().getManager(SoundManager.class)).unblockSound();

				AbstractWorld world = GameManager.get().getWorld();
				((CustomizedWorld)world).deSelectAll();
			}
		}
	}

	public void registerForRightClickNotification(Clickable thing) {
		listeners.add(thing);
	}
	
	public void unregisterForRightClickNotification(Clickable thing) {
		listeners.remove(thing);
	}
	
	/**
	 * Force ignore future left clicks
	 * @param ignore
	 */
	public void ignoreLeftClicks(boolean ignore) {
		ignoreLeftClick = ignore;
	}
	
	/**
	 * Control if multiple units can be selected
	 * @param true to enable, false to disable
	 */
	public void multiSelect(boolean enable) {
		this.multiselect = enable;
	}

}