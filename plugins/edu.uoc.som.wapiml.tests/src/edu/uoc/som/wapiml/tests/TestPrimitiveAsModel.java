package edu.uoc.som.wapiml.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;


@DisplayName("Test allOf with Ref")
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
	        URI outputURI = URI.createFileURI("outputs");
	        WAPImlFacade openAPItoUMLFacade = new WAPImlFacade();
	        try {
	        	openAPItoUMLFacade.generateAndSaveClassDiagram(input, "petstore-pt", new File(outputURI.appendSegment("petstore-pt").appendFileExtension("uml").toFileString()), false, false);
//	        	Resource res = RES_SET.getResource(outputURI.appendSegment("petstore").appendFileExtension("uml"), true);
//				Model model = (Model) res.getContents().get(0);
//				Class fish = (Class)((Package) model.getPackagedElements().get(0)).getOwnedMember("Fish");
//				assertTrue(!fish.getAssociations().isEmpty()); 
			} catch (IOException | ProcessingException e) {
				fail(e.getLocalizedMessage());
			}
	       
	}
	

}
