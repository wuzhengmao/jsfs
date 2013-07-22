package org.mingy.jsfs.model.orm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.mingy.kernel.bean.IEntity;
import org.mingy.kernel.bean.ILogicDeletable;

@Entity
@Table(name = "T_STAFF")
public class Staff implements IEntity, ILogicDeletable, INamedObject {

	private static final long serialVersionUID = 6113019262565286354L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "NAME", nullable = false, length = 100)
	@NotBlank(message = "{name.NotNull}")
	@Length(max = 50, message = "{name.MaxLength}")
	private String name;

	@Column(name = "SEX", nullable = false)
	@NotNull(message = "{sex.NotNull}")
	private Integer sex;

	@Column(name = "BIRTHDAY")
	@Temporal(TemporalType.DATE)
	private Date birthday;

	@Column(name = "CONTACTS", length = 100)
	@Length(max = 50, message = "{contacts.MaxLength}")
	private String contacts;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "POSITION_ID", nullable = false)
	@NotNull(message = "{position.NotNull}")
	private Position position;

	@Column(name = "MEMO", length = 200)
	@Length(max = 10, message = "{memo.MaxLength}")
	private String memo;

	@Column(name = "VALID", nullable = false)
	private boolean valid = true;

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

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
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
