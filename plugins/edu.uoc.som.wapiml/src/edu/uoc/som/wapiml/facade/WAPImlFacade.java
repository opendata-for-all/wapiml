package edu.uoc.som.wapiml.facade;

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

import edu.uoc.som.openapi2.Root;
import edu.uoc.som.openapi2.io.ExporterBuilder;
import edu.uoc.som.openapi2.io.OpenAPIExporter;
import edu.uoc.som.openapi2.io.OpenAPIImporter;
import edu.uoc.som.openapi2.io.utils.IOUtils;
import edu.uoc.som.wapiml.exception.OpenAPIValidationException;
import edu.uoc.som.wapiml.generators.ClassDiagramGenerator;
import edu.uoc.som.wapiml.generators.OpenAPIModelGenerator;
import edu.uoc.som.wapiml.model.OpenAPIValidationReport;
import edu.uoc.som.wapiml.validator.OpenAPIValidator;


public class WAPImlFacade {
	
	private ClassDiagramGenerator classDiagramGenerator;
	private OpenAPIImporter openAPIImporter;
	private OpenAPIModelGenerator openAPIModelGenerator;
	
	
	public WAPImlFacade () throws IOException {
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
				throw new OpenAPIValidationException("Invalid OpenAPI definition\n"+report.getError());
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
