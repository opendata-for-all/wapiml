package edu.uoc.som.openapitouml.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.uoc.som.openapitouml.facade.OpenAPItoUMLFacade;


class TestOpenAPIToUMLFacade {

	@DisplayName("Generate model")
	@Test
	void testGenerateClassDiagram() {
		
	        File input = new File("inputs/petstore.json");
	        OpenAPItoUMLFacade openAPItoUMLFacade = new OpenAPItoUMLFacade();
	        try {
	        	
	        	openAPItoUMLFacade.generateClassDiagram(input, "petstore");
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				fail(e.getLocalizedMessage());
			}
	       
	}
	@DisplayName("Generate and save model using file")
	@Test
	void testGenerateAndSaveClassDiagramFile() {
		
	        File input = new File("inputs/petstore.json");
	        File output = new File("outputs");
	        OpenAPItoUMLFacade openAPItoUMLFacade = new OpenAPItoUMLFacade();
	        try {
	        	openAPItoUMLFacade.generateAndSaveClassDiagram(input, "petstore", output);
			} catch (IOException e) {
				fail(e.getLocalizedMessage());
			}
	       
	}
	@DisplayName("Generate and save model using URI")
	@Test
	void testGenerateAndSaveClassDiagramURI() {
		
	        File input = new File("inputs/petstore.json");
	        URI outputURI = URI.createFileURI("outputs");
	        OpenAPItoUMLFacade openAPItoUMLFacade = new OpenAPItoUMLFacade();
	        try {
	        	openAPItoUMLFacade.generateAndSaveClassDiagram(input, "petstore", outputURI);
			} catch (IOException e) {
				fail(e.getLocalizedMessage());
			}
	       
	}

}
