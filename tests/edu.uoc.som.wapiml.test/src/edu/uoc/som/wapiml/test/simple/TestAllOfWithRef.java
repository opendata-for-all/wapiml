package edu.uoc.som.wapiml.test.simple;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.junit.Test;

import edu.uoc.som.openapi2.API;
import edu.uoc.som.openapi2.io.OpenAPI2Builder;
import edu.uoc.som.openapi2.io.model.SerializationFormat;
import edu.uoc.som.wapiml.generators.ClassDiagramGenerator;

public class TestAllOfWithRef {

	@Test
	public void testGenerateAndSaveClassDiagramURI() {

		try {
			File input = new File("resources/inputs/allOf-with-ref.json");
			API apiModel = new OpenAPI2Builder().setSerializationFormat(SerializationFormat.JSON).fromFile(input);
			ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator(apiModel, "allOf-with-ref", false,
					true);
			Model model = classDiagramGenerator.generateClassDiagramFromOpenAPI();
			Class fish = (Class) ((Package) model.getPackagedElements().get(0)).getOwnedMember("Fish");
			assertTrue(!fish.getAssociations().isEmpty());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

	}

}
