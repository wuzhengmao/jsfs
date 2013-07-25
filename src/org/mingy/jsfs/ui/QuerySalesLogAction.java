package org.mingy.jsfs.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.mingy.jsfs.Activator;

public class QuerySalesLogAction extends Action {

	private IWorkbenchWindow window;

	public QuerySalesLogAction(IWorkbenchWindow window, String text) {
		super(text);
		this.window = window;
		setId(ICommandIds.CMD_QUERY_SALES_LOG);
		setActionDefinitionId(ICommandIds.CMD_QUERY_SALES_LOG);
		setImageDescriptor(Activator
				.getImageDescriptor("/icons/query_sales_log.gif"));
	}

	@Override
	public void run() {
		new SalesLogQueryConditionInputDialog(window.getShell()).open();
	}
}
