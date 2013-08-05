package org.mingy.jsfs.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.mingy.jsfs.model.Role;
import org.mingy.jsfs.model.RoleManager;
import org.mingy.kernel.util.Langs;

public class LogoutAction extends Action {

	private static final Log logger = LogFactory.getLog(LogoutAction.class);

	private IWorkbenchWindow window;

	public LogoutAction(IWorkbenchWindow window, String text) {
		super(text);
		this.window = window;
		setId(ICommandIds.CMD_LOGOUT);
		setActionDefinitionId(ICommandIds.CMD_LOGOUT);
	}

	@Override
	public void run() {
		if (!MessageDialog.openConfirm(
				window.getShell(),
				Langs.getText("confirm.logout.title"),
				Langs.getText("confirm.logout.message", RoleManager
						.getInstance().getRole().toString()))) {
			return;
		}
		IEditorPart[] editors = window.getActivePage().getDirtyEditors();
		if (editors != null && editors.length > 0) {
			int rc = new MessageDialog(window.getShell(),
					Langs.getText("confirm.save.title"), null,
					Langs.getText("confirm.save.message.onCloseAll"),
					MessageDialog.CONFIRM, new String[] {
							IDialogConstants.YES_LABEL,
							IDialogConstants.NO_LABEL,
							IDialogConstants.CANCEL_LABEL }, 0).open();
			if (rc == 0) {
				for (IEditorPart editor : editors) {
					if (!window.getActivePage().saveEditor(editor, false)) {
						return;
					}
				}
			} else if (rc == 2) {
				return;
			}
		}
		window.getActivePage().closeAllEditors(false);
		if (logger.isInfoEnabled()) {
			logger.info("Logout: " + RoleManager.getInstance().getRole().name());
		}
		RoleManager.getInstance().setRole(Role.GUEST);
		CatalogView view = (CatalogView) window.getActivePage().findView(
				CatalogView.ID);
		if (view != null) {
			view.syncToolbarStatus();
		}
	}
}
