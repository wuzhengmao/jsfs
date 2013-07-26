package org.mingy.jsfs.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class SalesLogQueryCondition extends PropertyChangeSupportBean {

	@NotNull(message = "{startDate.NotNull}")
	private Date startDate;

	private Date endDate;

	private Staff staff;

	public void copyTo(SalesLogQueryCondition target) {
		target.setStartDate(startDate);
		target.setEndDate(endDate);
		target.setStaff(staff);
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		firePropertyChange("startDate", this.startDate,
				this.startDate = startDate);
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		firePropertyChange("endDate", this.endDate, this.endDate = endDate);
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		firePropertyChange("staff", this.staff, this.staff = staff);
	}
}
