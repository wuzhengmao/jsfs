package org.mingy.jsfs.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.mingy.jsfs.ui.model.Catalog;
import org.mingy.jsfs.ui.model.Catalogs;

public class NewStaffWizard extends Wizard implements INewWizard {

	public static final String ID = "org.mingy.jsfs.ui.NewStaffWizard"; //$NON-NLS-1$

	private static final Log logger = LogFactory.getLog(NewStaffWizard.class);

	private IWorkbench workbench;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
	}

	@Override
	public boolean performFinish() {
		try {
			workbench
					.getActiveWorkbenchWindow()
					.getActivePage()
					.openEditor(
							new CatalogEditorInput(
									Catalogs.getCatalog(Catalog.TYPE_STAFF)),
							StaffEditor.ID);
			return true;
		} catch (PartInitException e) {
			if (logger.isErrorEnabled()) {
				logger.error("error on open editor", e);
			}
			MessageDialog.openError(getShell(), "Error",
					"Error opening editor:" + e.getLocalizedMessage());
			return false;
		}
	}
}
