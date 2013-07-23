package org.mingy.jsfs.model;

import java.beans.PropertyChangeListener;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class GoodsType extends PropertyChangeSupportBean implements
		ICachable<GoodsType>, ICatalog {

	private Long id;

	@NotBlank(message = "{name.NotNull}")
	@Length(max = 50, message = "{name.MaxLength}")
	private String name;

	@Length(max = 100, message = "{desc.MaxLength}")
	private String description;

	@Override
	public void copyTo(GoodsType target) {
		target.setId(id);
		target.setName(name);
		target.setDescription(description);
	}

	@Override
	public void addNameChangeListener(PropertyChangeListener listener) {
		addPropertyChangeListener("name", listener);
	}

	@Override
	public void removeNameChangeListener(PropertyChangeListener listener) {
		removePropertyChangeListener("name", listener);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
