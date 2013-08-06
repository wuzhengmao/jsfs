package org.mingy.jsfs.ui;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.mingy.jsfs.Activator;
import org.mingy.jsfs.facade.ISalesLogFacade;
import org.mingy.jsfs.model.SalesLogQueryCondition;
import org.mingy.jsfs.model.SalesLogStat;
import org.mingy.jsfs.ui.model.DateToMaxTimeConverter;
import org.mingy.jsfs.ui.model.DateToMinTimeConverter;
import org.mingy.jsfs.ui.util.IAggregateValidateListener;
import org.mingy.jsfs.ui.util.UIUtils;
import org.mingy.kernel.context.GlobalBeanContext;
import org.mingy.kernel.util.Langs;

public class SalesLogStatConditionInputDialog extends TitleAreaDialog implements
		IAggregateValidateListener {

	private static final Log logger = LogFactory
			.getLog(SalesLogStatConditionInputDialog.class);

	private IWorkbenchWindow window;
	private SalesLogQueryCondition queryCondition;
	private DateTime dtStart;
	private DateTime dtEnd;
	private Map<Control, ControlDecoration> decoratorMap = new LinkedHashMap<Control, ControlDecoration>();

	public SalesLogStatConditionInputDialog(IWorkbenchWindow window) {
		super(window.getShell());
		this.window = window;
		this.queryCondition = new SalesLogQueryCondition();
		((SalesLogQueryCondition) SalesLogStatEditorInput.getInstance()
				.getAdapter(SalesLogQueryCondition.class))
				.copyTo(this.queryCondition);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setImage(Activator.getImageDescriptor(
				"/icons/stat_sales_log.gif").createImage());
		newShell.setText(Langs.getText("stat_salesLog.dialog.title"));
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitleImage(Activator.getImageDescriptor(
				"/icons/stat_sales_log_wiz.gif").createImage());
		setTitle(Langs.getText("stat_salesLog.dialog.title"));
		setMessage(Langs.getText("stat_salesLog.dialog.message"));

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
		createButton(parent, IDialogConstants.OK_ID, "统计", true);
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
		UIUtils.bindSelection(bindingContext, dtStart, queryCondition,
				"startDate", new DateToMinTimeConverter(), null, decoratorMap,
				this);
		UIUtils.bindSelection(bindingContext, dtEnd, queryCondition, "endDate",
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

	@SuppressWarnings("unchecked")
	@Override
	protected void okPressed() {
		ISalesLogFacade salesLogFacade = GlobalBeanContext.getInstance()
				.getBean(ISalesLogFacade.class);
		SalesLogStat salesLogStat = salesLogFacade.statSalesLog(queryCondition);
		SalesLogStatEditorInput input = SalesLogStatEditorInput.getInstance();
		queryCondition.copyTo((SalesLogQueryCondition) input
				.getAdapter(SalesLogQueryCondition.class));
		Set<String> set = (Set<String>) input.getAdapter(Set.class);
		set.clear();
		set.addAll(salesLogFacade.queryLockedDays(
				queryCondition.getStartDate(), queryCondition.getEndDate()));
		input.setSalesLogStat(salesLogStat);
		IEditorPart editor = window.getActivePage().findEditor(input);
		try {
			window.getActivePage().openEditor(input, SalesLogStatEditor.ID);
		} catch (PartInitException e) {
			if (logger.isErrorEnabled()) {
				logger.error("error on open editor", e);
			}
			MessageDialog.openError(getShell(), "Error",
					"Error opening editor:" + e.getLocalizedMessage());
			return;
		}
		if (editor != null) {
			((SalesLogStatEditor) editor).init(input);
		}
		super.okPressed();
	}
}
