package edu.uoc.som.wapiml.generators;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.util.UMLUtil;

import edu.som.uoc.openapi2.profile.CollectionFormat;
import edu.som.uoc.openapi2.profile.HTTPMethod;
import edu.som.uoc.openapi2.profile.Header;
import edu.som.uoc.openapi2.profile.JSONDataType;
import edu.som.uoc.openapi2.profile.SchemeType;
import edu.som.uoc.openapi2.profile.SecurityScope;
import edu.som.uoc.openapi2.profile.Tag;
import edu.som.uoc.openapi2.profile.XMLElement;
import edu.uoc.som.openapi2.API;
import edu.uoc.som.openapi2.ExternalDocs;
import edu.uoc.som.openapi2.Info;
import edu.uoc.som.openapi2.ItemsDefinition;
import edu.uoc.som.openapi2.OpenAPIFactory;
import edu.uoc.som.openapi2.Path;
import edu.uoc.som.openapi2.Property;
import edu.uoc.som.openapi2.Response;
import edu.uoc.som.openapi2.ResponseDefinition;
import edu.uoc.som.openapi2.Root;
import edu.uoc.som.openapi2.Schema;
import edu.uoc.som.openapi2.SecurityRequirement;
import edu.uoc.som.openapi2.SecurityScheme;
import edu.uoc.som.wapiml.utils.OpenAPIProfileUtils;
import edu.uoc.som.wapiml.utils.OpenAPIUtils;

public class OpenAPIModelGenerator {

	private OpenAPIFactory factory = OpenAPIFactory.eINSTANCE;
	private ResourceSet resourceSet;
	private Resource resource;
	private Map<Class, Schema> classMap = new HashMap<Class, Schema>();
	private Model umlModel;
	private Root OpenAPIModelRoot;

	public OpenAPIModelGenerator(File modelFile) {
		resourceSet = initUMLResourceSet();
		resource = resourceSet.getResource(URI.createFileURI(modelFile.getPath()), true);
		umlModel = (Model) resource.getContents().get(0);


	}

	private ResourceSet initUMLResourceSet() {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
				UMLResource.Factory.INSTANCE);
		resourceSet.getURIConverter().getURIMap().put(URI.createURI("pathmap://OPENAPI_PROFILES/openapi.profile.uml"),
				URI.createPlatformPluginURI("edu.uoc.som.openapi2.profile/resources/openapi.profile.uml", true));
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
		return resourceSet;
	}

	public Root generate() {
	

		OpenAPIModelRoot = factory.createRoot();
		API api = extractAPI(umlModel);
		OpenAPIModelRoot.setApi(api);

		return OpenAPIModelRoot;
	}

	private API extractAPI(Model model) {
		Stereotype apiStereotype = model.getApplicableStereotype(OpenAPIProfileUtils.API_QN);
		if (!model.isStereotypeApplied(apiStereotype)) {
			return null;
		}
		API api = factory.createAPI();
		api.setHost((String) UMLUtil.getTaggedValue(model, OpenAPIProfileUtils.API_QN, "host"));
		api.setBasePath((String) UMLUtil.getTaggedValue(model, OpenAPIProfileUtils.API_QN, "basePath"));
		api.setSwagger("2.0");
		List<SchemeType> pSchemes = (List<SchemeType>) UMLUtil.getTaggedValue(model, OpenAPIProfileUtils.API_QN,
				"schemes");
		if (pSchemes != null && !pSchemes.isEmpty()) {
			for (SchemeType from : pSchemes)
				api.getSchemes().add(OpenAPIProfileUtils.transformSchemeType(from));
		}
		List<String> consumes = (List<String>) UMLUtil.getTaggedValue(model, OpenAPIProfileUtils.API_QN, "consumes");
		if (consumes != null && !consumes.isEmpty())
			api.getConsumes().addAll(consumes);
		List<String> produces = (List<String>) UMLUtil.getTaggedValue(model, OpenAPIProfileUtils.API_QN, "produces");
		if (produces != null && !produces.isEmpty())
			api.getProduces().addAll(produces);
		api.setInfo(extractInfo(model));
		api.setExternalDocs(extractExternalDocs(model));
		if (model.isStereotypeApplied(model.getApplicableStereotype(OpenAPIProfileUtils.TAGS_QN))) {
			List<Tag> pTags = (List<Tag>) UMLUtil.getTaggedValue(model, OpenAPIProfileUtils.TAGS_QN, "tags");
			for (Tag pTag : pTags) {
				edu.uoc.som.openapi2.Tag mTag = factory.createTag();
				mTag.setDescription(pTag.getDescription());
				if (pTag.getExternalDocsDescription() != null || pTag.getExternalDocsURL() != null) {
					ExternalDocs externalDocs = factory.createExternalDocs();
					externalDocs.setDescription(pTag.getExternalDocsDescription());
					externalDocs.setUrl(pTag.getExternalDocsURL());
					mTag.setExternalDocs(externalDocs);
				}
				mTag.setName(pTag.getName());
				api.getTags().add(mTag);
			}

		}
		if (model.isStereotypeApplied(model.getApplicableStereotype(OpenAPIProfileUtils.SECURITY_DEFINITIONS_QN))) {
			List<edu.som.uoc.openapi2.profile.SecurityScheme> pSecuritySchemes = (List<edu.som.uoc.openapi2.profile.SecurityScheme>) UMLUtil
					.getTaggedValue(model, OpenAPIProfileUtils.SECURITY_DEFINITIONS_QN, "securitySchemes");
			if (pSecuritySchemes != null && !pSecuritySchemes.isEmpty()) {
				for (edu.som.uoc.openapi2.profile.SecurityScheme pSecurityScheme : pSecuritySchemes) {
					SecurityScheme mSecurityScheme = extractSecurityScheme(pSecurityScheme);
					if (mSecurityScheme != null) {
						api.getSecurityDefinitions().add(mSecurityScheme);
					}
				}
			}
		}
		if (model.isStereotypeApplied(model.getApplicableStereotype(OpenAPIProfileUtils.SECURITY_QN))) {
			List<edu.som.uoc.openapi2.profile.SecurityRequirement> pSecurityRequirements = (List<edu.som.uoc.openapi2.profile.SecurityRequirement>) UMLUtil
					.getTaggedValue(model, OpenAPIProfileUtils.SECURITY_QN, "securityRequirements");
			if (pSecurityRequirements != null && !pSecurityRequirements.isEmpty()) {
				for (edu.som.uoc.openapi2.profile.SecurityRequirement pSecurityRequirement : pSecurityRequirements) {
					SecurityRequirement mSecurityRequirement = extractSecurity(pSecurityRequirement, api);
					if (mSecurityRequirement != null) {
						api.getSecurity().add(mSecurityRequirement);
					}
				}
			}
		}

		List<org.eclipse.uml2.uml.Package> packages = new ArrayList<org.eclipse.uml2.uml.Package>();
		for (Iterator<EObject> it = model.eAllContents(); it.hasNext();) {
			EObject eObject = it.next();
			if (eObject instanceof org.eclipse.uml2.uml.Package) {
				packages.add((org.eclipse.uml2.uml.Package) eObject);
			}
		}
		// extract only the classes
		for (org.eclipse.uml2.uml.Package pkg : packages) {

			for (Iterator<EObject> it = pkg.eAllContents(); it.hasNext();) {
				EObject child = it.next();
				if (child instanceof Class) {
					if (((Class) child).isStereotypeApplied(
							((Class) child).getApplicableStereotype(OpenAPIProfileUtils.SCHEMA_QN))) {
						Schema definition = extractSchema((Class) child, null);
						if (definition != null) {
							definition.setDeclaringContext(api);
							api.getDefinitions().add(definition);
							classMap.put((Class) child, definition);
						}

					}
				}
			}
		}
		// extract the attributes
		for (org.eclipse.uml2.uml.Package pkg : packages) {
			for (Iterator<EObject> it = pkg.eAllContents(); it.hasNext();) {
				EObject child = it.next();
				if (child instanceof Class) {
					if (((Class) child).isStereotypeApplied(
							((Class) child).getApplicableStereotype(OpenAPIProfileUtils.SCHEMA_QN))) {
						Class clazz = (Class) child;
						Schema schema = classMap.get(clazz);
						if (!clazz.getGeneralizations().isEmpty()) {
							for (Generalization generalization : clazz.getGeneralizations()) {
								if (generalization.getGeneral() != null
										&& generalization.getGeneral() instanceof Class) {
									Class parentClass = (Class) generalization.getGeneral();
									Schema parentSchema = classMap.get(parentClass);
									if (parentSchema != null) {
										schema.getAllOf().add(parentSchema);
									}
								}
							}
							
								Schema allOfItem = factory.createSchema();
								allOfItem.setDeclaringContext(schema);
								schema.getAllOf().add(allOfItem);
								generateAttributes(clazz, allOfItem);
							
						} else {
							generateAttributes(clazz, schema);
						}
					}

				}
			}
		}

		// extract associations
		for (org.eclipse.uml2.uml.Package pkg : packages) {
			for (Iterator<EObject> it = pkg.eAllContents(); it.hasNext();) {
				EObject child = it.next();
				if (child instanceof Association) {
					List<org.eclipse.uml2.uml.Property> endMembers = ((Association) child).getOwnedEnds();
					if (endMembers.size() == 2) {
						org.eclipse.uml2.uml.Property firstEnd = endMembers.get(0);
						org.eclipse.uml2.uml.Property secondEnd = endMembers.get(1);
						if (secondEnd.getAggregation().equals(AggregationKind.COMPOSITE_LITERAL)) {
							Property mProperty = extractProperty(secondEnd);
							if (firstEnd.getType() instanceof Class) {
								Schema schema = classMap.get(firstEnd.getType());
								if(schema.getAllOf().isEmpty())
									schema.getProperties().add(mProperty);
								else 
									schema.getAllOf().get(schema.getAllOf().size()-1).getProperties().add(mProperty);

							}

						}
					}
				}
			}
		}

		// extact paths
		for (Map.Entry<Class, Schema> entry : classMap.entrySet()) {
			Class clazz = entry.getKey();
			for (Operation operation : clazz.getAllOperations()) {
				if (operation
						.isStereotypeApplied(operation.getApplicableStereotype(OpenAPIProfileUtils.API_OPERATION_QN))) {
					String path = (String) UMLUtil.getTaggedValue(operation, OpenAPIProfileUtils.API_OPERATION_QN,
							"relativePath");
					Path mRelativePath = api.getPathByRelativePath(path);
					if (mRelativePath == null) {
						mRelativePath = factory.createPath();
						mRelativePath.setRelativePath(path);
					}
					api.getPaths().add(mRelativePath);
					HTTPMethod httpMethod = (HTTPMethod) UMLUtil.getTaggedValue(operation,
							OpenAPIProfileUtils.API_OPERATION_QN, "method");
					edu.uoc.som.openapi2.Operation mOperation = extractOperation(operation, api);
					switch (httpMethod) {
					case DELETE:
						mRelativePath.setDelete(mOperation);
						break;
					case GET:
						mRelativePath.setGet(mOperation);
						break;
					case HEAD:
						mRelativePath.setHead(mOperation);
						break;
					case OPTIONS:
						mRelativePath.setOptions(mOperation);
						break;
					case PATCH:
						mRelativePath.setPatch(mOperation);
						break;
					case POST:
						mRelativePath.setPost(mOperation);
						break;
					case PUT:
						mRelativePath.setPut(mOperation);
						break;
					default:
						continue;
					}
				}
			}
		}

		return api;
	}

	private void generateAttributes(Class clazz, Schema schema) {
		for (org.eclipse.uml2.uml.Property attribute : clazz.getAllAttributes()) {
			if (attribute.isStereotypeApplied(attribute.getApplicableStereotype(OpenAPIProfileUtils.API_PROPERTY_QN))) {
				Property mProperty = extractProperty(attribute);
				schema.getProperties().add(mProperty);

			}
		}
	}

	private edu.uoc.som.openapi2.Operation extractOperation(Operation operation, API api) {
		edu.uoc.som.openapi2.Operation mOperation = factory.createOperation();
		mOperation.setOperationId(operation.getName());
		mOperation.setDeprecated(
				(Boolean) UMLUtil.getTaggedValue(operation, OpenAPIProfileUtils.API_OPERATION_QN, "deprecated"));
		mOperation.setDescription(
				(String) UMLUtil.getTaggedValue(operation, OpenAPIProfileUtils.API_OPERATION_QN, "description"));
		mOperation.setSummary(
				(String) UMLUtil.getTaggedValue(operation, OpenAPIProfileUtils.API_OPERATION_QN, "summary"));

		List<SchemeType> pSchemes = (List<SchemeType>) UMLUtil.getTaggedValue(operation,
				OpenAPIProfileUtils.API_OPERATION_QN, "schemes");
		if (pSchemes != null && !pSchemes.isEmpty()) {
			for (SchemeType from : pSchemes)
				mOperation.getSchemes().add(OpenAPIProfileUtils.transformSchemeType(from));
		}
		List<String> consumes = (List<String>) UMLUtil.getTaggedValue(operation, OpenAPIProfileUtils.API_OPERATION_QN,
				"consumes");
		if (consumes != null && !consumes.isEmpty())
			mOperation.getConsumes().addAll(consumes);
		List<String> produces = (List<String>) UMLUtil.getTaggedValue(operation, OpenAPIProfileUtils.API_OPERATION_QN,
				"produces");
		if (produces != null && !produces.isEmpty())
			mOperation.getProduces().addAll(produces);
		if (operation.isStereotypeApplied(operation.getApplicableStereotype(OpenAPIProfileUtils.EXTERNAL_DOCS_QN))) {
			ExternalDocs mExternalDocs = extractExternalDocs(operation);
			mOperation.setExternalDocs(mExternalDocs);
		}
		if (operation.isStereotypeApplied(operation.getApplicableStereotype(OpenAPIProfileUtils.SECURITY_QN))) {
			List<edu.som.uoc.openapi2.profile.SecurityRequirement> pSecurityRequirements = (List<edu.som.uoc.openapi2.profile.SecurityRequirement>) UMLUtil
					.getTaggedValue(operation, OpenAPIProfileUtils.SECURITY_QN, "securityRequirements");
			if (pSecurityRequirements != null && !pSecurityRequirements.isEmpty()) {
				for (edu.som.uoc.openapi2.profile.SecurityRequirement pSecurityRequirement : pSecurityRequirements) {
					SecurityRequirement mSecurityRequirement = extractSecurity(pSecurityRequirement, api);
					if (mSecurityRequirement != null) {
						mOperation.getSecurity().add(mSecurityRequirement);
					}
				}
			}
		}
		for (Parameter parameter : operation.getOwnedParameters()) {
			if (parameter.getDirection().equals(ParameterDirectionKind.IN_LITERAL)) {
				edu.uoc.som.openapi2.Parameter mParameter = extractPrameter(parameter);
				mOperation.getParameters().add(mParameter);
			}
			if (parameter.getDirection().equals(ParameterDirectionKind.RETURN_LITERAL)) {
				Response mResponse = extractResponse(parameter);
				mOperation.getResponses().add(mResponse);
			}
		}

		return mOperation;
	}

	private edu.uoc.som.openapi2.Parameter extractPrameter(Parameter parameter) {
		edu.uoc.som.openapi2.Parameter mParameter = factory.createParameter();
		mParameter.setName(parameter.getName());
		mParameter.setAllowEmplyValue(
				(Boolean) UMLUtil.getTaggedValue(parameter, OpenAPIProfileUtils.API_PARAMETER_QN, "allowEmptyValue"));
		mParameter.setCollectionFormat(OpenAPIProfileUtils.transformCollectionFormat((CollectionFormat) UMLUtil
				.getTaggedValue(parameter, OpenAPIProfileUtils.API_PARAMETER_QN, "collectionFormat")));
		mParameter.setDescription(
				(String) UMLUtil.getTaggedValue(parameter, OpenAPIProfileUtils.API_PARAMETER_QN, "description"));
		mParameter.setLocation(
				OpenAPIProfileUtils.transformParameterLocation((edu.som.uoc.openapi2.profile.ParameterLocation) UMLUtil
						.getTaggedValue(parameter, OpenAPIProfileUtils.API_PARAMETER_QN, "location")));
		mParameter.setRequired(
				(Boolean) UMLUtil.getTaggedValue(parameter, OpenAPIProfileUtils.API_PARAMETER_QN, "required"));

		Type type = parameter.getType();
		Schema schema = extractDataType(type);
		if (type instanceof PrimitiveType != type instanceof Enumeration) {
			{
				if (parameter.getUpper() == -1 || parameter.getUpper() > 1) {
					ItemsDefinition items = factory.createItemsDefinition();
					items.setType(schema.getType());
					items.setFormat(schema.getFormat());
					if (type instanceof Enumeration)
						items.getEnum().addAll(schema.getEnum());
					mParameter.setItems(items);
					mParameter.setType(edu.uoc.som.openapi2.JSONDataType.ARRAY);
				} else {
					mParameter.setType(schema.getType());
					mParameter.setFormat(schema.getFormat());
					if (type instanceof Enumeration)
						mParameter.getEnum().addAll(schema.getEnum());
				}
			}
		}
		if (type instanceof Class) {
			if (parameter.getUpper() == -1 || parameter.getUpper() > 1) {
				Schema arraySchema = factory.createSchema();
				arraySchema.setType(edu.uoc.som.openapi2.JSONDataType.ARRAY);
				arraySchema.setItems(schema);
				schema = arraySchema;
			}
			mParameter.setSchema(schema);
		}

		OpenAPIProfileUtils.extractJSONSchemaSubsetproperties(parameter, OpenAPIProfileUtils.API_PARAMETER_QN, schema);
		schema.setDefault(mParameter.getDefault());
		return mParameter;
	}

	private Response extractResponse(Parameter parameter) {
		Response mResponse = factory.createResponse();
		ResponseDefinition responseDefinition = factory.createResponseDefinition();
		mResponse.setResponseDefinition(responseDefinition);
		responseDefinition.setDeclaringContext(mResponse);
		Integer code = (Integer) UMLUtil.getTaggedValue(parameter, OpenAPIProfileUtils.API_RESPONSE_QN, "code");
		Boolean defaultFlag = (Boolean) UMLUtil.getTaggedValue(parameter, OpenAPIProfileUtils.API_RESPONSE_QN,
				"default");
		mResponse.setDefault(defaultFlag);
		mResponse.setCode(code);
		responseDefinition.setDescription(
				(String) UMLUtil.getTaggedValue(parameter, OpenAPIProfileUtils.API_RESPONSE_QN, "description"));
		Type type = parameter.getType();
		Schema schema = extractDataType(type);

		if (parameter.getUpper() == -1 || parameter.getUpper() > 1) {
			Schema arraySchema = factory.createSchema();
			arraySchema.setDeclaringContext(responseDefinition);
			arraySchema.setType(edu.uoc.som.openapi2.JSONDataType.ARRAY);
			arraySchema.setItems(schema);
			schema = arraySchema;
		}
		responseDefinition.setSchema(schema);
		List<Header> pHeaders = (List<Header>) UMLUtil.getTaggedValue(parameter, OpenAPIProfileUtils.API_RESPONSE_QN,
				"headers");
		if (pHeaders != null && !pHeaders.isEmpty()) {
			for (Header pHeader : pHeaders) {
				edu.uoc.som.openapi2.Header mHeader = factory.createHeader();
				mHeader.setCollectionFormat(
						OpenAPIProfileUtils.transformCollectionFormat(pHeader.getCollectionFormat()));
				mHeader.setDefault(pHeader.getDefault());
				mHeader.setDescription(pHeader.getDescription());
				mHeader.setExclusiveMaximum(pHeader.getExclusiveMaximum());
				mHeader.setFormat(pHeader.getFormat());
				mHeader.setMaximum(pHeader.getMaximum());
				mHeader.setMaxItems(pHeader.getMaxItems());
				mHeader.setMaxLength(pHeader.getMaxLength());
				mHeader.setMinimum(pHeader.getMinimum());
				mHeader.setMinItems(pHeader.getMinItems());
				mHeader.setMinLength(pHeader.getMinLength());
				mHeader.setMultipleOf(pHeader.getMultipleOf());
				mHeader.setName(pHeader.getName());
				mHeader.setPattern(pHeader.getPattern());
				mHeader.setType(OpenAPIProfileUtils.transformJSONDataType(pHeader.getType()));
				mHeader.setUniqueItems(pHeader.getUniqueItems());
				responseDefinition.getHeaders().add(mHeader);
			}
		}

		return mResponse;
	}

	private Property extractProperty(org.eclipse.uml2.uml.Property property) {
		Property mProperty = factory.createProperty();
		mProperty.setReferenceName(property.getName());
		mProperty.setRequired(
				(Boolean) UMLUtil.getTaggedValue(property, OpenAPIProfileUtils.API_PROPERTY_QN, "required"));
		Schema mSchema = extractDataType(property.getType());
		mSchema = extractSchema(property, mSchema);
		if (property.getUpper() == -1) {
			Schema arraySchema = factory.createSchema();
			arraySchema.setDeclaringContext(mProperty);
			arraySchema.setType(edu.uoc.som.openapi2.JSONDataType.ARRAY);
			arraySchema.setItems(mSchema);
			mSchema = arraySchema;
		}

		if (property.getUpper() != -1 && property.getUpper() != 0 && property.getUpper() != 1)
			mSchema.setMaxItems(property.getUpper());
		if (property.getLower() != 0)
			mSchema.setMinItems(property.getLower());
		mSchema.setDefault(property.getDefault());
		mProperty.setSchema(mSchema);
		return mProperty;

	}

	private Schema extractDataType(Type dataType) {
		if (dataType instanceof Class) {
			return classMap.get(dataType);
		}
		if (dataType instanceof PrimitiveType || dataType instanceof Enumeration) {
			Schema mDataType = factory.createSchema();
			mDataType.setFormat(
					(String) UMLUtil.getTaggedValue(dataType, OpenAPIProfileUtils.API_DATA_TYPE_QN, "format"));
			JSONDataType type = (JSONDataType) UMLUtil.getTaggedValue(dataType, OpenAPIProfileUtils.API_DATA_TYPE_QN,
					"type");
			if (type != null)
				mDataType.setType(OpenAPIProfileUtils.transformJSONDataType(type));
			if (dataType instanceof Enumeration) {
				Enumeration enumDataType = (Enumeration) dataType;
				List<String> enumList = new ArrayList<String>();
				for (EnumerationLiteral literal : enumDataType.getOwnedLiterals()) {
					enumList.add(literal.getName());
				}
				mDataType.getEnum().addAll(enumList);
			}
			return mDataType;
		}
		return null;

	}

	private Schema extractSchema(NamedElement element, Schema schema) {

		if (schema == null)
			schema = factory.createSchema();
		if (element instanceof Class) {
			schema.setReferenceName(element.getName());
			if(((Class) element).getGeneralizations().isEmpty())
				schema.setType(edu.uoc.som.openapi2.JSONDataType.OBJECT);
			classMap.put((Class) element, schema);
		}
		if (element instanceof Class) {
			schema.setTitle((String) UMLUtil.getTaggedValue(element, OpenAPIProfileUtils.SCHEMA_QN, "title"));
			schema.setMaxProperties(
					(Integer) UMLUtil.getTaggedValue(element, OpenAPIProfileUtils.SCHEMA_QN, "maxProperties"));
			schema.setMinProperties(
					(Integer) UMLUtil.getTaggedValue(element, OpenAPIProfileUtils.SCHEMA_QN, "minProperties"));
			schema.setDiscriminator(
					(String) UMLUtil.getTaggedValue(element, OpenAPIProfileUtils.SCHEMA_QN, "discriminator"));
			schema.setAdditonalPropertiesAllowed((Boolean) UMLUtil.getTaggedValue(element,
					OpenAPIProfileUtils.SCHEMA_QN, "additionalPropertiesAllowed"));

			schema.setExample((String) UMLUtil.getTaggedValue(element, OpenAPIProfileUtils.SCHEMA_QN, "example"));
			schema.setDefault((String) UMLUtil.getTaggedValue(element, OpenAPIProfileUtils.SCHEMA_QN, "default"));
			schema.setDescription(
					(String) UMLUtil.getTaggedValue(element, OpenAPIProfileUtils.SCHEMA_QN, "description"));
			Type additionalProperties = (Type) UMLUtil.getTaggedValue(element, OpenAPIProfileUtils.SCHEMA_QN,
					"additionalProperties");
			if (additionalProperties != null) {
				if (additionalProperties instanceof Class) {
					Schema additionalPropertiesSchema = classMap.get(additionalProperties);
					if (additionalPropertiesSchema != null)
						schema.setAdditonalProperties(additionalPropertiesSchema);
				}
				if (additionalProperties instanceof DataType) {
					Schema additionalPropertiesSchema = extractDataType(additionalProperties);
					schema.setAdditonalProperties(additionalPropertiesSchema);
				}
			}
		}
		if (element instanceof org.eclipse.uml2.uml.Property) {
			XMLElement pXMLElement = (XMLElement) UMLUtil.getTaggedValue(element, OpenAPIProfileUtils.API_PROPERTY_QN,
					"xml");
			if (pXMLElement != null) {
				edu.uoc.som.openapi2.XMLElement mXMLElement = factory.createXMLElement();
				mXMLElement.setAttribute(pXMLElement.getAttribute());
				mXMLElement.setName(pXMLElement.getName());
				mXMLElement.setNamespace(pXMLElement.getNamespace());
				mXMLElement.setPrefix(pXMLElement.getPrefix());
				mXMLElement.setWrapped(pXMLElement.getWrapped());
				schema.setXml(mXMLElement);

			}
			schema.setExample((String) UMLUtil.getTaggedValue(element, OpenAPIProfileUtils.API_PROPERTY_QN, "example"));
			schema.setDescription(
					(String) UMLUtil.getTaggedValue(element, OpenAPIProfileUtils.API_PROPERTY_QN, "description"));
			schema.setTitle((String) UMLUtil.getTaggedValue(element, OpenAPIProfileUtils.API_PROPERTY_QN, "title"));

			OpenAPIProfileUtils.extractJSONSchemaSubsetproperties(element, OpenAPIProfileUtils.API_PROPERTY_QN, schema);
		}

		return schema;

	}

	public Info extractInfo(Model model) {
		if (model.isStereotypeApplied(model.getApplicableStereotype(OpenAPIProfileUtils.API_INFO_QN))) {
			Info info = factory.createInfo();
			edu.som.uoc.openapi2.profile.Contact pContact = (edu.som.uoc.openapi2.profile.Contact) UMLUtil
					.getTaggedValue(model, OpenAPIProfileUtils.API_INFO_QN, "contact");
			if (pContact != null) {
				edu.uoc.som.openapi2.Contact mContact = factory.createContact();
				mContact.setEmail(pContact.getEmail());
				mContact.setName(pContact.getName());
				mContact.setUrl(pContact.getUrl());
				info.setContact(mContact);
			}
			info.setDescription((String) UMLUtil.getTaggedValue(model, OpenAPIProfileUtils.API_INFO_QN, "description"));
			info.setTermsOfService(
					(String) UMLUtil.getTaggedValue(model, OpenAPIProfileUtils.API_INFO_QN, "termsOfService"));
			info.setTitle((String) UMLUtil.getTaggedValue(model, OpenAPIProfileUtils.API_INFO_QN, "title"));
			info.setVersion((String) UMLUtil.getTaggedValue(model, OpenAPIProfileUtils.API_INFO_QN, "version"));
			edu.som.uoc.openapi2.profile.License pLicense = (edu.som.uoc.openapi2.profile.License) UMLUtil
					.getTaggedValue(model, OpenAPIProfileUtils.API_INFO_QN, "license");
			if (pLicense != null) {
				edu.uoc.som.openapi2.License mLicense = factory.createLicense();
				mLicense.setName(pLicense.getName());
				mLicense.setUrl(pLicense.getUrl());
				info.setLicense(mLicense);
			}
			return info;
		}
		return null;
	}

	private SecurityScheme extractSecurityScheme(edu.som.uoc.openapi2.profile.SecurityScheme pSecurityScheme) {

		SecurityScheme mSecurityScheme = factory.createSecurityScheme();
		mSecurityScheme.setReferenceName(pSecurityScheme.getReferenceName());
		mSecurityScheme.setName(pSecurityScheme.getName());
		if (pSecurityScheme.getType() != null) {
			mSecurityScheme.setType(OpenAPIProfileUtils.transformSecuritySchemeType(pSecurityScheme.getType()));
		}
		mSecurityScheme.setDescription(pSecurityScheme.getDescription());
		if (pSecurityScheme.getLocation() != null)
			mSecurityScheme.setLocation(OpenAPIProfileUtils.transformAPIKeyLocation(pSecurityScheme.getLocation()));
		if (pSecurityScheme.getFlow() != null)
			mSecurityScheme.setFlow(OpenAPIProfileUtils.transformOAuth2FlowType(pSecurityScheme.getFlow()));
		mSecurityScheme.setAuthorizationUrl(pSecurityScheme.getAuthorizationURL());
		mSecurityScheme.setTokenUrl(pSecurityScheme.getTokenURL());
		if (!pSecurityScheme.getScopes().isEmpty()) {
			for (SecurityScope pSecurityScope : pSecurityScheme.getScopes()) {
				edu.uoc.som.openapi2.SecurityScope mSecurityScope = factory.createSecurityScope();
				mSecurityScope.setDescription(pSecurityScope.getDescription());
				mSecurityScope.setName(pSecurityScope.getName());
				mSecurityScheme.getScopes().add(mSecurityScope);
			}
		}
		return mSecurityScheme;

	}

	private SecurityRequirement extractSecurity(edu.som.uoc.openapi2.profile.SecurityRequirement pSecurityRequirement,
			API api) {
		SecurityRequirement mSecurityRequirement = factory.createSecurityRequirement();

		SecurityScheme securityScheme = OpenAPIUtils.getSecurityDeiitionByName(pSecurityRequirement.getName(), api);
		mSecurityRequirement.setSecurityScheme(securityScheme);
		for (String s : pSecurityRequirement.getScopes()) {
			edu.uoc.som.openapi2.SecurityScope scope = OpenAPIUtils.getSecurityScopeByName(s, securityScheme);
			if (scope != null)
				mSecurityRequirement.getSecurityScopes().add(scope);
		}

		return mSecurityRequirement;

	}

	public ExternalDocs extractExternalDocs(Element element) {
		if (element.isStereotypeApplied(element.getApplicableStereotype(OpenAPIProfileUtils.EXTERNAL_DOCS_QN))) {
			ExternalDocs mExternalDocs = factory.createExternalDocs();
			mExternalDocs.setDescription(
					(String) UMLUtil.getTaggedValue(element, OpenAPIProfileUtils.EXTERNAL_DOCS_QN, "description"));
			mExternalDocs.setUrl((String) UMLUtil.getTaggedValue(element, OpenAPIProfileUtils.EXTERNAL_DOCS_QN, "url"));
			return mExternalDocs;
		}
		return null;
	}
}
