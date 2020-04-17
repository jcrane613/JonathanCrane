package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Trie;
import edu.yu.cs.com1320.project.stage3.Document;
import edu.yu.cs.com1320.project.stage3.impl.DocumentStoreImpl;

import java.util.*;

public class TrieImpl<Value> implements Trie<Value>
{
	private Set<Value> totalSet = new HashSet<>();
	private Set<Value> deleteSet = new HashSet<>();
	private Set<Value> totalSetPrefix = new HashSet<>();
	public TrieImpl()
	{}
	public void put(String key, Value val)
	{
		if (key == null)
		{
			return;
		}
		if (val == null)
		{
			return;
		}
		else
		{
			this.root = put(this.root, editString(key), val, 0);
		}
	}
	private Node put(Node x, String key, Value val, int d)
	{
		if (x == null)
		{
			x = new Node();
		}
		//we've reached the last node in the key, set the value for the key and return the node
		if (d == key.length())
		{
			if (x.val == null)
			{
				Set<Value> arraySet = new HashSet<>();
				(x.val) = arraySet;
				((Set) x.val).add(val);
				return x;
			}
			else if (x.val != null)
			{
				((Set) x.val).add(val);
				return x;
			}
		}
		//proceed to the next node in the chain of nodes that
		//forms the desired key
		char c = key.charAt(d);
		x.links[c] = put(x.links[c], key, val, d + 1);
		return x;
	}
	public List<Value> getAllSorted(String key, Comparator<Value> comparator)
	{
		Node x = this.get(this.root, editString(key), 0);
		if (x == null || x.val == null || key == null)
		{
			List<Value> emptyList = new ArrayList<>();
			return emptyList;
		}
		List<Value> thisList = new ArrayList<>((Set<Value>)x.val);
		thisList.sort(comparator);
		return thisList;
	}
	public List<Value> getAllWithPrefixSorted(String prefix, Comparator<Value> comparator)
	{
		List<Value> emptyList = new ArrayList<>();
		if (prefix == null)
		{
			return emptyList;
		}
		totalSet.clear();
		Queue<String> results = new ArrayDeque<>();
		//find node which represents the prefix
		Node x = this.get(this.root, editString(prefix), 0);
		if (x == null)
		{
			return emptyList;
		}
		//collect keys under it
		if(x != null)
		{
			this.collect(x, new StringBuilder(editString(prefix)), results);
		}
		List<Value> yoRet = new ArrayList(totalSet);
		yoRet.sort(comparator);
		return yoRet;
	}
	private Value get(String key)
	{
		Node x = get(root, key, 0);
		if(x == null)
		{
			return null;
		}
		return (Value) x.val;
	}
	private Node get(Node x, String key, int d)
	{
		//link was null - return null, indicating a miss
		if (x == null)
		{
			return null;
		}
		//we've reached the last node in the key,
		//return the node
		if (d == key.length())
		{
			return x;
		}
		//proceed to the next node in the chain of nodes that
		//forms the desired key
		char c = key.charAt(d);
		return get(x.links[c], key, d + 1);
	}
	private void collect(Node x,StringBuilder prefix, Queue<String> results)
	{
		//if this node has a value, add it’s key to the queue
		if (x.val != null)
		{
			//add a string made up of the chars from root to this node to the result set
			results.add(prefix.toString());
			//add all the values to a master set
			totalSet.addAll((Set< Value>) x.val);
		}
		//visit each non-null child/link
		for (char c = 0; c < alphabetSize; c++)
		{
			if(x.links[c]!=null)
			{
				//add child's char to the string
				prefix.append(c);
				this.collect(x.links[c], prefix, results);
				//remove the child's char to prepare for next iteration
				prefix.deleteCharAt(prefix.length() - 1);
			}
		}
	}
	public Set<Value> deleteAllWithPrefix(String prefix)
	{
		if(prefix == null)
		{
			return new HashSet<Value>();
		}
		totalSetPrefix.clear();
		Queue<String> deleteResults = new ArrayDeque<>();
		//find node which represents the prefix
		Node x = this.get(this.root, editString(prefix), 0);
		//collect keys under it
		if(x == null)
		{
			return new HashSet<Value>();
		}
		this.collectPrefix(x, new StringBuilder(editString(prefix)), deleteResults);
		for (String str: deleteResults)
		{
			deleteAll(str);
		}
		return totalSetPrefix;
	}
	private void collectPrefix(Node x,StringBuilder prefix, Queue<String> results)
	{
		//if this node has a value, add it’s key to the queue
		if (x.val != null)
		{
			//add a string made up of the chars from root to this node to the result set
			results.add(prefix.toString());
			//add all the values to a master set
			totalSetPrefix.addAll((Set< Value>) x.val);
		}
		//visit each non-null child/link
		for (char c = 0; c < alphabetSize; c++)
		{
			if(x.links[c]!=null)
			{
				//add child's char to the string
				prefix.append(c);
				this.collectPrefix(x.links[c], prefix, results);
				//remove the child's char to prepare for next iteration
				prefix.deleteCharAt(prefix.length() - 1);
			}
		}
	}
	public Set<Value> deleteAll(String key)
	{
		if (key == null) //if this key does not exsit to be deleted, it will return an empty set
			return new HashSet<Value>();
		if (get(editString(key))== null) //if this key does not exsit to be deleted, it will return an empty set
			return new HashSet<Value>();
		if (!deleteSet.isEmpty())
		{
			deleteSet.clear();
		}
		Node root = deleteAll(this.root, editString(key), 0);
		if (root == null)
		{
			return new HashSet<Value>();
		}
		return deleteSet;
	}
	private Node deleteAll(Node x, String key, int d)
	{
		if (x == null)
			return null;
		//we're at the node to del - set the val to null
		if (d == key.length())
		{
			if (!((Set<Value>) x.val).isEmpty())
			{
				deleteSet.addAll((Set<Value>) x.val);
			}
			x.val = null;
		}
		//continue down the trie to the target node
		else
		{
			char c = key.charAt(d);
			x.links[c] = this.deleteAll(x.links[c], key, d + 1);
		}
		if (x.val != null)
			return x;
		//remove subtrie rooted at x if it is completely empty
		for (int c = 0; c <alphabetSize; c++)
		{
			if (x.links[c] != null)
				return x; //not empty
		}
		return null;
	}
	public Value delete(String key, Value val)
	{
		if (key == null)
		{
			return null;
		}
		if (val == null)
		{
			return null;
		}
		Value here = get(editString(key));
		if (here == null) return null;
		if(!((Set<Value>) here).contains(val))
		{
			return null;
		}
		if ((deleteThis(this.root, editString(key), 0, val)) == null)
		{
			return null;
		}
		return val;
	}
	private Node deleteThis(Node x, String key, int d, Value val) {
		if (x == null)
			return null;
		//we're at the node to del - set the val to null
		if (d == key.length())
		{
			if (!((Set<Value>) x.val).isEmpty() && ((Set<Value>) x.val).contains(val))
			{
				((Set<Value>) x.val).remove(val);
				return x;
			}
			else return x;
		}
		else
		{
			char c = key.charAt(d);
			x.links[c] = this.deleteThis(x.links[c], key, d + 1, val);
		}
		if (x.val != null)
			return x;
		for (int c = 0; c < alphabetSize; c++) //remove subtrie rooted at x if it is completely empty
		{
			if (x.links[c] != null)
				return x; //not empty
		}
		return null;
	}
	private String editString(String string)
	{
		String deleteCharacters =  string.replaceAll("[^A-Za-z0-9 ]","");
		return deleteCharacters.toUpperCase();
	}
	private int alphabetSize = 256; // extended ASCII
	private Node root; // root of trie

	private class Node<Value>
	{
		private Value val;
		private Node[] links = new Node[alphabetSize];
	}
}
