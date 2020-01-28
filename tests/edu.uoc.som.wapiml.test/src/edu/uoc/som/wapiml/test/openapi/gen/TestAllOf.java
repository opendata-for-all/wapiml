package edu.uoc.som.wapiml.test.openapi.gen;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

import edu.uoc.som.openapi2.API;
import edu.uoc.som.openapi2.io.OpenAPI2Builder;
import edu.uoc.som.openapi2.io.model.SerializationFormat;
import edu.uoc.som.wapiml.generators.ClassDiagramGenerator;
import edu.uoc.som.wapiml.generators.OpenAPIModelGenerator;



public class TestAllOf {

	
	@Test
	public void testGenerateAndSaveClassDiagram() {
		
	       
	        
	        try {
	        	File input = new File("resources/inputs/allOf.json");
	 	        File output = File.createTempFile("allOf", ".uml");
	        	API apiModel = new OpenAPI2Builder().setSerializationFormat(SerializationFormat.JSON).fromFile(input);
	        	ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator(apiModel, "allOf", true, true);
	        	classDiagramGenerator.generateClassDiagramFromOpenAPI();
	        	classDiagramGenerator.saveClassDiagram(output);
	        	OpenAPIModelGenerator openAPIModelGenerator = new OpenAPIModelGenerator(output);
	        	API generatedAPI = openAPIModelGenerator.generate();
	        	
	        	
	        	assertTrue(generatedAPI.getDefinitions().get("Pet").getDiscriminator().equals("petType"));
	        	assertTrue(generatedAPI.getDefinitions().get("Pet").getProperties().stream().anyMatch(e -> e.getName().equals("name")));
	        	assertTrue(!generatedAPI.getDefinitions().get("Cat").getAllOf().isEmpty());
	        	assertTrue(generatedAPI.getDefinitions().get("Cat").getAllOf().size() == 2);
	        	assertTrue(generatedAPI.getDefinitions().get("Cat").getAllOf().get(1).getProperties().size() == 1);
	        	assertTrue(generatedAPI.getDefinitions().get("Cat").getAllOf().get(1).getProperties().get(0).getName().equals("huntingSkill"));
	        
	        	}
	        	catch (Exception e) {
	        		fail(e.getLocalizedMessage());
				}
			}
	       
	}
	


