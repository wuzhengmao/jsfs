package org.mingy.jsfs.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class CalcSalaryCondition extends PropertyChangeSupportBean {

	@NotNull(message = "{month.NotNull}")
	private Date month;

	public void copyTo(CalcSalaryCondition target) {
		target.setMonth(month);
	}

	public Date getMonth() {
		return month;
	}

	public void setMonth(Date month) {
		firePropertyChange("month", this.month, this.month = month);
	}
}
