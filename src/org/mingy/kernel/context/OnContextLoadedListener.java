package org.mingy.kernel.context;

/**
 * 当全局的对象容器加载完毕后的回调接口。<br>
 * 定义在容器中的对象实现了该接口后，会在容器启动完毕后自动回调。
 * 
 * @author Mingy
 */
public interface OnContextLoadedListener {

	/**
	 * 回调的顺序，越小的值越先被调用。
	 * 
	 * @return 顺序值
	 */
	int getOrder();

	/**
	 * 回调接口。
	 * 
	 * @param context
	 *            全局对象容器的上下文
	 */
	void onContextLoaded(GlobalBeanContext context);
}
