package edu.uoc.som.wapiml.test.simple;


import static org.junit.Assert.fail;

import java.io.File;
import org.junit.Test;

import edu.uoc.som.openapi2.API;
import edu.uoc.som.openapi2.io.OpenAPI2Builder;
import edu.uoc.som.openapi2.io.model.SerializationFormat;
import edu.uoc.som.wapiml.generators.ClassDiagramGenerator;



public class TestWAPIml {

	@Test
	public void testGenerateClassDiagram() {
		
	      
	       
	        try {
	        	  File input = new File("resources/inputs/petstore.json");
	        	  API apiModel = new OpenAPI2Builder().setSerializationFormat(SerializationFormat.JSON).fromFile(input);
	  			ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator(apiModel, "petstore", false,
	  					true);
	  			classDiagramGenerator.generateClassDiagramFromOpenAPI();
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
	       
	}
	@Test
	public void testGenerateAndSaveClassDiagramFile() {
		
	        try {
	        	File input = new File("resources/inputs/petstore.json");
	 	        File output = File.createTempFile("petstore", ".uml");
	 	       API apiModel = new OpenAPI2Builder().setSerializationFormat(SerializationFormat.JSON).fromFile(input);
	        	ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator(apiModel, "allOf", false, true);
	        	classDiagramGenerator.generateClassDiagramFromOpenAPI();
	        	classDiagramGenerator.saveClassDiagram(output);
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
	       
	}
	

}
