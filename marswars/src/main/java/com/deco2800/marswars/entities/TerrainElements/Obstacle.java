package com.deco2800.marswars.entities.TerrainElements;

import com.deco2800.marswars.entities.BaseEntity;

/**
 * used for general obstacles in the map
 */
public class Obstacle extends BaseEntity {

    private String colour;
    private ObstacleType type;

    public Obstacle(float posX, float posY, float posZ, float height, float width, ObstacleType type, String colour){
        super(posX, posY, posZ, 1, 1, 1f, height, width, false);
        switch (type) {
            case TREE1:
                this.setTexture("tree1_"+colour);
                break;
            case TREE2:
                this.setTexture("tree2_"+colour);
                break;
            case TREE3:
                this.setTexture("tree3_"+colour);
                break;
        }
        this.canWalkOver = false;
        this.setCost(Integer.MAX_VALUE); //patfinding should never go through this
        this.colour = colour;
        this.type = type;
        if (type==ObstacleType.TREE1||type==ObstacleType.TREE2||type==ObstacleType.TREE3) {
            this.setXoff(width*0.775f);
            this.setYoff(width*0.1f);
        }
    }

    @Override
    public boolean getFix() {
        return false;
    }
}
