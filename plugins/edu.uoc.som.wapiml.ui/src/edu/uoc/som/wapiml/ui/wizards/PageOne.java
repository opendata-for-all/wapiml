package edu.uoc.som.wapiml.ui.wizards;


import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import edu.uoc.som.wapiml.generators.ClassDiagramGenerator;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;
public class PageOne extends WizardPage{
	
	private ClassDiagramGenerator classDiagramGenerator;
	private Composite container;
     boolean applyProfile;
     boolean discoverAssociations;
    
	public PageOne(ClassDiagramGenerator classDiagramGenerator) {
		super("WAPIml - Generate a UML model");
		setTitle("WAPIml - Generate a UML model");
		setDescription("This page allows you to set the generation options.");
		this.classDiagramGenerator = classDiagramGenerator;
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		
		
		
		Label label1 = new Label(container, SWT.NONE);
		label1.setText("Apply the OpenAPI profile");
		Composite applyProfileRadioGroup = new Composite(container, SWT.NONE);
		applyProfileRadioGroup.setLayout(layout);
		
		Button applyProfileButton = new Button (applyProfileRadioGroup, SWT.CHECK);
		applyProfileButton.setSelection(true);
		applyProfile= true;
		applyProfileButton.addSelectionListener(widgetSelectedAdapter(event -> applyProfile = applyProfileButton.getSelection()));
		
		
		
		
		Label label2 = new Label(container, SWT.NONE);
		label2.setText("Discover associations");
		Composite discovererAssociationsRadioGroup = new Composite(container, SWT.NONE);
		discovererAssociationsRadioGroup.setLayout(layout);
		
		Button discoverAssocationsButton = new Button (discovererAssociationsRadioGroup, SWT.CHECK);
		discoverAssocationsButton.setSelection(true);
		discoverAssociations = true;
		discoverAssocationsButton.addSelectionListener(widgetSelectedAdapter(event -> {
			discoverAssociations = discoverAssocationsButton.getSelection(); getWizard().getContainer().updateButtons();
			}));
		
		
       
        setControl(container);
        setPageComplete(true);

		
	}
	@Override
	public boolean canFlipToNextPage() {
		if(!discoverAssociations)
			return false;
		return true;
	}
	
	

	
	  

	
}
