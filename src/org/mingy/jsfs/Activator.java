package org.mingy.jsfs;

import java.io.File;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.mingy.jsfs.facade.IConfigFacade;
import org.mingy.kernel.context.GlobalBeanContext;
import org.mingy.kernel.crypto.Signature;
import org.mingy.kernel.util.Langs;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.mingy.jsfs"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		loadApplicationContext();
		initApplication();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		GlobalBeanContext.getInstance().shutdownContext();
		plugin = null;
		super.stop(context);
	}

	private void loadApplicationContext() {
		ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(
					getDefault().getClass().getClassLoader());
			GlobalBeanContext.getInstance().loadClassPathContext(
					new String[] { "applicationContext.xml",
							"applicationContext-jsfs.xml" });
			GlobalBeanContext.getInstance().registerShutdownHook();
		} finally {
			Thread.currentThread().setContextClassLoader(oldLoader);
		}
	}

	private void initApplication() {
		IConfigFacade configFacade = GlobalBeanContext.getInstance().getBean(
				IConfigFacade.class);
		if (configFacade.getConfig("system.title") == null) {
			configFacade.saveConfig("system.title",
					Langs.getText("system.title"));
		}
		if (configFacade.getConfig("password.admin") == null) {
			try {
				String password = Signature.digestBase64("SHA",
						"admin".getBytes());
				configFacade.saveConfig("password.admin", password);
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		}
		if (configFacade.getConfig("password.accounting") == null) {
			try {
				String password = Signature.digestBase64("SHA",
						"account".getBytes());
				configFacade.saveConfig("password.accounting", password);
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static String getPath(String path) {
		Bundle bundle = Platform.getBundle(PLUGIN_ID);
		if (bundle != null) {
			URL url = FileLocator.find(bundle, Path.fromOSString(path), null);
			if (url != null) {
				try {
					url = FileLocator.toFileURL(url);
					if (url != null) {
						return new File(url.toURI()).getAbsolutePath();
					}
				} catch (Exception e) {
					// ignore
				}
			}
		}
		return null;
	}
}
