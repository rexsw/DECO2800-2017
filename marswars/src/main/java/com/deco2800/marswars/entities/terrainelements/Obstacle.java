package com.deco2800.marswars.entities.terrainelements;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.util.Box3D;

/**
 * used for general obstacles in the map
 */
public class Obstacle extends BaseEntity {

    /**
     * this function is used for gamesave. Never delete this
     */
    public Obstacle(){}

    public Obstacle(float posX, float posY, float posZ, float height, float width, ObstacleType type, String colour, boolean centered){

        super(new Box3D(posX, posY, posZ, 1f, 1f, 1f), height, width, centered);
        switch (type) {
            case TREE1:
                this.setTexture("tree1_"+ colour);
                break;
            case TREE2:
                this.setTexture("tree2_"+ colour);
                break;
            case TREE3:
                this.setTexture("tree3_"+ colour);
                break;
            case CLIFF_L:
                this.setTexture("cliff_left_"+ colour);
                break;
            case CLIFF_R:
                this.setTexture("cliff_right_"+ colour);
                break;
            default: // If for some reason wrong tree type is given, give it tree texture one
                this.setTexture("tree1_" + colour);
                break;
        }
        this.canWalkOver = false;
        //this.setCost(100000); This doesn't work unfortunately. I will add a clause in the pathfinder itself
        if (type==ObstacleType.TREE1||type==ObstacleType.TREE2||type==ObstacleType.TREE3) {
            this.setXoff(width*0.7f);
            this.setYoff(width*0.15f);
        } else {
            this.setXoff(width*0.7f);
            this.setYoff(width*0.05f);
        }
    }

    @Override
    public boolean getFix() {
        return false;
    }
}
