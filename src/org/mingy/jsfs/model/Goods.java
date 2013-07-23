package org.mingy.jsfs.model;

import java.beans.PropertyChangeListener;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class Goods extends PropertyChangeSupportBean implements
		ICachable<Goods>, INamedObject {

	private Long id;

	@NotNull(message = "{type.NotNull}")
	private GoodsType type;

	@NotBlank(message = "{name.NotNull}")
	@Length(max = 50, message = "{name.MaxLength}")
	private String name;

	@NotNull(message = "{salesPrice.NotNull}")
	private Double salesPrice;

	@Length(max = 100, message = "{memo.MaxLength}")
	private String memo;

	@Override
	public void copyTo(Goods target) {
		target.setId(id);
		target.setType(type);
		target.setName(name);
		target.setSalesPrice(salesPrice);
		target.setMemo(memo);
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

	public GoodsType getType() {
		return type;
	}

	public void setType(GoodsType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}

	public Double getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(Double salesPrice) {
		this.salesPrice = salesPrice;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
