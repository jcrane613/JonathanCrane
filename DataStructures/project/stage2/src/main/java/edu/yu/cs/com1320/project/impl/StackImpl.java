package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Stack;

public class StackImpl<T> implements Stack<T> {
	public StackImpl()//constructor that takes no arg
	{}
	LinkedValues<T> linkedValues = new LinkedValues<>();
	public void push(T element)
	{
		linkedValues.add(element);
	}

	public T pop()
	{
		if (linkedValues == null || linkedValues.head == null)
		{
			return null;
		}
		T value = (T) linkedValues.head.getElement();//get the value
		linkedValues.head = linkedValues.head.next;//delete it from the stack
		return value;
	}

	public T peek()
	{
		if (linkedValues == null || linkedValues.head == null)
		{
			return null;
		}
		return (T) linkedValues.head.getElement();
	}

	public int size()
	{
		return linkedValues.size();
	}
	private class Node <T>
	{
		T element;
		Node next;

		private void setElement(T element)
		{
			this.element = element;
		}
		private void setNode(Node node)
		{
			this.next = node;
		}
		private T getElement()
		{
			return element;
		}
	}
	private class LinkedValues<T> {
		Node head;
		private <T> void add(T element) {
			Node node = new Node();
			node.setElement(element);
			node.setNode(null);
			if (head == null)
			{
				head = node;
			} else
			{
				node.next = head;
				head = node;
			}
		}
		private int size()
		{
			int counter = 0;
			Node mover = head;
			while (mover != null)
			{
				counter++;
				mover = mover.next;
			}
			return counter;
		}
	}
}
