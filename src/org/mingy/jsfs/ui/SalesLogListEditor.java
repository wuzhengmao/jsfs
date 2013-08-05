package org.mingy.jsfs.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.List;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.mingy.jsfs.facade.IConfigFacade;
import org.mingy.jsfs.model.SalesLog;
import org.mingy.jsfs.model.SalesLog.SalesLogDetail;
import org.mingy.jsfs.model.SalesLogQueryCondition;
import org.mingy.jsfs.model.Staff;
import org.mingy.kernel.context.GlobalBeanContext;
import org.mingy.kernel.util.Calendars;

public class SalesLogListEditor extends EditorPart {

	public static final String ID = "org.mingy.jsfs.ui.SalesLogListEditor"; //$NON-NLS-1$
	private Table table;

	public SalesLogListEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		TableViewer tableViewer = new TableViewer(container, SWT.BORDER
				| SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn = tableViewerColumn.getColumn();
		tableColumn.setAlignment(SWT.CENTER);
		tableColumn.setWidth(120);
		tableColumn.setText("销售时间");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_1 = tableViewerColumn_1.getColumn();
		tableColumn_1.setAlignment(SWT.CENTER);
		tableColumn_1.setWidth(100);
		tableColumn_1.setText("员工");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_2 = tableViewerColumn_2.getColumn();
		tableColumn_2.setWidth(300);
		tableColumn_2.setText("销售商品");

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_3 = tableViewerColumn_3.getColumn();
		tableColumn_3.setWidth(100);
		tableColumn_3.setAlignment(SWT.RIGHT);
		tableColumn_3.setText("总价");

		tableViewer.addOpenListener(new IOpenListener() {
			@Override
			public void open(OpenEvent event) {
				if (!event.getSelection().isEmpty()) {
					SalesLog salesLog = (SalesLog) ((IStructuredSelection) event
							.getSelection()).getFirstElement();
					if (!"LOCK".equals(GlobalBeanContext
							.getInstance()
							.getBean(IConfigFacade.class)
							.getConfig(
									Calendars.get10Date(salesLog.getSalesTime())))) {
						new SalesLogEditDialog(getSite().getShell(), salesLog)
								.open();
					} else {
						new SalesLogViewDialog(getSite().getShell(), salesLog)
								.open();
					}
				}
			}
		});

		ObservableListContentProvider tableContentProvider = new ObservableListContentProvider();
		IObservableMap[] observeMaps = PojoObservables.observeMaps(
				tableContentProvider.getKnownElements(), SalesLog.class,
				new String[] { "salesTime", "staff", "details" });
		tableViewer
				.setLabelProvider(new ObservableMapLabelProvider(observeMaps) {
					@SuppressWarnings("unchecked")
					@Override
					public String getColumnText(Object element, int columnIndex) {
						switch (columnIndex) {
						case 0:
							Date salesTime = (Date) attributeMaps[0]
									.get(element);
							return Calendars.get16Date(salesTime);
						case 1:
							Staff staff = (Staff) attributeMaps[1].get(element);
							return staff.getName();
						case 2:
							StringBuilder sb = new StringBuilder();
							for (SalesLogDetail detail : (List<SalesLogDetail>) attributeMaps[2]
									.get(element)) {
								if (sb.length() > 0)
									sb.append("，");
								sb.append(detail.getGoods().getName() + "×"
										+ detail.getCount());
							}
							return sb.toString();
						default:
							double price = 0;
							for (SalesLogDetail detail : (List<SalesLogDetail>) attributeMaps[2]
									.get(element)) {
								price += detail.getTotalPrice();
							}
							return String.valueOf(price);
						}
					}
				});
		tableViewer.setContentProvider(tableContentProvider);
		tableViewer.setInput(getEditorInput().getAdapter(List.class));
	}

	@Override
	public void setFocus() {
		table.setFocus();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// Do the Save operation
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());
		SalesLogQueryCondition queryCondition = (SalesLogQueryCondition) SalesLogListEditorInput
				.getInstance().getAdapter(SalesLogQueryCondition.class);
		queryCondition.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setPartName(getEditorInput().getName());
			}
		});
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
}
