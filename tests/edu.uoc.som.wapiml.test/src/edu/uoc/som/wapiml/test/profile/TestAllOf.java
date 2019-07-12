package edu.uoc.som.wapiml.test.profile;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import edu.uoc.som.wapiml.facade.WAPImlFacade;


@DisplayName("Test allOf - profile")
class TestAllOf {

	
	@DisplayName("Test allOf")
	@Test
	void testGenerateAndSaveClassDiagramURI() {
		
	        File input = new File("inputs/allOf.json");
	        File output = new File("outputs/profile/allOf.uml");
	        
	        try {
	        	WAPImlFacade openAPItoUMLFacade = new WAPImlFacade();
	        	openAPItoUMLFacade.generateAndSaveClassDiagram(input, "allOf", output, true, true);
			} catch (IOException | ProcessingException e) {
				fail(e.getLocalizedMessage());
			}
	       
	}
	

}
