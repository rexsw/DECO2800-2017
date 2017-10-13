package com.deco2800.marswars.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.Tickable;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.entities.weatherEntities.Water;
import com.deco2800.marswars.util.Point;
import com.deco2800.marswars.worlds.BaseWorld;

import java.util.*;

/**
 * A WeatherManager class for tracking and controlling SpacWars dynamic
 * weather events and their respective effects.
 *
 * @author Isaac Doidge
 */
public class WeatherManager extends Manager implements Tickable {
    private TimeManager timeManager =
            (TimeManager) GameManager.get().getManager(TimeManager.class);
    private BaseWorld world = GameManager.get().getWorld();
    private Random random = new Random();
    private long interval = 0;
    private long currentTime = 0;
    private boolean damaged = false;
    private boolean floodWatersExist = false;
    private int waterEntities = 0;
    // Set as a class variable so that buildings can be unpaused properly
    private ArrayList<BaseEntity> pausedBuildings = new ArrayList<>();

    /**
     * Sets the relevant weather even according to the current in game time.
     * Returns true if a weather event is currently in progress.
     */
    public boolean setWeatherEvent() {
        /* Get world state each call to ensure no changes have occurred
         (allows for dynamic switching between worlds) */
        world = GameManager.get().getWorld();
        boolean status = false;
        currentTime = timeManager.getGlobalTime();
        if (! timeManager.isPaused()) {
            // Generate floodwaters if raining
            if (timeManager.getHours() < 1) {
                status = true;
                if (currentTime > interval + 10) {
                    world = GameManager.get().getWorld();
                    if (waterEntities < (world.getWidth() *
                            world.getLength()) / 2) {
                        this.generateFlood();
                    }
                    //this.applyEffects();
                    this.applyContinuousDamage(this.checkAffectedEntities());
                    this.setInterval();
                }
            }
            if (timeManager.getHours() >= 1 && floodWatersExist) {
                status = true;
                // +10 a good time for actual condition
                if (currentTime > interval + 2) {
                    world = GameManager.get().getWorld();
                    this.setInterval();
                    // While water still exists, continue to apply effects
                    this.retreatWaters();
                    this.applyContinuousDamage(this.checkAffectedEntities());
                }
            }
        }
        return status;
    }

    /**
     * Sets the interval used to slow down the generation of water tiles
     */
    private void setInterval() {
        interval = timeManager.getGlobalTime();
    }

    /**
     * Sets the relevant weather even according to the current in game time.
     */
    public boolean isRaining() {
        return timeManager.getHours() > 10 && timeManager.getHours() < 15;
    }

    /**
     * New method for determining which entities are caught by the flood.
     * Pauses all buildings that are positioned on the same tile as a Water
     * entity, and adds any units currently positioned on Water to a list which
     * is returned for processing by `applyContinuousDamage()`;
     * @return ArrayList</BaseEntity> containing units caught in flood waters
     */
    private ArrayList<BaseEntity> checkAffectedEntities() {
        ArrayList<BaseEntity> floodableEntities = world.getFloodableEntityList();
        ArrayList<BaseEntity> damagedUnits = new ArrayList<>();
        // Iterate over list of all floodable entities in the world
        for (BaseEntity entity: floodableEntities) {
            boolean buildingFlooded = false;
            float unitX = entity.getPosX();
            float unitY = entity.getPosY();
            List<BaseEntity> locationEntities =
                    GameManager.get().getWorld().getEntities((int) unitX,
                    (int) unitY);
            // Iterate through list of entities at the same location as the entity of interest
            for (BaseEntity e: locationEntities) {
                if (e instanceof Water && entity instanceof Soldier) {
                    // Check how many times comes in here - should only be once (can break if more than once)
                    //System.out.println("&&&&&&&&&&&&&&&&&&&&&&&& Adding DAMAGED SOLDIER FROM: " + (int) unitX + ", " + (int) unitY);
                    damagedUnits.add(entity);
                    break;
                } else if (e instanceof Water && entity instanceof BuildingEntity) {
                   // System.out.println("&&&&&&&&&&&&&&&&&&&&&&&& Adding PAUSED BUILDING FROM: " + (int) unitX + ", " + (int) unitY);
                    buildingFlooded = true;
                    ((BuildingEntity) entity).setFlooded(true);
                    pausedBuildings.add(entity);
                    timeManager.pause(entity);
                    break;
                }
            }
            // If water is not found on the same tile as a BuildingEntity, set it's flooded attribute to false
            if (entity instanceof BuildingEntity && ! buildingFlooded) {
                ((BuildingEntity) entity).setFlooded(false);
                if (pausedBuildings.remove(entity)) {
                    timeManager.unPause(entity);
                }
            }
        }
        return damagedUnits;
    }

    /**
     * Sets the damage over time taken by units caught in the weather event.
     */
/*    private void applyEffects(ArrayList<BaseEntity> damagedUnits, ) {
        ArrayList<float []> areaOfEffect = getAffectedTiles();
        ArrayList<BaseEntity> damagedUnits = new ArrayList<>();
        for (float [] tile: areaOfEffect) {
            List<BaseEntity> entities =
                    GameManager.get().getWorld().getEntities((int) tile[0],
                            (int) tile[1]);
            Iterator<BaseEntity> iterator = entities.iterator();
            if (entities.size() > 5) {
                // Currently getting far too many entities at only one location (18) still shouldn't be slowing things significantly
                System.out.println("&&&&&&&&&&&&&&&&&&&&&&&& NUMBER OF ENTITIES: " + entities.size() + "AT: " + tile[0] + ", " + tile[1]);
            }
            while (iterator.hasNext()) {
                BaseEntity e = iterator.next();
                if (e.getPosX() == tile[0] && e.getPosY() == tile[1]) {
                    if (e instanceof Soldier) {
                        damagedUnits.add(e);
                    } else if (e instanceof BuildingEntity) {
                        pausedBuildings.add(e);
                        timeManager.pause(e);
                        ((BuildingEntity) e).setFlooded(true);
                    }
                }
            }
        }
        applyContinuousDamage(damagedUnits);
        timeManager.pause(pausedBuildings);
        resumeProduction(pausedBuildings, areaOfEffect);
        areaOfEffect.clear();
    } */

    /**
     * Sets the damage over time taken by units caught in the weather event.
     * @param damagedUnits - the entities to take damage
     */
    private void applyContinuousDamage(List<BaseEntity> damagedUnits) {
        int timeBetween = 5;
        int condition = 1;
        if (damagedUnits.size() < 1) {
            return;
        }
        // assuming that the function will not always be called 5 seconds apart
        if (timeManager.getPlaySeconds() % timeBetween > condition && ! damaged) {
            for (BaseEntity e: damagedUnits) {
                //if (((Soldier) e).getHealth() <
                //        ((Soldier) e).getMaxHealth() / 10) {
                    // Check for existing action and complete before deletion
                //}
        	//Carrier units and its loaded units are not affected by weather damage
		if (((Soldier) e).getLoadStatus() == 0) {
		    ((Soldier) e).setHealth(((Soldier) e).getHealth()
			    - (((Soldier) e)).getMaxHealth() / 10);
		}
            }
            damaged = true;
        } else if (timeManager.getPlaySeconds() % timeBetween <= condition) {
            damaged = false;
        }
        damagedUnits.clear();
        return;
    }

    /**
     * Returns any building caught in the weather events area of effect to its
     * normal state.
     */
/*    private void resumeProduction(List<BaseEntity> pausedBuildings,
                                  List<float []> areaOfEffect) {
        Iterator<BaseEntity> buildingIterator = pausedBuildings.iterator();
        while (buildingIterator.hasNext()) {
            BaseEntity e = buildingIterator.next();
            boolean stillFlooded = false;
            for (float [] tile: areaOfEffect) {
                if (e.getPosX() == tile[0] && e.getPosY() == tile[1]) {
                    stillFlooded = true;
                }
            }
            // set flooded in here
            if (! stillFlooded) {
                timeManager.unPause(e);
                ((BuildingEntity) e).setFlooded(false);
                // Remove unpaused building from list
                buildingIterator.remove();
            }
        }
    } */

    /**
     * Returns the tiles affected by the current weather event.
     * @return
     */
/*    private ArrayList<float []> getAffectedTiles() {
        List<BaseEntity> entities =
                GameManager.get().getWorld().getEntities();
        ArrayList<float []> affectedTiles = new ArrayList<>();

        for (BaseEntity e : entities) {
            if (e instanceof Water) {
                float [] coords = {e.getPosX(), e.getPosY()};
                affectedTiles.add(coords);
            }
        }
        return affectedTiles;
    } */



    /**
     * Checks that the given point is within the bounds of the map.
     * @param p
     * @return
     */
    public boolean checkPosition(Point p) {
        /* Ensure new water position is on the map */
        // SHOULD THIS BE WIDTH AND LENGTH OR WIDTH-1, LENGTH-1?
        return p.getX() < 0 || p.getY() < 0 || p.getX() > world.getWidth()
                || p.getY() > world.getLength();
    }

    /**
     * Returns a list of the 8 tiles surrounding the tile at the position of the
     * new Water entity.
     * @param existingWater
     * @return
     */
    private ArrayList<Point> getPotentialPossitions(Water existingWater) {
        // Make list of all possible tile positions around current Water entity
        ArrayList<Point> positionList = new ArrayList<>();
        positionList.add(new Point(existingWater.getPosX() + 1,
                existingWater.getPosY()));
        positionList.add(new Point(existingWater.getPosX() - 1,
                existingWater.getPosY()));
        positionList.add(new Point(existingWater.getPosX(),
                existingWater.getPosY() + 1));
        positionList.add(new Point(existingWater.getPosX(),
                existingWater.getPosY() - 1));
        positionList.add(new Point(existingWater.getPosX() + 1,
                existingWater.getPosY() + 1));
        positionList.add(new Point(existingWater.getPosX() - 1,
                existingWater.getPosY() - 1));
        positionList.add(new Point(existingWater.getPosX() + 1,
                existingWater.getPosY() - 1));
        positionList.add(new Point(existingWater.getPosX() - 1,
                existingWater.getPosY() + 1));
        return positionList;
    }

    /**
     * Returns a position adjacent to the provided Water entity that is
     * currently free of water, should one exist. If no free positions exist,
     * returns the point (-1, -1) to indicate that placement cannot be made.
     * @param existingWater
     * @return
     */
    private Point findFreePoint(Water existingWater) {
        Point badPosition = new Point(-1,-1);
        boolean waterFound = false;
        int waterCount = 0;
        // Make list of all possible tile positions around current Water entity
        ArrayList<Point> positionList = getPotentialPossitions(existingWater);
        /* rearrange list each time function is called so water is not always
        placed in the same direction from the previous water */
        Collections.shuffle(positionList);
        // Find a position where there is currently no water
        for (Point p: positionList) {
            if (p.getX() > 0 && p.getX() < world.getWidth() && p.getY() > 0 &&
                    p.getY() < world.getLength()) {
                List<BaseEntity> entities = world.getEntities((int) p.getX(),
                        (int) p.getY());
                for (BaseEntity e : entities) {
                    if (e instanceof Water) {
                        waterFound = true;
                        waterCount++;
                        break;
                    }
                }
                if (!waterFound) {
                    return p;
                }
            }
        }
        /* No possible positions for placement - indicate this to increase
        efficiency of future checks */
        // For some reason, this count check is necessary; above logic is not
        // entirely complete (should be trivially surrounded if arriving here)
        if (waterCount == 8) {
            existingWater.setSurrounded();
        }
        return badPosition;
    }

    /**
     * Generates new water entities in a random position adjacent to existing
     * water entities with a fixed chance.
     * @param existingWater
     */
    private void generateWater(Water existingWater) {
        if (random.nextInt(100) < 11) {
            // Point math needs fixing
            Point position = findFreePoint(existingWater);
            if (! this.checkPosition(position)) {
                generateWater(existingWater);
            }
            // Previous check should cover this, but to confirm
            if (position.getX() != -1 && position.getY() != -1) {
                Water floodDrop = new Water(position.getX(),
                        position.getY(), 0);
                world.addEntity(floodDrop);
                floodWatersExist = true;
                waterEntities++;
            } else {
                // No free position found, so return with no change
                return;
            }
        }
    }

    /**
     * Generates the flooding effect. Multiplies existing water on the map
     * when it is raining in the game, or creates new water which is then
     * multiplied.
     */
    private void generateFlood() {
        List<BaseEntity> entities = world.getEntities();
        Boolean waterFound = false;
        // Find existing water entities
        for (BaseEntity e : entities) {
            if (e instanceof Water) {
                waterFound = true;
                if (!((Water) e).isSurrounded()) {
                    this.generateWater((Water) e);
                }
            }
        }
        if (waterFound) {
            return;
        } else {
            // No water found in map... Create some
            this.generateFirstDrop();
            return;
        }
    }

    /**
     * Creates a Water entity for use in flood generation on the given map if
     * none currently exist.
     */
    private void generateFirstDrop() {
        Random r = new Random();
        int width = world.getWidth();
        int length = world.getLength();
        Point p = new Point(width/2
                - 1,  length/2 - 1);
        //Point p = new Point(r.nextInt(width/2)
        //        - 1,  r.nextInt(length/2) - 1);
        //Point p = new Point(width - r.nextInt(width/4)
         //       - 1, length - r.nextInt(length/4) - 1);
        // Ensure position valid (should be trivially true)
        if (! checkPosition(p)) {
            Water firstDrop = new Water(p.getX(),
                    p.getY(), 0);
            world.addEntity(firstDrop);
            waterEntities++;
            floodWatersExist = true;
            this.generateWater(firstDrop);
            return;
        } else {
            return;
        }
    }

    /**
     * Systematically removes the Water entities created by the flood, returns
     * false when no further entities remain.
     *
     * Made public to enable potential use in cheat codes (also helps with
     * testing).
     */
    public boolean retreatWaters() {
        /* Get world state each call to ensure no changes have occurred
         (allows for dynamic switching between worlds) */
        world = GameManager.get().getWorld();
        List<BaseEntity> entities = world.getEntities();
        Boolean waterFound = false;
        int waterCount = 0;

        List<BaseEntity> removeEntities = new ArrayList<>();
        for (BaseEntity e: entities) {
            if (e instanceof Water) {
                waterFound = true;
                removeEntities.add(e);
                waterCount++;
                if (waterCount == 10) {
                    break;
                }
            }
        }
        if (waterFound) {
            for (BaseEntity water: removeEntities) {
                world.removeEntity(water);
            }
        } else {
            floodWatersExist = false;
        }
        removeEntities.clear();
        return floodWatersExist;
    }

    public void addRainVisuals(SpriteBatch batch) {
        if (this.isRaining()) {
            // Particle for rain visuals
            ParticleEffect effect = new ParticleEffect();
            effect.load(Gdx.files.internal("resources/WeatherAssets/rain.p"),
                    Gdx.files.internal("resources/WeatherAssets"));
            effect.setPosition(Gdx.graphics.getWidth() / 2,
                    Gdx.graphics.getHeight());
            effect.start();
            effect.draw(batch);
        }
    }

    /**
     * Causes effects to come into play each game tick.
     * @param tick Current game tick
     */
    @Override
    public void onTick(int tick) {

        //this.setWeatherEvent();
    }
//    //Add Rain Particle effect
//    private SpriteBatch batch;    //is already within MarsWars
//    private ParticleEffect effect;    
//    public void show(){
//    	batch = new SpriteBatch;
//    	effect = new particleEffect();
//    	effect.load(Gdx.files.internal("resources/WeatherAssets/rainParticle"),
//    			(Gdx.files.internal("resources/WeatherAssets")));
//    	effect.setPosition(Gdx.graphics.getWidth() / 2,
//    			Gdx.graphics.getHeight());
//      effect.start();
//    }
    
//		Within MarsWars just need to draw this particle effect using effect.draw();    
    	
}

