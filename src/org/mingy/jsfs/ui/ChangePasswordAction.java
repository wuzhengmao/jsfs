package org.mingy.jsfs.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

public class ChangePasswordAction extends Action {

	private IWorkbenchWindow window;

	public ChangePasswordAction(IWorkbenchWindow window, String text) {
		super(text);
		this.window = window;
		setId(ICommandIds.CMD_CHANGE_PASSWORD);
		setActionDefinitionId(ICommandIds.CMD_CHANGE_PASSWORD);
	}

	@Override
	public void run() {
		new ChangePasswordDialog(window).open();
	}
}
