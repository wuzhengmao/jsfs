package org.mingy.jsfs.ui;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.mingy.jsfs.Activator;
import org.mingy.jsfs.model.Staff;
import org.mingy.jsfs.ui.model.Catalogs;
import org.mingy.jsfs.ui.model.DateToMaxTimeConverter;
import org.mingy.jsfs.ui.model.DateToMinTimeConverter;
import org.mingy.jsfs.ui.model.SalesLogQueryCondition;
import org.mingy.jsfs.ui.util.IAggregateValidateListener;
import org.mingy.jsfs.ui.util.UIUtils;
import org.mingy.kernel.util.Calendars;
import org.mingy.kernel.util.Langs;

public class SalesLogQueryConditionInputDialog extends TitleAreaDialog
		implements IAggregateValidateListener {

	private static SalesLogQueryCondition queryCondition;
	private DateTime dtStart;
	private DateTime dtEnd;
	private ComboViewer cvStaff;
	private Map<Control, ControlDecoration> decoratorMap = new LinkedHashMap<Control, ControlDecoration>();

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public SalesLogQueryConditionInputDialog(Shell parentShell) {
		super(parentShell);
		if (queryCondition == null) {
			queryCondition = new SalesLogQueryCondition();
			queryCondition.setStartDate(Calendars.getMinTimeNow());
			queryCondition.setEndDate(Calendars.getMaxTimeNow());
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setImage(Activator.getImageDescriptor(
				"/icons/query_sales_log.gif").createImage());
		newShell.setText("查询销售记录");
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitleImage(Activator.getImageDescriptor(
				"/icons/query_sales_log_wiz.gif").createImage());
		setTitle("查询销售记录");
		setMessage("输入查询条件，查询符合条件的销售记录。");

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
		label.setText("开始日期：");
		dtStart = new DateTime(container, SWT.BORDER | SWT.DROP_DOWN);
		dtStart.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		Label label1 = new Label(container, SWT.NONE);
		label1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label1.setText("结束日期：");
		dtEnd = new DateTime(container, SWT.BORDER | SWT.DROP_DOWN);
		dtEnd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		Label label2 = new Label(container, SWT.NONE);
		label2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label2.setText("员工：");
		cvStaff = new ComboViewer(container, SWT.READ_ONLY);
		cvStaff.getCombo().setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "查询", true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
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
		return new Point(450, 300);
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		IObservableMap observeMap = BeansObservables.observeMap(
				listContentProvider.getKnownElements(), Staff.class, "name");
		cvStaff.setLabelProvider(new ObservableMapLabelProvider(observeMap));
		cvStaff.setContentProvider(listContentProvider);
		cvStaff.setInput(Catalogs.getStaffList());
		UIUtils.bindSelection(bindingContext, dtStart, queryCondition,
				"startDate", new DateToMinTimeConverter(), null, decoratorMap,
				this);
		UIUtils.bindSelection(bindingContext, dtEnd, queryCondition, "endDate",
				new DateToMaxTimeConverter(), null, decoratorMap, this);
		UIUtils.bindSelection(bindingContext, cvStaff, queryCondition, "staff",
				null, null, decoratorMap, this);
		return bindingContext;
	}

	@Override
	public void onValidateFinish(boolean passed) {
		if (passed) {
			if (dtEnd.getYear() < dtStart.getYear()
					|| (dtEnd.getYear() == dtStart.getYear() && dtEnd
							.getMonth() < dtStart.getMonth())
					|| (dtEnd.getYear() == dtStart.getYear()
							&& dtEnd.getMonth() == dtStart.getMonth() && dtEnd
							.getDay() < dtStart.getDay())) {
				setErrorMessage(Langs
						.getText("error.input.startDateLargerThanEndDate"));
				getButton(IDialogConstants.OK_ID).setEnabled(false);
			} else {
				setErrorMessage(null);
				getButton(IDialogConstants.OK_ID).setEnabled(true);
			}
		} else {
			setErrorMessage(null);
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		}
	}
}
