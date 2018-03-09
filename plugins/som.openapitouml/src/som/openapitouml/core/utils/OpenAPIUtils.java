package som.openapitouml.core.utils;

import java.util.ArrayList;
import java.util.List;

import edu.uoc.som.openapi.Operation;
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
	public static List<Operation> getAllRelatedOperations(Root root, Schema schema){
		List<Operation> operationList = new ArrayList<Operation>();
		for(Operation operation : root.getApi().getAllOperations()) {
			if(isSchemaInTags(schema, operation.getTagReferences()))
				operationList.add(operation);
			else {
				Schema p =operation.getProducedSchema();
				if(p!= null && p.equals(schema))
				operationList.add(operation);
				else {
					Schema c = operation.getConsumedSchema();
					if(c!=null && c.equals(schema))
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
		
			return operation.getMethod()+(((operation.getProducedSchema()!=null && operation.getProducedSchema().getName()!=null) )?operation.getProducedSchema().getName():"Unknown");
		}
		
	}
	
	public static boolean isSchemaInTags(Schema schema, List<String> tags) {
		if(schema.getName()== null)
			return false;
		for(String tag: tags)
			if(tag.equalsIgnoreCase(schema.getName()))
				return true;
		return false;
	}
}
