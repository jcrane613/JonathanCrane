package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.stage5.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DocumentImpl implements Document {
	private URI uri;
	private String txt;
	private int txtHash;
	private byte[] pdfBytes;
	private HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
	private long lastTime;
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
		convertTxtToPdf();
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
	private void convertTxtToPdf() {
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
			this.pdfBytes = byteArrayOutputStream.toByteArray();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public byte[] getDocumentAsPdf()
	{
		return pdfBytes;
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
		return lastTime;
	}

	public void setLastUseTime(long timeInMilliseconds) {
		this.lastTime = timeInMilliseconds;
	}

	@Override
	public Map<String, Integer> getWordMap() {
		return hashMap;
	}

	@Override
	public void setWordMap(Map<String, Integer> wordMap) {
		this.hashMap = (HashMap<String, Integer>) wordMap;
	}

	@Override
	public int compareTo(Document o) {
		if (getLastUseTime() == o.getLastUseTime())
			return 0;
		if (getLastUseTime() > o.getLastUseTime())
			return 1;
		else return -1;
	}
}

