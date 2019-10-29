package edu.uoc.som.wapiml.utils;

import java.io.File;
import java.net.URISyntaxException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.UMLPlugin;
import org.eclipse.uml2.uml.resource.UMLResource;

import edu.uoc.som.openapi2.profile.OpenAPIProfilePackage;

public class UMLUtils {

	public static AggregationKind getAggregationKindByValue(int value) {
		switch (value) {
		case AggregationKind.NONE:
			return AggregationKind.NONE_LITERAL;
		case AggregationKind.SHARED:
			return AggregationKind.SHARED_LITERAL;
		case AggregationKind.COMPOSITE:
			return AggregationKind.COMPOSITE_LITERAL;
		default:
			return null;
		}
	}
	
	
	public static ResourceSet initUMLResourceSet(boolean standalone) throws URISyntaxException {
		ResourceSet resourceSet = new ResourceSetImpl();
		
		

		
		resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
				UMLResource.Factory.INSTANCE);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap( ).put(UMLResource.FILE_EXTENSION,
				UMLResource.Factory.INSTANCE);
		if (standalone) {
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap( ).put(UMLResource.FILE_EXTENSION,
					UMLResource.Factory.INSTANCE);
//			File baseURIFolder = new File(UMLUtils.class.getResource("resources").getFile());
//			URI baseUri = URI.createURI("jar:"+baseURIFolder.getPath());
//			java.net.URI uri = UMLUtils.class.getResource("").toURI();
//			URI baseUri = URI.createURI(UMLUtils.class.getResource("resources").toURI().toString());
			URI baseUri = IOUtils.getResourcesURI();
			URI umlBaseUri = baseUri.appendSegment("uml");
			URI openAPIBaseUri = baseUri.appendSegment("openapi");
			resourceSet.getPackageRegistry().put(OpenAPIProfilePackage.eNS_URI, OpenAPIProfilePackage.eINSTANCE);
			resourceSet.getURIConverter().getURIMap().put(URI.createURI(UMLResource.LIBRARIES_PATHMAP),
					umlBaseUri.appendSegment("libraries").appendSegment(""));
			resourceSet.getURIConverter().getURIMap().put(URI.createURI(UMLResource.METAMODELS_PATHMAP),
					umlBaseUri.appendSegment("metamodels").appendSegment(""));
			resourceSet.getURIConverter().getURIMap().put(URI.createURI(UMLResource.PROFILES_PATHMAP),
					umlBaseUri.appendSegment("profiles").appendSegment(""));
			resourceSet.getURIConverter().getURIMap().put(
					URI.createURI("pathmap://OPENAPI_PROFILES/"),
					openAPIBaseUri.appendSegment(""));
			UMLPlugin.getEPackageNsURIToProfileLocationMap().put(OpenAPIProfilePackage.eNS_URI, URI.createURI("pathmap://OPENAPI_PROFILES/openapi.profile.uml").appendFragment("_8zK3wHyGEemaV87q0fd26g"));



		} else {
			
			resourceSet.getURIConverter().getURIMap().put(
					URI.createURI("pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml"), URI.createPlatformPluginURI(
							"org.eclipse.uml2.uml.resources/libraries/UMLPrimitiveTypes.library.uml", true));
			resourceSet.getURIConverter().getURIMap().put(URI.createURI(UMLResource.LIBRARIES_PATHMAP),
					URI.createPlatformPluginURI("org.eclipse.uml2.uml.resources", true).appendSegment("libraries")
							.appendSegment(""));
			resourceSet.getURIConverter().getURIMap().put(URI.createURI(UMLResource.METAMODELS_PATHMAP),
					URI.createPlatformPluginURI("org.eclipse.uml2.uml.resources", true).appendSegment("metamodels")
							.appendSegment(""));
			resourceSet.getURIConverter().getURIMap().put(URI.createURI(UMLResource.PROFILES_PATHMAP),
					URI.createPlatformPluginURI("org.eclipse.uml2.uml.resources", true).appendSegment("profiles")
							.appendSegment(""));
			resourceSet.getURIConverter().getURIMap().put(
					URI.createURI("pathmap://OPENAPI_PROFILES/openapi.profile.uml"),
					URI.createPlatformPluginURI("edu.uoc.som.openapi2.profile/resources/openapi.profile.uml", true));
		}

		return resourceSet;
	}
	
}
