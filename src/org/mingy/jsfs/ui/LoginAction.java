package org.mingy.jsfs.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.mingy.kernel.util.Langs;

public class LoginAction extends Action {

	private IWorkbenchWindow window;

	public LoginAction(IWorkbenchWindow window, String text) {
		super(text);
		this.window = window;
		setId(ICommandIds.CMD_LOGIN);
		setActionDefinitionId(ICommandIds.CMD_LOGIN);
	}

	@Override
	public void run() {
		IEditorPart[] editors = window.getActivePage().getDirtyEditors();
		if (editors != null && editors.length > 0) {
			int rc = new MessageDialog(window.getShell(),
					Langs.getText("confirm.save.title"), null,
					Langs.getText("confirm.save.message.onLogin"),
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
		new LoginDialog(window).open();
	}
}
