package som.openapitouml.generators;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import core.API;
import core.APIKeyLocation;
import core.Contact;
import core.Example;
import core.ExternalDocs;
import core.Header;
import core.Info;
import core.ItemsDefinition;
import core.License;
import core.OAuth2FlowType;
import core.OpenAPIFactory;
import core.Operation;
import core.Parameter;
import core.ParameterLocation;
import core.Path;
import core.Response;
import core.Root;
import core.Schema;
import core.SchemeType;
import core.SecurityRequirement;
import core.SecuritySchema;
import core.SecuritySchemeType;
import core.SecurityScope;
import core.Tag;
import core.XMLElement;
import som.openapitouml.utils.OpenAPIUtils;

public class OpenAPIGenerator {
	OpenAPIFactory openAPIFactory;

	public OpenAPIGenerator() {
		openAPIFactory = OpenAPIFactory.eINSTANCE;
		
	}
	public Root createOpenAPIModelFromJson(File jsonFile) throws FileNotFoundException, UnsupportedEncodingException {
		   InputStream in = new FileInputStream(jsonFile);
	        Reader reader = new InputStreamReader(in, "UTF-8");
			JsonParser parser = new JsonParser();
			JsonElement jsonElement =  parser.parse(reader);
		
		return createOpenAPIModelFromJson(jsonElement.getAsJsonObject());
		
	}
	public Root createOpenAPIModelFromJson(JsonObject jsonObject) {

		Root root = openAPIFactory.createRoot();
		API api = openAPIFactory.createAPI();
		root.setApi(api);
		if (jsonObject.has("swagger"))
			api.setSwagger(jsonObject.get("swagger").getAsString());
		if (jsonObject.has("info")) {
			Info info = openAPIFactory.createInfo();
			api.setInfo(info);
			discoverInfo(jsonObject.get("info"), info);
		}
		if (jsonObject.has("host"))
			api.setHost(jsonObject.get("host").getAsString());

		if (jsonObject.has("basePath"))
			api.setBasePath(jsonObject.get("basePath").getAsString());
		if (jsonObject.has("schemes")) {
			JsonArray schemes = jsonObject.get("schemes").getAsJsonArray();
			for (JsonElement scheme : schemes) {
				api.getSchemes().add(SchemeType.get(scheme.getAsString()));
			}
		}
		if (jsonObject.has("consumes")) {
			JsonArray mimeTypes = jsonObject.get("consumes").getAsJsonArray();
			for (JsonElement mimeType : mimeTypes) {
				api.getConsumes().add(mimeType.getAsString());
			}
		}

		if (jsonObject.has("produces")) {
			JsonArray mimeTypes = jsonObject.get("produces").getAsJsonArray();
			for (JsonElement mimeType : mimeTypes) {
				api.getProduces().add(mimeType.getAsString());
			}
		}
		if (jsonObject.has("definitions")) {
			discoverDefinitions(jsonObject.get("definitions"), root);
		}
		if (jsonObject.has("responses")) {
			discoverResponses(jsonObject.get("responses"), root);
		}
		if (jsonObject.has("securityDefinitions")) {
			discoverSecurityDefinitions(jsonObject.get("securityDefinitions"), root);
		}
		if (jsonObject.has("paths")) {
			discoverPaths(jsonObject.get("paths"), root);
		}
		
		if (jsonObject.has("parameters")) {
			discoverParameters(jsonObject.get("parameters"), root);
		}
		
	

		if (jsonObject.has("security")) {
			discoverSecurity(jsonObject.get("security"), api);
		}
		if (jsonObject.has("tags")) {
			discoverTags(jsonObject.get("tags"), root);
		}
		if (jsonObject.has("externalDocs")) {
			ExternalDocs externalDocs = openAPIFactory.createExternalDocs();
			api.setExternalDocs(externalDocs);
			discoverExternalDocs(jsonObject.get("externalDocs"), externalDocs);
		}
		return root;

	}

	private  void discoverTags(JsonElement jsonElement, Root root) {
		JsonArray tagArray = jsonElement.getAsJsonArray();
		for (JsonElement tagElement : tagArray) {
			Tag tag = openAPIFactory.createTag();
			root.getApi().getTags().add(tag);
			discoverTag(tagElement, tag);
		}

	}

	private  void discoverTag(JsonElement tagElement, Tag tag) {
		JsonObject tagObject = tagElement.getAsJsonObject();
		if (tagObject.has("name"))
			tag.setName(tagObject.get("name").getAsString());
		if (tagObject.has("description"))
			tag.setDescription(tagObject.get("description").getAsString());
		if (tagObject.has("externalDocs")) {
			ExternalDocs externalDocs = openAPIFactory.createExternalDocs();
			tag.setExternalDocs(externalDocs);
			discoverExternalDocs(tagObject.get("externalDocs"), externalDocs);
		}

	}

	private  void discoverSecurity(JsonElement jsonElement, API api) {
		JsonArray securityArray = jsonElement.getAsJsonArray();
		for (JsonElement securityElement : securityArray) {
			SecurityRequirement security = openAPIFactory.createSecurityRequirement();
			api.getSecurityRequirements().add(security);
			discoverSecurityRequirement(securityElement, security, api);
		}

	}

	private  void discoverParameters(JsonElement jsonElement, Root root) {
		JsonObject aPIParametersObject = jsonElement.getAsJsonObject();
		Set<Entry<String, JsonElement>> aPIParameters = aPIParametersObject.entrySet();
		for (Entry<String, JsonElement> aPIParameterElement : aPIParameters) {
			Parameter aPIParameter = openAPIFactory.createParameter();
			aPIParameter.setReferenceName(aPIParameterElement.getKey());
			aPIParameter.setDeclaringContext(root.getApi());
			root.getApi().getParameters().add(aPIParameter);
			root.getParamters().add(aPIParameter);
			discoverParameter(aPIParameterElement.getValue(), aPIParameter, root);
		}

	}

	private  void discoverSecurityDefinitions(JsonElement jsonElement, Root root) {
		JsonObject securityDefinitionsObject = jsonElement.getAsJsonObject();
		Set<Entry<String, JsonElement>> securityDefinitions = securityDefinitionsObject.entrySet();
		for (Entry<String, JsonElement> securityDefinitionElement : securityDefinitions) {
			SecuritySchema securityDefinition = openAPIFactory.createSecuritySchema();
			securityDefinition.setGlobalName(securityDefinitionElement.getKey());
			root.getApi().getSecurityDefinitions().add(securityDefinition);
			discoverSecuritySchema(securityDefinitionElement.getValue(), securityDefinition);
		}

	}

	private  void discoverSecuritySchema(JsonElement jsonElement, SecuritySchema securitySchema) {
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		if (jsonObject.has("type"))
			securitySchema.setType(SecuritySchemeType.get(jsonObject.get("type").getAsString()));
		if (jsonObject.has("description"))
			securitySchema.setDescription(jsonObject.get("description").getAsString());
		if (jsonObject.has("name"))
			securitySchema.setName(jsonObject.get("name").getAsString());
		if (jsonObject.has("in"))
			securitySchema.setLocation(APIKeyLocation.get(jsonObject.get("in").getAsString()));
		if (jsonObject.has("flow"))
			securitySchema.setFlow(OAuth2FlowType.get(jsonObject.get("flow").getAsString()));
		if (jsonObject.has("authorizationUrl"))
			securitySchema.setAuthorizationUrl(jsonObject.get("authorizationUrl").getAsString());
		if (jsonObject.has("tokenUrl"))
			securitySchema.setTokenUrl(jsonObject.get("tokenUrl").getAsString());
		if (jsonObject.has("scopes")) {
			Set<Entry<String, JsonElement>> scopesElements = jsonObject.get("scopes").getAsJsonObject().entrySet();
			for (Entry<String, JsonElement> scopeElement : scopesElements) {
				SecurityScope scope = openAPIFactory.createSecurityScope();
				securitySchema.getScopes().add(scope);

				scope.setName(scopeElement.getKey());

				scope.setDescription(scopeElement.getValue().getAsString());
			}
		}

	}

	private  void discoverResponses(JsonElement jsonElement, Root root) {
		JsonObject responsesObject = jsonElement.getAsJsonObject();
		Set<Entry<String, JsonElement>> responses = responsesObject.entrySet();
		for (Entry<String, JsonElement> responseElement : responses) {
			Response response = openAPIFactory.createResponse();
			response.setCode(responseElement.getKey());
			root.getApi().getResponses().add(response);
			discoverResponse(responseElement.getValue(), response, root);
		}
	}

	private  void discoverDefinitions(JsonElement jsonElement, Root root) {
		JsonObject definitionsObject = jsonElement.getAsJsonObject();
		Set<Entry<String, JsonElement>> definitions = definitionsObject.entrySet();
		for (Entry<String, JsonElement> definitionElement : definitions) {
			Schema schema = openAPIFactory.createSchema();
			schema.setName(definitionElement.getKey());
			root.getSchemas().add(schema);
			schema.setDeclaringContext(root.getApi());
			root.getApi().getDefinitions().add(schema);
		}
		for (Entry<String, JsonElement> definitionElement : definitions) {
			discoverSchema(definitionElement.getValue().getAsJsonObject(), OpenAPIUtils.getSchemaByName(definitionElement.getKey(), root.getApi()),root);
		}
	}

	private  void discoverSchema(JsonObject schemaObject, Schema schema,  Root root) {
		if (schemaObject.has("format"))
			schema.setFormat(schemaObject.get("format").getAsString());
		if (schemaObject.has("description"))
			schema.setDescription(schemaObject.get("description").getAsString());
		if (schemaObject.has("title"))
			schema.setTitle(schemaObject.get("title").getAsString());
		if (schemaObject.has("type"))
			schema.setType(core.JSONDataType.get(schemaObject.get("type").getAsString()));
		if (schemaObject.has("default"))
			schema.setDefault(schemaObject.get("default").toString());
		if (schemaObject.has("maximum"))
			schema.setMaximum(schemaObject.get("maximum").getAsDouble());
		if (schemaObject.has("exclusiveMaximum"))
			schema.setExclusiveMaximum(schemaObject.get("exclusiveMaximum").getAsBoolean());
		if (schemaObject.has("minimum"))
			schema.setMinimum(schemaObject.get("minimum").getAsDouble());
		if (schemaObject.has("exclusiveMinimim"))
			schema.setExclusiveMinimum(schemaObject.get("exclusiveMinimum").getAsBoolean());
		if (schemaObject.has("maxLength"))
			schema.setMaxLength(schemaObject.get("maxLength").getAsInt());
		if (schemaObject.has("minLength"))
			schema.setMinLength(schemaObject.get("minLength").getAsInt());
		if (schemaObject.has("pattern"))
			schema.setPattern(schemaObject.get("pattern").getAsString());
		if (schemaObject.has("maxItems"))
			schema.setMaxItems(schemaObject.get("maxItems").getAsInt());
		if (schemaObject.has("minItems"))
			schema.setMinItems(schemaObject.get("minItems").getAsInt());
		if (schemaObject.has("uniqueItems"))
			schema.setUniqueItems(schemaObject.get("uniqueItems").getAsBoolean());
		if (schemaObject.has("enum")) {
			JsonArray enumItems = schemaObject.get("enum").getAsJsonArray();
			for (JsonElement item : enumItems)
				schema.getEnum().add(item.getAsString());
		}
		if (schemaObject.has("multipleOf"))
			schema.setMultipleOf(schemaObject.get("multipleOf").getAsInt());
		if (schemaObject.has("maxProperties"))
			schema.setMaxProperties(schemaObject.get("maxProperties").getAsInt());
		if (schemaObject.has("minProperties"))
			schema.setMinProperties(schemaObject.get("minProperties").getAsInt());
	
		if (schemaObject.has("properties")) {
			Set<Entry<String, JsonElement>> properties = schemaObject.get("properties").getAsJsonObject().entrySet();
			for (Entry<String, JsonElement> jsonProperty : properties) {
				Schema property = openAPIFactory.createSchema();
				property.setName(jsonProperty.getKey());
				property.setDeclaringContext(schema);
				root.getSchemas().add(property);
				schema.getProperties().add(property);
				JsonObject value = jsonProperty.getValue().getAsJsonObject();
				if(value.has("$ref")) {
					String ref = value.get("$ref").getAsString();
					Schema referencedchema = OpenAPIUtils.getSchemaByPathReference(ref, root.getApi());
					if(value != null) {
						property.setValue(referencedchema);
					}
				}
				else
				discoverSchema(jsonProperty.getValue().getAsJsonObject(), property, root);
			}
		}
		if (schemaObject.has("additionalProperties")) {
			JsonElement additionalProperties = schemaObject.get("additionalProperties");
			
			if(additionalProperties.isJsonPrimitive())
				schema.setAdditonalPropertiesAllowed(additionalProperties.getAsBoolean());
			else 
			{
				JsonObject additionalPropertiesObject = additionalProperties.getAsJsonObject();
				if(additionalPropertiesObject.has("$ref")) {
					String ref = additionalPropertiesObject.get("$ref").getAsString();
					Schema referencedchema = OpenAPIUtils.getSchemaByPathReference(ref, root.getApi());
					schema.setAdditonalProperties(referencedchema);
				}
				else {
					Schema additionalPropertieSchema = openAPIFactory.createSchema();
					schema.setAdditonalProperties(additionalPropertieSchema);
					root.getSchemas().add(additionalPropertieSchema);
					discoverSchema(additionalPropertiesObject, additionalPropertieSchema, root);
				}
			}
				
		
		}
		if (schemaObject.has("allOf")) {
			JsonArray allOfArray = schemaObject.get("allOf").getAsJsonArray();
			for (JsonElement allOfElement : allOfArray) {
				JsonObject allOfObject = allOfElement.getAsJsonObject();
				if(allOfObject.has("$ref")) {
					schema.getAllOf().add(OpenAPIUtils.getSchemaByPathReference(allOfObject.get("$ref").getAsString(), root.getApi()));
				}
				else {
					Schema allOfSchema = openAPIFactory.createSchema();
					schema.getAllOf().add(allOfSchema);
					root.getSchemas().add(allOfSchema);
					discoverSchema(allOfObject, allOfSchema, root);
				}
				
			
			}
		}
		if (schemaObject.has("items")) {
			JsonObject itemsObject = schemaObject.get("items").getAsJsonObject();
			if(itemsObject.has("$ref")) {
				schema.setItems(OpenAPIUtils.getSchemaByPathReference(itemsObject.get("$ref").getAsString(),root.getApi()));
			}
			else {
				Schema itemsSchema = openAPIFactory.createSchema();
				schema.setItems(itemsSchema);
				root.getSchemas().add(itemsSchema);
				discoverSchema(itemsObject,itemsSchema, root);
			}
		}
		if (schemaObject.has("discrimitaor")) {
			schema.setDiscriminator(schemaObject.get("discriminator").getAsString());
		}
		if (schemaObject.has("readOnly"))
			schema.setReadOnly(schemaObject.get("readOnly").getAsBoolean());
		if (schemaObject.has("xml")) {
			XMLElement xml = openAPIFactory.createXMLElement();
			JsonObject xmlObject = schemaObject.get("xml").getAsJsonObject();
			if (xmlObject.has("name"))
				xml.setName(xmlObject.get("name").getAsString());
			if (xmlObject.has("namespace"))
				xml.setNamespace(xmlObject.get("namespace").getAsString());
			if (xmlObject.has("prefix"))
				xml.setPrefix(xmlObject.get("prefix").getAsString());
			if (xmlObject.has("attribute"))
				xml.setAttribute(xmlObject.get("attribute").getAsBoolean());
			if (xmlObject.has("wrapped"))
				xml.setWrapped(xmlObject.get("wrapped").getAsBoolean());
			schema.setXml(xml);
		}
		if (schemaObject.has("externalDocs")) {
			ExternalDocs externalDocs = openAPIFactory.createExternalDocs();
			schema.setExternalDocs(externalDocs);
			discoverExternalDocs(schemaObject.get("externalDocs"), externalDocs);
		}
		if (schemaObject.has("example"))
			schema.setExample(schemaObject.get("example").toString());
		if (schemaObject.has("required")) {
			for (JsonElement requiredItem : schemaObject.get("required").getAsJsonArray()) {
				schema.getRequired().add(OpenAPIUtils.getPropertyByName(requiredItem.getAsString(), schema));
			
			}
		}
	}

	private  void discoverPaths(JsonElement jsonElement, Root root) {
		JsonObject pathsObject = jsonElement.getAsJsonObject();
		Set<Entry<String, JsonElement>> paths = pathsObject.entrySet();
		for (Entry<String, JsonElement> pathElement : paths) {
			JsonObject pathObject = pathElement.getValue().getAsJsonObject();
			Path path = openAPIFactory.createPath();
			root.getApi().getPaths().add(path);
			path.setPattern(pathElement.getKey());
			if (pathObject.has("get")) {
			Operation getAPIOperation = openAPIFactory.createOperation();
				path.setGet(getAPIOperation);
				discoverOperation(pathObject.get("get"), getAPIOperation, root);
			}
			if (pathObject.has("put")) {
				Operation putAPIOperation = openAPIFactory.createOperation();
				path.setPut(putAPIOperation);
				discoverOperation(pathObject.get("put"), putAPIOperation, root);
			}
			if (pathObject.has("post")) {
				Operation aPIOperation = openAPIFactory.createOperation();
				path.setPost(aPIOperation);
				discoverOperation(pathObject.get("post"), aPIOperation, root);
			}
			if (pathObject.has("delete")) {
				Operation aPIOperation = openAPIFactory.createOperation();
				path.setDelete(aPIOperation);
				discoverOperation(pathObject.get("delete"), aPIOperation, root);
			}
			if (pathObject.has("options")) {
				Operation aPIOperation = openAPIFactory.createOperation();
				path.setOptions(aPIOperation);
				discoverOperation(pathObject.get("options"), aPIOperation, root);
			}
			if (pathObject.has("head")) {
				Operation aPIOperation = openAPIFactory.createOperation();
				path.setHead(aPIOperation);
				discoverOperation(pathObject.get("head"), aPIOperation, root);
			}
			if (pathObject.has("patch")) {
				Operation aPIOperation = openAPIFactory.createOperation();
				path.setPatch(aPIOperation);
				discoverOperation(pathObject.get("patch"), aPIOperation, root);
			}
			if (pathObject.has("parameters")) {
				JsonArray aPIParametersArray = pathObject.get("parameters").getAsJsonArray();
				for (JsonElement aPIParameterElement : aPIParametersArray) {
					Parameter aPIParameter = openAPIFactory.createParameter();
					path.getParameters().add(aPIParameter);
					discoverParameter(aPIParameterElement, aPIParameter, root);
				}
			}

		}

	}

	private  void discoverOperation(JsonElement jsonElement, Operation aPIOperation, Root root) {
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		if (jsonObject.has("tags")) {
			JsonArray tagsArray = jsonObject.get("tags").getAsJsonArray();
			for (JsonElement tagElement : tagsArray)
				aPIOperation.getTagReferences().add(tagElement.getAsString());
		}
		if (jsonObject.has("summary"))
			aPIOperation.setSummary(jsonObject.get("summary").getAsString());

		if (jsonObject.has("description"))
			aPIOperation.setDescription(jsonObject.get("description").getAsString());
		if (jsonObject.has("externalDocs")) {
			ExternalDocs externalDocs = openAPIFactory.createExternalDocs();
			aPIOperation.setExternalDocs(externalDocs);
			discoverExternalDocs(jsonObject.get("externalDocs"), externalDocs);
		}
		if (jsonObject.has("operationId"))
			aPIOperation.setOperationId(jsonObject.get("operationId").getAsString());

		if (jsonObject.has("consumes")) {
			JsonArray mimeArray = jsonObject.get("consumes").getAsJsonArray();
			for (JsonElement mimeType : mimeArray)
				aPIOperation.getConsumes().add(mimeType.getAsString());
		}
		if (jsonObject.has("produces")) {
			JsonArray mimeArray = jsonObject.get("produces").getAsJsonArray();
			for (JsonElement mimeType : mimeArray)
				aPIOperation.getProduces().add(mimeType.getAsString());
		}
		if (jsonObject.has("parameters")) {
			JsonArray aPIParameterArray = jsonObject.get("parameters").getAsJsonArray();
			for (JsonElement aPIParameterElement : aPIParameterArray) {
				if(aPIParameterElement.getAsJsonObject().has("$ref")) {
					OpenAPIUtils.getParameterByRef(aPIParameterElement.getAsJsonObject().get("$ref").getAsString(), root.getApi());
					
				} else {
				Parameter aPIParameter = openAPIFactory.createParameter();
				aPIOperation.getParameters().add(aPIParameter);
				root.getParamters().add(aPIParameter);
				discoverParameter(aPIParameterElement, aPIParameter, root);}
			}

		}
		if (jsonObject.has("responses")) {
			Set<Entry<String, JsonElement>> responses = jsonObject.get("responses").getAsJsonObject().entrySet();
			for (Entry<String, JsonElement> responseElement : responses) {
				Response response = openAPIFactory.createResponse();
				aPIOperation.getResponses().add(response);
				root.getResponses().add(response);
				response.setCode(responseElement.getKey());
				discoverResponse(responseElement.getValue(), response, root);
			}
		}
		if (jsonObject.has("schemes")) {
			JsonArray schemeArray = jsonObject.get("schemes").getAsJsonArray();
			for (JsonElement schemeElement : schemeArray)
				aPIOperation.getSchemes().add(SchemeType.get(schemeElement.getAsString()));
		}
		if (jsonObject.has("deprecated")) {
			aPIOperation.setDeprecated(jsonObject.get("deprecated").getAsBoolean());
		}
		if (jsonObject.has("security")) {
			JsonArray securityArray = jsonObject.get("security").getAsJsonArray();
			for (JsonElement securityElement : securityArray) {
				SecurityRequirement security = openAPIFactory.createSecurityRequirement();
				aPIOperation.getSecurityRequirements().add(security);
				discoverSecurityRequirement(securityElement, security, root.getApi());

			}

		}
	}

	private  void discoverSecurityRequirement(JsonElement securityElement, SecurityRequirement security, API api) {
		Set<Entry<String, JsonElement>> securityAttributes = securityElement.getAsJsonObject().entrySet();
		Entry<String, JsonElement> first = (Entry<String, JsonElement>) securityAttributes.toArray()[0];
		SecuritySchema securitySchema = OpenAPIUtils.getSecuritySchemaByName(api, first.getKey());
		security.setSecuritySchema(securitySchema);
		for (JsonElement value : first.getValue().getAsJsonArray())
			security.getSecurityScopes().add(OpenAPIUtils.getSecurityScopeByName(securitySchema, value.getAsString()));

	}

	private  void discoverResponse(JsonElement responseElement, Response response, Root root) {
		JsonObject responseObject = responseElement.getAsJsonObject();
		if (responseObject.has("description"))
			response.setDescription(responseObject.get("description").getAsString());
		if (responseObject.has("schema")) {
			Schema responseSchema = openAPIFactory.createSchema();
			response.setSchema(responseSchema);
			root.getSchemas().add(responseSchema);
			discoverSchema(responseObject.get("schema").getAsJsonObject(), responseSchema, root);
		}
		if (responseObject.has("headers")) {
			Set<Entry<String, JsonElement>> headers = responseObject.get("headers").getAsJsonObject().entrySet();
			for (Entry<String, JsonElement> headerEntry : headers) {
				Header header = openAPIFactory.createHeader();
				header.setName(headerEntry.getKey());
				discoverHeader(headerEntry.getValue(), header);
			}
		}
		if (responseObject.has("example")) {
			Set<Entry<String, JsonElement>> examples = responseObject.get("example").getAsJsonObject().entrySet();

			for (Entry<String, JsonElement> exampleEntry : examples) {
				Example example = openAPIFactory.createExample();
				example.setMimeType(exampleEntry.getKey());
				example.setValue(exampleEntry.getValue().toString());
				response.getExamples().add(example);
			}

		}

	}

	private  void discoverHeader(JsonElement jsonElement, Header header) {
		JsonObject jsonObject = jsonElement.getAsJsonObject();

		if (jsonObject.has("description"))
			header.setDescription(jsonObject.get("description").getAsString());

		if (jsonObject.has("type"))
			header.setType(core.JSONDataType.get(jsonObject.get("type").getAsString()));
		if (jsonObject.has("format"))
			header.setFormat(jsonObject.get("format").getAsString());

		if (jsonObject.has("items")) {
			ItemsDefinition items = openAPIFactory.createItemsDefinition();
			header.setItems(items);
			discoverItems(jsonObject.get("items"), items);
		}
		if (jsonObject.has("collectionFormat"))
			header.setCollectionFormat(core.CollectionFormat.get(jsonObject.get("collectionFormat").getAsString()));
		if (jsonObject.has("default"))
			header.setDefault(jsonObject.get("default").toString());
		if (jsonObject.has("maximum"))
			header.setMaximum(jsonObject.get("maximum").getAsDouble());
		if (jsonObject.has("exclusiveMaximum"))
			header.setExclusiveMaximum(jsonObject.get("exclusiveMaximum").getAsBoolean());//to fix 
		if (jsonObject.has("minimum"))
			header.setMinimum(jsonObject.get("minimum").getAsDouble());
		if (jsonObject.has("exclusiveMinimim"))
			header.setExclusiveMinimum(jsonObject.get("exclusiveMinimum").getAsBoolean());
		if (jsonObject.has("maxLength"))
			header.setMaxLength(jsonObject.get("maxLength").getAsInt());
		if (jsonObject.has("minLength"))
			header.setMinLength(jsonObject.get("minLength").getAsInt());
		if (jsonObject.has("pattern"))
			header.setPattern(jsonObject.get("pattern").getAsString());
		if (jsonObject.has("maxItems"))
			header.setMaxItems(jsonObject.get("maxItems").getAsInt());
		if (jsonObject.has("minItems"))
			header.setMinItems(jsonObject.get("minItems").getAsInt());
		if (jsonObject.has("uniqueItems"))
			header.setUniqueItems(jsonObject.get("uniqueItems").getAsBoolean());
		if (jsonObject.has("enum")) {
			JsonArray enumItems = jsonObject.get("enum").getAsJsonArray();
			for (JsonElement item : enumItems)
				header.getEnum().add(item.getAsString());
		}
		if (jsonObject.has("multipleOf"))
			header.setMultipleOf(jsonObject.get("multipleOf").getAsInt());

	}

	private  void discoverParameter(JsonElement aPIParameterElement,Parameter aPIParameter, Root root) {
		JsonObject jsonObject = aPIParameterElement.getAsJsonObject();
		if (jsonObject.has("name"))
			aPIParameter.setName(jsonObject.get("name").getAsString());

		if (jsonObject.has("in")){
			aPIParameter.setLocation(ParameterLocation.get(jsonObject.get("in").getAsString()));
		}
		if (jsonObject.has("description"))
			aPIParameter.setDescription(jsonObject.get("description").getAsString());
		if (jsonObject.has("required"))
			aPIParameter.setRequired(jsonObject.get("required").getAsBoolean());
		if (jsonObject.has("schema")) {
			Schema schema = openAPIFactory.createSchema();
			aPIParameter.setSchema(schema);
			root.getSchemas().add(schema);
			discoverSchema(jsonObject.get("schema").getAsJsonObject(), schema, root);
		}
		if (jsonObject.has("type"))
			aPIParameter.setType(core.JSONDataType.get(jsonObject.get("type").getAsString()));
		if (jsonObject.has("format"))
			aPIParameter.setFormat(jsonObject.get("format").getAsString());
		if (jsonObject.has("allowEmptyValue")) 
			aPIParameter.setAllowEmplyValue(jsonObject.get("allowEmptyValue").getAsBoolean());
			if (jsonObject.has("items")) {
				ItemsDefinition items = openAPIFactory.createItemsDefinition();
				aPIParameter.setItems(items);
				discoverItems(jsonObject.get("items"), items);
			}
			if (jsonObject.has("collectionFormat"))
				aPIParameter.setCollectionFormat(core.CollectionFormat.get(jsonObject.get("collectionFormat").getAsString()));
			if (jsonObject.has("default"))
				aPIParameter.setDefault(jsonObject.get("default").toString());
			if (jsonObject.has("maximum"))
				aPIParameter.setMaximum(jsonObject.get("maximum").getAsDouble());
			if (jsonObject.has("exclusiveMaximum"))
				aPIParameter.setExclusiveMaximum(jsonObject.get("exclusiveMaximum").getAsBoolean());
			if (jsonObject.has("minimum"))
				aPIParameter.setMinimum(jsonObject.get("minimum").getAsDouble());
			if (jsonObject.has("exclusiveMinimim"))
				aPIParameter.setExclusiveMinimum(jsonObject.get("exclusiveMinimum").getAsBoolean());
			if (jsonObject.has("maxLength"))
				aPIParameter.setMaxLength(jsonObject.get("maxLength").getAsInt());
			if (jsonObject.has("minLength"))
				aPIParameter.setMinLength(jsonObject.get("minLength").getAsInt());
			if (jsonObject.has("pattern"))
				aPIParameter.setPattern(jsonObject.get("pattern").getAsString());
			if (jsonObject.has("maxItems"))
				aPIParameter.setMaxItems(jsonObject.get("maxItems").getAsInt());
			if (jsonObject.has("minItems"))
				aPIParameter.setMinItems(jsonObject.get("minItems").getAsInt());
			if (jsonObject.has("uniqueItems"))
				aPIParameter.setUniqueItems(jsonObject.get("uniqueItems").getAsBoolean());
			if (jsonObject.has("enum")) {
				JsonArray enumItems = jsonObject.get("enum").getAsJsonArray();
				for (JsonElement item : enumItems)
					aPIParameter.getEnum().add(item.getAsString());
			}
			if (jsonObject.has("multipleOf"))
				aPIParameter.setMultipleOf(jsonObject.get("multipleOf").getAsInt());

		
	}

	private  void discoverItems(JsonElement jsonElement, ItemsDefinition items) {
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		if (jsonObject.has("type"))
			items.setType(core.JSONDataType.get(jsonObject.get("type").getAsString()));
		if (jsonObject.has("format"))
			items.setFormat(jsonObject.get("format").getAsString());
		if (jsonObject.has("items")) {
			ItemsDefinition innerItmes = openAPIFactory.createItemsDefinition();
			items.setItems(innerItmes);
			discoverItems(jsonObject.get("items"), innerItmes);
		}
		if (jsonObject.has("collectionFormat"))
			items.setCollectionFormat(core.CollectionFormat.get(jsonObject.get("collectionFormat").getAsString()));
		if (jsonObject.has("default"))
			items.setDefault(jsonObject.get("default").toString());
		if (jsonObject.has("maximum"))
			items.setMaximum(jsonObject.get("maximum").getAsDouble());
		if (jsonObject.has("exclusiveMaximum"))
			items.setExclusiveMaximum(jsonObject.get("exclusiveMaximum").getAsBoolean());
		if (jsonObject.has("minimum"))
			items.setMinimum(jsonObject.get("minimum").getAsDouble());
		if (jsonObject.has("exclusiveMinimim"))
			items.setExclusiveMinimum(jsonObject.get("exclusiveMinimum").getAsBoolean());
		if (jsonObject.has("maxLength"))
			items.setMaxLength(jsonObject.get("maxLength").getAsInt());
		if (jsonObject.has("minLength"))
			items.setMinLength(jsonObject.get("minLength").getAsInt());
		if (jsonObject.has("pattern"))
			items.setPattern(jsonObject.get("pattern").getAsString());
		if (jsonObject.has("maxItems"))
			items.setMaxItems(jsonObject.get("maxItems").getAsInt());
		if (jsonObject.has("minItems"))
			items.setMinItems(jsonObject.get("minItems").getAsInt());
		if (jsonObject.has("uniqueItems"))
			items.setUniqueItems(jsonObject.get("uniqueItems").getAsBoolean());
		if (jsonObject.has("enum")) {
			JsonArray enumItems = jsonObject.get("enum").getAsJsonArray();
			for (JsonElement item : enumItems)
				items.getEnum().add(item.getAsString());
		}
		if (jsonObject.has("multipleOf"))
			items.setMultipleOf(jsonObject.get("multipleOf").getAsInt());
	}

	private  void discoverExternalDocs(JsonElement jsonElement, ExternalDocs externalDocs) {
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		if (jsonObject.has("description"))
			externalDocs.setDescription(jsonObject.get("description").getAsString());

		if (jsonObject.has("url"))
			externalDocs.setUrl(jsonObject.get("url").getAsString());

	}

	private  void discoverInfo(JsonElement jsonElement, Info info) {
		JsonObject infoObject = jsonElement.getAsJsonObject();
		if (infoObject.has("title")) {
			info.setTitle(infoObject.get("title").getAsString());
		}
		if (infoObject.has("description"))
			info.setDescription(infoObject.get("description").getAsString());
		if (infoObject.has("termsOfServices"))
			info.setTermsOfServices(infoObject.get("termsOfService").getAsString());
		if (infoObject.has("contact"))
			discoverContact(infoObject.get("contact"), info);
		if (infoObject.has("license"))
			discoverLicense(infoObject.get("license"), info);
		if (infoObject.has("version"))
			info.setVersion(infoObject.get("version").getAsString());

	}

	private  void discoverLicense(JsonElement jsonElement, Info info) {
		JsonObject licenseObject = jsonElement.getAsJsonObject();
		License license = openAPIFactory.createLicense();
		info.setLicense(license);
		if (licenseObject.has("name"))
			license.setName(licenseObject.get("name").getAsString());
		if (licenseObject.has("url"))
			license.setUrl(licenseObject.get("url").getAsString());
	}

	private  void discoverContact(JsonElement jsonElement, Info info) {
		JsonObject contactObject = jsonElement.getAsJsonObject();
		Contact contact = openAPIFactory.createContact();
		info.setContact(contact);
		if (contactObject.has("name"))
			contact.setName(contactObject.get("name").getAsString());
		if (contactObject.has("url"))
			contact.setUrl(contactObject.get("url").getAsString());
		if (contactObject.has("email"))
			contact.setEmail(contactObject.get("email").getAsString());

	}
	public Root createOpenAPIModelFromYaml() {
		return null;
		
	}

}
