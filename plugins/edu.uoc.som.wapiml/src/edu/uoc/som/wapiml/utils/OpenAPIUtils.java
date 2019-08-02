package edu.uoc.som.wapiml.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import edu.uoc.som.openapi2.API;
import edu.uoc.som.openapi2.JSONDataType;
import edu.uoc.som.openapi2.Operation;
import edu.uoc.som.openapi2.Parameter;
import edu.uoc.som.openapi2.ParameterLocation;
import edu.uoc.som.openapi2.Response;
import edu.uoc.som.openapi2.Schema;
import edu.uoc.som.openapi2.SecurityScheme;
import edu.uoc.som.openapi2.SecurityScope;



public class OpenAPIUtils {

	
	public static List<Operation> getAllProducingSchemaOperations(API api,Schema schema) {
		List<Operation> operationList = new ArrayList<Operation>();
		for(Operation operation: api.getAllOperations()) {
			Schema s = getProducedSchema(operation);
			if(s!= null && s.equals(schema))
			operationList.add(operation);
		}
		return operationList;
		
	}
	public static List<Operation> getAllRelatedOperations(API api, Schema definition){
		List<Operation> operationList = new ArrayList<Operation>();
		for(Operation operation : api.getAllOperations()) {
			if(isDefinitionInTags(definition, operation.getTagReferences()))
				operationList.add(operation);
			else {
				Schema p = getProducedSchema(operation);
				if(p!= null && p.equals(definition))
				operationList.add(operation);
				else {
					Schema c = getConsumedSchema(operation);
					if(c!=null && c.equals(definition))
						operationList.add(operation);
				}
				
			}
		}
		return operationList;
	}



	public static List<Operation> getAccessiblSchemaProducingOperations(API api,Schema schema){
		List<Operation> accessibileOperations = new ArrayList<Operation>();
		List<Operation> allProducingOperations = getAllProducingSchemaOperations(api, schema);
		for(Operation operation: allProducingOperations) {
			if(operation.getParameters().isEmpty())
				accessibileOperations.add(operation);
		}
		return accessibileOperations;
	}



	public static String getOperationName(Operation operation) {
		if(operation.getOperationId() != null && !operation.getOperationId().equals(""))
			return operation.getOperationId();
		else {
		
			return operation.getHTTPMethod();
		}
		
	}
	
	public static boolean isDefinitionInTags(Schema definition, List<String> tags) {
		if(definition.getName()== null)
			return false;
		for(String tag: tags)
			if(tag.equalsIgnoreCase(definition.getName()))
				return true;
		return false;
	}
	public static Schema getAppropriateLocation(API api, Operation operation) {
		for(Entry<String, Schema> definition: api.getDefinitions())
			if(isDefinitionInTags(definition.getValue(), operation.getTagReferences()))
		return definition.getValue();
		Schema produced = getProducedSchema(operation);
		if(produced!= null)
			return produced;
		Schema consumed = getConsumedSchema(operation);
		if(consumed != null)
			return consumed;
		return null;
	}

	public static String getLastMeaningfullSegment(String path) {
		if(!path.equals("/")) {
		String[] segments = path.substring(1).split("/");
		if(segments.length > 0) {
		for(int i = (segments.length -1); i >= 0; i--) {
			if(!segments[i].contains("{")) {
				return segments[i];
			}}
		}
		}
		return "Resource";
	}
	
	public static SecurityScope getSecurityScopeByName(String scope, SecurityScheme securityScheme) {
		for(SecurityScope s : securityScheme.getScopes())
				if(scope.equals(s.getName()))
					return s;
		return null;
	}
	
	public static Schema getConsumedSchema(Operation operation) {
		for(Parameter parameter: operation.getParameters()) {
			if(parameter.getLocation().equals(ParameterLocation.BODY))
				if(parameter.getSchema().getType().equals(JSONDataType.ARRAY))
					return parameter.getSchema().getItems();
					else
						return parameter.getSchema();
			}
		return null;
	}


	public static Schema getProducedSchema(Operation operation) {
		for(Entry<String, Response> response: operation.getResponses()) {
			if(((response.getKey().equals("200") || response.getKey().equals("200") ))   && response.getValue()!=null && response.getValue().getSchema()!= null && response.getValue().getSchema().getType().equals(JSONDataType.ARRAY)) {
				return response.getValue().getSchema().getItems();
			
			}
			if(((response.getKey().equals("200") || response.getKey().equals("200") ))  &&  response.getValue()!=null  && response.getValue().getSchema()!= null && response.getValue().getSchema().getType().equals(JSONDataType.OBJECT)) {
				return response.getValue().getSchema();
			}
		}
		return null;
	}


	public static String getDecoratedName(Schema schema) {
		if( schema.getType().equals(JSONDataType.OBJECT)) {
			if(schema.getName()!= null)
				return schema.getName();
			else 
				return "undefined";
		}
		if(schema.getType().equals(JSONDataType.ARRAY))
			return getDecoratedName(schema.getItems())+" [*]";
		return "undefined";
	
	}
	public static boolean isObject(Schema schema) {
		if (schema.getType().equals(JSONDataType.OBJECT))
			return true;

		if (!schema.getProperties().isEmpty())
			return true;

		if (!schema.getAllOf().isEmpty())
			return true;

		return false;
	}

	public static boolean isArrayOfObjects(Schema schema) {

		if (schema.getType().equals(JSONDataType.ARRAY) && isObject(schema.getItems()))
			return true;

		return false;
	}
	
}
