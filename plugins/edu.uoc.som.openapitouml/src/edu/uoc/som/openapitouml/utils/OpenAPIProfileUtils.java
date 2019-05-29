package edu.uoc.som.openapitouml.utils;

import java.util.List;


import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.util.UMLUtil;

import edu.uoc.som.openapi.API;
import edu.uoc.som.openapi.ExternalDocs;
import edu.uoc.som.openapi.Info;
import edu.uoc.som.openapi.Property;
import edu.uoc.som.openapi.Schema;
import edu.uoc.som.openapi.SecurityRequirement;
import edu.uoc.som.openapi.SecurityScheme;
import edu.uoc.som.openapi.Tag;

public class OpenAPIProfileUtils {
	public static void applyAPIStereotype(Model model, API api) {
		Stereotype apiStereotype = model.getApplicableStereotype("OpenAPIProfile::API");
		if (!model.isStereotypeApplied(apiStereotype)) {
			model.applyStereotype(apiStereotype);
		}
		UMLUtil.setTaggedValue(model, apiStereotype, "host", api.getHost());
		UMLUtil.setTaggedValue(model, apiStereotype, "basePath", api.getBasePath());
		if (!api.getSchemes().isEmpty())
			UMLUtil.setTaggedValue(model, apiStereotype, "schemes", api.getSchemes());
		if (!api.getConsumes().isEmpty())
			UMLUtil.setTaggedValue(model, apiStereotype, "consumes", api.getConsumes());
		if (!api.getProduces().isEmpty())
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
		if (info.getContact() != null) {
			UMLUtil.setTaggedValue(model, infoStereotype, "contactName", info.getContact().getName());
			UMLUtil.setTaggedValue(model, infoStereotype, "contactURL", info.getContact().getUrl());
			UMLUtil.setTaggedValue(model, infoStereotype, "contactEmail", info.getContact().getEmail());
		}
		if (info.getLicense() != null) {
			UMLUtil.setTaggedValue(model, infoStereotype, "licenseName", info.getLicense().getName());
			UMLUtil.setTaggedValue(model, infoStereotype, "licenseURL", info.getLicense().getUrl());
		}
	}

	public static void applyExternalDocsStereotype(Model model, ExternalDocs externalDocs) {
		Stereotype externalDocsStereotype = model.getApplicableStereotype("OpenAPIProfile::ExternalDocs");
		if (!model.isStereotypeApplied(externalDocsStereotype)) {
			model.applyStereotype(externalDocsStereotype);
		}
		UMLUtil.setTaggedValue(model, externalDocsStereotype, "description", externalDocs.getDescription());
		UMLUtil.setTaggedValue(model, externalDocsStereotype, "url", externalDocs.getUrl());
	}
	public static void applyTagsStereotype(Model model, List<Tag> tagList) {
		Stereotype tagsStereotype = model.getApplicableStereotype("OpenAPIProfile::Tags");
		if(!model.isStereotypeApplied(tagsStereotype))
			model.applyStereotype(tagsStereotype);
		// TODO tags
	}
	public static void applySchemaStereotype(Class clazz, Schema schema) {
		Stereotype schemaStereotype = clazz.getApplicableStereotype("OpenAPIProfile::Schema");
		if(!clazz.isStereotypeApplied(schemaStereotype))
			clazz.applyStereotype(schemaStereotype);
		UMLUtil.setTaggedValue(clazz, schemaStereotype, "title", schema.getTitle());
		UMLUtil.setTaggedValue(clazz, schemaStereotype, "maxProperties", schema.getMaxProperties());
		UMLUtil.setTaggedValue(clazz, schemaStereotype, "minProperties", schema.getMinProperties());
		UMLUtil.setTaggedValue(clazz, schemaStereotype, "discriminator", schema.getDiscriminator());
		UMLUtil.setTaggedValue(clazz, schemaStereotype, "additionalPropertiesAllowed", schema.getAdditonalPropertiesAllowed());
	}
	public static void applyAPIPropertyStereotype (org.eclipse.uml2.uml.Property property, Property apiProperty) {
		Stereotype apiPropertyStereotype = property.getApplicableStereotype("OpenAPIProfile::APIProperty");
		if(!property.isStereotypeApplied(apiPropertyStereotype))
			property.applyStereotype(apiPropertyStereotype);
		// TODO APIProperty
		
	}
	public static void applyAPIDataTypeStereotype (org.eclipse.uml2.uml.PrimitiveType primitiveType, Schema schema) {
		Stereotype apiDataTypeStereotype = primitiveType.getApplicableStereotype("OpenAPIProfile::APIDataType");
		if(!primitiveType.isStereotypeApplied(apiDataTypeStereotype))
			primitiveType.applyStereotype(apiDataTypeStereotype);
		// TODO APIDataType
	}
	public static void applyAPIDataTypeStereotype (Enumeration enumeration, Schema schema) {
		Stereotype apiDataTypeStereotype = enumeration.getApplicableStereotype("OpenAPIProfile::APIDataType");
		if(!enumeration.isStereotypeApplied(apiDataTypeStereotype))
			enumeration.applyStereotype(apiDataTypeStereotype);
		// TODO APIProperty
	}
	public static void applyAPIParameterStereotype (Parameter parameter, edu.uoc.som.openapi.Parameter apiParameter) {
		Stereotype apiParameterStereotype = parameter.getApplicableStereotype("OpenAPIProfile::APIParameter");
		if(!parameter.isStereotypeApplied(apiParameterStereotype))
			parameter.applyStereotype(apiParameterStereotype);
		// TODO APIParameter
	}
	
	public static void applyAPIResponseStereotype (Parameter parameter, edu.uoc.som.openapi.Response apiResponse) {
		Stereotype apiResponseStereotype = parameter.getApplicableStereotype("OpenAPIProfile::APIResponse");
		if(!parameter.isStereotypeApplied(apiResponseStereotype))
			parameter.applyStereotype(apiResponseStereotype);
		// TODO APiResponse
	}
	public static void applyAPIOperationeStereotype (Operation operation, edu.uoc.som.openapi.Operation apiOperation) {
		Stereotype apiOperationStereotype = operation.getApplicableStereotype("OpenAPIProfile::APIOperation");
		if(!operation.isStereotypeApplied(apiOperationStereotype))
			operation.applyStereotype(apiOperationStereotype);
		// TODO APIOperation
	}
	public static void applyExternalDocsStereotype(Class clazz, ExternalDocs externalDocs) {
		Stereotype externalDocsStereotype = clazz.getApplicableStereotype("OpenAPIProfile::ExternalDocs");
		if (!clazz.isStereotypeApplied(externalDocsStereotype)) {
			clazz.applyStereotype(externalDocsStereotype);
		}
		UMLUtil.setTaggedValue(clazz, externalDocsStereotype, "description", externalDocs.getDescription());
		UMLUtil.setTaggedValue(clazz, externalDocsStereotype, "url", externalDocs.getUrl());
	}
	public static void applyExternalDocsStereotype(Operation operation, ExternalDocs externalDocs) {
		Stereotype externalDocsStereotype = operation.getApplicableStereotype("OpenAPIProfile::ExternalDocs");
		if (!operation.isStereotypeApplied(externalDocsStereotype)) {
			operation.applyStereotype(externalDocsStereotype);
		}
		UMLUtil.setTaggedValue(operation, externalDocsStereotype, "description", externalDocs.getDescription());
		UMLUtil.setTaggedValue(operation, externalDocsStereotype, "url", externalDocs.getUrl());
	}
	
	public static void applySecurityDefinitionsStereotype(Model model, List<SecurityScheme> securitySchemes) {
		Stereotype securityDefinitionsStereotype = model.getApplicableStereotype("OpenAPIProfile::SecurityDefinitions");
		if (!model.isStereotypeApplied(securityDefinitionsStereotype)) {
			model.applyStereotype(securityDefinitionsStereotype);
		}
	// TODO SecurityDefinitions
	}
	public static void applySecurityRequirementsStereotype(Model model, List<SecurityRequirement> securityRequirements) {
		Stereotype securityRequirementsStereotype = model.getApplicableStereotype("OpenAPIProfile::SecurityRequirements");
		if (!model.isStereotypeApplied(securityRequirementsStereotype)) {
			model.applyStereotype(securityRequirementsStereotype);
		}
	// TODO SecurityRequirements for Model
	}
	public static void applySecurityRequirementsStereotype(Operation operation, List<SecurityRequirement> securityRequirements) {
		Stereotype securityRequirementsStereotype = operation.getApplicableStereotype("OpenAPIProfile::SecurityRequirements");
		if (!operation.isStereotypeApplied(securityRequirementsStereotype)) {
			operation.applyStereotype(securityRequirementsStereotype);
		}
	// TODO SecurityRequirements for Operation
	}

}
