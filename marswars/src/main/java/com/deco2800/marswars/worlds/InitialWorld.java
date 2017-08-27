package com.deco2800.marswars.worlds;

import com.deco2800.marswars.entities.*;
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
		this.addEntity(new Base2(this, 10, 10, 0));
		this.addEntity(new Soldier(50, 10, 0)); // test for combat
		this.addEntity(new Soldier(50, 15, 0)); // test for combat
		//Missile Test
		Soldier a = new Soldier(30, 30, 0);
		this.addEntity(a);
		this.addEntity(new Bullet(50, 50, 0, a, 100, 100));
		//Priest Test
		this.addEntity(new Priest(25, 35, 0));
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
	}
}