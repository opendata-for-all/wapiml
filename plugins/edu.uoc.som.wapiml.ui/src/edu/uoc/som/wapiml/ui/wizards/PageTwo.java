package edu.uoc.som.wapiml.ui.wizards;


import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import edu.uoc.som.wapiml.generators.ClassDiagramGenerator;
import edu.uoc.som.wapiml.model.AssociationCandidate;
import edu.uoc.som.wapiml.utils.OpenAPIUtils;

import static org.eclipse.swt.events.SelectionListener.*;

import org.eclipse.swt.widgets.*;

public class PageTwo extends WizardPage{
	
	private ClassDiagramGenerator classDiagramGenerator;
	private Composite container;

    
	public PageTwo(ClassDiagramGenerator classDiagramGenerator) {
		super("WAPIml - Generate a UML model");
		setTitle("WAPIml - Generate a UML model");
		setDescription("This page allows you to modify the discovered associations.");
		this.classDiagramGenerator = classDiagramGenerator;
	}
	
	
	
	

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		setControl(container);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns =1;
		gridLayout.makeColumnsEqualWidth = true;
	       container.setLayout(gridLayout);
	       
	       
		   Label label2 = new Label(container, SWT.NONE);
		   label2.setText("Inferred associations");
		
		
		Table discoveredAssociationsTable = new Table (container, SWT.BORDER | SWT.FULL_SELECTION);
		Rectangle clientArea2 = container.getClientArea ();
		discoveredAssociationsTable.setBounds (clientArea2.x, clientArea2.y,280, 150);
		discoveredAssociationsTable.setLinesVisible (true);
		discoveredAssociationsTable.setHeaderVisible (true);
		GridData data2 = new GridData(SWT.FILL, SWT.FILL, true, true);
		data2.heightHint = 100;
		discoveredAssociationsTable.setLayoutData(data2);
		String[] titles2 = {"Schema", "Property", "Target", "Aggregation kind"};
		for (int i=0; i<titles2.length; i++) {
			TableColumn column = new TableColumn (discoveredAssociationsTable, SWT.NONE);
			column.setText (titles2 [i]);
		}
		for (AssociationCandidate candidate : classDiagramGenerator.getDiscoveredAssociations()) {
			TableItem item2 = new TableItem (discoveredAssociationsTable, SWT.NONE);
			item2.setText (0,candidate.getSchema().getName());
			item2.setText(1, candidate.getProperty().getName());
			item2.setText(2,OpenAPIUtils.getDecoratedName(candidate.getTargetSchema()));
			item2.setText(3,candidate.getAggregationKind().getLiteral());
			item2.setData(candidate);
		}
		for (int i=0; i<titles2.length; i++) {
			discoveredAssociationsTable.getColumn(i).pack();
		}
		final TableEditor editor2 = new TableEditor(discoveredAssociationsTable);
		editor2.horizontalAlignment = SWT.LEFT;
		editor2.grabHorizontal = true;
		editor2.grabVertical = true;
		editor2.minimumWidth = 50;

		discoveredAssociationsTable.addSelectionListener(widgetSelectedAdapter(e -> {
				Control oldEditor = editor2.getEditor();
				if (oldEditor != null)
					oldEditor.dispose();

				// Identify the selected row
				TableItem item2 = (TableItem) e.item;
				if (item2 == null)
					return;

				Combo combo = new Combo (discoveredAssociationsTable, SWT.READ_ONLY);
				combo.setItems("None", "Shared", "Composite");
				combo.select(((AssociationCandidate)item2.getData()).getAggregationKind().getValue());
				
				combo.addListener (SWT.Modify, ev -> {
					System.out.println(combo.getSelectionIndex());
					TableItem[] selectedItems = discoveredAssociationsTable.getSelection();
					AssociationCandidate associationCandidate = (AssociationCandidate) selectedItems[0].getData();
					associationCandidate.setAggregationKind(associationCandidate.getAggregationKindByValue(combo.getSelectionIndex()));
					
					
				});
				
				combo.setFocus();
				final int EDITABLECOLUMN = 3;
				editor2.setEditor(combo, item2, EDITABLECOLUMN);
			}));
		
		
		Label shadow_sep_h = new Label(container, SWT.SEPARATOR | SWT.SHADOW_OUT | SWT.HORIZONTAL);
	       
	       Label label = new Label(container, SWT.NONE);
	   	label.setText("Found associations");
		Table inferredAssociationsTable = new Table (container, SWT.BORDER | SWT.FULL_SELECTION);
		Rectangle clientArea = container.getClientArea();
		inferredAssociationsTable.setBounds (clientArea.x, clientArea.y,480, 150);
		inferredAssociationsTable.setLinesVisible (true);
		inferredAssociationsTable.setHeaderVisible (true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 100;
		inferredAssociationsTable.setLayoutData(data);
		String[] titles = {"Schema", "Property", "Target", "Aggregation kind"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (inferredAssociationsTable, SWT.NONE);
			column.setText (titles [i]);
		}
		for (AssociationCandidate candidate : classDiagramGenerator.getAssocationCandidates()) {
			TableItem item = new TableItem (inferredAssociationsTable, SWT.NONE);
			item.setText (0,candidate.getSchema().getName());
			item.setText(1, candidate.getProperty().getName());
			item.setText(2,candidate.getTargetSchema().getName());
			item.setText(3,candidate.getAggregationKind().getLiteral());
			item.setData(candidate);
		}
		for (int i=0; i<titles.length; i++) {
			inferredAssociationsTable.getColumn(i).pack();
		}
		final TableEditor editor = new TableEditor(inferredAssociationsTable);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		editor.grabVertical = true;
		editor.minimumWidth = 50;
		final int EDITABLECOLUMN = 3;

		inferredAssociationsTable.addSelectionListener(widgetSelectedAdapter(e -> {
				Control oldEditor = editor.getEditor();
				if (oldEditor != null)
					oldEditor.dispose();

				// Identify the selected row
				TableItem item = (TableItem) e.item;
				if (item == null)
					return;

				Combo combo = new Combo (inferredAssociationsTable, SWT.READ_ONLY);
				combo.setItems("None", "Shared", "Composite");
				combo.select(((AssociationCandidate)item.getData()).getAggregationKind().getValue());
				
				combo.addListener (SWT.Modify, ev -> {
					System.out.println(combo.getSelectionIndex());
					TableItem[] selectedItems = inferredAssociationsTable.getSelection();
					AssociationCandidate associationCandidate = (AssociationCandidate) selectedItems[0].getData();
					associationCandidate.setAggregationKind(associationCandidate.getAggregationKindByValue(combo.getSelectionIndex()));
					
					
				});
				
				combo.setFocus();
				editor.setEditor(combo, item, EDITABLECOLUMN);
			}));
		
		Menu menu = new Menu (container.getShell(), SWT.POP_UP);
		inferredAssociationsTable.setMenu (menu);
		MenuItem item = new MenuItem (menu, SWT.PUSH);
		item.setText ("Delete Selection");
		item.addListener (SWT.Selection, event -> { 
		TableItem[] selectedItems =inferredAssociationsTable.getSelection();
		
		for(int i = 0; i< selectedItems.length; i++)
			classDiagramGenerator.getAssocationCandidates().remove(selectedItems[i].getData());
		inferredAssociationsTable.remove (inferredAssociationsTable.getSelectionIndices ());
		});
		

	
	
		
        setPageComplete(true);
        getWizard().getContainer().updateButtons();

		
	}
	
	@Override
	public boolean isCurrentPage() {
		return super.isCurrentPage();
	}

	
}
