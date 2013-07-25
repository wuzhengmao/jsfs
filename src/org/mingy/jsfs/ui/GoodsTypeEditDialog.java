package org.mingy.jsfs.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.mingy.jsfs.facade.IGoodsFacade;
import org.mingy.jsfs.model.GoodsType;
import org.mingy.jsfs.ui.model.Catalog;
import org.mingy.jsfs.ui.model.Catalogs;
import org.mingy.kernel.context.GlobalBeanContext;
import org.mingy.kernel.util.Langs;

public class GoodsTypeEditDialog extends CatalogEditDialog {

	private Catalog catalog;
	private GoodsType goodsType;
	private boolean editMode;

	public GoodsTypeEditDialog(Shell parentShell, Catalog catalog) {
		super(parentShell);
		this.goodsType = new GoodsType();
		if (catalog.isRoot()) {
			this.catalog = new Catalog(Catalog.TYPE_CATALOG);
			this.catalog.setParent(catalog);
		} else {
			this.catalog = catalog;
			((GoodsType) catalog.getValue()).copyTo(this.goodsType);
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
			GlobalBeanContext.getInstance().getBean(IGoodsFacade.class)
					.saveGoodsType(goodsType);
		} catch (Exception e) {
			MessageDialog.openError(
					getShell(),
					Langs.getText("error.save.title"),
					Langs.getText("error.save.message", e.getClass().getName()
							+ ": " + e.getLocalizedMessage()));
			return false;
		}
		Catalogs.updateCatalog(catalog, goodsType);
		return true;
	}
}
