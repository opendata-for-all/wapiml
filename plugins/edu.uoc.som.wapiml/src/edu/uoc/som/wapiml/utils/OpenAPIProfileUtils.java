package edu.uoc.som.wapiml.utils;

import static edu.uoc.som.openapi2.profile.OpenAPIProfilePackage.Literals.API;
import static edu.uoc.som.openapi2.profile.OpenAPIProfilePackage.Literals.API_INFO;
import static edu.uoc.som.openapi2.profile.OpenAPIProfilePackage.Literals.EXTERNAL_DOCS;
import static edu.uoc.som.openapi2.profile.OpenAPIProfilePackage.Literals.TAGS;
import static edu.uoc.som.openapi2.profile.OpenAPIProfilePackage.Literals.SCHEMA;
import static edu.uoc.som.openapi2.profile.OpenAPIProfilePackage.Literals.API_DATA_TYPE;
import static edu.uoc.som.openapi2.profile.OpenAPIProfilePackage.Literals.API_PARAMETER;
import static edu.uoc.som.openapi2.profile.OpenAPIProfilePackage.Literals.API_RESPONSE;
import static edu.uoc.som.openapi2.profile.OpenAPIProfilePackage.Literals.API_OPERATION;
import static edu.uoc.som.openapi2.profile.OpenAPIProfilePackage.Literals.SECURITY_DEFINITIONS;
import static edu.uoc.som.openapi2.profile.OpenAPIProfilePackage.Literals.SECURITY;
import static edu.uoc.som.openapi2.profile.OpenAPIProfilePackage.Literals.API_PROPERTY;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.math.NumberUtils;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.util.UMLUtil;

import edu.uoc.som.openapi2.profile.APIKeyLocation;
import edu.uoc.som.openapi2.profile.CollectionFormat;
import edu.uoc.som.openapi2.profile.Contact;
import edu.uoc.som.openapi2.profile.Example;
import edu.uoc.som.openapi2.profile.HTTPMethod;
import edu.uoc.som.openapi2.profile.JSONDataType;
import edu.uoc.som.openapi2.profile.License;
import edu.uoc.som.openapi2.profile.OAuth2FlowType;
import edu.uoc.som.openapi2.profile.OpenAPIProfileFactory;
import edu.uoc.som.openapi2.profile.ParameterLocation;
import edu.uoc.som.openapi2.profile.RequiredSecurityScheme;
import edu.uoc.som.openapi2.profile.SchemeType;
import edu.uoc.som.openapi2.profile.SecuritySchemeType;
import edu.uoc.som.openapi2.profile.SecurityScope;
import edu.uoc.som.openapi2.profile.XMLElement;
import edu.uoc.som.openapi2.API;
import edu.uoc.som.openapi2.ExternalDocs;
import edu.uoc.som.openapi2.Header;
import edu.uoc.som.openapi2.Info;
import edu.uoc.som.openapi2.JSONSchemaSubset;
import edu.uoc.som.openapi2.Path;
import edu.uoc.som.openapi2.Response;
import edu.uoc.som.openapi2.Schema;
import edu.uoc.som.openapi2.SecurityRequirement;
import edu.uoc.som.openapi2.SecurityScheme;
import edu.uoc.som.openapi2.Tag;

public class OpenAPIProfileUtils {

	public static final String API_QN = OpenAPIStereotypesUtils.getStereotypeQn(API);
	public static final String API_INFO_QN = OpenAPIStereotypesUtils.getStereotypeQn(API_INFO);
	public static final String EXTERNAL_DOCS_QN = OpenAPIStereotypesUtils.getStereotypeQn(EXTERNAL_DOCS);
	public static final String TAGS_QN = OpenAPIStereotypesUtils.getStereotypeQn(TAGS);
	public static final String SCHEMA_QN = OpenAPIStereotypesUtils.getStereotypeQn(SCHEMA);
	public static final String API_DATA_TYPE_QN = OpenAPIStereotypesUtils.getStereotypeQn(API_DATA_TYPE);
	public static final String API_PARAMETER_QN = OpenAPIStereotypesUtils.getStereotypeQn(API_PARAMETER);
	public static final String API_RESPONSE_QN = OpenAPIStereotypesUtils.getStereotypeQn(API_RESPONSE);
	public static final String API_OPERATION_QN = OpenAPIStereotypesUtils.getStereotypeQn(API_OPERATION);
	public static final String SECURITY_DEFINITIONS_QN = OpenAPIStereotypesUtils.getStereotypeQn(SECURITY_DEFINITIONS);
	public static final String SECURITY_QN = OpenAPIStereotypesUtils.getStereotypeQn(SECURITY);
	public static final String API_PROPERTY_QN = OpenAPIStereotypesUtils.getStereotypeQn(API_PROPERTY);


	public static void applyAPIStereotype(Model model, API api) {
		Stereotype apiStereotype = model.getApplicableStereotype(API_QN);
		if (!model.isStereotypeApplied(apiStereotype)) {
			model.applyStereotype(apiStereotype);
		}
		UMLUtil.setTaggedValue(model, apiStereotype, "host", api.getHost());
		UMLUtil.setTaggedValue(model, apiStereotype, "basePath", api.getBasePath());
		if (!api.getSchemes().isEmpty()) {
			List<SchemeType> schemeTypes = new ArrayList<SchemeType>();
			for (edu.uoc.som.openapi2.SchemeType from : api.getSchemes())
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
			Contact pContact = OpenAPIProfileFactory.eINSTANCE.createContact();
			pContact.setEmail(info.getContact().getEmail());
			pContact.setName(info.getContact().getName());
			pContact.setUrl(info.getContact().getUrl());
			UMLUtil.setTaggedValue(model, infoStereotype, "contact", pContact);
		}
		if (info.getLicense() != null) {
			License pLicense = OpenAPIProfileFactory.eINSTANCE.createLicense();
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
		List<edu.uoc.som.openapi2.profile.Tag> pTags = new ArrayList<edu.uoc.som.openapi2.profile.Tag>();
		for (Tag mTag : tagList) {
			edu.uoc.som.openapi2.profile.Tag pTag = OpenAPIProfileFactory.eINSTANCE.createTag();
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
		UMLUtil.setTaggedValue(element, schemaStereotype, "description", schema.getDescription());
		UMLUtil.setTaggedValue(element, schemaStereotype, "maxProperties", schema.getMaxProperties());
		UMLUtil.setTaggedValue(element, schemaStereotype, "minProperties", schema.getMinProperties());
		UMLUtil.setTaggedValue(element, schemaStereotype, "discriminator", schema.getDiscriminator());
		UMLUtil.setTaggedValue(element, schemaStereotype, "additionalPropertiesAllowed",
				schema.getAdditonalPropertiesAllowed());
		UMLUtil.setTaggedValue(element, schemaStereotype, "example", schema.getExample());
		UMLUtil.setTaggedValue(element, schemaStereotype, "default", schema.getDefault());

	}
	
	public static void applyAPIPropertyStereotype(Property property, edu.uoc.som.openapi2.Property apiProperty) {
		Stereotype apiPropertyStereotype = property.getApplicableStereotype(API_PROPERTY_QN);
		if (!property.isStereotypeApplied(apiPropertyStereotype))
			property.applyStereotype(apiPropertyStereotype);
		if(apiProperty.getSchema()!=null)
		UMLUtil.setTaggedValue(property, apiPropertyStereotype, "title", apiProperty.getSchema().getTitle());
		if(apiProperty.getSchema()!=null)
		UMLUtil.setTaggedValue(property, apiPropertyStereotype, "example", apiProperty.getSchema().getExample());
		UMLUtil.setTaggedValue(property, apiPropertyStereotype, "required", apiProperty.getRequired());
			

		if (apiProperty.getSchema()!=null && apiProperty.getSchema().getXml() != null) {
			XMLElement pXMLElement = OpenAPIProfileFactory.eINSTANCE.createXMLElement();
			pXMLElement.setAttribute(apiProperty.getSchema().getXml().getAttribute());
			pXMLElement.setName(apiProperty.getSchema().getXml().getName());
			pXMLElement.setNamespace(apiProperty.getSchema().getXml().getNamespace());
			pXMLElement.setPrefix(apiProperty.getSchema().getXml().getPrefix());
			pXMLElement.setWrapped(apiProperty.getSchema().getXml().getWrapped());
			UMLUtil.setTaggedValue(property, apiPropertyStereotype, "xml", pXMLElement);
		}
		if(apiProperty.getSchema()!=null)
			addJSONSchemaSubsetAttribute(property, apiPropertyStereotype, apiProperty.getSchema());

	}


	public static void applyAPIDataTypeStereotype(Type type, edu.uoc.som.openapi2.JSONDataType jsonDataType, String format) {
		Stereotype apiDataTypeStereotype = type.getApplicableStereotype(API_DATA_TYPE_QN);
		if (!type.isStereotypeApplied(apiDataTypeStereotype))
			type.applyStereotype(apiDataTypeStereotype);
		UMLUtil.setTaggedValue(type, apiDataTypeStereotype, "type", transformJSONDataType(jsonDataType));
		UMLUtil.setTaggedValue(type, apiDataTypeStereotype, "format", format);
	}

	public static void applyAPIParameterStereotype(Parameter parameter, edu.uoc.som.openapi2.Parameter mParameter) {
		Stereotype apiParameterStereotype = parameter.getApplicableStereotype(API_PARAMETER_QN);
		if (!parameter.isStereotypeApplied(apiParameterStereotype))
			parameter.applyStereotype(apiParameterStereotype);
		UMLUtil.setTaggedValue(parameter, apiParameterStereotype, "description", mParameter.getDescription());
		if (mParameter.getLocation() != null)
			UMLUtil.setTaggedValue(parameter, apiParameterStereotype, "location",
					transformParameterLocation(mParameter.getLocation()));
		UMLUtil.setTaggedValue(parameter, apiParameterStereotype, "allowEmptyValue", mParameter.getAllowEmplyValue());
		if (mParameter.getCollectionFormat() != null)
			UMLUtil.setTaggedValue(parameter, apiParameterStereotype, "collectionFormat",
					transformCollectionFormat(mParameter.getCollectionFormat()));
		UMLUtil.setTaggedValue(parameter, apiParameterStereotype, "required", mParameter.getRequired());
		addJSONSchemaSubsetAttribute(parameter, apiParameterStereotype, mParameter);
	}

	public static void applyAPIResponseStereotype(Parameter parameter, Entry<String, Response> response) {
		Stereotype apiResponseStereotype = parameter.getApplicableStereotype(API_RESPONSE_QN);
		if (!parameter.isStereotypeApplied(apiResponseStereotype))
			parameter.applyStereotype(apiResponseStereotype);
		UMLUtil.setTaggedValue(parameter, apiResponseStereotype, "description", response.getValue().getDescription());
		if( response.getKey().equals("default")) {
		UMLUtil.setTaggedValue(parameter, apiResponseStereotype, "default", response.getKey());
		}
		else {
			if(NumberUtils.isNumber(response.getKey()))
			UMLUtil.setTaggedValue(parameter, apiResponseStereotype, "code", response.getKey());
		}
		if (!response.getValue().getHeaders().isEmpty()) {
			List<edu.uoc.som.openapi2.profile.Header> pHeaders = new ArrayList<edu.uoc.som.openapi2.profile.Header>();
			for (Header mHeader : response.getValue().getHeaders()) {
				edu.uoc.som.openapi2.profile.Header pHeader = OpenAPIProfileFactory.eINSTANCE.createHeader();
				pHeader.setName(mHeader.getName());
				pHeader.setDescription(mHeader.getDescription());
				if (mHeader.getType() != null)
					pHeader.setType(transformJSONDataType(mHeader.getType()));
				pHeader.setFormat(mHeader.getFormat());
				if (mHeader.getCollectionFormat() != null)
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
		if (!response.getValue().getExamples().isEmpty()) {
			List<Example> pExamples = new ArrayList<Example>();
			for (edu.uoc.som.openapi2.Example mExample : response.getValue().getExamples()) {
				Example pExample = OpenAPIProfileFactory.eINSTANCE.createExample();
				pExample.setMimeType(mExample.getMimeType());
				pExample.setValue(mExample.getValue());
				pExamples.add(pExample);
			}
			UMLUtil.setTaggedValue(parameter, apiResponseStereotype, "examples", pExamples);
		}
	}

	public static void applyAPIOperationeStereotype(Operation operation, edu.uoc.som.openapi2.Operation mOperation) {
		Stereotype apiOperationStereotype = operation.getApplicableStereotype(API_OPERATION_QN);
		if (!operation.isStereotypeApplied(apiOperationStereotype))
			operation.applyStereotype(apiOperationStereotype);
		UMLUtil.setTaggedValue(operation, apiOperationStereotype, "relativePath",
				((Path) mOperation.eContainer()).getRelativePath());
		UMLUtil.setTaggedValue(operation, apiOperationStereotype, "method", extractHTTPMethod(mOperation.getHTTPMethod()));
		if (!mOperation.getSchemes().isEmpty()) {
			List<SchemeType> schemeTypes = new ArrayList<SchemeType>();
			for (edu.uoc.som.openapi2.SchemeType from : mOperation.getSchemes())
				schemeTypes.add(transformSchemeType(from));
			UMLUtil.setTaggedValue(operation, apiOperationStereotype, "schemes", schemeTypes);
		}
		if (!mOperation.getConsumes().isEmpty())
			UMLUtil.setTaggedValue(operation, apiOperationStereotype, "consumes", mOperation.getConsumes());
		if (!mOperation.getProduces().isEmpty())
			UMLUtil.setTaggedValue(operation, apiOperationStereotype, "produces", mOperation.getProduces());
		UMLUtil.setTaggedValue(operation, apiOperationStereotype, "description", mOperation.getDescription());
		UMLUtil.setTaggedValue(operation, apiOperationStereotype, "summary", mOperation.getSummary());
		UMLUtil.setTaggedValue(operation, apiOperationStereotype, "deprecated", mOperation.getDeprecated());
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

	public static void applySecurityDefinitionsStereotype(Model model, EMap<String, SecurityScheme> eMap) {
		Stereotype securityDefinitionsStereotype = model.getApplicableStereotype(SECURITY_DEFINITIONS_QN);
		if (!model.isStereotypeApplied(securityDefinitionsStereotype)) {
			model.applyStereotype(securityDefinitionsStereotype);
		}
		List<edu.uoc.som.openapi2.profile.SecurityScheme> pSecuritySchemes = new ArrayList<edu.uoc.som.openapi2.profile.SecurityScheme>();
		for (Entry<String, SecurityScheme> mSecurityScheme : eMap) {
			edu.uoc.som.openapi2.profile.SecurityScheme pSecurityScheme = OpenAPIProfileFactory.eINSTANCE
					.createSecurityScheme();
			pSecurityScheme.setReferenceName(mSecurityScheme.getKey());
			pSecurityScheme.setName(mSecurityScheme.getValue().getName());
			if (mSecurityScheme.getValue().getType() != null) {
				pSecurityScheme.setType(transformSecuritySchemeType(mSecurityScheme.getValue().getType()));
			}
			pSecurityScheme.setDescription(mSecurityScheme.getValue().getDescription());
			if (mSecurityScheme.getValue().getLocation() != null)
				pSecurityScheme.setLocation(transformAPIKeyLocation(mSecurityScheme.getValue().getLocation()));
			if (mSecurityScheme.getValue().getFlow() != null)
				pSecurityScheme.setFlow(transformOAuth2FlowType(mSecurityScheme.getValue().getFlow()));
			pSecurityScheme.setAuthorizationURL(mSecurityScheme.getValue().getAuthorizationUrl());
			pSecurityScheme.setTokenURL(mSecurityScheme.getValue().getTokenUrl());
			if (!mSecurityScheme.getValue().getScopes().isEmpty()) {
				for (edu.uoc.som.openapi2.SecurityScope mSecurityScope : mSecurityScheme.getValue().getScopes()) {
					SecurityScope pSecurityScope = OpenAPIProfileFactory.eINSTANCE.createSecurityScope();
					pSecurityScope.setDescription(mSecurityScope.getDescription());
					pSecurityScope.setName(mSecurityScope.getName());
					pSecurityScheme.getScopes().add(pSecurityScope);
				}
			}
			pSecuritySchemes.add(pSecurityScheme);
			UMLUtil.setTaggedValue(model, securityDefinitionsStereotype, "securitySchemes", pSecuritySchemes);
		}
	}



	public static void applySecurityStereotype(Element element, List<SecurityRequirement> securityRequirements) {
		Stereotype securityRequirementsStereotype = element.getApplicableStereotype(SECURITY_QN);
		if (!element.isStereotypeApplied(securityRequirementsStereotype)) {
			element.applyStereotype(securityRequirementsStereotype);
		}
		List<edu.uoc.som.openapi2.profile.SecurityRequirement> pSecurityRequirements = new ArrayList<edu.uoc.som.openapi2.profile.SecurityRequirement>();
		for (SecurityRequirement mSecurityRequirement : securityRequirements) {
			edu.uoc.som.openapi2.profile.SecurityRequirement pSecurityRequirement = OpenAPIProfileFactory.eINSTANCE
					.createSecurityRequirement();
			for(edu.uoc.som.openapi2.RequiredSecurityScheme mRequiredSecurityScheme : mSecurityRequirement.getSecuritySchemes()) {
				RequiredSecurityScheme pRequiredSecurityScheme = OpenAPIProfileFactory.eINSTANCE.createRequiredSecurityScheme();
				pRequiredSecurityScheme.setName(mRequiredSecurityScheme.getSecurityScheme().getName());
			for (edu.uoc.som.openapi2.SecurityScope mScope : mRequiredSecurityScheme.getSecurityScheme().getScopes()) {
				pRequiredSecurityScheme.getScopes().add(mScope.getName());
			}
			pSecurityRequirement.getSecuritySchemes().add(pRequiredSecurityScheme);
			}
			pSecurityRequirements.add(pSecurityRequirement);
		}
		
		UMLUtil.setTaggedValue(element, securityRequirementsStereotype, "securityRequirements", pSecurityRequirements);
	}



	public static SchemeType transformSchemeType(edu.uoc.som.openapi2.SchemeType from) {
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

	public static SecuritySchemeType transformSecuritySchemeType(edu.uoc.som.openapi2.SecuritySchemeType from) {
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

	public static APIKeyLocation transformAPIKeyLocation(edu.uoc.som.openapi2.APIKeyLocation from) {
		switch (from) {
		case QUERY:
			return APIKeyLocation.QUERY;
		case HEADER:
			return APIKeyLocation.HEADER;
		default:
			return APIKeyLocation.UNDEFINED;
		}
	}

	public static OAuth2FlowType transformOAuth2FlowType(edu.uoc.som.openapi2.OAuth2FlowType from) {
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

	public static JSONDataType transformJSONDataType(edu.uoc.som.openapi2.JSONDataType from) {
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

	public static ParameterLocation transformParameterLocation(edu.uoc.som.openapi2.ParameterLocation from) {
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

	public static CollectionFormat transformCollectionFormat(edu.uoc.som.openapi2.CollectionFormat from) {
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

	public static edu.uoc.som.openapi2.SchemeType transformSchemeType(SchemeType from) {
		switch (from) {
		case HTTP:
			return edu.uoc.som.openapi2.SchemeType.HTTP;
		case HTTPS:
			return edu.uoc.som.openapi2.SchemeType.HTTPS;
		case WS:
			return edu.uoc.som.openapi2.SchemeType.WS;
		case WSS:
			return edu.uoc.som.openapi2.SchemeType.WSS;
		default:
			return null;
		}
	}

	public static edu.uoc.som.openapi2.SecuritySchemeType transformSecuritySchemeType(SecuritySchemeType from) {
		switch (from) {
		case BASIC:
			return edu.uoc.som.openapi2.SecuritySchemeType.BASIC;
		case API_KEY:
			return edu.uoc.som.openapi2.SecuritySchemeType.API_KEY;
		case OAUTH2:
			return edu.uoc.som.openapi2.SecuritySchemeType.OAUTH2;
		default:
			return edu.uoc.som.openapi2.SecuritySchemeType.UNSPECIFIED;
		}
	}

	public static edu.uoc.som.openapi2.APIKeyLocation transformAPIKeyLocation(APIKeyLocation from) {
		switch (from) {
		case QUERY:
			return edu.uoc.som.openapi2.APIKeyLocation.QUERY;
		case HEADER:
			return edu.uoc.som.openapi2.APIKeyLocation.HEADER;
		default:
			return edu.uoc.som.openapi2.APIKeyLocation.UNSPECIFIED;
		}
	}

	public static edu.uoc.som.openapi2.OAuth2FlowType transformOAuth2FlowType(OAuth2FlowType from) {
		switch (from) {
		case IMPLICIT:
			return edu.uoc.som.openapi2.OAuth2FlowType.IMPLICIT;
		case PASSWORD:
			return edu.uoc.som.openapi2.OAuth2FlowType.PASSWORD;
		case APPLICATION:
			return edu.uoc.som.openapi2.OAuth2FlowType.APPLICATION;
		case ACCESS_CODE:
			return edu.uoc.som.openapi2.OAuth2FlowType.ACCESS_CODE;
		default:
			return edu.uoc.som.openapi2.OAuth2FlowType.UNSPECIFIED;
		}
	}

	public static edu.uoc.som.openapi2.JSONDataType transformJSONDataType(JSONDataType from) {
		switch (from) {
		case BOOLEAN:
			return edu.uoc.som.openapi2.JSONDataType.BOOLEAN;
		case INTEGER:
			return edu.uoc.som.openapi2.JSONDataType.INTEGER;
		case NUMBER:
			return edu.uoc.som.openapi2.JSONDataType.NUMBER;
		case STRING:
			return edu.uoc.som.openapi2.JSONDataType.STRING;
		case FILE:
			return edu.uoc.som.openapi2.JSONDataType.FILE;
		case UNDEFINED:
			return edu.uoc.som.openapi2.JSONDataType.UNSPECIFIED;
		default:
			return null;
		}
	}

	public static edu.uoc.som.openapi2.ParameterLocation transformParameterLocation(ParameterLocation from) {
		switch (from) {
		case BODY:
			return edu.uoc.som.openapi2.ParameterLocation.BODY;
		case HEADER:
			return edu.uoc.som.openapi2.ParameterLocation.HEADER;
		case QUERY:
			return edu.uoc.som.openapi2.ParameterLocation.QUERY;
		case FORM_DATA:
			return edu.uoc.som.openapi2.ParameterLocation.FORM_DATA;
		case PATH:
			return edu.uoc.som.openapi2.ParameterLocation.PATH;
		case UNDEFINED:
			return edu.uoc.som.openapi2.ParameterLocation.UNSPECIFIED;
		}
		return null;
	}

	public static edu.uoc.som.openapi2.CollectionFormat transformCollectionFormat(CollectionFormat from) {
		switch (from) {
		case CSV:
			return edu.uoc.som.openapi2.CollectionFormat.CSV;
		case MULTI:
			return edu.uoc.som.openapi2.CollectionFormat.MULTI;
		case PIPES:
			return edu.uoc.som.openapi2.CollectionFormat.PIPES;
		case SSV:
			return edu.uoc.som.openapi2.CollectionFormat.SSV;
		case TSV:
			return edu.uoc.som.openapi2.CollectionFormat.TSV;
		case UNDEFINED:
			return edu.uoc.som.openapi2.CollectionFormat.UNSPECIFIED;
		default:
			return null;
		}
	}

	private static void addJSONSchemaSubsetAttribute(Element element, Stereotype stereotype,
			JSONSchemaSubset jsonSchemaSubset) {
		UMLUtil.setTaggedValue(element, stereotype, "pattern", jsonSchemaSubset.getPattern());
		UMLUtil.setTaggedValue(element, stereotype, "exclusiveMinimum", jsonSchemaSubset.getExclusiveMinimum());
		UMLUtil.setTaggedValue(element, stereotype, "maximum", jsonSchemaSubset.getMaximum());
		UMLUtil.setTaggedValue(element, stereotype, "minimum", jsonSchemaSubset.getMinimum());
		UMLUtil.setTaggedValue(element, stereotype, "maxLength", jsonSchemaSubset.getMaxLength());
		UMLUtil.setTaggedValue(element, stereotype, "exclusiveMaximum", jsonSchemaSubset.getExclusiveMaximum());
		UMLUtil.setTaggedValue(element, stereotype, "minLength", jsonSchemaSubset.getMinLength());
		UMLUtil.setTaggedValue(element, stereotype, "uniqueItems", jsonSchemaSubset.getUniqueItems());
		UMLUtil.setTaggedValue(element, stereotype, "multipleOf", jsonSchemaSubset.getMultipleOf());
	}
	public static void extractJSONSchemaSubsetproperties(Element element, String stereotypeQN,
			JSONSchemaSubset jsonSchemaSubset) {
		jsonSchemaSubset.setPattern((String) UMLUtil.getTaggedValue(element, stereotypeQN, "pattern"));
		jsonSchemaSubset.setExclusiveMinimum((Boolean) UMLUtil.getTaggedValue(element, stereotypeQN, "exclusiveMinimum"));
		jsonSchemaSubset.setMaximum((Double) UMLUtil.getTaggedValue(element, stereotypeQN, "maximum"));
		jsonSchemaSubset.setMinimum((Double) UMLUtil.getTaggedValue(element, stereotypeQN, "minimum"));
		jsonSchemaSubset.setMaxLength((Integer) UMLUtil.getTaggedValue(element, stereotypeQN, "maxLength"));
		jsonSchemaSubset.setExclusiveMaximum((Boolean) UMLUtil.getTaggedValue(element, stereotypeQN, "exclusiveMaximum"));
		jsonSchemaSubset.setMinLength((Integer) UMLUtil.getTaggedValue(element, stereotypeQN, "minLength"));
		jsonSchemaSubset.setUniqueItems((Boolean) UMLUtil.getTaggedValue(element, stereotypeQN, "uniqueItems"));
		jsonSchemaSubset.setMultipleOf((Double) UMLUtil.getTaggedValue(element, stereotypeQN, "multipleOf"));
	}
}
