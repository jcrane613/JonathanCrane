package edu.yu.cs.com1320.project.stage1.impl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class DocumentImplTest {

//API Testing
	@Test
	public void interfaceCount() {//tests that the class only implements one interface and its the correct one
		@SuppressWarnings("rawtypes")
		Class[] classes = DocumentImpl.class.getInterfaces();
		assertTrue(classes.length == 1);
		assertTrue(classes[0].getName().equals("edu.yu.cs.com1320.project.stage1.Document"));
	}

	@Test
	public void methodCount() {//need only test for non constructors
		Method[] methods = DocumentImpl.class.getDeclaredMethods();
		int publicMethodCount = 0;
		for (Method method : methods) {
			if (Modifier.isPublic(method.getModifiers())) {
				if(!method.getName().equals("equals") && !method.getName().equals("hashCode")) {
					publicMethodCount++;
				}
			}
		}
		assertTrue(publicMethodCount == 4);
	}

	@Test
	public void fieldCount() {
		Field[] fields = DocumentImpl.class.getFields();
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
		Class[] classes = DocumentImpl.class.getClasses();
		assertTrue(classes.length == 0);
	}

	@Test
	public void constructor1Exists() throws URISyntaxException {
		URI uri = new URI("https://this.com");
		try {
			new DocumentImpl(uri, "hi", 1);
		} catch (RuntimeException e) {}
	}

	@Test
	public void constructor2Exists() throws URISyntaxException {
		URI uri = new URI("https://this.com");
		byte[] ary = {0,0,0};
		try {
			new DocumentImpl(uri, "hi", 1, ary );
		} catch (RuntimeException e) {}
	}

	@Test
	public void getDocumentTextHashCodeExists() throws URISyntaxException{
		URI uri = new URI("https://this.com");
		try {
			new DocumentImpl(uri, "hi", 1).getDocumentTextHashCode();
		} catch (RuntimeException e) {}
	}

	@Test
	public void getDocumentAsPdfExists() throws URISyntaxException{
		URI uri = new URI("https://this.com");
		try {
			new DocumentImpl(uri, "hi", 1).getDocumentAsPdf();
		} catch (RuntimeException e) {}
	}

	@Test
	public void getDocumentAsTxtExists() throws URISyntaxException{
		URI uri = new URI("https://this.com");
		try {
			new DocumentImpl(uri, "hi", 1).getDocumentAsTxt();
		} catch (RuntimeException e) {}
	}

	@Test
	public void getKeyExists() throws URISyntaxException {
		URI uri = new URI("https://this.com");
		try {
			new DocumentImpl(uri, "hi", 1).getKey();
		} catch (RuntimeException e) {}
	}

	//Reg Testing
	private URI textUri;
	private String textString;
	private int textHashCode;

	private URI pdfUri;
	private String pdfString;
	private int pdfHashCode;
	private byte[] pdfData;

	@Before
	public void setUp() throws Exception {
		this.textUri = new URI("http://edu.yu.cs/com1320/txt");
		this.textString = "This is text content. Lots of it.";
		this.textHashCode = this.textString.hashCode();

		this.pdfUri = new URI("http://edu.yu.cs/com1320/pdf");
		this.pdfString = "This is a PDF, brought to you by Adobe.";
		this.pdfHashCode = this.pdfString.hashCode();
		this.pdfData = textToPdfData(this.pdfString);
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
	public void testGetTextDocumentAsTxt() {
		DocumentImpl textDocument = new DocumentImpl(this.textUri, this.textString, this.textHashCode);
		assertEquals(this.textString, textDocument.getDocumentAsTxt());
	}

	@Test
	public void testGetPdfDocumentAsTxt() {
		DocumentImpl pdfDocument = new DocumentImpl(this.pdfUri, this.pdfString, this.pdfHashCode, this.pdfData);
		assertEquals(this.pdfString, pdfDocument.getDocumentAsTxt());
	}

	@Test
	public void testGetTextDocumentAsPdf() {
		DocumentImpl textDocument = new DocumentImpl(this.textUri, this.textString, this.textHashCode);
		byte[] pdfBytes = textDocument.getDocumentAsPdf();
		String textAsPdfString = pdfDataToText(pdfBytes);
		assertEquals(this.textString, textAsPdfString);
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
	public void testGetPdfDocumentAsPdf() {
		DocumentImpl pdfDocument = new DocumentImpl(this.pdfUri, this.pdfString, this.pdfHashCode, this.pdfData);
		byte[] pdfBytes = pdfDocument.getDocumentAsPdf();
		String pdfAsPdfString = pdfDataToText(pdfBytes);
		assertEquals(this.pdfString, pdfAsPdfString);
	}

	@Test
	public void testGetTextDocumentTextHashCode() {
		DocumentImpl textDocument = new DocumentImpl(this.textUri, this.textString, this.textHashCode);
		assertEquals(this.textHashCode, textDocument.getDocumentTextHashCode());
	}

	@Test
	public void testGetPdfDocumentTextHashCode() {
		DocumentImpl pdfDocument = new DocumentImpl(this.pdfUri, this.pdfString, this.pdfHashCode, this.pdfData);
		assertEquals(this.pdfHashCode, pdfDocument.getDocumentTextHashCode());
		assertNotEquals(this.pdfHashCode, 25);
	}

	@Test
	public void testGetTextDocumentKey() {
		DocumentImpl textDocument = new DocumentImpl(this.textUri, this.textString, this.textHashCode);
		assertEquals(this.textUri, textDocument.getKey());
		URI fakeUri = null;
		try {
			fakeUri = new URI("http://wrong.com");
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
		assertNotEquals(this.textUri, fakeUri);
	}

	@Test
	public void testGetPdfDocumentKey() {
		DocumentImpl pdfDocument = new DocumentImpl(this.pdfUri, this.pdfString, this.pdfHashCode, this.pdfData);
		assertEquals(this.pdfUri, pdfDocument.getKey());
		URI fakeUri = null;
		try {
			fakeUri = new URI("http://wrong.com");
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
		assertNotEquals(this.pdfUri, fakeUri);
	}

}