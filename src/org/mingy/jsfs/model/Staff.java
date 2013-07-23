package org.mingy.jsfs.model;

import java.beans.PropertyChangeListener;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class Staff extends PropertyChangeSupportBean implements
		ICachable<Staff>, INamedObject {

	private Long id;

	@NotBlank(message = "{name.NotNull}")
	@Length(max = 50, message = "{name.MaxLength}")
	private String name;

	@NotNull(message = "{sex.NotNull}")
	private Integer sex;

	private Date birthday;

	@Length(max = 50, message = "{contacts.MaxLength}")
	private String contacts;

	@NotNull(message = "{position.NotNull}")
	private Position position;

	@Length(max = 100, message = "{memo.MaxLength}")
	private String memo;

	@Override
	public void copyTo(Staff target) {
		target.setId(id);
		target.setName(name);
		target.setSex(sex);
		target.setBirthday(birthday);
		target.setContacts(contacts);
		target.setPosition(position);
		target.setMemo(memo);
	}

	@Override
	public void addNameChangeListener(PropertyChangeListener listener) {
		addPropertyChangeListener("name", listener);
	}

	@Override
	public void removeNameChangeListener(PropertyChangeListener listener) {
		removePropertyChangeListener("name", listener);
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
		firePropertyChange("name", this.name, this.name = name);
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
}
