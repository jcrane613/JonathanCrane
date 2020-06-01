package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.*;
import edu.yu.cs.com1320.project.impl.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.DocumentStore;


public class DocumentStoreImpl implements DocumentStore {
	private StackImpl<Undoable> stack = new StackImpl<>();
	private TrieImpl<URI> documentTrie = new TrieImpl<>();//converted to only contain URI
	private BTreeImpl bTree;//insta at a later point
	private int MaxDocumentCount = Integer.MAX_VALUE ;
	private int	CurrentDocCount = 0;
	private int MaxDocumentBytes = Integer.MAX_VALUE;
	private int	CurrentBytes = 0;
	private MinHeapImpl<MinheapCompartor> minHeap = new MinHeapImpl<>();//converted to only contain URI
	private File basedir;
	private HashMap<URI, MinheapCompartor> hashMap =  new HashMap<>();

	public DocumentStoreImpl() {
		String current = System.getProperty("user.dir");
		Path path = Paths.get(current);
		this.basedir = path.toFile();
		DocumentPersistenceManager per = null;
		this.bTree = new BTreeImpl();
		try
		{
			per = new DocumentPersistenceManager(basedir);
			bTree.setPersistenceManager(per);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		try
		{
			bTree.put(new URI(""), null);
		} catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
	}
	public DocumentStoreImpl(File basDir)  {
		this.basedir = basDir;
		DocumentPersistenceManager per = null;
		this.bTree = new BTreeImpl();
		try
		{
			per = new DocumentPersistenceManager(basDir);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		bTree.setPersistenceManager(per);
		try
		{
			bTree.put(new URI(""), null);
		} catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
	}
	public int putDocument(InputStream input, URI uri, DocumentFormat format) {
		if (uri == null) { throw new IllegalArgumentException("The uri was null");}
		if (format == null){throw new IllegalArgumentException("The format was null");}
		if (input == null)
		{
			return checkURINull(uri);
		}
		while (CurrentDocCount > MaxDocumentCount || CurrentBytes > MaxDocumentBytes){ removeTooManyDoc(); }
		int txthash = 0;
		byte[] inputByte = convertToByteArray(input);
		if (format == DocumentFormat.PDF)
		{
			while (CurrentDocCount > MaxDocumentCount || CurrentBytes > MaxDocumentBytes){ removeTooManyDoc();}
			return pdfHelper(inputByte,txthash,uri);
		}
		if (format == DocumentFormat.TXT)
		{
			while (CurrentDocCount > MaxDocumentCount || CurrentBytes > MaxDocumentBytes){ removeTooManyDoc();}
			return txtHelper(inputByte, txthash, uri);
		}
		while (CurrentDocCount > MaxDocumentCount || CurrentBytes > MaxDocumentBytes){removeTooManyDoc();}
		return txthash;
	}
	private void removeTooManyDoc()  {
		Document doc = null;
		try
		{
			 doc = removeMinHeap();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		deleteHeapWithoutReheapify(doc); //gets rid of the bytes and count
		/*
		StackImpl stackSwitch = new StackImpl();
		while(stack.size() != 0)//gets rid of commands in stack
		{
			boolean skip = false;
			Undoable command = stack.peek();
			if (command instanceof CommandSet)
				if (((CommandSet) command).containsTarget(doc.getKey()))
				{
					while (((CommandSet) command).iterator().hasNext())
					{
						URI uri2 = (URI) ((CommandSet) command).iterator().next();
						if (uri2 == doc.getKey())
						{
							((CommandSet) command).iterator().remove();
							break;
						}
					}
					break;
				}
			if (command instanceof GenericCommand)
				if ((command.hashCode()) == (doc.getKey().hashCode()))
				{
					stack.pop();//get rid of this command,
					skip = true; //don't add anything to the temp stake
				}
			if (skip == false)
			{
				stackSwitch.push(stack.pop());
			}
		}
		while (stackSwitch.size() != 0) {
			stack.push((Undoable) stackSwitch.pop());
		}
		*/
	}
	private boolean deleteHeapWithoutReheapify(Document doc)
	{
		CurrentBytes -= getBytes(doc);
		CurrentDocCount--;
		//deleteInputKey(doc.getDocumentAsTxt(), doc.getKey());
		return true;
	}
	private Document removeMinHeap ()
	{
		MinheapCompartor minheapCompartor = minHeap.removeMin();//takes it out of the heap, and decides which will be deleted
		Document doc = (Document) bTree.get(minheapCompartor.getUri());
		try
		{
			bTree.moveToDisk(minheapCompartor.getUri());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		hashMap.remove(minheapCompartor.getUri());
		return doc;
	}
	private int checkURINull(URI uri)
	{
		if (bTree.get(uri) == null) //the documentation says it returns null if the key was not found
		{
			GenericCommand command = new GenericCommand(uri, ((j) -> doNothing(uri)));
			stack.push(command);
			return 0;
		}
		Document valueToReturn = (Document) bTree.get(uri);
		deleteDocument(uri);
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
			Document testDocument = (Document) bTree.get(uri);
			if (testDocument != null && testDocument.getDocumentTextHashCode() == txthash)//same doc
			{
				GenericCommand command = new GenericCommand(uri, ((j) -> doNothing(uri)));
				stack.push(command);
				testDocument.setLastUseTime(System.nanoTime());
				MinheapCompartor minheapCompartor = hashMap.get(uri);
				minheapCompartor.setLastTime(System.nanoTime());
				minHeap.reHeapify(minheapCompartor);
				return testDocument.getDocumentTextHashCode();
			}
			if (testDocument != null && testDocument.getDocumentTextHashCode() != txthash)//same uri
			{
				return sameUriPDF(uri,stringText,txthash,inputByte);
			}
			DocumentImpl newDocument = new DocumentImpl(uri, stringText, txthash, inputByte);
			insertionDoc(newDocument, uri, stringText);
			thisDoc.close();
			return 0;
		}
		catch (Exception e) { e.printStackTrace(); }
		return 0;
	}
	private int sameUriPDF(URI uri, String stringText, int txthash, byte[] inputByte)   {
		Document valueToReturn = (Document)bTree.get(uri);
		deleteUndoPut(valueToReturn);
		DocumentImpl newDocument = new DocumentImpl(uri, stringText, txthash, inputByte);
		bTree.put(newDocument.getKey(), newDocument);
		inputKey(stringText,uri);//put the key into the trie
		newDocument.setLastUseTime(System.nanoTime());//set the time of the document
		MinheapCompartor minheapCompartor = new MinheapCompartor(uri,System.nanoTime());
		minHeap.insert(minheapCompartor);
		hashMap.put(uri,minheapCompartor);
		CurrentBytes += getBytes(newDocument);
		lambdaExpressionAlreadyExisting(valueToReturn.getKey(), valueToReturn);
		while (CurrentDocCount > MaxDocumentCount || CurrentBytes > MaxDocumentBytes){removeTooManyDoc();}
		return valueToReturn.getDocumentTextHashCode();
	}
	private boolean deleteUndoPut(Document doc)
	{
		CurrentBytes -= getBytes(doc);
		CurrentDocCount--;
		deleteInputKey(doc.getDocumentAsTxt(), doc.getKey());
		try
		{
			removeDocHeap(doc);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}
	private int txtHelper(byte[] inputByte,int txthash,URI uri) {
		String string = new String(inputByte).trim();
		txthash = string.hashCode();
		Document testDocument = (Document) bTree.get(uri);
		if (testDocument !=null && testDocument.getDocumentTextHashCode() == txthash)
		{
			GenericCommand command = new GenericCommand(uri, ((j) -> doNothing(uri)));
			stack.push(command);
			testDocument.setLastUseTime(System.nanoTime());//set the time of the document
			MinheapCompartor minheapCompartor = hashMap.get(uri);
			minheapCompartor.setLastTime(System.nanoTime());
			minHeap.reHeapify(minheapCompartor);
			return testDocument.getDocumentTextHashCode();
		}
		if (testDocument != null && testDocument.getDocumentTextHashCode() != txthash)//same uri
		{
			return sameUriDOC(uri, string, txthash);
		}
		DocumentImpl newDocument = new DocumentImpl(uri,string,txthash);
		insertionDoc(newDocument, uri, string);
		return 0;
	}
	private void insertionDoc(Document newDocument, URI uri, String string)  {
		bTree.put(uri, newDocument);
		inputKey(string, uri);
		CurrentDocCount++;
		CurrentBytes += getBytes(newDocument);
		newDocument.setLastUseTime(System.nanoTime());//set the time of the document
		MinheapCompartor minheapCompartor = new MinheapCompartor(uri,System.nanoTime());
		minHeap.insert(minheapCompartor);
		hashMap.put(uri, minheapCompartor);
		lambdaExpression(uri, newDocument);
		while (CurrentDocCount > MaxDocumentCount || CurrentBytes > MaxDocumentBytes){removeTooManyDoc();}
	}

	private int sameUriDOC(URI uri, String string, int txthash) {
		Document valueToReturn = (Document)bTree.get(uri);
		//deletedHashTable.put(uri,valueToReturn);//add it to a deleted hashtable
		deleteUndoPut(valueToReturn);
		DocumentImpl newDocument = new DocumentImpl(uri, string, txthash);
		bTree.put(uri, newDocument);
		inputKey(string, uri);
		newDocument.setLastUseTime(System.nanoTime());
		MinheapCompartor minheapCompartor = new MinheapCompartor(uri,System.nanoTime());
		minHeap.insert(minheapCompartor);
		hashMap.put(uri, minheapCompartor);
		CurrentBytes += getBytes(newDocument);
		lambdaExpressionAlreadyExisting(valueToReturn.getKey(), valueToReturn);
		while (CurrentDocCount > MaxDocumentCount || CurrentBytes > MaxDocumentBytes){removeTooManyDoc();}
		return valueToReturn.getDocumentTextHashCode();
	}
	private void lambdaExpression( URI uri, Document doc)
	{
		GenericCommand command = new GenericCommand(uri, ((k) -> deleteUndoPut( doc)));
		stack.push(command);
	}
	private void lambdaExpressionAlreadyExisting(URI uri, Document doc)
	{
		GenericCommand command = new GenericCommand(uri, ((j) -> deleteUndoPutAlreadyExisting(uri, doc)));
		stack.push(command);
	}
	private boolean deleteUndoCommand(Document doc)  {
		bTree.put(doc.getKey(), doc);
		inputKey(doc.getDocumentAsTxt(), doc.getKey());
		doc.setLastUseTime(System.nanoTime());
		MinheapCompartor minheapCompartor = new MinheapCompartor(doc.getKey(),System.nanoTime());
		minHeap.insert(minheapCompartor);
		hashMap.put(doc.getKey(), minheapCompartor);
		CurrentDocCount++;
		CurrentBytes += getBytes(doc);
		while (CurrentDocCount > MaxDocumentCount || CurrentBytes > MaxDocumentBytes){removeTooManyDoc();}
		return true;
	}

	private void removeDocHeap(Document doc) {
		doc.setLastUseTime(Long.MIN_VALUE);
		MinheapCompartor min = hashMap.get(doc.getKey());
		min.setLastTime(Long.MIN_VALUE);
		minHeap.removeMin();
		hashMap.remove(doc.getKey());
	}
	private boolean deleteUndoPutAlreadyExisting(URI uri, Document DelDoc) {
		Document currentDoc = (Document) bTree.get(uri);
		deleteInputKey(currentDoc.getDocumentAsTxt(), currentDoc.getKey());
		removeDocHeap(currentDoc); //I feel like this should go there, but it wasn't there before
		CurrentBytes -= getBytes(currentDoc);//delete the old bytes
		bTree.put(uri, DelDoc);//put the new one back in
		CurrentBytes += getBytes(DelDoc);//put the new bytes in
		inputKey(DelDoc.getDocumentAsTxt(),uri);
		DelDoc.setLastUseTime(System.nanoTime());
		MinheapCompartor minheapCompartor = new MinheapCompartor(uri,System.nanoTime());
		minHeap.insert(minheapCompartor);
		hashMap.put(uri,minheapCompartor);
		while (CurrentDocCount > MaxDocumentCount || CurrentBytes > MaxDocumentBytes){removeTooManyDoc();}
		return true;
	}
	public byte[] getDocumentAsPdf(URI uri) {
		while (CurrentDocCount > MaxDocumentCount || CurrentBytes > MaxDocumentBytes){removeTooManyDoc();}
		Document document = (Document) bTree.get(uri);
		if (document == null)//this is if no document exists
		{
			return null;
		}
		document.setLastUseTime(System.nanoTime());
		rehapifyMinHeap(uri);
		return document.getDocumentAsPdf();
	}

	public String getDocumentAsTxt(URI uri) {
		while (CurrentDocCount > MaxDocumentCount || CurrentBytes > MaxDocumentBytes){ removeTooManyDoc();}
		Document document = (Document) bTree.get(uri);
		if (document == null)//this is if no document exists
		{
			return null;
		}
		document.setLastUseTime(System.nanoTime());
		rehapifyMinHeap(uri);
		return document.getDocumentAsTxt().trim();
	}
	public boolean deleteDocument(URI uri)
	{

		Document doc = (Document) bTree.get(uri);
		if (doc == null)
			{
				GenericCommand command = new GenericCommand(uri, ((k) -> doNothing(uri)));
				stack.push(command);
				return false;
			}
		deleteInputKey(doc.getDocumentAsTxt(), doc.getKey());
		bTree.put(uri,null);//delete the old value
		GenericCommand command = new GenericCommand(uri, (k) -> deleteUndoCommand(doc));
		stack.push(command);
		try
		{
			removeDocHeap(doc);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		CurrentDocCount--;
		CurrentBytes -= getBytes(doc);
		return true;
	}

	public void undo() throws IllegalStateException
	{
		while (CurrentDocCount > MaxDocumentCount || CurrentBytes > MaxDocumentBytes){ removeTooManyDoc(); }
		if (stack.size() == 0)
		{
			throw new IllegalStateException("There are no undo's to execute");
		}
		Undoable commandUndo = stack.pop();
		if (commandUndo instanceof CommandSet)
		{
			((CommandSet) commandUndo).undoAll();
		}
		if (commandUndo instanceof GenericCommand)
		{
			commandUndo.undo();
		}
	}
	public void undo(URI uri) throws IllegalStateException {
		while (CurrentDocCount > MaxDocumentCount || CurrentBytes > MaxDocumentBytes){ removeTooManyDoc();}
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
			}
			if (command instanceof GenericCommand && (command.hashCode()) == (uri.hashCode())) {
				Undoable commandUri = stack.pop();
				commandUri.undo();
				tester = false;
				break;
			}
			stackSwitch.push(stack.pop());
		}
		while (stackSwitch.size() != 0) {
			stack.push(stackSwitch.pop());
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
		while (CurrentDocCount > MaxDocumentCount || CurrentBytes > MaxDocumentBytes){ removeTooManyDoc();}
		List<String> strings = new ArrayList<>();
		if (keyword == null){return strings;}
		List<URI> searchURI = documentTrie.getAllSorted(editInputKey(keyword),new JonathanComparator(keyword));
		List<Document> searchDoc = new ArrayList<>();
		for (URI uri : searchURI)
		{
			searchDoc.add((Document) bTree.get(uri));
		}
		long currentTime = System.nanoTime();
		if (searchDoc == null)
		{
			return strings;
		}
		List<String> myList = new ArrayList<>();
		for (Document doc: searchDoc)
		{
			myList.add(doc.getDocumentAsTxt());
			doc.setLastUseTime(currentTime);
			rehapifyMinHeap(doc.getKey());
		}
		return myList;
	}
	private void rehapifyMinHeap(URI uri)
	{
		if (!hashMap.containsKey(uri))
		{
			MinheapCompartor minheapCompartor = new MinheapCompartor(uri, System.nanoTime());
			hashMap.put(uri, minheapCompartor);
			minHeap.insert(minheapCompartor);
			CurrentDocCount++;
			CurrentBytes+=getBytes((Document) bTree.get(uri));
			while (CurrentDocCount > MaxDocumentCount || CurrentBytes > MaxDocumentBytes){ removeTooManyDoc();}
			return;
		}
		if (hashMap.containsKey(uri))
		{
			MinheapCompartor minheapCompartor = hashMap.get(uri);
			minheapCompartor.setLastTime(System.nanoTime());
			minHeap.reHeapify(minheapCompartor);
		}
	}
	public List<byte[]> searchPDFs(String keyword) {
		while (CurrentDocCount > MaxDocumentCount || CurrentBytes > MaxDocumentBytes){removeTooManyDoc();}
		ArrayList<byte[]> byteList = new ArrayList<>();
		if (keyword == null){return byteList;}
		List<URI> searchURI = documentTrie.getAllSorted(editInputKey(keyword),new JonathanComparator(keyword));
		List<Document> searchDoc = new ArrayList<>();
		for (URI uri : searchURI)
		{
			searchDoc.add((Document) bTree.get(uri));
		}
		if (searchURI == null) { return byteList;}
		List<byte[]> byteListReturn = new ArrayList<>();
		long currentTime = System.nanoTime();
		for (Document doc: searchDoc)
		{
			byteListReturn.add(doc.getDocumentAsPdf());
			doc.setLastUseTime(currentTime);
			rehapifyMinHeap(doc.getKey());
		}
		return byteListReturn;
	}
	public List<String> searchByPrefix(String prefix) {
		while (CurrentDocCount > MaxDocumentCount || CurrentBytes > MaxDocumentBytes){removeTooManyDoc();}
		List<String> empty = new ArrayList<>();
		if (prefix == null){return empty;}
		List<URI> searchURI = documentTrie.getAllWithPrefixSorted(editInputKey(prefix),new JonathanComparatorPrefix(prefix));
		if (searchURI == null)
		{
			return empty;
		}
		List<Document> searchDoc = new ArrayList<>();
		for (URI uri : searchURI)
		{
			searchDoc.add((Document) bTree.get(uri));
		}
		List<String> myList = new ArrayList<>();
		long currentTime = System.nanoTime();
		for (Document doc: searchDoc)
		{
			myList.add(doc.getDocumentAsTxt());
			doc.setLastUseTime(currentTime);
			rehapifyMinHeap(doc.getKey());
		}
		return myList;
	}
	public List<byte[]> searchPDFsByPrefix(String prefix) {
		while (CurrentDocCount > MaxDocumentCount || CurrentBytes > MaxDocumentBytes){ removeTooManyDoc(); }
		ArrayList<byte[]> byteList = new ArrayList<>();
		if (prefix == null){return byteList;}
		List<URI> searchURI = documentTrie.getAllWithPrefixSorted(editInputKey(prefix),new JonathanComparatorPrefix(prefix));
		if (searchURI == null)
		{
			return byteList;
		}
		List<Document> searchDoc = new ArrayList<>();
		for (URI uri : searchURI)
		{
			searchDoc.add((Document) bTree.get(uri));
		}
		List<byte[]> byteListReturn = new ArrayList<>();
		long currentTime = System.nanoTime();
		for (Document doc: searchDoc)
		{
			byteListReturn.add(doc.getDocumentAsPdf());
			doc.setLastUseTime(currentTime);
			rehapifyMinHeap(doc.getKey());
		}
		return byteListReturn;
	}
	public Set<URI> deleteAll(String key)
	{
		if (key == null){return deleteNothing();}
		Set<URI> deleteAllURI= documentTrie.deleteAll(editInputKey(key));
		if (deleteAllURI == null) {return deleteNothing();}
		Set<Document> searchDoc = new HashSet<>();
		for (URI uri : deleteAllURI)
		{
			searchDoc.add((Document) bTree.get(uri));
		}
		CommandSet commandSet = new CommandSet();
		stack.push(commandSet);
		for (Document doc: searchDoc)
		{
			deleteDoc(doc, commandSet);
		}
		return deleteAllURI;
	}
	public Set<URI> deleteAllWithPrefix(String prefix){
		if (prefix == null){return deleteNothing();}
		Set<URI> deleteAllPrefixURI= documentTrie.deleteAllWithPrefix(editInputKey(prefix));
		if (deleteAllPrefixURI == null)
			return deleteNothing();
		Set<Document> searchDoc = new HashSet<>();
		for (URI uri : deleteAllPrefixURI)
		{
			searchDoc.add((Document) bTree.get(uri));
		}
		CommandSet commandSet = new CommandSet();
		stack.push(commandSet);
		for (Document doc: searchDoc)
		{
			deleteDoc(doc, commandSet);
		}
		return deleteAllPrefixURI;
	}
	private String righthere(String str)
	{
		String deleteCharacters = str.replaceAll("[^A-Za-z0-9 ]","");
		return deleteCharacters.toUpperCase();
	}

	private boolean undoDeleteAll(Document doc)
	{
		inputKey(doc.getDocumentAsTxt(), doc.getKey());
		bTree.put(doc.getKey(),doc);
		doc.setLastUseTime(System.nanoTime());
		MinheapCompartor minheapCompartor = new MinheapCompartor(doc.getKey(), System.nanoTime());
		hashMap.put(doc.getKey(), minheapCompartor);
		minHeap.insert(minheapCompartor);
		CurrentBytes+=getBytes(doc);
		CurrentDocCount++;
		while (CurrentDocCount > MaxDocumentCount || CurrentBytes > MaxDocumentBytes){ removeTooManyDoc();}
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
	private void deleteDoc (Document doc, CommandSet commandSet)
	{
		commandSet.addCommand(new GenericCommand(doc.getKey(), (k)->undoDeleteAll(doc)));
		String txt = doc.getDocumentAsTxt();
		bTree.put(doc.getKey(), null);
		if (hashMap.containsKey(doc.getKey()))//these are the live documents, if it isn't a live document it is irreleveant
		{
			CurrentDocCount--;
			CurrentBytes-=getBytes(doc);
		}
		try
		{
			removeDocHeap(doc);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		for (String str : delString(txt))
		{
			documentTrie.delete(str, doc.getKey());
		}
	}
	private boolean TesterinDisk(URI uri)
	{
		String finaldir = basedir+File.separator+uri.getHost()+uri.getPath()+".json";
		File file = new File(finaldir);
		if (file.exists())
			return true;
		else return false;
	}
	@Override
	public void setMaxDocumentCount(int limit)
	{
		this.MaxDocumentCount = limit;
		while (CurrentDocCount > MaxDocumentCount || CurrentBytes > MaxDocumentBytes){ removeTooManyDoc();}
	}

	@Override
	public void setMaxDocumentBytes(int limit)
	{
		this.MaxDocumentBytes = limit;
		while (CurrentDocCount > MaxDocumentCount || CurrentBytes > MaxDocumentBytes){ removeTooManyDoc(); }
	}
	private int getBytes(Document doc)
	{
		return doc.getDocumentAsTxt().getBytes().length + doc.getDocumentAsPdf().length;
	}

	protected Document getDocument(URI uri){
		if (!hashMap.containsKey(uri))//if true, it is in disk and not memory, so should return false
		{
			return null;
		}
		return (Document) bTree.get(uri);

	}
	private String editInputKey(String key)
	{
		String deleteCharacters =  key.replaceAll("[^A-Za-z0-9 ]","");
		String input = deleteCharacters.toUpperCase();
		return input;
	}
	private void inputKey(String key, URI uri)
	{
		String deleteCharacters = key.replaceAll("[^A-Za-z0-9 ]","");
		String input = deleteCharacters.toUpperCase();
		Scanner scanner = new Scanner(input);
		while (scanner.hasNext())
		{
			documentTrie.put(scanner.next(),uri);
		}
	}
	private void deleteInputKey(String key, URI uri)
	{
		String deleteCharacters = key.replaceAll("[^A-Za-z0-9 ]","");
		String input = deleteCharacters.toUpperCase();
		Scanner scanner = new Scanner(input);
		while (scanner.hasNext())
		{
			documentTrie.delete(scanner.next(),uri);
		}
	}
	private class JonathanComparator implements Comparator<URI>
	{
		private String word;
		private JonathanComparator(String word)
		{
			this.word = word.toUpperCase();
		}
		public int compare(URI firstDoc, URI secondDoc)
		{
			if (((Document)bTree.get(firstDoc)).wordCount(word)< ((Document)bTree.get(firstDoc)).wordCount(word))
			{
				return 1;
			}
			return -1;
		}
	}
	private class JonathanComparatorPrefix implements Comparator<URI>
	{
		private String word;
		private JonathanComparatorPrefix(String prefix)
		{
			this.word = prefix.toUpperCase();
		}
		public int compare(URI uri1, URI uri2)
		{
			Document firstDoc = (Document) bTree.get(uri1);
			Document secondDoc = (Document) bTree.get(uri2);
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

	private class MinheapCompartor implements Comparable {
		private long lastTime;
		private URI uri;
		private MinheapCompartor (URI uri, long l )
		{
			this.uri = uri;
			this.lastTime = l;
		}
		private long getLastTime()
		{
			return lastTime;
		}
		private void setLastTime(long lo)
		{
			this.lastTime = lo;
		}
		private URI getUri()
		{
			return uri;
		}

		@Override
		public int compareTo(Object o) {

			if (getLastTime() == ((MinheapCompartor)o).getLastTime() )
				return 0;
			if (getLastTime() > ((MinheapCompartor)o).getLastTime())
				return 1;
			else return -1;
		}

		@Override
		public boolean equals(Object o) {
			if( uri.compareTo(((MinheapCompartor)o).getUri()) == 0 )
				return true;
			else return false;

		}
	}
}