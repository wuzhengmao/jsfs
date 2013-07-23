package org.mingy.jsfs.ui;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.mingy.jsfs.model.orm.GoodsType;
import org.mingy.jsfs.ui.model.Catalog;
import org.mingy.kernel.context.GlobalBeanContext;
import org.mingy.kernel.facade.IEntityDaoFacade;
import org.mingy.kernel.util.Langs;

public class GoodsTypeEditDialog extends CatalogEditDialog {

	private IEntityDaoFacade entityDao = GlobalBeanContext.getInstance()
			.getBean(IEntityDaoFacade.class);
	private Catalog catalog;
	private GoodsType goodsType;
	private boolean editMode;

	public GoodsTypeEditDialog(Shell parentShell, Catalog catalog) {
		super(parentShell);
		this.goodsType = new GoodsType();
		if (catalog.isRoot()) {
			this.catalog = new Catalog(goodsType);
			this.catalog.setParent(catalog);
		} else {
			this.catalog = catalog;
			try {
				BeanUtils.copyProperties(this.goodsType, catalog.getValue());
			} catch (Exception e) {
				throw new RuntimeException("error on clone bean", e);
			}
		}
		this.editMode = !catalog.isRoot();
	}

	@Override
	protected String getDialogTitle() {
		return editMode ? Langs.getText("edit_goodsType.dialog.title") : Langs
				.getText("new_goodsType.dialog.title");
	}

	@Override
	protected String getDialogMessage() {
		return editMode ? Langs.getText("edit_goodsType.dialog.message")
				: Langs.getText("new_goodsType.dialog.message");
	}

	@Override
	protected void initDataBindings() {
		bindText(txtName, goodsType, "name");
		bindText(txtDescription, goodsType, "description");
	}

	@Override
	protected boolean onOk() {
		try {
			entityDao.save(goodsType);
		} catch (Exception e) {
			MessageDialog.openError(
					getShell(),
					Langs.getText("error.save.title"),
					Langs.getText("error.save.message", e.getClass().getName()
							+ ": " + e.getLocalizedMessage()));
			return false;
		}
		catalog.setValue(goodsType);
		if (!editMode) {
			catalog.getParent().getChildren().add(catalog);
		}
		return true;
	}
}
