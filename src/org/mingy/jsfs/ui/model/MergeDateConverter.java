package org.mingy.jsfs.ui.model;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.observable.value.IObservableValue;

public class MergeDateConverter extends Converter {

	private IObservableValue observableValue;

	public MergeDateConverter(IObservableValue observableValue) {
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
				calendar.set(Calendar.YEAR, cal.get(Calendar.YEAR));
				calendar.set(Calendar.MONTH, cal.get(Calendar.MONTH));
				calendar.set(Calendar.DAY_OF_MONTH,
						cal.get(Calendar.DAY_OF_MONTH));
			}
			return calendar.getTime();
		} else {
			return fromObject;
		}
	}
}
