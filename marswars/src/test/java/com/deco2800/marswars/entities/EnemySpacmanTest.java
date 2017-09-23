package com.deco2800.marswars.entities;

import static org.junit.Assert.*;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.entities.EnemySpacman;
import com.deco2800.marswars.entities.TerrainElements.Resource;
import com.deco2800.marswars.managers.Manager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class EnemySpacmanTest {
    EnemySpacman man;

    @Before
    public void setup(){
        man = new EnemySpacman(0, 0, 0);
    }

    @Test
    public void constructorTest() {
        Assert.assertTrue(man != null);
        assertEquals(man.getOwner(),-1);
        assertFalse(man.showProgress());
    }

    @Test
    public void checkOwner() {
        EnemySpacman man = new EnemySpacman(1,1,1);
        EnemySpacman man2 = new EnemySpacman(1,1,1);
        EnemySpacman man3 = new EnemySpacman(1,1,1);
        man.setOwner(1);
        man2.setOwner(3);
        man3.setOwner(3);
        assertEquals(man.getOwner(), 1);
        assertFalse(man.sameOwner(man2));
        assertTrue(man2.sameOwner(man3));
    }

    @Test
    public void actionTest() {
        EnemySpacman man = new EnemySpacman(1,1,1);
        DecoAction action = Mockito.mock(DecoAction.class);
        man.setAction(action);

        assertTrue(man.showProgress());
        assertEquals(action.hashCode(), man.getCurrentAction().hashCode());
    }

    @Test
    public void actionTestTick() {
        EnemySpacman man = new EnemySpacman(1,1,1);
        DecoAction action = Mockito.mock(DecoAction.class);
        man.setAction(action);
        man.onTick(1);

        assertTrue(man.showProgress());
        assertEquals(action.hashCode(), man.getCurrentAction().hashCode());
    }

    @Test
    public void setActionTest() {
        EnemySpacman man = new EnemySpacman(1,1,1);
        DecoAction action = Mockito.mock(DecoAction.class);
        man.setCurrentAction(action);
        assertTrue(man.showProgress());
        assertEquals(action.hashCode(), man.getCurrentAction().hashCode());
    }
    @Test
    public void getProgressTest() {
        EnemySpacman man = new EnemySpacman(1,1,1);
        DecoAction action = Mockito.mock(DecoAction.class);
        man.setCurrentAction(action);
        assertEquals(0, man.getProgress());
    }
    @Test
    public void getNoProgressTest() {
        EnemySpacman man = new EnemySpacman(1,1,1);
        DecoAction action = Mockito.mock(DecoAction.class);
        assertEquals(0,man.getProgress());
    }
}

