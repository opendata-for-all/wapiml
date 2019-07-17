package edu.uoc.som.wapiml.ui.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

public class GenerateClassDiagramWizard extends Wizard{

	private IFile inputDefinition;
	private PageOne pageOne = new PageOne(inputDefinition);
	private PageTwo pageTwo = new PageTwo(inputDefinition);
	
	public GenerateClassDiagramWizard(IFile inputDefinition) {
		super();
		this.inputDefinition = inputDefinition;
		
		
	}
	
	@Override
	public void addPages() {
		addPage(pageOne);
		addPage(pageTwo);
	}
	
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		return super.getNextPage(page);
	}
	
	
	@Override
	public boolean performFinish() {
		System.out.println(pageOne.isApplyProfile());
		System.out.println(pageOne.isDiscoverAssociations());
		return true;
	}


}
