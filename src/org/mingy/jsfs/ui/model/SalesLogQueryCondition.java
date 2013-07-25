package org.mingy.jsfs.ui.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.mingy.jsfs.model.Staff;

public class SalesLogQueryCondition {

	@NotNull(message = "{startDate.NotNull}")
	private Date startDate;

	private Date endDate;

	private Staff staff;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}
}
