package com.deco2800.marswars;


import org.junit.Before;
import org.junit.Test;


import com.deco2800.marswars.entities.MultiSelectionTile;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.MultiSelection;
import com.deco2800.marswars.worlds.BaseWorld;


/**
 * Code coverage and Junit tests for MultiSelection and SelectedTiles.
 * 
 * @author jdtran21
 *
 */
public class MultiSelectionTest {

	/**
	 * initialize everything needed by the test
	 */
	@Before
	public void initMultiSelectionTest(){

		GameManager.get().setWorld(new BaseWorld("resources/mapAssets/tinyMars.tmx"));
	}


	/**
	 * Code coverage for MultiSelection, where world = null.
	 */
	@Test
	public void nullWorld() {	
		MultiSelection.resetSelectedTiles();
		BaseWorld baseWorld = new BaseWorld(20, 20);
		GameManager.get().setWorld(new BaseWorld(10, 15));
		MultiSelection multi = new MultiSelection();
		MultiSelection.resetSelectedTiles();
		MultiSelection.updateSelectedTiles(1, 1);
		MultiSelection.getSelectedTiles(1, 1);
		MultiSelection.updateSelectedTiles(0, 0);
		MultiSelection.updateSelectedTiles(25, 25);
	}

	/**
	 * Test for function addStartTile
	 */
	@Test
	public void testAddStartTile(){

		MultiSelection.resetSelectedTiles();

		MultiSelection.addStartTile(9f,12f);


	}

	/**
	 * Test for function addEndTile
	 */
	@Test
	public void testAddEndTile(){

		MultiSelection.resetSelectedTiles();

		MultiSelection.addEndTile(9f,12f);


	}

	/**
	 * Test for function clicking all tiles
	 */
	@Test
	public void testCallAllTiles(){
		MultiSelection multiSelection = new MultiSelection();
		MultiSelection.resetSelectedTiles();
		MultiSelection.addStartTile(3f,3f);
		MultiSelection.addEndTile(9f,12f);
		multiSelection.clickAllTiles();


	}

	/**
	 * Test for calling mouse handler
	 */
	@Test
	public void testCallMouseHandler(){
		MultiSelection multiSelection = new MultiSelection();
		multiSelection.callMouseHandler(2,2);

	}
	
	/**
	 * Code Coverage for entities.MultiSelectionTile.
	 */
	@Test
	public void multiSelectionTile() {
		MultiSelectionTile mst = new MultiSelectionTile(0, 0, 0, 0, 0);
	}
	

}
