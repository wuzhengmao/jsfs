package org.mingy.kernel.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 用于通用的日期应用类
 * 
 * @author Mingy
 * 
 */
public class Calendars {

	/**
	 * 默认的构造函数
	 */
	private Calendars() {
		super();
	}

	/**
	 * 在一个指定的日期对象上进行日期计算
	 * 
	 * @param date
	 *            进行计算的基准日期对象
	 * @param field
	 *            Calendar对象中支持的字段类型，如：Calendar.DAY_OF_MONTH等
	 * @param amount
	 *            在基准日期上安装Calendar指定的字段类型进行计算的数量
	 * @return 计算好的日期对象
	 */
	public static Date calculate(Date date, int field, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * 时间格式转换函数，转换为指定格式的时间字符串。
	 * 
	 * @param date
	 *            待格式化的时间对象
	 * @param pattern
	 *            时间格式
	 * @return 输出的时间字符串
	 */
	public static String dateFormat(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 时间格式转换函数，转换为时间对象。
	 * 
	 * @param date
	 *            待解析的时间字符串
	 * @param pattern
	 *            时间格式
	 * @return 输出的时间对象
	 * @throws ParseException
	 */
	public static Date dateParse(String date, String pattern)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(date);
	}

	/**
	 * 将一种格式的时间字符串转换为另一种时间格式的字符串。
	 * 
	 * @param srcDate
	 *            源时间字符串
	 * @param srcPattern
	 *            源时间格式
	 * @param tarPattern
	 *            目标时间格式
	 * @return 转换后的目标时间字符串
	 * @throws ParseException
	 */
	public static String dateTransform(String srcDate, String srcPattern,
			String tarPattern) throws ParseException {
		if (srcDate == null) {
			return "";
		}
		SimpleDateFormat srcSdf = new SimpleDateFormat(srcPattern);
		Date date = srcSdf.parse(srcDate);
		SimpleDateFormat tarSdf = new SimpleDateFormat(tarPattern);
		return tarSdf.format(date);
	}

	/**
	 * 将指定的日历转换为10位长度的日期字串，日期格式为：yyyy-MM-dd。
	 * 
	 * @param cal
	 *            日历对象
	 * @return 日期字串
	 * @see #get10Date(Date)
	 */
	public static String get10Date(Calendar cal) {
		return get10Date(cal.getTime());
	}

	/**
	 * 将指定的日期转换为10位长度的日期字串，日期格式为：yyyy-MM-dd。
	 * 
	 * @param date
	 *            日期对象
	 * @return 日期字串
	 */
	public static String get10Date(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	/**
	 * 获得一个10位长度的当前日期字串
	 * 
	 * @return 日期字串
	 * @see #get10Date(Date)
	 */
	public static String get10DateNow() {
		return get10Date(new Date());
	}

	/**
	 * 将指定的日历转换为7位长度的日期字串，日期格式为：yyyy-MM。
	 * 
	 * @param cal
	 *            日历对象
	 * @return 日期字串
	 * @see #get7Date(Date)
	 */
	public static String get7Date(Calendar cal) {
		return get7Date(cal.getTime());
	}

	/**
	 * 将指定的日期转换为7位长度的日期字串，日期格式为：yyyy-MM。
	 * 
	 * @param date
	 *            日期对象
	 * @return 日期字串
	 */
	public static String get7Date(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(date);
	}

	/**
	 * 获得一个7位长度的当前日期字串
	 * 
	 * @return 日期字串
	 * @see #get7Date(Date)
	 */
	public static String get7Date() {
		return get7Date(new Date());
	}

	/**
	 * 将指定的日历转换为12位长度的时间字串，日期格式为：HH:mm:ss.SSS。
	 * 
	 * @param cal
	 *            日历对象
	 * @return 时间字串
	 * @see #get12Time(Date)
	 */
	public static String get12Time(Calendar cal) {
		return get12Time(cal.getTime());
	}

	/**
	 * 将指定的日期转换为12位长度的时间字串，日期格式为：HH:mm:ss.SSS。
	 * 
	 * @param date
	 *            日期对象
	 * @return 时间字串
	 */
	public static String get12Time(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
		return sdf.format(date);
	}

	/**
	 * 获得一个12位长度的当前时间字串
	 * 
	 * @return 时间字串
	 * @see #get12Time(Date)
	 */
	public static String get12TimeNow() {
		return get12Time(new Date());
	}

	/**
	 * 将指定的日历转换为16位长度的日期字串，日期格式为：yyyy-MM-dd HH:mm。
	 * 
	 * @param cal
	 *            日历对象
	 * @return 日期字串
	 * @see #get16Date(Date)
	 */
	public static String get16Date(Calendar cal) {
		return get16Date(cal.getTime());
	}

	/**
	 * 将指定的日期转换为16位长度的日期字串，日期格式为：yyyy-MM-dd HH:mm。
	 * 
	 * @param date
	 *            日期对象
	 * @return 日期字串
	 */
	public static String get16Date(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(date);
	}

	/**
	 * 获得一个16位长度的当前日期字串
	 * 
	 * @return 日期字串
	 * @see #get16Date(Date)
	 */
	public static String get16DateNow() {
		return get16Date(new Date());
	}

	/**
	 * 将指定的日历转换为19位长度的日期字串，日期格式为：yyyy-MM-dd HH:mm:ss。
	 * 
	 * @param cal
	 *            日历对象
	 * @return 日期字串
	 * @see #get19Date(Date)
	 */
	public static String get19Date(Calendar cal) {
		return get19Date(cal.getTime());
	}

	/**
	 * 将指定的日期转换为19位长度的日期字串，日期格式为：yyyy-MM-dd HH:mm:ss。
	 * 
	 * @param date
	 *            日期对象
	 * @return 日期字串
	 */
	public static String get19Date(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 获得一个19位长度的当前日期字串
	 * 
	 * @return 日期字串
	 * @see #get19Date(Date)
	 */
	public static String get19DateNow() {
		return get19Date(new Date());
	}

	/**
	 * 将指定的日历转换为23位长度的日期字串，日期格式为：yyyy-MM-dd HH:mm:ss.SSS。
	 * 
	 * @param cal
	 *            日历对象
	 * @return 日期字串
	 * @see #get23Date(Date)
	 */
	public static String get23Date(Calendar cal) {
		return get23Date(cal.getTime());
	}

	/**
	 * 将指定的日期转换为23位长度的日期字串，日期格式为：yyyy-MM-dd HH:mm:ss.SSS。
	 * 
	 * @param date
	 *            日期对象
	 * @return 日期字串
	 */
	public static String get23Date(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return sdf.format(date);
	}

	/**
	 * 获得一个23位长度的当前日期字串
	 * 
	 * @return 日期字串
	 * @see #get23Date(Date)
	 */
	public static String get23DateNow() {
		return get23Date(new Date());
	}

	/**
	 * 将指定的日历转换为5位长度的时间字串，日期格式为：HH:mm。
	 * 
	 * @param cal
	 *            日历对象
	 * @return 时间字串
	 * @see #get5Time(Date)
	 */
	public static String get5Time(Calendar cal) {
		return get5Time(cal.getTime());
	}

	/**
	 * 将指定的日期转换为5位长度的时间字串，日期格式为：HH:mm。
	 * 
	 * @param date
	 *            日期对象
	 * @return 时间字串
	 */
	public static String get5Time(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(date);
	}

	/**
	 * 获得一个5位长度的当前时间字串
	 * 
	 * @return 时间字串
	 * @see #get5Time(Date)
	 */
	public static String get5TimeNow() {
		return get5Time(new Date());
	}

	/**
	 * 将指定的日历转换为8位长度的时间字串，日期格式为：HH:mm:ss。
	 * 
	 * @param cal
	 *            日历对象
	 * @return 时间字串
	 * @see #get8Time(Date)
	 */
	public static String get8Time(Calendar cal) {
		return get8Time(cal.getTime());
	}

	/**
	 * 将指定的日期转换为8位长度的时间字串，日期格式为：HH:mm:ss。
	 * 
	 * @param date
	 *            日期对象
	 * @return 时间字串
	 */
	public static String get8Time(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 获得一个8位长度的当前时间字串
	 * 
	 * @return 时间字串
	 * @see #get8Time(Date)
	 */
	public static String get8TimeNow() {
		return get8Time(new Date());
	}

	/**
	 * 获取指定日期最大的时间，包括年月日时分秒和毫秒，如：2009/10/10 23:59:59.999
	 * 
	 * @param date
	 *            指定的日期对象
	 * @return 日期对象
	 */
	public static Date getMaxTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	/**
	 * 获取当天最大的时间，包括年月日时分秒和毫秒，如：2009/10/10 23:59:59.999
	 * 
	 * @return 日期对象
	 */
	public static Date getMaxTimeNow() {
		return getMaxTime(new Date());
	}

	/**
	 * 获取指定日期最小的时间，包括年月日，时分秒和毫秒均为0，如：2009/10/10 00:00:00.000
	 * 
	 * @param date
	 *            指定的日期对象
	 * @return 日期对象
	 */
	public static Date getMinTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取当天最小的时间，包括年月日，时分秒和毫秒均为0，如：2009/10/10 00:00:00.000
	 * 
	 * @return 日期对象
	 * @see #getMinTime(Date)
	 */
	public static Date getMinTimeNow() {
		return getMinTime(new Date());
	}

	/**
	 * 合并两个Date类型的数据为一个Date对象，一般是针对日期和时间的合并。
	 * 
	 * @param date
	 *            日期对象
	 * @param time
	 *            时间对象
	 * @return 包含日期和时间的Date对象
	 */
	public static Date merge(Date date, Date time) {
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}
		if (time != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(time);
			calendar.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, cal.get(Calendar.SECOND));
		}
		return calendar.getTime();
	}

	public static Date getMinTimeOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getMaxTimeOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}
}
