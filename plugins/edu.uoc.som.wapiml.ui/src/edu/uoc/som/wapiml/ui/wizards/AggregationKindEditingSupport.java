package edu.uoc.som.wapiml.ui.wizards;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

import edu.uoc.som.wapiml.model.AssociationCandidate;
import edu.uoc.som.wapiml.utils.UMLUtils;

public class AggregationKindEditingSupport extends EditingSupport{
	

	private final TableViewer viewer;
	
	public AggregationKindEditingSupport(TableViewer  viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		String[] aggregationKinds =  new String[] {"none", "shared", "composite"};
		ComboBoxCellEditor comboBoxCellEditor =	new ComboBoxCellEditor(viewer.getTable(),aggregationKinds, SWT.READ_ONLY);
		return comboBoxCellEditor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
	
		
		return ((AssociationCandidate)element).getAggregationKind().getValue();
	}

	@Override
	protected void setValue(Object element, Object value) {
		AssociationCandidate associationCandidate = (AssociationCandidate) element;
		associationCandidate.setAggregationKind(UMLUtils.getAggregationKindByValue((Integer)value));
		viewer.update(element, null);
		
	}
	

}
