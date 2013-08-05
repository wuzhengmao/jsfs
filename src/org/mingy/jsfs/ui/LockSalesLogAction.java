package org.mingy.jsfs.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.mingy.jsfs.Activator;

public class LockSalesLogAction extends Action {

	private IWorkbenchWindow window;

	public LockSalesLogAction(IWorkbenchWindow window, String text) {
		super(text);
		this.window = window;
		setId(ICommandIds.CMD_LOCK_SALES_LOG);
		setActionDefinitionId(ICommandIds.CMD_LOCK_SALES_LOG);
		setImageDescriptor(Activator
				.getImageDescriptor("/icons/lock_sales_log.gif"));
		setDisabledImageDescriptor(Activator
				.getImageDescriptor("/icons/lock_sales_log_disabled.gif"));
	}

	@Override
	public void run() {
		new LockSalesLogDialog(window).open();
	}
}
