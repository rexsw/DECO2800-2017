package com.deco2800.marswars.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple tree to test the engine.
 * This tree is a hasProgress to test that interface
 * @author Tim Hadwen
 */
public class Tree extends BaseEntity implements HasProgress {

    private static final Logger LOGGER = LoggerFactory.getLogger(Tree.class);

    /**
     * Constructor for a Tree
     * @param posX
     * @param posY
     * @param posZ
     */
    public Tree(float posX, float posY, float posZ) {
        super(posX, posY, posZ, 1f, 1f, 1f);

        /* Sets the texture of the tree to tree */
        this.setTexture("tree");
        LOGGER.info("Making a tree @ [" + posX + ", " + posY + "]");
    }

    @Override
    public int getProgress() {
        return 100;
    }

    @Override
    public boolean showProgress() {
        return true;
    }
}
