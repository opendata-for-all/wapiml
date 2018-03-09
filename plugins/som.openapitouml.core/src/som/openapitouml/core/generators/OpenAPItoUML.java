package som.openapitouml.core.generators;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.uoc.som.openapi.Root;
import edu.uoc.som.openapi.io.OpenAPIImporter;


public class OpenAPItoUML {

	public static Model generateClassDiagram(File file, String modelName) throws FileNotFoundException, UnsupportedEncodingException {
		InputStream in = new FileInputStream(file);
        Reader reader = new InputStreamReader(in, "UTF-8");
		JsonParser parser = new JsonParser();
		JsonElement jsonElement =  parser.parse(reader);
		return generateClassDiagram(jsonElement.getAsJsonObject(), modelName);
	
	}
	public static Model generateClassDiagram(JsonObject jsonObject, String modelName) throws FileNotFoundException, UnsupportedEncodingException {

		ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator();
		OpenAPIImporter openAPIImporter = new OpenAPIImporter();
		Root openAPIRoot = openAPIImporter.createOpenAPIModelFromJson(jsonObject);
		return classDiagramGenerator.generateClassDiagramFromOpenAPI(openAPIRoot, modelName);
	
	}
	public static void genrateAndSaveClassDiagram(File definitionFile, String modelName, String location) throws IOException {
		InputStream in = new FileInputStream(definitionFile);
        Reader reader = new InputStreamReader(in, "UTF-8");
		JsonParser parser = new JsonParser();
		JsonElement jsonElement =  parser.parse(reader);
		ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator();
		OpenAPIImporter openAPIImporter = new OpenAPIImporter();
		Root openAPIRoot = openAPIImporter.createOpenAPIModelFromJson(jsonElement.getAsJsonObject());
		Model model =  classDiagramGenerator.generateClassDiagramFromOpenAPI(openAPIRoot, modelName);
		classDiagramGenerator.saveClassDiagram(model,
				URI.createPlatformResourceURI(location, true)
						.appendSegment(modelName)
						.appendFileExtension("uml"));
	}
	
	
}
