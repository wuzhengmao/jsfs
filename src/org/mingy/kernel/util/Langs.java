package org.mingy.kernel.util;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.mingy.kernel.bean.LabelValueBean;

public abstract class Langs {

	private static final ResourceBundle I18N;

	static {
		I18N = ResourceBundle.getBundle("i18n");
	}

	public static String getText(String key, Object... args) {
		try {
			String text = I18N.getString(key);
			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					String arg = args[i] != null ? args[i].toString() : "";
					text = text.replaceAll("\\{" + i + "\\}", arg);
				}
			}
			return text;
		} catch (MissingResourceException e) {
			return null;
		}
	}

	public static String getLabel(String prefix, int index) {
		return getText(prefix + ".label." + index);
	}

	public static String getValue(String prefix, int index) {
		return getText(prefix + ".value." + index);
	}

	public static List<String> enumValues(String prefix) {
		List<String> list = new ArrayList<String>();
		int i = 0;
		String label;
		while ((label = getLabel(prefix, i)) != null) {
			list.add(label);
			i++;
		}
		return list;
	}

	public static List<LabelValueBean> enumLabelValues(String prefix) {
		List<LabelValueBean> list = new ArrayList<LabelValueBean>();
		int i = 0;
		String label, value;
		while ((label = getLabel(prefix, i)) != null) {
			value = getValue(prefix, i);
			list.add(new LabelValueBean(label, value != null ? value : String
					.valueOf(i)));
			i++;
		}
		return list;
	}
}
