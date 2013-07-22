package org.mingy.jsfs.ui;

/**
 * Interface defining the application's command IDs. Key bindings can be defined
 * for specific commands. To associate an action with a command, use
 * IAction.setActionDefinitionId(commandId).
 * 
 * @see org.eclipse.jface.action.IAction#setActionDefinitionId(String)
 */
public interface ICommandIds {

	public static final String CMD_OPEN_CATALOG = "org.mingy.jsfs.ui.openCatalog";
	public static final String CMD_OPEN_CONSOLE = "org.mingy.jsfs.ui.openConsole";

}
