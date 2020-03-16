package edu.yu.cs.com1320.project.impl;

import com.sun.jndi.toolkit.url.Uri;
import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.stage1.Document;
import org.junit.Before;
import org.junit.Test;

import javax.print.Doc;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;

import static org.junit.Assert.*;

public class HashTableImplTest {

	@Test
	public void getWithInt() {
		HashTableImpl<Integer, Integer> hashTable = new HashTableImpl<Integer, Integer>();
		hashTable.put(1, 27);
		hashTable.put(3, 7);
		hashTable.put(6, 2);
		hashTable.put(16, 22);
		hashTable.put(0, 22);
		assertEquals((Integer)22,hashTable.get(16));
		assertEquals((Integer)22,hashTable.get(0));
		assertEquals((Integer)7,hashTable.get(3));
		assertEquals(null,hashTable.get(21));
	}
	@Test
	public void put()
	{
		HashTableImpl<Integer, Integer> hashTable = new HashTableImpl<Integer, Integer>();
		assertEquals(null,hashTable.put(1, 27));
		assertEquals(null,hashTable.put(3, 7));
		assertEquals(null,hashTable.put(6, 2));
		assertEquals(null,hashTable.put(16, 22));
		assertEquals((Integer)27,hashTable.put(1, 22));
	}
	@Test
	public void delete()
	{
		HashTableImpl<Integer, Integer> hashTable = new HashTableImpl<Integer, Integer>();
		hashTable.put(1, 27);
		hashTable.put(3, 7);
		hashTable.put(6, 2);
		hashTable.put(16, 22);
		hashTable.put(0, 22);
		hashTable.put(1, null);
		hashTable.put(1, 27);
		hashTable.put(6, null);
		hashTable.put(6, 2);
		hashTable.put(16, null);
		hashTable.put(16, 22);
		hashTable.put(23, null); //nothing was put in this bucket
		hashTable.put(21, null);//not in the first bucket
		hashTable.put(4, null);//nothing is in this row

	}
	@Test
	public void putMethodWhenKeyIsAlreadyPresent()
	{
		HashTableImpl<Integer, Integer> hashTable = new HashTableImpl<Integer, Integer>();
		hashTable.put(2, 7);
		assertEquals(null, hashTable.put(7, 22));//return null when the key was not inputted before
		hashTable.put(12, 29);
		assertEquals((Integer)22,hashTable.put(7,45));//return the previous value since the key was already inside
		assertEquals((Integer)45,hashTable.get(7));//replace the old value
	}
	//API Testing
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
	private HashTable<String,String> table;

	@Before
	public void initTable(){
		this.table = new HashTableImpl<String, String>();
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
		assertEquals("entry 12",table.put(12, "entry 12+1"));
		assertEquals("entry 12+1",table.get(12));
		assertEquals("entry 23",table.get(23));
	}
}