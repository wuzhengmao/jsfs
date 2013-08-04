package org.mingy.jsfs.model;

import java.util.ArrayList;
import java.util.List;

public class RoleManager {

	private static RoleManager instance = new RoleManager();

	private Role role = Role.GUEST;
	private List<RoleChangeListener> listeners = new ArrayList<RoleChangeListener>();

	public static RoleManager getInstance() {
		return instance;
	}

	public void addRoleChangeListener(RoleChangeListener listener) {
		listeners.add(listener);
	}

	public void removeRoleChangeListener(RoleChangeListener listener) {
		listeners.remove(listener);
	}

	private void fireRoleChangeEvent(Role oldRole, Role newRole) {
		for (RoleChangeListener listener : new ArrayList<RoleChangeListener>(
				listeners)) {
			listener.onChange(oldRole, newRole);
		}
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		if (this.role != role) {
			fireRoleChangeEvent(this.role, this.role = role);
		}
	}

	public static interface RoleChangeListener {

		void onChange(Role oldRole, Role newRole);
	}
}
