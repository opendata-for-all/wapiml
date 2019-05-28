package edu.uoc.som.openapitouml.ui.handlers;

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
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import edu.uoc.som.openapitouml.exception.OpenAPIValidationException;
import edu.uoc.som.openapitouml.facade.OpenAPItoUMLFacade;
import edu.uoc.som.openapitouml.ui.OpenAPIToUMLUIPlugin;

public class ConvertToClassDiagram extends AbstractHandler {

	public static final String ID = "edu.uoc.som.openapitouml.ui.popup.handlers.ConvertToClassDiagram";

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
								OpenAPItoUMLFacade openAPItoUMLFacade = new OpenAPItoUMLFacade();
								openAPItoUMLFacade.generateAndSaveClassDiagram(inputFile,
										iFile.getName().substring(0, iFile.getName().lastIndexOf('.')),target.getLocation()
										.append(iFile.getName().substring(0, iFile.getName().lastIndexOf('.')))
										.addFileExtension("uml").toFile(), false, true);
								iFile.getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
							}
						}
					} catch (IOException | CoreException | ProcessingException e) {
						return new Status(IStatus.ERROR, OpenAPIToUMLUIPlugin.PLUGIN_ID, e.getLocalizedMessage(),
								e.getCause());

					} catch (OpenAPIValidationException e) {
						return new Status(IStatus.ERROR, OpenAPIToUMLUIPlugin.PLUGIN_ID, e.getLocalizedMessage());
					} finally {
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