package edu.uoc.som.wapiml.ui.wizards;


import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.uml2.uml.AggregationKind;

import edu.uoc.som.wapiml.generators.ClassDiagramGenerator;
import edu.uoc.som.wapiml.model.AssociationCandidate;
import static org.eclipse.swt.events.SelectionListener.*;

import org.eclipse.swt.events.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class PageTwo extends WizardPage{
	
	private ClassDiagramGenerator classDiagramGenerator;
	private Composite container;

    
	public PageTwo(ClassDiagramGenerator classDiagramGenerator) {
		super("Inferred associations");
		setTitle("Discovered associations");
		setDescription("Remove the associations that you think are not correct.");
		this.classDiagramGenerator = classDiagramGenerator;
	}
	
	
	
	

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		setControl(container);
		Table table = new Table (container, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		Rectangle clientArea = container.getClientArea ();
		table.setBounds (clientArea.x, clientArea.y,400, 300);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);
		String[] titles = {"Schema", "Property", "Target", "Aggregation kind"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles [i]);
		}
		for (AssociationCandidate candidate : classDiagramGenerator.getAssocationCandidates()) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText (0,candidate.getSchema().getName());
			item.setText(1, candidate.getProperty().getName());
			item.setText(2,candidate.getTargetSchema().getName());
			item.setText(3,candidate.getAggregationKind().getLiteral());
			item.setData(candidate);
		}
		for (int i=0; i<titles.length; i++) {
			table.getColumn(i).pack();
		}
		final TableEditor editor = new TableEditor(table);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		editor.grabVertical = true;
		editor.minimumWidth = 50;
		final int EDITABLECOLUMN = 3;

		table.addSelectionListener(widgetSelectedAdapter(e -> {
				Control oldEditor = editor.getEditor();
				if (oldEditor != null)
					oldEditor.dispose();

				// Identify the selected row
				TableItem item = (TableItem) e.item;
				if (item == null)
					return;

				Combo combo = new Combo (table, SWT.READ_ONLY);
				combo.setItems("None", "Shared", "Composite");
				combo.select(((AssociationCandidate)item.getData()).getAggregationKind().getValue());
				
				combo.addListener (SWT.Modify, ev -> {
					System.out.println(combo.getSelectionIndex());
					TableItem[] selectedItems = table.getSelection();
					AssociationCandidate associationCandidate = (AssociationCandidate) selectedItems[0].getData();
					associationCandidate.setAggregationKind(associationCandidate.getAggregationKindByValue(combo.getSelectionIndex()));
					
					
				});
				
				combo.setFocus();
				editor.setEditor(combo, item, EDITABLECOLUMN);
			}));
		
		Menu menu = new Menu (container.getShell(), SWT.POP_UP);
		table.setMenu (menu);
		MenuItem item = new MenuItem (menu, SWT.PUSH);
		item.setText ("Delete Selection");
		item.addListener (SWT.Selection, event -> { 
		TableItem[] selectedItems =table.getSelection();
		
		for(int i = 0; i< selectedItems.length; i++)
			classDiagramGenerator.getAssocationCandidates().remove(selectedItems[i].getData());
		table.remove (table.getSelectionIndices ());
		});
        setPageComplete(true);
        getWizard().getContainer().updateButtons();

		
	}
	
	@Override
	public boolean isCurrentPage() {
		return super.isCurrentPage();
	}

	
}
