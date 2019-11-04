package edu.uoc.som.wapiml.resources;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.UMLPlugin;
import org.eclipse.uml2.uml.resource.UMLResource;

import edu.uoc.som.openapi2.profile.OpenAPIProfilePackage;
import edu.uoc.som.wapiml.utils.IOUtils;

public class WAPImlResource {
	
	private static ResourceSet umlResourceSet;

	private WAPImlResource() {
	}

	public static ResourceSet getUMResourceSet() {
		if (umlResourceSet == null) {
			umlResourceSet = new ResourceSetImpl();
			umlResourceSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
			umlResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
					UMLResource.Factory.INSTANCE);
			boolean standalone;
			// Check if we are inside eclipse or in a standalone mode
			try {
				umlResourceSet.getResource(
						URI.createPlatformPluginURI("edu.uoc.som.openapi2.profile/resources/openapi.profile.uml", true),
						true);
				standalone = false;
			} catch (Exception e) {
				standalone = true;
			}
			
			if (standalone) {
				Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap( ).put(UMLResource.FILE_EXTENSION,
						UMLResource.Factory.INSTANCE);
				URI baseUri = IOUtils.getResourcesURI();
				URI umlBaseUri = baseUri.appendSegment("uml");
				URI openAPIBaseUri = baseUri.appendSegment("openapi");
				umlResourceSet.getPackageRegistry().put(OpenAPIProfilePackage.eNS_URI, OpenAPIProfilePackage.eINSTANCE);
				umlResourceSet.getURIConverter().getURIMap().put(URI.createURI(UMLResource.LIBRARIES_PATHMAP),
						umlBaseUri.appendSegment("libraries").appendSegment(""));
				umlResourceSet.getURIConverter().getURIMap().put(URI.createURI(UMLResource.METAMODELS_PATHMAP),
						umlBaseUri.appendSegment("metamodels").appendSegment(""));
				umlResourceSet.getURIConverter().getURIMap().put(URI.createURI(UMLResource.PROFILES_PATHMAP),
						umlBaseUri.appendSegment("profiles").appendSegment(""));
				umlResourceSet.getURIConverter().getURIMap().put(
						URI.createURI("pathmap://OPENAPI_PROFILES/"),
						openAPIBaseUri.appendSegment(""));
				UMLPlugin.getEPackageNsURIToProfileLocationMap().put(OpenAPIProfilePackage.eNS_URI, URI.createURI("pathmap://OPENAPI_PROFILES/openapi.profile.uml").appendFragment("_8zK3wHyGEemaV87q0fd26g"));



			} else {
				
				umlResourceSet.getURIConverter().getURIMap().put(
						URI.createURI("pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml"), URI.createPlatformPluginURI(
								"org.eclipse.uml2.uml.resources/libraries/UMLPrimitiveTypes.library.uml", true));
				umlResourceSet.getURIConverter().getURIMap().put(URI.createURI(UMLResource.LIBRARIES_PATHMAP),
						URI.createPlatformPluginURI("org.eclipse.uml2.uml.resources", true).appendSegment("libraries")
								.appendSegment(""));
				umlResourceSet.getURIConverter().getURIMap().put(URI.createURI(UMLResource.METAMODELS_PATHMAP),
						URI.createPlatformPluginURI("org.eclipse.uml2.uml.resources", true).appendSegment("metamodels")
								.appendSegment(""));
				umlResourceSet.getURIConverter().getURIMap().put(URI.createURI(UMLResource.PROFILES_PATHMAP),
						URI.createPlatformPluginURI("org.eclipse.uml2.uml.resources", true).appendSegment("profiles")
								.appendSegment(""));
				umlResourceSet.getURIConverter().getURIMap().put(
						URI.createURI("pathmap://OPENAPI_PROFILES/openapi.profile.uml"),
						URI.createPlatformPluginURI("edu.uoc.som.openapi2.profile/resources/openapi.profile.uml", true));
			}


		}
		return umlResourceSet;
	}

}
