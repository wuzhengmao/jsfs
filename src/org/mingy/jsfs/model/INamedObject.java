package org.mingy.jsfs.model;

import java.beans.PropertyChangeListener;

public interface INamedObject {

	String getName();

	void addNameChangeListener(PropertyChangeListener listener);

	void removeNameChangeListener(PropertyChangeListener listener);
}
