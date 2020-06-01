package edu.yu.cs.com1320.project.stage5.impl;

import com.google.gson.*;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

/**
 * created by the document store and given to the BTree via a call to BTree.setPersistenceManager
 */
public class DocumentPersistenceManager implements PersistenceManager<URI, Document> {
	private File basedir;
	public DocumentPersistenceManager(File baseDir) throws IOException {
		if (baseDir == null)
		{
			String current = System.getProperty("user.dir");
			Path path = Paths.get(current);
			this.basedir = path.toFile();
		}
		if (baseDir != null)
		{
			this.basedir = baseDir;
		}
	}

	@Override
	public void serialize(URI uri, Document val) throws IOException {
		DocumentSer doc = new DocumentSer();
		JsonObject json = doc.serialize(val, Document.class,null);
		String finaldir = null;
		if (uri.getHost() == null)
		{
			finaldir = basedir.getPath()+File.separator+uri.getPath().replace("/",File.separator)+".json";
		}
		else finaldir = basedir.getPath()+File.separator+uri.getHost().replace("/",File.separator)+uri.getPath().replace("/",File.separator)+".json";
		String dir = basedir.getPath()+File.separator+uri.getHost()+uri.getPath().substring(0, uri.getPath().lastIndexOf("/"));
		File file = new File(basedir, uri.getHost()+uri.getPath()+".json");
		file.getParentFile().mkdirs();
		//Files.createDirectories(Paths.get(finaldir));
		//Files.createFile(Paths.get(finaldir));
		//String nameOfFile = uri.getPath().substring(uri.getPath().lastIndexOf('/') + 1).trim()+".json";
		FileWriter writer = new FileWriter(finaldir);
		writer.write(json.toString());
		writer.close();//close the writer
	}
	@Override
	public Document deserialize(URI uri) throws IOException {
		String finaldir = basedir+File.separator+uri.getHost()+uri.getPath()+".json";
		File file = new File(finaldir);
		if (!file.exists())
			return null;
		StringBuffer text = new StringBuffer();
		String currentLine = null;
		Scanner scanner = new Scanner( new File(finaldir) );
		while( scanner.hasNext() )
		{
			currentLine = scanner.nextLine();
			text.append(currentLine);
		}
		String dir = null;
		if (uri.getHost() == null)
		{
			dir= basedir+File.separator+uri.getPath().substring(0, uri.getPath().lastIndexOf("/"));
		}
		else
		{
			dir = basedir+File.separator+uri.getHost()+uri.getPath().substring(0, uri.getPath().lastIndexOf("/"));

		}
		JsonObject jsonObject = new JsonParser().parse(currentLine).getAsJsonObject();
		// try this as an alterantive so that don't need to worry about deprecated spftware jsonObject.getAsJsonObject(currentLine);
		new File(finaldir).delete();
		while (new File(dir).listFiles().length == 0)
		{
			if (dir.equals(basedir.toString()))
			{
				break;
			}
			File file2 = new File(dir);
			file2.delete();
			dir = dir.substring(0, dir.lastIndexOf("/"));
		}
		DocumentDeSer documentDeSer = new DocumentDeSer();
		return documentDeSer.deserialize(jsonObject, Document.class, null);

	}
}


class DocumentSer implements JsonSerializer<Document> {
	@Override
	public JsonObject serialize(Document doc, Type typeOfSrc, JsonSerializationContext context) {
		// This method gets involved whenever the parser encounters the Dog
		// object (for which this serializer is registered)
		JsonObject object = new JsonObject();
		//String txt = doc.getDocumentAsTxt().replaceAll(" ", "_");
		object.addProperty("text", doc.getDocumentAsTxt());
		object.addProperty("URI", doc.getKey().toString());
		object.addProperty("hashcode",doc.getDocumentTextHashCode());
		object.addProperty("hashmap",doc.getWordMap().toString());
		// we create the json object for the dog and send it back to the Gson serializer
		return object;
	}
}
class DocumentDeSer implements JsonDeserializer<Document> {
	@Override
	public Document deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		String name = json.getAsJsonObject().get("text").getAsString();
		//name = name.replaceAll("_", " ");
		String uri = json.getAsJsonObject().get("URI").getAsString();
		int hashcode = json.getAsJsonObject().get("hashcode").getAsInt();
		String hashmap = json.getAsJsonObject().get("hashmap").getAsString();
		URI newUri = null;
		try
		{
			newUri = new URI(uri);
		} catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
		Document doc = new DocumentImpl(newUri, name, hashcode);
		doc.setWordMap(convertToHash(hashmap));
		return doc;
	}
	private HashMap convertToHash(String string) {
		//new HashMap object
		HashMap<String, Integer> hMapData = new HashMap<>();

		//split the String by a comma
		String[] parts = string.split(",");
		//iterate the parts and add them to a map
		for (String part : parts)
		{
			//split the employee data by = to get string and int
			String[] empdata = part.split("=");
			String strId = editInputKey(empdata[0]).trim();
			String newNum = editInputKey(empdata[1]);
			int num = Integer.parseInt(newNum.trim());
			//add to map
			hMapData.put(strId.replaceAll("[^A-Za-z0-9 ]",""), num);
		}
		return hMapData;
	}
	private String editInputKey(String key)
	{
		String deleteCharacters =  key.replaceAll("[^A-Za-z0-9 ]","");
		String input = deleteCharacters.toUpperCase();
		return input;
	}
}