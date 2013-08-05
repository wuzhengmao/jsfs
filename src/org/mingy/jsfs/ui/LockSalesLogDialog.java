package org.mingy.jsfs.ui;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.mingy.jsfs.Activator;
import org.mingy.jsfs.facade.ISalesLogFacade;
import org.mingy.jsfs.model.SalesLogLockCondition;
import org.mingy.jsfs.ui.model.DateToMaxTimeConverter;
import org.mingy.jsfs.ui.model.DateToMinTimeConverter;
import org.mingy.jsfs.ui.util.IAggregateValidateListener;
import org.mingy.jsfs.ui.util.UIUtils;
import org.mingy.kernel.context.GlobalBeanContext;
import org.mingy.kernel.util.Calendars;
import org.mingy.kernel.util.Langs;

import com.ibm.icu.util.Calendar;

public class LockSalesLogDialog extends TitleAreaDialog implements
		IAggregateValidateListener {

	private SalesLogLockCondition condition;
	private DateTime dtStart;
	private DateTime dtEnd;
	private Map<Control, ControlDecoration> decoratorMap = new LinkedHashMap<Control, ControlDecoration>();

	public LockSalesLogDialog(IWorkbenchWindow window) {
		super(window.getShell());
		condition = new SalesLogLockCondition();
		condition.setStartDate(Calendars.getMinTime(Calendars.calculate(
				new Date(), Calendar.DATE, -1)));
		condition.setEndDate(Calendars.getMaxTime(Calendars.calculate(
				new Date(), Calendar.DATE, -1)));
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setImage(Activator.getImageDescriptor(
				"/icons/lock_sales_log.gif").createImage());
		newShell.setText(Langs.getText("lock_salesLog.dialog.title"));
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle(Langs.getText("lock_salesLog.dialog.title"));
		setMessage(Langs.getText("lock_salesLog.dialog.message"));

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

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "锁定", true);
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
		UIUtils.bindSelection(bindingContext, dtStart, condition, "startDate",
				new DateToMinTimeConverter(), null, decoratorMap, this);
		UIUtils.bindSelection(bindingContext, dtEnd, condition, "endDate",
				new DateToMaxTimeConverter(), null, decoratorMap, this);
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

	@Override
	protected void okPressed() {
		if (!MessageDialog.openConfirm(getShell(),
				Langs.getText("confirm.lock.title"),
				Langs.getText("confirm.lock.message"))) {
			return;
		}
		GlobalBeanContext.getInstance().getBean(ISalesLogFacade.class)
				.lockSalesLog(condition);
		super.okPressed();
	}
}
