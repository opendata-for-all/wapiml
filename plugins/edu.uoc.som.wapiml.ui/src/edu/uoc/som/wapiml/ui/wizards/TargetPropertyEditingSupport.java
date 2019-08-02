package edu.uoc.som.wapiml.ui.wizards;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

import edu.uoc.som.openapi2.Property;
import edu.uoc.som.wapiml.model.AssociationCandidate;

public class TargetPropertyEditingSupport extends EditingSupport{
	

	private final TableViewer viewer;
	
	public TargetPropertyEditingSupport(TableViewer  viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		
		
		AssociationCandidate associationCandidate = (AssociationCandidate) element;
		List<String> propertyNames = associationCandidate.getTargetSchema().getProperties().stream().map(Property::getName).collect(Collectors.toList());
		ComboBoxCellEditor comboBoxCellEditor =	new ComboBoxCellEditor(viewer.getTable(),propertyNames.toArray(new String[propertyNames.size()]), SWT.READ_ONLY);
		return comboBoxCellEditor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return (((AssociationCandidate)element).getUpperBound() == 1)?true:false;
	}

	@Override
	protected Object getValue(Object element) {
	
		AssociationCandidate associationCandidate = (AssociationCandidate) element;
		return associationCandidate.getTargetSchema().getProperties().indexOf(associationCandidate.getTargetProperty());
	}

	@Override
	protected void setValue(Object element, Object value) {
		AssociationCandidate associationCandidate = (AssociationCandidate) element;
		associationCandidate.setTargetProperty(associationCandidate.getTargetSchema().getProperties().get((Integer) value));
		viewer.update(element, null);
		
	}
	

}
