package org.mingy.jsfs.facade.impl;

import org.mingy.jsfs.facade.IConfigFacade;
import org.mingy.jsfs.model.orm.ConfigEntity;
import org.mingy.kernel.facade.IEntityDaoFacade;

public class ConfigFacadeImpl implements IConfigFacade {

	private IEntityDaoFacade entityDao;

	@Override
	public String getConfig(String key) {
		ConfigEntity entity = entityDao.load(ConfigEntity.class, key);
		return entity != null ? entity.getValue() : null;
	}

	@Override
	public String getConfig(String key, String defaultValue) {
		ConfigEntity entity = entityDao.load(ConfigEntity.class, key);
		return entity != null ? entity.getValue() : defaultValue;
	}

	@Override
	public void saveConfig(String key, String value) {
		ConfigEntity entity = new ConfigEntity();
		entity.setId(key);
		entity.setValue(value);
		entityDao.save(entity);
	}

	@Override
	public void deleteConfig(String key) {
		entityDao.delete(ConfigEntity.class, key);
	}

	public void setEntityDao(IEntityDaoFacade entityDao) {
		this.entityDao = entityDao;
	}
}
