package edu.uoc.som.wapiml.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import org.eclipse.emf.common.util.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import edu.uoc.som.wapiml.facade.WAPImlFacade;

@DisplayName("Test OpenAPItoUML Facade")
class TestOpenAPIToUMLFacade {

	@DisplayName("Test generate model")
	@Test
	void testGenerateClassDiagram() {
		
	        File input = new File("inputs/petstore.json");
	        WAPImlFacade openAPItoUMLFacade = new WAPImlFacade();
	        try {
	        	
	        	openAPItoUMLFacade.generateClassDiagram(input, "petstore", true);
			} catch (IOException | ProcessingException e) {
				fail(e.getLocalizedMessage());
			}
	       
	}
	@DisplayName("Test generate and save model using file")
	@Test
	void testGenerateAndSaveClassDiagramFile() {
		
	        File input = new File("inputs/petstore.json");
	        File output = new File("outputs");
	        OpenAPItoUMLFacade openAPItoUMLFacade = new OpenAPItoUMLFacade();
	        try {
	        	openAPItoUMLFacade.generateAndSaveClassDiagram(input, "petstore", output, true);
			} catch (IOException | ProcessingException e) {
				fail(e.getLocalizedMessage());
			}
	       
	}
	@DisplayName("Test generate and save model using URI")
	@Test
	void testGenerateAndSaveClassDiagramURI() {
		
	        File input = new File("inputs/petstore.json");
	        URI outputURI = URI.createFileURI("outputs");
	        OpenAPItoUMLFacade openAPItoUMLFacade = new OpenAPItoUMLFacade();
	        try {
	        	openAPItoUMLFacade.generateAndSaveClassDiagram(input, "petstore", outputURI, true);
			} catch (IOException | ProcessingException e) {
				fail(e.getLocalizedMessage());
			}
	       
	}
	

}
