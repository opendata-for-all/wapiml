package edu.uoc.som.wapiml.validator;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import edu.uoc.som.wapiml.model.OpenAPIValidationReport;

public class OpenAPIValidator {
	JsonSchema openAPIschema = null;
	
	
	
	public OpenAPIValidator() throws IOException, ProcessingException {
		JsonNode openAPIJson = null;
		File resourceFile = new File("resources/schema.json");
		
		if(resourceFile.exists())
			openAPIJson= JsonLoader.fromFile(resourceFile);
		else {
			URL resource =  new URL("platform:/plugin/edu.uoc.som.wapiml/resources/schema.json");
			openAPIJson= JsonLoader.fromURL(resource);
		}
		JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		openAPIschema = factory.getJsonSchema(openAPIJson);
	}
	
	public OpenAPIValidationReport validate(File openAPIDef) throws ProcessingException, IOException {
		return new OpenAPIValidationReport(openAPIschema.validate(JsonLoader.fromFile(openAPIDef)));
	}
}
