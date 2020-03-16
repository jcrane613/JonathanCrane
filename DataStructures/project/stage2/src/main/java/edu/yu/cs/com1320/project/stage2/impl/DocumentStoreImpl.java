package edu.yu.cs.com1320.project.stage2.impl;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import edu.yu.cs.com1320.project.Command;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.stage2.Document;
import edu.yu.cs.com1320.project.stage2.DocumentStore;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;

import static edu.yu.cs.com1320.project.stage2.DocumentStore.DocumentFormat.PDF;
import static edu.yu.cs.com1320.project.stage2.DocumentStore.DocumentFormat.TXT;

public class DocumentStoreImpl implements DocumentStore
{
	private HashTableImpl<URI, Document> hashTable = new HashTableImpl<URI, Document>();
	private StackImpl<Command> stack = new StackImpl<>();
	private HashTableImpl<URI, Document> deletedHashTable = new HashTableImpl<URI, Document>();
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
			Command command = new Command(uri, ((k) -> doNothing(k)));
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
				Command command = new Command(uri, ((k) -> doNothing(k)));
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
			return testDocument.getDocumentTextHashCode();
		}
		if (testDocument != null && testDocument.getDocumentTextHashCode() != txthash)//same uri
		{
			return sameUriDOC(uri, string, txthash);
		}
		DocumentImpl newDocument = new DocumentImpl(uri,string,txthash);
		hashTable.put(uri, newDocument);
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
		lambdaExpressionAlreadyExisting(uri);
		return valueToReturn.getDocumentTextHashCode();
	}
	private void lambdaExpression(URI uri)
	{
		Command command = new Command(uri, ((k) -> deleteUndoPut(k)));
		stack.push(command);
	}
	private void lambdaExpressionAlreadyExisting(URI uri)
	{
		Command command = new Command(uri, ((k) -> deleteUndoPutAlreadyExisting(k)));
		stack.push(command);
	}
	private boolean deleteUndoCommand(URI k)
	{
		hashTable.put(k,deletedHashTable.get(k));
		return true;
	}
	private boolean deleteUndoPut(URI uri)
	{
		if (hashTable.get(uri) != null)
		{
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
			hashTable.put(uri,null);//delete the old value
			hashTable.put(uri,deletedHashTable.get(uri));//put the new one back in
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
			deletedHashTable.put(uri,hashTable.get(uri));//add it to a deleted hashtable
			hashTable.put(uri,null);//delete the old value
			Command command = new Command(uri, k -> deleteUndoCommand(k));
			stack.push(command);
			return true;
		}
		else {
			Command command = new Command(uri, ((k) -> doNothing(k)));
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
		Command commandUndo = stack.pop();
		commandUndo.undo();
	}
	public void undo(URI uri) throws IllegalStateException
	{
		if (stack.size() == 0)
		{
			throw new IllegalStateException("There are no undo's to execute");
		}
		StackImpl<Command> stackSwitch = new StackImpl<>();
		boolean tester = true;
		while (stack.size() != 0)
		{
			Command command = stack.peek();
			if ((command.getUri().hashCode()) == (uri.hashCode()))
			{
				Command commandUri = stack.pop();
				commandUri.undo();
				tester = false;
				break;
			}
			stackSwitch.push(command);
			stack.pop();
		}
		while (stackSwitch.size() != 0)
		{
			Command reAddTOStack= stackSwitch.pop();
			stack.push(reAddTOStack);
		}
		if (tester == true)
		{ throw new IllegalStateException("The inputted uri was not found in the command stack"); }
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
	public void print()
	{
		hashTable.printTest();
	}

	public static void main(String[] args) throws URISyntaxException, IOException {
		String pdfTxt1 = "This is some PDF text for doc1, hat tip to Adobe.";
		byte[] pdfData1 = textToPdfData(pdfTxt1);
		URI uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
		URI uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
		String txt2 = "Text for doc2. A plain old String.";
		String pdfTxt2 = "PDF content for doc2: PDF format was opened in 2008.";
		byte[] pdfData2 = textToPdfData(pdfTxt2);
		DocumentStoreImpl store = new DocumentStoreImpl();
		store.putDocument(new ByteArrayInputStream(pdfData1), uri1, PDF);
		store.putDocument(new ByteArrayInputStream(pdfData2), uri2, PDF);
		URI jonathan = new URI("www.youtubee");
		URI moshe = new URI("www.thisboy/isrunnig");
		String txt1 = "This is the text of doc1, in plain text. No fancy file format - just plain old String";
		String txt4 = "This is the  of doc1, in  text. No fancy  format - just plain String";
		store.putDocument(new ByteArrayInputStream(txt1.getBytes()),jonathan, TXT);
		store.putDocument(new ByteArrayInputStream(txt4.getBytes()), moshe, TXT);
		System.out.println("full version");
		store.print();
		store.undo(uri1);
		System.out.println("one undo");
		store.print();
		store.undo();
		System.out.println("second undo");
		store.print();
		store.undo();
		System.out.println("third undo");
		store.print();

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

}