package org.mingy.jsfs.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.mingy.jsfs.Activator;

public class InputSalesLogAction extends Action {

	private IWorkbenchWindow window;

	public InputSalesLogAction(IWorkbenchWindow window, String text) {
		super(text);
		this.window = window;
		setId(ICommandIds.CMD_INPUT_SALES_LOG);
		setActionDefinitionId(ICommandIds.CMD_INPUT_SALES_LOG);
		setImageDescriptor(Activator
				.getImageDescriptor("/icons/edit_sales_log.gif"));
	}

	@Override
	public void run() {
		new SalesLogEditDialog(window.getShell(), null).open();
	}
}
