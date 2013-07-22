package org.mingy.kernel.bean;

import java.util.Date;

/**
 * 简单时间跟踪类型的数据实体接口，提供了创建时间和更新时间的跟踪。
 * 
 * @author Mingy
 * 
 */
public interface ITimeTrackable extends IEntity {

	/**
	 * 获取创建时间
	 * 
	 * @return 创建时间，一般数据精度只到秒（由具体数据库决定）
	 */
	public Date getCreateTime();

	/**
	 * 设置创建时间，一般不需要外部设置，如果使用org.mx.platform，系统会自动维护。
	 * 
	 * @param createTime
	 *            创建时间，一般数据精度只到秒（由具体数据库决定）
	 */
	public void setCreateTime(Date createTime);

	/**
	 * 获取更新时间
	 * 
	 * @return 更新时间，一般数据精度只到秒（由具体数据库决定）
	 */
	public Date getLastUpdateTime();

	/**
	 * 设置更新时间，一般不需要外部设置，如果使用org.mx.platform，系统会自动维护。
	 * 
	 * @param updateTime
	 *            更新时间，一般数据精度只到秒（由具体数据库决定）
	 */
	public void setLastUpdateTime(Date updateTime);
}
