package org.mingy.jsfs.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class SalesLog {

	private Long id;

	@NotNull(message = "{salesTime.NotNull}")
	private Date salesTime;

	@NotNull(message = "{staff.NotNull}")
	private Staff staff;

	@NotEmpty(message = "{salesLog.details.NotEmpty}")
	private IObservableList details = new WritableList();

	@Length(max = 100, message = "{memo.MaxLength}")
	private String memo;

	@SuppressWarnings("unchecked")
	public void copyTo(SalesLog target) {
		target.setId(id);
		target.setSalesTime(salesTime);
		target.setStaff(staff);
		target.getDetails().clear();
		target.getDetails().addAll(details);
		target.setMemo(memo);
	}

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

	@SuppressWarnings("unchecked")
	public List<SalesLogDetail> getDetails() {
		return details;
	}

	public IObservableList getObservableDetails() {
		return details;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public class SalesLogDetail {

		private Long id;

		@NotNull(message = "{goods.NotNull}")
		private Goods goods;

		@NotNull(message = "{count.NotNull}")
		@Min(value = 1, message = "{count.LargerThanZero}")
		private Integer count;

		@NotNull(message = "{totalPrice.NotNull}")
		@Min(value = 0, message = "{totalPrice.NotLessThanZero}")
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
