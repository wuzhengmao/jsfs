package org.mingy.jsfs.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.mingy.jsfs.model.orm.Position;
import org.mingy.jsfs.ui.model.Catalog;
import org.mingy.kernel.context.GlobalBeanContext;
import org.mingy.kernel.facade.IEntityDaoFacade;
import org.mingy.kernel.util.Langs;

public class PositionEditDialog extends CatalogEditDialog {

	private IEntityDaoFacade entityDao = GlobalBeanContext.getInstance()
			.getBean(IEntityDaoFacade.class);
	private Catalog catalog;
	private Position position;
	private boolean editMode;

	public PositionEditDialog(Shell parentShell, Catalog catalog) {
		super(parentShell);
		this.position = new Position();
		if (catalog.isRoot()) {
			this.catalog = new Catalog(position);
			this.catalog.setParent(catalog);
		} else {
			this.catalog = catalog;
			try {
				BeanUtils.copyProperties(this.position, catalog.getValue());
			} catch (Exception e) {
				throw new RuntimeException("error on clone bean", e);
			}
		}
		this.editMode = !catalog.isRoot();
	}

	@Override
	protected String getDialogTitle() {
		return editMode ? Langs.getText("edit_position.dialog.title") : Langs
				.getText("new_position.dialog.title");
	}

	@Override
	protected String getDialogMessage() {
		return editMode ? Langs.getText("edit_position.dialog.message") : Langs
				.getText("new_position.dialog.message");
	}

	@Override
	protected void initDataBindings() {
		bindText(txtName, position, "name");
		bindText(txtDescription, position, "description");
	}

	@Override
	protected boolean onOk() {
		try {
			entityDao.save(position);
		} catch (Exception e) {
			MessageDialog.openError(
					getShell(),
					Langs.getText("error.save.title"),
					Langs.getText("error.save.message", e.getClass().getName()
							+ ": " + e.getLocalizedMessage()));
			return false;
		}
		catalog.setValue(position);
		if (!editMode) {
			List<Catalog> list = new ArrayList<Catalog>(catalog.getParent()
					.getChildren());
			list.add(catalog);
			catalog.getParent().setChildren(list);
		}
		return true;
	}
}
