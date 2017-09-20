package com.deco2800.marswars.hud;

import com.deco2800.marswars.MarsWars;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.Manager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.TimeManager;

import java.util.List;

/**
 * This class is to interprete and excute the cheatcode catched in cheatbox
 */
public class CodeInterpreter {




    /**
     * call different methods when receiving different codes
     * @param String the code catched in cheatbox
     */
    public void executeCode(String a){
        if (a.equals("killOne"))
        {
            reduceOneEnemy();
        }
        else if (a.equals("killAll"))
        {
            reduceAllEnemy();
        }
        else if (a.contains("rock"))
        {   String str = a.replaceAll("\\D+","");
            int result = Integer.parseInt(str);
            addRock(result);
        }
        else if (a.contains("crystal"))
        {   String str = a.replaceAll("\\D+","");
            int result = Integer.parseInt(str);
            addCrystal(result);
        }
        else if (a.contains("water"))
        {   String str = a.replaceAll("\\D+","");
            int result = Integer.parseInt(str);
            addWater(result);
        }
        else if (a.contains("biomass"))
        {   String str = a.replaceAll("\\D+","");
            int result = Integer.parseInt(str);;
            addBiomass(result);
        }
       // else if (a.equals("day"))
       // {
       //     switchDay();
       // }
       // else if (a.equals("night"))
       // {
       //     switchNight();
        //}
        else if (a.equals("whosyourdaddy"))
        {
            invincible();
        }



    }


    /**
     * If the code is equal to "killOne", reduce one enemy
     */
    public void reduceOneEnemy()
    {
        List<BaseEntity> entitylist = GameManager.get().getWorld().getEntities();
        for(BaseEntity e:entitylist)
        {
            if(e.getOwner() != -1)
            {
                GameManager.get().getWorld().removeEntity(e);
                System.out.println( GameManager.get().getWorld().getEntities().size());
                return;
            }
        }
    }

    /**
     * If the code is equal to "killAll", reduce all enemy
     */
    public void reduceAllEnemy()
    {
        List<BaseEntity> entitylist = GameManager.get().getWorld().getEntities();
        for(BaseEntity e:entitylist)
        {
            if(e.getOwner() != -1)
            {
                GameManager.get().getWorld().removeEntity(e);
                System.out.println( GameManager.get().getWorld().getEntities().size());
            }
        }
        System.out.println( GameManager.get().getWorld().getEntities().size());
        return;
    }


    /**
     * If the code contains "rock" and digits, add the number of rock indicated by the digits.
     * @param int the number indicated by the digits
     */
    public void addRock(int a){
        Manager manager = GameManager.get().getManager(ResourceManager.class);
        ResourceManager rm = (ResourceManager)manager;
        int num = rm.getRocks(-1) + a;
        rm.setRocks(num,-1);
    }



    /**
     * If the code contains "biomass" and digits, add the number of biomass indicated by the digits.
     *  @param int the number indicated by the digits
     */
    public void addBiomass(int a){
        Manager manager = GameManager.get().getManager(ResourceManager.class);
        ResourceManager rm = (ResourceManager)manager;
        int num = rm.getBiomass(-1) + a;
        rm.setBiomass(num,-1);
    }



    /**
     * If the code contains "crystal" and digits, add the number of crystal indicated by the digits.
     *  @param int the number indicated by the digits
     */
    public void addCrystal(int a){
        Manager manager = GameManager.get().getManager(ResourceManager.class);
        ResourceManager rm = (ResourceManager)manager;
        int num = rm.getCrystal(-1) + a;
        rm.setCrystal(num,-1);
    }



    /**
     * If the code contains "water" and digits, add the number of water indicated by the digits.
     *  @param int the number indicated by the digits
     */
    public void addWater(int a){
        Manager manager = GameManager.get().getManager(ResourceManager.class);
        ResourceManager rm = (ResourceManager)manager;
        int num = rm.getWater(-1) + a;
        rm.setWater(num,-1);
    }




    //public void switchDay(){
      //  Manager manager = GameManager.get().getManager(TimeManager.class);
        //TimeManager tm = (TimeManager)manager;


    //}



    //public void switchNight(){
      //  Manager manager = GameManager.get().getManager(TimeManager.class);
        //TimeManager tm = (TimeManager)manager;

    //}


    /**
     * If the code is "whosyourdaddy", set the player's team to be invincible.
     */
    public void invincible(){
        MarsWars.invincible = 1;
    }
















}
