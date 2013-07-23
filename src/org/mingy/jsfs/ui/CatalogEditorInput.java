package org.mingy.jsfs.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.mingy.jsfs.ui.model.Catalog;

public class CatalogEditorInput implements IEditorInput {

	private Catalog catalog;

	public CatalogEditorInput(Catalog catalog) {
		if (catalog.getType() == Catalog.TYPE_ITEM) {
			this.catalog = catalog;
		} else {
			this.catalog = new Catalog(null);
			this.catalog.setParent(catalog);
		}
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof CatalogEditorInput ? catalog
				.equals(((CatalogEditorInput) obj).catalog) : false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.isAssignableFrom(Catalog.class)) {
			return catalog;
		} else {
			return catalog.getValue();
		}
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "";
	}
}
