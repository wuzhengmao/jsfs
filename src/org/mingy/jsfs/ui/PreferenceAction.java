package org.mingy.jsfs.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.ui.IWorkbenchWindow;

public class PreferenceAction extends Action {

	private IWorkbenchWindow window;

	public PreferenceAction(IWorkbenchWindow window, String text) {
		super(text);
		this.window = window;
		setId(ICommandIds.CMD_PREFERENCE);
		setActionDefinitionId(ICommandIds.CMD_PREFERENCE);
	}

	@Override
	public void run() {
		PreferenceManager pm = new PreferenceManager();
		pm.addToRoot(new PreferenceNode("system", new PreferencePage()));
		PreferenceDialog dialog = new PreferenceDialog(window.getShell(), pm);
		dialog.open();
	}
}
