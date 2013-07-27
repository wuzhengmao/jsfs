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
			openConsoleAction = new OpenConsoleAction(window, "控制台(&L)");
			openConsoleAction.setToolTipText("打开控制台输出日志");
			register(openConsoleAction);
		}
	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager("文件(&F)",
				IWorkbenchActionConstants.M_FILE);
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
		menuBar.add(windowMenu);
		windowMenu.add(openCatalogAction);
		fileMenu.add(new Separator());
		windowMenu.add(inputSalesLogAction);
		windowMenu.add(querySalesLogAction);
		windowMenu.add(statSalesLogAction);
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
		toolbar.add(openConsoleAction);
	}
}
