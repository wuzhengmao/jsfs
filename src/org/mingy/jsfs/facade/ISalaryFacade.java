package org.mingy.jsfs.facade;

import java.util.List;

import org.mingy.jsfs.model.CalcSalaryCondition;
import org.mingy.jsfs.model.Salary;

public interface ISalaryFacade {

	List<Salary> calculate(CalcSalaryCondition condition);

}
