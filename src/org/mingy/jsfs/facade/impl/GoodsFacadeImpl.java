package org.mingy.jsfs.facade.impl;

import java.util.ArrayList;
import java.util.List;

import org.mingy.jsfs.facade.IGoodsFacade;
import org.mingy.jsfs.model.Caches;
import org.mingy.jsfs.model.Goods;
import org.mingy.jsfs.model.GoodsType;
import org.mingy.jsfs.model.orm.GoodsEntity;
import org.mingy.jsfs.model.orm.GoodsTypeEntity;
import org.mingy.kernel.facade.IEntityDaoFacade;
import org.mingy.kernel.util.ApplicationException;

public class GoodsFacadeImpl implements IGoodsFacade {

	private IEntityDaoFacade entityDao;

	@Override
	public List<GoodsType> getGoodsTypes() {
		List<GoodsType> list = new ArrayList<GoodsType>();
		for (GoodsTypeEntity entity : entityDao.loadAll(GoodsTypeEntity.class,
				true)) {
			GoodsType goodsType = Caches.load(GoodsType.class, entity.getId());
			if (goodsType == null) {
				goodsType = new GoodsType();
				goodsType.setId(entity.getId());
				Caches.save(GoodsType.class, goodsType);
			}
			goodsType.setName(entity.getName());
			goodsType.setDescription(entity.getDescription());
			list.add(goodsType);
		}
		return list;
	}

	@Override
	public void saveGoodsType(GoodsType goodsType) {
		GoodsTypeEntity entity = null;
		if (goodsType.getId() != null) {
			entity = entityDao.load(GoodsTypeEntity.class, goodsType.getId());
		}
		if (entity == null) {
			entity = new GoodsTypeEntity();
		}
		entity.setId(goodsType.getId());
		entity.setName(goodsType.getName());
		entity.setDescription(goodsType.getDescription());
		entityDao.save(entity);
		goodsType.setId(entity.getId());
		Caches.save(GoodsType.class, goodsType);
	}

	@Override
	public void deleteGoodsType(Long id) {
		if (entityDao.load(GoodsEntity.class, new Object[] { id, true },
				new String[] { "type.id", "valid" }) != null) {
			throw new ApplicationException(
					"error.delete_goodsType.goodsNotEmpty");
		} else if (!entityDao
				.query("SELECT e.id FROM RewardRuleEntity e WHERE e.goodsType.id = ?1",
						new Object[] { id }, false, 0, 1).isEmpty()) {
			throw new ApplicationException("error.delete_goodsType.useInRule");
		} else if (entityDao.load(GoodsEntity.class, id, "type.id") != null) {
			entityDao.logicDelete(GoodsTypeEntity.class, id);
			Caches.remove(GoodsType.class, id);
		} else {
			entityDao.delete(GoodsTypeEntity.class, id);
			Caches.remove(GoodsType.class, id);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Goods> getGoods() {
		List<Goods> list = new ArrayList<Goods>();
		for (GoodsEntity entity : (List<GoodsEntity>) entityDao
				.query("SELECT e FROM GoodsEntity e WHERE e.valid = true ORDER BY e.type.id, e.id",
						(Object[]) null, false)) {
			Goods goods = Caches.load(Goods.class, entity.getId());
			if (goods == null) {
				goods = new Goods();
				goods.setId(entity.getId());
				Caches.save(Goods.class, goods);
			}
			goods.setType(EntityConverts.toGoodsType(entity.getType()));
			goods.setName(entity.getName());
			goods.setSalesPrice(entity.getSalesPrice());
			goods.setMemo(entity.getMemo());
			list.add(goods);
		}
		return list;
	}

	@Override
	public void saveGoods(Goods goods) {
		GoodsEntity entity = null;
		if (goods.getId() != null) {
			entity = entityDao.load(GoodsEntity.class, goods.getId());
		}
		if (entity == null) {
			entity = new GoodsEntity();
		}
		entity.setId(goods.getId());
		entity.setType(entityDao.load(GoodsTypeEntity.class, goods.getType()
				.getId()));
		entity.setName(goods.getName());
		entity.setSalesPrice(goods.getSalesPrice());
		entity.setMemo(goods.getMemo());
		entityDao.save(entity);
		goods.setId(entity.getId());
		Caches.save(Goods.class, goods);
	}

	@Override
	public void deleteGoods(Long id) {
		if (!entityDao.query(
				"SELECT e.id FROM RewardRuleEntity e WHERE e.goods.id = ?1",
				new Object[] { id }, false, 0, 1).isEmpty()) {
			throw new ApplicationException("error.delete_goods.useInRule");
		} else if (!entityDao
				.query("SELECT e.id FROM SalesLogDetailEntity e WHERE e.goods.id = ?1",
						new Object[] { id }, false, 0, 1).isEmpty()) {
			entityDao.logicDelete(GoodsEntity.class, id);
			Caches.remove(Goods.class, id);
		} else {
			entityDao.delete(GoodsEntity.class, id);
			Caches.remove(Goods.class, id);
		}
	}

	public void setEntityDao(IEntityDaoFacade entityDao) {
		this.entityDao = entityDao;
	}
}
