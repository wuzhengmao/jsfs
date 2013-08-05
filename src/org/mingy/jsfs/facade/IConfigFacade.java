package org.mingy.jsfs.facade;

public interface IConfigFacade {

	String getConfig(String key);

	String getConfig(String key, String defaultValue);

	void saveConfig(String key, String value);

	void deleteConfig(String key);
}
