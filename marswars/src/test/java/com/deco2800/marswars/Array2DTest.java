package com.deco2800.marswars;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.deco2800.marswars.util.Array2D;

public class Array2DTest {
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void ConstructorTest() {
		Array2D<Integer> arr = new Array2D<>(3,4);
		assertEquals(arr.getWidth(), 3);
		assertEquals(arr.getLength(), 4);
	}
	
	@Test
	public void SetTestException() {
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
	public void SetTest() {
		Array2D<Integer> arr = new Array2D<>(3,4);
		arr.set(2, 1, 1);
	}
	
	@Test
	public void GetTestException() {
		Array2D<Integer> arr = new Array2D<>(5,5);
		thrown.expect(IndexOutOfBoundsException.class);
		arr.get(5, 5);
	}
	
	@Test
	public void GetTest() {
		Array2D<Integer> arr = new Array2D<>(5,5);
		arr.set(3, 4, 1);
		assertTrue(arr.get(3, 4).equals(1));
	}
	
	@Test
	public void GetListTest() {
		Array2D<Integer> arr = new Array2D<>(5,5);
		arr.set(3, 4, 1);
		arr.set(4, 4, 2);
		
		List<Integer> expected = new ArrayList<>();
		expected.add(1);
		expected.add(2);
		
		assertEquals(arr.getList(), expected);
	}
}
