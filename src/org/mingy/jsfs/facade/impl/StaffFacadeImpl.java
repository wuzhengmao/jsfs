package org.mingy.jsfs.facade.impl;

import java.util.ArrayList;
import java.util.List;

import org.mingy.jsfs.facade.IStaffFacade;
import org.mingy.jsfs.model.Caches;
import org.mingy.jsfs.model.Position;
import org.mingy.jsfs.model.Staff;
import org.mingy.jsfs.model.orm.PositionEntity;
import org.mingy.jsfs.model.orm.StaffEntity;
import org.mingy.kernel.facade.IEntityDaoFacade;
import org.mingy.kernel.util.ApplicationException;

public class StaffFacadeImpl implements IStaffFacade {

	private IEntityDaoFacade entityDao;

	@Override
	public List<Position> getPositions() {
		List<Position> list = new ArrayList<Position>();
		for (PositionEntity entity : entityDao.loadAll(PositionEntity.class,
				true)) {
			Position position = Caches.load(Position.class, entity.getId());
			if (position == null) {
				position = new Position();
				position.setId(entity.getId());
				Caches.save(Position.class, position);
			}
			position.setName(entity.getName());
			position.setDescription(entity.getDescription());
			list.add(position);
		}
		return list;
	}

	@Override
	public void savePosition(Position position) {
		PositionEntity entity = null;
		if (position.getId() != null) {
			entity = entityDao.load(PositionEntity.class, position.getId());
		}
		if (entity == null) {
			entity = new PositionEntity();
		}
		entity.setId(position.getId());
		entity.setName(position.getName());
		entity.setDescription(position.getDescription());
		entityDao.save(entity);
		position.setId(entity.getId());
		Caches.save(Position.class, position);
	}

	@Override
	public void deletePosition(Long id) {
		if (entityDao.load(StaffEntity.class, new Object[] { id, true },
				new String[] { "position.id", "valid" }) != null) {
			throw new ApplicationException(
					"error.delete_position.staffNotEmpty");
		} else if (!entityDao
				.query("SELECT e.id FROM RewardRuleEntity e, IN (e.positions) p WHERE p.id = ?1",
						new Object[] { id }, false, 0, 1).isEmpty()) {
			throw new ApplicationException("error.delete_position.useInRule");
		} else if (entityDao.load(StaffEntity.class, id, "position.id") != null) {
			entityDao.logicDelete(PositionEntity.class, id);
			Caches.remove(Position.class, id);
		} else {
			entityDao.delete(PositionEntity.class, id);
			Caches.remove(Position.class, id);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Staff> getStaffs() {
		List<Staff> list = new ArrayList<Staff>();
		for (StaffEntity entity : (List<StaffEntity>) entityDao
				.query("SELECT e FROM StaffEntity e WHERE e.valid = true ORDER BY e.position.id, e.id",
						(Object[]) null, false)) {
			Staff staff = Caches.load(Staff.class, entity.getId());
			if (staff == null) {
				staff = new Staff();
				staff.setId(entity.getId());
				Caches.save(Staff.class, staff);
			}
			staff.setName(entity.getName());
			staff.setSex(entity.getSex());
			staff.setBirthday(entity.getBirthday());
			staff.setContacts(entity.getContacts());
			staff.setPosition(toPosition(entity.getPosition()));
			staff.setMemo(entity.getMemo());
			list.add(staff);
		}
		return list;
	}

	@Override
	public void saveStaff(Staff staff) {
		StaffEntity entity = null;
		if (staff.getId() != null) {
			entity = entityDao.load(StaffEntity.class, staff.getId());
		}
		if (entity == null) {
			entity = new StaffEntity();
		}
		entity.setId(staff.getId());
		entity.setName(staff.getName());
		entity.setSex(staff.getSex());
		entity.setBirthday(staff.getBirthday());
		entity.setContacts(staff.getContacts());
		entity.setPosition(entityDao.load(PositionEntity.class, staff
				.getPosition().getId()));
		entity.setMemo(staff.getMemo());
		entityDao.save(entity);
		staff.setId(entity.getId());
		Caches.save(Staff.class, staff);
	}

	private static Position toPosition(PositionEntity entity) {
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

	@Override
	public void deleteStaff(Long id) {
		if (!entityDao.query(
				"SELECT e.id FROM SalesLogEntity e WHERE e.staff.id = ?1",
				new Object[] { id }, false, 0, 1).isEmpty()) {
			entityDao.logicDelete(StaffEntity.class, id);
			Caches.remove(Staff.class, id);
		} else {
			entityDao.delete(StaffEntity.class, id);
			Caches.remove(Staff.class, id);
		}
	}

	public void setEntityDao(IEntityDaoFacade entityDao) {
		this.entityDao = entityDao;
	}
}
