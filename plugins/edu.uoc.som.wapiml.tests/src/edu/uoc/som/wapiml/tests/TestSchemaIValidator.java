package edu.uoc.som.wapiml.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

@DisplayName("Test Schema validator")
public class TestSchemaIValidator {

	static final Logger LOGGER = LogManager.getLogger(TestSchemaIValidator.class);
	static JsonSchema openAPISchema = null;
	
	@BeforeAll
	public static void init() throws ProcessingException, IOException {
		JsonNode openAPIJson = JsonLoader.fromFile(new File("resources/schema.json"));
		JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		openAPISchema = factory.getJsonSchema(openAPIJson);
	}
	
	@DisplayName("Test valid definition")
	@Test
	public void TestGoodDefinition() {
		JsonNode good;
		ProcessingReport report;
		try {
			good = JsonLoader.fromFile(new File("inputs/petstore.json"));

			report = openAPISchema.validate(good);
			LOGGER.info(report.toString());
			assertTrue(report.isSuccess());
			
		} catch (IOException | ProcessingException e) {
			fail(e.getLocalizedMessage());
		}

	}
	
	@DisplayName("Test invalid definition")
	@Test
	public void TestBadDefinition() {
		JsonNode bad;
		ProcessingReport report;
		try {
			bad = JsonLoader.fromFile(new File("inputs/bad-petstore.json"));
			report = openAPISchema.validate(bad);
			LOGGER.info(report.toString());
			assertFalse(report.isSuccess());
			
		} catch (IOException | ProcessingException e) {
			fail(e.getLocalizedMessage());
		}

	}
}
