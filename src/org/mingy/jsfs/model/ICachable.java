package org.mingy.jsfs.model;

public interface ICachable<T extends ICachable<?>> extends IIdentity {

	void copyTo(T bean);

}
