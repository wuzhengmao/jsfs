package org.mingy.jsfs.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.mingy.jsfs.facade.IStaffFacade;
import org.mingy.jsfs.model.Position;
import org.mingy.jsfs.ui.model.Catalog;
import org.mingy.kernel.context.GlobalBeanContext;
import org.mingy.kernel.util.Langs;

public class PositionEditDialog extends CatalogEditDialog {

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
			((Position) catalog.getValue()).copyTo(this.position);
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
			GlobalBeanContext.getInstance().getBean(IStaffFacade.class)
					.savePosition(position);
		} catch (Exception e) {
			MessageDialog.openError(
					getShell(),
					Langs.getText("error.save.title"),
					Langs.getText("error.save.message", e.getClass().getName()
							+ ": " + e.getLocalizedMessage()));
			return false;
		}
		position.copyTo((Position) catalog.getValue());
		if (!editMode) {
			catalog.getParent().getChildren().add(catalog);
		}
		return true;
	}
}
