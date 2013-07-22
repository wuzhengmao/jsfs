/**
 * 
 */
package org.mingy.kernel.bean;

/**
 * 简单的映射实体对象的有效性跟踪接口。
 * 
 * @author Mingy
 * 
 */
public interface ILogicDeletable extends IEntity {

	/**
	 * 获取是否有效
	 * 
	 * @return true表示有效，false表示无效。
	 */
	public boolean isValid();

	/**
	 * 设置有效性
	 * 
	 * @param valid
	 *            true表示有效，false表示无效。
	 */
	public void setValid(boolean valid);
}
