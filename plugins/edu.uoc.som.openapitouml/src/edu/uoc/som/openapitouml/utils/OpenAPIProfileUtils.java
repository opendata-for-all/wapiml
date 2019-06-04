package edu.uoc.som.openapitouml.utils;

import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.API;
import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.API_INFO;
import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.EXTERNAL_DOCS;
import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.TAGS;
import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.SCHEMA;
import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.API_PROPERTY;
import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.API_DATA_TYPE;
import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.API_PARAMETER;
import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.API_RESPONSE;
import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.API_OPERATION;
import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.SECURITY_DEFINITIONS;
import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.SECURITY;

import java.util.ArrayList;
import java.util.List;


import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.util.UMLUtil;

import edu.som.uoc.openapiprofile.types.SchemeType;
import edu.uoc.som.openapi.API;
import edu.uoc.som.openapi.ExternalDocs;
import edu.uoc.som.openapi.Info;
import edu.uoc.som.openapi.Property;
import edu.uoc.som.openapi.Schema;
import edu.uoc.som.openapi.SecurityRequirement;
import edu.uoc.som.openapi.SecurityScheme;
import edu.uoc.som.openapi.Tag;

public class OpenAPIProfileUtils {
	
	
	private static final String API_QN = OpenAPIStereotypesUtils.getStereotypeQn(API);
	private static final String API_INFO_QN = OpenAPIStereotypesUtils.getStereotypeQn(API_INFO);
	private static final String EXTERNAL_DOCS_QN = OpenAPIStereotypesUtils.getStereotypeQn(EXTERNAL_DOCS);
	private static final String TAGS_QN = OpenAPIStereotypesUtils.getStereotypeQn(TAGS);
	private static final String SCHEMA_QN = OpenAPIStereotypesUtils.getStereotypeQn(SCHEMA);
	private static final String API_PROPERTY_QN = OpenAPIStereotypesUtils.getStereotypeQn(API_PROPERTY);
	private static final String API_DATA_TYPE_QN = OpenAPIStereotypesUtils.getStereotypeQn(API_DATA_TYPE);
	private static final String API_PARAMETER_QN = OpenAPIStereotypesUtils.getStereotypeQn(API_PARAMETER);
	private static final String API_RESPONSE_QN = OpenAPIStereotypesUtils.getStereotypeQn(API_RESPONSE);
	private static final String API_OPERATION_QN = OpenAPIStereotypesUtils.getStereotypeQn(API_OPERATION);
	private static final String SECURITY_DEFINITIONS_QN = OpenAPIStereotypesUtils.getStereotypeQn(SECURITY_DEFINITIONS);
	private static final String SECURITY_QN = OpenAPIStereotypesUtils.getStereotypeQn(SECURITY);
	
	public static void applyAPIStereotype(Model model, API api) {
		Stereotype apiStereotype = model.getApplicableStereotype(API_QN);
		if (!model.isStereotypeApplied(apiStereotype)) {
			model.applyStereotype(apiStereotype);
		}
		UMLUtil.setTaggedValue(model, apiStereotype, "host", api.getHost());
		UMLUtil.setTaggedValue(model, apiStereotype, "basePath", api.getBasePath());
		if (!api.getSchemes().isEmpty()) {
			List<SchemeType> schemeTypes = new ArrayList<SchemeType>();
			for(edu.uoc.som.openapi.SchemeType from: api.getSchemes())
				schemeTypes.add(transformSchemeType(from));
			UMLUtil.setTaggedValue(model, apiStereotype, "schemes", schemeTypes);
		}
		if (!api.getConsumes().isEmpty())
			UMLUtil.setTaggedValue(model, apiStereotype, "consumes", api.getConsumes());
		if (!api.getProduces().isEmpty())
			UMLUtil.setTaggedValue(model, apiStereotype, "produces", api.getProduces());

	}

	public static void applyAPIInfoStereotype(Model model, Info info) {
		Stereotype infoStereotype = model.getApplicableStereotype(API_INFO_QN);
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
		Stereotype externalDocsStereotype = model.getApplicableStereotype(EXTERNAL_DOCS_QN);
		if (!model.isStereotypeApplied(externalDocsStereotype)) {
			model.applyStereotype(externalDocsStereotype);
		}
		UMLUtil.setTaggedValue(model, externalDocsStereotype, "description", externalDocs.getDescription());
		UMLUtil.setTaggedValue(model, externalDocsStereotype, "url", externalDocs.getUrl());
	}
	public static void applyTagsStereotype(Model model, List<Tag> tagList) {
		Stereotype tagsStereotype = model.getApplicableStereotype(TAGS_QN);
		if(!model.isStereotypeApplied(tagsStereotype))
			model.applyStereotype(tagsStereotype);
		// TODO tags
	}
	public static void applySchemaStereotype(Class clazz, Schema schema) {
		Stereotype schemaStereotype = clazz.getApplicableStereotype(SCHEMA_QN);
		if(!clazz.isStereotypeApplied(schemaStereotype))
			clazz.applyStereotype(schemaStereotype);
		UMLUtil.setTaggedValue(clazz, schemaStereotype, "title", schema.getTitle());
		UMLUtil.setTaggedValue(clazz, schemaStereotype, "maxProperties", schema.getMaxProperties());
		UMLUtil.setTaggedValue(clazz, schemaStereotype, "minProperties", schema.getMinProperties());
		UMLUtil.setTaggedValue(clazz, schemaStereotype, "discriminator", schema.getDiscriminator());
		UMLUtil.setTaggedValue(clazz, schemaStereotype, "additionalPropertiesAllowed", schema.getAdditonalPropertiesAllowed());
	}
	public static void applyAPIPropertyStereotype (org.eclipse.uml2.uml.Property property, Property apiProperty) {
		Stereotype apiPropertyStereotype = property.getApplicableStereotype(API_PROPERTY_QN);
		if(!property.isStereotypeApplied(apiPropertyStereotype))
			property.applyStereotype(apiPropertyStereotype);
		// TODO APIProperty
		
	}
	public static void applyAPIDataTypeStereotype (org.eclipse.uml2.uml.PrimitiveType primitiveType, Schema schema) {
		Stereotype apiDataTypeStereotype = primitiveType.getApplicableStereotype(API_DATA_TYPE_QN);
		if(!primitiveType.isStereotypeApplied(apiDataTypeStereotype))
			primitiveType.applyStereotype(apiDataTypeStereotype);
		// TODO APIDataType
	}
	public static void applyAPIDataTypeStereotype (Enumeration enumeration, Schema schema) {
		Stereotype apiDataTypeStereotype = enumeration.getApplicableStereotype(API_DATA_TYPE_QN);
		if(!enumeration.isStereotypeApplied(apiDataTypeStereotype))
			enumeration.applyStereotype(apiDataTypeStereotype);
		// TODO APIProperty
	}
	public static void applyAPIParameterStereotype (Parameter parameter, edu.uoc.som.openapi.Parameter apiParameter) {
		Stereotype apiParameterStereotype = parameter.getApplicableStereotype(API_PARAMETER_QN);
		if(!parameter.isStereotypeApplied(apiParameterStereotype))
			parameter.applyStereotype(apiParameterStereotype);
		// TODO APIParameter
	}
	
	public static void applyAPIResponseStereotype (Parameter parameter, edu.uoc.som.openapi.Response apiResponse) {
		Stereotype apiResponseStereotype = parameter.getApplicableStereotype(API_RESPONSE_QN);
		if(!parameter.isStereotypeApplied(apiResponseStereotype))
			parameter.applyStereotype(apiResponseStereotype);
		// TODO APiResponse
	}
	public static void applyAPIOperationeStereotype (Operation operation, edu.uoc.som.openapi.Operation apiOperation) {
		Stereotype apiOperationStereotype = operation.getApplicableStereotype(API_OPERATION_QN);
		if(!operation.isStereotypeApplied(apiOperationStereotype))
			operation.applyStereotype(apiOperationStereotype);
		// TODO APIOperation
	}
	public static void applyExternalDocsStereotype(Class clazz, ExternalDocs externalDocs) {
		Stereotype externalDocsStereotype = clazz.getApplicableStereotype(EXTERNAL_DOCS_QN);
		if (!clazz.isStereotypeApplied(externalDocsStereotype)) {
			clazz.applyStereotype(externalDocsStereotype);
		}
		UMLUtil.setTaggedValue(clazz, externalDocsStereotype, "description", externalDocs.getDescription());
		UMLUtil.setTaggedValue(clazz, externalDocsStereotype, "url", externalDocs.getUrl());
	}
	public static void applyExternalDocsStereotype(Operation operation, ExternalDocs externalDocs) {
		Stereotype externalDocsStereotype = operation.getApplicableStereotype(EXTERNAL_DOCS_QN);
		if (!operation.isStereotypeApplied(externalDocsStereotype)) {
			operation.applyStereotype(externalDocsStereotype);
		}
		UMLUtil.setTaggedValue(operation, externalDocsStereotype, "description", externalDocs.getDescription());
		UMLUtil.setTaggedValue(operation, externalDocsStereotype, "url", externalDocs.getUrl());
	}
	
	public static void applySecurityDefinitionsStereotype(Model model, List<SecurityScheme> securitySchemes) {
		Stereotype securityDefinitionsStereotype = model.getApplicableStereotype(SECURITY_DEFINITIONS_QN);
		if (!model.isStereotypeApplied(securityDefinitionsStereotype)) {
			model.applyStereotype(securityDefinitionsStereotype);
		}
	// TODO SecurityDefinitions
	}
	public static void applySecurityRequirementsStereotype(Model model, List<SecurityRequirement> securityRequirements) {
		Stereotype securityRequirementsStereotype = model.getApplicableStereotype(SECURITY_QN);
		if (!model.isStereotypeApplied(securityRequirementsStereotype)) {
			model.applyStereotype(securityRequirementsStereotype);
		}
	// TODO SecurityRequirements for Model
	}
	public static void applySecurityRequirementsStereotype(Operation operation, List<SecurityRequirement> securityRequirements) {
		Stereotype securityRequirementsStereotype = operation.getApplicableStereotype(SECURITY_QN);
		if (!operation.isStereotypeApplied(securityRequirementsStereotype)) {
			operation.applyStereotype(securityRequirementsStereotype);
		}
	// TODO SecurityRequirements for Operation
	}

	public static SchemeType transformSchemeType(edu.uoc.som.openapi.SchemeType from) {
		if (from.equals(edu.uoc.som.openapi.SchemeType.HTTP))
			return SchemeType.HTTP;
		return null;
	}
}
