package edu.yu.cs.com1320.project.stage4.impl;

import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.Undoable;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;


import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.Undoable;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.stage4.Document;
import edu.yu.cs.com1320.project.stage4.DocumentStore;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import edu.yu.cs.com1320.project.impl.TrieImpl;

public class DocumentStoreImpl implements DocumentStore{

	private HashTableImpl<URI, Document> hashTable = new HashTableImpl<>();
	private StackImpl<Undoable> stack = new StackImpl<>();
	private HashTableImpl<URI, Document> deletedHashTable = new HashTableImpl<>();
	private TrieImpl<Document> documentTrie = new TrieImpl<>();
	private HashTableImpl<URI, Document> hashTableDeleteUndoDeleteAll = new HashTableImpl<>();
	private HashTableImpl<URI, Document> hashTableDeleteUndoDeletePrefix = new HashTableImpl<>();
	public DocumentStoreImpl()
	{}
	public int putDocument(InputStream input, URI uri, DocumentFormat format) {
		if (uri == null) { throw new IllegalArgumentException("The uri was null");}
		if (format == null){throw new IllegalArgumentException("The format was null");}
		if (input == null)
		{
			return checkURINull(uri);
		}
		int txthash = 0;
		byte[] inputByte = convertToByteArray(input);
		if (format == DocumentFormat.PDF)
		{
			return pdfHelper(inputByte,txthash,uri);
		}
		if (format == DocumentFormat.TXT)
		{
			return txtHelper(inputByte, txthash, uri);
		}
		return txthash;
	}
	private int checkURINull(URI uri)
	{
		if (hashTable.get(uri)==null)
		{
			GenericCommand command = new GenericCommand(uri, ((j) -> doNothing(uri)));
			stack.push(command);
			return 0;
		}
		Document valueToReturn = (Document)hashTable.get(uri);
		boolean test = deleteDocument(uri);
		return valueToReturn.getDocumentTextHashCode();
	}
	private boolean doNothing(URI uri)
	{
		return true;
	}
	private boolean doNothing(String string)
	{
		return true;
	}
	private byte[] convertToByteArray(InputStream inputStream)
	{
		try
		{
			BufferedInputStream stream = new BufferedInputStream(inputStream);
			int size = 0;
			size = stream.available();
			byte[] bytes = new byte[size];
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			int number;
			while ((number = inputStream.read(bytes,0, bytes.length)) != -1)
				output.write(bytes, 0, number);
			return output.toByteArray();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return new byte[0];
	}
	private int pdfHelper(byte[] inputByte, int txthash,URI uri)
	{
		try
		{
			PDDocument thisDoc = PDDocument.load(inputByte);
			PDFTextStripper stripper = new PDFTextStripper();
			String stringText = stripper.getText(thisDoc).trim();
			txthash = stringText.hashCode();
			Document testDocument = (Document) hashTable.get(uri);
			if (testDocument != null && testDocument.getDocumentTextHashCode() == txthash)//same doc
			{
				GenericCommand command = new GenericCommand(uri, ((j) -> doNothing(uri)));
				stack.push(command);
				return testDocument.getDocumentTextHashCode();
			}
			if (testDocument != null && testDocument.getDocumentTextHashCode() != txthash)//same uri
			{
				return sameUriPDF(uri,stringText,txthash,inputByte);
			}
			DocumentImpl newDocument = new DocumentImpl(uri, stringText, txthash, inputByte);
			hashTable.put(uri, newDocument);
			lambdaExpression(uri);
			inputKey(stringText, newDocument);//add the keys to the trie
			thisDoc.close();
			return 0;
		}
		catch (IOException e) { e.printStackTrace(); }
		return 0;
	}
	private int sameUriPDF(URI uri, String stringText, int txthash, byte[] inputByte)
	{
		Document valueToReturn = (Document)hashTable.get(uri);
		deletedHashTable.put(uri,hashTable.get(uri));//add it to a deleted hashtable
		boolean test = deleteUndoPut(uri);
		DocumentImpl newDocument = new DocumentImpl(uri, stringText, txthash, inputByte);
		hashTable.put(uri, newDocument);
		inputKey(stringText,newDocument);//put the key into the trie
		lambdaExpressionAlreadyExisting(uri);
		return valueToReturn.getDocumentTextHashCode();
	}
	private int txtHelper(byte[] inputByte,int txthash,URI uri)
	{
		String string = new String(inputByte).trim();
		txthash = string.hashCode();
		Document testDocument = (Document) hashTable.get(uri);
		if (testDocument !=null && testDocument.getDocumentTextHashCode() == txthash)
		{
			GenericCommand command = new GenericCommand(uri, ((j) -> doNothing(uri)));
			stack.push(command);
			return testDocument.getDocumentTextHashCode();
		}
		if (testDocument != null && testDocument.getDocumentTextHashCode() != txthash)//same uri
		{
			return sameUriDOC(uri, string, txthash);
		}
		DocumentImpl newDocument = new DocumentImpl(uri,string,txthash);
		hashTable.put(uri, newDocument);
		inputKey(string, newDocument);
		lambdaExpression(uri);
		return 0;
	}
	private int sameUriDOC(URI uri, String string, int txthash)
	{
		Document valueToReturn = (Document)hashTable.get(uri);
		deletedHashTable.put(uri,hashTable.get(uri));//add it to a deleted hashtable
		boolean test = deleteUndoPut(uri);
		DocumentImpl newDocument = new DocumentImpl(uri, string, txthash);
		hashTable.put(uri, newDocument);
		inputKey(string, newDocument);
		lambdaExpressionAlreadyExisting(uri);
		return valueToReturn.getDocumentTextHashCode();
	}
	private void lambdaExpression(URI uri)
	{
		GenericCommand command = new GenericCommand(uri, ((j) -> deleteUndoPut(uri)));
		stack.push(command);
	}
	private void lambdaExpressionAlreadyExisting(URI uri)
	{
		GenericCommand command = new GenericCommand(uri, ((j) -> deleteUndoPutAlreadyExisting(uri)));
		stack.push(command);
	}
	private boolean deleteUndoCommand(URI k)
	{
		Document doc = deletedHashTable.get(k);
		hashTable.put(k,doc);
		inputKey(doc.getDocumentAsTxt(), doc);
		deletedHashTable.put(k,null);//delete the old doc from the hashtable that was storing it
		return true;
	}
	private boolean deleteUndoPut(URI uri)
	{

		if (hashTable.get(uri) != null)
		{
			Document doc = hashTable.get(uri);
			deleteInputKey(doc.getDocumentAsTxt(), doc);
			hashTable.put(uri,null);//delete the old value
			return true;
		}
		else {
			return false;
		}
	}
	private boolean deleteUndoPutAlreadyExisting(URI uri)
	{
		if (hashTable.get(uri) != null)
		{
			Document doc1 = hashTable.get(uri);
			deleteInputKey(doc1.getDocumentAsTxt(), doc1);
			hashTable.put(uri,null);//delete the old value
			Document doc = deletedHashTable.get(uri);
			hashTable.put(uri,doc);//put the new one back in
			inputKey(doc.getDocumentAsTxt(),doc);
			deletedHashTable.put(uri,null);
			return true;
		}
		else {
			return false;
		}
	}
	public byte[] getDocumentAsPdf(URI uri) {
		Document document = (Document) hashTable.get(uri);
		if (document == null)//this is if no document exists
		{
			return null;
		}
		return document.getDocumentAsPdf();
	}

	public String getDocumentAsTxt(URI uri) {
		Document document = (Document) hashTable.get(uri);
		if (document == null)//this is if no document exists
		{
			return null;
		}
		return document.getDocumentAsTxt().trim();
	}
	public boolean deleteDocument(URI uri)
	{
		if (hashTable.get(uri) != null)
		{
			Document doc = hashTable.get(uri);
			deletedHashTable.put(uri,doc);//add it to a deleted hashtable
			deleteInputKey(doc.getDocumentAsTxt(), doc);
			hashTable.put(uri,null);//delete the old value
			GenericCommand command = new GenericCommand(uri, (k) -> deleteUndoCommand(uri));
			stack.push(command);
			return true;
		}
		else {
			GenericCommand command = new GenericCommand(uri, ((k) -> doNothing(uri)));
			stack.push(command);
			return false;
		}
	}

	public void undo() throws IllegalStateException
	{
		if (stack.size() == 0)
		{
			throw new IllegalStateException("There are no undo's to execute");
		}
		Undoable commandUndo = stack.pop();
		if (commandUndo instanceof CommandSet)
		{
			((CommandSet) commandUndo).undoAll();
		}
		commandUndo.undo();
	}
	public void undo(URI uri) throws IllegalStateException {
		if (stack.size() == 0) {throw new IllegalStateException("There are no undo's to execute");}
		StackImpl<Undoable> stackSwitch = new StackImpl<>();
		boolean tester = true;
		while (stack.size() != 0) {
			Undoable command = stack.peek();
			if (command instanceof CommandSet)
			{
				if (((CommandSet) command).containsTarget(uri)) {
					CommandUndo(command, uri);
					tester = false;
					break;
				}
				break;
			}
			if ((command.hashCode()) == (uri.hashCode())) {
				Undoable commandUri = stack.pop();
				commandUri.undo();
				tester = false;
				break;
			}
			stackSwitch.push(command);
			stack.pop();
		}
		while (stackSwitch.size() != 0) {
			Undoable reAddTOStack= stackSwitch.pop();
			stack.push(reAddTOStack);
		}
		if (tester == true)
		{ throw new IllegalStateException("The inputted uri was not found in the command stack"); }
	}
	private void CommandUndo(Undoable command, URI uri)
	{
		((CommandSet) command).undo(uri);
		if (((CommandSet) command).size() == 0)
		{
			stack.pop();
		}
	}
	public List<String> search(String keyword) {
		List<String> strings = new ArrayList<>();
		if (keyword == null){return strings;}
		List<Document> searchDoc = documentTrie.getAllSorted(editInputKey(keyword),new JonathanComparator(keyword));
		if (searchDoc == null)
		{
			return strings;
		}
		List<String> myList = new ArrayList<>();
		for (Document doc: searchDoc)
		{
			myList.add(doc.getDocumentAsTxt());
		}
		return myList;
	}
	public List<byte[]> searchPDFs(String keyword) {
		ArrayList<byte[]> byteList = new ArrayList<>();
		if (keyword == null){return byteList;}
		List<Document> jonathan = documentTrie.getAllSorted(editInputKey(keyword),new JonathanComparator(keyword));
		if (jonathan == null) { return byteList;}
		List<byte[]> byteListReturn = new ArrayList<>();
		for (Document doc: jonathan)
		{
			byteListReturn.add(doc.getDocumentAsPdf());
		}
		return byteListReturn;
	}
	public List<String> searchByPrefix(String prefix) {
		List<String> empty = new ArrayList<>();
		if (prefix == null){return empty;}
		List<Document> jonathan = documentTrie.getAllWithPrefixSorted(editInputKey(prefix),new JonathanComparatorPrefix(prefix));
		if (jonathan == null)
		{
			return empty;
		}
		List<String> myList = new ArrayList<>();
		for (Document doc: jonathan)
		{
			myList.add(doc.getDocumentAsTxt());
		}
		return myList;
	}
	public List<byte[]> searchPDFsByPrefix(String prefix) {
		ArrayList<byte[]> byteList = new ArrayList<>();
		if (prefix == null){return byteList;}
		List<Document> jonathan = documentTrie.getAllWithPrefixSorted(editInputKey(prefix),new JonathanComparatorPrefix(prefix));
		if (jonathan == null)
		{
			return byteList;
		}
		List<byte[]> byteListReturn = new ArrayList<>();
		for (Document doc: jonathan)
		{
			byte[] byteDoc = doc.getDocumentAsPdf();
			byteListReturn.add(byteDoc);
		}
		return byteListReturn;
	}
	public Set<URI> deleteAll(String key)
	{
		if (key == null){return deleteNothing();}
		Set<Document> deleteAll= documentTrie.deleteAll(editInputKey(key));
		if (deleteAll == null) {return deleteNothing();}
		Set<URI> setURI = new HashSet<>();
		for (Document doc: deleteAll)
		{
			setURI.add(doc.getKey());
		}
		CommandSet commandSet = new CommandSet();
		stack.push(commandSet);
		for (Document doc: deleteAll)
		{
			hashTableDeleteUndoDeleteAll.put(doc.getKey(), doc);
			commandSet.addCommand(new GenericCommand(doc.getKey(), (k)->undoDeleteAll(doc.getKey())));
			String txt = doc.getDocumentAsTxt();
			hashTable.put(doc.getKey(), null);
			for (String str : delString(txt))
			{
				if (Objects.equals(str, righthere(key)))
				{
					break;
				}
				documentTrie.delete(str, doc);
			}
		}
		return setURI;
	}
	private String righthere(String str)
	{
		String deleteCharacters = str.replaceAll("[^A-Za-z0-9 ]","");
		return deleteCharacters.toUpperCase();
	}

	private boolean undoDeleteAll(URI uri)
	{
		Document doc = hashTableDeleteUndoDeleteAll.get(uri);
		inputKey(doc.getDocumentAsTxt(), doc);
		hashTable.put(doc.getKey(),doc);
		hashTableDeleteUndoDeleteAll.put(doc.getKey(),null);
		return true;
	}
	private boolean undoDeletePrefix(URI uri)
	{
		Document doc = hashTableDeleteUndoDeletePrefix.get(uri);
		inputKey(doc.getDocumentAsTxt(), doc);
		hashTable.put(doc.getKey(),doc);
		hashTableDeleteUndoDeletePrefix.put(doc.getKey(),null);
		return true;
	}
	private List<String> delString(String str)
	{
		String deleteCharacters = str.replaceAll("[^A-Za-z0-9 ]","");
		String input = deleteCharacters.toUpperCase();
		Scanner scanner = new Scanner(input);
		List<String> listString = new ArrayList<>();
		while (scanner.hasNext())
		{
			listString.add(scanner.next());
		}
		return listString;
	}
	private Set<URI> deleteNothing()  {
		GenericCommand command = new GenericCommand("string", ((j) -> doNothing("string")));
		stack.push(command);
		Set<URI> empty = new HashSet<>();
		return empty;
	}
	public Set<URI> deleteAllWithPrefix(String prefix) {
		if (prefix == null){return deleteNothing();}
		Set<Document> deleteAllPrefix= documentTrie.deleteAllWithPrefix(editInputKey(prefix));
		if (deleteAllPrefix == null)
			return deleteNothing();
		Set<URI> setURI = new HashSet<>();
		for (Document doc: deleteAllPrefix)
		{
			setURI.add(doc.getKey());
		}
		CommandSet commandSet = new CommandSet();
		stack.push(commandSet);
		for (Document doc: deleteAllPrefix)
		{
			hashTableDeleteUndoDeletePrefix.put(doc.getKey(), doc);
			commandSet.addCommand(new GenericCommand(doc.getKey(), (k)->undoDeletePrefix(doc.getKey())));
			String txt = doc.getDocumentAsTxt();
			hashTable.put(doc.getKey(), null);
			for (String str : delString(txt))
			{
				if (documentTrie.delete(str, doc)== null)
				{
					break;
				}
			}
		}
		return setURI;
	}

	@Override
	public void setMaxDocumentCount(int limit) {

	}

	@Override
	public void setMaxDocumentBytes(int limit) {

	}


	protected Document getDocument(URI uri){
		Document document = hashTable.get(uri);
		if (document != null)
		{
			return document;
		}
		else{
			return null;
		}
	}
	private String editInputKey(String key)
	{
		String deleteCharacters =  key.replaceAll("[^A-Za-z0-9 ]","");
		String input = deleteCharacters.toUpperCase();
		return input;
	}
	private void inputKey(String key, Document doc)
	{
		String deleteCharacters = key.replaceAll("[^A-Za-z0-9 ]","");
		String input = deleteCharacters.toUpperCase();
		Scanner scanner = new Scanner(input);
		while (scanner.hasNext())
		{
			documentTrie.put(scanner.next(),doc);
		}
	}
	private void deleteInputKey(String key, Document doc)
	{
		String deleteCharacters = key.replaceAll("[^A-Za-z0-9 ]","");
		String input = deleteCharacters.toUpperCase();
		Scanner scanner = new Scanner(input);
		while (scanner.hasNext())
		{
			documentTrie.delete(scanner.next(),doc);
		}
	}
	private class JonathanComparator implements Comparator<Document>
	{
		private String word;
		private JonathanComparator(String word)
		{
			this.word = word.toUpperCase();
		}
		public int compare(Document firstDoc, Document secondDoc)
		{
			if (firstDoc.wordCount(word)< secondDoc.wordCount(word))
			{
				return 1;
			}
			return -1;
		}
	}
	private class JonathanComparatorPrefix implements Comparator<Document>
	{
		private String word;
		private JonathanComparatorPrefix(String prefix)
		{
			this.word = prefix.toUpperCase();
		}
		public int compare(Document firstDoc, Document secondDoc)
		{
			List<String> doc1 = putMe(word, firstDoc);
			List<String> doc2 = putMe(word, secondDoc);
			int result1 = 0;
			int result2 = 0;
			for (String word1 : doc1)
			{
				result1 = result1 + firstDoc.wordCount(word1);
			}
			for (String word2 : doc2)
			{
				result2 = result2 + secondDoc.wordCount(word2);
			}
			if (result1 > result2)
				return -1;
			else
			{
				return 1;
			}
		}
	}
	private List<String> putMe(String prefix, Document doc)
	{
		String deleteCharacters = doc.getDocumentAsTxt().replaceAll("[^A-Za-z0-9 ]","");
		String input = deleteCharacters.toUpperCase();
		Scanner scanner = new Scanner(input);
		while (scanner.hasNext())
		{
			putPrefix(scanner.next(),doc);
		}
		return getN(prefix);
	}
	private int alphabetSize = 256; // extended ASCII
	private Node root; // root of trie

	private class Node<Value>
	{
		private Value val;
		private Node[] links = new Node[alphabetSize];
	}
	private void putPrefix(String key, Document val)
	{
		this.root = put(this.root, editInputKey(key), val, 0);
	}
	private Node put(Node x, String key, Document val, int d)
	{
		if (x == null)
		{
			x = new Node();
		}
		if (d == key.length())
		{
			if (x.val == null)
			{
				x.val = val;
				return x;
			}
			return x;
		}
		char c = key.charAt(d);
		x.links[c] = put(x.links[c], editInputKey(key), val, d + 1);
		return x;
	}
	private List<String> getN(String prefix)
	{
		Queue<String> results = new ArrayDeque<>();
		//find node which represents the prefix
		Node x = this.get(this.root, prefix.toUpperCase(), 0);
		if (x == null)
		{
			return null;
		}
		if(x != null)
		{
			this.collect(x, new StringBuilder(prefix), results);
		}
		List<String> returnList = new ArrayList<>(results);
		return returnList;
	}
	private Node get(Node x, String key, int d)
	{
		if (x == null)
		{
			return null;
		}
		if (d == key.length())
		{
			return x;
		}
		char c = key.charAt(d);
		return get(x.links[c], key, d + 1);
	}
	private void collect(Node x, StringBuilder prefix, Queue<String> results)
	{
		if (x.val != null)
		{
			results.add(prefix.toString());
		}
		for (char c = 0; c < alphabetSize; c++)
		{
			if(x.links[c]!=null)
			{
				prefix.append(c);
				this.collect(x.links[c], prefix, results);

				prefix.deleteCharAt(prefix.length() - 1);
			}
		}
	}



}
