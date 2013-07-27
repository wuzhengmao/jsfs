package org.mingy.jsfs.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mingy.kernel.util.Calendars;

public class SalesLogStat {

	private Map<Date, Double> days = new LinkedHashMap<Date, Double>();
	private Map<Staff, Double> staffs = new LinkedHashMap<Staff, Double>();
	private Map<String, Double> statData = new HashMap<String, Double>();
	private Double summary = 0d;

	public void add(Date day, Staff staff, Double price) {
		String key = getKey(day, staff);
		Double value = statData.get(key);
		statData.put(key, value != null ? value + price : price);
		value = days.get(day);
		days.put(day, value != null ? value + price : price);
		value = staffs.get(staff);
		staffs.put(staff, value != null ? value + price : price);
		summary += price;
	}

	public Date[] getDays() {
		List<Date> list = new ArrayList<Date>(days.keySet());
		Collections.sort(list);
		return list.toArray(new Date[list.size()]);
	}

	public Staff[] getStaffs() {
		return staffs.keySet().toArray(new Staff[staffs.size()]);
	}

	public Double getStat(Date day, Staff staff) {
		return statData.get(getKey(day, staff));
	}

	public Double getStat(Date day) {
		return days.get(day);
	}

	public Double getStat(Staff staff) {
		return staffs.get(staff);
	}

	public Double getStat() {
		return summary;
	}

	private String getKey(Date day, Staff staff) {
		return Calendars.get10Date(day) + "," + staff.getId();
	}
}
