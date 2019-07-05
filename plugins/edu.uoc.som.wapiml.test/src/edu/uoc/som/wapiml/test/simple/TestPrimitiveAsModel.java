package edu.uoc.som.wapiml.test.simple;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import edu.uoc.som.wapiml.facade.WAPImlFacade;


@DisplayName("Test allOf with Ref - simple")
class TestPrimitiveAsModel {

	private static ResourceSet RES_SET = new ResourceSetImpl();

	@BeforeAll
	private static void init() {
		RES_SET.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		RES_SET.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
				UMLResource.Factory.INSTANCE);
	}
	
	@DisplayName("Test primitive as model")
	@Test
	void testGenerateAndSaveClassDiagramURI() throws IOException {
		
	        File input = new File("inputs/petstore-pt.json");
	        File output = new File("outputs/simple/petstore-pt.uml");
	        
	        try {
	        	WAPImlFacade wAPImlFacade = new WAPImlFacade();
	        	wAPImlFacade.generateAndSaveClassDiagram(input, "petstore-pt", output, false, false);
			} catch (IOException | ProcessingException e) {
				fail(e.getLocalizedMessage());
			}
	       
	}
	

}
