package com.deco2800.marswars.hud;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.managers.GameManager;

import java.util.List;

/**
 * Created by dell on 2017/9/18.
 */
public class CodeInterpreter {







    public void executeCode(String a){

        if (a.equals("killOne"))
        {
            reduceOneEnemy();
        }
        else if (a.equals("killAll"))
        {
            reduceAllEnemy();
        }


    }



    public void reduceOneEnemy()
    {
        List<BaseEntity> entitylist = GameManager.get().getWorld().getEntities();
        for(BaseEntity e:entitylist)
        {
            if(e.getOwner() != 0)
            {
                GameManager.get().getWorld().removeEntity(e);
                System.out.println( GameManager.get().getWorld().getEntities().size());
                return;
            }
        }
    }


    public void reduceAllEnemy()
    {
        List<BaseEntity> entitylist = GameManager.get().getWorld().getEntities();
        for(BaseEntity e:entitylist)
        {
            if(e.getOwner() != 0)
            {
                GameManager.get().getWorld().removeEntity(e);
                System.out.println( GameManager.get().getWorld().getEntities().size());
            }
        }
        System.out.println( GameManager.get().getWorld().getEntities().size());
        return;
    }








}
