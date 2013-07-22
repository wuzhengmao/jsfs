package org.mingy.kernel.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 全局的基于Spring的对象容器的Bean对象获取类。 <br>
 * 此对象为一个单例类，只能通过getInstance获得实例。<br>
 * <br>
 * 要使用此类，首先要使用注入的setter，来设置spring使用的上下文环境，也即是告诉spring到什么地方装载配置。<br>
 * 这里有2种方法：<br>
 * 一种通过web容器来装载，那么要使用setServletContext方法。一般的，不需要调用者手工调用，直接在web.
 * xml中配置GlobalBeanContextListener就可以了。<br>
 * 另外一种是手工注入来装载，那么要使用setFileContext方法，在此方法中，需要将所有的spring配置文件加入到参数指定的数组中就可以了。 <br>
 * <br>
 * 例如：<br>
 * <br>
 * <i> String final [] FILE_CONTEXT = {"applicationContext.xml",
 * "applicationContextExample.xml"};<br>
 * GlobalBeanContext.getInstance().setFileContext(FILE_CONTEXT);<br>
 * <br>
 * Object obj = GlobalBeanContext.getInstance().getBean("example");<br>
 * </i>
 * <hr>
 * 在完成这些操作后，你就可以在任何需要的时候调用getBean来获得spring容器中的对象实例了。
 * 
 * @author Mingy
 * 
 */
public class GlobalBeanContext {

	private static final Log logger = LogFactory
			.getLog(GlobalBeanContext.class);
	private static GlobalBeanContext context = null;

	private ApplicationContext applicationContext = null;
	private String appRoot = System.getProperty("user.home");

	private GlobalBeanContext() {

	}

	/**
	 * 获得GlobalBeanContext对象实例。
	 * 
	 * @return GlobalBeanContext对象实例
	 */
	public static GlobalBeanContext getInstance() {
		if (context == null) {
			synchronized (GlobalBeanContext.class) {
				if (context == null) {
					context = new GlobalBeanContext();
				}
			}
		}
		return context;
	}

	/**
	 * 获取应用的根路径（对于Web应用而言，对应于“/”的真实路径），默认为“user.home”路径。
	 * 
	 * @see GlobalBeanContext#setWebAppRoot(String)
	 * @see GlobalBeanContext#setServletContext(ServletContext)
	 */
	public String getAppRoot() {
		return appRoot;
	}

	/**
	 * 根据指定类型从spring容器中获取对象实例
	 * 
	 * @param <T>
	 *            指定的类型定义
	 * @param clazz
	 *            指定的类型
	 * @return 对象实例的map，如不存在返回空的map
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<String, T> getBeans(Class<T> clazz) {
		return applicationContext.getBeansOfType(clazz);
	}

	/**
	 * 根据指定类型从spring容器中获取对象实例
	 * 
	 * @param <T>
	 *            指定的类型定义
	 * @param clazz
	 *            指定的类型
	 * @return 对象实例，如果不存在，则返回null。
	 */
	public <T> T getBean(Class<T> clazz) {
		Map<String, T> map = getBeans(clazz);
		if (!map.isEmpty()) {
			T defaultObj = null;
			for (T obj : map.values()) {
				if (defaultObj == null) {
					defaultObj = obj;
				}
				if (clazz.getName().equals(obj.getClass().getName())) {
					return (T) obj;
				}
			}
			return (T) defaultObj;
		} else {
			return null;
		}
	}

	/**
	 * 从spring容器中获得指定名称的对象实例
	 * 
	 * @param beanName
	 *            指定对象的唯一标示名称
	 * @return 指定名称的对象实例
	 */
	public Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	/**
	 * 在容器中发布事件。
	 * 
	 * @param event
	 *            事件
	 */
	public void publishEvent(ApplicationEvent event) {
		applicationContext.publishEvent(event);
	}

	/**
	 * 注册一个虚拟机退出时安全关闭spring的钩子
	 */
	public void registerShutdownHook() {
		if (applicationContext instanceof AbstractApplicationContext) {
			((AbstractApplicationContext) applicationContext)
					.registerShutdownHook();
		}
	}

	/**
	 * 设置文件类型描述的spring容器上下文<br>
	 * 注意：一般地，此方法仅仅用于<b>web容器之外的注入情况</b>。
	 * 
	 * @param contextConfig
	 *            传入给spring容器的上下文配置信息，打包在jar中的配置
	 */
	public void loadClassPathContext(String[] contextConfig) {
		try {
			this.applicationContext = new ClassPathXmlApplicationContext(
					contextConfig);
			postLoaded();
		} catch (BeansException e) {
			if (logger.isErrorEnabled()) {
				logger.error("load application context failed", e);
			}
			throw e;
		}
	}

	/**
	 * 设置文件类型描述的spring容器上下文<br>
	 * 注意：一般地，此方法仅仅用于<b>web容器之外的注入情况</b>。
	 * 
	 * @param contextConfig
	 *            传入给spring容器的上下文配置信息
	 */
	public void loadFileContext(String[] contextConfig) {
		try {
			this.applicationContext = new FileSystemXmlApplicationContext(
					contextConfig);
			postLoaded();
		} catch (BeansException e) {
			if (logger.isErrorEnabled()) {
				logger.error("load application context failed", e);
			}
			throw e;
		}
	}

	/**
	 * 关闭spring容器上下文。
	 */
	public void shutdownContext() {
		if (applicationContext instanceof AbstractApplicationContext) {
			((AbstractApplicationContext) applicationContext).close();
		}
	}

	private void postLoaded() {
		Map<String, OnContextLoadedListener> map = getBeans(OnContextLoadedListener.class);
		List<OnContextLoadedListener> list = new ArrayList<OnContextLoadedListener>(
				map.values());
		Collections.sort(list, new Comparator<OnContextLoadedListener>() {
			@Override
			public int compare(OnContextLoadedListener o1,
					OnContextLoadedListener o2) {
				return o1.getOrder() - o2.getOrder();
			}
		});
		for (OnContextLoadedListener listener : list)
			listener.onContextLoaded(this);
	}
}
