package com.deco2800.marswars.hud;

import com.deco2800.marswars.MarsWars;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.managers.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * This class is to interprete and excute the cheatcode catched in chatbox
 */
public class CodeInterpreter {
    private TimeManager tm = (TimeManager)GameManager.get().getManager(TimeManager.class);
    private ResourceManager rm = (ResourceManager)GameManager.get().getManager(ResourceManager.class);
    private TechnologyManager tem = (TechnologyManager)GameManager.get().getManager(TechnologyManager.class);
    private WeatherManager wm = (WeatherManager)GameManager.get().getManager(WeatherManager.class);





    /**
     * call different methods when receiving different codes
     * @param String the code catched in chatbox
     */
    public void executeCode(String a) {

        String[] part = a.split("(?<=\\D)(?=\\d)");
        if(part.length == 2) {
            String part1 = part[0];
            String part2 = part[1];
            int num = Integer.parseInt(part2);
            try {
                Method sAge = this.getClass().getDeclaredMethod(part1, int.class);
                sAge.invoke(this, num);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NoSuchMethodException e) {
            }
        }
        else if (part.length == 1)
        {
            String part1 = part[0];
            try {
                Method sAge = this.getClass().getDeclaredMethod(part1);
                sAge.invoke(this);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NoSuchMethodException e) {
            }
        }

    }


    /**
     * If the code is equal to "killOne", reduce one enemy soldier
     */
    public void killone()
    {
        List<BaseEntity> entitylist = GameManager.get().getWorld().getEntities();
        for(BaseEntity e:entitylist)
        {
            if(e.getOwner() != -1 && e instanceof Soldier)
            {
                GameManager.get().getWorld().removeEntity(e);
                return;
            }
        }
    }

    /**
     * If the code is equal to "killAll", reduce all enemy soldier
     */
    public void killall()
    {
        List<BaseEntity> entitylist = GameManager.get().getWorld().getEntities();
        for(BaseEntity e:entitylist)
        {
            if(e.getOwner() != -1 && e instanceof Soldier)
            {
                GameManager.get().getWorld().removeEntity(e);

            }
        }

        return;
    }


    /**
     * If the code contains "rock" and digits, add the number of rock indicated by the digits.
     * @param int the number indicated by the digits
     */
    public void rock(int a){
        int num = rm.getRocks(-1) + a;
        rm.setRocks(num,-1);
    }



    /**
     * If the code contains "biomass" and digits, add the number of biomass indicated by the digits.
     *  @param int the number indicated by the digits
     */
    public void biomass(int a){
        int num = rm.getBiomass(-1) + a;
        rm.setBiomass(num,-1);
    }



    /**
     * If the code contains "crystal" and digits, add the number of crystal indicated by the digits.
     *  @param int the number indicated by the digits
     */
    public void crystal(int a){
        int num = rm.getCrystal(-1) + a;
        rm.setCrystal(num,-1);
    }



    /**
     * If the code contains "day", add the game time, so it makes it 6 am in the game.
     */
    public void day(){
        long add = 0;
        if ((tm.getHours()) < 6)
        {
            add = 21600 - tm.getHours()*60*60-tm.getMinutes()*60;

        }
        else if ((tm.getHours()) >= 6)
        {
            add = 24*60*60 - tm.getHours()*60*60 + 21600-tm.getMinutes()*60;

        }
        tm.addTime(add);
    }


    /**
     * If the code contains "night", add the game time, so it makes it 9 pm in the game.
     */
    public void night(){
        long add = 0;
        if ((tm.getHours()) < 21)
        {
            add = 75600 - tm.getHours()*60*60-tm.getMinutes()*60;

        }
        else if ((tm.getHours()) >= 21)
        {
            add = 24*60*60 - tm.getHours()*60*60 + 75600-tm.getMinutes()*60;

        }
        tm.addTime(add);

    }

    /**
     * If the code contains "hurryup", increase the in-game time by three hours.
     */
    public void hurryup() {
        tm.addTime(10800);
    }

    /**
     * If the code is "whosyourdaddy", set the enemies attack to be of no effect.
     */
    public void whosyourdaddy(){
        MarsWars.setInvincible(1);
    }


    /**
     * If the code is "fogoff", make the fog appear.
     */
    public void fogoff(){
            FogManager.toggleFog(false);

    }


    /**
     * If the code is "fogon", make the fog disappear.
     */
    public void fogon(){
        FogManager.toggleFog(true);

    }


    /**
     * Turns the flood effect on when floodon is typed into the chat window
     * during a single player game.
     */
    public void floodon(){
        wm.toggleFlood(true);
    }

    /**
     * Turns the flood effect off when "floodoff" is typed into the chat window
     * during a single player game.
     */
    public void floodoff(){
        wm.toggleFlood(false);
    }
    
    /**
     * Gets whether or not costs of buildings and units are free
     */
    public void costsFree(){
        GameManager.get().setCostsFree(true);
    }
    
    /**
     * Sets costs of buildings and units to be free or not
     */
    public void costsPaid(){
        GameManager.get().setCostsFree(false);
    }



}
