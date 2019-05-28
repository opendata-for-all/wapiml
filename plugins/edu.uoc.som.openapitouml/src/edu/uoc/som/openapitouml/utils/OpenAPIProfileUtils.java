package edu.uoc.som.openapitouml.utils;

import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.util.UMLUtil;

import edu.uoc.som.openapi.API;
import edu.uoc.som.openapi.ExternalDocs;
import edu.uoc.som.openapi.Info;

public class OpenAPIProfileUtils {
	public static void applyAPIStereotype(Model model, API api) {
		Stereotype apiStereotype = model.getApplicableStereotype("OpenAPIProfile::API");
		if(!model.isStereotypeApplied(apiStereotype)) {
			model.applyStereotype(apiStereotype);
		}
			UMLUtil.setTaggedValue(model, apiStereotype, "host", api.getHost());
			UMLUtil.setTaggedValue(model, apiStereotype, "basePath", api.getBasePath());
			if(!api.getSchemes().isEmpty())
				UMLUtil.setTaggedValue(model, apiStereotype, "schemes", api.getSchemes());
			if(!api.getConsumes().isEmpty())
				UMLUtil.setTaggedValue(model, apiStereotype, "consumes", api.getConsumes());
			if(!api.getProduces().isEmpty())
				UMLUtil.setTaggedValue(model, apiStereotype, "produces", api.getProduces());
			
	}
	public static void applyAPIInfoStereotype(Model model, Info info) {
		Stereotype infoStereotype = model.getApplicableStereotype("OpenAPIProfile::APIInfo");
		if (!model.isStereotypeApplied(infoStereotype)) {
			model.applyStereotype(infoStereotype);
		}
			UMLUtil.setTaggedValue(model, infoStereotype, "title", info.getTitle());
			UMLUtil.setTaggedValue(model, infoStereotype, "description", info.getDescription());
			UMLUtil.setTaggedValue(model, infoStereotype, "termsOfService", info.getTermsOfService());
			UMLUtil.setTaggedValue(model, infoStereotype, "version", info.getVersion());
			if(info.getContact()!= null) {
				UMLUtil.setTaggedValue(model, infoStereotype, "contactName", info.getContact().getName());
				UMLUtil.setTaggedValue(model, infoStereotype, "contactURL", info.getContact().getUrl());
				UMLUtil.setTaggedValue(model, infoStereotype, "contactEmail", info.getContact().getEmail());	
			}
			if(info.getLicense()!= null) {
				UMLUtil.setTaggedValue(model, infoStereotype, "licenseName", info.getLicense().getName());
				UMLUtil.setTaggedValue(model, infoStereotype, "licenseURL", info.getLicense().getUrl());
			}
	}
	public static void applyExternalDocsStereotype(Model model, ExternalDocs externalDocs) {
		Stereotype externalDocsStereotype = model.getApplicableStereotype("OpenAPIProfile::ExternalDocs");
		if(!model.isStereotypeApplied(externalDocsStereotype)) {
			model.applyStereotype(externalDocsStereotype);
		}
			UMLUtil.setTaggedValue(model, externalDocsStereotype, "description", externalDocs.getDescription());
			UMLUtil.setTaggedValue(model, externalDocsStereotype, "url", externalDocs.getUrl());
		
	}

}
