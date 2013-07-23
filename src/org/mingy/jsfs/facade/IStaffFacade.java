package org.mingy.jsfs.facade;

import java.util.List;

import org.mingy.jsfs.model.Position;
import org.mingy.jsfs.model.Staff;

public interface IStaffFacade {

	List<Position> getPositions();

	void savePosition(Position position);

	void deletePosition(Long id);

	List<Staff> getStaffs();

	void saveStaff(Staff staff);

	void deleteStaff(Long id);
}
