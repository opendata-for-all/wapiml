package edu.uoc.som.wapiml.test.profile;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import edu.uoc.som.wapiml.facade.WAPImlFacade;

@DisplayName("Test OpenAPItoUML Facade - profile")
class TestOpenAPIToUMLFacade {

	@DisplayName("Test generate model")
	@Test
	void testGenerateClassDiagram() {
		
	        File input = new File("inputs/petstore.json");
	        File output = new File("outputs/profile/petstore.uml");
	       
	        try {
	        	 WAPImlFacade wAPImlFacade = new WAPImlFacade();
	        	wAPImlFacade.generateClassDiagram(input, "petstore",output, true, true);
			} catch (IOException | ProcessingException e) {
				fail(e.getLocalizedMessage());
			}
	       
	}
	@DisplayName("Test generate and save model using file")
	@Test
	void testGenerateAndSaveClassDiagramFile() {
		
	        File input = new File("inputs/petstore.json");
	        File output = new File("outputs/profile/petstore.uml");
	        try {
	        	 WAPImlFacade wAPImlFacade = new WAPImlFacade();
	        	 wAPImlFacade.generateAndSaveClassDiagram(input, "petstore", output, true, true);
			} catch (IOException | ProcessingException e) {
				fail(e.getLocalizedMessage());
			}
	       
	}
	

}
