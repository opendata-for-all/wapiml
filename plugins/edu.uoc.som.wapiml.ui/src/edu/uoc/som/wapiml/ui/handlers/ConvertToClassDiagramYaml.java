package edu.uoc.som.wapiml.ui.handlers;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import edu.uoc.som.openapi2.API;
import edu.uoc.som.openapi2.io.exception.OpenAPIValidationException;
import edu.uoc.som.openapi2.io.model.SerializationFormat;
import edu.uoc.som.wapiml.generators.ClassDiagramGenerator;
import edu.uoc.som.wapiml.ui.WAPImlUIPlugin;
import edu.uoc.som.wapiml.ui.wizards.GenerateClassDiagramWizard;
import edu.uoc.som.wapiml.utils.IOUtils;

public class ConvertToClassDiagramYaml extends AbstractHandler {

	public static final String ID = "edu.uoc.som.wapiml.ui.handlers.ConvertToClassDiagramYaml";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		if (selection != null & selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Job job = new Job(ID) {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					final StringBuilder message = new StringBuilder();
					if (monitor == null)
						monitor = new NullProgressMonitor();
					try {

						monitor.beginTask("Generationg the Class diagram", IProgressMonitor.UNKNOWN);

						for (Iterator<?> iterator = structuredSelection.iterator(); iterator.hasNext();) {

							Object obj = iterator.next();
							if (obj instanceof IFile) {
								IFile iFile = (IFile) obj;

								IContainer target = iFile.getProject().getFolder("src-gen");
								if (!target.getLocation().toFile().exists()) {
									target.getLocation().toFile().mkdirs();
									iFile.getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
								}
								File inputFile = new File(iFile.getLocation().toString());
								API api = IOUtils.loadOpenAPIModel(inputFile,SerializationFormat.YAML);
								ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator(api, iFile.getName().substring(0, iFile.getName().lastIndexOf('.')),true,true);
								Display.getDefault().syncExec(new Runnable() {
								    public void run() {
								    	WizardDialog dialog = new WizardDialog(new Shell (Display.getCurrent()), new GenerateClassDiagramWizard(classDiagramGenerator,target.getLocation()
												.append(iFile.getName().substring(0, iFile.getName().lastIndexOf('.')))
												.addFileExtension("uml").toFile()));
										dialog.open();
								    }
								});
							

								iFile.getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
							}
						}
					} catch (IOException | CoreException e) {
						e.printStackTrace();
						return new Status(IStatus.ERROR, WAPImlUIPlugin.PLUGIN_ID, e.getLocalizedMessage(),
								e.getCause());

					} catch (OpenAPIValidationException  e) {
						return new Status(IStatus.ERROR, WAPImlUIPlugin.PLUGIN_ID, e.getLocalizedMessage());
					} 
					finally {
						monitor.done();
					}

					return Status.OK_STATUS;
				}
			};
			job.schedule();

		}
		return null;
	}
}