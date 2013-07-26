package org.mingy.jsfs.ui.model;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.observable.value.IObservableValue;

public class MergeTimeConverter extends Converter {

	private IObservableValue observableValue;

	public MergeTimeConverter(IObservableValue observableValue) {
		super(Date.class, Date.class);
		this.observableValue = observableValue;
	}

	@Override
	public Object convert(Object fromObject) {
		Date value = (Date) observableValue.getValue();
		Calendar calendar = Calendar.getInstance();
		if (value != null) {
			calendar.setTime(value);
			if (fromObject != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime((Date) fromObject);
				calendar.set(Calendar.HOUR_OF_DAY,
						cal.get(Calendar.HOUR_OF_DAY));
				calendar.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
				calendar.set(Calendar.SECOND, cal.get(Calendar.SECOND));
			}
			return calendar.getTime();
		} else {
			return fromObject;
		}
	}
}
