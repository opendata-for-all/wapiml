package som.openapitouml.popup.actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.uml2.uml.Model;

import core.Root;
import som.openapitouml.generators.ClassDiagramGenerator;
import som.openapitouml.generators.OpenAPIGenerator;

public class GenerateClassDiagramAction implements IObjectActionDelegate {

	private Shell shell;

	private IFile iFile;

	/**
	 * Constructor for Action1.
	 */
	public GenerateClassDiagramAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		// Initialize the model
		IContainer target = iFile.getProject().getFolder("src-gen");
		if (!target.getLocation().toFile().exists()) {
			target.getLocation().toFile().mkdirs();
		}

		try {

			ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator();
			OpenAPIGenerator openAPIGenerator = new OpenAPIGenerator();
			Root openAPIRoot = openAPIGenerator.createOpenAPIModelFromJson(new File(iFile.getLocation().toString()));
			Model model = classDiagramGenerator.generateClassDiagramFromOpenAPI(openAPIRoot, iFile.getName());
			classDiagramGenerator.saveClassDiagram(model,
					URI.createPlatformResourceURI(target.getFullPath().toString(), true)
							.appendSegment(iFile.getName().substring(0, iFile.getName().lastIndexOf('.')))
							.appendFileExtension("uml"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.getFirstElement() instanceof IFile) {
				iFile = (IFile) structuredSelection.getFirstElement();
			}
		}
	}

}
