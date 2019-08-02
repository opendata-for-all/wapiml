package edu.uoc.som.wapiml.ui.wizards;


import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import edu.uoc.som.wapiml.generators.ClassDiagramGenerator;
import edu.uoc.som.wapiml.model.AssociationCandidate;

public class PageTwo extends WizardPage{
	
	private ClassDiagramGenerator classDiagramGenerator;
	private Composite container;
	
	private TableViewer topViewer;
	private TableViewer bottomViewer;
    
	public PageTwo(ClassDiagramGenerator classDiagramGenerator) {
		super("WAPIml - Generate a UML model");
		setTitle("WAPIml - Generate a UML model");
		setDescription("This page allows you to modify the discovered associations.");
		this.classDiagramGenerator = classDiagramGenerator;
	}
	
	
	
	

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		setControl(container);
		GridLayout layout  = new GridLayout(2,false);
	       container.setLayout(layout);
	        createTopViewer(container);
	        createBottomViewer(container);
	       
		  
		
        setPageComplete(true);
        getWizard().getContainer().updateButtons();

		
	}
	private void createTopViewer(Composite parent) {
		topViewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        createColumns(parent, topViewer);
        final Table table = topViewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        topViewer.setContentProvider(new ArrayContentProvider());
        // Get the content for the viewer, setInput will call getElements in the
        // contentProvider
        topViewer.setInput(classDiagramGenerator.getAssociations());
    
        // Layout the viewer
        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        topViewer.getControl().setLayoutData(gridData);
    }

	private void createBottomViewer(Composite parent) {
		bottomViewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        createColumns(parent, bottomViewer);
        final Table table = bottomViewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        bottomViewer.setContentProvider(new ArrayContentProvider());
        // Get the content for the viewer, setInput will call getElements in the
        // contentProvider
        bottomViewer.setInput(classDiagramGenerator.getAssociationCandidates());
    
        // Layout the viewer
        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        bottomViewer.getControl().setLayoutData(gridData);
    }
	 private void createColumns(final Composite parent, final TableViewer viewer) {
	        String[] titles = { "Source", "Property", "Target","Cardinality","Aggregation Kind", "Target property"};
	        int[] bounds = { 100, 100, 100,100,100,100 };


	        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], viewer, 0);
	        col.setLabelProvider(new CellLabelProvider() {
	            @Override
	            public void update(ViewerCell cell) {
	            	AssociationCandidate a = (AssociationCandidate) cell.getElement();
	              cell.setText(a.getSourceSchema().getName());
	            }
	        });

	        col = createTableViewerColumn(titles[1], bounds[1], viewer, 1);
	        col.setLabelProvider(new CellLabelProvider() {
	            @Override
	            public void update(ViewerCell cell) {
	            	AssociationCandidate a = (AssociationCandidate) cell.getElement();
	              cell.setText(a.getSourceProperty().getName());
	            }
	        });

	        col = createTableViewerColumn(titles[2], bounds[2], viewer, 2);
	        col.setLabelProvider(new CellLabelProvider() {
	            @Override
	            public void update(ViewerCell cell) {
	            	AssociationCandidate a = (AssociationCandidate) cell.getElement();
	              cell.setText(a.getTargetSchema().getName());
	            }
	        });
	        
	        
	        col = createTableViewerColumn(titles[3], bounds[3], viewer, 3);
	        col.setLabelProvider(new CellLabelProvider() {
	            @Override
	            public void update(ViewerCell cell) {
	            	AssociationCandidate a = (AssociationCandidate) cell.getElement();
	              cell.setText(a.printCardinality());
	            }
	        });
	        
	        col = createTableViewerColumn(titles[4], bounds[4], viewer, 4);
	        col.setLabelProvider(new CellLabelProvider() {
	            @Override
	            public void update(ViewerCell cell) {
	            	AssociationCandidate a = (AssociationCandidate) cell.getElement();
	              cell.setText(a.getAggregationKind().getLiteral());
	            }
	        });
	        col.setEditingSupport(new AggregationKindEditingSupport(viewer));
	        
	        col = createTableViewerColumn(titles[5], bounds[5], viewer, 5);
	        col.setLabelProvider(new CellLabelProvider() {
	            @Override
	            public void update(ViewerCell cell) {
	            	AssociationCandidate a = (AssociationCandidate) cell.getElement();
	              cell.setText((a.getTargetProperty()!=null)?a.getTargetProperty().getName():"");
	            }
	        });
	        col.setEditingSupport(new TargetPropertyEditingSupport(viewer));



	       
	    }

	 private TableViewerColumn createTableViewerColumn(String title, int bound, TableViewer viewer, final int colNumber) {
	        final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
	                SWT.NONE);
	        final TableColumn column = viewerColumn.getColumn();
	        column.setText(title);
	        column.setWidth(bound);
	        column.setResizable(true);
	        column.setMoveable(true);
	        return viewerColumn;

	    }
	
	@Override
	public boolean isCurrentPage() {
		return super.isCurrentPage();
	}

	
}
