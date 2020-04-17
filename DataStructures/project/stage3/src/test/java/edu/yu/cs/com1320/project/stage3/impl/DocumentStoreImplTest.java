package edu.yu.cs.com1320.project.stage3.impl;

import edu.yu.cs.com1320.project.Trie;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.impl.TrieImplTest;
import edu.yu.cs.com1320.project.impl.Utils;
import edu.yu.cs.com1320.project.stage3.Document;
import edu.yu.cs.com1320.project.stage3.DocumentStore;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Before;
import org.junit.Test;

import javax.print.Doc;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.util.*;

import static org.junit.Assert.*;

public class DocumentStoreImplTest {
	private URI uri5;
	private String txt5;
	private byte[] pdfData5;
	private String pdfTxt5;
	//variables to hold possible values for doc4
	private URI uri4;
	private String txt4;
	private byte[] pdfData4;
	private String pdfTxt4;
	//variables to hold possible values for doc2
	private URI uri6;
	private String txt6;
	private byte[] pdfData6;
	private String pdfTxt6;

	//variables to hold possible values for doc3
	private URI uri3;
	private String txt3;
	private byte[] pdfData3;
	private String pdfTxt3;

	@Before
	public void init() throws Exception {
		//init possible values for doc1
		this.uri5 = new URI("http://edu.yu.cs/com1320/project/doc1");
		this.txt5 = "Hey how are 637You-e doing today, Typical this is a grea surprise howdaa";
		this.pdfTxt5 = "There hope is haa hope";
		this.pdfData5 = textToPdfData(this.pdfTxt5);
		//init possible values for doc4
		this.uri4 = new URI("https://github.com/Yeshiva-University-CS/DataStructuresProjectSpring2020");
		this.txt4 = "A ifferent test dung dickle doob dors ding test dum Typical";
		this.pdfTxt4 = "There hope is haa hope that hopeone will hagrow firstonenenenen";
		this.pdfData4 = textToPdfData(this.pdfTxt4);

		//init possible values for doc2
		this.uri6 = new URI("http://edu.yu.cs/com1320/project/doc2");
		this.txt6 = "daces dacha dadas daddy dados test daffs daffy dagga Typical";
		this.pdfTxt6 = "PDF hacontent hope for hope doc2: PhaDF hope format was hope opened in 2008.";
		this.pdfData6 = textToPdfData(this.pdfTxt6);

		//init possible values for doc3
		this.uri3 = new URI("http://youtube.com");
		this.txt3 = "Typical Doc for a typical doc this is thee most three two twere.";
		this.pdfTxt3 = "I hape I hope am able to fix some of my grades, I want to do the best";
		this.pdfData3 = textToPdfData(this.pdfTxt3);
	}
	public static byte[] textToPdfData(String text) throws IOException {
		//setup document and page
		PDDocument document = new PDDocument();
		PDPage page = new PDPage();
		document.addPage(page);
		PDPageContentStream content = new PDPageContentStream(document, page);
		content.beginText();
		PDFont font = PDType1Font.HELVETICA_BOLD;
		content.setFont(font, 10);
		content.newLineAtOffset(20, 20);
		//add text
		content.showText(text);
		content.endText();
		content.close();
		//save to ByteArrayOutputStream
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		document.save(outputStream);
		document.close();
		return outputStream.toByteArray();
	}
	@Test
	public void testPutTXTDocumentWithUndo() {
		DocumentStore store = new DocumentStoreImpl();
		store.putDocument(new ByteArrayInputStream(this.txt5.getBytes()), this.uri5, DocumentStore.DocumentFormat.TXT);
		store.putDocument(new ByteArrayInputStream(this.txt6.getBytes()), this.uri6, DocumentStore.DocumentFormat.TXT);
		store.putDocument(new ByteArrayInputStream(this.txt3.getBytes()), this.uri3, DocumentStore.DocumentFormat.TXT);
		store.putDocument(new ByteArrayInputStream(this.txt4.getBytes()), this.uri4, DocumentStore.DocumentFormat.TXT);
		store.putDocument(new ByteArrayInputStream(this.txt5.getBytes()), this.uri5, DocumentStore.DocumentFormat.TXT);
		//search word
		List<String> text = new ArrayList<>();
		text.add(this.txt4);
		text.add(this.txt6);
		assertEquals(text,store.search("test"));
		//search prefix
		List<String> text2 = new ArrayList<>();
		text2.add(this.txt6);
		text2.add(this.txt4);
		text2.add(this.txt3);
		text2.add(this.txt5);
		assertEquals(text2, store.searchByPrefix("d"));
		//delete all those with prefix
		Set<URI> uriSet = new HashSet<>();
		uriSet.add(this.uri4);
		uriSet.add(this.uri6);
		uriSet.add(this.uri3);
		uriSet.add(this.uri5);
		assertEquals(uriSet, store.deleteAllWithPrefix("d"));
		store.undo(this.uri6);
		Set<URI> uriSetNew = new HashSet<>();
		uriSetNew.add(this.uri6);
		assertEquals(uriSetNew,store.deleteAll("typical"));
		store.undo();
		List<String> text5 = new ArrayList<>();
		text5.add(this.txt6);
		assertEquals(text5, store.searchByPrefix("d"));
		store.deleteAllWithPrefix("iyui");
		store.undo();
		store.undo();
		assertEquals(text2, store.searchByPrefix("d"));
	}
	@Test
	public void DoesnotExist()
	{
		DocumentStore store = new DocumentStoreImpl();
		store.putDocument(new ByteArrayInputStream(this.txt5.getBytes()), this.uri5, DocumentStore.DocumentFormat.TXT);
		store.putDocument(new ByteArrayInputStream(this.txt6.getBytes()), this.uri6, DocumentStore.DocumentFormat.TXT);
		store.putDocument(new ByteArrayInputStream(this.txt3.getBytes()), this.uri3, DocumentStore.DocumentFormat.TXT);
		store.putDocument(new ByteArrayInputStream(this.txt4.getBytes()), this.uri4, DocumentStore.DocumentFormat.TXT);
		store.putDocument(new ByteArrayInputStream(this.txt5.getBytes()), this.uri5, DocumentStore.DocumentFormat.TXT);
		assertEquals(new ArrayList<String>(), store.search("goalieforthewin"));
		assertEquals(new ArrayList<String>(), store.searchPDFs("goalieforthewin"));
		assertEquals(new ArrayList<String>(), store.searchByPrefix("goalieforthewin"));
		assertEquals(new ArrayList<String>(), store.searchPDFsByPrefix("goalieforthewin"));
		assertEquals(new HashSet<URI>(), store.deleteAll("goalieforthewin"));
		assertEquals(new HashSet<URI>(), store.deleteAllWithPrefix("goalieforthewin"));
	}
	public static String pdfDataToText(byte[] pdfBytes) {
		try {
			PDFTextStripper textStripper = new PDFTextStripper();
			return textStripper.getText(PDDocument.load(pdfBytes)).trim();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	/*
	@Test
	public void pdfvresion()
	{
		DocumentStore store = new DocumentStoreImpl();
		store.putDocument(new ByteArrayInputStream(pdfData5), this.uri5, DocumentStore.DocumentFormat.PDF);
		store.putDocument(new ByteArrayInputStream(pdfData6), this.uri6, DocumentStore.DocumentFormat.PDF);
		store.putDocument(new ByteArrayInputStream(pdfData3), this.uri3, DocumentStore.DocumentFormat.PDF);
		store.putDocument(new ByteArrayInputStream(pdfData4), this.uri4, DocumentStore.DocumentFormat.PDF);
		List<byte[]> first = store.searchPDFs("hope");
		System.out.println("hope search");
		for (byte[] hope: first)
		{
			System.out.println(pdfDataToText(hope));
		}

		System.out.println("ha search");
		List<byte[]> second = store.searchPDFsByPrefix("ha");
		for (byte[] nohope: second)
		{
			System.out.println(pdfDataToText(nohope));
		}
		System.out.println("delete");
		store.deleteAll("hope");
		List<byte[]> mee = store.searchPDFs("hope");
		System.out.println("hope search after delete");
		System.out.println(mee);
		for (byte[] hope: mee)
		{
			System.out.println(pdfDataToText(hope));
		}
		store.undo(this.uri4);
		store.undo(this.uri6);
		store.undo(this.uri3);
		store.undo(this.uri5);
		List<byte[]> meet = store.searchPDFs("hope");
		System.out.println("hope take two after unod delete");
		for (byte[] hope: meet)
		{
			System.out.println(pdfDataToText(hope));
		}
		store.undo();
		List<byte[]> meett = store.searchPDFs("hope");
		System.out.println("hope no doc 4");
		for (byte[] hope: meett)
		{
			System.out.println(pdfDataToText(hope));
		}

	}
	*/
	//API Testing
	@Test
	public void interfaceCount() {//tests that the class only implements one interface and its the correct one
		@SuppressWarnings("rawtypes")
		Class[] classes = DocumentStoreImpl.class.getInterfaces();
		assertTrue(classes.length == 1);
		assertTrue(classes[0].getName().equals("edu.yu.cs.com1320.project.stage3.DocumentStore"));
	}

	@Test
	public void methodCount() {
		Method[] methods = DocumentStoreImpl.class.getDeclaredMethods();
		int publicMethodCount = 0;
		for (Method method : methods) {
			if (Modifier.isPublic(method.getModifiers())) {
				publicMethodCount++;
			}
		}
		assertTrue(publicMethodCount == 12);
	}

	@Test
	public void fieldCount() {
		Field[] fields = DocumentStoreImpl.class.getFields();
		int publicFieldCount = 0;
		for (Field field : fields) {
			if (Modifier.isPublic(field.getModifiers())) {
				publicFieldCount++;
			}
		}
		assertTrue(publicFieldCount == 0);
	}

	@Test
	public void subClassCount() {
		@SuppressWarnings("rawtypes")
		Class[] classes = DocumentStoreImpl.class.getClasses();
		assertTrue(classes.length == 0);
	}

	@Test
	public void constructorExists() {
		try {
			new DocumentStoreImpl();
		} catch (Exception e) {}
	}

	@Test
	public void putDocumentExists(){
		try {
			new DocumentStoreImpl().putDocument(null, new URI("hi"), DocumentStore.DocumentFormat.PDF);
		} catch (Exception e) {}
	}

	@Test
	public void getDocumentAsPdfExists(){
		try {
			new DocumentStoreImpl().getDocumentAsPdf(new URI("hi"));
		} catch (Exception e) {}
	}

	@Test
	public void getDocumentAsTxtExists(){
		try {
			new DocumentStoreImpl().getDocumentAsTxt(new URI("hi"));
		} catch (Exception e) {}
	}

	@Test
	public void deleteDocumentExists(){
		try {
			new DocumentStoreImpl().deleteDocument(new URI("hi"));
		} catch (Exception e) {}
	}

	@Test
	public void stage2UndoExists(){
		try {
			new DocumentStoreImpl().undo();
		} catch (Exception e) {}
	}

	@Test
	public void stage2UndoByURIExists(){
		try {
			new DocumentStoreImpl().undo(new URI("hi"));
		} catch (Exception e) {}
	}

	@Test
	public void stage2GetDocumentExists(){
		try {
			new DocumentStoreImpl().getDocument(new URI("hi"));
		} catch (Exception e) {}
	}
	@Test
	public void stage3SearchDocumentExists(){
		try {
			new DocumentStoreImpl().search("hi");
		} catch (Exception e) {}
	}
	@Test
	public void stage3SearchPrefixDocumentExists(){
		try {
			new DocumentStoreImpl().searchByPrefix("h");
		} catch (Exception e) {}
	}
	@Test
	public void stage3SearchPDFSDocumentExists(){
		try {
			new DocumentStoreImpl().searchPDFs("hi");
		} catch (Exception e) {}
	}
	@Test
	public void stage3SearchPDFSPrefixPDocumentExists(){
		try {
			new DocumentStoreImpl().searchPDFsByPrefix("h");
		} catch (Exception e) {}
	}
	@Test
	public void stage3DeleteAllPDocumentExists(){
		try {
			new DocumentStoreImpl().deleteAll("hi");
		} catch (Exception e) {}
	}
	@Test
	public void stage3DeletePrefixPDocumentExists(){
		try {
			new DocumentStoreImpl().deleteAllWithPrefix("h");
		} catch (Exception e) {}
	}
	//variables to hold possible values for doc1
	private URI uri1;
	private String txt1;
	private byte[] pdfData1;
	private String pdfTxt1;

	//variables to hold possible values for doc2
	private URI uri2;
	private String txt2;
	private byte[] pdfData2;
	private String pdfTxt2;

	@Before
	public void initTest() throws Exception {
		//init possible values for doc1
		this.uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
		this.txt1 = "This is the text of doc1, in plain text. No fancy file format - just plain old String";
		this.pdfTxt1 = "This is some PDF text for doc1, hat tip to Adobe.";
		this.pdfData1 = Utils.textToPdfData(this.pdfTxt1);

		//init possible values for doc2
		this.uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
		this.txt2 = "Text for doc2. A plain old String.";
		this.pdfTxt2 = "PDF content for doc2: PDF format was opened in 2008.";
		this.pdfData2 = Utils.textToPdfData(this.pdfTxt2);
	}

	@Test
	public void testPutPdfDocumentNoPreviousDocAtURITest(){
		DocumentStore store = new DocumentStoreImpl();
		int returned = store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
		//TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
		assertTrue(returned == 0 || returned == this.pdfTxt1.hashCode());
	}

	@Test
	public void testPutTxtDocumentNoPreviousDocAtURI(){
		DocumentStore store = new DocumentStoreImpl();
		int returned = store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
		//TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
		assertTrue(returned == 0 || returned == this.txt1.hashCode());
	}

	@Test
	public void testPutDocumentWithNullArguments(){
		DocumentStore store = new DocumentStoreImpl();
		try {
			store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()), null, DocumentStore.DocumentFormat.TXT);
			fail("null URI should've thrown IllegalArgumentException");
		}catch(IllegalArgumentException e){}
		try {
			store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()), this.uri1, null);
			fail("null format should've thrown IllegalArgumentException");
		}catch(IllegalArgumentException e){}
	}

	@Test
	public void testPutNewVersionOfDocumentPdf(){
		//put the first version
		DocumentStore store = new DocumentStoreImpl();
		int returned = store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
		//TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
		assertTrue(returned == 0 || returned == this.pdfTxt1.hashCode());
		assertEquals("failed to return correct pdf text",this.pdfTxt1,Utils.pdfDataToText(store.getDocumentAsPdf(this.uri1)));

		//put the second version, testing both return value of put and see if it gets the correct text
		returned = store.putDocument(new ByteArrayInputStream(this.pdfData2),this.uri1, DocumentStore.DocumentFormat.PDF);
		//TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
		assertTrue("should return hashcode of old text",this.pdfTxt1.hashCode() == returned || this.pdfTxt2.hashCode() == returned);
		assertEquals("failed to return correct pdf text", this.pdfTxt2,Utils.pdfDataToText(store.getDocumentAsPdf(this.uri1)));
	}

	@Test
	public void testPutNewVersionOfDocumentTxt(){
		//put the first version
		DocumentStore store = new DocumentStoreImpl();
		int returned = store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
		//TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
		assertTrue(returned == 0 || returned == this.txt1.hashCode());
		assertEquals("failed to return correct text",this.txt1,store.getDocumentAsTxt(this.uri1));

		//put the second version, testing both return value of put and see if it gets the correct text
		returned = store.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
		//TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
		assertTrue("should return hashcode of old text",this.txt1.hashCode() == returned || this.txt2.hashCode() == returned);
		assertEquals("failed to return correct text",this.txt2,store.getDocumentAsTxt(this.uri1));
	}

	@Test
	public void testGetTxtDocAsPdf(){
		DocumentStore store = new DocumentStoreImpl();
		int returned = store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
		//TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
		assertTrue(returned == 0 || returned == this.txt1.hashCode());
		assertEquals("failed to return correct pdf text",this.txt1,Utils.pdfDataToText(store.getDocumentAsPdf(this.uri1)));
	}

	@Test
	public void testGetTxtDocAsTxt(){
		DocumentStore store = new DocumentStoreImpl();
		int returned = store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
		//TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
		assertTrue(returned == 0 || returned == this.txt1.hashCode());
		assertEquals("failed to return correct text",this.txt1,store.getDocumentAsTxt(this.uri1));
	}

	@Test
	public void testGetPdfDocAsPdf(){
		DocumentStore store = new DocumentStoreImpl();
		int returned = store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
		//TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
		assertTrue(returned == 0 || returned == this.pdfTxt1.hashCode());
		assertEquals("failed to return correct pdf text",this.pdfTxt1,Utils.pdfDataToText(store.getDocumentAsPdf(this.uri1)));
	}

	@Test
	public void testGetPdfDocAsTxt(){
		DocumentStore store = new DocumentStoreImpl();
		int returned = store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
		//TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
		assertTrue(returned == 0 || returned == this.pdfTxt1.hashCode());
		assertEquals("failed to return correct text",this.pdfTxt1,store.getDocumentAsTxt(this.uri1));
	}

	@Test
	public void testDeleteDoc(){
		DocumentStore store = new DocumentStoreImpl();
		store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
		store.deleteDocument(this.uri1);
		assertEquals("calling get on URI from which doc was deleted should've returned null", null, store.getDocumentAsPdf(this.uri1));
	}

	@Test
	public void testDeleteDocReturnValue(){
		DocumentStore store = new DocumentStoreImpl();
		store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
		//should return true when deleting a document
		assertEquals("failed to return true when deleting a document",true,store.deleteDocument(this.uri1));
		//should return false if I try to delete the same doc again
		assertEquals("failed to return false when trying to delete that which was already deleted",false,store.deleteDocument(this.uri1));
		//should return false if I try to delete something that was never there to begin with
		assertEquals("failed to return false when trying to delete that which was never there to begin with",false,store.deleteDocument(this.uri2));
	}
}

