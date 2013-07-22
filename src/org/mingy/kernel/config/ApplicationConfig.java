package org.mingy.kernel.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mingy.kernel.thread.BaseThread;
import org.mingy.kernel.util.Strings;

/**
 * 应用级层面的通用properties或者xml的配置信息加载应用类<br>
 * 本应用类是线程安全的。<br>
 * 本类还支持周期性载入配置信息文件（通过设置等待时间实现）。
 * 
 * @author Mingy
 * 
 */
public class ApplicationConfig {
	/**
	 * 类路径载入定义前缀“classpath:”。
	 */
	public static final String TYPE_CLASSPATH = "classpath:";
	private static final Log logger = LogFactory
			.getLog(ApplicationConfig.class);
	private static ApplicationConfig appConfig = null;
	private ConcurrentMap<String, String> configs = null;
	private BaseThread refreshThread = null;

	/**
	 * 默认的构造函数
	 */
	private ApplicationConfig() {
		configs = new ConcurrentHashMap<String, String>();
	}

	/**
	 * 清除已经加载的配置信息
	 */
	public void clear() {
		if (refreshThread != null) {
			refreshThread.stop();
		}
		configs.clear();
	}

	@Override
	protected void finalize() throws Throwable {
		if (refreshThread != null) {
			refreshThread.stop();
		}
		configs.clear();
		super.finalize();
	}

	/**
	 * 获取指定key的配置信息
	 * 
	 * @param key
	 *            key
	 * @return 配置信息，如果该key不存在，则返回null。
	 */
	public String getConfig(String key) {
		return configs.get(key);
	}

	/**
	 * 获取指定key的配置信息，此配置信息为一个boolean值。
	 * 
	 * @param key
	 *            key
	 * @return 配置的boolean值，如果该值没有配置，则返回false。
	 */
	public boolean getConfigBoolean(String key) {
		return "true".equalsIgnoreCase(getConfig(key));
	}

	/**
	 * 获取指定key的配置信息，此配置信息为一个双精度浮点值。
	 * 
	 * @param key
	 *            key
	 * @return 配置的浮点值，如果该值没有配置，则抛出异常。
	 */
	public double getConfigDouble(String key) {
		String value = getConfig(key);
		if (Strings.isBlank(value)) {
			throw new NullPointerException("The [" + key + "] not be set.");
		}
		return Double.parseDouble(value);
	}

	/**
	 * 获取指定key的配置信息，此配置信息为一个双精度浮点值。
	 * 
	 * @param key
	 *            key
	 * @param defaultValue
	 *            没有配置时的默认浮点值。
	 * @return 配置的浮点值，如果该值没有配置，则返回传入的默认值。
	 */
	public double getConfigDouble(String key, double defaultValue) {
		String value = getConfig(key);
		if (Strings.isBlank(value)) {
			return defaultValue;
		} else {
			return Double.parseDouble(value);
		}
	}

	/**
	 * 获取指定key的配置信息，此配置信息为一个单精度浮点值。
	 * 
	 * @param key
	 *            key
	 * @return 配置的浮点值，如果该值没有配置，则抛出异常。
	 */
	public float getConfigFloat(String key) {
		String value = getConfig(key);
		if (Strings.isBlank(value)) {
			throw new NullPointerException("The [" + key + "] not be set.");
		}
		return Float.parseFloat(value);
	}

	/**
	 * 获取指定key的配置信息，此配置信息为一个单精度浮点值。
	 * 
	 * @param key
	 *            key
	 * @param defaultValue
	 *            没有配置时的默认浮点值。
	 * @return 配置的浮点值，如果该值没有配置，则返回传入的默认值。
	 */
	public float getConfigFloat(String key, float defaultValue) {
		String value = getConfig(key);
		if (Strings.isBlank(value)) {
			return defaultValue;
		} else {
			return Float.parseFloat(value);
		}
	}

	/**
	 * 获取指定key的配置信息，此配置信息为一个整数值(int)。
	 * 
	 * @param key
	 *            key
	 * @param radix
	 *            数制，2（二进制） | 8（八进制） | 10（十进制） | 16（十六进制）。
	 * @return 按照指定数制转换的配置整数值，如果该值没有配置，则抛出异常。
	 */
	public int getConfigInt(String key, int radix) {
		String value = getConfig(key);
		if (Strings.isBlank(value)) {
			throw new NullPointerException("The [" + key + "] not be set.");
		}
		return Integer.parseInt(value, radix);
	}

	/**
	 * 获取指定key的配置信息，此配置信息为一个整数值(int)。
	 * 
	 * @param key
	 *            key
	 * @param radix
	 *            数制，2（二进制） | 8（八进制） | 10（十进制） | 16（十六进制）。
	 * @param defaultValue
	 *            没有配置时的默认整数值。
	 * @return 按照指定数制转换的配置整数值，如果该值没有配置，则返回传入的默认值。
	 */
	public int getConfigInt(String key, int radix, int defaultValue) {
		String value = getConfig(key);
		if (Strings.isBlank(value)) {
			return defaultValue;
		} else {
			return Integer.parseInt(value, radix);
		}
	}

	/**
	 * 获取指定key的配置信息，此配置信息为一个整数值(long)。
	 * 
	 * @param key
	 *            key
	 * @param radix
	 *            数制，2（二进制） | 8（八进制） | 10（十进制） | 16（十六进制）。
	 * @return 按照指定数制转换的配置整数值，如果该值没有配置，则抛出异常。
	 */
	public long getConfigLong(String key, int radix) {
		String value = getConfig(key);
		if (Strings.isBlank(value)) {
			throw new NullPointerException("The [" + key + "] not be set.");
		}
		return Long.parseLong(value, radix);
	}

	/**
	 * 获取指定key的配置信息，此配置信息为一个整数值(long)。
	 * 
	 * @param key
	 *            key
	 * @param radix
	 *            数制，2（二进制） | 8（八进制） | 10（十进制） | 16（十六进制）。
	 * @param defaultValue
	 *            没有配置时的默认整数值。
	 * @return 按照指定数制转换的配置整数值，如果该值没有配置，则返回传入的默认值。
	 */
	public long getConfigLong(String key, int radix, long defaultValue) {
		String value = getConfig(key);
		if (Strings.isBlank(value)) {
			return defaultValue;
		} else {
			return Long.parseLong(value, radix);
		}
	}

	/**
	 * 获取当前所有的配置信息列表
	 * 
	 * @return 所有配置信息列表
	 */
	public Map<String, String> getConfigs() {
		Map<String, String> result = new HashMap<String, String>();
		for (String key : configs.keySet()) {
			result.put(key, configs.get(key));
		}
		return result;
	}

	/**
	 * 获取符合正则表达式范围的配置信息列表
	 * 
	 * @param regex
	 *            正则表达式
	 * @return 符合条件的配置信息列表
	 */
	public Map<String, String> getConfigs(String regex) {
		Map<String, String> result = new HashMap<String, String>();
		for (String key : configs.keySet()) {
			if (key.matches(regex)) {
				result.put(key, configs.get(key));
			}
		}
		return result;
	}

	/**
	 * 从URL加载配置信息<br>
	 * 将自动根据配置文件的后缀名来识别（properties或者XML）
	 * 
	 * @param url
	 *            配置文件的URL
	 * @throws IOException
	 *             操作流过程中发生的异常
	 */
	public void loadFromURL(URL url) throws IOException {
		InputStream in = url.openStream();
		FILE_TYPE type = FILE_TYPE.PROPERTIES;
		if (url.getFile().toUpperCase().endsWith(".XML")) {
			type = FILE_TYPE.XML;
		}
		loadFromStream(in, type);
		in.close();
	}

	/**
	 * 从类加载路径中的properties配置文件中加载配置信息<br>
	 * 将自动根据配置文件的后缀名来识别（properties或者XML）
	 * 
	 * @param classPath
	 *            配置文件的类路径
	 * @throws IOException
	 *             操作流过程中发生的异常
	 */
	public void loadFromClassPath(String classPath) throws IOException {
		InputStream in = ApplicationConfig.class.getResourceAsStream(classPath);
		FILE_TYPE type = FILE_TYPE.PROPERTIES;
		if (classPath.toUpperCase().endsWith(".XML")) {
			type = FILE_TYPE.XML;
		}
		loadFromStream(in, type);
		in.close();
	}

	/**
	 * 从指定的多个properties配置文件中加载配置信息<br>
	 * 将自动根据配置文件的后缀名来识别（properties或者XML）<br>
	 * 将根据指定的refreshInterval值，定期载入配置信息（特别适合于可能不定期修改的配置信息）。
	 * 
	 * @param configFiles
	 *            多个配置文件路径列表
	 * @param refreshInterval
	 *            定期时间，以秒为单位。
	 * @throws IOException
	 * @throws InterruptedException
	 * @see #loadFromFile(String)
	 * @see #loadFromFile(String, long)
	 * @see #loadFromFile(String[], long)
	 */
	public void loadFromFile(List<String> configFiles, long refreshInterval)
			throws IOException, InterruptedException {
		// 先载入数据
		for (String file : configFiles) {
			loadFromFile(file);
		}
		if (refreshInterval > 0) {
			// 如果需要定期载入，则启动定期载入线程
			final long delay = refreshInterval * 1000;
			final List<String> files = configFiles;
			refreshThread = new BaseThread() {
				@Override
				public void runBody() throws Throwable {
					for (String file : files) {
						loadFromFile(file);
					}
				}
			};
			refreshThread.setDelay(delay);
			refreshThread.setExitWhenException(true);
			refreshThread.start();
		}
	}

	/**
	 * 从指定的properties配置文件中加载配置信息<br>
	 * 将自动根据配置文件的后缀名来识别（properties或者XML）
	 * 
	 * @param configFile
	 *            配置文件路径
	 * @throws IOException
	 *             操作文件过程中发生的异常
	 * @see #loadFromClassPath(String)
	 */
	public void loadFromFile(String configFile) throws IOException {
		File file = new File(configFile);
		if (!file.exists()) {
			if (logger.isWarnEnabled()) {
				logger.warn("The file(" + configFile + ") is not exist.");
			}
			return;
		}
		FileInputStream fin = new FileInputStream(file);
		FILE_TYPE type = FILE_TYPE.PROPERTIES;
		if (configFile.toUpperCase().endsWith(".XML")) {
			type = FILE_TYPE.XML;
		}
		loadFromStream(fin, type);
		fin.close();
		if (logger.isDebugEnabled()) {
			logger.debug("load config file success: " + configFile);
		}
	}

	/**
	 * 从指定的properties配置文件中加载配置信息<br>
	 * 将自动根据配置文件的后缀名来识别（properties或者XML）<br>
	 * 将根据指定的refreshInterval值，定期载入配置信息（特别适合于可能不定期修改的配置信息）。
	 * 
	 * @param configFile
	 *            配置文件路径
	 * @param refreshInterval
	 *            定期时间，以秒为单位。
	 * @throws IOException
	 * @throws InterruptedException
	 * @see #loadFromFile(String)
	 * @see #loadFromFile(List, long)
	 * @see #loadFromFile(String[], long)
	 */
	public void loadFromFile(String configFile, long refreshInterval)
			throws IOException, InterruptedException {
		loadFromFile(configFile);
		if (refreshInterval > 0) {
			final long delay = refreshInterval * 1000;
			final String file = configFile;
			refreshThread = new BaseThread() {
				@Override
				public void runBody() throws Throwable {
					loadFromFile(file);
				}
			};
			refreshThread.setDelay(delay);
			refreshThread.setExitWhenException(true);
			refreshThread.start();
		}
	}

	/**
	 * 从指定的多个properties配置文件中加载配置信息<br>
	 * 将自动根据配置文件的后缀名来识别（properties或者XML）<br>
	 * 将根据指定的refreshInterval值，定期载入配置信息（特别适合于可能不定期修改的配置信息）。
	 * 
	 * @param configFiles
	 *            多个配置文件路径数组
	 * @param refreshInterval
	 *            定期时间，以秒为单位。
	 * @throws IOException
	 * @throws InterruptedException
	 * @see #loadFromFile(String)
	 * @see #loadFromFile(String, long)
	 * @see #loadFromFile(List, long)
	 */
	public void loadFromFile(String[] configFiles, long refreshInterval)
			throws IOException, InterruptedException {
		for (String file : configFiles) {
			loadFromFile(file);
		}
		if (refreshInterval > 0) {
			final long delay = refreshInterval * 1000;
			final String[] files = configFiles;
			refreshThread = new BaseThread() {
				@Override
				public void runBody() throws Throwable {
					for (String file : files) {
						loadFromFile(file);
					}
				}
			};
			refreshThread.setDelay(delay);
			refreshThread.setExitWhenException(true);
			refreshThread.start();
		}
	}

	/**
	 * 从指定的输入流中加载配置信息
	 * 
	 * @param in
	 *            输入流
	 * @param type
	 *            输入流配置信息类型，可能是Properties或者XML类型
	 * @throws IOException
	 *             操作流过程中发生的异常
	 * @see FILE_TYPE
	 */
	public void loadFromStream(InputStream in, FILE_TYPE type)
			throws IOException {
		Properties properties = new Properties();
		if (type == FILE_TYPE.PROPERTIES) {
			properties.load(in);
		} else if (type == FILE_TYPE.XML) {
			properties.loadFromXML(in);
		}
		for (String key : properties.stringPropertyNames()) {
			setConfig(key, properties.getProperty(key));
		}
	}

	/**
	 * 删除指定的键
	 * 
	 * @param key
	 *            键名
	 */
	public void removeConfig(String key) {
		configs.remove(key);
	}

	/**
	 * 根据指定的键的前缀进行删除操作
	 * 
	 * @param prefix
	 *            键名的前缀
	 */
	public void removeConfigByPrefix(String prefix) {
		List<String> removed = new ArrayList<String>();
		Iterator<String> it = configs.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (key.startsWith(prefix)) {
				removed.add(key);
			}
		}
		for (String key : removed) {
			configs.remove(key);
		}
	}

	/**
	 * 将当前所有的配置信息保存到指定的properties配置文件中<br>
	 * 将自动根据配置文件的后缀名来识别（properties或者XML）
	 * 
	 * @param configFile
	 *            指定的文件路径
	 * @throws IOException
	 *             操作文件过程中发生的异常
	 */
	public void saveToFile(String configFile) throws IOException {
		Properties properties = new Properties();
		for (String key : configs.keySet()) {
			properties.put(key, configs.get(key));
		}
		FileOutputStream fout = new FileOutputStream(configFile);
		FILE_TYPE type = FILE_TYPE.PROPERTIES;
		if (configFile.toUpperCase().endsWith(".XML")) {
			type = FILE_TYPE.XML;
		}
		if (type == FILE_TYPE.PROPERTIES) {
			properties.store(fout, "Don't modiy this file");
		} else if (type == FILE_TYPE.XML) {
			properties.storeToXML(fout, "Don't modiy this file", "UTF-8");
		}
		fout.flush();
		fout.close();
	}

	/**
	 * 将指定的配置信息加入到加载类中，便于其他应用获取。
	 * 
	 * @param key
	 *            key
	 * @param config
	 *            配置信息
	 */
	public void setConfig(String key, String config) {
		configs.put(key, Strings.fillVariable(config));
	}

	/**
	 * 将指定的配置信息列表加入到加载类中，便于其他应用获取。
	 * 
	 * @param configs
	 *            配置信息列表
	 */
	public void setConfigs(Map<String, String> configs) {
		if (configs != null && !configs.isEmpty()) {
			for (String key : configs.keySet()) {
				setConfig(key, configs.get(key));
			}
		}
	}

	/**
	 * 配置文件类型的枚举量
	 * 
	 * @author josh
	 * 
	 */
	public enum FILE_TYPE {
		/**
		 * Properties类型的配置文件
		 * 
		 */
		PROPERTIES {
			@Override
			public String toString() {
				return ".properties";
			}
		},
		/**
		 * XML类型的配置文件
		 * 
		 */
		XML {
			@Override
			public String toString() {
				return ".xml";
			}
		}
	}

	/**
	 * 获得统一的ApplicaitonConfig对象（使用单例）
	 * 
	 * @return 通用的配置信息加载类对象
	 */
	public static ApplicationConfig getInstance() {
		if (appConfig == null) {
			synchronized (ApplicationConfig.class) {
				if (appConfig == null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Initialize the ApplicationConfig object.");
					}
				}
			}
			appConfig = new ApplicationConfig();
		}
		return appConfig;
	}
}
