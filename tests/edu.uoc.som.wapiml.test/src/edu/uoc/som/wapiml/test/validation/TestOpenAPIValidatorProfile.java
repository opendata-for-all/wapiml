package edu.uoc.som.wapiml.test.validation;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import edu.uoc.som.openapi2.API;
import edu.uoc.som.openapi2.io.OpenAPI2Builder;
import edu.uoc.som.openapi2.io.exceptions.OpenAPIValidationException;
import edu.uoc.som.openapi2.io.model.SerializationFormat;
import edu.uoc.som.wapiml.generators.ClassDiagramGenerator;

public class TestOpenAPIValidatorProfile {

	@Test
	public void testGenerateClassDiagramValidDef() {

		try {
			File input = new File("resources/inputs/petstore.json");
			API apiModel = new OpenAPI2Builder().setSerializationFormat(SerializationFormat.JSON).fromFile(input);
			ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator(apiModel, "allOf", false, true);
			classDiagramGenerator.generateClassDiagramFromOpenAPI();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

	}

	@Test(expected = OpenAPIValidationException.class)
	public void testGenerateClassDiagramInvalidDef()
			throws  IOException {
		File input = new File("resources/inputs/bad-petstore.json");
		API apiModel = new OpenAPI2Builder().setSerializationFormat(SerializationFormat.JSON).fromFile(input);
		ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator(apiModel, "bad-petstore", false, true);
		classDiagramGenerator.generateClassDiagramFromOpenAPI();

	}

}
