package org.mingy.jsfs.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.DecoratingObservableList;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.mingy.jsfs.Activator;
import org.mingy.jsfs.facade.IGoodsFacade;
import org.mingy.jsfs.facade.IRewardRuleFacade;
import org.mingy.jsfs.facade.IStaffFacade;
import org.mingy.jsfs.model.Goods;
import org.mingy.jsfs.model.GoodsType;
import org.mingy.jsfs.model.Position;
import org.mingy.jsfs.model.RewardRule;
import org.mingy.jsfs.model.Role;
import org.mingy.jsfs.model.RoleManager;
import org.mingy.jsfs.model.Staff;
import org.mingy.jsfs.ui.model.Catalog;
import org.mingy.jsfs.ui.model.Catalogs;
import org.mingy.jsfs.ui.util.ActionWrapper;
import org.mingy.kernel.context.GlobalBeanContext;
import org.mingy.kernel.util.ApplicationException;
import org.mingy.kernel.util.Langs;

public class CatalogView extends ViewPart {

	public static final String ID = "org.mingy.jsfs.ui.CatalogView"; //$NON-NLS-1$

	private static final Log logger = LogFactory.getLog(CatalogView.class);

	private TreeViewer treeViewer;
	private Action refreshAction;
	private Action addAction;
	private Action editAction;
	private Action deleteAction;

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		treeViewer = new TreeViewer(container, SWT.BORDER);
		ObservableListTreeContentProvider contentProvider = new ObservableListTreeContentProvider(
				new IObservableFactory() {
					@Override
					public IObservable createObservable(Object target) {
						return new DecoratingObservableList(
								((Catalog) target).getObservableChildren(),
								false);
					}
				}, null);
		treeViewer.setContentProvider(contentProvider);
		ObservableMapLabelProvider labelProvider = new ObservableMapLabelProvider(
				BeanProperties.value("label").observeDetail(
						contentProvider.getKnownElements())) {
			private Image folderImage = Activator.getImageDescriptor(
					"/icons/folder.gif").createImage();
			private Image fileImage = Activator.getImageDescriptor(
					"/icons/file.gif").createImage();

			@Override
			public Image getImage(Object element) {
				Catalog node = (Catalog) element;
				if (node.getType() != Catalog.TYPE_ITEM) {
					return folderImage;
				} else {
					return fileImage;
				}
			}

			@Override
			public void dispose() {
				folderImage.dispose();
				fileImage.dispose();
				super.dispose();
			}
		};
		treeViewer.setLabelProvider(labelProvider);
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				syncToolbarStatus();
			}
		});
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				Catalog node = getSelectedItem();
				if (node != null) {
					if (node.getType() != Catalog.TYPE_ITEM) {
						treeViewer.setExpandedState(node,
								!treeViewer.getExpandedState(node));
					} else {
						editAction.run();
					}
				}
			}
		});

		createActions();
		initializeToolBar();
		initializeMenu();

		loadTree(false);
	}

	private Catalog getSelectedItem() {
		ISelection selection = treeViewer.getSelection();
		if (selection.isEmpty()) {
			return null;
		} else if (selection instanceof IStructuredSelection) {
			return (Catalog) ((IStructuredSelection) selection)
					.getFirstElement();
		} else {
			return null;
		}
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
		refreshAction = new Action() {
			@Override
			public void run() {
				List<IEditorPart> editors = getDirtyEditors();
				if (!editors.isEmpty()) {
					if (MessageDialog.openConfirm(getSite().getShell(),
							Langs.getText("confirm.save.title"),
							Langs.getText("confirm.save.message.onRefresh"))) {
						for (IEditorPart editor : editors) {
							if (!getSite().getPage().saveEditor(editor, false)) {
								return;
							}
						}
					} else {
						return;
					}
				}
				loadTree(true);
			}
		};
		refreshAction.setImageDescriptor(Activator
				.getImageDescriptor("/icons/refresh.gif"));
		refreshAction.setToolTipText("刷新");

		addAction = new Action() {
			@Override
			public void run() {
				Catalog catalog = getSelectedItem();
				switch (catalog.getType()) {
				case Catalog.TYPE_STAFF:
					new PositionEditDialog(getSite().getShell(), catalog)
							.open();
					break;
				case Catalog.TYPE_GOODS:
					new GoodsTypeEditDialog(getSite().getShell(), catalog)
							.open();
					break;
				case Catalog.TYPE_RULE:
					try {
						getSite().getPage().openEditor(
								new CatalogEditorInput(catalog),
								RewardRuleEditor.ID);
					} catch (PartInitException e) {
						if (logger.isErrorEnabled()) {
							logger.error("error on open editor", e);
						}
						MessageDialog.openError(
								getSite().getShell(),
								"Error",
								"Error opening editor:"
										+ e.getLocalizedMessage());
					}
					break;
				case Catalog.TYPE_CATALOG:
					try {
						switch (catalog.getRoot().getType()) {
						case Catalog.TYPE_STAFF:
							getSite().getPage().openEditor(
									new CatalogEditorInput(catalog),
									StaffEditor.ID);
							break;
						case Catalog.TYPE_GOODS:
							getSite().getPage().openEditor(
									new CatalogEditorInput(catalog),
									GoodsEditor.ID);
							break;
						}
					} catch (PartInitException e) {
						if (logger.isErrorEnabled()) {
							logger.error("error on open editor", e);
						}
						MessageDialog.openError(
								getSite().getShell(),
								"Error",
								"Error opening editor:"
										+ e.getLocalizedMessage());
					}
					break;
				}
			}
		};
		addAction.setImageDescriptor(Activator
				.getImageDescriptor("/icons/add.gif"));
		addAction.setDisabledImageDescriptor(Activator
				.getImageDescriptor("/icons/add_disabled.gif"));
		addAction.setToolTipText("新增");
		addAction.setEnabled(false);

		editAction = new Action() {
			@Override
			public void run() {
				Catalog catalog = getSelectedItem();
				switch (catalog.getType()) {
				case Catalog.TYPE_CATALOG:
					switch (catalog.getRoot().getType()) {
					case Catalog.TYPE_STAFF:
						new PositionEditDialog(getSite().getShell(), catalog)
								.open();
						break;
					case Catalog.TYPE_GOODS:
						new GoodsTypeEditDialog(getSite().getShell(), catalog)
								.open();
						break;
					}
					break;
				case Catalog.TYPE_ITEM:
					try {
						switch (catalog.getRoot().getType()) {
						case Catalog.TYPE_STAFF:
							getSite()
									.getPage()
									.openEditor(
											new CatalogEditorInput(catalog),
											RoleManager.getInstance().getRole() == Role.ADMIN ? StaffEditor.ID
													: ViewStaffEditor.ID);
							break;
						case Catalog.TYPE_GOODS:
							getSite()
									.getPage()
									.openEditor(
											new CatalogEditorInput(catalog),
											RoleManager.getInstance().getRole() == Role.ADMIN ? GoodsEditor.ID
													: ViewGoodsEditor.ID);
							break;
						case Catalog.TYPE_RULE:
							getSite()
									.getPage()
									.openEditor(
											new CatalogEditorInput(catalog),
											RoleManager.getInstance().getRole() == Role.ADMIN ? RewardRuleEditor.ID
													: ViewRewardRuleEditor.ID);
							break;
						}
					} catch (PartInitException e) {
						if (logger.isErrorEnabled()) {
							logger.error("error on open editor", e);
						}
						MessageDialog.openError(
								getSite().getShell(),
								"Error",
								"Error opening editor:"
										+ e.getLocalizedMessage());
					}
					break;
				}
			}
		};
		editAction.setImageDescriptor(Activator
				.getImageDescriptor("/icons/edit.gif"));
		editAction.setDisabledImageDescriptor(Activator
				.getImageDescriptor("/icons/edit_disabled.gif"));
		editAction.setToolTipText("修改");
		editAction.setEnabled(false);

		deleteAction = new Action() {
			@Override
			public void run() {
				Catalog catalog = getSelectedItem();
				switch (catalog.getType()) {
				case Catalog.TYPE_CATALOG:
					switch (catalog.getRoot().getType()) {
					case Catalog.TYPE_STAFF:
						deletePosition(catalog);
						break;
					case Catalog.TYPE_GOODS:
						deleteGoodsType(catalog);
						break;
					}
					break;
				case Catalog.TYPE_ITEM:
					switch (catalog.getRoot().getType()) {
					case Catalog.TYPE_STAFF:
						deleteStaff(catalog);
						break;
					case Catalog.TYPE_GOODS:
						deleteGoods(catalog);
						break;
					case Catalog.TYPE_RULE:
						deleteRewardRule(catalog);
						break;
					}
					break;
				}
			}
		};
		deleteAction.setImageDescriptor(Activator
				.getImageDescriptor("/icons/delete.gif"));
		deleteAction.setDisabledImageDescriptor(Activator
				.getImageDescriptor("/icons/delete_disabled.gif"));
		deleteAction.setToolTipText("删除");
		deleteAction.setEnabled(false);
	}

	private boolean deletePosition(Catalog catalog) {
		Position position = (Position) catalog.getValue();
		if (MessageDialog.openConfirm(
				getSite().getShell(),
				Langs.getText("confirm.delete.title"),
				Langs.getText("confirm.delete_position.message",
						position.getName()))) {
			try {
				GlobalBeanContext.getInstance().getBean(IStaffFacade.class)
						.deletePosition(position.getId());
				Catalogs.removeCatalog(catalog, position);
				return true;
			} catch (Exception e) {
				handleExceptionOnDelete(e);
			}
		}
		return false;
	}

	private boolean deleteGoodsType(Catalog catalog) {
		GoodsType goodsType = (GoodsType) catalog.getValue();
		if (MessageDialog.openConfirm(
				getSite().getShell(),
				Langs.getText("confirm.delete.title"),
				Langs.getText("confirm.delete_goodsType.message",
						goodsType.getName()))) {
			try {
				GlobalBeanContext.getInstance().getBean(IGoodsFacade.class)
						.deleteGoodsType(goodsType.getId());
				Catalogs.removeCatalog(catalog, goodsType);
				return true;
			} catch (Exception e) {
				handleExceptionOnDelete(e);
			}
		}
		return false;
	}

	private boolean deleteStaff(Catalog catalog) {
		Staff staff = (Staff) catalog.getValue();
		if (MessageDialog.openConfirm(getSite().getShell(),
				Langs.getText("confirm.delete.title"),
				Langs.getText("confirm.delete_staff.message", staff.getName()))) {
			try {
				GlobalBeanContext.getInstance().getBean(IStaffFacade.class)
						.deleteStaff(staff.getId());
				Catalogs.removeCatalog(catalog, staff);
				IEditorPart editor = getSite().getPage().findEditor(
						new CatalogEditorInput(catalog));
				if (editor != null) {
					getSite().getPage().closeEditor(editor, false);
				}
				return true;
			} catch (Exception e) {
				handleExceptionOnDelete(e);
			}
		}
		return false;
	}

	private boolean deleteGoods(Catalog catalog) {
		Goods goods = (Goods) catalog.getValue();
		if (MessageDialog.openConfirm(getSite().getShell(),
				Langs.getText("confirm.delete.title"),
				Langs.getText("confirm.delete_goods.message", goods.getName()))) {
			try {
				GlobalBeanContext.getInstance().getBean(IGoodsFacade.class)
						.deleteGoods(goods.getId());
				Catalogs.removeCatalog(catalog, goods);
				IEditorPart editor = getSite().getPage().findEditor(
						new CatalogEditorInput(catalog));
				if (editor != null) {
					getSite().getPage().closeEditor(editor, false);
				}
				return true;
			} catch (Exception e) {
				handleExceptionOnDelete(e);
			}
		}
		return false;
	}

	private boolean deleteRewardRule(Catalog catalog) {
		RewardRule rule = (RewardRule) catalog.getValue();
		if (MessageDialog.openConfirm(
				getSite().getShell(),
				Langs.getText("confirm.delete.title"),
				Langs.getText("confirm.delete_rewardRule.message",
						rule.getName()))) {
			try {
				GlobalBeanContext.getInstance()
						.getBean(IRewardRuleFacade.class)
						.deleteRule(rule.getId());
				Catalogs.removeCatalog(catalog, rule);
				IEditorPart editor = getSite().getPage().findEditor(
						new CatalogEditorInput(catalog));
				if (editor != null) {
					getSite().getPage().closeEditor(editor, false);
				}
				return true;
			} catch (Exception e) {
				handleExceptionOnDelete(e);
			}
		}
		return false;
	}

	private void handleExceptionOnDelete(Exception e) {
		if (logger.isErrorEnabled() && !(e instanceof ApplicationException)) {
			logger.error("error on delete", e);
		}
		MessageDialog.openError(getSite().getShell(), Langs
				.getText("error.delete.title"), Langs.getText(
				"error.delete.message",
				(e instanceof ApplicationException ? "" : e.getClass()
						.getName() + ": ")
						+ e.getLocalizedMessage()));
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
		toolbarManager.add(refreshAction);
		toolbarManager.add(new Separator());
		toolbarManager.add(addAction);
		toolbarManager.add(editAction);
		toolbarManager.add(deleteAction);
	}

	void syncToolbarStatus() {
		Catalog catalog = getSelectedItem();
		if (catalog != null) {
			addAction.setEnabled(catalog.isRoot() || catalog.isSub());
			editAction.setEnabled(!catalog.isRoot()
					&& RoleManager.getInstance().getRole() == Role.ADMIN);
			deleteAction.setEnabled(!catalog.isRoot()
					&& RoleManager.getInstance().getRole() == Role.ADMIN);
		} else {
			addAction.setEnabled(false);
			editAction.setEnabled(false);
			deleteAction.setEnabled(false);
		}
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		final IAction addPositionAction = new ActionWrapper(addAction, "新增职位",
				null, null);
		final IAction addStaffAction = new ActionWrapper(addAction, "新增员工",
				null, null);
		final IAction addGoodsTypeAction = new ActionWrapper(addAction,
				"新增商品分类", null, null);
		final IAction addGoodsAction = new ActionWrapper(addAction, "新增商品",
				null, null);
		final IAction addRuleAction = new ActionWrapper(addAction, "新增提成规则",
				null, null);
		final IAction editPositionAction = new ActionWrapper(editAction,
				"修改职位", null, null);
		final IAction editStaffAction = new ActionWrapper(editAction, "修改员工",
				null, null);
		final IAction editGoodsTypeAction = new ActionWrapper(editAction,
				"修改商品分类", null, null);
		final IAction editGoodsAction = new ActionWrapper(editAction, "修改商品",
				null, null);
		final IAction editRuleAction = new ActionWrapper(editAction, "修改提成规则",
				null, null);
		final IAction delPositionAction = new ActionWrapper(deleteAction,
				"删除职位", null, null);
		final IAction delStaffAction = new ActionWrapper(deleteAction, "删除员工",
				null, null);
		final IAction delGoodsTypeAction = new ActionWrapper(deleteAction,
				"删除商品分类", null, null);
		final IAction delGoodsAction = new ActionWrapper(deleteAction, "删除商品",
				null, null);
		final IAction delRuleAction = new ActionWrapper(deleteAction, "删除提成规则",
				null, null);
		final Separator separator = new Separator();
		MenuManager menuManager = new MenuManager();
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				Catalog catalog = getSelectedItem();
				switch (catalog.getType()) {
				case Catalog.TYPE_STAFF:
					manager.add(addPositionAction);
					break;
				case Catalog.TYPE_GOODS:
					manager.add(addGoodsTypeAction);
					break;
				case Catalog.TYPE_RULE:
					manager.add(addRuleAction);
					break;
				case Catalog.TYPE_CATALOG:
					switch (catalog.getRoot().getType()) {
					case Catalog.TYPE_STAFF:
						manager.add(addStaffAction);
						manager.add(separator);
						manager.add(editPositionAction);
						manager.add(delPositionAction);
						break;
					case Catalog.TYPE_GOODS:
						manager.add(addGoodsAction);
						manager.add(separator);
						manager.add(editGoodsTypeAction);
						manager.add(delGoodsTypeAction);
						break;
					}
					break;
				case Catalog.TYPE_ITEM:
					switch (catalog.getRoot().getType()) {
					case Catalog.TYPE_STAFF:
						manager.add(editStaffAction);
						manager.add(delStaffAction);
						break;
					case Catalog.TYPE_GOODS:
						manager.add(editGoodsAction);
						manager.add(delGoodsAction);
						break;
					case Catalog.TYPE_RULE:
						manager.add(editRuleAction);
						manager.add(delRuleAction);
						break;
					}
					break;
				}
			}
		});
		treeViewer.getTree().setMenu(
				menuManager.createContextMenu(treeViewer.getTree()));
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	private List<IEditorPart> getEditors() {
		List<IEditorPart> list = new ArrayList<IEditorPart>();
		for (IEditorReference editorRef : getSite().getPage()
				.getEditorReferences()) {
			IEditorPart editor = editorRef.getEditor(true);
			if (editor instanceof AbstractFormEditor) {
				list.add(editor);
			}
		}
		return list;
	}

	private List<IEditorPart> getDirtyEditors() {
		List<IEditorPart> list = new ArrayList<IEditorPart>();
		for (IEditorPart editor : getSite().getPage().getDirtyEditors()) {
			if (editor instanceof AbstractFormEditor) {
				list.add(editor);
			}
		}
		return list;
	}

	private void loadTree(boolean forceLoad) {
		if (forceLoad || !Catalogs.isInited()) {
			Catalogs.loadAll();
		}
		treeViewer.setInput(Catalogs.getCatalog(Catalog.TYPE_ROOT));
		treeViewer.setExpandedState(Catalogs.getCatalog(Catalog.TYPE_STAFF),
				true);
		treeViewer.setExpandedState(Catalogs.getCatalog(Catalog.TYPE_GOODS),
				true);
		for (IEditorPart editor : getEditors()) {
			Catalog catalog = (Catalog) editor.getEditorInput().getAdapter(
					Catalog.class);
			Catalog newCatalog = Catalogs.getCatalog(catalog.getValue());
			if (newCatalog == null) {
				getSite().getPage().closeEditor(editor, false);
			} else {
				((AbstractFormEditor<?>) editor).init(new CatalogEditorInput(
						newCatalog));
			}
		}
	}
}
