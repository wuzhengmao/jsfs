package org.mingy.jsfs.model.orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.mingy.kernel.bean.IEntity;

@Entity
@Table(name = "T_CONFIG")
public class ConfigEntity implements IEntity {

	private static final long serialVersionUID = 716001925388928437L;

	@Id
	@Column(name = "CONFIG_KEY", updatable = false, nullable = false, length = 100)
	private String id;

	@Column(name = "CONFIG_VALUE", length = 500)
	private String value;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
