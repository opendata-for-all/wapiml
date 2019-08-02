package edu.uoc.som.wapiml.utils;

import org.eclipse.uml2.uml.AggregationKind;

public class UMLUtils {

	public static AggregationKind getAggregationKindByValue(int value) {
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
