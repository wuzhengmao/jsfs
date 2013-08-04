package org.mingy.jsfs.ui;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.mingy.jsfs.Activator;
import org.mingy.kernel.util.ApplicationException;
import org.mingy.kernel.util.Langs;

public class RestoreDatabaseAction extends Action {

	private static final Log logger = LogFactory
			.getLog(RestoreDatabaseAction.class);

	private IWorkbenchWindow window;

	public RestoreDatabaseAction(IWorkbenchWindow window, String text) {
		super(text);
		this.window = window;
		setId(ICommandIds.CMD_RESTORE_DATABASE);
		setActionDefinitionId(ICommandIds.CMD_RESTORE_DATABASE);
		setImageDescriptor(Activator
				.getImageDescriptor("/icons/restore_database.gif"));
		setDisabledImageDescriptor(Activator
				.getImageDescriptor("/icons/restore_database_disabled.gif"));
	}

	@Override
	public void run() {
		FileDialog dialog = new FileDialog(window.getShell(), SWT.OPEN);
		dialog.setFilterExtensions(new String[] { "*.sql" });
		final String path = dialog.open();
		if (path != null) {
			if (MessageDialog.openConfirm(window.getShell(),
					Langs.getText("restore.database.title"),
					Langs.getText("restore.database.confirm.message"))) {
				try {
					String url = System.getProperty("jdbc.url");
					int i = url.indexOf("//");
					int j = url.indexOf(':', i);
					int k = url.indexOf('/', j);
					final String cmd = Activator.getPath("/tool/mysql.exe")
							+ " -h" + url.substring(i + 2, j) + " -P"
							+ url.substring(j + 1, k) + " -u"
							+ System.getProperty("jdbc.user") + " -p"
							+ System.getProperty("jdbc.password") + " "
							+ url.substring(k + 1);
					ProgressMonitorDialog progress = new ProgressMonitorDialog(
							null);
					progress.run(true, false, new IRunnableWithProgress() {
						@Override
						public void run(IProgressMonitor monitor)
								throws InvocationTargetException,
								InterruptedException {
							try {
								monitor.beginTask("正在还原数据库", 0);
								if (logger.isDebugEnabled()) {
									logger.debug("run: " + cmd);
								}
								Process process = Runtime.getRuntime()
										.exec(cmd);
								new Thread(new StreamDrainer(process
										.getInputStream())).start();
								new Thread(new StreamDrainer(process
										.getErrorStream())).start();
								OutputStreamWriter writer = new OutputStreamWriter(
										process.getOutputStream(), "utf-8");
								BufferedReader reader = new BufferedReader(
										new InputStreamReader(
												new FileInputStream(path),
												"utf-8"));
								String line;
								while ((line = reader.readLine()) != null) {
									writer.write(line + "\n");
								}
								reader.close();
								writer.flush();
								writer.close();
								process.waitFor();
								monitor.done();
								window.getShell().getDisplay()
										.syncExec(new Runnable() {
											@Override
											public void run() {
												MessageDialog.openInformation(
														window.getShell(),
														Langs.getText("restore.database.title"),
														Langs.getText(
																"restore.database.ok.message",
																path));
												PlatformUI.getWorkbench()
														.restart();
											}
										});
							} catch (final Exception e) {
								if (logger.isErrorEnabled()) {
									logger.error("restore database failed", e);
								}
								window.getShell().getDisplay()
										.syncExec(new Runnable() {
											@Override
											public void run() {
												MessageDialog.openError(
														window.getShell(),
														Langs.getText("restore.database.title"),
														Langs.getText(
																"restore.database.error.message",
																(e instanceof ApplicationException ? ""
																		: e.getClass()
																				.getName()
																				+ ": ")
																		+ e.getLocalizedMessage()));
											}
										});
							}
						}
					});
				} catch (InvocationTargetException e) {
					if (logger.isErrorEnabled()) {
						logger.error("restore database failed", e);
					}
				} catch (InterruptedException e) {
					if (logger.isErrorEnabled()) {
						logger.error("restore database failed", e);
					}
				}
			}
		}
	}

	private class StreamDrainer implements Runnable {

		private InputStream in;

		public StreamDrainer(InputStream in) {
			this.in = in;
		}

		public void run() {
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));
				String line = null;
				while ((line = reader.readLine()) != null) {
					if (logger.isDebugEnabled()) {
						logger.debug(line);
					}
				}
			} catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error("IO error", e);
				}
			}
		}
	}
}
