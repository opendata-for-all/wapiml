package edu.uoc.som.wapiml.model;

import org.eclipse.uml2.uml.AggregationKind;

import edu.uoc.som.openapi2.Property;
import edu.uoc.som.openapi2.Schema;

public class AssociationCandidate {

	private Schema sourceSchema;
	private Property sourceProperty;
	private Schema targetSchema;
	private Property targetProperty;
	private int lowerBound; 
	private int upperBound;
	private AggregationKind aggregationKind;

	

	public AssociationCandidate(Schema sourceSchema, Property sourceProperty, Schema targetSchema,
			Property targetProperty, int lowerBound, int upperBound, AggregationKind aggregationKind) {
		super();
		this.sourceSchema = sourceSchema;
		this.sourceProperty = sourceProperty;
		this.targetSchema = targetSchema;
		this.targetProperty = targetProperty;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.aggregationKind = aggregationKind;
	}

	public String printCardinality() {
		if(lowerBound == 0 && upperBound == -1)
		return "[0]";
		return "["+lowerBound+".."+upperBound+"]";
		
	}

	public Property getTargetProperty() {
		return targetProperty;
	}

	public void setTargetProperty(Property targetProperty) {
		this.targetProperty = targetProperty;
	}

	public Schema getSourceSchema() {
		return sourceSchema;
	}

	public void setSourceSchema(Schema sourceSchema) {
		this.sourceSchema = sourceSchema;
	}

	public Property getSourceProperty() {
		return sourceProperty;
	}

	public void setSourceProperty(Property sourceProperty) {
		this.sourceProperty = sourceProperty;
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

	public int getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(int lowerBound) {
		this.lowerBound = lowerBound;
	}

	public int getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(int upperBound) {
		this.upperBound = upperBound;
	}

}
