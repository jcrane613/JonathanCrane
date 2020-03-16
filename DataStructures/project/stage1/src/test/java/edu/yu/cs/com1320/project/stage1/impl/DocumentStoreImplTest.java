package edu.yu.cs.com1320.project.stage1.impl;

import edu.yu.cs.com1320.project.stage1.DocumentStore;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;

import static edu.yu.cs.com1320.project.stage1.DocumentStore.DocumentFormat.PDF;
import static edu.yu.cs.com1320.project.stage1.DocumentStore.DocumentFormat.TXT;
import static org.junit.Assert.*;

public class DocumentStoreImplTest {

	@Test
	public void putDocument() throws URISyntaxException, IOException {
		File file = new File("/Users/jonathancrane/MYGIT/comSci/TestFile.pdf");
		InputStream input = new ByteArrayInputStream(inputThis("This is the test to determine the validity of my methods"));
		DocumentStoreImpl docStore = new DocumentStoreImpl();
		URI uri = new URI("https://me.com/class/k5fvaanpgc56kx");
		docStore.putDocument(input, uri, PDF);
		byte[] thisByte = docStore.getDocumentAsPdf(uri);
		PDDocument thisDoc = PDDocument.load(thisByte);
		PDFTextStripper stripper = new PDFTextStripper();
		String stringText = stripper.getText(thisDoc).trim();
		assertEquals(stringText, "This is the test to determine the validity of my methods");
	}


	@Test
	public void getDocumentAsPdf() throws FileNotFoundException, URISyntaxException {
		DocumentStoreImpl docStore = new DocumentStoreImpl();
		File file = new File("/Users/jonathancrane/MYGIT/comSci/Intro.pdf");
		InputStream targetStream = new FileInputStream(file);
		URI uri = new URI("https://piazza.com/class/k5fvaanpgc56kx");
		docStore.putDocument(targetStream, uri, TXT);
		URI uri2 = new URI("https://pizza.com/class/k5fvaanpgc56kx");
		docStore.putDocument(new ByteArrayInputStream((inputThis("how is life"))),uri2, PDF);
		assertTrue(docStore.deleteDocument(uri2));
	}

	@Test
	public void getDocumentAsTxt() throws URISyntaxException {
		String text = "Hey, I hope you're having a nice day";
		InputStream input = new ByteArrayInputStream(text.getBytes());
		DocumentStoreImpl docStore = new DocumentStoreImpl();
		URI uri = new URI("https://piazza.com/class/k5fvaanpgc56kx");
		docStore.putDocument(input, uri, TXT);
		String jon= new String(docStore.getDocumentAsPdf(uri));
	}
	@Test
	public void nullInputStream() throws URISyntaxException {
		String text = "Hey, I hope you're having a nice day";
		InputStream input = new ByteArrayInputStream(text.getBytes());
		DocumentStoreImpl docStore = new DocumentStoreImpl();
		URI uri = new URI("https://piazza.com/class/k5fvaanpgc56kx");
		docStore.putDocument(input, uri, TXT);
		URI uri2 = new URI("https://www.amazon.com/");
		assertEquals(text,docStore.getDocumentAsTxt(uri));//this shows that the text funciton works and the input for text
		InputStream nullInput = null;
		InputStream nullInput2 = null;
		assertEquals(0,docStore.putDocument(nullInput,uri2,TXT));//deleted a doc that doesn't exist
		assertEquals(text.hashCode(), docStore.putDocument(nullInput2,uri,TXT));//deleting a document that was already there
		URI uri4 = new URI("https://pizza.com/class/k5fvaanpgc56kx");
		assertEquals(0,docStore.putDocument(new ByteArrayInputStream((inputThis("how is life"))),uri4, PDF));
		assertEquals(null,docStore.getDocumentAsTxt(uri2));//testing that the doc was deleted
		assertEquals(null,docStore.getDocumentAsTxt(uri));//testing that the doc was deleted
	}
	@Test
	public void deleteMethod() throws URISyntaxException {
		String text = "Hey, I hope you're having a nice day";
		InputStream input = new ByteArrayInputStream(text.getBytes());
		DocumentStoreImpl docStore = new DocumentStoreImpl();
		URI uri = new URI("https://piazza.com/class/k5fvaanpgc56kx");
		URI uri2 = new URI("https://amazon.com");
		docStore.putDocument(input, uri, TXT);
		assertEquals(text,docStore.getDocumentAsTxt(uri));
		assertTrue(docStore.deleteDocument(uri));//returns true when the document exists
		assertFalse(docStore.deleteDocument(uri2));//returns false when the document doesn't exist
	}
	@Test//how does one test the moethod get as a pdf method, It is clear it does something
	public void getDAsPDF() throws URISyntaxException, IOException {
		String text = "Hey, I hope you're having a nice day";
		InputStream input = new ByteArrayInputStream(text.getBytes());
		DocumentStoreImpl docStore = new DocumentStoreImpl();
		URI uri = new URI("https://piazza.com/class/k5fvaanpgc56kx");
		docStore.putDocument(input, uri, TXT);
		byte[] testerByte = docStore.getDocumentAsPdf(uri);
		PDDocument thisDoc = PDDocument.load(inputThis(text));
		PDFTextStripper stripper = new PDFTextStripper();
		String stringText = stripper.getText(thisDoc).trim();
		PDDocument thisDoc2 = PDDocument.load(testerByte);
		PDFTextStripper stripper2 = new PDFTextStripper();
		String stringText2 = stripper2.getText(thisDoc2).trim();
		assertEquals(stringText,stringText2);

	}
	@Test
	public void putDocumentReturnValueTXT() throws URISyntaxException {
		String text = "Hey, I hope you're having a nice day";
		InputStream input = new ByteArrayInputStream(text.getBytes());
		DocumentStoreImpl docStore = new DocumentStoreImpl();
		URI uri = new URI("https://piazza.com/class/k5fvaanpgc56kx");
		assertEquals(0,docStore.putDocument(input, uri, TXT));

	}
	@Test
	public void putDocumentReturnValuePDF() throws URISyntaxException //I'm not sure how to test this one, but it shoudl be made
	{
		String text = "This is a test class";
		InputStream input = new ByteArrayInputStream(inputThis(text));
		DocumentStoreImpl docStore = new DocumentStoreImpl();
		URI uri = new URI("https://piazza.com/class/k5fvaanpgc56kx");
		assertEquals(0,docStore.putDocument(input, uri, PDF));
	}
	@Test
	public void DocumentAlreadyExists() throws URISyntaxException //I'm not sure how to test this one, but it shoudl be made
	{
		String text = "Hey, I hope you're having a nice day";
		InputStream input = new ByteArrayInputStream(text.getBytes());
		DocumentStoreImpl docStore = new DocumentStoreImpl();
		URI uri = new URI("https://piazza.com/class/k5fvaanpgc56kx");
		docStore.putDocument(input, uri, TXT);
		String text2 = "Hey, I hope you're having a nice day";
		InputStream input2 = new ByteArrayInputStream(inputThis(text2));
		DocumentStoreImpl docStore2 = new DocumentStoreImpl();
		URI uri2 = new URI("https://piazza.com/class/k5fvaanpgc56kx");

	}
	public byte[] inputThis(String s) {
		PDDocument doc = new PDDocument();
		PDPage page = new PDPage();
		doc.addPage(page);
		PDFont font = PDType1Font.HELVETICA_BOLD;
		try
		{
			PDPageContentStream contents = new PDPageContentStream(doc, page);
			contents.beginText();
			contents.setFont(font, 12);
			//contents.newLineAtOffset(100, 700);
			contents.showText(s);
			contents.endText();
			contents.close();
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			doc.save(byteArrayOutputStream);
			doc.close();
			return (byteArrayOutputStream.toByteArray());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return new byte[0];
	}
	@Test
	public void interfaceCount() {//tests that the class only implements one interface and its the correct one
		@SuppressWarnings("rawtypes")
		Class[] classes = DocumentStoreImpl.class.getInterfaces();
		assertTrue(classes.length == 1);
		assertTrue(classes[0].getName().equals("edu.yu.cs.com1320.project.stage1.DocumentStore"));
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
		assertTrue(publicMethodCount == 4);
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
	public void putDocumentExists() throws URISyntaxException{
		try {
			new DocumentStoreImpl().putDocument(null, new URI("hi"), DocumentStore.DocumentFormat.PDF);
		} catch (Exception e) {}
	}

	@Test
	public void getDocumentAsPdfExists() throws URISyntaxException{
		try {
			new DocumentStoreImpl().getDocumentAsPdf(new URI("hi"));
		} catch (Exception e) {}
	}

	@Test
	public void getDocumentAsTxtExists() throws URISyntaxException{
		try {
			new DocumentStoreImpl().getDocumentAsTxt(new URI("hi"));
		} catch (Exception e) {}
	}

	@Test
	public void deleteDocumentExists() throws URISyntaxException {
		try {
			new DocumentStoreImpl().deleteDocument(new URI("hi"));
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
	public void init() throws Exception {
		//init possible values for doc1
		this.uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
		this.txt1 = "This is the text of doc1, in plain text. No fancy file format - just plain old String";
		this.pdfTxt1 = "This is some PDF text for doc1, hat tip to Adobe.";
		this.pdfData1 = textToPdfData(this.pdfTxt1);

		//init possible values for doc2
		this.uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
		this.txt2 = "Text for doc2. A plain old String.";
		this.pdfTxt2 = "PDF content for doc2: PDF format was opened in 2008.";
		this.pdfData2 = textToPdfData(this.pdfTxt2);
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
	@Test
	public void testPutPdfDocumentNoPreviousDocAtURI(){
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
		assertEquals("failed to return correct pdf text",this.pdfTxt1,pdfDataToText(store.getDocumentAsPdf(this.uri1)));

		//put the second version, testing both return value of put and see if it gets the correct text
		returned = store.putDocument(new ByteArrayInputStream(this.pdfData2),this.uri1, DocumentStore.DocumentFormat.PDF);
		//TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
		assertTrue("should return hashcode of old text",this.pdfTxt1.hashCode() == returned || this.pdfTxt2.hashCode() == returned);
		assertEquals("failed to return correct pdf text", this.pdfTxt2,pdfDataToText(store.getDocumentAsPdf(this.uri1)));
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
		assertEquals("failed to return correct pdf text",this.txt1,pdfDataToText(store.getDocumentAsPdf(this.uri1)));
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
		assertEquals("failed to return correct pdf text",this.pdfTxt1,pdfDataToText(store.getDocumentAsPdf(this.uri1)));
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