package som.openapitouml.core.generators;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.eclipse.uml2.uml.Model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OpenAPItoUML {

	public static Model generateClassDiagram(File file) throws FileNotFoundException, UnsupportedEncodingException {
		InputStream in = new FileInputStream(file);
        Reader reader = new InputStreamReader(in, "UTF-8");
		JsonParser parser = new JsonParser();
		JsonElement jsonElement =  parser.parse(reader);
		return generateClassDiagram(jsonElement.getAsJsonObject());
	
	}
	public static Model generateClassDiagram(JsonObject jsonObject) throws FileNotFoundException, UnsupportedEncodingException {
		
		return null;
	}
	public static void genrateAndSaveClassDiagram(File file, String location) {
		
	}
	
}
