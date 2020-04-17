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
			Command command = new Command(uri, ((k) -> doNothing(k)));
			stack.push(command);
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
		deletedHashTable.put(k,null);//delete the old doc from the hashtable that was storing it
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
			deletedHashTable.put(uri,hashTable.get(uri));//add it to a deleted hashtable
			hashTable.put(uri,null);//delete the old value
			Command command = new Command(uri, (k) -> deleteUndoCommand(k));
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

}