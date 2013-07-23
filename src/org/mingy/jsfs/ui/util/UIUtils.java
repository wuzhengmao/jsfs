package org.mingy.jsfs.ui.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.mingy.kernel.bean.LabelValueBean;
import org.mingy.kernel.util.Langs;

public abstract class UIUtils {

	public static void initCombo(ComboViewer combo, String prefix,
			Class<?> valueType) {
		combo.setContentProvider(new ArrayContentProvider());
		final Map<Object, String> map = new HashMap<Object, String>();
		final List<Object> values = new ArrayList<Object>();
		for (LabelValueBean bean : Langs.enumLabelValues(prefix)) {
			Object value = convert(bean.getValue(), valueType);
			map.put(value, bean.getLabel());
			values.add(value);
		}
		combo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return map.get(element);
			}
		});
		combo.setInput(values.toArray());
	}

	private static Object convert(String value, Class<?> valueType) {
		if (valueType == String.class) {
			return value;
		} else if (valueType == Boolean.class || valueType == boolean.class) {
			return Boolean.parseBoolean(value);
		} else if (valueType == Byte.class || valueType == byte.class) {
			return Byte.parseByte(value);
		} else if (valueType == Short.class || valueType == short.class) {
			return Short.parseShort(value);
		} else if (valueType == Integer.class || valueType == int.class) {
			return Integer.parseInt(value);
		} else if (valueType == Long.class || valueType == long.class) {
			return Long.parseLong(value);
		} else {
			throw new IllegalArgumentException("valueType error");
		}
	}
}
