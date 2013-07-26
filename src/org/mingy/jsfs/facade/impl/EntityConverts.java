package org.mingy.jsfs.facade.impl;

import org.mingy.jsfs.model.Caches;
import org.mingy.jsfs.model.Goods;
import org.mingy.jsfs.model.GoodsType;
import org.mingy.jsfs.model.Position;
import org.mingy.jsfs.model.Staff;
import org.mingy.jsfs.model.orm.GoodsEntity;
import org.mingy.jsfs.model.orm.GoodsTypeEntity;
import org.mingy.jsfs.model.orm.PositionEntity;
import org.mingy.jsfs.model.orm.StaffEntity;

public abstract class EntityConverts {

	public static Position toPosition(PositionEntity entity) {
		Position position = Caches.load(Position.class, entity.getId());
		if (position == null) {
			position = new Position();
			position.setId(entity.getId());
			position.setName(entity.getName());
			position.setDescription(entity.getDescription());
			Caches.save(Position.class, position);
		}
		return position;
	}

	public static Staff toStaff(StaffEntity entity) {
		Staff staff = Caches.load(Staff.class, entity.getId());
		if (staff == null) {
			staff = new Staff();
			staff.setId(entity.getId());
			staff.setName(entity.getName());
			staff.setSex(entity.getSex());
			staff.setBirthday(entity.getBirthday());
			staff.setContacts(entity.getContacts());
			staff.setPosition(toPosition(entity.getPosition()));
			staff.setMemo(entity.getMemo());
			Caches.save(Staff.class, staff);
		}
		return staff;
	}

	public static GoodsType toGoodsType(GoodsTypeEntity entity) {
		GoodsType goodsType = Caches.load(GoodsType.class, entity.getId());
		if (goodsType == null) {
			goodsType = new GoodsType();
			goodsType.setId(entity.getId());
			goodsType.setName(entity.getName());
			goodsType.setDescription(entity.getDescription());
			Caches.save(GoodsType.class, goodsType);
		}
		return goodsType;
	}

	public static Goods toGoods(GoodsEntity entity) {
		Goods goods = Caches.load(Goods.class, entity.getId());
		if (goods == null) {
			goods = new Goods();
			goods.setId(entity.getId());
			goods.setType(toGoodsType(entity.getType()));
			goods.setName(entity.getName());
			goods.setSalesPrice(entity.getSalesPrice());
			goods.setMemo(entity.getMemo());
			Caches.save(Goods.class, goods);
		}
		return goods;
	}
}
