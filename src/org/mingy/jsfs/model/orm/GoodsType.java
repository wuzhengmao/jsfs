package org.mingy.jsfs.model.orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.mingy.kernel.bean.IEntity;
import org.mingy.kernel.bean.ILogicDeletable;

@Entity
@Table(name = "T_GOODS_TYPE")
public class GoodsType implements IEntity, ILogicDeletable, ICatalog {

	private static final long serialVersionUID = 3221586385062860874L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "NAME", nullable = false, length = 100)
	@NotBlank(message = "{name.NotNull}")
	@Length(max = 50, message = "{name.MaxLength}")
	private String name;

	@Column(name = "DESCRIPTION", length = 200)
	@Length(max = 100, message = "{desc.MaxLength}")
	private String description;

	@Column(name = "VALID", nullable = false)
	private boolean valid = true;

	@Override
	public int hashCode() {
		return id != null ? GoodsType.class.hashCode() * 31 + id.hashCode()
				: super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GoodsType)) {
			return false;
		} else if (id == null) {
			return super.equals(obj);
		} else {
			return id.equals(((GoodsType) obj).getId());
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
}
