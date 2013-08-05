package org.mingy.jsfs.ui;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.mingy.jsfs.Activator;
import org.mingy.jsfs.model.Goods;
import org.mingy.jsfs.model.SalesLog;
import org.mingy.jsfs.model.SalesLog.SalesLogDetail;
import org.mingy.jsfs.ui.model.DateFormatConverter;
import org.mingy.jsfs.ui.util.UIUtils;
import org.mingy.kernel.util.Langs;

public class SalesLogViewDialog extends TitleAreaDialog {

	private SalesLog salesLog;
	private Text txtSalesTime;
	private Text txtStaff;
	private Table table;
	private Text txtMemo;
	private TableViewer tableViewer;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public SalesLogViewDialog(Shell parentShell, SalesLog salesLog) {
		super(parentShell);
		this.salesLog = salesLog;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setImage(Activator.getImageDescriptor(
				"/icons/edit_sales_log.gif").createImage());
		newShell.setText(Langs.getText("view_salesLog.dialog.title"));
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitleImage(Activator.getImageDescriptor(
				"/icons/edit_sales_log_wiz.gif").createImage());
		setTitle(Langs.getText("view_salesLog.dialog.title"));
		setMessage(Langs.getText("view_salesLog.dialog.message"));

		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 10;
		container.setLayout(layout);
		Label label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label.setText("销售时间：");
		txtSalesTime = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		txtSalesTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		Label label2 = new Label(container, SWT.NONE);
		label2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label2.setText("员工：");
		txtStaff = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		txtStaff.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label label_1 = new Label(container, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_1.setText("销售明细：");

		tableViewer = new TableViewer(container, SWT.BORDER
				| SWT.FULL_SELECTION);
		tableViewer.addOpenListener(new IOpenListener() {
			public void open(OpenEvent event) {
			}
		});
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
			}
		});
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.heightHint = 100;
		table.setLayoutData(gd_table);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn = tableViewerColumn.getColumn();
		tableColumn.setWidth(160);
		tableColumn.setText("商品");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_1 = tableViewerColumn_1.getColumn();
		tableColumn_1.setAlignment(SWT.RIGHT);
		tableColumn_1.setWidth(80);
		tableColumn_1.setText("数量");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_2 = tableViewerColumn_2.getColumn();
		tableColumn_2.setAlignment(SWT.RIGHT);
		tableColumn_2.setWidth(100);
		tableColumn_2.setText("总价");

		Label label_2 = new Label(container, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_2.setText("备注：");

		txtMemo = new Text(container, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);
		GridData gd_txtMemo = new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1);
		gd_txtMemo.heightHint = 60;
		txtMemo.setLayoutData(gd_txtMemo);

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID,
				IDialogConstants.CLOSE_LABEL, true);
	}

	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		initDataBindings();
		return contents;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 410);
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		UIUtils.bindText(bindingContext, txtSalesTime, salesLog, "salesTime",
				null, new DateFormatConverter("yyyy/M/d hh:mm"), null);
		UIUtils.bindText(bindingContext, txtStaff, salesLog, "staff.name", null);
		UIUtils.bindText(bindingContext, txtMemo, salesLog, "memo", null);
		//
		ObservableListContentProvider tableContentProvider = new ObservableListContentProvider();
		IObservableMap[] observeMaps = PojoObservables.observeMaps(
				tableContentProvider.getKnownElements(), SalesLogDetail.class,
				new String[] { "goods", "count", "totalPrice" });
		tableViewer
				.setLabelProvider(new ObservableMapLabelProvider(observeMaps) {
					@Override
					public String getColumnText(Object element, int columnIndex) {
						if (columnIndex == 0) {
							Goods goods = (Goods) attributeMaps[0].get(element);
							return goods != null ? goods.getName() : null;
						} else {
							return super.getColumnText(element, columnIndex);
						}
					}
				});
		tableViewer.setContentProvider(tableContentProvider);
		tableViewer.setInput(salesLog.getObservableDetails());
		//

		return bindingContext;
	}
}
