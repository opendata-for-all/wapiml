package edu.uoc.som.wapiml.utils;

import java.io.File;
import java.io.IOException;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import edu.uoc.som.openapi2.API;
import edu.uoc.som.openapi2.io.ExporterBuilder;
import edu.uoc.som.openapi2.io.OpenAPI2Importer;
import edu.uoc.som.openapi2.io.model.SerializationFormat;
import edu.uoc.som.wapiml.exception.OpenAPIValidationException;
import edu.uoc.som.wapiml.generators.OpenAPIModelGenerator;
import edu.uoc.som.wapiml.model.OpenAPIValidationReport;
import edu.uoc.som.wapiml.validator.OpenAPIValidator;

public class Utils {
	
	public static API loadOpenAPIModel(File inputFile) throws IOException, ProcessingException {
		OpenAPIValidator openAPIValidator = new OpenAPIValidator();
		OpenAPIValidationReport report = openAPIValidator.validate(inputFile);
		if(!report.isSuccess()){
			throw new OpenAPIValidationException("Invalid OpenAPI definition\n"+report.getError());
		}
		
	return new OpenAPI2Importer().createOpenAPI2ModelFromFile(inputFile, SerializationFormat.JSON);
	}
	public static void convertAndSaveOpenAPIDefinition(File inputFile, File targetFile) throws IOException {
		OpenAPIModelGenerator openAPIModelGenerator = new OpenAPIModelGenerator(inputFile);
		API api = openAPIModelGenerator.generate();
		String jsonDefinition = new ExporterBuilder().setJsonPrettyPrinting().exportJson(api);
		edu.uoc.som.openapi2.io.utils.Utils.saveOpenAPIDefintion(jsonDefinition, targetFile);
	}

}
