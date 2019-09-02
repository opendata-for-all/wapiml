package edu.uoc.som.wapiml.test.simple;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import org.junit.Test;

import edu.uoc.som.openapi2.API;
import edu.uoc.som.openapi2.io.OpenAPI2Builder;
import edu.uoc.som.openapi2.io.model.SerializationFormat;
import edu.uoc.som.wapiml.generators.ClassDiagramGenerator;

public class TestPrimitiveAsModel {

	@Test
	public void testGenerateAndSaveClassDiagramURI() throws IOException {

		try {
			File input = new File("resources/inputs/petstore-pt.json");
			API apiModel = new OpenAPI2Builder().setSerializationFormat(SerializationFormat.JSON).fromFile(input);
			ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator(apiModel, "petstore-pt", false, true);
			classDiagramGenerator.generateClassDiagramFromOpenAPI();

		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

	}

}
