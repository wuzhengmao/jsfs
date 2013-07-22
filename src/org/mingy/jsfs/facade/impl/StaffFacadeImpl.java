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
		if (entityDao.load(Staff.class, new Object[] { id, true },
				new String[] { "position.id", "valid" }) != null) {
			throw new ApplicationException(
					"error.delete_position.staffNotEmpty");
		} else if (entityDao.load(Staff.class, id, "position.id") != null) {
			entityDao.logicDelete(Position.class, id);
		} else {
			entityDao.delete(Position.class, id);
		}
	}

	public void setEntityDao(IEntityDaoFacade entityDao) {
		this.entityDao = entityDao;
	}
}
