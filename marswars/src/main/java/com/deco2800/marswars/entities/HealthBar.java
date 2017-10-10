package com.deco2800.marswars.entities;

import com.badlogic.gdx.graphics.Texture;
import com.deco2800.marswars.util.Box3D;

/**
 * Created by hayde on 5/10/2017.
 */

public class HealthBar extends AbstractEntity {
    private BaseEntity parentEntity;
    private int state;
    private static String textures[] = {"", ""};
    private boolean visible;

    public HealthBar(float posX, float posY, float posZ, float xLength, float yLength, float zLength, BaseEntity parentEntity) {
        super(posX, posY, posZ, xLength, yLength, zLength);
        this.parentEntity = parentEntity;
    }

    public void translateToParent() {
        super.setPosition(parentEntity.getPosX(), parentEntity.getPosY() + parentEntity.getYRenderLength(), parentEntity.getPosZ());
    }


    public void update() {
        if (!visible) {
            super.setPosition(100000, 0, -4);
        } else {
            translateToParent();
        }
        state = Math.round(parentEntity.getStats().getMaxHealth() * 20 / parentEntity.getStats().getHealth());
        this.setTexture(textures[state]);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
