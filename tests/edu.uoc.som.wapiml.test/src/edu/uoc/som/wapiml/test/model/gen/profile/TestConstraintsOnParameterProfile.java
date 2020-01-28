package edu.uoc.som.wapiml.test.model.gen.profile;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.junit.Test;

import edu.uoc.som.openapi2.API;
import edu.uoc.som.openapi2.io.OpenAPI2Builder;
import edu.uoc.som.openapi2.io.model.SerializationFormat;
import edu.uoc.som.wapiml.generators.ClassDiagramGenerator;

public class TestConstraintsOnParameterProfile {


	@Test
	public void testMaximum() {

		try {
			File input = new File("resources/inputs/constraints/parameters.json");
			API apiModel = new OpenAPI2Builder().setSerializationFormat(SerializationFormat.JSON).fromFile(input);
			ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator(apiModel, "parameters", true,
					true);
			Model model = classDiagramGenerator.generateClassDiagramFromOpenAPI();

			Class concept = (Class) ((Package) model.getPackagedElement("parameters")).getPackagedElement("Concept");
			Operation findConcepts = concept.getOperation("findConcepts", null, null);
			String constraintName = "findConcepts-parameterWithMaximum-maximumConstraint";
			String constraintValue = "self.parameterWithMaximum <= 10.0";
			assertNotNull(findConcepts.getOwnedRule(constraintName));
			OpaqueExpression expression = (OpaqueExpression) ((Constraint) findConcepts.getOwnedRule(constraintName))
					.getSpecification();
			assertTrue(expression.getBodies().get(0).equals(constraintValue));

		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testMinimum() {

		try {
			File input = new File("resources/inputs/constraints/parameters.json");

			API apiModel = new OpenAPI2Builder().setSerializationFormat(SerializationFormat.JSON).fromFile(input);
			ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator(apiModel, "parameters", true,
					true);
			Model model = classDiagramGenerator.generateClassDiagramFromOpenAPI();
			Class concept = (Class) ((Package) model.getPackagedElement("parameters")).getPackagedElement("Concept");
			Operation findConcepts = concept.getOperation("findConcepts", null, null);
			String constraintName = "findConcepts-parameterWithMinimum-minimumConstraint";
			String constraintValue = "self.parameterWithMinimum >= 10.0";
			assertNotNull(findConcepts.getOwnedRule(constraintName));

			OpaqueExpression expression = (OpaqueExpression) ((Constraint) findConcepts.getOwnedRule(constraintName))
					.getSpecification();
			assertTrue(expression.getBodies().get(0).equals(constraintValue));

		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

}
