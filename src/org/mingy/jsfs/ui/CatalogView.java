package org.mingy.jsfs.ui;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.DecoratingObservableList;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
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
import org.mingy.jsfs.facade.IStaffFacade;
import org.mingy.jsfs.model.Goods;
import org.mingy.jsfs.model.GoodsType;
import org.mingy.jsfs.model.Position;
import org.mingy.jsfs.model.Staff;
import org.mingy.jsfs.ui.model.Catalog;
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
	private IStaffFacade staffFacade = GlobalBeanContext.getInstance().getBean(
			IStaffFacade.class);
	private IGoodsFacade goodsFacade = GlobalBeanContext.getInstance().getBean(
			IGoodsFacade.class);
	private static Catalog root;
	private static Map<Object, Catalog> catalogs;
	private static boolean inited = false;

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
		treeViewer.setAutoExpandLevel(2);
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				Catalog catalog = getSelectedItem();
				if (catalog != null) {
					addAction.setEnabled(catalog.isRoot() || catalog.isSub());
					editAction.setEnabled(!catalog.isRoot());
					deleteAction.setEnabled(!catalog.isRoot());
				} else {
					addAction.setEnabled(false);
					editAction.setEnabled(false);
					deleteAction.setEnabled(false);
				}
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
				case Catalog.TYPE_CATALOG:
					try {
						switch (catalog.getParent().getType()) {
						case Catalog.TYPE_STAFF:
							getSite()
									.getWorkbenchWindow()
									.getActivePage()
									.openEditor(
											new CatalogEditorInput(catalog),
											StaffEditor.ID);
							break;
						case Catalog.TYPE_GOODS:
							getSite()
									.getWorkbenchWindow()
									.getActivePage()
									.openEditor(
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
					switch (catalog.getParent().getType()) {
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
						switch (catalog.getParent().getParent().getType()) {
						case Catalog.TYPE_STAFF:
							getSite()
									.getWorkbenchWindow()
									.getActivePage()
									.openEditor(
											new CatalogEditorInput(catalog),
											StaffEditor.ID);
							break;
						case Catalog.TYPE_GOODS:
							getSite()
									.getWorkbenchWindow()
									.getActivePage()
									.openEditor(
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
					switch (catalog.getParent().getType()) {
					case Catalog.TYPE_STAFF:
						deletePosition(catalog);
						break;
					case Catalog.TYPE_GOODS:
						deleteGoodsType(catalog);
						break;
					}
					break;
				case Catalog.TYPE_ITEM:
					switch (catalog.getParent().getParent().getType()) {
					case Catalog.TYPE_STAFF:
						deleteStaff(catalog);
						break;
					case Catalog.TYPE_GOODS:
						deleteGoods(catalog);
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
				catalog.getParent().getChildren().remove(catalog);
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
				catalog.getParent().getChildren().remove(catalog);
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
				catalog.getParent().getChildren().remove(catalog);
				IEditorPart editor = getSite().getWorkbenchWindow()
						.getActivePage()
						.findEditor(new CatalogEditorInput(catalog));
				if (editor != null) {
					getSite().getWorkbenchWindow().getActivePage()
							.closeEditor(editor, false);
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
				catalog.getParent().getChildren().remove(catalog);
				IEditorPart editor = getSite().getWorkbenchWindow()
						.getActivePage()
						.findEditor(new CatalogEditorInput(catalog));
				if (editor != null) {
					getSite().getWorkbenchWindow().getActivePage()
							.closeEditor(editor, false);
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

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	private void loadTree(boolean forceLoad) {
		if (forceLoad || !inited) {
			loadAll();
			inited = true;
		}
		treeViewer.setInput(root);
		for (IEditorReference editorRef : getSite().getWorkbenchWindow()
				.getActivePage().getEditorReferences()) {
			IEditorPart editor = editorRef.getEditor(true);
			if (editor instanceof AbstractFormEditor) {
				Catalog catalog = (Catalog) editor.getEditorInput().getAdapter(
						Catalog.class);
				Catalog newCatalog = catalogs.get(catalog.getValue());
				if (newCatalog == null) {
					getSite().getWorkbenchWindow().getActivePage()
							.closeEditor(editor, false);
				} else {
					((AbstractFormEditor<?>) editor)
							.init(new CatalogEditorInput(newCatalog));
				}
			}
		}
	}

	private void loadAll() {
		root = new Catalog(Catalog.TYPE_ROOT);
		Catalog staffCatalog = new Catalog(Catalog.TYPE_STAFF);
		staffCatalog.setParent(root);
		root.getChildren().add(staffCatalog);
		Catalog goodsCatalog = new Catalog(Catalog.TYPE_GOODS);
		goodsCatalog.setParent(root);
		root.getChildren().add(goodsCatalog);
		Catalog ruleCatalog = new Catalog(Catalog.TYPE_RULE);
		ruleCatalog.setParent(root);
		root.getChildren().add(ruleCatalog);
		catalogs = new HashMap<Object, Catalog>();
		loadStaffs(staffCatalog);
		loadGoods(goodsCatalog);
	}

	private void loadStaffs(Catalog parent) {
		for (Position position : staffFacade.getPositions()) {
			Catalog catalog = new Catalog(position);
			catalog.setParent(parent);
			parent.getChildren().add(catalog);
		}
		for (Staff staff : staffFacade.getStaffs()) {
			for (Catalog catalog : parent.getChildren()) {
				if (catalog.getValue().equals(staff.getPosition())) {
					Catalog child = new Catalog(staff);
					child.setParent(catalog);
					catalog.getChildren().add(child);
					catalogs.put(staff, child);
					break;
				}
			}
		}
	}

	private void loadGoods(Catalog parent) {
		for (GoodsType goodsType : goodsFacade.getGoodsTypes()) {
			Catalog catalog = new Catalog(goodsType);
			catalog.setParent(parent);
			parent.getChildren().add(catalog);
		}
		for (Goods goods : goodsFacade.getGoods()) {
			for (Catalog catalog : parent.getChildren()) {
				if (catalog.getValue().equals(goods.getType())) {
					Catalog child = new Catalog(goods);
					child.setParent(catalog);
					catalog.getChildren().add(child);
					catalogs.put(goods, child);
					break;
				}
			}
		}
	}
}
