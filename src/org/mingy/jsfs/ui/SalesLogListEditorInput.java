package org.mingy.jsfs.ui;

import java.util.List;
import java.util.Set;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.mingy.jsfs.model.SalesLog;
import org.mingy.jsfs.model.SalesLogQueryCondition;
import org.mingy.kernel.util.Calendars;

public class SalesLogListEditorInput implements IEditorInput {

	private static SalesLogListEditorInput singleton;

	private SalesLogQueryCondition queryCondition;
	private List<SalesLog> salesLogList;
	private Set<String> lockedDays;

	public static SalesLogListEditorInput getInstance() {
		if (singleton == null) {
			singleton = new SalesLogListEditorInput();
		}
		return singleton;
	}

	@SuppressWarnings("unchecked")
	private SalesLogListEditorInput() {
		queryCondition = new SalesLogQueryCondition();
		queryCondition.setStartDate(Calendars.getMinTimeNow());
		queryCondition.setEndDate(Calendars.getMaxTimeNow());
		salesLogList = WritableList.withElementType(SalesLog.class);
		lockedDays = WritableSet.withElementType(String.class);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.isAssignableFrom(SalesLogQueryCondition.class)) {
			return queryCondition;
		} else if (adapter.isAssignableFrom(List.class)) {
			return salesLogList;
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
		return "销售记录 ("
				+ Calendars.get10Date(queryCondition.getStartDate())
				+ " - "
				+ Calendars.get10Date(queryCondition.getEndDate())
				+ ")"
				+ (queryCondition.getStaff() != null ? " - "
						+ queryCondition.getStaff().getName() : "");
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
