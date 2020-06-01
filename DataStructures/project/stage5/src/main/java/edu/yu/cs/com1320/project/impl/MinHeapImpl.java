package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.MinHeap;

import java.util.HashMap;
import java.util.NoSuchElementException;

public class MinHeapImpl<E extends Comparable> extends MinHeap<E> {
	public MinHeapImpl()//no arg constructor
	{
		this.elements = (E[]) new Comparable[10];
		this.elementsToArrayIndex = new HashMap<>();
	}
	@Override
	public void reHeapify(E element)
	{
		int x = getArrayIndex(element);
		downHeap(x);
		upHeap(x);
	}
	@Override
	protected int getArrayIndex(E element)
	{
		if (!elementsToArrayIndex.containsKey(element))
		{
			throw new NoSuchElementException("The inputted key was not in the heap");
		}
		return elementsToArrayIndex.get(element);
	}
	@Override
	protected void doubleArraySize()
	{
		E[] newElements = (E[]) new Comparable[elements.length*2];
		for (int j = 1; j <= count; j++)
		{
			newElements[j] = elements[j];
		}
		elements = newElements;
	}

	@Override
	protected boolean isEmpty() {
		return super.isEmpty();
	}

	@Override
	protected boolean isGreater(int i, int j) { return super.isGreater(i,j); }

	@Override
	protected void swap(int i, int j) {
		E tempI = this.elements[i];
		E tempJ = this.elements[j];
		elementsToArrayIndex.remove(tempI);
		elementsToArrayIndex.remove(tempJ);
		elementsToArrayIndex.put(tempI, j);
		elementsToArrayIndex.put(tempJ, i);
		this.elements[i] = this.elements[j];
		this.elements[j] = tempI;
	}
	@Override
	protected void upHeap(int k) {
		super.upHeap(k);
	}

	@Override
	protected void downHeap(int k) {
		super.downHeap(k);
	}

	@Override
	public void insert(E x) {
		// double size of array if necessary
		if (this.count >= this.elements.length - 1)
		{
			this.doubleArraySize();
		}
		//add x to the bottom of the heap
		this.elements[++this.count] = x;
		elementsToArrayIndex.put( x, this.count);
		//percolate it up to maintain heap order property
		this.upHeap(this.count);
	}

	@Override
	public E removeMin() {
		if (isEmpty())
		{
			throw new NoSuchElementException("Heap is empty");
		}
		E min = this.elements[1];
		//swap root with last, decrement count
		this.swap(1, this.count--);
		//move new root down as needed
		this.downHeap(1);
		this.elements[this.count + 1] = null; //null it to prepare for GC
		elementsToArrayIndex.remove(min);
		return (E)(min);
	}
	/* code from sedekwick to check if it is a minheap is ordered as needed to be
	// is pq[1..n] a min heap?
	public boolean isMinHeap() {
		for (int i = 1; i <= count; i++) {
			if (elements[i] == null) return false;
		}
		for (int i = count+1; i < elements.length; i++) {
			if (elements[i] != null) return false;
		}
		if (elements[0] != null) return false;
		return isMinHeapOrdered(1);
	}

	// is subtree of pq[1..n] rooted at k a min heap?
	private boolean isMinHeapOrdered(int k) {
		if (k > count) return true;
		int left = 2*k;
		int right = 2*k + 1;
		if (left  <= count && isGreater(k, left))  return false;
		if (right <= count && isGreater(k, right)) return false;
		return isMinHeapOrdered(left) && isMinHeapOrdered(right);
	}

	 */
}

