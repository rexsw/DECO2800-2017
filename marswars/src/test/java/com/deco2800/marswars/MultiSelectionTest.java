package com.deco2800.marswars;

import org.junit.Test;
import com.deco2800.marswars.entities.MultiSelectionTile;
import com.deco2800.marswars.managers.MultiSelection;

/**
 * Code coverage and Junit tests for MultiSelection
 * 
 * @author jdtran21
 *
 */
public class MultiSelectionTest {

	/**
	 * Code coverage for MultiSelection, where world = null.
	 */
	@Test
	public void nullWorld() {
		MultiSelection multi = new MultiSelection();
		MultiSelection.resetSelectedTiles();
	}
	
	/**
	 * Code Coverage for MultiSelectionTile.
	 */
	@Test
	public void MultiSelectionTile() {
		MultiSelectionTile mst = new MultiSelectionTile(0, 0, 0, 0, 0);
	}
}
