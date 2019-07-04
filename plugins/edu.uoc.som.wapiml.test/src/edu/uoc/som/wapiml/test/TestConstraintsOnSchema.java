package edu.uoc.som.wapiml.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import edu.uoc.som.wapiml.facade.WAPImlFacade;

@DisplayName("Test constraints on Schema elements")
public class TestConstraintsOnSchema {

	private static ResourceSet RES_SET = new ResourceSetImpl();

	@BeforeAll
	private static void init() {
		RES_SET.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		RES_SET.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
				UMLResource.Factory.INSTANCE);
	}

	@DisplayName("Test maximum")
	@Test
	public void testMaximum() {
		File input = new File("inputs/constraints/max-min.json");
		File output = new File("outputs/constraints/max-min.uml");
		
		try {
			WAPImlFacade wAPImlFacade = new WAPImlFacade();
			wAPImlFacade.generateAndSaveClassDiagram(input, "max-min", output,false, true);
			Resource res = RES_SET.getResource(URI.createFileURI(output.toString()), true);
			Model model = (Model) res.getContents().get(0);
			Class concept = (Class) ((Package) model.getPackagedElement("max-min")).getPackagedElement("Concept");
			assertAll("maximum", 
					() -> assertNotNull(concept.getOwnedRule("Concept-attributeWithMaximum-maximumConstraint")),
					() -> {
						OpaqueExpression expression = (OpaqueExpression) ((Constraint)concept.getOwnedRule("Concept-attributeWithMaximum-maximumConstraint")).getSpecification();
						assertTrue(expression.getBodies().get(0).equals("self.attributeWithMaximum <= 2000.0"));
					});
			assertAll("exclusiveMaximum", 
					() -> assertNotNull(concept.getOwnedRule("Concept-attributeWithExclusiveMaximum-maximumConstraint")),
					() -> {
						OpaqueExpression expression = (OpaqueExpression) ((Constraint)concept.getOwnedRule("Concept-attributeWithExclusiveMaximum-maximumConstraint")).getSpecification();
						assertTrue(expression.getBodies().get(0).equals("self.attributeWithExclusiveMaximum < 2000.0"));
					});
			} catch (IOException | ProcessingException e) {
			fail(e.getLocalizedMessage());
		}
	}
	@DisplayName("Test minimum")
	@Test
	public void testMinimum() {
		File input = new File("inputs/constraints/max-min.json");
		File output = new File("outputs/constraints/max-min.uml");
		try {
			WAPImlFacade wAPImlFacade = new WAPImlFacade();
			wAPImlFacade.generateAndSaveClassDiagram(input, "max-min", output ,false, true);
			Resource res = RES_SET.getResource(URI.createFileURI(output.toString()), true);
			Model model = (Model) res.getContents().get(0);
			Class concept = (Class) ((Package) model.getPackagedElement("max-min")).getPackagedElement("Concept");
			
			assertAll("minimum", 
					() -> assertNotNull(concept.getOwnedRule("Concept-attributeWithMinimum-minimumConstraint")),
					() -> {
						OpaqueExpression expression = (OpaqueExpression) ((Constraint)concept.getOwnedRule("Concept-attributeWithMinimum-minimumConstraint")).getSpecification();
						assertTrue(expression.getBodies().get(0).equals("self.attributeWithMinimum >= 1.0"));
					});
			assertAll("exclusiveMinimum", 
					() -> assertNotNull(concept.getOwnedRule("Concept-attributeWithExclusiveMinimum-minimumConstraint")),
					() -> {
						OpaqueExpression expression = (OpaqueExpression) ((Constraint)concept.getOwnedRule("Concept-attributeWithExclusiveMinimum-minimumConstraint")).getSpecification();
						assertTrue(expression.getBodies().get(0).equals("self.attributeWithExclusiveMinimum > 1.0"));
					});
		
		
		} catch (IOException | ProcessingException e) {
			fail(e.getLocalizedMessage());
		}
	}
		
		@DisplayName("Test multipleOf")
		@Test
		public void testMultipleOf() {
			File input = new File("inputs/constraints/multipleOf.json");
			File output = new File("outputs/constraints/multipleOf.uml");
			try {
				WAPImlFacade wAPImlFacade = new WAPImlFacade();
				wAPImlFacade.generateAndSaveClassDiagram(input, "multipleOf", output,false, true);
				Resource res = RES_SET.getResource(URI.createFileURI(output.toString()), true);
				Model model = (Model) res.getContents().get(0);
				Class concept = (Class) ((Package) model.getPackagedElement("multipleOf")).getPackagedElement("Concept");
				
				assertAll("multipleOf", 
						() -> assertNotNull(concept.getOwnedRule("Concept-attributeWithMultipleOf-multipleOfConstraint")),
						() -> {
							OpaqueExpression expression = (OpaqueExpression) ((Constraint)concept.getOwnedRule("Concept-attributeWithMultipleOf-multipleOfConstraint")).getSpecification();
							assertTrue(expression.getBodies().get(0).equals("self.attributeWithMultipleOf.div(2.0) = 0"));
						});
				
			
			} catch (IOException | ProcessingException e) {
				fail(e.getLocalizedMessage());
			}
	}
		
		@DisplayName("Test maxLength")
		@Test
		public void testMaxLength() {
			File input = new File("inputs/constraints/length.json");
			File output = new File("outputs/constraints/length.uml");
			
			try {
				WAPImlFacade wAPImlFacade = new WAPImlFacade();
				wAPImlFacade.generateAndSaveClassDiagram(input, "length", output, false, true);
				Resource res = RES_SET.getResource(URI.createFileURI(output.toString()), true);
				Model model = (Model) res.getContents().get(0);
				Class concept = (Class) ((Package) model.getPackagedElement("length")).getPackagedElement("Concept");
				
				assertAll("maxLength", 
						() -> assertNotNull(concept.getOwnedRule("Concept-attributeWithMaxLength-maxLengthConstraint")),
						() -> {
							OpaqueExpression expression = (OpaqueExpression) ((Constraint)concept.getOwnedRule("Concept-attributeWithMaxLength-maxLengthConstraint")).getSpecification();
							assertTrue(expression.getBodies().get(0).equals("self.attributeWithMaxLength.size() <= 20"));
						});
				
			
			} catch (IOException | ProcessingException e) {
				fail(e.getLocalizedMessage());
			}
	}
		
		@DisplayName("Test minLength")
		@Test
		public void testMinLength() {
			File input = new File("inputs/constraints/length.json");
			File output = new File("outputs/constraints/length.uml");
			
			try {
				WAPImlFacade wAPImlFacade = new WAPImlFacade();
				wAPImlFacade.generateAndSaveClassDiagram(input, "length", output, false, true);
				Resource res = RES_SET.getResource(URI.createFileURI(output.toString()), true);
				Model model = (Model) res.getContents().get(0);
				Class concept = (Class) ((Package) model.getPackagedElement("length")).getPackagedElement("Concept");
				
				assertAll("maxLength", 
						() -> assertNotNull(concept.getOwnedRule("Concept-attributeWithMinLength-minLengthConstraint")),
						() -> {
							OpaqueExpression expression = (OpaqueExpression) ((Constraint)concept.getOwnedRule("Concept-attributeWithMinLength-minLengthConstraint")).getSpecification();
							assertTrue(expression.getBodies().get(0).equals("self.attributeWithMinLength.size() >= 1"));
						});
				
			
			} catch (IOException | ProcessingException e) {
				fail(e.getLocalizedMessage());
			}
	}
}
