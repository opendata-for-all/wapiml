package edu.uoc.som.wapiml.ui.wizards;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import edu.uoc.som.wapiml.generators.ClassDiagramGenerator;

public class GenerateClassDiagramWizard extends Wizard{


	private ClassDiagramGenerator classDiagramGenerator;
	private File targetFile;
	private PageOne pageOne;
	private PageTwo pageTwo;
	
	public GenerateClassDiagramWizard(ClassDiagramGenerator classDiagramGenerator, File targetFile) {
		super();
		setNeedsProgressMonitor(true);
		this.classDiagramGenerator = classDiagramGenerator;
		this.targetFile = targetFile;
		pageOne = new PageOne(classDiagramGenerator);
		pageTwo = new PageTwo(classDiagramGenerator);
		
		
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
	public boolean canFinish() {
		if(!pageOne.discoverAssociations)
			return true;
		if(pageTwo.isCurrentPage())
			return true;
		return false;
		
		
	}
	@Override
	public boolean performFinish() {

		try {
			classDiagramGenerator.generateClassDiagramFromOpenAPI(pageOne.applyProfile,pageOne.discoverAssociations);
			classDiagramGenerator.saveClassDiagram(targetFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}


}
