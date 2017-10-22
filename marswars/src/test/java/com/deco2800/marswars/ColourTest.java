package com.deco2800.marswars;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.marswars.managers.ColourManager;
import com.deco2800.marswars.managers.Colours;
import org.junit.Test;

import static org.junit.Assert.*;


public class ColourTest {

	@Test
	public void colourToStringTest() {
		assertEquals(Colours.BLUE.toString(), "Blue");
		assertEquals(Colours.YELLOW.toString(), "Yellow");
		assertEquals(Colours.PINK.toString(), "Pink");
		assertEquals(Colours.PURPLE.toString(), "Purple");
		assertEquals(Colours.GREEN.toString(), "Green");
	}
	
	@Test
	public void colourTolidgxTest() {
		ColourManager test = new ColourManager();
		int testid = 1;
		test.setColour(testid);
		assertTrue(test.getLibColour(testid) instanceof Color);
	}
	
	@Test
	public void setColourTest() {
		ColourManager test = new ColourManager();
		int testid = 1;
		test.setColour(testid);
		assertTrue(test.getColour(testid) instanceof String);
	}
	
	@Test
	public void setTwoColoursTest() {
		ColourManager test = new ColourManager();
		int testid = 1;
		int testid2 = 2;
		test.setColour(testid);
		test.setColour(testid2);
		assertTrue(test.getColour(testid) instanceof String);
		assertTrue(test.getColour(testid2) instanceof String);
		assertFalse(test.getColour(testid) == test.getColour(testid2));
		assertFalse(test.getColour(testid2) == test.getColour(testid));
	}
}
