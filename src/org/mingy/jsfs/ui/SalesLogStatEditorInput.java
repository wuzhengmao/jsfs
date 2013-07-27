package org.mingy.jsfs.ui;

import java.util.Date;

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

	public static SalesLogStatEditorInput getInstance() {
		if (singleton == null) {
			singleton = new SalesLogStatEditorInput();
		}
		return singleton;
	}

	private SalesLogStatEditorInput() {
		queryCondition = new SalesLogQueryCondition();
		queryCondition.setStartDate(Calendars.getMinTimeOfMonth(new Date()));
		queryCondition.setEndDate(Calendars.getMaxTimeOfMonth(new Date()));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.isAssignableFrom(SalesLogQueryCondition.class)) {
			return queryCondition;
		} else if (adapter.isAssignableFrom(SalesLogStat.class)) {
			return salesLogStat;
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
