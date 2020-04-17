package edu.yu.cs.com1320.project.stage4.impl;

import edu.yu.cs.com1320.project.stage4.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Scanner;

public class DocumentImpl implements Document {
	private URI uri;
	private String txt;
	private int txtHash;
	private byte[] pdfBytes;
	private HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
	public DocumentImpl(URI uri, String txt, int txtHash)
	{
		if (uri == null)
		{
			throw new IllegalArgumentException("uri was null");
		}
		this.uri = uri;
		if (txt == null)
		{
			throw new IllegalArgumentException("txt was null");
		}
		this.txt = txt;
		hashing(txt);
		this.txtHash = txtHash;
	}
	public DocumentImpl(URI uri, String txt, int txtHash, byte[] pdfBytes)
	{
		if (uri == null)
		{
			throw new IllegalArgumentException("uri was null");
		}
		this.uri = uri;
		if (txt == null)
		{
			throw new IllegalArgumentException("txt was null");
		}
		this.txt = txt;
		this.txtHash = txtHash;
		hashing(txt);
		this.pdfBytes = pdfBytes;
	}
	private void hashing(String txt)
	{
		Scanner scanner = new Scanner(editInputKey(txt));
		while (scanner.hasNext())
		{
			String input = scanner.next();
			boolean checker = hashMap.containsKey(input);
			if (checker)
			{
				int occurences = hashMap.get(input);
				hashMap.put(input, ++occurences);
			}
			else
			{
				hashMap.put(input, 1);
			}
		}
	}
	public byte[] getDocumentAsPdf() {
		if (pdfBytes == null)
		{
			PDDocument doc = new PDDocument();
			PDPage page = new PDPage();
			doc.addPage(page);
			PDFont font = PDType1Font.HELVETICA_BOLD;
			try
			{
				PDPageContentStream contents = new PDPageContentStream(doc, page);
				contents.beginText();
				contents.setFont(font, 12);
				contents.showText(txt);
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
		}
		if (pdfBytes != null)
		{
			return pdfBytes;
		}
		return new byte[0];//this is probably wrong and if an ecemption happens, but something better than this should happen
	}
	public String getDocumentAsTxt() {
		return this.txt;
	}

	public int getDocumentTextHashCode() {
		return this.txtHash;
	}

	public URI getKey() {
		return this.uri;
	}
	public int wordCount(String word)
	{
		if (!hashMap.containsKey(editInputKey(word)))
		{
			return 0;
		}
		return hashMap.get(editInputKey(word));
	}
	private String editInputKey(String key)
	{
		String deleteCharacters =  key.replaceAll("[^A-Za-z0-9 ]","");
		String input = deleteCharacters.toUpperCase();
		return input;
	}

	public long getLastUseTime() {
		return 0;
	}

	public void setLastUseTime(long timeInMilliseconds) {

	}

	public int compareTo(Document o) {
		return 0;
	}
}
