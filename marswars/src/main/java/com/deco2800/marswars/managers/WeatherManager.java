package com.deco2800.marswars.managers;

import com.deco2800.marswars.buildings.Barracks;
import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.buildings.Bunker;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.entities.units.Soldier;

import java.util.ArrayList;
import java.util.List;

public class WeatherManager extends Manager {
    private TimeManager timeManager =
            (TimeManager) GameManager.get().getManager(TimeManager.class);
    private ArrayList<BuildingEntity> pausedBuildings;

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
        List<int []> areaOfEffect = getAffectedTiles();

        // Maybe set time condition based on seconds passed to ensure dot works as intended?

        // NEED CONDITION RELATIVE TO POSITION
        for (BaseEntity e: entities) {
            // Find entity storage for water tiles to get positions, might be able to resize from there too
            for (int [] tile: areaOfEffect) {
                if (e.getPosX() == tile[0] && e.getPosY() == tile[1])
                if (e instanceof Soldier) {
                    setUnitDamage(e, damage);
                } else if (e instanceof BuildingEntity) {
                    haltProduction((BuildingEntity) e);
                }
            }
        }
    }

    /**
     * Sets the damage over time taken by units caught in the weather event.
     * @param damage
     */
    public void setUnitDamage(BaseEntity e, int damage) {
        ((Soldier) e).setDamage(damage);
    }

    public void haltProduction(BuildingEntity e) {
        pausedBuildings.add(e);
        if (e instanceof Barracks) {
            ((Barracks) e).getAction().get().pauseAction();
        } else  if (e instanceof Bunker) {
            ((Bunker) e).getAction().get().pauseAction();
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
                ((Barracks) building).getAction().get().resumeAction();
            } else  if (building instanceof Bunker) {
                ((Bunker) building).getAction().get().resumeAction();
            } else  if (building instanceof Base) {
                ((Base) building).getAction().get().resumeAction();
            }
        }
    }

    /**
     * Returns the tiles affected by the current weather event.
     * @return
     */
    public List<int []> getAffectedTiles() {
        List<int []> affectedTiles = new ArrayList<>();
        return affectedTiles;
    }
}
