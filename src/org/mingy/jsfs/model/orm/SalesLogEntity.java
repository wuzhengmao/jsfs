package org.mingy.jsfs.model.orm;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.mingy.kernel.bean.IEntity;

@Entity
@Table(name = "T_SALES_LOG")
public class SalesLogEntity implements IEntity {

	private static final long serialVersionUID = -5260268466646200649L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "SALES_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date salesTime;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "STAFF_ID", nullable = false)
	private StaffEntity staff;

	@OneToMany(mappedBy = "salesLog", fetch = FetchType.EAGER)
	private List<SalesLogDetailEntity> details;

	@Column(name = "DESCRIPTION", length = 200)
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

	public StaffEntity getStaff() {
		return staff;
	}

	public void setStaff(StaffEntity staff) {
		this.staff = staff;
	}

	public List<SalesLogDetailEntity> getDetails() {
		return details;
	}

	public void setDetails(List<SalesLogDetailEntity> details) {
		this.details = details;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
