package org.mingy.jsfs.model;

import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

public class RewardRule extends PropertyChangeSupportBean implements
		ICachable<RewardRule>, INamedObject {

	private Long id;

	@NotBlank(message = "{name.NotNull}")
	@Length(max = 50, message = "{name.MaxLength}")
	private String name;

	@NotEmpty(message = "{positions.NotEmpty}")
	private Set<Position> positions;

	private GoodsType goodsType;

	private Goods goods;

	@NotBlank(message = "{script.NotNull}")
	@Length(max = 2000, message = "{script.MaxLength}")
	private String script;

	@Length(max = 100, message = "{desc.MaxLength}")
	private String description;

	public void copyTo(RewardRule target) {
		target.setId(id);
		target.setName(name);
		target.setPositions(new HashSet<Position>(positions));
		target.setGoodsType(goodsType);
		target.setGoods(goods);
		target.setScript(script);
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

	public Set<Position> getPositions() {
		return positions;
	}

	public void setPositions(Set<Position> positions) {
		firePropertyChange("positions", this.positions,
				this.positions = positions);
	}

	@NotNull(message = "{goodsOrType.NotNull}")
	public INamedObject getGoodsOrType() {
		return goods != null ? goods : goodsType;
	}

	public void setGoodsOrType(INamedObject goodsOrType) {
		if (goodsOrType instanceof Goods) {
			goodsType = null;
			goods = (Goods) goodsOrType;
		} else {
			goodsType = (GoodsType) goodsOrType;
			goods = null;
		}
	}

	public GoodsType getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(GoodsType goodsType) {
		this.goodsType = goodsType;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
