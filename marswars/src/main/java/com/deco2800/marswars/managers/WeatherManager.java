package com.deco2800.marswars.managers;

import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.entities.buildings.Barracks;
import com.deco2800.marswars.entities.buildings.Base;
import com.deco2800.marswars.entities.buildings.BuildingEntity;
import com.deco2800.marswars.entities.buildings.Bunker;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.entities.weatherEntities.Water;
import com.deco2800.marswars.util.Array2D;
import com.deco2800.marswars.util.Point;
import com.deco2800.marswars.worlds.BaseWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeatherManager extends Manager implements Tickable {
    private TimeManager timeManager =
            (TimeManager) GameManager.get().getManager(TimeManager.class);
    private ArrayList<BuildingEntity> pausedBuildings;
    private boolean damaged = false;
    private BaseWorld world = GameManager.get().getWorld();


    /**
     * Sets the relevant weather even according to the current in game time.
     */
    public void setWeatherEvent() {
        if (timeManager.getGameDays() % 3 == 0) {

        }
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
    public void applyEffects() {

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
    public void applyContinuousDamage(BaseEntity e) {
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
    public void haltProduction(BuildingEntity e) {
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
    public void resumeProduction() {
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
    public ArrayList<float []> getAffectedTiles() {
        List<BaseEntity> entities =
                GameManager.get().getWorld().getEntities();
        ArrayList<float []> affectedTiles = new ArrayList<>();

        for (BaseEntity e : entities) {
            if (e instanceof Water) {
                float [] coords = {e.getPosX(), e.getPosY()};
                affectedTiles.add(coords);
            }
        }
        /*// Iterate over x coord
        for (int i = 0; i < tileMap.getLength(); i++) {
            // Iterate over y coord
            for (int j = 0; j < tileMap.getWidth(); j++) {
                // Iterate through entity list at current coord
                for (int k = 0; k < tileMap.get(i, j).size(); k++) {
                    // NEED TO DEFINE NEW ENTITY TYPE FOR WATER (and others if implementing other effects)
                    if (tileMap.get(i, j).get(k).getEntityType().equals(5)) {
                        int [] coords = {i, j};
                        affectedTiles.add(coords);
                    }
                }
            }
        }*/
        return affectedTiles;
    }



    /**
     * Checks that the given point is within the bounds of the map.
     * @param p
     * @return
     */
    public boolean checkPosition(Point p) {
        /* Ensure new water position is on the map */
        return (p.getX() < 0 || p.getY() < 0 || p.getX() > world.getWidth()
                || p.getY() > world.getLength());
    }

    // ALTERNATELY COULD JUST INCREASE THE SIZE OF A SINGLE ENTITY?
    /**
     * Generates new water entities in a random position adjacent to existing
     * water entities with a fixed chance.
     * @param existingWater
     */
    public void generateWater(Water existingWater) {
        Random random = new Random();
        if (random.nextInt(100) < 11) {
            Random r = new Random();
            Point p = new Point(existingWater.getPosX() + r.nextInt(2)
                    - 1, existingWater.getPosY() + r.nextInt(2) - 1);
            if (! this.checkPosition(p)) {
                generateWater(existingWater);
            }
			/* Check that the new position does not contain water */
            List<BaseEntity> entities = world.getEntities((int) p.getX(),
                    (int) p.getY());
            boolean canPlace = true;
            for (BaseEntity e: entities) {
                if (e instanceof Water) {
                    canPlace = false;
                } else {
                    canPlace = true;
                }
            }
            if (canPlace) {
                new Water(world, p.getX(), p.getY(), 0, 52,
                        52);
            } else {
                return;
            }
        }
    }

    /**
     * Generates the flooding effect. Multiplies existing water on the map
     * when it is raining in the game, or creates new water which is then
     * multiplied.
     */
    public void generateFlood() {
        List<BaseEntity> entities = world.getEntities();
        // Find existing water entities
        for (BaseEntity e : entities) {
            if (e instanceof Water) {
                this.generateWater((Water) e);
            }
        }
        // No water found in map... Create some
        Random r = new Random();
        Point p = new Point(world.getWidth() - r.nextInt(10)
                - 1, world.getLength() - r.nextInt(10) - 1);
        // Ensure position valid (should be trivially true)
        if (checkPosition(p)) {
            Water firstDrop = new Water(world, p.getX(),
                    p.getY(), 0, 52, 52);
            this.generateWater(firstDrop);
        } else {
            return;
        }
    }

    /**
     * Causes effects to come into play each game tick.
     * @param tick Current game tick
     */
    @Override
    public void onTick(int tick) {
        if (this.isRaining()) {
            //this.generateFlood();
            //this.applyEffects();
        } else {
            this.resumeProduction();
        }
    }
}

