package edu.uoc.som.openapitouml.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import org.eclipse.emf.common.util.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import edu.uoc.som.openapitouml.facade.OpenAPItoUMLFacade;

@DisplayName("Test allOf")
class TestAllOf {

	
	@DisplayName("Test allOf")
	@Test
	void testGenerateAndSaveClassDiagramURI() {
		
	        File input = new File("inputs/allOf.json");
	        URI outputURI = URI.createFileURI("outputs");
	        OpenAPItoUMLFacade openAPItoUMLFacade = new OpenAPItoUMLFacade();
	        try {
	        	openAPItoUMLFacade.generateAndSaveClassDiagram(input, "allOf", outputURI, true);
			} catch (IOException | ProcessingException e) {
				fail(e.getLocalizedMessage());
			}
	       
	}
	

}
