package org.mingy.jsfs.model.orm;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.mingy.kernel.bean.IEntity;

@Entity
@Table(name = "T_REWARD_RULE")
public class RewardRule implements IEntity, INamedObject {

	private static final long serialVersionUID = 2104183255814439785L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "NAME", nullable = false, length = 100)
	@NotBlank(message = "{name.NotNull}")
	@Length(max = 50, message = "{name.MaxLength}")
	private String name;

	@ManyToMany(targetEntity = Position.class, cascade = {}, fetch = FetchType.LAZY)
	@JoinTable(name = "T_REWARD_RULE_POSITION", joinColumns = { @JoinColumn(name = "RULE_ID", referencedColumnName = "ID", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "POSITION_ID", referencedColumnName = "ID", nullable = false) })
	@NotEmpty(message = "{positions.NotEmpty}")
	private List<Position> positions;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GOODS_TYPE")
	private GoodsType goodsType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GOODS_ID")
	private Goods goods;

	@OneToMany(mappedBy = "rule", fetch = FetchType.EAGER)
	private List<RewardRuleDetail> details;

	@Column(name = "DESCRIPTION", length = 200)
	@Length(max = 100, message = "{desc.MaxLength}")
	private String description;

	@Override
	public int hashCode() {
		return id != null ? RewardRule.class.hashCode() * 31 + id.hashCode()
				: super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RewardRule)) {
			return false;
		} else if (id == null) {
			return super.equals(obj);
		} else {
			return id.equals(((RewardRule) obj).getId());
		}
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
		this.name = name;
	}

	public List<Position> getPositions() {
		return positions;
	}

	public void setPositions(List<Position> positions) {
		this.positions = positions;
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

	public List<RewardRuleDetail> getDetails() {
		return details;
	}

	public void setDetails(List<RewardRuleDetail> details) {
		this.details = details;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
