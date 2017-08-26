
import static org.junit.Assert.*;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.entities.EnemySpacman;
import com.deco2800.marswars.entities.Resource;
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
        assertEquals(man.getOwner(), null);
        assertFalse(man.isWorking());
    }

    @Test
    public void checkOwner() {
        EnemySpacman man = new EnemySpacman(1,1,1);
        EnemySpacman man2 = new EnemySpacman(1,1,1);
        EnemySpacman man3 = new EnemySpacman(1,1,1);
        Resource mockResource = Mockito.mock(Resource.class);
        Manager mockManager = Mockito.mock(Manager.class);
        Manager mockManager2 = Mockito.mock(Manager.class);
        man.setOwner(mockManager);
        man2.setOwner(mockManager2);
        man3.setOwner(mockManager);

        assertEquals(man.getOwner(), mockManager);
        assertFalse(man.sameOwner(man2));
        assertTrue(man.sameOwner(man3));
        assertFalse(man.sameOwner(mockResource));
    }

    @Test
    public void actionTest() {
        EnemySpacman man = new EnemySpacman(1,1,1);
        DecoAction action = Mockito.mock(DecoAction.class);
        man.setAction(action);

        assertTrue(man.isWorking());
        assertEquals(action.hashCode(), man.getCurrentAction().hashCode());
    }
}

