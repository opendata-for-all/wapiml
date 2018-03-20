package edu.uoc.som.openapitouml.facade;

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
import com.google.gson.JsonParser;

import edu.uoc.som.openapi.Root;
import edu.uoc.som.openapi.io.OpenAPIImporter;
import edu.uoc.som.openapitouml.generators.ClassDiagramGenerator;


public class OpenAPItoUMLFacade {
	
	private ClassDiagramGenerator classDiagramGenerator;
	private OpenAPIImporter openAPIImporter;
	
	public OpenAPItoUMLFacade () {
		classDiagramGenerator = new ClassDiagramGenerator();
		openAPIImporter = new OpenAPIImporter();
	}

	public Model generateClassDiagram(File definitionFile, String modelName) throws FileNotFoundException, UnsupportedEncodingException {
		InputStream in = new FileInputStream(definitionFile);
        Reader reader = new InputStreamReader(in, "UTF-8");
		JsonElement jsonElement =  (new JsonParser()).parse(reader);
		Root openAPIRoot  = openAPIImporter.createOpenAPIModelFromJson(jsonElement.getAsJsonObject());
		return classDiagramGenerator.generateClassDiagramFromOpenAPI(openAPIRoot, modelName);
	
	}
	
	public void generateAndSaveClassDiagram(File definitionFile, String modelName, File location) throws IOException {
		Model model = generateClassDiagram(definitionFile, modelName);
		classDiagramGenerator.saveClassDiagram(model,
				URI.createFileURI(location.getPath())
						.appendSegment(modelName)
						.appendFileExtension("uml"));
	}
	public void generateAndSaveClassDiagram(File definitionFile, String modelName, URI output) throws IOException {
		Model model = generateClassDiagram(definitionFile, modelName);
		classDiagramGenerator.saveClassDiagram(model,
				output.appendSegment(modelName).appendFileExtension("uml"));
	}

	
	
}
