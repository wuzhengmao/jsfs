package org.mingy.jsfs.ui.model;

import org.eclipse.core.databinding.conversion.Converter;
import org.mingy.kernel.util.Langs;

public class ValueToLabelConverter extends Converter {

	private String prefix;

	public ValueToLabelConverter(String prefix) {
		super(Integer.class, String.class);
		this.prefix = prefix;
	}

	@Override
	public Object convert(Object fromObject) {
		return fromObject != null ? Langs
				.getLabel(prefix, (Integer) fromObject) : null;
	}
}
