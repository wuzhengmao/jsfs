package org.mingy.jsfs.facade.impl;

import org.mingy.jsfs.facade.IStaffFacade;
import org.mingy.jsfs.model.orm.Position;
import org.mingy.jsfs.model.orm.Staff;
import org.mingy.kernel.facade.IEntityDaoFacade;
import org.mingy.kernel.util.ApplicationException;

public class StaffFacadeImpl implements IStaffFacade {

	private IEntityDaoFacade entityDao;

	@Override
	public void deletePosition(Long id) {
		// TODO: error.delete_position.useInRule
		if (entityDao.load(Staff.class, new Object[] { id, true },
				new String[] { "position.id", "valid" }) != null) {
			throw new ApplicationException(
					"error.delete_position.staffNotEmpty");
		} else if (!entityDao.query(
				"SELECT e.id FROM RewardRule e WHERE e.positions.id = ?1",
				new Object[] { id }, true, 0, 1).isEmpty()) {
			throw new ApplicationException("error.delete_position.useInRule");
		} else if (entityDao.load(Staff.class, id, "position.id") != null) {
			entityDao.logicDelete(Position.class, id);
		} else {
			entityDao.delete(Position.class, id);
		}
	}

	@Override
	public void deleteStaff(Long id) {
		// TODO: 逻辑删除
		entityDao.delete(Staff.class, id);
	}

	public void setEntityDao(IEntityDaoFacade entityDao) {
		this.entityDao = entityDao;
	}
}
