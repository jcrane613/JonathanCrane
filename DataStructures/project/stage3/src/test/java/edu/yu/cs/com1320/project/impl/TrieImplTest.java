package edu.yu.cs.com1320.project.impl;
import edu.yu.cs.com1320.project.Trie;
import edu.yu.cs.com1320.project.stage3.Document;
import edu.yu.cs.com1320.project.stage3.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage3.impl.DocumentStoreImpl;
import org.junit.Before;
import org.junit.Test;
import sun.awt.SunHints;

import javax.print.Doc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.util.*;

import static org.junit.Assert.*;

public class TrieImplTest {
	//API Testing

	@Test
	public void interfaceCount() {//tests that the class only implements one interface and its the correct one
		@SuppressWarnings("rawtypes")
		Class[] classes = TrieImpl.class.getInterfaces();
		assertTrue(classes.length == 1);
		assertTrue(classes[0].getName().equals("edu.yu.cs.com1320.project.Trie"));
	}

	@Test
	public void methodCount() {//tests for public and protected methods. the expected number should match the number of methods explicitly tested below save for the constructor
		Method[] methods = TrieImpl.class.getDeclaredMethods();
		int publicMethodCount = 0;
		for (Method method : methods) {
			if (Modifier.isPublic(method.getModifiers())) {
				if(!method.getName().equals("equals") && !method.getName().equals("hashCode")) {
					publicMethodCount++;
				}
			}
		}
		assertTrue(publicMethodCount == 6);
	}

	@Test
	public void fieldCount() {//tests for public or protected fields
		Field[] fields = TrieImpl.class.getFields();
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
		Class[] classes = TrieImpl.class.getClasses();
		assertTrue(classes.length == 0);
	}

	@Test
	public void constructorExists() {
		new TrieImpl<Document>();
	}

	@Test
	public void putExists() {
		try {
			new TrieImpl<String>().put("hello", "hello" );
		} catch (RuntimeException e) {}//catch any run time error this input might cause. This is meant to test method existence, not correctness
	}
	@Test
	public void getExists() {
		try {
			new TrieImpl<Document>().getAllSorted("hello", new JonathanComparator("wow") );
		} catch (RuntimeException e) {}//catch any run time error this input might cause. This is meant to test method existence, not correctness
	}
	@Test
	public void getPrefixExists() {
		try {
			new TrieImpl<Document>().getAllWithPrefixSorted("hello", new JonathanComparator("wow") );
		} catch (RuntimeException e) {}//catch any run time error this input might cause. This is meant to test method existence, not correctness
	}
	@Test
	public void deleteALlExists() {
		try {
			new TrieImpl<String>().deleteAll("hello" );
		} catch (RuntimeException e) {}//catch any run time error this input might cause. This is meant to test method existence, not correctness
	}
	@Test
	public void deleteExists() {
		try {
			new TrieImpl<String>().delete("hello" , "helo");
		} catch (RuntimeException e) {}//catch any run time error this input might cause. This is meant to test method existence, not correctness
	}
	@Test
	public void deletePrefixExists() {
		try {
			new TrieImpl<String>().deleteAllWithPrefix("h" );
		} catch (RuntimeException e) {}//catch any run time error this input might cause. This is meant to test method existence, not correctness
	}

	@Test
	public void stage2NoArgsConstructorExists(){
		try {
			new TrieImpl<>();
		} catch (RuntimeException e) {}
	}

	//variables to hold possible values for doc1
	private URI uri1;
	private String txt1;
	private byte[] pdfData1;
	private String pdfTxt1;
	//variables to hold possible values for doc4
	private URI uri4;
	private String txt4;
	private byte[] pdfData4;
	private String pdfTxt4;
	//variables to hold possible values for doc2
	private URI uri2;
	private String txt2;
	private byte[] pdfData2;
	private String pdfTxt2;

	//variables to hold possible values for doc3
	private URI uri3;
	private String txt3;
	private byte[] pdfData3;
	private String pdfTxt3;

	@Before
	public void init() throws Exception {
		//init possible values for doc1
		this.uri1 = new URI("http://edu.yu.cs/com1320/project/fourth");
		this.txt1 = "Hey how &*daare 637You-e doing today, Ty*&pical this is a grea fourtneen surprise howdaa";
		this.pdfTxt1 = "This is some PDF text for daphoc1, hat daip to daty Adobe.";
		this.pdfData1 = Utils.textToPdfData(this.pdfTxt1);
		//init possible values for doc4
		this.uri4 = new URI("https://github.com/Yeshiva-University-CS/DataStructuresProjectSpring2020/thirdplace");
		this.txt4 = "This is some PDF text for doc1, fourtneen hat tip to Adobe.";
		this.pdfTxt4 = "A different datest  dang diphckle doob phirty dors ding dum ";
		this.pdfData4 = Utils.textToPdfData(this.pdfTxt4);

		//init possible values for doc2
		this.uri2 = new URI("http://edu.yu.cs/com1320/project/firstplace");
		this.txt2 = "daces dacha dadas fourtneen daddy &*typical&* dados daffs typi*(cal daffy dagga TypICal";
		this.pdfTxt2 = "PDF content for doc2: PDF format was opened in 20008.";
		this.pdfData2 = Utils.textToPdfData(this.pdfTxt2);

		//init possible values for doc3
		this.uri3 = new URI("http://youtube.com/secondplace");
		this.txt3 = "78Typical Daoc for a 8typical daoc fourtneen 9typical dabba this is thee most three two twere.";
		this.pdfTxt3 = "PDF content for doc2: PDF format was opened in 2008.";
		this.pdfData3 = Utils.textToPdfData(this.pdfTxt3);
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
	private String editInputKey(String key)
	{
		String deleteCharacters =  key.replaceAll("[^A-Za-z0-9 ]","");
		String input = deleteCharacters.toUpperCase();
		return input;
	}
	private void inputKey(String key, Document doc, TrieImpl documentTrie)
	{
		String deleteCharacters = key.replaceAll("[^A-Za-z0-9 ]","");
		String input = deleteCharacters.toUpperCase();
		Scanner scanner = new Scanner(input);
		while (scanner.hasNext())
		{
			documentTrie.put(scanner.next(),doc);
		}
	}
	@Test
	public void test2()
	{
		TrieImpl<Document> documentTrie = new TrieImpl<>();
		Document doc = new DocumentImpl(uri1, txt1, txt1.hashCode());
		inputKey(txt1, doc, documentTrie);
		Document doc4 = new DocumentImpl(uri4, txt4, txt4.hashCode());
		inputKey(txt4, doc4, documentTrie);
		Document doc2 = new DocumentImpl(uri2, txt2, txt2.hashCode());
		inputKey(txt2, doc2, documentTrie);
		Document doc3 = new DocumentImpl(uri3, txt3, txt3.hashCode());
		inputKey(txt3, doc3, documentTrie);
		assertEquals(new ArrayList<Document>(), documentTrie.getAllSorted("trialertooo", new JonathanComparator("trialertooo")));
		assertNull(documentTrie.delete(null, doc4));
		assertNull(documentTrie.delete("butter", null));
	}
	@Test//delete doesn't exist
	public void delete()
	{
		TrieImpl<Document> documentTrie = new TrieImpl<>();
		Document doc = new DocumentImpl(uri1, txt1, txt1.hashCode());
		inputKey(txt1, doc, documentTrie);
		Document doc4 = new DocumentImpl(uri4, txt4, txt4.hashCode());
		inputKey(txt4, doc4, documentTrie);
		Document doc2 = new DocumentImpl(uri2, txt2, txt2.hashCode());
		inputKey(txt2, doc2, documentTrie);
		Document doc3 = new DocumentImpl(uri3, txt3, txt3.hashCode());
		inputKey(txt3, doc3, documentTrie);
		assertNull(documentTrie.delete("crazy", null));
		assertNull(documentTrie.delete("2008", doc));
		assertNull(documentTrie.delete(null, doc));
		assertEquals(doc, documentTrie.delete("fourtneen", doc));
		Set<Document> go = new HashSet<>();
		go.add(doc4);
		go.add(doc3);
		documentTrie.delete("fourtneen", doc2);

		assertEquals(go, documentTrie.deleteAll("fourtneen"));
	}
	/*
	@Test
	public void test1()
	{
		TrieImpl<Document> documentTrie = new TrieImpl<>();
		Document doc = new DocumentImpl(uri1, txt1, txt1.hashCode());
		inputKey(txt1, doc, documentTrie);
		Document doc4 = new DocumentImpl(uri4, txt4, txt4.hashCode());
		inputKey(txt4, doc4, documentTrie);
		Document doc2 = new DocumentImpl(uri2, txt2, txt2.hashCode());
		inputKey(txt2, doc2, documentTrie);
		Document doc3 = new DocumentImpl(uri3, txt3, txt3.hashCode());
		inputKey(txt3, doc3, documentTrie);
		System.out.println("typical");
		for (Document docN:(documentTrie.getAllSorted("typical", new JonathanComparator("typical"))))
		{
			System.out.println(docN.getKey());
		}
		System.out.println("d");
		for (Document docM:(documentTrie.getAllWithPrefixSorted("d", new JonathanComparatorPrefix("d"))))
		{
			System.out.println(docM.getKey());
		}
		System.out.println("d- delete");
		System.out.println();
		for (Document docT:(documentTrie.deleteAllWithPrefix("d")))
		{
			System.out.println(docT.getKey());
		}
		System.out.println("dagga");
		for (Document docM:(documentTrie.getAllSorted("dagga", new JonathanComparatorPrefix("dagga"))))
		{
			System.out.println(docM.getKey());
		}
		System.out.println("prefix get typ");
		for (Document docM:(documentTrie.getAllWithPrefixSorted("typ", new JonathanComparatorPrefix("typ"))))
		{
			System.out.println(docM.getKey());
		}
		//finish to make sure all the deletes work
		for (Document docM:(documentTrie.deleteAll("typ")))
		{
			System.out.println(docM.getKey());
		}

	}
*/
}