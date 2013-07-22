package org.mingy.jsfs.facade.impl;

import org.mingy.jsfs.facade.IGoodsFacade;
import org.mingy.jsfs.model.orm.Goods;
import org.mingy.jsfs.model.orm.GoodsType;
import org.mingy.kernel.facade.IEntityDaoFacade;
import org.mingy.kernel.util.ApplicationException;

public class GoodsFacadeImpl implements IGoodsFacade {

	private IEntityDaoFacade entityDao;

	@Override
	public void deleteGoodsType(Long id) {
		if (entityDao.load(Goods.class, new Object[] { id, true },
				new String[] { "type.id", "valid" }) != null) {
			throw new ApplicationException(
					"error.delete_goodsType.goodsNotEmpty");
		} else if (entityDao.load(Goods.class, id, "type.id") != null) {
			entityDao.logicDelete(GoodsType.class, id);
		} else {
			entityDao.delete(GoodsType.class, id);
		}
	}

	public void setEntityDao(IEntityDaoFacade entityDao) {
		this.entityDao = entityDao;
	}
}
