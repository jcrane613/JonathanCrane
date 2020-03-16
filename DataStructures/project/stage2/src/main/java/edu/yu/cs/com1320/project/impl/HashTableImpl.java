package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.HashTable;

public class HashTableImpl<Key, Value> implements HashTable <Key,Value> {
	private LinkedValues[] array = new LinkedValues[5];
	private int arraySize = 0;
	public HashTableImpl ()
	{}
	public Value get(Key k) {
		int index = hashFunction(k);
		if (array[index]== null)
		{
			return null;
		}
		Node head = array[index].head;
		while (head != null)
		{
			if ((head.key).equals(k))
			{
				return (Value) head.getValue();
			}
			head = head.next;
		}
		return null;
	}

	public Value put(Key k, Value v) {
		if (v == null)
		{
			delete(k);
			return null;
		}
		if (((((double)arraySize / array.length)))>=3.0)//should be the calculation when I want to resize the array
		{ resize(); }
		int index = hashFunction(k);
		if (array[index] == null)
		{
			LinkedValues linkedList = new LinkedValues();
			array[index] = linkedList;
			array[index].add(k,v);
			arraySize++;
			return null;
		}
		Node head = array[index].head;
		while (head != null)
		{
			if ((head.key).equals(k))
			{
				Value value = (Value) head.getValue();
				delete(k);
				array[index].add(k,v);
				return value;
			}
			head = head.next;
		}
		array[index].add(k,v);
		arraySize++;
		return null;
	}
	private void delete(Key k)
	{
		if (get(k)==null)
		{
			return;
		}
		int index = hashFunction(k);
		Node head = array[index].head;
		if ((head.getKey()).equals(k))//if it's the first node
		{
			array[index].head = head.next;
		}
		else
		{
			middleNode(head,k);
		}
	}
	private void middleNode(Node head, Key k)
	{
		Node previous = null;
		while (head.next != null)//this is to delete the node if it is in the middle of the array
		{
			if ((head.next.key).equals(k))
			{
				break;
			}
			previous = head;
			head = head.next;
		}
		if (head.next != null) // this deletes the one in the middle
		{
			if ((head.next.key).equals(k))
			{
				head.next = head.next.next;
			}
		}
		if ((head.key).equals(k) && head.next == null) //this will delete the one at the end
		{
			previous.next = null;
		}
	}
	private int hashFunction(Key key)
	{
		return (key.hashCode() & 0x7fffffff) % array.length;
	}
	private int hashFunctionGrow(Key key)
	{
		return (key.hashCode() & 0x7fffffff) % (array.length*2);
	}
	private void resize()
	{
		LinkedValues[] newArray = new LinkedValues[array.length*2];
		for (int i = 0; i<array.length; i++)
		{
			if (array[i] != null)
			{
				Node head = array[i].head;
				{
					while (head != null)
					{
						resizePut((Key) head.getKey(), (Value) head.getValue(), newArray);
						head = head.next;
					}
				}
			}
		}
		array = newArray;
	}
	private void resizePut(Key key, Value value, LinkedValues[] newArray)
	{
		int index = hashFunctionGrow(key);
		if (newArray[index] == null)//this linked list does not exist yet
		{
			LinkedValues linkedList = new LinkedValues();
			newArray[index] = linkedList;
			newArray[index].add(key,value);
		}
		if (newArray[index] != null)//this linked list already exists
		{
			newArray[index].add(key, value);
		}
	}
	private int size()
	{
		return arraySize;
	}
	public void printTest()
	{
		for (int j = 0; j<array.length; j++)
		{
			if (array[j] != null)
			{
				Node head = array[j].head;
				while (head != null)
				{
					System.out.print("This is the key: " + head.getKey() + " -- " + head.getValue() + "->");
					head = head.next;
				}
				System.out.println();
			}
		}
	}

	//the linked list that handles individual pieces of data
	private class Node <Key, Value>
	{
		Key key;
		Value value;
		Node next;
		private void setKey(Key key)
		{
			this.key = key;
		}
		private void setValue(Value value)
		{
			this.value = value;
		}
		private void setNode(Node node)
		{
			this.next = node;
		}
		private Value getValue()
		{
			return value;
		}
		private Key getKey()
		{
			return key;
		}
	}
	//class to link multiple nodes together
	private class LinkedValues <Key, Value>
	{
		Node head;
		Node node;

		private <Key, Value> void add(Key key, Value value)
		{
			Node node = new Node();
			node.setKey(key);
			node.setValue(value);
			node.setNode(null);
			if (head == null)
			{
				head = node;
			}
			else {
				Node current = head;
				while (current.next!= null){
					current = current.next;
				}
				current.next = node;
			}
		}
		private Node next()
		{
			Node current = head;
			while (current.next!= null){
				current = current.next;
			}
			return current.next = node;
		}
		private int length()
		{
			int size = 0;
			Node front = head;
			while (front!= null)
			{
				size++;
				front =front.next;
			}
			return size;
		}

	}
}