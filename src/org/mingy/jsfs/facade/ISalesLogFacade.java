package org.mingy.jsfs.facade;

import java.util.List;

import org.mingy.jsfs.model.SalesLog;
import org.mingy.jsfs.model.SalesLogQueryCondition;

public interface ISalesLogFacade {

	List<SalesLog> querySalesLog(SalesLogQueryCondition queryCondition);

	void saveSalesLog(SalesLog salesLog);

	void deleteSalesLog(Long id);
}
