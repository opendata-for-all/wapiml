package edu.uoc.som.wapiml.test.profile;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import edu.uoc.som.wapiml.facade.WAPImlFacade;

@DisplayName("Test allOf with Enum - profile")
class TestAllOfWithEnum {

	private static ResourceSet RES_SET = new ResourceSetImpl();

	@BeforeAll
	private static void init() {
		RES_SET.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		RES_SET.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
				UMLResource.Factory.INSTANCE);
	}
	
	@DisplayName("Test allOf with Enum")
	@Test
	void testGenerateAndSaveClassDiagramURI() {
		
	        File input = new File("inputs/allOf.json");
	        File output = new File("outputs/profile/allOf-enum.uml");
	        
	        try {
	        	WAPImlFacade wAPImlFacade = new WAPImlFacade();
	        	wAPImlFacade.generateAndSaveClassDiagram(input, "allOf-enum", output, true, true);
	        	Resource res = RES_SET.getResource(URI.createFileURI(output.toString()), true);
				Model model = (Model) res.getContents().get(0);
				Enumeration cantHuntingSkill  = (Enumeration)((Package) model.getPackagedElements().get(1)).getOwnedType("CatHuntingSkill");
				assertNotNull(cantHuntingSkill);
			} catch (IOException | ProcessingException e) {
				fail(e.getLocalizedMessage());
			}
	       
	}
	

}
