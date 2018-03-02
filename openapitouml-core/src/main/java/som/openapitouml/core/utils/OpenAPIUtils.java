package som.openapitouml.core.utils;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;


import core.API;
import core.JSONDataType;
import core.Operation;
import core.Parameter;
import core.Path;
import core.Response;
import core.Root;
import core.Schema;
import core.SecuritySchema;
import core.SecurityScope;

public class OpenAPIUtils {

	public static Schema getSchemaByPathReference(String ref, API api) {
		List<Schema> schemas = api.getDefinitions();
		for (Schema schema : schemas) {
			if (schema.getRef().equalsIgnoreCase(ref))
				return schema;
		}
		return null;
	}

	public static List<Operation> getAllOperations(Root apiRoot) {
		TreeIterator<EObject> allElements = apiRoot.eAllContents();
		List<Operation> allOperations = new ArrayList<Operation>();
		while (allElements.hasNext()) {
			EObject object = allElements.next();
			if (object instanceof Operation) {
				allOperations.add((Operation) object);
			}
		}
		return allOperations;
	}

	public static Operation getOperationById(Root apiRoot, String operationId) {
		List<Operation> allOperations = getAllOperations(apiRoot);
		for (Operation operation : allOperations) {
			if (operation.getOperationId().equals(operationId))
				return operation;
		}
		return null;
	}

	public static String getOpearationPath(Operation operation) {
		return ((API) operation.getPath().eContainer()).getHost()
				+ ((API) operation.getPath().eContainer()).getBasePath() + operation.getPath().getPattern();

	}
	public static Path getPathByPattern (API api, String parttern) {
		for(Path path: api.getPaths()) {
			if(path.getPattern().equalsIgnoreCase(parttern)) {
				return path;
			}
		}
		return null;
		
	}

	public static Operation getOperationByMethod(String method, Path path) {
		switch(method) {
		case "GET": return path.getGet();
		case "POST": return path.getPost();
		case "PUT": return path.getPost();
		case "DELETE": return path.getDelete();
		default: return null;
		}
	}
	


	public static Schema getSchemaByName(String name, API api) {
		for (Schema schema : api.getDefinitions()) {
			if (schema.getName().equalsIgnoreCase(name))
				return schema;
		}
		return null;
	}

	public static Schema getPropertyByName(String name, Schema schema) {
		for (Schema property : schema.getProperties())
			if (property.getName().equals(name))
				return property;
		return null;

	}


	public static String getPathFromOperation(Operation operation) {
		return ((API) operation.eContainer().eContainer()).getHost()
				+ ((API) operation.eContainer().eContainer()).getBasePath() + operation.getPath().getPattern();

	}
	public static List<Operation> getAllProducingSchemaOperations(Root root,Schema schema) {
		List<Operation> operationList = new ArrayList<Operation>();
		for(Operation operation: getAllOperations(root)) {
			for(Response response: operation.getResponses()) {
				if(response.getCode().equals("200") && ((response.getSchema().getType().equals(JSONDataType.ARRAY) && response.getSchema().getItems().equals(schema)) || response.getSchema().equals(schema))) {
					operationList.add(operation);
				}
			}
		}
		return operationList;
		
	}
	public static Schema getProperty(Root root,Schema schema, Schema property) {
		for(Schema prop: schema.getProperties()) {
			if((prop.getValue() != null && prop.getValue().equals(property)) || (prop.getType().equals(JSONDataType.ARRAY) && prop.getItems() != null && prop.getItems().equals(property)))
				return prop;
		}
		return null;
	}

	public static List<Operation> getAccessiblSchemaProducingOperations(Root root,Schema schema){
		List<Operation> accessibileOperations = new ArrayList<Operation>();
		List<Operation> allProducingOperations = getAllProducingSchemaOperations(root, schema);
		for(Operation operation: allProducingOperations) {
			if(operation.getParameters().isEmpty())
				accessibileOperations.add(operation);
		}
		return accessibileOperations;
	}
	public static SecuritySchema getSecuritySchemaByName (API api, String name) {
		for(SecuritySchema securitySchema: api.getSecurityDefinitions())
			if(securitySchema.getGlobalName().equals(name))
				return securitySchema;
		return null;
	}
	public static SecurityScope getSecurityScopeByName(SecuritySchema securitySchema, String securityScopeName) {
		for(SecurityScope securityScope: securitySchema.getScopes())
			if(securityScope.getName().equals(securityScopeName))
				return securityScope;
		return null;
	}
	public static Parameter getParameterByRef (String ref, API api) {
		String referenceName = ref.substring(ref.lastIndexOf("/")+1);
		for (Parameter p : api.getParameters()) {
			if(p.getReferenceName().equals(referenceName))
				return p;
		}
		return null;
	}
}
