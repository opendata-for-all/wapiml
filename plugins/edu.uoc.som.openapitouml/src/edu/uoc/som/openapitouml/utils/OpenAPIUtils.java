package edu.uoc.som.openapitouml.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import edu.uoc.som.openapi.API;
import edu.uoc.som.openapi.Definition;
import edu.uoc.som.openapi.Operation;
import edu.uoc.som.openapi.Path;
import edu.uoc.som.openapi.Root;
import edu.uoc.som.openapi.Schema;



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
	public static List<Operation> getAllRelatedOperations(Root root, Definition definition){
		List<Operation> operationList = new ArrayList<Operation>();
		for(Operation operation : root.getApi().getAllOperations()) {
			if(isDefinitionInTags(definition, operation.getTagReferences()))
				operationList.add(operation);
			else {
				Schema p =operation.getProducedSchema();
				if(p!= null && p.equals(definition.getSchema()))
				operationList.add(operation);
				else {
					Schema c = operation.getConsumedSchema();
					if(c!=null && c.equals(definition.getSchema()))
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
	
	public static boolean isDefinitionInTags(Definition definition, List<String> tags) {
		if(definition.getName()== null)
			return false;
		for(String tag: tags)
			if(tag.equalsIgnoreCase(definition.getName()))
				return true;
		return false;
	}
	public static Definition getAppropriateLocation(API api, Operation operation) {
		for(Definition definition: api.getDefinitions())
			if(isDefinitionInTags(definition, operation.getTagReferences()))
		return definition;
		Schema produced =operation.getProducedSchema();
		if(produced!= null)
			return getDefinitionFromSchema(api, produced);
		Schema consumed =operation.getConsumedSchema();
		if(consumed != null)
			return getDefinitionFromSchema(api, consumed);
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
	public static Definition getDefinitionFromSchema(API api, Schema schema) {
		for(Definition definition: api.getDefinitions()) {
			if(definition.getSchema().equals(schema))
				return definition;
		}
		return null;
	}
}
