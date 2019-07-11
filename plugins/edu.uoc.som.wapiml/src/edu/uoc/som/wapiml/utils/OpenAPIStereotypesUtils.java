package edu.uoc.som.wapiml.utils;


import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPlugin;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Inspired from {@link https://github.com/dice-project/DICE-Simulation/blob/master/bundles/es.unizar.disco.simulation.models/src/es/unizar/disco/simulation/models/util/MarteStereotypesUtils.java}}
 *
 */
import edu.som.uoc.openapi2.profile.OpenAPIProfilePackage;

public class OpenAPIStereotypesUtils {


	private static class InternalUMLUtil  extends UMLUtil{
		private static final URI OPENAPI_PROFILE_URI = UMLPlugin.getEPackageNsURIToProfileLocationMap().get(OpenAPIProfilePackage.eNS_URI);
		private static final EObject OPENAPI_PROFILE_EPACKAGE = new ResourceSetImpl().getResource(OPENAPI_PROFILE_URI, true).getEObject(OPENAPI_PROFILE_URI.fragment());

		protected static Stereotype getStereotype(EClass definition) {
			NamedElement namedElement = UMLUtil.getNamedElement(definition, OPENAPI_PROFILE_EPACKAGE);
			return namedElement instanceof Stereotype ? (Stereotype) namedElement : null;
		}
	
	}
	private static Stereotype getStereotype(EClass definition) {
		return InternalUMLUtil.getStereotype(definition);
	}
	public static String getStereotypeQn(EClass definition) {
		Stereotype stereotype = getStereotype(definition);
		return stereotype != null ? stereotype.getQualifiedName() : null; //$NON-NLS-1$
	}
}
