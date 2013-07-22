package org.mingy.jsfs.ui.model;

import java.util.ArrayList;
import java.util.List;

import org.mingy.jsfs.model.orm.ICatalog;
import org.mingy.jsfs.model.orm.INamedObject;
import org.mingy.kernel.util.Langs;

public class Catalog extends PropertyChangeSupportBean {

	public static final int TYPE_ROOT = 0;
	public static final int TYPE_STAFF = 1;
	public static final int TYPE_GOODS = 2;
	public static final int TYPE_RULE = 3;
	public static final int TYPE_CATALOG = 90;
	public static final int TYPE_ITEM = 99;

	private int type;
	private Catalog parent;
	private List<Catalog> children = new ArrayList<Catalog>();

	private Object value;

	public Catalog(int type) {
		this.type = type;
	}

	public Catalog(Object value) {
		this.type = value instanceof ICatalog ? TYPE_CATALOG : TYPE_ITEM;
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		String oldLabel = getLabel();
		this.value = value;
		firePropertyChange("label", oldLabel, getLabel());
	}

	public String getLabel() {
		if (isRoot()) {
			return Langs.getLabel("catalog.type", type);
		} else if (value instanceof INamedObject) {
			return ((INamedObject) value).getName();
		} else {
			return value != null ? value.toString() : null;
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

	public List<Catalog> getChildren() {
		return children;
	}

	public void setChildren(List<Catalog> children) {
		firePropertyChange("children", this.children, this.children = children);
	}
}
