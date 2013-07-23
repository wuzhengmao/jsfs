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

import org.mingy.kernel.bean.IEntity;

@Entity
@Table(name = "T_REWARD_RULE")
public class RewardRuleEntity implements IEntity {

	private static final long serialVersionUID = 2104183255814439785L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "NAME", nullable = false, length = 100)
	private String name;

	@ManyToMany(targetEntity = PositionEntity.class, cascade = {}, fetch = FetchType.LAZY)
	@JoinTable(name = "T_REWARD_RULE_POSITION", joinColumns = { @JoinColumn(name = "RULE_ID", referencedColumnName = "ID", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "POSITION_ID", referencedColumnName = "ID", nullable = false) })
	private List<PositionEntity> positions;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GOODS_TYPE")
	private GoodsTypeEntity goodsType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GOODS_ID")
	private GoodsEntity goods;

	@OneToMany(mappedBy = "rule", fetch = FetchType.EAGER)
	private List<RewardRuleDetailEntity> details;

	@Column(name = "DESCRIPTION", length = 200)
	private String description;

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

	public List<PositionEntity> getPositions() {
		return positions;
	}

	public void setPositions(List<PositionEntity> positions) {
		this.positions = positions;
	}

	public GoodsTypeEntity getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(GoodsTypeEntity goodsType) {
		this.goodsType = goodsType;
	}

	public GoodsEntity getGoods() {
		return goods;
	}

	public void setGoods(GoodsEntity goods) {
		this.goods = goods;
	}

	public List<RewardRuleDetailEntity> getDetails() {
		return details;
	}

	public void setDetails(List<RewardRuleDetailEntity> details) {
		this.details = details;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
