package com.deco2800.marswars.entities;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.deco2800.marswars.entities.units.Soldier;
/**
 * Created by Hayden Bird on 5/10/2017.
 */

public class HealthBar extends BaseEntity {
    private BaseEntity parentEntity;
    private int state;
    private boolean visible = true;
    private boolean finished = false;

    public HealthBar(float posX, float posY, float posZ, float xLength, float yLength, float zLength, BaseEntity parentEntity) {
        super(posX, posY, posZ, xLength, yLength, zLength);
        this.parentEntity = parentEntity;

    }

    public void translateToParent() {
        super.setPosition(parentEntity.getPosX(), parentEntity.getPosY(), parentEntity.getPosZ()+3);
    }

    public void update() {
	if (parentEntity.getEntityType() == EntityType.UNIT) {
	    if (((Soldier) parentEntity).getLoadStatus() == 1) {
		setVisible(false);
	    }
		}
        if (!visible) {
            super.setPosition(100000, 0, -4);
            return;
        } else {
            translateToParent();
        }
        state = Math.round(parentEntity.getStats().getHealth() * 20 / parentEntity.getStats().getMaxHealth());
        this.setTexture("Health" + state);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getState() {return state;}

    public void generateTextures(int number) {
        PixmapIO pIO = new PixmapIO();
        for (int i = 0; i <= number; i++) {
            FileHandle f = new FileHandle("resources/UnitAssets/HealthBar/Health"+i+".png");
            int width = 512;
            int fillPoint = (width * i) /number;
            Pixmap p = new Pixmap(width, 20, Pixmap.Format.RGBA8888);
            p.setColor(Color.GRAY);
            p.fill();
            p.setColor(Color.GREEN);
            if (number*100/i < 50) {
            	p.setColor(Color.ORANGE);
            }
            if (number*100/i < 10) {
            	p.setColor(Color.RED);
            }
            p.fillRectangle(0,0,fillPoint,20);
            pIO.writePNG(f,p);
            p.dispose();
        }
    }

    @Override
    public boolean isCollidable() {return false;}

    /*Disable all of the methods that are not needed for health bars*/

    @Override
    public HealthBar getHealthBar() {
        return null;
    }

    @Override
    public void makeSelected() {return;}

    @Override
    protected void modifyCollisionMap(boolean add) { return;}
}
