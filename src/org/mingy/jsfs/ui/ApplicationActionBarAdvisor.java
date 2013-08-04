package org.mingy.jsfs.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.mingy.jsfs.model.Role;
import org.mingy.jsfs.model.RoleManager;
import org.mingy.jsfs.model.RoleManager.RoleChangeListener;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private IAction aboutAction;
	private IAction quitAction;
	private IAction newAction;
	private IAction saveAction;
	private IAction saveAllAction;
	private IAction closeAction;
	private IAction closeAllAction;
	private IAction openCatalogAction;
	private IAction inputSalesLogAction;
	private IAction querySalesLogAction;
	private IAction statSalesLogAction;
	private IAction calcSalaryAction;
	private IAction backupDatabaseAction;
	private IAction restoreDatabaseAction;
	private IAction openConsoleAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	@Override
	protected void makeActions(final IWorkbenchWindow window) {
		{
			aboutAction = ActionFactory.ABOUT.create(window);
			register(aboutAction);
		}
		{
			quitAction = ActionFactory.QUIT.create(window);
			register(quitAction);
		}
		{
			newAction = ActionFactory.NEW_WIZARD_DROP_DOWN.create(window);
			register(newAction);
		}
		{
			saveAction = ActionFactory.SAVE.create(window);
			register(saveAction);
		}
		{
			saveAllAction = ActionFactory.SAVE_ALL.create(window);
			register(saveAllAction);
		}
		{
			closeAction = ActionFactory.CLOSE.create(window);
			register(closeAction);
		}
		{
			closeAllAction = ActionFactory.CLOSE_ALL.create(window);
			register(closeAllAction);
		}
		{
			openCatalogAction = new OpenCatalogAction(window, "目录浏览(&R)");
			openCatalogAction.setToolTipText("打开目录浏览视图");
			register(openCatalogAction);
		}
		{
			inputSalesLogAction = new InputSalesLogAction(window,
					"输入销售记录(&I)...");
			inputSalesLogAction.setToolTipText("输入销售记录");
			register(inputSalesLogAction);
		}
		{
			querySalesLogAction = new QuerySalesLogAction(window,
					"查询销售记录(&Q)...");
			querySalesLogAction.setToolTipText("查询销售记录");
			register(querySalesLogAction);
		}
		{
			statSalesLogAction = new StatSalesLogAction(window, "统计销售记录(&S)...");
			statSalesLogAction.setToolTipText("统计销售记录");
			register(statSalesLogAction);
		}
		{
			calcSalaryAction = new CalcSalaryAction(window, "计算工资(&C)...");
			calcSalaryAction.setToolTipText("计算工资");
			register(calcSalaryAction);
		}
		{
			backupDatabaseAction = new BackupDatabaseAction(window,
					"数据备份(&B)...");
			backupDatabaseAction.setToolTipText("将数据库备份到文件");
			register(backupDatabaseAction);
		}
		{
			restoreDatabaseAction = new RestoreDatabaseAction(window,
					"数据还原(&R)...");
			restoreDatabaseAction.setToolTipText("根据备份文件还原数据库");
			register(restoreDatabaseAction);
		}
		{
			openConsoleAction = new OpenConsoleAction(window, "控制台(&L)");
			openConsoleAction.setToolTipText("打开控制台输出日志");
			register(openConsoleAction);
		}
		statSalesLogAction.setEnabled(false);
		calcSalaryAction.setEnabled(false);
		backupDatabaseAction.setEnabled(false);
		restoreDatabaseAction.setEnabled(false);
		RoleManager.getInstance().addRoleChangeListener(
				new RoleChangeListener() {
					@Override
					public void onChange(Role oldRole, Role newRole) {
						statSalesLogAction
								.setEnabled(newRole == Role.ACCOUNTING);
						calcSalaryAction.setEnabled(newRole == Role.ACCOUNTING);
						backupDatabaseAction.setEnabled(newRole == Role.ADMIN);
						restoreDatabaseAction.setEnabled(newRole == Role.ADMIN);
					}
				});
	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager("文件(&F)",
				IWorkbenchActionConstants.M_FILE);
		MenuManager launchMenu = new MenuManager("运行(&R)",
				IWorkbenchActionConstants.M_LAUNCH);
		MenuManager windowMenu = new MenuManager("窗口(&W)",
				IWorkbenchActionConstants.M_WINDOW);
		MenuManager helpMenu = new MenuManager("帮助(&H)",
				IWorkbenchActionConstants.M_HELP);

		menuBar.add(fileMenu);
		fileMenu.add(newAction);
		fileMenu.add(new Separator());
		fileMenu.add(closeAction);
		fileMenu.add(closeAllAction);
		fileMenu.add(new Separator());
		fileMenu.add(saveAction);
		fileMenu.add(saveAllAction);
		fileMenu.add(new Separator());
		fileMenu.add(quitAction);
		menuBar.add(launchMenu);
		launchMenu.add(inputSalesLogAction);
		launchMenu.add(querySalesLogAction);
		launchMenu.add(statSalesLogAction);
		launchMenu.add(new Separator());
		launchMenu.add(calcSalaryAction);
		launchMenu.add(new Separator());
		launchMenu.add(backupDatabaseAction);
		launchMenu.add(restoreDatabaseAction);
		menuBar.add(windowMenu);
		windowMenu.add(openCatalogAction);
		windowMenu.add(new Separator());
		windowMenu.add(openConsoleAction);
		menuBar.add(helpMenu);
		helpMenu.add(aboutAction);
	}

	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
		coolBar.add(new ToolBarContributionItem(toolbar, "main"));
		toolbar.add(newAction);
		toolbar.add(new Separator());
		toolbar.add(saveAction);
		toolbar.add(saveAllAction);
		toolbar.add(new Separator());
		toolbar.add(openCatalogAction);
		toolbar.add(new Separator());
		toolbar.add(inputSalesLogAction);
		toolbar.add(querySalesLogAction);
		toolbar.add(statSalesLogAction);
		toolbar.add(new Separator());
		toolbar.add(calcSalaryAction);
		toolbar.add(new Separator());
		toolbar.add(backupDatabaseAction);
		toolbar.add(restoreDatabaseAction);
		toolbar.add(new Separator());
		toolbar.add(openConsoleAction);
	}
}
