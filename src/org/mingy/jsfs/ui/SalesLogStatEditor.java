package org.mingy.jsfs.ui;

import java.util.Date;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.mingy.jsfs.model.SalesLogStat;
import org.mingy.jsfs.model.Staff;
import org.mingy.kernel.util.Calendars;
import org.mingy.kernel.util.Strings;

public class SalesLogStatEditor extends EditorPart {

	public static final String ID = "org.mingy.jsfs.ui.SalesLogStatEditor"; //$NON-NLS-1$

	private Composite container;
	private Table table;

	public SalesLogStatEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		init();
	}

	public void init() {
		table.removeAll();
		for (TableColumn col : table.getColumns()) {
			col.dispose();
		}
		TableColumn staffCol = new TableColumn(table, SWT.NONE);
		staffCol.setWidth(80);
		staffCol.setText("员工");
		SalesLogStat salesLogStat = (SalesLogStat) getEditorInput().getAdapter(
				SalesLogStat.class);
		Date[] days = salesLogStat.getDays();
		for (Date day : days) {
			TableColumn col = new TableColumn(table, SWT.NONE);
			col.setAlignment(SWT.RIGHT);
			col.setWidth(80);
			col.setText(Calendars.get10Date(day));
		}
		TableColumn statCol = new TableColumn(table, SWT.NONE);
		statCol.setAlignment(SWT.RIGHT);
		statCol.setWidth(80);
		statCol.setText("总计");
		for (Staff staff : salesLogStat.getStaffs()) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, staff.getName());
			for (int i = 0; i < days.length; i++) {
				item.setText(i + 1, Strings.objToString(salesLogStat.getStat(
						days[i], staff)));
			}
			item.setText(days.length + 1,
					Strings.objToString(salesLogStat.getStat(staff)));
		}
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(0, "合计");
		for (int i = 0; i < days.length; i++) {
			item.setText(i + 1,
					Strings.objToString(salesLogStat.getStat(days[i])));
		}
		item.setText(days.length + 1,
				Strings.objToString(salesLogStat.getStat()));
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
	}

	public void init(IEditorInput input) {
		setInput(input);
		setPartName(input.getName());
		init();
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
