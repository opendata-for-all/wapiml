package edu.uoc.som.wapiml.test.model.gen.simple;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.junit.Test;

import edu.uoc.som.openapi2.API;
import edu.uoc.som.openapi2.io.OpenAPI2Builder;
import edu.uoc.som.openapi2.io.model.SerializationFormat;
import edu.uoc.som.wapiml.generators.ClassDiagramGenerator;



public class TestAllOfWithEnum {


	
	@Test
	public void testGenerateAndSaveClassDiagramURI() {
		
	        
	        
	        try {
	        	File input = new File("resources/inputs/allOf.json");
	        	API apiModel = new OpenAPI2Builder().setSerializationFormat(SerializationFormat.JSON).fromFile(input);
	        	ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator(apiModel, "allOf", false, true);
				Model model = classDiagramGenerator.generateClassDiagramFromOpenAPI();
				Enumeration cantHuntingSkill  = (Enumeration)((Package) model.getPackagedElements().get(1)).getOwnedType("CatHuntingSkill");
				assertNotNull(cantHuntingSkill);
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
	       
	}
	

}
