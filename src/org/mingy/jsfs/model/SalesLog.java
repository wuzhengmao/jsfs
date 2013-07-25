package org.mingy.jsfs.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class SalesLog {

	private Long id;

	@NotNull(message = "{salesTime.NotNull}")
	private Date salesTime;

	@NotNull(message = "{staff.NotNull}")
	private Staff staff;

	@NotEmpty(message = "{salesLog.details.NotEmpty}")
	private List<SalesLogDetail> details = new ArrayList<SalesLogDetail>();

	@Length(max = 100, message = "{desc.MaxLength}")
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getSalesTime() {
		return salesTime;
	}

	public void setSalesTime(Date salesTime) {
		this.salesTime = salesTime;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<SalesLogDetail> getDetails() {
		return details;
	}

	public class SalesLogDetail {

		private Long id;

		@NotNull(message = "{goods.NotNull}")
		private Goods goods;

		@NotNull(message = "{count.NotNull}")
		@Min(value = 1, message = "{count.largerThanZero}")
		private Integer count;

		@NotNull(message = "{totalPrice.NotNull}")
		private Double totalPrice;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Goods getGoods() {
			return goods;
		}

		public void setGoods(Goods goods) {
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
}
