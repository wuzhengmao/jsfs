package org.mingy.jsfs.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.mingy.jsfs.Activator;

public class StatSalesLogAction extends Action {

	private IWorkbenchWindow window;

	public StatSalesLogAction(IWorkbenchWindow window, String text) {
		super(text);
		this.window = window;
		setId(ICommandIds.CMD_STAT_SALES_LOG);
		setActionDefinitionId(ICommandIds.CMD_STAT_SALES_LOG);
		setImageDescriptor(Activator
				.getImageDescriptor("/icons/stat_sales_log.gif"));
	}

	@Override
	public void run() {
		new SalesLogStatConditionInputDialog(window).open();
	}
}
