package edu.uoc.som.openapitouml.validator;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class OpenAPIValidator {
	JsonSchema openAPIschema = null;
	
	public OpenAPIValidator() throws IOException, ProcessingException {
		JsonNode openAPIJson = JsonLoader.fromFile(new File("resources/schema.json"));
		JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		openAPIschema = factory.getJsonSchema(openAPIJson);
	}
	
	public ProcessingReport validate(File openAPIDef) throws ProcessingException, IOException {
		return openAPIschema.validate(JsonLoader.fromFile(openAPIDef));
	}
}
