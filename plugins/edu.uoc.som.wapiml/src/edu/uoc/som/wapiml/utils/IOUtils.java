package edu.uoc.som.wapiml.utils;

import java.io.File;
import java.io.IOException;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.google.gson.JsonObject;

import edu.uoc.som.openapi2.Root;
import edu.uoc.som.openapi2.io.ExporterBuilder;
import edu.uoc.som.openapi2.io.OpenAPIExporter;
import edu.uoc.som.openapi2.io.OpenAPIImporter;
import edu.uoc.som.wapiml.exception.OpenAPIValidationException;
import edu.uoc.som.wapiml.generators.OpenAPIModelGenerator;
import edu.uoc.som.wapiml.model.OpenAPIValidationReport;
import edu.uoc.som.wapiml.validator.OpenAPIValidator;

public class IOUtils {
	
	public static Root loadOpenAPIModel(File inputFile) throws IOException, ProcessingException {
		OpenAPIValidator openAPIValidator = new OpenAPIValidator();
		OpenAPIValidationReport report = openAPIValidator.validate(inputFile);
		if(!report.isSuccess()){
			throw new OpenAPIValidationException("Invalid OpenAPI definition\n"+report.getError());
		}
		
	return new OpenAPIImporter().createOpenAPIModelFromJson(inputFile);
	}
	public static void convertAndSaveOpenAPIDefinition(File inputFile, File targetFile) throws IOException {
		OpenAPIModelGenerator openAPIModelGenerator = new OpenAPIModelGenerator(inputFile);
		Root openAPIModelRoot = openAPIModelGenerator.generate();
		OpenAPIExporter exporter = ExporterBuilder.create();
		JsonObject jsonDefinition = exporter.toJson(openAPIModelRoot.getApi());
		edu.uoc.som.openapi2.io.utils.IOUtils.saveOpenAPIDefintion(jsonDefinition, targetFile);
	}

}
