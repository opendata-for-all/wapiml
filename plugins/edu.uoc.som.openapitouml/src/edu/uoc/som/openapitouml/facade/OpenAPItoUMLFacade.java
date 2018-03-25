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

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import edu.uoc.som.openapi.Root;
import edu.uoc.som.openapi.io.OpenAPIImporter;
import edu.uoc.som.openapitouml.exception.OpenAPIValidationException;
import edu.uoc.som.openapitouml.exception.OpenAPItoUMLRuntimeException;
import edu.uoc.som.openapitouml.generators.ClassDiagramGenerator;
import edu.uoc.som.openapitouml.model.OpenAPIValidationReport;
import edu.uoc.som.openapitouml.validator.OpenAPIValidator;


public class OpenAPItoUMLFacade {
	
	private ClassDiagramGenerator classDiagramGenerator;
	private OpenAPIImporter openAPIImporter;
	
	public OpenAPItoUMLFacade () {
		classDiagramGenerator = new ClassDiagramGenerator();
		openAPIImporter = new OpenAPIImporter();
	}

	public OpenAPIValidationReport validateOpenAPIDefinition(File definitionFile) throws ProcessingException, IOException{
		OpenAPIValidator openAPIValidator = new OpenAPIValidator();
		return openAPIValidator.validate(definitionFile);
	}
	public Model generateClassDiagram(File definitionFile, String modelName, boolean validate) throws IOException, ProcessingException {
		if(validate) {
			OpenAPIValidator openAPIValidator = new OpenAPIValidator();
			OpenAPIValidationReport report = openAPIValidator.validate(definitionFile);
			if(!report.isSuccess()){
				throw new OpenAPIValidationException("Invalid Open API definition\n"+report.toString());
			}
			
		}
		InputStream in = new FileInputStream(definitionFile);
        Reader reader = new InputStreamReader(in, "UTF-8");
		JsonElement jsonElement =  (new JsonParser()).parse(reader);
		Root openAPIRoot  = openAPIImporter.createOpenAPIModelFromJson(jsonElement.getAsJsonObject());
		return classDiagramGenerator.generateClassDiagramFromOpenAPI(openAPIRoot, modelName);
	
	}
	
	public void generateAndSaveClassDiagram(File definitionFile, String modelName, File location,  boolean validate) throws IOException, ProcessingException {
		Model model = generateClassDiagram(definitionFile, modelName, validate);
		classDiagramGenerator.saveClassDiagram(model,
				URI.createFileURI(location.getPath())
						.appendSegment(modelName)
						.appendFileExtension("uml"));
	}
	public void generateAndSaveClassDiagram(File definitionFile, String modelName, URI output, boolean validate) throws IOException, ProcessingException {
		Model model = generateClassDiagram(definitionFile, modelName, validate);
		classDiagramGenerator.saveClassDiagram(model,
				output.appendSegment(modelName).appendFileExtension("uml"));
	}

	
	
}
