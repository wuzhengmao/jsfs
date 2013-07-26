package org.mingy.jsfs.facade.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mingy.jsfs.facade.ISalesLogFacade;
import org.mingy.jsfs.model.SalesLog;
import org.mingy.jsfs.model.SalesLog.SalesLogDetail;
import org.mingy.jsfs.model.orm.GoodsEntity;
import org.mingy.jsfs.model.orm.SalesLogDetailEntity;
import org.mingy.jsfs.model.orm.SalesLogEntity;
import org.mingy.jsfs.model.orm.StaffEntity;
import org.mingy.kernel.facade.IEntityDaoFacade;

public class SalesLogFacadeImpl implements ISalesLogFacade {

	private IEntityDaoFacade entityDao;

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

	public void setEntityDao(IEntityDaoFacade entityDao) {
		this.entityDao = entityDao;
	}
}
