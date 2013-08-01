package org.mingy.jsfs.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.mingy.jsfs.Activator;

public class CalcSalaryAction extends Action {

	private IWorkbenchWindow window;

	public CalcSalaryAction(IWorkbenchWindow window, String text) {
		super(text);
		this.window = window;
		setId(ICommandIds.CMD_CALC_SALARY);
		setActionDefinitionId(ICommandIds.CMD_CALC_SALARY);
		setImageDescriptor(Activator
				.getImageDescriptor("/icons/calc_salary.gif"));
	}

	@Override
	public void run() {
		new CalcSalaryConditionInputDialog(window).open();
	}
}
