package edu.uoc.som.wapiml.test.validation;

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
	        File output = new File("outputs/petstore.uml");
	        
	        try {
	        	WAPImlFacade wAPImlFacade = new WAPImlFacade();
	        	wAPImlFacade.generateClassDiagram(input, "petstore",output, false, true);
			} catch (IOException | ProcessingException | OpenAPIValidationException e ) {
				fail(e.getLocalizedMessage());
			}
	       
	}
	@DisplayName("Test generate model using invalid def")
	@Test
	void testGenerateClassDiagramInvalidDef() {
		  File input = new File("inputs/bad-petstore.json");
		  File output = new File("outputs/simple/bad-petstore.uml");
		 

		Throwable exception = assertThrows(OpenAPIValidationException.class, () -> 
	    {
	    	 WAPImlFacade wAPImlFacade = new WAPImlFacade();
	    	 wAPImlFacade.generateClassDiagram(input, "petstore", output, false, true);
	    });
		
	       LOG.info(exception.getMessage());
	}
	
	
	

}
