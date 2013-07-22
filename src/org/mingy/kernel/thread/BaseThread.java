package org.mingy.kernel.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 通用的线程处理类，封装了线程的基本操作接口。<br>
 * 已经使用锁定类型来保护相关的可能并行操作。
 * 
 * @author Mingy
 * @see IBaseThreadListener
 */
public abstract class BaseThread {
	
	private static final Log logger = LogFactory.getLog(BaseThread.class);
	private final Lock lock = new ReentrantLock();
	private final Condition conRunning = lock.newCondition();
	private final Condition conPausing = lock.newCondition();

	/**
	 * 默认的执行操作前需要等待的时间，单位为毫秒。
	 */
	public static final long DEFAULT_DELAY = 500;

	/**
	 * 线程执行的状态
	 */
	public static enum ThreadState {
		/**
		 * 无效状态
		 */
		NA, /**
		 * 运行状态
		 */
		RUN, /**
		 * 停止状态
		 */
		STOP, /**
		 * 暂停状态
		 */
		PAUSED
	};

	private volatile Thread thread = null;
	private ThreadState state = ThreadState.NA;
	private IBaseThreadListener listener = null;
	private long delay = DEFAULT_DELAY, preDelay = DEFAULT_DELAY / 3,
			postDelay = delay - preDelay;
	private boolean exitWhenException = false;

	/**
	 * 默认的构造函数
	 */
	public BaseThread() {
		super();
	}

	/**
	 * 带输入参数的构造函数
	 * 
	 * @param millis
	 *            运行用户任务前暂停的时间，单位为毫秒。
	 * @param listener
	 *            运行线程过程中监听的接口类。
	 * @param exitWhenException
	 *            如果设置为true，则在线程执行异常时退出线程；否则不退出。
	 */
	public BaseThread(long millis, IBaseThreadListener listener,
			boolean exitWhenException) {
		this();
		this.delay = millis;
		this.listener = listener;
		this.exitWhenException = exitWhenException;
	}

	/**
	 * 处理线程的暂停任务
	 * 
	 * @return 如果返回true，表示后续正常执行；否则不应该执行后续任务。
	 * @throws InterruptedException
	 */
	public boolean dealPausing() throws InterruptedException {
		lock.lock();
		try {
			while (state == ThreadState.PAUSED) {
				if (listener != null) {
					listener.paused(this);
				}
				conPausing.await();
			}
			return state == ThreadState.RUN;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 获取当前线程状态
	 * 
	 * @return 线程状态枚举
	 * @see ThreadState
	 */
	public ThreadState getState() {
		lock.lock();
		try {
			return state;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 发出暂停当前线程执行的请求
	 */
	public void pause() {
		lock.lock();
		try {
			state = ThreadState.PAUSED;
			if (logger.isDebugEnabled()) {
				logger.debug("The request for pausing thread is sent.");
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 发出恢复当前线程执行的请求
	 * 
	 * @throws InterruptedException
	 */
	public void resume() throws InterruptedException {
		lock.lock();
		try {
			state = ThreadState.RUN;
			conPausing.signal();
			if (logger.isDebugEnabled()) {
				logger.debug("The request for resuming thread is sent.");
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 抽象的用户执行任务，由用户自行实现。<br>
	 * 如果用户需要执行较长时间的任务，需要在合适的时机调用dealPausing方法，并且检测其返回值。<br>
	 * 如果返回值为true，后续任务可以继续执行；否则应该终止执行后续任务。
	 * 
	 * @throws Throwable
	 */
	public abstract void runBody() throws Throwable;

	/**
	 * 设置线程执行监控接口类
	 * 
	 * @param listener
	 *            线程执行监控接口类
	 */
	public void setBaseThreadListener(IBaseThreadListener listener) {
		this.listener = listener;
	}

	/**
	 * 设置线程执行用户任务总体需要等待的时间，单位为毫秒。<br>
	 * 同时自动设置前延迟和后延迟时间，规则为：<br>
	 * &nbsp;&nbsp;preDelay = delay / 3, postDelay = delay - preDelay。
	 * 
	 * @param millis
	 *            时间
	 */
	public void setDelay(long millis) {
		if (millis > 0) {
			this.delay = millis;
			this.preDelay = this.delay / 3;
			this.postDelay = this.delay - this.preDelay;
		}
	}

	/**
	 * 设置异常是否退出的标志位
	 * 
	 * @param exitWhenException
	 *            如果设置为true，则线程执行碰到异常后会退出线程；否则线程继续执行。
	 */
	public void setExitWhenException(boolean exitWhenException) {
		this.exitWhenException = exitWhenException;
	}

	/**
	 * 设置线程执行用户任务后需要等待的时间，单位为毫秒。
	 * 
	 * @param millis
	 *            时间
	 * @see #setDelay(long)
	 */
	public void setPostDelay(long millis) {
		if (millis > 0) {
			this.postDelay = millis;
			this.delay = this.postDelay + this.preDelay;
		}
	}

	/**
	 * 设置线程执行用户任务前需要等待的时间，单位为毫秒。
	 * 
	 * @param millis
	 *            时间
	 * @see #setDelay(long)
	 */
	public void setPreDelay(long millis) {
		if (millis > 0) {
			this.preDelay = millis;
			this.delay = this.preDelay + this.postDelay;
		}
	}

	/**
	 * 启动当前线程
	 * 
	 * @throws InterruptedException
	 */
	public void start() throws InterruptedException {
		lock.lock();
		try {
			while (state == ThreadState.RUN || state == ThreadState.PAUSED) {
				conRunning.await();
			}
			final BaseThread baseThread = this;
			thread = new Thread() {
				@Override
				public void run() {
					Thread currentThread = Thread.currentThread();
					while (thread == currentThread
							&& (state == ThreadState.RUN || state == ThreadState.PAUSED)) {
						// 前延迟等待
						try {
							Thread.sleep(preDelay);
						} catch (InterruptedException e) {
							if (logger.isErrorEnabled()) {
								logger.error("Thread sleep fail.", e);
							}
							if (exitWhenException) {
								thread = null;
								state = ThreadState.STOP;
								break;
							}
						}
						// 处理暂停操作
						try {
							dealPausing();
						} catch (InterruptedException e) {
							if (logger.isErrorEnabled()) {
								logger.error("Pausing the thread fail.", e);
							}
							if (exitWhenException) {
								thread = null;
								state = ThreadState.STOP;
								break;
							}
						}
						// 执行业务方法
						try {
							runBody();
						} catch (Throwable e) {
							if (logger.isErrorEnabled()) {
								logger.error("Run user's function fail.", e);
							}
							if (exitWhenException) {
								thread = null;
								state = ThreadState.STOP;
								break;
							}
						}
						// 执行后延迟等待
						try {
							Thread.sleep(postDelay);
						} catch (InterruptedException e) {
							if (logger.isErrorEnabled()) {
								logger.error("Thread sleep fail.", e);
							}
							if (exitWhenException) {
								thread = null;
								state = ThreadState.STOP;
								break;
							}
						}
					}
					if (listener != null) {
						listener.stopped(baseThread);
					}
					state = ThreadState.NA;
					if (logger.isDebugEnabled()) {
						logger.debug("The thread is finished.");
					}
				}
			};
			state = ThreadState.RUN;
			thread.start();
			conPausing.signal();
			if (logger.isDebugEnabled()) {
				logger.debug("The thread is started.");
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 发出停止当前线程的请求。
	 */
	public void stop() {
		lock.lock();
		try {
			state = ThreadState.STOP;
			thread = null;
			conPausing.signal();
			conRunning.signal();
			if (logger.isDebugEnabled()) {
				logger.debug("The request for stopping thread is sent.");
			}
		} finally {
			lock.unlock();
		}
	}
}
