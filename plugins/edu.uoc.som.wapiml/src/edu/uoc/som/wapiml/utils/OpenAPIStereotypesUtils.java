package edu.uoc.som.wapiml.utils;


import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPlugin;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Inspired from {@link https://github.com/dice-project/DICE-Simulation/blob/master/bundles/es.unizar.disco.simulation.models/src/es/unizar/disco/simulation/models/util/MarteStereotypesUtils.java}}
 *
 */
import edu.uoc.som.openapi2.profile.OpenAPIProfilePackage;

public class OpenAPIStereotypesUtils {


	private static class InternalUMLUtil  extends UMLUtil{
		private static final URI OPENAPI_PROFILE_URI = UMLPlugin.getEPackageNsURIToProfileLocationMap().get(OpenAPIProfilePackage.eNS_URI);
		private static final EObject OPENAPI_PROFILE_EPACKAGE = new ResourceSetImpl().getResource(OPENAPI_PROFILE_URI, true).getEObject(OPENAPI_PROFILE_URI.fragment());

		protected static Stereotype getStereotype(EClass definition, ResourceSet resourceSet) {
//			 EObject OPENAPI_PROFILE_EPACKAGE = resourceSet.getResource(OPENAPI_PROFILE_URI, true).getEObject(OPENAPI_PROFILE_URI.fragment());
			NamedElement namedElement = UMLUtil.getNamedElement(definition, OPENAPI_PROFILE_EPACKAGE);
			return namedElement instanceof Stereotype ? (Stereotype) namedElement : null;
		}
	
	}
	private static Stereotype getStereotype(EClass definition, ResourceSet resourceSet) {
		return InternalUMLUtil.getStereotype(definition, resourceSet);
	}
	public static String getStereotypeQn(EClass definition, ResourceSet resourceSet) {
		Stereotype stereotype = getStereotype(definition,resourceSet);
		return stereotype != null ? stereotype.getQualifiedName() : null; //$NON-NLS-1$
	}
}
