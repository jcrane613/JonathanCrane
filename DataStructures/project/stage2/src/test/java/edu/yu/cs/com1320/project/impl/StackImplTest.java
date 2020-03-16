package edu.yu.cs.com1320.project.impl;

import org.junit.Test;

import static org.junit.Assert.*;

public class StackImplTest {

	@Test
	public void push() {
		StackImpl<Integer> stack = new StackImpl<>();
		stack.push(12);
		stack.push(24);
		stack.push(78);
		assertEquals(3,stack.size());
		assertEquals((Integer) 78,(stack.peek()));
		assertEquals((Integer) 78,(stack.pop()));
		assertEquals((Integer)24,stack.pop());
		stack.push(56);
		assertEquals((Integer)56,stack.pop());
		assertEquals(1,stack.size());
		stack.push(12);
		assertEquals(2,stack.size());
		stack.push(34);
		assertEquals(3,stack.size());
		stack.push(34);
		assertEquals(4,stack.size());
		assertEquals((Integer)34,stack.peek());
		assertEquals((Integer)34,stack.pop());
		assertEquals(3,stack.size());

	}
	@Test
	public void emptyStack()
	{
		StackImpl<Integer> stack = new StackImpl<>();
		assertEquals(null,stack.peek());
		assertEquals(null, stack.pop());
		assertEquals(0,stack.size());
	}
	@Test
	public void interfaceCount() {//tests that the class only implements one interface and its the correct one
		@SuppressWarnings("rawtypes")
		Class[] classes = StackImpl.class.getInterfaces();
		assertTrue(classes.length == 1);
		assertTrue(classes[0].getName().equals("edu.yu.cs.com1320.project.Stack"));
	}
	@Test
	public void constructorExists() {
		try {
			new StackImpl<>();
		} catch (Exception e) {}
	}
}