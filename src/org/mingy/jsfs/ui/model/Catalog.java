package org.mingy.jsfs.ui.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.mingy.jsfs.model.ICatalog;
import org.mingy.jsfs.model.INamedObject;
import org.mingy.jsfs.model.PropertyChangeSupportBean;
import org.mingy.kernel.util.Langs;

public class Catalog extends PropertyChangeSupportBean implements
		PropertyChangeListener {

	public static final int TYPE_ROOT = 0;
	public static final int TYPE_STAFF = 1;
	public static final int TYPE_GOODS = 2;
	public static final int TYPE_RULE = 3;
	public static final int TYPE_CATALOG = 90;
	public static final int TYPE_ITEM = 99;

	private int type;
	private Catalog parent;
	private IObservableList children = new WritableList();

	private INamedObject value;

	public Catalog(int type) {
		this.type = type;
	}

	public Catalog(INamedObject value) {
		this.type = value instanceof ICatalog ? TYPE_CATALOG : TYPE_ITEM;
		this.value = value;
		if (this.value != null) {
			this.value.addNameChangeListener(this);
		}
	}

	@Override
	public int hashCode() {
		return type * 31 + (value != null ? value.hashCode() : 0);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Catalog)) {
			return false;
		} else if (type != ((Catalog) obj).type) {
			return false;
		} else if (value == null && ((Catalog) obj).value == null) {
			return type == Catalog.TYPE_ITEM ? parent
					.equals(((Catalog) obj).parent) : true;
		} else {
			return value != null && value.equals(((Catalog) obj).value);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		firePropertyChange("label", evt.getOldValue(), evt.getNewValue());
	}

	public int getType() {
		return type;
	}

	public INamedObject getValue() {
		return value;
	}

	public void setValue(INamedObject value) {
		if (this.value != value) {
			if (this.value != null) {
				this.value.removeNameChangeListener(this);
			}
			String label = getLabel();
			this.value = value;
			if (this.value != null) {
				this.value.addNameChangeListener(this);
			}
			firePropertyChange("label", label, getLabel());
		}
	}

	public String getLabel() {
		if (isRoot()) {
			return Langs.getLabel("catalog.type", type);
		} else {
			return value != null ? value.getName() : null;
		}
	}

	public boolean isRoot() {
		return type != TYPE_CATALOG && type != TYPE_ITEM;
	}

	public boolean isSub() {
		return type == TYPE_CATALOG;
	}

	public Catalog getParent() {
		return parent;
	}

	public void setParent(Catalog parent) {
		firePropertyChange("parent", this.parent, this.parent = parent);
	}

	@SuppressWarnings("unchecked")
	public List<Catalog> getChildren() {
		return children;
	}

	public IObservableList getObservableChildren() {
		return children;
	}
}
