package org.mingy.jsfs.model;

import org.mingy.kernel.util.Langs;

public enum Role {

	ADMIN, ACCOUNTING, GUEST;

	@Override
	public String toString() {
		return Langs.getLabel("role", ordinal());
	}
}
