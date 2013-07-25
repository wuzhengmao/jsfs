package org.mingy.jsfs.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.mingy.jsfs.Activator;

public class OpenCatalogAction extends Action {

	private static final Log logger = LogFactory
			.getLog(OpenCatalogAction.class);

	private IWorkbenchWindow window;

	public OpenCatalogAction(IWorkbenchWindow window, String text) {
		super(text);
		this.window = window;
		setId(ICommandIds.CMD_OPEN_CATALOG);
		setActionDefinitionId(ICommandIds.CMD_OPEN_CATALOG);
		setImageDescriptor(Activator.getImageDescriptor("/icons/folder.gif"));
	}

	@Override
	public void run() {
		try {
			window.getActivePage().showView(CatalogView.ID, null,
					IWorkbenchPage.VIEW_VISIBLE);
		} catch (PartInitException e) {
			if (logger.isErrorEnabled()) {
				logger.error("error on open view", e);
			}
			MessageDialog.openError(window.getShell(), "Error",
					"Error opening view:" + e.getLocalizedMessage());
		}
	}
}
