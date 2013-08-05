package org.mingy.jsfs.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class SalesLogLockCondition {

	@NotNull(message = "{startDate.NotNull}")
	private Date startDate;

	@NotNull(message = "{endDate.NotNull}")
	private Date endDate;

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
}
