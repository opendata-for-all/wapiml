package edu.uoc.som.openapitouml.ui.handlers;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import edu.uoc.som.openapitouml.generators.OpenAPItoUML;
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
					if (monitor == null) monitor = new NullProgressMonitor();
					try {
						monitor.beginTask("Generationg the Class diagram", IProgressMonitor.UNKNOWN);
						for (Iterator<?> iterator = structuredSelection.iterator(); iterator.hasNext();) {
							
							Object obj = iterator.next();
							if (obj instanceof IFile) {
								IFile iFile = (IFile) obj;
								IContainer target = iFile.getProject().getFolder("src-gen");
								if (!target.getLocation().toFile().exists()) {
									target.getLocation().toFile().mkdirs();	
								}
								OpenAPItoUML.genrateAndSaveClassDiagram(new File(iFile.getLocation().toString()),iFile.getName().substring(0, iFile.getName().lastIndexOf('.')) , target.getFullPath().toString());

							}
						}
					} catch (IOException e) {
						return new Status(IStatus.ERROR, OpenAPIToUMLUIPlugin.PLUGIN_ID, e.getLocalizedMessage(), e);
						
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