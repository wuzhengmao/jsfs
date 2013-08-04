package org.mingy.jsfs.ui.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.databinding.conversion.Converter;

public class DateFormatConverter extends Converter {

	private String pattern;

	public DateFormatConverter(String pattern) {
		super(Date.class, String.class);
		this.pattern = pattern;
	}

	@Override
	public Object convert(Object fromObject) {
		return fromObject != null ? new SimpleDateFormat(pattern)
				.format((Date) fromObject) : null;
	}
}
