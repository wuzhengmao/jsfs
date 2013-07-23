package org.mingy.jsfs.model;

import java.util.HashMap;
import java.util.Map;

public abstract class Caches {

	private static final Map<Class<?>, Map<Long, ICachable<?>>> globals;

	static {
		globals = new HashMap<Class<?>, Map<Long, ICachable<?>>>();
	}

	@SuppressWarnings("unchecked")
	public static <T extends ICachable<T>> T load(Class<T> clazz, Long id) {
		if (clazz == null) {
			throw new NullPointerException();
		}
		if (id == null) {
			return null;
		}
		Map<Long, ICachable<?>> beans = globals.get(clazz);
		if (beans == null) {
			return null;
		} else {
			return (T) beans.get(id);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends ICachable<T>> void save(Class<T> clazz, T bean) {
		if (clazz == null || bean == null || bean.getId() == null) {
			throw new NullPointerException();
		}
		Map<Long, ICachable<?>> beans = globals.get(clazz);
		if (beans == null) {
			beans = new HashMap<Long, ICachable<?>>();
			globals.put(clazz, beans);
		}
		T src = (T) beans.get(bean.getId());
		if (src != null) {
			bean.copyTo(src);
		} else {
			beans.put(bean.getId(), bean);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends ICachable<T>> T remove(Class<T> clazz, Long id) {
		if (clazz == null) {
			throw new NullPointerException();
		}
		if (id == null) {
			return null;
		}
		Map<Long, ICachable<?>> beans = globals.get(clazz);
		if (beans == null) {
			return null;
		} else {
			return (T) beans.remove(id);
		}
	}
}
