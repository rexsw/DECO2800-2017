package com.deco2800.marswars.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private boolean floodOn = true;
    private ParticleEffect effect;

    /**
     * Sets the toggle value for the UI flood toggle button. The toggle either
     * enables the flood effect, or prevents further flooding and causes any
     * currently existing flood waters to retreat.
     * @param isFlooding
     * @return
     */
    public void toggleFlood(boolean isFlooding) {
        this.floodOn = isFlooding;
    }

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
            if (timeManager.getHours() < 3 && floodOn) {
                status = true;
                if (currentTime > interval + 10) {
                    world = GameManager.get().getWorld();
                    this.generateFlood();
                    this.applyContinuousDamage(this.checkAffectedEntities());
                    this.setInterval();
                }
            }
            if ((timeManager.getHours() >= 3 && floodWatersExist) || ! floodOn) {
                status = true;
                // +10 a good time for actual condition
                if (currentTime > interval + 2) {
                    world = GameManager.get().getWorld();
                    this.setInterval();
                    this.retreatWaters();
                    // While water still exists, continue to apply effects
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
        return timeManager.getHours() > 0 && timeManager.getHours() < 15;
    }

    /**
     * New method for determining which entities are caught by the flood.
     * Pauses all buildings that are positioned on the same tile as a Water
     * entity, and adds any units currently positioned on Water to a list which
     * is returned for processing by `applyContinuousDamage()`;
     * @return ArrayList</BaseEntity> containing units caught in flood waters
     */
    private List<BaseEntity> checkAffectedEntities() {
        List<BaseEntity> floodableEntities = world.getFloodableEntityList();
        List<BaseEntity> damagedUnits = new ArrayList<>();
        // Iterate over list of all floodable entities in the world
        for (BaseEntity entity: floodableEntities) {
            boolean buildingFlooded = false;
            float unitX = entity.getPosX();
            float unitY = entity.getPosY();
            List<BaseEntity> locationEntities =
                    GameManager.get().getWorld().getEntities((int) unitX,
                    (int) unitY);
            // Iterate through list of entities at the same location as
            // the entity of interest
            for (BaseEntity e: locationEntities) {
                if (e instanceof Water && entity instanceof Soldier) {
                    damagedUnits.add(entity);
                    break;
                } else if (e instanceof Water && entity instanceof
                        BuildingEntity) {
                    buildingFlooded = true;
                    ((BuildingEntity) entity).setFlooded(true);
                    timeManager.pause(entity);
                    break;
                }
            }
            // If water is not found on the same tile as a BuildingEntity,
            // set it's flooded attribute to false
            if (entity instanceof BuildingEntity && ! buildingFlooded) {
                ((BuildingEntity) entity).setFlooded(false);
                    timeManager.unPause(entity);
            }
        }
        return damagedUnits;
    }

    /**
     * Sets the damage over time taken by units caught in the weather event.
     * @param damagedUnits - the entities to take damage
     */
    private void applyContinuousDamage(List<BaseEntity> damagedUnits) {
        int timeBetween = 3;
        int condition = 1;
        if (damagedUnits.isEmpty()) {
            return;
        }
        // assuming that the function will not always be called 5 seconds apart
        if (timeManager.getPlaySeconds() % timeBetween > condition && ! damaged) {
            for (BaseEntity e: damagedUnits) {
                if (((Soldier) e).getLoadStatus() == 0) {
		            ((Soldier) e).setHealth(((Soldier) e).getHealth()
			                - ((Soldier) e).getMaxHealth() / 10);
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
     * Checks that the given point is within the bounds of the map.
     * @param p
     * @return
     */
    public boolean checkPosition(Point p) {
        /* Ensure new water position is on the map */
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
        if (waterEntities < (world.getWidth() * world.getLength()) / 3 * 2) {
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
    }

    /**
     * Creates a Water entity for use in flood generation on the given map if
     * none currently exist.
     */
    private void generateFirstDrop() {
        int width = world.getWidth();
        int length = world.getLength();
        Point p = new Point(width/2
                - 1,  length/2 - 1);
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

    /**
     * Renders the rain particle effect for the flood.
     * @param batch
     */
    public void render(SpriteBatch batch) {
        if (effect == null) {
            effect = new ParticleEffect();
            effect.load(Gdx.files.internal("resources/WeatherAssets/rain2.p"),
                    Gdx.files.internal("resources/WeatherAssets"));
            effect.setPosition(GameManager.get().getCamera().position.x -
                    GameManager.get().getCamera().viewportWidth *
                            GameManager.get().getCamera().zoom/2,
                    GameManager.get().getCamera().position.y +
                            GameManager.get().getCamera().viewportHeight *
                                    GameManager.get().getCamera().zoom/2);
            effect.start();
        }
        effect.setPosition(GameManager.get().getCamera().position.x -
                GameManager.get().getCamera().viewportWidth *
                        GameManager.get().getCamera().zoom/2,
                GameManager.get().getCamera().position.y +
                        GameManager.get().getCamera().viewportHeight *
                                GameManager.get().getCamera().zoom/2);
        effect.draw(batch,  Gdx.graphics.getDeltaTime());
    }

    /**
     * Stub method. Water's onTick controls the weather effect.
     * @param tick Current game tick
     */
    @Override
    public void onTick(int tick) {
        return;
    }
}

