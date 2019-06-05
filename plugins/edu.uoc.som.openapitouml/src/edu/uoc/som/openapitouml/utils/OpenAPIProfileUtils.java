package edu.uoc.som.openapitouml.utils;

import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.API;
import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.API_INFO;
import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.EXTERNAL_DOCS;
import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.TAGS;
import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.SCHEMA;
import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.API_DATA_TYPE;
import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.API_PARAMETER;
import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.API_RESPONSE;
import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.API_OPERATION;
import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.SECURITY_DEFINITIONS;
import static edu.som.uoc.openapiprofile.OpenapiprofilePackage.Literals.SECURITY;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.util.UMLUtil;

import edu.som.uoc.openapiprofile.APIKeyLocation;
import edu.som.uoc.openapiprofile.CollectionFormat;
import edu.som.uoc.openapiprofile.Contact;
import edu.som.uoc.openapiprofile.Example;
import edu.som.uoc.openapiprofile.HTTPMethod;
import edu.som.uoc.openapiprofile.JSONDataType;
import edu.som.uoc.openapiprofile.License;
import edu.som.uoc.openapiprofile.OAuth2FlowType;
import edu.som.uoc.openapiprofile.OpenapiprofileFactory;
import edu.som.uoc.openapiprofile.ParameterLocation;
import edu.som.uoc.openapiprofile.SchemeType;
import edu.som.uoc.openapiprofile.SecuritySchemeType;
import edu.som.uoc.openapiprofile.SecurityScope;
import edu.som.uoc.openapiprofile.XMLElement;
import edu.uoc.som.openapi.API;
import edu.uoc.som.openapi.ExternalDocs;
import edu.uoc.som.openapi.Header;
import edu.uoc.som.openapi.Info;
import edu.uoc.som.openapi.JSONSchemaSubset;
import edu.uoc.som.openapi.Path;
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

	public static void applySchemaStereotype(Element element, Schema schema) {
		Stereotype schemaStereotype = element.getApplicableStereotype(SCHEMA_QN);
		if (!element.isStereotypeApplied(schemaStereotype))
			element.applyStereotype(schemaStereotype);
		UMLUtil.setTaggedValue(element, schemaStereotype, "title", schema.getTitle());
		UMLUtil.setTaggedValue(element, schemaStereotype, "maxProperties", schema.getMaxProperties());
		UMLUtil.setTaggedValue(element, schemaStereotype, "minProperties", schema.getMinProperties());
		UMLUtil.setTaggedValue(element, schemaStereotype, "discriminator", schema.getDiscriminator());
		UMLUtil.setTaggedValue(element, schemaStereotype, "additionalPropertiesAllowed",
				schema.getAdditonalPropertiesAllowed());
		UMLUtil.setTaggedValue(element, schemaStereotype, "example",
				schema.getExample());
		UMLUtil.setTaggedValue(element, schemaStereotype, "readOnly",
				schema.getReadOnly());
		
		if(schema.getXml()!= null) {
			XMLElement pXMLElement = OpenapiprofileFactory.eINSTANCE.createXMLElement();
			pXMLElement.setAttribute(schema.getXml().getAttribute());
			pXMLElement.setName(schema.getXml().getName());
			pXMLElement.setNamespace(schema.getXml().getNamespace());
			pXMLElement.setPrefix(schema.getXml().getPrefix());
			pXMLElement.setWrapped(schema.getXml().getWrapped());
			UMLUtil.setTaggedValue(element, schemaStereotype, "xml", pXMLElement);
		}
		addJSONSchemaSubsetAttribute(element, schemaStereotype, schema);
	

	}

	

	public static void applyAPIDataTypeStereotype(Type type, Schema schema) {
		Stereotype apiDataTypeStereotype = type.getApplicableStereotype(API_DATA_TYPE_QN);
		if (!type.isStereotypeApplied(apiDataTypeStereotype))
			type.applyStereotype(apiDataTypeStereotype);
		UMLUtil.setTaggedValue(type, apiDataTypeStereotype, "type",
				transformJSONDataType(schema.getType()));
		UMLUtil.setTaggedValue(type, apiDataTypeStereotype, "format",schema.getFormat());
	}

	public static void applyAPIParameterStereotype(Parameter parameter, edu.uoc.som.openapi.Parameter mParameter) {
		Stereotype apiParameterStereotype = parameter.getApplicableStereotype(API_PARAMETER_QN);
		if (!parameter.isStereotypeApplied(apiParameterStereotype))
			parameter.applyStereotype(apiParameterStereotype);
		UMLUtil.setTaggedValue(parameter, apiParameterStereotype, "description", mParameter.getDescription());
		if(mParameter.getLocation()!= null)
			UMLUtil.setTaggedValue(parameter, apiParameterStereotype, "location", transformParameterLocation(mParameter.getLocation()));
		UMLUtil.setTaggedValue(parameter, apiParameterStereotype, "allowEmptyValues", mParameter.getAllowEmplyValue());
		if(mParameter.getCollectionFormat()!=null)
			UMLUtil.setTaggedValue(parameter, apiParameterStereotype, "collectionFormat", transformCollectionFormat(mParameter.getCollectionFormat()));
	addJSONSchemaSubsetAttribute(parameter, apiParameterStereotype, mParameter);
	}

	public static void applyAPIResponseStereotype(Parameter parameter, edu.uoc.som.openapi.Response mResponse) {
		Stereotype apiResponseStereotype = parameter.getApplicableStereotype(API_RESPONSE_QN);
		if (!parameter.isStereotypeApplied(apiResponseStereotype))
			parameter.applyStereotype(apiResponseStereotype);
		UMLUtil.setTaggedValue(parameter, apiResponseStereotype, "description", mResponse.getDeclaringContext());
		UMLUtil.setTaggedValue(parameter, apiResponseStereotype, "code", mResponse.getCode());
		if(!mResponse.getHeaders().isEmpty()) {
			List<edu.som.uoc.openapiprofile.Header> pHeaders = new ArrayList<edu.som.uoc.openapiprofile.Header>();
			for(Header mHeader : mResponse.getHeaders()) {
				edu.som.uoc.openapiprofile.Header pHeader = OpenapiprofileFactory.eINSTANCE.createHeader();
				pHeader.setName(mHeader.getName());
				pHeader.setDescription(mHeader.getDescription());
				if(mHeader.getType()!=null)
					pHeader.setType(transformJSONDataType(mHeader.getType()));
				pHeader.setFormat(mHeader.getFormat());
				if(mHeader.getCollectionFormat()!=null)
					pHeader.setCollectionFormat(transformCollectionFormat(mHeader.getCollectionFormat()));
				pHeader.setMinItems(mHeader.getMinItems());
				pHeader.setPattern(mHeader.getPattern());
				pHeader.setExclusiveMinimum(mHeader.getExclusiveMinimum());
				pHeader.setMinimum(mHeader.getMinimum());
				pHeader.setMinLength(mHeader.getMinLength());
				pHeader.setMaxLength(mHeader.getMaxLength());
				pHeader.setMaximum(mHeader.getMaximum());
				pHeader.setMaxItems(mHeader.getMaxItems());
				pHeader.setExclusiveMaximum(mHeader.getExclusiveMaximum());
				pHeader.setDefault(mHeader.getDefault());
				pHeader.setUniqueItems(mHeader.getUniqueItems());
				pHeader.setMultipleOf(mHeader.getMultipleOf());
				pHeader.getEnum().addAll(mHeader.getEnum());
				pHeaders.add(pHeader);
				
			}
			UMLUtil.setTaggedValue(parameter, apiResponseStereotype, "headers", pHeaders);
		}
		if(!mResponse.getExamples().isEmpty()) {
			List<Example> pExamples = new ArrayList<Example>();
			for(edu.uoc.som.openapi.Example mExample: mResponse.getExamples()) {
				Example  pExample = OpenapiprofileFactory.eINSTANCE.createExample();
				pExample.setMimeType(mExample.getMimeType());
				pExample.setValue(mExample.getValue());
				pExamples.add(pExample);
			}
			UMLUtil.setTaggedValue(parameter, apiResponseStereotype, "examples", pExamples);
		}
	}

	public static void applyAPIOperationeStereotype(Operation operation, edu.uoc.som.openapi.Operation mOperation) {
		Stereotype apiOperationStereotype = operation.getApplicableStereotype(API_OPERATION_QN);
		if (!operation.isStereotypeApplied(apiOperationStereotype))
			operation.applyStereotype(apiOperationStereotype);
		UMLUtil.setTaggedValue(operation, apiOperationStereotype, "relativePath", ((Path) mOperation.eContainer()).getRelativePath());
		UMLUtil.setTaggedValue(operation, apiOperationStereotype, "method", extractHTTPMethod(mOperation.getMethod()));
		if (!mOperation.getSchemes().isEmpty()) {
			List<SchemeType> schemeTypes = new ArrayList<SchemeType>();
			for (edu.uoc.som.openapi.SchemeType from : mOperation.getSchemes())
				schemeTypes.add(transformSchemeType(from));
			UMLUtil.setTaggedValue(operation, apiOperationStereotype, "schemes", schemeTypes);
		}
		if (!mOperation.getConsumes().isEmpty())
			UMLUtil.setTaggedValue(operation, apiOperationStereotype, "consumes", mOperation.getConsumes());
		if (!mOperation.getProduces().isEmpty())
			UMLUtil.setTaggedValue(operation, apiOperationStereotype, "produces", mOperation.getProduces());
		UMLUtil.setTaggedValue(operation, apiOperationStereotype, "description", mOperation.getDescription());
		UMLUtil.setTaggedValue(operation, apiOperationStereotype, "summary", mOperation.getSummary());
		if (!mOperation.getTagReferences().isEmpty()) {
			UMLUtil.setTaggedValue(operation, apiOperationStereotype, "tags", mOperation.getTagReferences());
		}
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
					pSecurityScope.setName(mSecurityScope.getName());
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
			pSecurityRequirement.setName(mSecurityRequirement.getSecurityScheme().getReferenceName());
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
	public static JSONDataType transformJSONDataType(edu.uoc.som.openapi.JSONDataType from) {
		switch (from) {
		case BOOLEAN:
			return JSONDataType.BOOLEAN;
		case INTEGER:
			return JSONDataType.INTEGER;
		case NUMBER:
			return JSONDataType.NUMBER;
		case STRING:
			return JSONDataType.STRING;
		case FILE:
			return JSONDataType.FILE;
		case UNSPECIFIED:
			return JSONDataType.UNDEFINED;
		default:
			return null;
		}
	}
	public static HTTPMethod extractHTTPMethod(String method) {
		switch (method) {
		case "GET":
			return HTTPMethod.GET;
		case "PUT":
			return HTTPMethod.PUT;
		case "POST":
			return HTTPMethod.POST;
		case "DELETE":
			return HTTPMethod.DELETE;
		case "OPTIONS":
			return HTTPMethod.OPTIONS;
		case "PATCH":
			return HTTPMethod.PATCH;
		case "HEAD":
			return HTTPMethod.HEAD;
		default:
			return null;
		}	
	}
	public static ParameterLocation transformParameterLocation(edu.uoc.som.openapi.ParameterLocation from) {
		switch (from) {
		case BODY:
			return ParameterLocation.BODY;
		case HEADER:
			return ParameterLocation.HEADER;
		case QUERY:
			return ParameterLocation.QUERY;
		case FORM_DATA:
			return ParameterLocation.FORM_DATA;
		case PATH:
			return ParameterLocation.PATH;
		case UNSPECIFIED:
			return ParameterLocation.UNDEFINED;
		}
		return null;
	}
	
	public static CollectionFormat transformCollectionFormat(edu.uoc.som.openapi.CollectionFormat from) {
		switch (from) {
		case CSV:
			return CollectionFormat.CSV;
		case MULTI:
			return CollectionFormat.MULTI;
		case PIPES:
			return CollectionFormat.PIPES;
		case SSV:
			return CollectionFormat.SSV;
		case TSV:
			return CollectionFormat.TSV;
		case UNSPECIFIED:
			return CollectionFormat.UNDEFINED;
		default:
			return null;
		}
	}
	private static void addJSONSchemaSubsetAttribute(Element element, Stereotype stereotype, JSONSchemaSubset jsonSchemaSubset) {
		UMLUtil.setTaggedValue(element, stereotype, "pattern", jsonSchemaSubset.getPattern());
		UMLUtil.setTaggedValue(element, stereotype, "exclusiveMinimum", jsonSchemaSubset.getExclusiveMinimum());
		UMLUtil.setTaggedValue(element, stereotype, "maximum", jsonSchemaSubset.getMaximum());
		UMLUtil.setTaggedValue(element, stereotype, "minimum", jsonSchemaSubset.getMinimum());
		UMLUtil.setTaggedValue(element, stereotype, "maxLength", jsonSchemaSubset.getMaxLength());
		UMLUtil.setTaggedValue(element, stereotype, "exclusiveMaximum", jsonSchemaSubset.getExclusiveMaximum());
		UMLUtil.setTaggedValue(element, stereotype, "minLength", jsonSchemaSubset.getMinLength());
		UMLUtil.setTaggedValue(element, stereotype, "maxItems", jsonSchemaSubset.getMaxItems());
		UMLUtil.setTaggedValue(element, stereotype, "minItems", jsonSchemaSubset.getMinItems());
		UMLUtil.setTaggedValue(element, stereotype, "description", jsonSchemaSubset.getDescription());
		UMLUtil.setTaggedValue(element, stereotype, "uniqueItems", jsonSchemaSubset.getUniqueItems());
		UMLUtil.setTaggedValue(element, stereotype, "default", jsonSchemaSubset.getDefault());
		UMLUtil.setTaggedValue(element, stereotype, "multipleOf", jsonSchemaSubset.getMultipleOf());
	}
}
