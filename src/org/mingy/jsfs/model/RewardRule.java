package org.mingy.jsfs.model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

public class RewardRule extends PropertyChangeSupportBean implements IIdentity,
		INamedObject {

	private Long id;

	@NotBlank(message = "{name.NotNull}")
	@Length(max = 50, message = "{name.MaxLength}")
	private String name;

	@NotEmpty(message = "{positions.NotEmpty}")
	private List<Position> positions = new ArrayList<Position>();

	private GoodsType goodsType;

	private Goods goods;

	@NotEmpty(message = "{details.NotEmpty}")
	private List<RewardRuleDetail> details = new ArrayList<RewardRuleDetail>();

	@Length(max = 100, message = "{desc.MaxLength}")
	private String description;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Position> getPositions() {
		return positions;
	}

	public List<RewardRuleDetail> getDetails() {
		return details;
	}

	public class RewardRuleDetail {

		private Long id;

		private Integer minCount;

		private Integer maxCount;

		private Integer countType;

		private Double bonusValue;

		private Double bonusPercent;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Integer getMinCount() {
			return minCount;
		}

		public void setMinCount(Integer minCount) {
			this.minCount = minCount;
		}

		public Integer getMaxCount() {
			return maxCount;
		}

		public void setMaxCount(Integer maxCount) {
			this.maxCount = maxCount;
		}

		public Integer getCountType() {
			return countType;
		}

		public void setCountType(Integer countType) {
			this.countType = countType;
		}

		public Double getBonusValue() {
			return bonusValue;
		}

		public void setBonusValue(Double bonusValue) {
			this.bonusValue = bonusValue;
		}

		public Double getBonusPercent() {
			return bonusPercent;
		}

		public void setBonusPercent(Double bonusPercent) {
			this.bonusPercent = bonusPercent;
		}
	}
}
