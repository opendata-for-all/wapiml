package edu.uoc.som.openapitouml.facade;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Model;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.uoc.som.openapi.Root;
import edu.uoc.som.openapi.io.ExporterBuilder;
import edu.uoc.som.openapi.io.OpenAPIExporter;
import edu.uoc.som.openapi.io.OpenAPIImporter;
import edu.uoc.som.openapi.io.utils.IOUtils;
import edu.uoc.som.openapitouml.exception.OpenAPIValidationException;
import edu.uoc.som.openapitouml.generators.ClassDiagramGenerator;
import edu.uoc.som.openapitouml.generators.OpenAPIModelGenerator;
import edu.uoc.som.openapitouml.model.OpenAPIValidationReport;
import edu.uoc.som.openapitouml.validator.OpenAPIValidator;


public class OpenAPItoUMLFacade {
	
	private ClassDiagramGenerator classDiagramGenerator;
	private OpenAPIImporter openAPIImporter;
	private OpenAPIModelGenerator openAPIModelGenerator;
	
	
	public OpenAPItoUMLFacade () throws IOException {
		classDiagramGenerator = new ClassDiagramGenerator();
		openAPIImporter = new OpenAPIImporter();
		openAPIModelGenerator = new OpenAPIModelGenerator();
	}

	public OpenAPIValidationReport validateOpenAPIDefinition(File definitionFile) throws ProcessingException, IOException{
		OpenAPIValidator openAPIValidator = new OpenAPIValidator();
		return openAPIValidator.validate(definitionFile);
	}
	public Model generateClassDiagram(File definitionFile, String modelName, File targetFile, boolean appyProfile, boolean validate) throws IOException, ProcessingException {
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
		return classDiagramGenerator.generateClassDiagramFromOpenAPI(openAPIRoot, modelName, targetFile, appyProfile);
	
	}
	
	public void generateAndSaveClassDiagram(File definitionFile, String modelName,  File targetFile, boolean appyProfile, boolean validate) throws IOException, ProcessingException {
		 generateClassDiagram(definitionFile, modelName,targetFile, appyProfile, validate);
		 classDiagramGenerator.saveClassDiagram();
		
	}
	public void generateAndSaveOpenAPIDefinition(File umlFile, String modelName,  File targetFile) throws IOException, ProcessingException {
		Root openAPIRoot = openAPIModelGenerator.umlToModel(URI.createFileURI(umlFile.getPath()));
		OpenAPIExporter exporter = ExporterBuilder.create();
		JsonObject jsonDefinition = exporter.toJson(openAPIRoot.getApi());
		IOUtils.saveOpenAPIDefintion(jsonDefinition, targetFile);
	}
	

	


	
	
}
