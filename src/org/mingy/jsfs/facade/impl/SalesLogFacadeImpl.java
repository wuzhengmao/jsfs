package org.mingy.jsfs.facade.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.mingy.jsfs.facade.IConfigFacade;
import org.mingy.jsfs.facade.ISalesLogFacade;
import org.mingy.jsfs.model.SalesLog;
import org.mingy.jsfs.model.SalesLog.SalesLogDetail;
import org.mingy.jsfs.model.SalesLogLockCondition;
import org.mingy.jsfs.model.SalesLogQueryCondition;
import org.mingy.jsfs.model.SalesLogStat;
import org.mingy.jsfs.model.orm.GoodsEntity;
import org.mingy.jsfs.model.orm.SalesLogDetailEntity;
import org.mingy.jsfs.model.orm.SalesLogEntity;
import org.mingy.jsfs.model.orm.StaffEntity;
import org.mingy.kernel.facade.IEntityDaoFacade;
import org.mingy.kernel.util.Calendars;

import com.ibm.icu.util.Calendar;

public class SalesLogFacadeImpl implements ISalesLogFacade {

	private IEntityDaoFacade entityDao;
	private IConfigFacade configFacade;

	@SuppressWarnings("unchecked")
	@Override
	public List<SalesLog> querySalesLog(SalesLogQueryCondition queryCondition) {
		List<SalesLogEntity> list;
		if (queryCondition.getStaff() != null) {
			list = entityDao
					.query("SELECT e FROM SalesLogEntity e WHERE e.salesTime BETWEEN ?1 AND ?2 AND e.staff.id = ?3 ORDER BY e.salesTime",
							new Object[] { queryCondition.getStartDate(),
									queryCondition.getEndDate(),
									queryCondition.getStaff().getId() }, false);
		} else {
			list = entityDao
					.query("SELECT e FROM SalesLogEntity e WHERE e.salesTime BETWEEN ?1 AND ?2 ORDER BY e.salesTime",
							new Object[] { queryCondition.getStartDate(),
									queryCondition.getEndDate() }, false);
		}
		List<SalesLog> result = new ArrayList<SalesLog>(list.size());
		for (SalesLogEntity entity : list) {
			SalesLog salesLog = new SalesLog();
			salesLog.setId(entity.getId());
			salesLog.setSalesTime(entity.getSalesTime());
			salesLog.setStaff(EntityConverts.toStaff(entity.getStaff()));
			for (SalesLogDetailEntity detailEntity : entity.getDetails()) {
				SalesLogDetail detail = salesLog.new SalesLogDetail();
				detail.setId(detailEntity.getId());
				detail.setGoods(EntityConverts.toGoods(detailEntity.getGoods()));
				detail.setCount(detailEntity.getCount());
				detail.setTotalPrice(detailEntity.getTotalPrice());
				salesLog.getDetails().add(detail);
			}
			salesLog.setMemo(entity.getMemo());
			result.add(salesLog);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SalesLogStat statSalesLog(SalesLogQueryCondition queryCondition) {
		List<SalesLogEntity> list = entityDao
				.query("SELECT e FROM SalesLogEntity e WHERE e.salesTime BETWEEN ?1 AND ?2 ORDER BY e.staff.position.id, e.staff.id",
						new Object[] { queryCondition.getStartDate(),
								queryCondition.getEndDate() }, false);
		SalesLogStat stat = new SalesLogStat();
		for (SalesLogEntity entity : list) {
			double price = 0;
			for (SalesLogDetailEntity detailEntity : entity.getDetails()) {
				price += detailEntity.getTotalPrice();
			}
			stat.add(Calendars.getMinTime(entity.getSalesTime()),
					EntityConverts.toStaff(entity.getStaff()), price);
		}
		return stat;
	}

	@Override
	public void saveSalesLog(SalesLog salesLog) {
		SalesLogEntity entity = null;
		if (salesLog.getId() != null) {
			entity = entityDao.load(SalesLogEntity.class, salesLog.getId());
		}
		if (entity == null) {
			entity = new SalesLogEntity();
		}
		entity.setId(salesLog.getId());
		entity.setSalesTime(salesLog.getSalesTime());
		entity.setStaff(entityDao.load(StaffEntity.class, salesLog.getStaff()
				.getId()));
		entity.setMemo(salesLog.getMemo());
		entityDao.save(entity);
		salesLog.setId(entity.getId());
		List<SalesLogDetailEntity> list = entity.getDetails();
		if (list == null) {
			list = new ArrayList<SalesLogDetailEntity>();
			entity.setDetails(list);
		}
		for (Iterator<SalesLogDetailEntity> it = list.iterator(); it.hasNext();) {
			SalesLogDetailEntity detailEntity = it.next();
			boolean b = false;
			for (SalesLogDetail detail : salesLog.getDetails()) {
				if (detailEntity.getId().equals(detail.getId())) {
					b = true;
					break;
				}
			}
			if (!b) {
				entityDao.delete(detailEntity);
				it.remove();
			}
		}
		for (SalesLogDetail detail : salesLog.getDetails()) {
			SalesLogDetailEntity detailEntity = null;
			if (detail.getId() != null) {
				for (SalesLogDetailEntity e : list) {
					if (detail.getId().equals(e.getId())) {
						detailEntity = e;
						break;
					}
				}
			}
			if (detailEntity == null) {
				detailEntity = new SalesLogDetailEntity();
				list.add(detailEntity);
			}
			detailEntity.setId(detail.getId());
			detailEntity.setSalesLog(entity);
			detailEntity.setGoods(entityDao.load(GoodsEntity.class, detail
					.getGoods().getId()));
			detailEntity.setCount(detail.getCount());
			detailEntity.setTotalPrice(detail.getTotalPrice());
			entityDao.save(detailEntity);
			detail.setId(detailEntity.getId());
		}
	}

	@Override
	public void deleteSalesLog(Long id) {
		SalesLogEntity entity = entityDao.load(SalesLogEntity.class, id);
		if (entity != null) {
			for (SalesLogDetailEntity detailEntity : entity.getDetails()) {
				entityDao.delete(detailEntity);
			}
			entityDao.delete(entity);
		}
	}

	@Override
	public void lockSalesLog(SalesLogLockCondition condition) {
		Date date = condition.getStartDate();
		while (date.compareTo(condition.getEndDate()) < 0) {
			configFacade.saveConfig(Calendars.get10Date(date), "LOCK");
			date = Calendars.calculate(date, Calendar.DATE, 1);
		}
	}

	@Override
	public boolean isLocked(Date date) {
		return "LOCK".equals(configFacade.getConfig(Calendars.get10Date(date)));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> queryLockedDays(Date startDate, Date endDate) {
		List<String> list = (List<String>) entityDao
				.query("SELECT e.id FROM ConfigEntity e WHERE e.value = 'LOCK' AND e.id between ?1 AND ?2",
						new Object[] { Calendars.get10Date(startDate),
								Calendars.get10Date(endDate) }, false);
		return new HashSet<String>(list);
	}

	public void setEntityDao(IEntityDaoFacade entityDao) {
		this.entityDao = entityDao;
	}

	public void setConfigFacade(IConfigFacade configFacade) {
		this.configFacade = configFacade;
	}
}
