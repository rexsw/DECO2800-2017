package com.deco2800.marswars;

import com.deco2800.marswars.util.Array2D;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Array2DTest {
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void constructorTest() {
		Array2D<Integer> arr = new Array2D<>(3,4);
		assertEquals(arr.getWidth(), 3);
		assertEquals(arr.getLength(), 4);
	}
	
	@Test
	public void setTestException() {
		Array2D<Integer> arr = new Array2D<>(3,4);
		thrown.expect(IndexOutOfBoundsException.class);
		// x >= 3
		arr.set(3, 1, 1);
		arr.set(3, -1, 1);
		arr.set(3, 4, 1);
		// y >= 4
		arr.set(1, 4, 1);
		arr.set(-1, 4, 1);
		// x < 3 && x >= 0
		arr.set(2, -4, 1);
		arr.set(2, 1, 1);
		// y < 4 && y >= 0
		arr.set(1, 3, 1);
		arr.set(-1, 3, 1);
	}
	
	@Test
	public void setTest() {
		Array2D<Integer> arr = new Array2D<>(3,4);
		arr.set(2, 1, 1);
	}
	
	@Test
	public void getTestException() {
		Array2D<Integer> arr = new Array2D<>(5,5);
		thrown.expect(IndexOutOfBoundsException.class);
		arr.get(5, 5);
	}
	
	@Test
	public void getTest() {
		Array2D<Integer> arr = new Array2D<>(5,5);
		arr.set(3, 4, 1);
		assertTrue(arr.get(3, 4).equals(1));
	}
	
	@Test
	public void getListTest() {
		Array2D<Integer> arr = new Array2D<>(5,5);
		arr.set(3, 4, 1);
		arr.set(4, 4, 2);
		
		List<Integer> expected = new ArrayList<>();
		expected.add(1);
		expected.add(2);
		
		assertEquals(arr.getList(), expected);
	}
}
