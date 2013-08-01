package org.mingy.jsfs.ui;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import org.mingy.jsfs.facade.ISalaryFacade;
import org.mingy.jsfs.model.CalcSalaryCondition;
import org.mingy.jsfs.model.Salary;
import org.mingy.jsfs.ui.util.IAggregateValidateListener;
import org.mingy.jsfs.ui.util.UIUtils;
import org.mingy.kernel.context.GlobalBeanContext;
import org.mingy.kernel.util.Langs;

public class CalcSalaryConditionInputDialog extends TitleAreaDialog implements
		IAggregateValidateListener {

	private static final Log logger = LogFactory
			.getLog(CalcSalaryConditionInputDialog.class);

	private IWorkbenchWindow window;
	private CalcSalaryCondition condition;
	private DateTime dtMonth;
	private Map<Control, ControlDecoration> decoratorMap = new LinkedHashMap<Control, ControlDecoration>();

	public CalcSalaryConditionInputDialog(IWorkbenchWindow window) {
		super(window.getShell());
		this.window = window;
		this.condition = new CalcSalaryCondition();
		this.condition.setMonth(new Date());
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setImage(Activator
				.getImageDescriptor("/icons/calc_salary.gif").createImage());
		newShell.setText(Langs.getText("calc_salary.dialog.title"));
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitleImage(Activator
				.getImageDescriptor("/icons/calc_salary_wiz.gif").createImage());
		setTitle(Langs.getText("calc_salary.dialog.title"));
		setMessage(Langs.getText("calc_salary.dialog.message"));

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
		label.setText("计算月份：");
		dtMonth = new DateTime(container, SWT.BORDER | SWT.SHORT
				| SWT.DROP_DOWN);
		dtMonth.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "计算", true);
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
		UIUtils.bindSelection(bindingContext, dtMonth, condition, "month",
				null, null, decoratorMap, this);
		return bindingContext;
	}

	@Override
	public void onValidateFinish(boolean passed) {
		getButton(IDialogConstants.OK_ID).setEnabled(passed);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void okPressed() {
		List<Salary> salaryList;
		try {
			salaryList = GlobalBeanContext.getInstance()
					.getBean(ISalaryFacade.class).calculate(condition);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("error on calculate salary", e);
			}
			MessageDialog.openError(getShell(),
					Langs.getText("error.execute.title"),
					e.getLocalizedMessage() + "\n"
							+ e.getCause().getLocalizedMessage());
			return;
		}
		SalaryListEditorInput input = SalaryListEditorInput
				.getInstance(condition);
		List<Salary> list = (List<Salary>) input.getAdapter(List.class);
		list.clear();
		list.addAll(salaryList);
		IEditorPart editor = window.getActivePage().findEditor(input);
		try {
			window.getActivePage().openEditor(input, SalaryListEditor.ID);
		} catch (PartInitException e) {
			if (logger.isErrorEnabled()) {
				logger.error("error on open editor", e);
			}
			MessageDialog.openError(getShell(), "Error",
					"Error opening editor:" + e.getLocalizedMessage());
			return;
		}
		if (editor != null) {
			((SalaryListEditor) editor).init(input);
		}
		super.okPressed();
	}
}
