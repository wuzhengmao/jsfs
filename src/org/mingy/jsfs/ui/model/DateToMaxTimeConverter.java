package org.mingy.jsfs.ui.model;

import java.util.Date;

import org.eclipse.core.databinding.conversion.Converter;
import org.mingy.kernel.util.Calendars;

public class DateToMaxTimeConverter extends Converter {

	public DateToMaxTimeConverter() {
		super(Date.class, Date.class);
	}

	@Override
	public Object convert(Object fromObject) {
		return fromObject != null ? Calendars.getMaxTime((Date) fromObject)
				: null;
	}
}
