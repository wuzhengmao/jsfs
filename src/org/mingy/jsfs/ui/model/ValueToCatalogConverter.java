package org.mingy.jsfs.ui.model;

import org.eclipse.core.databinding.conversion.Converter;
import org.mingy.jsfs.model.INamedObject;

public class ValueToCatalogConverter extends Converter {

	public ValueToCatalogConverter(Class<?> fromType) {
		super(fromType, Catalog.class);
	}

	@Override
	public Object convert(Object fromObject) {
		return fromObject != null ? new Catalog((INamedObject) fromObject)
				: null;
	}
}
