package edu.yu.cs.com1320.project.stage1.impl;

import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.stage1.Document;
import edu.yu.cs.com1320.project.stage1.DocumentStore;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.xml.bind.ValidationEvent;
import static edu.yu.cs.com1320.project.stage1.DocumentStore.DocumentFormat.*;

public class DocumentStoreImpl implements DocumentStore
{
	private HashTableImpl<URI, Document> hashTable = new HashTableImpl<URI, Document>();
	public DocumentStoreImpl()
	{}
//Regarding the same method, in the case of a null InputStream, the previous mapping from that
// URI should be deleted, and the hashCode of the previously stored string should be returned.
// If there was no previous mapping, 0 should be returned.
	public int putDocument(InputStream input, URI uri, DocumentFormat format) {
		if (uri == null) { throw new IllegalArgumentException("The uri was null"); }
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
				return 0;
			}
			Document valueToReturn = (Document)hashTable.get(uri);
			boolean test = deleteDocument(uri);
			return valueToReturn.getDocumentTextHashCode();
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
	private int pdfHelper(byte[] inputByte, int txthash,URI uri) {
		try
		{
			PDDocument thisDoc = PDDocument.load(inputByte);
			PDFTextStripper stripper = new PDFTextStripper();
			String stringText = stripper.getText(thisDoc).trim();
			txthash = stringText.hashCode();
			Document testDocument = (Document) hashTable.get(uri);
			if (testDocument != null && testDocument.getDocumentTextHashCode() == txthash)//same doc
			{
				return testDocument.getDocumentTextHashCode();
			}
			if (testDocument != null && testDocument.getDocumentTextHashCode() != txthash)//same uri
			{
				Document valueToReturn = (Document)hashTable.get(uri);
				boolean test = deleteDocument(uri);
				DocumentImpl newDocument = new DocumentImpl(uri, stringText, txthash, inputByte);
				hashTable.put(uri, newDocument);
				return valueToReturn.getDocumentTextHashCode();
			}
			DocumentImpl newDocument = new DocumentImpl(uri, stringText, txthash, inputByte);
			hashTable.put(uri, newDocument);
			thisDoc.close();
			return 0;
		}
		catch (IOException e) { e.printStackTrace(); }
		return 0;
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
			Document valueToReturn = (Document)hashTable.get(uri);
			boolean test = deleteDocument(uri);
			DocumentImpl newDocument = new DocumentImpl(uri, string, txthash);
			hashTable.put(uri, newDocument);
			return valueToReturn.getDocumentTextHashCode();
		}
		DocumentImpl newDocument = new DocumentImpl(uri,string,txthash);
		hashTable.put(uri, newDocument);
		return 0;
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

	public boolean deleteDocument(URI uri) {
		if (hashTable.get(uri) != null)
		{
			hashTable.put(uri,null);
			return true;
		}
		else {
			return false;
		}
	}
}
