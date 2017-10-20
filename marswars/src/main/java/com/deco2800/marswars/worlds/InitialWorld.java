package com.deco2800.marswars.worlds;

import com.deco2800.marswars.entities.Selectable;
import com.deco2800.marswars.entities.terrainelements.Resource;
import com.deco2800.marswars.entities.terrainelements.ResourceType;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.renderers.Renderable;

import java.util.Random;

/**
 * Created by timhadwen on 19/7/17.
 */
public class InitialWorld extends BaseWorld {

	/**
	 * Constructor for InitialWorld
	 * @param worldFileName File name of the world to be loaded
	 */
	public InitialWorld() {
		super("resources/placeholderassets/placeholder200.tmx");
	}

	/**
	 * Adds entities to the world
	 */
	public void loadEntities() {
		for (int x = 0; x < this.getWidth(); x += 5) {
			for (int y = 0; y < this.getLength(); y += 5) {
				Random r = new Random();

				// CRYSTAL
				if (r.nextInt(10) < 0.1) {
					this.addEntity(new Resource(x, y, 0, 1f, 1f, ResourceType.CRYSTAL));
					continue;
				} 

				// ROCK
				if (r.nextInt(10) < 0.1) {
					this.addEntity(new Resource(x, y, 0, 1f, 1f, ResourceType.ROCK));
					continue;
				}

				// BIOMASS
				if (r.nextInt(10) < 0.1) {
					this.addEntity(new Resource(x, y, 0, 1f, 1f, ResourceType.BIOMASS));
				}
			}
		}

	}


	/**
	 * Adds cluster of enemy spacman, needs to be improved
	 */
	private void addEnemyGroup(int x, int y){
		if(x-1 < 0 || y-1 < 0 || y +1 >= this.getLength() || x + 1 >= this.getWidth()){
			return;
		}
		this.addEntity(new Soldier(x, y, 0, 2)); //Changed to Soldier

	}

	/**
	 * Adds randomly placed spacman onto grid
	 */
	private void addRandomSpacman(){
		Random r = new Random();
		int x = r.nextInt(this.getLength()-1);
		int y = r.nextInt(this.getWidth()-1);
		//this.addEntity(new HeroSpacman(this, x, y, 0)); // NEED TO FIX THIS TO MAKE IT WORK
	}


	/**
	 * Deselects all entities
	 */
	public void deSelectAll() {
		for (Renderable r : this.getEntities()) {
			if (r instanceof Selectable) {
				((Selectable) r).deselect();
			}
		}
	}


}