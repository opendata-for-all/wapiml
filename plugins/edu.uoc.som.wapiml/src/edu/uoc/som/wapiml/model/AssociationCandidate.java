package edu.uoc.som.wapiml.model;

import edu.uoc.som.openapi2.Property;
import edu.uoc.som.openapi2.Schema;

public class AssociationCandidate {
	
	private Schema schema;
	private Property property;
	private Schema targetSchema;
	
	
	public AssociationCandidate(Schema schema, Property property, Schema targetSchema) {
		super();
		this.schema = schema;
		this.property = property;
		this.targetSchema = targetSchema;
	}
	public Schema getSchema() {
		return schema;
	}
	public void setSchema(Schema schema) {
		this.schema = schema;
	}
	public Property getProperty() {
		return property;
	}
	public void setProperty(Property property) {
		this.property = property;
	}
	public Schema getTargetSchema() {
		return targetSchema;
	}
	public void setTargetSchema(Schema targetSchema) {
		this.targetSchema = targetSchema;
	}
	
	@Override
	public String toString() {
		return schema.getReferenceName()+": "+property.getReferenceName()+" -> "+targetSchema.getReferenceName();
	}

}
