package org.mingy.jsfs.model;

import java.util.ArrayList;
import java.util.List;

public class Salary {

	private Staff staff;

	private Double amount = 0d;

	private Double salesAmount = 0d;

	private List<StatDetail> details = new ArrayList<StatDetail>();

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getSalesAmount() {
		return salesAmount;
	}

	public void setSalesAmount(Double salesAmount) {
		this.salesAmount = salesAmount;
	}

	public List<StatDetail> getDetails() {
		return details;
	}

	public static class StatDetail {

		private INamedObject goodsOrType;

		private Integer count = 0;

		private Double price = 0d;

		private Double amount = 0d;

		public INamedObject getGoodsOrType() {
			return goodsOrType;
		}

		public void setGoodsOrType(INamedObject goodsOrType) {
			this.goodsOrType = goodsOrType;
		}

		public Integer getCount() {
			return count;
		}

		public void setCount(Integer count) {
			this.count = count;
		}

		public Double getPrice() {
			return price;
		}

		public void setPrice(Double price) {
			this.price = price;
		}

		public Double getAmount() {
			return amount;
		}

		public void setAmount(Double amount) {
			this.amount = amount;
		}
	}
}
