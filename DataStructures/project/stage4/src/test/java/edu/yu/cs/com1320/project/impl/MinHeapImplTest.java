package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.MinHeap;
import edu.yu.cs.com1320.project.stage4.Document;
import edu.yu.cs.com1320.project.stage4.impl.DocumentImpl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Before;
import org.junit.Test;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class MinHeapImplTest {
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
	public void removeMin()
	{
		DocumentImpl doc1 = new DocumentImpl(uri3, txt3, txt3.hashCode());
		doc1.setLastUseTime(System.nanoTime());
		DocumentImpl doc2 = new DocumentImpl(uri4, txt4, txt4.hashCode());
		doc2.setLastUseTime(System.nanoTime());
		DocumentImpl doc3 = new DocumentImpl(uri5, txt5, txt5.hashCode());
		doc3.setLastUseTime(System.nanoTime());
		DocumentImpl doc4 = new DocumentImpl(uri6, txt6, txt6.hashCode());
		doc4.setLastUseTime(System.nanoTime());
		MinHeapImpl heap = new MinHeapImpl();
		heap.insert(doc1);
		heap.insert(doc2);
		heap.insert(doc3);
		doc1.setLastUseTime(System.nanoTime());
		heap.reHeapify(doc1);
		assertEquals(doc2.getKey(), ((Document)heap.removeMin()).getKey());

	}
	@Test
	public void reheapifymovesmostrecenttotop()
	{
		DocumentImpl doc1 = new DocumentImpl(uri3, txt3, txt3.hashCode());
		doc1.setLastUseTime(System.nanoTime());
		DocumentImpl doc2 = new DocumentImpl(uri4, txt4, txt4.hashCode());
		doc2.setLastUseTime(System.nanoTime());
		DocumentImpl doc3 = new DocumentImpl(uri5, txt5, txt5.hashCode());
		doc3.setLastUseTime(System.nanoTime());
		DocumentImpl doc4 = new DocumentImpl(uri6, txt6, txt6.hashCode());
		doc4.setLastUseTime(System.nanoTime());
		MinHeapImpl heap = new MinHeapImpl();
		heap.insert(doc4);
		heap.insert(doc3);
		heap.reHeapify(doc4);
		assertEquals(doc3.getKey(), ((Document)heap.removeMin()).getKey());
	}
	@Test
	public void reheapifymovesmostrecenttobottom()
	{
		DocumentImpl doc1 = new DocumentImpl(uri3, txt3, txt3.hashCode());
		doc1.setLastUseTime(System.nanoTime());
		DocumentImpl doc2 = new DocumentImpl(uri4, txt4, txt4.hashCode());
		doc2.setLastUseTime(System.nanoTime());
		DocumentImpl doc3 = new DocumentImpl(uri5, txt5, txt5.hashCode());
		doc3.setLastUseTime(System.nanoTime());
		DocumentImpl doc4 = new DocumentImpl(uri6, txt6, txt6.hashCode());
		doc4.setLastUseTime(System.nanoTime());
		MinHeapImpl heap = new MinHeapImpl();
		heap.insert(doc4);
		heap.insert(doc3);
		heap.insert(doc1);
		heap.insert(doc2);
		doc1.setLastUseTime(System.nanoTime());
		heap.reHeapify(doc1);
		assertEquals(doc2.getKey(), ((Document)heap.removeMin()).getKey());

	}
	@Test
	public void isEmpty()
	{
		DocumentImpl doc1 = new DocumentImpl(uri3, txt3, txt3.hashCode());
		doc1.setLastUseTime(System.nanoTime());
		DocumentImpl doc2 = new DocumentImpl(uri4, txt4, txt4.hashCode());
		doc2.setLastUseTime(System.nanoTime());
		DocumentImpl doc3 = new DocumentImpl(uri5, txt5, txt5.hashCode());
		doc3.setLastUseTime(System.nanoTime());
		DocumentImpl doc4 = new DocumentImpl(uri6, txt6, txt6.hashCode());
		doc4.setLastUseTime(System.nanoTime());
		MinHeapImpl heap = new MinHeapImpl();
		assertTrue(heap.isEmpty());
		heap.insert(doc1);
		assertFalse(heap.isEmpty());
		heap.removeMin();
		assertTrue(heap.isEmpty());

	}
	@Test
	public void heapSwap()
	{
		DocumentImpl doc1 = new DocumentImpl(uri3, txt3, txt3.hashCode());
		doc1.setLastUseTime(System.nanoTime());
		DocumentImpl doc2 = new DocumentImpl(uri4, txt4, txt4.hashCode());
		doc2.setLastUseTime(System.nanoTime());
		DocumentImpl doc3 = new DocumentImpl(uri5, txt5, txt5.hashCode());
		doc3.setLastUseTime(System.nanoTime());
		DocumentImpl doc4 = new DocumentImpl(uri6, txt6, txt6.hashCode());
		doc4.setLastUseTime(System.nanoTime());
		MinHeapImpl heap = new MinHeapImpl();
		heap.insert(doc1);
		heap.insert(doc2);
		heap.insert(doc3);
		heap.insert(doc4);
		assertEquals(2,heap.getArrayIndex(doc2));
		assertEquals(4,heap.getArrayIndex(doc4));
		heap.swap(2, 4);
		assertEquals(2,heap.getArrayIndex(doc4));
		assertEquals(4,heap.getArrayIndex(doc2));


	}
	@Test
	public void heapSwapMove() throws URISyntaxException {
		DocumentImpl doc1 = new DocumentImpl(uri3, txt3, txt3.hashCode());
		doc1.setLastUseTime(System.nanoTime());
		DocumentImpl doc2 = new DocumentImpl(uri4, txt4, txt4.hashCode());
		doc2.setLastUseTime(System.nanoTime());
		DocumentImpl doc3 = new DocumentImpl(uri5, txt5, txt5.hashCode());
		doc3.setLastUseTime(System.nanoTime());
		DocumentImpl doc4 = new DocumentImpl(uri6, txt6, txt6.hashCode());
		doc4.setLastUseTime(System.nanoTime());
		DocumentImpl doc5 = new DocumentImpl(new URI("www.gmm"), "www.gmm", "www.gmm".hashCode());
		doc5.setLastUseTime(System.nanoTime());
		DocumentImpl doc6 = new DocumentImpl(new URI("www.wearethere"), "www.wearethere", "www.wearethere".hashCode());
		doc6.setLastUseTime(System.nanoTime());
		DocumentImpl doc7 = new DocumentImpl(new URI("www.ihope/thereiscamp"), "www.ihope/thereiscamp", "www.ihope/thereiscamp".hashCode());
		doc7.setLastUseTime(System.nanoTime());
		DocumentImpl doc8 = new DocumentImpl(new URI("www.google.com"), "www.google.com", "www.google.com".hashCode());
		doc8.setLastUseTime(System.nanoTime());
		DocumentImpl doc9 = new DocumentImpl(new URI("www.buzzfeed"), "www.buzzfeed", "www.buzzfeed".hashCode());
		doc9.setLastUseTime(System.nanoTime());
		DocumentImpl doc10 = new DocumentImpl(new URI("www.wowza"), "www.wowza", "www.wowza".hashCode());
		doc10.setLastUseTime(System.nanoTime());
		DocumentImpl doc11 = new DocumentImpl(new URI("www.this/almost/my/last"), "www.this/almost/my/last", "www.this/almost/my/last".hashCode());
		doc11.setLastUseTime(System.nanoTime());
		DocumentImpl doc12 = new DocumentImpl(new URI("www.ihope/tobe/done"), "www.ihope/tobe/done", "www.ihope/tobe/done".hashCode());
		doc12.setLastUseTime(System.nanoTime());
		MinHeapImpl heap = new MinHeapImpl();
		heap.insert(doc1);
		heap.insert(doc2);
		heap.insert(doc3);
		heap.insert(doc4);
		heap.insert(doc5);
		heap.insert(doc6);
		heap.insert(doc7);
		heap.insert(doc8);
		heap.insert(doc9);
		heap.insert(doc10);
		heap.insert(doc11);
		heap.insert(doc12);
		assertEquals(1,heap.getArrayIndex(doc1));
		heap.removeMin();
		heap.removeMin();
		assertEquals(1,heap.getArrayIndex(doc3));
		heap.doubleArraySize();
		heap.removeMin();
		assertEquals(1,heap.getArrayIndex(doc4));
		doc5.setLastUseTime(Long.MIN_VALUE);
		heap.reHeapify(doc5);
		assertEquals(doc5.getKey(), ((Document)heap.removeMin()).getKey());//can delete any doc that i want and not doc 4
		assertTrue(heap.isGreater(heap.getArrayIndex(doc9), heap.getArrayIndex(doc4))); //is greater works
	}
	//API TESTING


	@Test
	public void methodCount() {//need only test for non constructors
		Method[] methods = MinHeapImpl.class.getDeclaredMethods();
		int publicMethodCount = 0;
		for (Method method : methods) {
			if (Modifier.isPublic(method.getModifiers())) {
				if(!method.getName().equals("equals") && !method.getName().equals("hashCode")) {
					publicMethodCount++;
				}
			}
		}
		assertTrue(publicMethodCount == 3);
	}

	@Test
	public void fieldCount() {
		Field[] fields = MinHeapImpl.class.getFields();
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
		Class[] classes = MinHeapImpl.class.getClasses();
		assertTrue(classes.length == 0);
	}

	@Test
	public void noArgsConstructorExists(){
		try {
			new MinHeapImpl();
		} catch (RuntimeException e) {}
	}


}