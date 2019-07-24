package edu.uoc.som.wapiml.ui.wizards;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

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
		setWindowTitle("WAPIml");
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		// use the org.eclipse.core.runtime.Path as import
		URL url = FileLocator.find(bundle,
		    new Path("images/logo.png"), null);
		ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(url);
		setDefaultPageImageDescriptor(imageDescriptor);
		
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
