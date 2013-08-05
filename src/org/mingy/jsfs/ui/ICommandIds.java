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
	public static final String CMD_INPUT_SALES_LOG = "org.mingy.jsfs.ui.inputSalesLog";
	public static final String CMD_QUERY_SALES_LOG = "org.mingy.jsfs.ui.querySalesLog";
	public static final String CMD_STAT_SALES_LOG = "org.mingy.jsfs.ui.statSalesLog";
	public static final String CMD_CALC_SALARY = "org.mingy.jsfs.ui.calcSalary";
	public static final String CMD_OPEN_CONSOLE = "org.mingy.jsfs.ui.openConsole";
	public static final String CMD_BACKUP_DATABASE = "org.mingy.jsfs.ui.backupDatabase";
	public static final String CMD_RESTORE_DATABASE = "org.mingy.jsfs.ui.restoreDatabase";
	public static final String CMD_LOGIN = "org.mingy.jsfs.ui.login";
	public static final String CMD_LOGOUT = "org.mingy.jsfs.ui.logout";
	public static final String CMD_CHANGE_PASSWORD = "org.mingy.jsfs.ui.changePassword";

}
