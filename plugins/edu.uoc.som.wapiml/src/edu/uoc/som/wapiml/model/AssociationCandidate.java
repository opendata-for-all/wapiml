package edu.uoc.som.wapiml.model;

import org.eclipse.uml2.uml.AggregationKind;

import edu.uoc.som.openapi2.Property;
import edu.uoc.som.openapi2.Schema;

public class AssociationCandidate {
	
	private Schema schema;
	private Property property;
	private Schema targetSchema;
	private AggregationKind aggregationKind;
	
	
	
	public AssociationCandidate(Schema schema, Property property, Schema targetSchema, AggregationKind aggregationKind) {
		this.schema = schema;
		this.property = property;
		this.targetSchema = targetSchema;
		this.aggregationKind = aggregationKind;
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
	public AggregationKind getAggregationKind() {
		return aggregationKind;
	}
	public void setAggregationKind(AggregationKind aggregationKind) {
		this.aggregationKind = aggregationKind;
	}
	
	public AggregationKind getAggregationKindByValue(int value) {
		switch (value) {
		case AggregationKind.NONE:
			return AggregationKind.NONE_LITERAL;
		case AggregationKind.SHARED:
			return AggregationKind.SHARED_LITERAL;
		case AggregationKind.COMPOSITE:
			return AggregationKind.COMPOSITE_LITERAL;
		default: 
			return null;
		}
	}
}
