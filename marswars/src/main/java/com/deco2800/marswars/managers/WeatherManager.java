package com.deco2800.marswars.managers;

import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.util.Array2D;

import java.util.ArrayList;
import java.util.List;

public class WeatherManager extends Manager {
    private TimeManager timeManager =
            (TimeManager) GameManager.get().getManager(TimeManager.class);
    private ArrayList<BuildingEntity> pausedBuildings;
    boolean damaged = false;

    /**
     * Sets the relevant weather even according to the current in game time.
     */
    public void setWeatherEvent() {
        if (timeManager.getGameDays() % 3 == 0) {

        }
    }

    /**
     * Sets the damage over time taken by units caught in the weather event.
     * @param damage
     */
    public void applyEffects(int damage) {
        List<BaseEntity> entities =
                GameManager.get().getWorld().getEntities();
        ArrayList<int []> areaOfEffect = getAffectedTiles();
        // Maybe set time condition based on seconds passed to ensure dot works as intended?
        // ALSO VERY INEFFICIENT
        for (BaseEntity e: entities) {
            // Find entity storage for water tiles to get positions, might be able to resize from there too
            for (int [] tile: areaOfEffect) {
                if (e.getPosX() == tile[0] && e.getPosY() == tile[1]) {
                    if (e instanceof Soldier) {
                        setUnitDamage(e, damage);
                        applyContinuousDamage(e, damage);
                    } else if (e instanceof BuildingEntity) {
                        haltProduction((BuildingEntity) e);
                    }
                }
            }
        }
    }

    // May not need this
    /**
     * Sets the damage over time taken by units caught in the weather event.
     * @param damage
     */
    public void setUnitDamage(BaseEntity e, int damage) {
        ((Soldier) e).setDamage(damage);
    }

    /**
     * Sets the damage over time taken by units caught in the weather event.
     * @param damage
     */
    public void applyContinuousDamage(BaseEntity e, int damage) {
        int timeBetween = 5;
        // assuming that the function will not always be called 5 seconds apart
        if (timeManager.getPlaySeconds() % 10 > timeBetween && ! damaged) {
            ((Soldier) e).setDamage(damage);
            damaged = true;
        } else {
            damaged = false;
        }
    }

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

/*    *//**
     * Returns the tiles affected by the current weather event.
     * @return
     *//*
    public Array2D<List<BaseEntity>> getTileMap(
            Array2D<List<BaseEntity>> map) {
        return map;
    }*/

    /**
     * Returns the tiles affected by the current weather event.
     * @return
     */
    public ArrayList<int []> getAffectedTiles() {
        Array2D<List<BaseEntity>> tileMap =
                GameManager.get().getWorld().getCollisionMap();
        ArrayList<int []> affectedTiles = new ArrayList<>();
        // VERY INEFFICIENT SEE IF CAN RECORD POSITION OF WATER ON MAP INITALISATION
        // Iterate over x coord
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
        }
        return affectedTiles;
    }
}
