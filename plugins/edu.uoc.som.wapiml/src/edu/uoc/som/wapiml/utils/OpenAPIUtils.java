package edu.uoc.som.wapiml.utils;

import java.util.ArrayList;
import java.util.List;

import edu.uoc.som.openapi2.API;
import edu.uoc.som.openapi2.Operation;
import edu.uoc.som.openapi2.Root;
import edu.uoc.som.openapi2.Schema;
import edu.uoc.som.openapi2.SecurityScheme;
import edu.uoc.som.openapi2.SecurityScope;



public class OpenAPIUtils {

	
	public static List<Operation> getAllProducingSchemaOperations(Root root,Schema schema) {
		List<Operation> operationList = new ArrayList<Operation>();
		for(Operation operation: root.getApi().getAllOperations()) {
			Schema s = operation.getProducedSchema();
			if(s!= null && s.equals(schema))
			operationList.add(operation);
		}
		return operationList;
		
	}
	public static List<Operation> getAllRelatedOperations(Root root, Schema definition){
		List<Operation> operationList = new ArrayList<Operation>();
		for(Operation operation : root.getApi().getAllOperations()) {
			if(isDefinitionInTags(definition, operation.getTagReferences()))
				operationList.add(operation);
			else {
				Schema p =operation.getProducedSchema();
				if(p!= null && p.equals(definition))
				operationList.add(operation);
				else {
					Schema c = operation.getConsumedSchema();
					if(c!=null && c.equals(definition))
						operationList.add(operation);
				}
				
			}
		}
		return operationList;
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



	public static String getOperationName(Operation operation) {
		if(operation.getOperationId() != null && !operation.getOperationId().equals(""))
			return operation.getOperationId();
		else {
		
			return operation.getMethod();
		}
		
	}
	
	public static boolean isDefinitionInTags(Schema definition, List<String> tags) {
		if(definition.getReferenceName()== null)
			return false;
		for(String tag: tags)
			if(tag.equalsIgnoreCase(definition.getReferenceName()))
				return true;
		return false;
	}
	public static Schema getAppropriateLocation(API api, Operation operation) {
		for(Schema definition: api.getDefinitions())
			if(isDefinitionInTags(definition, operation.getTagReferences()))
		return definition;
		Schema produced =operation.getProducedSchema();
		if(produced!= null)
			return produced;
		Schema consumed =operation.getConsumedSchema();
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
	public static SecurityScheme getSecurityDeiitionByName(String securityName, API api) {
		for (SecurityScheme  securityScheme : api.getSecurityDefinitions())
			if(securityScheme.getReferenceName().equals(securityName))
				return securityScheme;
		return null;
	}
	public static SecurityScope getSecurityScopeByName(String scope, SecurityScheme securityScheme) {
		for(SecurityScope s : securityScheme.getScopes())
				if(scope.equals(s.getName()))
					return s;
		return null;
	}
}
