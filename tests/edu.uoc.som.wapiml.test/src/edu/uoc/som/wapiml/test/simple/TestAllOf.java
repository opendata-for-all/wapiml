package edu.uoc.som.wapiml.test.simple;


import static org.junit.Assert.fail;

import java.io.File;

import org.eclipse.uml2.uml.Model;
import org.junit.Test;

import edu.uoc.som.openapi2.API;
import edu.uoc.som.openapi2.io.OpenAPI2Builder;
import edu.uoc.som.openapi2.io.model.SerializationFormat;
import edu.uoc.som.wapiml.generators.ClassDiagramGenerator;



public class TestAllOf {

	
	@Test
	public void testGenerateAndSaveClassDiagram() {
		
	       
	        
	        try {
	        	File input = new File("resources/inputs/allOf.json");
	 	        File output = File.createTempFile("allOf", ".uml");
	        	API apiModel = new OpenAPI2Builder().setSerializationFormat(SerializationFormat.JSON).fromFile(input);
	        	ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator(apiModel, "allOf", false, true);
	        	Model model = classDiagramGenerator.generateClassDiagramFromOpenAPI();
	        	classDiagramGenerator.saveClassDiagram(output);}
	        	catch (Exception e) {
	        		fail(e.getLocalizedMessage());
				}
			}
	       
	}
	


