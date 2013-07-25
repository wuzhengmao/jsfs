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
import javax.persistence.UniqueConstraint;

import org.mingy.kernel.bean.IEntity;

@Entity
@Table(name = "T_SALES_LOG_DETAIL", uniqueConstraints = @UniqueConstraint(columnNames = {
		"LOG_ID", "GOODS_ID" }))
public class SalesLogDetailEntity implements IEntity {

	private static final long serialVersionUID = 5047261923415640657L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "LOG_ID", nullable = false)
	private SalesLogEntity salesLog;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "GOODS_ID", nullable = false)
	private GoodsEntity goods;

	@Column(name = "COUNT_NUM", nullable = false)
	private Integer count;

	@Column(name = "TOTLE_PRICE", nullable = false, precision = 7, scale = 2)
	private Double totalPrice;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SalesLogEntity getSalesLog() {
		return salesLog;
	}

	public void setSalesLog(SalesLogEntity salesLog) {
		this.salesLog = salesLog;
	}

	public GoodsEntity getGoods() {
		return goods;
	}

	public void setGoods(GoodsEntity goods) {
		this.goods = goods;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
}
