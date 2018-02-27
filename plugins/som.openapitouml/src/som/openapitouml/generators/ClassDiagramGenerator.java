package som.openapitouml.generators;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import core.JSONDataType;
import core.Operation;
import core.Path;
import core.Root;
import core.Schema;
import som.openapitouml.utils.OpenAPIUtils;

public class ClassDiagramGenerator implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;
	private UMLFactory umlFactory;
	ResourceSet resourceSet = new ResourceSetImpl();

	public ClassDiagramGenerator() {
		umlFactory = UMLFactory.eINSTANCE;
		resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
				UMLResource.Factory.INSTANCE);
		resourceSet.getURIConverter().getURIMap().put(
				URI.createURI("pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml"), URI.createPlatformPluginURI(
						"/org.eclipse.uml2.uml.resources/libraries/UMLPrimitiveTypes.library.uml", true));

	}

	private void loadDefaultImports(Model model) {
		Model umlLibrary = (Model) resourceSet
				.getResource(URI.createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI), true).getContents().get(0);
		model.createElementImport(umlLibrary.getOwnedType("Boolean"));
		model.createElementImport(umlLibrary.getOwnedType("String"));
		model.createElementImport(umlLibrary.getOwnedType("UnlimitedNatural"));
		model.createElementImport(umlLibrary.getOwnedType("Real"));
		model.createElementImport(umlLibrary.getOwnedType("Integer"));
	}

	public Model generateClassDiagramFromOpenAPI(Root root, String modelName) {
		Model model = umlFactory.createModel();
		model.setName(modelName);

		Map<Schema, Class> map = new HashMap<Schema, Class>();
		loadDefaultImports(model);
		for (Schema schema : root.getApi().getDefinitions()) {
			if (schema.getType().equals(JSONDataType.OBJECT)) {
				Class clazz = umlFactory.createClass();
				clazz.setName(schema.getName());

				if (!OpenAPIUtils.getAccessiblSchemaProducingOperations(root, schema).isEmpty()) {
					String operations = "";
					for (Operation operation : OpenAPIUtils.getAccessiblSchemaProducingOperations(root, schema)) {
						operations += "GET " + ((Path) operation.eContainer()).getPattern();
					}

				}
				model.getOwnedTypes().add(clazz);
				map.put(schema, clazz);
				for (Schema property : schema.getProperties()) {
					if (isPrimitive(property)) {
						Property umlProperty = umlFactory.createProperty();
						umlProperty.setName(property.getName());

						if (!property.getType().equals(JSONDataType.ARRAY))
							umlProperty.setType(getUMLTypeFromJson(model, property.getType()));
						else {
							umlProperty.setUpper(-1);
							umlProperty.setType(getUMLTypeFromJson(model, property.getItems().getType()));
						}
						clazz.getOwnedAttributes().add(umlProperty);
					}
				}
			}
		}
		// resolve associations
		for (Schema schema : root.getApi().getDefinitions()) {
			if (schema.getType().equals(JSONDataType.OBJECT)) {
				for (Schema property : schema.getProperties()) {
					if (!isPrimitive(property)) {
						Association association = umlFactory.createAssociation();
						association.setName(schema.getName() + "_" + property.getName());
						Property firstOwnedEnd = umlFactory.createProperty();
						association.getOwnedEnds().add(firstOwnedEnd);
						Property secondOwnedEnd = umlFactory.createProperty();
						association.getOwnedEnds().add(secondOwnedEnd);
						firstOwnedEnd.setName(schema.getName());
						firstOwnedEnd.setType(map.get(schema));
						secondOwnedEnd.setName(property.getName());
						if (!property.getType().equals(JSONDataType.ARRAY)) {
							Class type = map.get(property.getValue());
							Class schemaClass = map.get(schema);

							secondOwnedEnd.setType(type);
						} else {
							secondOwnedEnd.setUpper(-1);
							secondOwnedEnd.setType(map.get(property.getItems()));
							Class schemaClass = map.get(schema);
							Class type = map.get(property.getItems());

						}
						association.getNavigableOwnedEnds().add(secondOwnedEnd);
						model.getPackagedElements().add(association);
					}

				}
			}
		}

		return model;

	}

	public Model refine(Model model) {
		List<Class> classes = new ArrayList<Class>();
		List<Association> associations = new ArrayList<Association>();
		List<Property> propertiesToRemore = new ArrayList<Property>();
		for (PackageableElement element : model.getPackagedElements()) {
			if (element instanceof Class) {
				Class clazz = (Class) element;
				classes.add(clazz);
			}
		}

		for (PackageableElement element : model.getPackagedElements()) {
			if (element instanceof Class) {
				Class clazz = (Class) element;
				for (Property property : clazz.getOwnedAttributes()) {
					for (Class c : classes) {
						if (property.getName().equalsIgnoreCase(c.getName())
								&& !property.getName().equalsIgnoreCase(clazz.getName())) {
							associations.add(extractAssociation(clazz, c, property));
							propertiesToRemore.add(property);

						}
					}
				}
			}
		}
		for (Association association : associations) {
			model.getPackagedElements().add(association);
		}
		for (Property property : propertiesToRemore) {
			EcoreUtil.remove(property);
		}
		return model;
	}

	public Model class2DataType(Model model, Class clazz, List<Element> newElements) {
		DataType dataType = umlFactory.createDataType();
		dataType.setName(clazz.getName());

		for (Element element : clazz.getOwnedElements()) {
			Element copy = EcoreUtil.copy(element);
			dataType.getOwnedAttributes().add((org.eclipse.uml2.uml.Property) copy);
		}
		List<Association> associationsToRemove = new ArrayList<Association>();
		for (PackageableElement element : model.getPackagedElements()) {
			if (element instanceof Association) {
				Association association = (Association) element;
				for (Property end : association.getOwnedEnds()) {
					if (end.getType().equals(clazz))
						associationsToRemove.add(association);
				}
			}
		}
		for (Association association : associationsToRemove) {
			Property firstEnd = association.getOwnedEnds().get(0);
			Property secondEnd = association.getOwnedEnds().get(1);
			if (firstEnd.getType().equals(clazz)) {
				Property copy = EcoreUtil.copy(firstEnd);
				copy.setType(dataType);
				newElements.add(copy);
				((Class) secondEnd.getType()).getOwnedAttributes().add(copy);
			}
			if (secondEnd.getType().equals(clazz)) {
				Property copy = EcoreUtil.copy(secondEnd);
				copy.setType(dataType);
				newElements.add(copy);

				((Class) firstEnd.getType()).getOwnedAttributes().add(copy);
			}
		}
		EcoreUtil.remove(clazz);
		for (Association association : associationsToRemove)
			EcoreUtil.remove(association);
		newElements.add(dataType);
		model.getPackagedElements().add(dataType);
		return model;

	}

	private Association extractAssociation(Class class1, Class class2, Property property) {
		Association association = umlFactory.createAssociation();

		association.setName(class1.getName() + "_" + class2.getName());
		Property firstEnd = umlFactory.createProperty();
		firstEnd.setName(property.getName());
		firstEnd.setType(class2);
		firstEnd.setUpper(property.getUpper());
		association.getOwnedEnds().add(firstEnd);
		Property secondEnd = umlFactory.createProperty();
		secondEnd.setName(class1.getName());
		secondEnd.setType(class1);
		association.getOwnedEnds().add(secondEnd);
		return association;
	}

	private boolean isPrimitive(Schema property) {
		if (property.getType().equals(JSONDataType.BOOLEAN) || property.getType().equals(JSONDataType.INTEGER)
				|| property.getType().equals(JSONDataType.NUMBER) || property.getType().equals(JSONDataType.STRING))
			return true;
		if (property.getType().equals(JSONDataType.ARRAY) && (property.getItems().getType().equals(JSONDataType.BOOLEAN)
				|| property.getItems().getType().equals(JSONDataType.INTEGER)
				|| property.getItems().getType().equals(JSONDataType.NUMBER)
				|| property.getItems().getType().equals(JSONDataType.STRING)))
			return true;
		return false;
	}

	private PrimitiveType getUMLTypeFromJson(Model model, JSONDataType jsonDataType) {
		PrimitiveType type = null;
		switch (jsonDataType) {
		case BOOLEAN:
			type = (PrimitiveType) model.getImportedMember("Boolean");
			break;
		case INTEGER:
			type = (PrimitiveType) model.getImportedMember("Integer");
			break;
		case STRING:
			type = (PrimitiveType) model.getImportedMember("String");
			break;
		case NUMBER:
			type = (PrimitiveType) model.getImportedMember("Real");
			break;

		default:
			break;
		}
		return type;

	}

	public UMLFactory getUmlFactory() {
		return umlFactory;
	}

	public void setUmlFactory(UMLFactory umlFactory) {
		this.umlFactory = umlFactory;
	}

	public void saveClassDiagram(Model model, URI resourceURI) throws IOException {
		Resource resource = resourceSet.createResource(resourceURI);
		resource.getContents().add(model);
		resource.save(Collections.EMPTY_MAP);

	}

}
