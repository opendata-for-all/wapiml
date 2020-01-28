package edu.uoc.som.wapiml.test.model.gen.simple;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Package;
import org.junit.Test;

import edu.uoc.som.openapi2.API;
import edu.uoc.som.openapi2.io.OpenAPI2Builder;
import edu.uoc.som.openapi2.io.model.SerializationFormat;
import edu.uoc.som.wapiml.generators.ClassDiagramGenerator;



public class TestConstraintsOnSchema {



	@Test
	public void testMaximum() {
	
		
		try {
			File input = new File("resources/inputs/constraints/max-min.json");
			API apiModel = new OpenAPI2Builder().setSerializationFormat(SerializationFormat.JSON).fromFile(input);
			ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator(apiModel, "max-min", false,
					true);
			Model model = classDiagramGenerator.generateClassDiagramFromOpenAPI();
			Class concept = (Class) ((Package) model.getPackagedElement("max-min")).getPackagedElement("Concept");

		assertNotNull(concept.getOwnedRule("Concept-attributeWithMaximum-maximumConstraint"));
				
						OpaqueExpression expression = (OpaqueExpression) ((Constraint)concept.getOwnedRule("Concept-attributeWithMaximum-maximumConstraint")).getSpecification();
						assertTrue(expression.getBodies().get(0).equals("self.attributeWithMaximum <= 2000.0"));
				
	 
					assertNotNull(concept.getOwnedRule("Concept-attributeWithExclusiveMaximum-maximumConstraint"));
				
						OpaqueExpression expression2 = (OpaqueExpression) ((Constraint)concept.getOwnedRule("Concept-attributeWithExclusiveMaximum-maximumConstraint")).getSpecification();
						assertTrue(expression2.getBodies().get(0).equals("self.attributeWithExclusiveMaximum < 2000.0"));
			
			} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}
	@Test
	public void testMinimum() {
		
		try {
			File input = new File("resources/inputs/constraints/max-min.json");
			API apiModel = new OpenAPI2Builder().setSerializationFormat(SerializationFormat.JSON).fromFile(input);
			ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator(apiModel, "max-min", false,
					true);
			Model model = classDiagramGenerator.generateClassDiagramFromOpenAPI();
			Class concept = (Class) ((Package) model.getPackagedElement("max-min")).getPackagedElement("Concept");
			

					 assertNotNull(concept.getOwnedRule("Concept-attributeWithMinimum-minimumConstraint"));
				
						OpaqueExpression expression = (OpaqueExpression) ((Constraint)concept.getOwnedRule("Concept-attributeWithMinimum-minimumConstraint")).getSpecification();
						assertTrue(expression.getBodies().get(0).equals("self.attributeWithMinimum >= 1.0"));
				
	
				assertNotNull(concept.getOwnedRule("Concept-attributeWithExclusiveMinimum-minimumConstraint"));
				
						OpaqueExpression expression2 = (OpaqueExpression) ((Constraint)concept.getOwnedRule("Concept-attributeWithExclusiveMinimum-minimumConstraint")).getSpecification();
						assertTrue(expression2.getBodies().get(0).equals("self.attributeWithExclusiveMinimum > 1.0"));
				
		
		
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}
		
		@Test
		public void testMultipleOf() {
			try {
				File input = new File("resources/inputs/constraints/multipleOf.json");
				API apiModel = new OpenAPI2Builder().setSerializationFormat(SerializationFormat.JSON).fromFile(input);
				ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator(apiModel, "multipleOf", false,
						true);
				Model model = classDiagramGenerator.generateClassDiagramFromOpenAPI();
				Class concept = (Class) ((Package) model.getPackagedElement("multipleOf")).getPackagedElement("Concept");
				
		
			 assertNotNull(concept.getOwnedRule("Concept-attributeWithMultipleOf-multipleOfConstraint"));
					
							OpaqueExpression expression = (OpaqueExpression) ((Constraint)concept.getOwnedRule("Concept-attributeWithMultipleOf-multipleOfConstraint")).getSpecification();
							assertTrue(expression.getBodies().get(0).equals("self.attributeWithMultipleOf.div(2.0) = 0"));
				
				
			
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
	}
		
		@Test
		public void testMaxLength() {
	
			
			try {
				File input = new File("resources/inputs/constraints/length.json");
				API apiModel = new OpenAPI2Builder().setSerializationFormat(SerializationFormat.JSON).fromFile(input);
				ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator(apiModel, "length", false,
						true);
				Model model = classDiagramGenerator.generateClassDiagramFromOpenAPI();
				Class concept = (Class) ((Package) model.getPackagedElement("length")).getPackagedElement("Concept");
				
						 assertNotNull("maxLength",concept.getOwnedRule("Concept-attributeWithMaxLength-maxLengthConstraint"));
							OpaqueExpression expression = (OpaqueExpression) ((Constraint)concept.getOwnedRule("Concept-attributeWithMaxLength-maxLengthConstraint")).getSpecification();
							assertTrue("maxLength", expression.getBodies().get(0).equals("self.attributeWithMaxLength.size() <= 20"));
				
			
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
	}
		
		@Test
		public void testMinLength() {
			
			
			try {
				File input = new File("resources/inputs/constraints/length.json");
				API apiModel = new OpenAPI2Builder().setSerializationFormat(SerializationFormat.JSON).fromFile(input);
				ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator(apiModel, "length", false,
						true);
				Model model = classDiagramGenerator.generateClassDiagramFromOpenAPI();
				Class concept = (Class) ((Package) model.getPackagedElement("length")).getPackagedElement("Concept");
				
					 assertNotNull("minLength", concept.getOwnedRule("Concept-attributeWithMinLength-minLengthConstraint"));
							OpaqueExpression expression = (OpaqueExpression) ((Constraint)concept.getOwnedRule("Concept-attributeWithMinLength-minLengthConstraint")).getSpecification();
							assertTrue(expression.getBodies().get(0).equals("self.attributeWithMinLength.size() >= 1"));
				
			
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
	}
}
