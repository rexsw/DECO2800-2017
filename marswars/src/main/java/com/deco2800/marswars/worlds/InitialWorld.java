package com.deco2800.marswars.worlds;

import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.renderers.Renderable;

import java.util.Random;

/**
 * Created by timhadwen on 19/7/17.
 */
public class InitialWorld extends BaseWorld {

	/**
	 * Constructor for InitialWorld
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

				// WATER
				if (r.nextInt(10) < 0.1) {
					this.addEntity(new Resource(x, y, 0, 1f, 1f, ResourceType.WATER));
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

		this.addEntity(new Spacman(0,0,0));
		this.addEntity(new Spacman(1, 1, 1)); // this spac man is for resource gather test
		this.addEntity(new HeroSpacman(this, 4, 4, 0));
		this.addEntity(new Base(this, 8, 8, 0));
		this.addEntity(new Base2(this, 14, 18, 0));
		this.addEntity(new EnemySpacman(24, 24, 0));
		this.addEntity(new EnemyTank(20, 20, 0));
		this.addEntity(new Base2(this, 10, 10, 0));
		this.addEntity(new Bunker(this, 35, 35, 0));
		this.addEntity(new Turret(this, 30, 30, 0));
		this.addEntity(new Barracks(this, 40, 40, 0));

	}


	/**
	 * Adds cluster of enemy spacman, needs to be improved
	 */
	private void addEnemyGroup(int x, int y){
		if(x-1 < 0 || y-1 < 0 || y +1 >= this.getLength() || x + 1 >= this.getWidth()){
			return;
		}
		this.addEntity(new EnemySpacman(x, y, 0));
		this.addEntity(new EnemySpacman(x - 1, y, 0));
		this.addEntity(new EnemySpacman(x, y - 1, 0));
		this.addEntity(new EnemySpacman(x + 1, y, 0));
		this.addEntity(new EnemySpacman(x, y+1, 0));
		this.addEntity(new EnemySpacman(x + 1, y + 1, 0));
		this.addEntity(new EnemySpacman(x - 1, y - 1, 0));
		this.addEntity(new EnemySpacman(x - 1, y + 1, 0));
		this.addEntity(new EnemySpacman(x + 1, y -1, 0));
	}

	/**
	 * Adds randomly placed spacman onto grid
	 */
	private void addRandomSpacman(){
		Random r = new Random();
		int x = r.nextInt(this.getLength()-1);
		int y = r.nextInt(this.getWidth()-1);
		this.addEntity(new HeroSpacman(this, x, y, 0));
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
	

		this.addEntity(new Base2(this, 14, 18, 0));
		this.addEntity(new EnemySpacman(24, 24, 0));
		this.addEntity(new EnemyTank(20, 20, 0));
	}


}