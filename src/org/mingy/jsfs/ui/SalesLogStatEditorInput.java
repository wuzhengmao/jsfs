package org.mingy.jsfs.ui;

import java.util.Date;
import java.util.Set;

import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.mingy.jsfs.model.SalesLogQueryCondition;
import org.mingy.jsfs.model.SalesLogStat;
import org.mingy.kernel.util.Calendars;

public class SalesLogStatEditorInput implements IEditorInput {

	private static SalesLogStatEditorInput singleton;

	private SalesLogQueryCondition queryCondition;
	private SalesLogStat salesLogStat;
	private Set<String> lockedDays;

	public static SalesLogStatEditorInput getInstance() {
		if (singleton == null) {
			singleton = new SalesLogStatEditorInput();
		}
		return singleton;
	}

	@SuppressWarnings("unchecked")
	private SalesLogStatEditorInput() {
		queryCondition = new SalesLogQueryCondition();
		queryCondition.setStartDate(Calendars.getMinTimeOfMonth(new Date()));
		queryCondition.setEndDate(Calendars.getMaxTimeOfMonth(new Date()));
		lockedDays = WritableSet.withElementType(String.class);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.isAssignableFrom(SalesLogQueryCondition.class)) {
			return queryCondition;
		} else if (adapter.isAssignableFrom(SalesLogStat.class)) {
			return salesLogStat;
		} else if (adapter.isAssignableFrom(Set.class)) {
			return lockedDays;
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
		return "销售统计 (" + Calendars.get10Date(queryCondition.getStartDate())
				+ " - " + Calendars.get10Date(queryCondition.getEndDate())
				+ ")";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "";
	}

	public void setSalesLogStat(SalesLogStat salesLogStat) {
		this.salesLogStat = salesLogStat;
	}
}
