package com.deco2800.marswars.managers;

import com.deco2800.marswars.buildings.Barracks;
import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.buildings.Bunker;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.entities.weatherEntities.Water;
import com.deco2800.marswars.util.Point;
import com.deco2800.marswars.worlds.BaseWorld;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WeatherManager extends Manager implements Tickable {
    private TimeManager timeManager =
            (TimeManager) GameManager.get().getManager(TimeManager.class);
    private ArrayList<BuildingEntity> pausedBuildings;
    private boolean damaged = false;
    private BaseWorld world = GameManager.get().getWorld();
    private Random random = new Random();
    long interval = 0;
    long currentTime = 0;
    boolean eventStarted = false;


    private static final org.slf4j.Logger LOGGER =
            LoggerFactory.getLogger(WeatherManager.class);

    /**
     * Sets the relevant weather even according to the current in game time.
     */
    public void setWeatherEvent() {
        LOGGER.info("WEATHER MAN TICKING");
        currentTime = timeManager.getGlobalTime();
        if (! timeManager.isPaused()) { //this.isRaining()) {
            if (currentTime > interval + 1000) {
                world = GameManager.get().getWorld();
                this.generateFlood();
                this.applyEffects(); 
                this.setInterval();
                LOGGER.info("WEATHER MAN TICKING");
            }
        } else {
            //this.resumeProduction();
            eventStarted = false;
        }
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
        return timeManager.getGameDays() % 3 == 0;
    }

    /**
     * Sets the damage over time taken by units caught in the weather event.
     */
    private void applyEffects() {
        ArrayList<float []> areaOfEffect = getAffectedTiles();
        // Maybe set time condition based on seconds passed to ensure dot works as intended?
        // ALSO VERY INEFFICIENT
        for (float [] tile: areaOfEffect) {
            List<BaseEntity> entities =
                    GameManager.get().getWorld().getEntities((int) tile[0],
                            (int) tile[1]);
            for (BaseEntity e: entities) {
                if (e.getPosX() == tile[0] && e.getPosY() == tile[1]) {
                    if (e instanceof Soldier) {
                        applyContinuousDamage(e);
                    } else if (e instanceof BuildingEntity) {
                        haltProduction((BuildingEntity) e);
                    }
                }
            }
        }
    }

    /**
     * Sets the damage over time taken by units caught in the weather event.
     * @param e - the entity to take damage
     */
    private void applyContinuousDamage(BaseEntity e) {
        int timeBetween = 5;
        // assuming that the function will not always be called 5 seconds apart
        if (timeManager.getPlaySeconds() % 10 > timeBetween && ! damaged) {
            ((Soldier) e).setDamage(((Soldier) e).getMaxHealth() / 5);
            damaged = true;
        } else {
            damaged = false;
        }
    }

    /**
     * Halts the production or build progression of the passed building
     * @param e - the building to be affected
     */
    private void haltProduction(BuildingEntity e) {
        pausedBuildings.add(e);
        if (e instanceof Barracks) {
            ((Barracks) e).getCurrentAction().get().pauseAction();
        } else  if (e instanceof Bunker) {
            ((Bunker) e).getCurrentAction().get().pauseAction();
        } else  if (e instanceof Base) {
            ((Base) e).getAction().get().pauseAction();
        }
    }

    /**
     * Returns any building caught in the weather events area of effect to its
     * normal state.
     */
    private void resumeProduction() {
        for (BuildingEntity building: pausedBuildings) {
            if (building instanceof Barracks) {
                ((Barracks) building).getCurrentAction().get().resumeAction();
            } else  if (building instanceof Bunker) {
                ((Bunker) building).getCurrentAction().get().resumeAction();
            } else  if (building instanceof Base) {
                ((Base) building).getCurrentAction().get().resumeAction();
            }
        }
    }

    /**
     * Returns the tiles affected by the current weather event.
     * @return
     */
    private ArrayList<float []> getAffectedTiles() {
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
    }



    /**
     * Checks that the given point is within the bounds of the map.
     * @param p
     * @return
     */
    private boolean checkPosition(Point p) {
        /* Ensure new water position is on the map */
        return (p.getX() < 0 || p.getY() < 0 || p.getX() > world.getWidth()
                || p.getY() > world.getLength());
    }

    /**
     * Returns a position adjacent to the provided Water entity that is
     * currently free of water, should one exist. If no free positions exist,
     * returns the point (-1, -1) to indicate that placement cannot be made.
     * @param existingWater
     * @return
     */
    private Point findFreePoint(Water existingWater) {
        Point position = new Point(-1,-1);
        boolean waterFound = false;
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
                        //break;
                    }
                }
                if (!waterFound) {
                    return p;
                }
            }
        }
        return position;
    }

    // ALTERNATELY COULD JUST INCREASE THE SIZE OF A SINGLE ENTITY?
    /**
     * Generates new water entities in a random position adjacent to existing
     * water entities with a fixed chance.
     * @param existingWater
     */
    private void generateWater(Water existingWater) {
        LOGGER.info("GENERATING WATER");
        if (random.nextInt(100) < 11) {
            // Point math needs fixing
            Point position = findFreePoint(existingWater);
            if (! this.checkPosition(position)) {
                LOGGER.info("FINDING NEW POS");
                generateWater(existingWater);
            }
            // Previous check should cover this, but to confirm
            if (position.getX() != -1 && position.getY() != -1) {
                Water floodDrop = new Water(world, position.getX(),
                        position.getY(), 0);
                world.addEntity(floodDrop);
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
        LOGGER.info("GENERATING FLOOD");
        // COLLISION MAP IS AUTOMATICALLY UPDATED, BUT ARE ENTITIES?
        List<BaseEntity> entities = world.getEntities();
        Boolean waterFound = false;
        // Find existing water entities
        for (BaseEntity e : entities) {
            if (e instanceof Water) {
                waterFound = true;
                this.generateWater((Water) e);
            }
        }
        if (waterFound) {
            LOGGER.info("WATER FOUND");
            return;
        } else {
            LOGGER.info("NO WATER YET");
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
        LOGGER.info("GENERATING DROP");
        Random r = new Random();
        int width = world.getWidth();
        int length = world.getLength();
        System.out.println(width + " WIDTH");
        Point p = new Point(width - r.nextInt(width/4)
                - 1, length - r.nextInt(length/4) - 1);
        // Ensure position valid (should be trivially true)
        if (! checkPosition(p)) {
            LOGGER.info("POSITION CHECKED");
            Water firstDrop = new Water(world, p.getX(),
                    p.getY(), 0);
            world.addEntity(firstDrop);
            LOGGER.info("WATER FINE");
            this.generateWater(firstDrop);
            LOGGER.info("DROP GENERATED");
            return;
        } else {
            LOGGER.info("BAD POSITION");
            return;
        }
    }

    //POSSIBLY ADD FUNCTION FOR PERIODICALLY ADDING MORE WATER IN NEW PLACES,
    // OR ADD LOOP IN FIRST DROP TO GENERATE 3 to 5 pools

    /**
     * Causes effects to come into play each game tick.
     * @param tick Current game tick
     */
    @Override
    public void onTick(int tick) {
        //this.setWeatherEvent();
    }
}

