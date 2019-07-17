package edu.uoc.som.wapiml.ui.wizards;


import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import edu.uoc.som.wapiml.generators.ClassDiagramGenerator;
import edu.uoc.som.wapiml.model.AssociationCandidate;
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
		Table table = new Table (container, SWT.BORDER | SWT.MULTI);
		Rectangle clientArea = container.getClientArea ();
		table.setBounds (clientArea.x, clientArea.y,400, 300);
		for (AssociationCandidate candidate : classDiagramGenerator.getAssocationCandidates()) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText (candidate.toString());
			item.setData(candidate);
		}
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
