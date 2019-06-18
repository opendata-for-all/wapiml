package edu.uoc.som.wapiml.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import edu.uoc.som.wapiml.exception.OpenAPIValidationException;
import edu.uoc.som.wapiml.facade.WAPImlFacade;

@DisplayName("Test Open API validator")
class TestOpenAPIValidator {

	static final Logger LOG = LogManager.getLogger(TestOpenAPIValidator.class);
	@DisplayName("Test generate model using valid def")
	@Test
	void testGenerateClassDiagramValidDef() {
		
	        File input = new File("inputs/petstore.json");
	        WAPImlFacade openAPItoUMLFacade = new WAPImlFacade();
	        try {
	        	
	        openAPItoUMLFacade.generateClassDiagram(input, "petstore", true);
			} catch (IOException | ProcessingException | OpenAPIValidationException e ) {
				fail(e.getLocalizedMessage());
			}
	       
	}
	@DisplayName("Test generate model using invalid def")
	@Test
	void testGenerateClassDiagramInvalidDef() {
		  File input = new File("inputs/bad-petstore.json");
		  WAPImlFacade openAPItoUMLFacade = new WAPImlFacade();

		Throwable exception = assertThrows(OpenAPIValidationException.class, () -> 
	    {
	    	 openAPItoUMLFacade.generateClassDiagram(input, "petstore", true);
	    });
		
	       LOG.info(exception.getMessage());
	}
	
	
	

}
