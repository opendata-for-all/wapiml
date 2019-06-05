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

import edu.som.uoc.openapiprofile.APIKeyLocation;
import edu.som.uoc.openapiprofile.Contact;
import edu.som.uoc.openapiprofile.License;
import edu.som.uoc.openapiprofile.OAuth2FlowType;
import edu.som.uoc.openapiprofile.OpenapiprofileFactory;
import edu.som.uoc.openapiprofile.SchemeType;
import edu.som.uoc.openapiprofile.SecurityDefinitions;
import edu.som.uoc.openapiprofile.SecuritySchemeType;
import edu.som.uoc.openapiprofile.SecurityScope;
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
			for (edu.uoc.som.openapi.SchemeType from : api.getSchemes())
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
			Contact pContact = OpenapiprofileFactory.eINSTANCE.createContact();
			pContact.setEmail(info.getContact().getEmail());
			pContact.setName(info.getContact().getName());
			pContact.setUrl(info.getContact().getUrl());
			UMLUtil.setTaggedValue(model, infoStereotype, "contact", pContact);
		}
		if (info.getLicense() != null) {
			License pLicense = OpenapiprofileFactory.eINSTANCE.createLicense();
			pLicense.setName(info.getLicense().getName());
			pLicense.setUrl(info.getLicense().getUrl());
			UMLUtil.setTaggedValue(model, infoStereotype, "license", pLicense);
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
		if (!model.isStereotypeApplied(tagsStereotype))
			model.applyStereotype(tagsStereotype);
		List<edu.som.uoc.openapiprofile.Tag> pTags = new ArrayList<edu.som.uoc.openapiprofile.Tag>();
		for (Tag mTag : tagList) {
			edu.som.uoc.openapiprofile.Tag pTag = OpenapiprofileFactory.eINSTANCE.createTag();
			if (mTag.getExternalDocs() != null) {
				pTag.setExternalDocsDescription(mTag.getExternalDocs().getDescription());
				pTag.setExternalDocsURL(mTag.getExternalDocs().getUrl());
			}
			pTag.setName(mTag.getName());
			pTag.setDescription(mTag.getDescription());
			pTags.add(pTag);
		}
		UMLUtil.setTaggedValue(model, tagsStereotype, "tags", pTags);
	}

	public static void applySchemaStereotype(Class clazz, Schema schema) {
		Stereotype schemaStereotype = clazz.getApplicableStereotype(SCHEMA_QN);
		if (!clazz.isStereotypeApplied(schemaStereotype))
			clazz.applyStereotype(schemaStereotype);
		UMLUtil.setTaggedValue(clazz, schemaStereotype, "title", schema.getTitle());
		UMLUtil.setTaggedValue(clazz, schemaStereotype, "maxProperties", schema.getMaxProperties());
		UMLUtil.setTaggedValue(clazz, schemaStereotype, "minProperties", schema.getMinProperties());
		UMLUtil.setTaggedValue(clazz, schemaStereotype, "discriminator", schema.getDiscriminator());
		UMLUtil.setTaggedValue(clazz, schemaStereotype, "additionalPropertiesAllowed",
				schema.getAdditonalPropertiesAllowed());
	}

	public static void applyAPIPropertyStereotype(org.eclipse.uml2.uml.Property property, Property apiProperty) {
		Stereotype apiPropertyStereotype = property.getApplicableStereotype(API_PROPERTY_QN);
		if (!property.isStereotypeApplied(apiPropertyStereotype))
			property.applyStereotype(apiPropertyStereotype);
		// TODO APIProperty

	}

	public static void applyAPIDataTypeStereotype(org.eclipse.uml2.uml.PrimitiveType primitiveType, Schema schema) {
		Stereotype apiDataTypeStereotype = primitiveType.getApplicableStereotype(API_DATA_TYPE_QN);
		if (!primitiveType.isStereotypeApplied(apiDataTypeStereotype))
			primitiveType.applyStereotype(apiDataTypeStereotype);
		// TODO APIDataType
	}

	public static void applyAPIDataTypeStereotype(Enumeration enumeration, Schema schema) {
		Stereotype apiDataTypeStereotype = enumeration.getApplicableStereotype(API_DATA_TYPE_QN);
		if (!enumeration.isStereotypeApplied(apiDataTypeStereotype))
			enumeration.applyStereotype(apiDataTypeStereotype);
		// TODO APIProperty
	}

	public static void applyAPIParameterStereotype(Parameter parameter, edu.uoc.som.openapi.Parameter apiParameter) {
		Stereotype apiParameterStereotype = parameter.getApplicableStereotype(API_PARAMETER_QN);
		if (!parameter.isStereotypeApplied(apiParameterStereotype))
			parameter.applyStereotype(apiParameterStereotype);
		// TODO APIParameter
	}

	public static void applyAPIResponseStereotype(Parameter parameter, edu.uoc.som.openapi.Response apiResponse) {
		Stereotype apiResponseStereotype = parameter.getApplicableStereotype(API_RESPONSE_QN);
		if (!parameter.isStereotypeApplied(apiResponseStereotype))
			parameter.applyStereotype(apiResponseStereotype);
		// TODO APiResponse
	}

	public static void applyAPIOperationeStereotype(Operation operation, edu.uoc.som.openapi.Operation apiOperation) {
		Stereotype apiOperationStereotype = operation.getApplicableStereotype(API_OPERATION_QN);
		if (!operation.isStereotypeApplied(apiOperationStereotype))
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

	public static void applySecurityDefinitionsStereotype(Model model, List<SecurityScheme> mSecuritySchemes) {
		Stereotype securityDefinitionsStereotype = model.getApplicableStereotype(SECURITY_DEFINITIONS_QN);
		if (!model.isStereotypeApplied(securityDefinitionsStereotype)) {
			model.applyStereotype(securityDefinitionsStereotype);
		}
		List<edu.som.uoc.openapiprofile.SecurityScheme> pSecuritySchemes = new ArrayList<edu.som.uoc.openapiprofile.SecurityScheme>();
		for (SecurityScheme mSecurityScheme : mSecuritySchemes) {
			edu.som.uoc.openapiprofile.SecurityScheme pSecurityScheme = OpenapiprofileFactory.eINSTANCE
					.createSecurityScheme();
			pSecurityScheme.setReferenceName(mSecurityScheme.getReferenceName());
			pSecurityScheme.setName(mSecurityScheme.getName());
			if (mSecurityScheme.getType() != null) {
				pSecurityScheme.setType(transformSecuritySchemeType(mSecurityScheme.getType()));
			}
			pSecurityScheme.setDescription(mSecurityScheme.getDescription());
			if (mSecurityScheme.getLocation() != null)
				pSecurityScheme.setLocation(transformAPIKeyLocation(mSecurityScheme.getLocation()));
			if (mSecurityScheme.getFlow() != null)
				pSecurityScheme.setFlow(transformOAuth2FlowType(mSecurityScheme.getFlow()));
			pSecurityScheme.setAuthorizationURL(mSecurityScheme.getAuthorizationUrl());
			pSecurityScheme.setTokenURL(mSecurityScheme.getTokenUrl());
			if (!mSecurityScheme.getScopes().isEmpty()) {
				for (edu.uoc.som.openapi.SecurityScope mSecurityScope : mSecurityScheme.getScopes()) {
					SecurityScope pSecurityScope = OpenapiprofileFactory.eINSTANCE.createSecurityScope();
					pSecurityScope.setDescription(mSecurityScope.getDescription());
					pSecurityScheme.setName(mSecurityScheme.getName());
					pSecurityScheme.getScopes().add(pSecurityScope);
				}
			}
			pSecuritySchemes.add(pSecurityScheme);
			UMLUtil.setTaggedValue(model, securityDefinitionsStereotype, "securitySchemes", pSecuritySchemes);
		}
	}

	public static void applySecurityStereotype(Model model, List<SecurityRequirement> securityRequirements) {
		Stereotype securityRequirementsStereotype = model.getApplicableStereotype(SECURITY_QN);
		if (!model.isStereotypeApplied(securityRequirementsStereotype)) {
			model.applyStereotype(securityRequirementsStereotype);
		}
		List<edu.som.uoc.openapiprofile.SecurityRequirement> pSecurityRequirements = new ArrayList<edu.som.uoc.openapiprofile.SecurityRequirement>();
		for (SecurityRequirement mSecurityRequirement : securityRequirements) {
			edu.som.uoc.openapiprofile.SecurityRequirement pSecurityRequirement = OpenapiprofileFactory.eINSTANCE
					.createSecurityRequirement();
			pSecurityRequirement.setName(mSecurityRequirement.getSecurityScheme().getRef());
			for (edu.uoc.som.openapi.SecurityScope mScope : mSecurityRequirement.getSecurityScheme().getScopes()) {
				pSecurityRequirement.getScopes().add(mScope.getName());
			}
			pSecurityRequirements.add(pSecurityRequirement);
		}
		UMLUtil.setTaggedValue(model, securityRequirementsStereotype, "securityRequirements", pSecurityRequirements);
	}

	public static void applySecurityStereotype(Operation operation, List<SecurityRequirement> securityRequirements) {
		Stereotype securityRequirementsStereotype = operation.getApplicableStereotype(SECURITY_QN);
		if (!operation.isStereotypeApplied(securityRequirementsStereotype)) {
			operation.applyStereotype(securityRequirementsStereotype);
		}
		List<edu.som.uoc.openapiprofile.SecurityRequirement> pSecurityRequirements = new ArrayList<edu.som.uoc.openapiprofile.SecurityRequirement>();
		for (SecurityRequirement mSecurityRequirement : securityRequirements) {
			edu.som.uoc.openapiprofile.SecurityRequirement pSecurityRequirement = OpenapiprofileFactory.eINSTANCE
					.createSecurityRequirement();
			pSecurityRequirement.setName(mSecurityRequirement.getSecurityScheme().getRef());
			for (edu.uoc.som.openapi.SecurityScope mScope : mSecurityRequirement.getSecurityScheme().getScopes()) {
				pSecurityRequirement.getScopes().add(mScope.getName());
			}
			pSecurityRequirements.add(pSecurityRequirement);
		}
		UMLUtil.setTaggedValue(operation, securityRequirementsStereotype, "securityRequirements", pSecurityRequirements);
	}

	public static SchemeType transformSchemeType(edu.uoc.som.openapi.SchemeType from) {
		switch (from) {
		case HTTP:
			return SchemeType.HTTP;
		case HTTPS:
			return SchemeType.HTTPS;
		case WS:
			return SchemeType.WS;
		case WSS:
			return SchemeType.WSS;
		default:
			return null;
		}
	}

	public static SecuritySchemeType transformSecuritySchemeType(edu.uoc.som.openapi.SecuritySchemeType from) {
		switch (from) {
		case BASIC:
			return SecuritySchemeType.BASIC;
		case API_KEY:
			return SecuritySchemeType.API_KEY;
		case OAUTH2:
			return SecuritySchemeType.OAUTH2;
		default:
			return SecuritySchemeType.UNDEFINED;
		}
	}

	public static APIKeyLocation transformAPIKeyLocation(edu.uoc.som.openapi.APIKeyLocation from) {
		switch (from) {
		case QUERY:
			return APIKeyLocation.QUERY;
		case HEADER:
			return APIKeyLocation.HEADER;
		default:
			return APIKeyLocation.UNDEFINED;
		}
	}

	public static OAuth2FlowType transformOAuth2FlowType(edu.uoc.som.openapi.OAuth2FlowType from) {
		switch (from) {
		case IMPLICIT:
			return OAuth2FlowType.IMPLICIT;
		case PASSWORD:
			return OAuth2FlowType.PASSWORD;
		case APPLICATION:
			return OAuth2FlowType.APPLICATION;
		case ACCESS_CODE:
			return OAuth2FlowType.ACCESS_CODE;
		default:
			return OAuth2FlowType.UNDEFINED;
		}
	}
}
