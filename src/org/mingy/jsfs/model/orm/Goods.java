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
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.mingy.kernel.bean.IEntity;
import org.mingy.kernel.bean.ILogicDeletable;

@Entity
@Table(name = "T_GOODS")
public class Goods implements IEntity, ILogicDeletable, INamedObject {

	private static final long serialVersionUID = -4894504350810578341L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "TYPE", nullable = false)
	@NotNull(message = "{type.NotNull}")
	private GoodsType type;

	@Column(name = "NAME", nullable = false, length = 100)
	@NotBlank(message = "{name.NotNull}")
	@Length(max = 50, message = "{name.MaxLength}")
	private String name;

	@Column(name = "SALES_PRICE", nullable = false, precision = 7, scale = 2)
	@NotBlank(message = "{salesPrice.NotNull}")
	private Double salesPrice;

	@Column(name = "MEMO", length = 200)
	@Length(max = 100, message = "{memo.MaxLength}")
	private String memo;

	@Column(name = "VALID", nullable = false)
	private boolean valid = true;

	@Override
	public int hashCode() {
		return id != null ? Goods.class.hashCode() * 31 + id.hashCode() : super
				.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Goods)) {
			return false;
		} else if (id == null) {
			return super.equals(obj);
		} else {
			return id.equals(((Goods) obj).getId());
		}
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
		this.name = name;
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

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
}
