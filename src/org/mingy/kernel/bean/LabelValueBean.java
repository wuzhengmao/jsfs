package org.mingy.kernel.bean;

public class LabelValueBean {

	private String label;
	private String value;

	public LabelValueBean(String label, String value) {
		this.label = label;
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public String getValue() {
		return value;
	}
}
