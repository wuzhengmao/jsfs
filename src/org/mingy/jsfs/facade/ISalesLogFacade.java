package org.mingy.jsfs.facade;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.mingy.jsfs.model.SalesLog;
import org.mingy.jsfs.model.SalesLogLockCondition;
import org.mingy.jsfs.model.SalesLogQueryCondition;
import org.mingy.jsfs.model.SalesLogStat;

public interface ISalesLogFacade {

	List<SalesLog> querySalesLog(SalesLogQueryCondition queryCondition);

	SalesLogStat statSalesLog(SalesLogQueryCondition queryCondition);

	void saveSalesLog(SalesLog salesLog);

	void deleteSalesLog(Long id);

	void lockSalesLog(SalesLogLockCondition condition);

	boolean isLocked(Date date);

	Set<String> queryLockedDays(Date startDate, Date endDate);
}
