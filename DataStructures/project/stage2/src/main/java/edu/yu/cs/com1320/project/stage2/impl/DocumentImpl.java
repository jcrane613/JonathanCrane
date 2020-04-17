package edu.yu.cs.com1320.project.stage2.impl;

import edu.yu.cs.com1320.project.stage2.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

public class DocumentImpl implements Document
{
	private URI uri;
	private String txt;
	private int txtHash;
	private byte[] pdfBytes;
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
		this.pdfBytes = pdfBytes;
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
				contents.newLineAtOffset(20, 20);
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
}

