package org.mingy.jsfs.model.orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.mingy.kernel.bean.IEntity;

@Entity
@Table(name = "T_REWARD_RULE_DETAIL")
public class RewardRuleDetail implements IEntity {

	private static final long serialVersionUID = 1788503780897322068L;

	public static final int COUNT_TYPE_ALL = 0;
	public static final int COUNT_TYPE_DELTA = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "RULE_ID", nullable = false)
	private RewardRule rule;

	@Column(name = "MIN_COUNT")
	private Integer minCount;

	@Column(name = "MAX_COUNT")
	private Integer maxCount;

	@Column(name = "COUNT_TYPE")
	private Integer countType;

	@Column(name = "BONUS_PRICE", precision = 7, scale = 2)
	private Double bonusValue;

	@Column(name = "BONUS_PERCENT", precision = 7, scale = 2)
	private Double bonusPercent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RewardRule getRule() {
		return rule;
	}

	public void setRule(RewardRule rule) {
		this.rule = rule;
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
