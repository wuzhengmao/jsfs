package org.mingy.kernel.thread;

/**
 * 线程执行动作的监控接口
 * 
 * @author Mingy
 * @see BaseThread
 * 
 */
public interface IBaseThreadListener {
	/**
	 * 线程被暂停时被调用
	 * 
	 * @param thread
	 *            被调用的线程对象
	 */
	public void paused(BaseThread thread);

	/**
	 * 线程被恢复时被调用
	 * 
	 * @param thread
	 *            被调用的线程对象
	 */
	public void resumed(BaseThread thread);

	/**
	 * 线程被启动时被调用
	 * 
	 * @param thread
	 *            被调用的线程对象
	 */
	public void started(BaseThread thread);

	/**
	 * 线程被停止时被调用
	 * 
	 * @param thread
	 *            被调用的线程对象
	 */
	public void stopped(BaseThread thread);
}
