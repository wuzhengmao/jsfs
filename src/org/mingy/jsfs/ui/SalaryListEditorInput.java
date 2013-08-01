package org.mingy.jsfs.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.mingy.jsfs.model.CalcSalaryCondition;
import org.mingy.jsfs.model.Salary;
import org.mingy.kernel.util.Calendars;

public class SalaryListEditorInput implements IEditorInput {

	private static final Map<String, SalaryListEditorInput> instances = new HashMap<String, SalaryListEditorInput>();

	private CalcSalaryCondition condition;
	private List<Salary> salaryList;

	public static SalaryListEditorInput getInstance(
			CalcSalaryCondition condition) {
		String month = Calendars.get7Date(condition.getMonth());
		SalaryListEditorInput instance = instances.get(month);
		if (instance == null) {
			instance = new SalaryListEditorInput(condition);
			instances.put(month, instance);
		}
		return instance;
	}

	private SalaryListEditorInput(CalcSalaryCondition condition) {
		this.condition = condition;
		this.salaryList = new ArrayList<Salary>();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.isAssignableFrom(CalcSalaryCondition.class)) {
			return condition;
		} else if (adapter.isAssignableFrom(List.class)) {
			return salaryList;
		} else {
			return null;
		}
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return "工资计算结果 (" + Calendars.get7Date(condition.getMonth()) + ")";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "";
	}
}
