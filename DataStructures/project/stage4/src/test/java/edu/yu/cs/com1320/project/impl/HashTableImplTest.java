package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.HashTable;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

public class HashTableImplTest {

	@Test
	public void interfaceCount() {//tests that the class only implements one interface and its the correct one
		@SuppressWarnings("rawtypes")
		Class[] classes = HashTableImpl.class.getInterfaces();
		assertTrue(classes.length == 1);
		assertTrue(classes[0].getName().equals("edu.yu.cs.com1320.project.HashTable"));
	}

	@Test
	public void methodCount() {//tests for public and protected methods. the expected number should match the number of methods explicitly tested below save for the constructor
		Method[] methods = HashTableImpl.class.getDeclaredMethods();
		int publicMethodCount = 0;
		for (Method method : methods) {
			if (Modifier.isPublic(method.getModifiers())) {
				if(!method.getName().equals("equals") && !method.getName().equals("hashCode")) {
					publicMethodCount++;
				}
			}
		}
		assertTrue(publicMethodCount == 2);
	}

	@Test
	public void fieldCount() {//tests for public or protected fields
		Field[] fields = HashTableImpl.class.getFields();
		int publicProtectedFieldCount = 0;
		for (Field field : fields) {
			if (Modifier.isPublic(field.getModifiers()) || Modifier.isProtected(field.getModifiers())) {
				publicProtectedFieldCount++;
			}
		}
		assertTrue(publicProtectedFieldCount == 0);
	}

	@Test
	public void subClassCount() {//tests if any subclasses are public/protected
		@SuppressWarnings("rawtypes")
		Class[] classes = HashTableImpl.class.getClasses();
		assertTrue(classes.length == 0);
	}

	@Test
	public void constructorExists() {
		new HashTableImpl<String,String>();
	}

	@Test
	public void putExists() {
		try {
			new HashTableImpl<String,String>().put("hello", "there");
		} catch (RuntimeException e) {}//catch any run time error this input might cause. This is meant to test method existence, not correctness
	}

	@Test
	public void getExists() {
		try {
			new HashTableImpl<String,String>().get("hello");
		} catch (RuntimeException e) {}//catch any run time error this input might cause. This is meant to test method existence, not correctness
	}

	@Test
	public void stage2NoArgsConstructorExists(){
		try {
			new HashTableImpl<>();
		} catch (RuntimeException e) {}
	}
	private HashTable<String,String> table;

	@Before
	public void initTable(){
		this.table = new HashTableImpl<>();
		this.table.put("Key1", "Value1");
		this.table.put("Key2","Value2");
		this.table.put("Key3","Value3");
		this.table.put("Key4","Value4");
		this.table.put("Key5","Value5");
		this.table.put("Key6","Value6");
	}
	@Test
	public void testGet() {
		assertEquals("Value1",this.table.get("Key1"));
		assertEquals("Value2",this.table.get("Key2"));
		assertEquals("Value3",this.table.get("Key3"));
		assertEquals("Value4",this.table.get("Key4"));
		assertEquals("Value5",this.table.get("Key5"));
	}
	@Test
	public void testGetChained() {
		//second node in chain
		assertEquals("Value6",this.table.get("Key6"));
		//second node in chain after being modified
		this.table.put("Key6","Value6+1");
		assertEquals("Value6+1",this.table.get("Key6"));
		//check that other values still come back correctly
		testGet();
	}
	@Test
	public void testGetMiss() {
		assertEquals(null,this.table.get("Key20"));
	}
	@Test
	public void testPutReturnValue() {
		assertEquals("Value3",this.table.put("Key3","Value3+1"));
		assertEquals("Value6",this.table.put("Key6", "Value6+1"));
		assertEquals(null,this.table.put("Key7","Value7"));
	}
	@Test
	public void testGetChangedValue () {
		HashTableImpl<String, String> table = new HashTableImpl<String, String>();
		String key1 = "hello";
		String value1 = "how are you today?";
		String value2 = "HI!!!";
		table.put(key1, value1);
		assertEquals(value1,table.get(key1));
		table.put(key1, value2);
		assertEquals(value2,table.get(key1));
	}
	@Test
	public void testDeleteViaPutNull() {
		HashTableImpl<String, String> table = new HashTableImpl<String, String>();
		String key1 = "hello";
		String value1 = "how are you today?";
		String value2 = null;
		table.put(key1, value1);
		table.put(key1, value2);
		assertEquals(value2,table.get(key1));
	}
	@Test
	public void testSeparateChaining () {
		HashTableImpl<Integer, String> table = new HashTableImpl<Integer, String>();
		for(int i = 0; i <= 23; i++) {
			table.put(i, "entry " + i);
		}
		assertEquals("entry 12",table.put(12, "entry 12v2"));
		assertEquals("entry 12v2",table.get(12));
		assertEquals("entry 23",table.get(23));
	}
	@Test
	public void stage2TestArrayDoubling(){
		testSeparateChaining();
	}
	@Test
	public void stage2TestNoArgsConstructorExists(){
		try {
			new HashTableImpl();
		} catch (RuntimeException e) {}
	}

}