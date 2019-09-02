package edu.uoc.som.wapiml.ui.wizards;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.uml2.uml.AggregationKind;

import edu.uoc.som.openapi2.Property;
import edu.uoc.som.wapiml.model.AssociationCandidate;
import edu.uoc.som.wapiml.utils.OpenAPIUtils;

public class TargetPropertyEditingSupport extends EditingSupport {

	private final TableViewer viewer;

	public TargetPropertyEditingSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {

		AssociationCandidate associationCandidate = (AssociationCandidate) element;
		List<Property> properties = OpenAPIUtils.getSingleValuedPrimitiveProperties(associationCandidate.getTargetSchema());
		List<String> propertyNames = properties.stream().map(Property::getName).collect(Collectors.toList());
		ComboBoxCellEditor comboBoxCellEditor = new ComboBoxCellEditor(viewer.getTable(),
				propertyNames.toArray(new String[propertyNames.size()]), SWT.READ_ONLY);
		return comboBoxCellEditor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return (((AssociationCandidate) element).getAggregationKind().equals(AggregationKind.COMPOSITE_LITERAL)) ? false
				: true;
	}

	@Override
	protected Object getValue(Object element) {
		AssociationCandidate associationCandidate = (AssociationCandidate) element;
		List<Property> properties = OpenAPIUtils.getSingleValuedPrimitiveProperties(associationCandidate.getTargetSchema());
		return properties.indexOf(associationCandidate.getTargetProperty());
	}

	@Override
	protected void setValue(Object element, Object value) {
		AssociationCandidate associationCandidate = (AssociationCandidate) element;
		List<Property> properties = OpenAPIUtils.getSingleValuedPrimitiveProperties(associationCandidate.getTargetSchema());
		if ((Integer) value != -1 && !properties.isEmpty()) {
			associationCandidate.setTargetProperty(properties.get((Integer) value));
		}
		viewer.update(element, null);

	}

}
