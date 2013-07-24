package org.mingy.jsfs.ui.model;

import org.eclipse.core.databinding.conversion.Converter;

public class CatalogToValueConverter extends Converter {

	public CatalogToValueConverter(Class<?> toType) {
		super(Catalog.class, toType);
	}

	@Override
	public Object convert(Object fromObject) {
		return fromObject != null ? ((Catalog) fromObject).getValue() : null;
	}
}
