package edu.uoc.som.wapiml.utils;

import java.io.File;
import java.io.IOException;


import edu.uoc.som.openapi2.API;
import edu.uoc.som.openapi2.io.ExporterBuilder;
import edu.uoc.som.openapi2.io.OpenAPI2Importer;
import edu.uoc.som.openapi2.io.exception.OpenAPIValidationException;
import edu.uoc.som.openapi2.io.model.SerializationFormat;
import edu.uoc.som.wapiml.generators.OpenAPIModelGenerator;

public class IOUtils {

	public static API loadOpenAPIModel(File inputFile, SerializationFormat serializationFormat)
			throws IOException, OpenAPIValidationException {
		if (serializationFormat.equals(SerializationFormat.JSON))
			return new OpenAPI2Importer().createOpenAPI2ModelFromFile(inputFile, SerializationFormat.JSON);
		if (serializationFormat.equals(SerializationFormat.YAML))
			return new OpenAPI2Importer().createOpenAPI2ModelFromFile(inputFile, SerializationFormat.YAML);

		return null;
	}

	public static void convertAndSaveOpenAPIDefinition(File inputFile, File targetFile,
			SerializationFormat serializationFormat) throws IOException {
		OpenAPIModelGenerator openAPIModelGenerator = new OpenAPIModelGenerator(inputFile);
		API api = openAPIModelGenerator.generate();
		if (serializationFormat.equals(SerializationFormat.JSON)) {
			String jsonDefinition = new ExporterBuilder().setJsonPrettyPrinting().exportJson(api);
			edu.uoc.som.openapi2.io.utils.Utils.saveOpenAPIDefintion(jsonDefinition, targetFile);
		}
		if (serializationFormat.equals(SerializationFormat.YAML)) {
			String yamlDefinition = new ExporterBuilder().setJsonPrettyPrinting().exportYaml(api);
			edu.uoc.som.openapi2.io.utils.Utils.saveOpenAPIDefintion(yamlDefinition, targetFile);
		}

	}

}
