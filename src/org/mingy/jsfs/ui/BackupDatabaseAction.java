package org.mingy.jsfs.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import org.mingy.jsfs.Activator;
import org.mingy.kernel.util.ApplicationException;
import org.mingy.kernel.util.Langs;

public class BackupDatabaseAction extends Action {

	private static final Log logger = LogFactory
			.getLog(BackupDatabaseAction.class);

	private IWorkbenchWindow window;

	public BackupDatabaseAction(IWorkbenchWindow window, String text) {
		super(text);
		this.window = window;
		setId(ICommandIds.CMD_BACKUP_DATABASE);
		setActionDefinitionId(ICommandIds.CMD_BACKUP_DATABASE);
		setImageDescriptor(Activator
				.getImageDescriptor("/icons/backup_database.gif"));
		setDisabledImageDescriptor(Activator
				.getImageDescriptor("/icons/backup_database_disabled.gif"));
	}

	@Override
	public void run() {
		FileDialog dialog = new FileDialog(window.getShell(), SWT.SAVE);
		dialog.setFileName("jsfsdb-"
				+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
				+ ".sql");
		dialog.setFilterExtensions(new String[] { "*.sql" });
		dialog.setOverwrite(true);
		final String path = dialog.open();
		if (path != null) {
			try {
				String url = System.getProperty("jdbc.url");
				int i = url.indexOf("//");
				int j = url.indexOf(':', i);
				int k = url.indexOf('/', j);
				final String cmd = Activator.getPath("/tool/mysqldump.exe")
						+ " -h" + url.substring(i + 2, j) + " -P"
						+ url.substring(j + 1, k) + " -u"
						+ System.getProperty("jdbc.user") + " -p"
						+ System.getProperty("jdbc.password") + " "
						+ url.substring(k + 1);
				ProgressMonitorDialog progress = new ProgressMonitorDialog(null);
				progress.run(true, false, new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor)
							throws InvocationTargetException,
							InterruptedException {
						try {
							monitor.beginTask("正在备份数据库", 0);
							if (logger.isDebugEnabled()) {
								logger.debug("run: " + cmd);
							}
							Process process = Runtime.getRuntime().exec(cmd);
							new Thread(new StreamDrainer(process
									.getInputStream(), new File(path))).start();
							new Thread(new StreamDrainer(process
									.getErrorStream())).start();
							process.getOutputStream().close();
							process.waitFor();
							monitor.done();
							window.getShell().getDisplay()
									.syncExec(new Runnable() {
										@Override
										public void run() {
											MessageDialog.openInformation(
													window.getShell(),
													Langs.getText("backup.database.title"),
													Langs.getText(
															"backup.database.ok.message",
															path));
										}
									});
						} catch (final Exception e) {
							if (logger.isErrorEnabled()) {
								logger.error("backup database failed", e);
							}
							window.getShell().getDisplay()
									.syncExec(new Runnable() {
										@Override
										public void run() {
											MessageDialog.openError(
													window.getShell(),
													Langs.getText("backup.database.title"),
													Langs.getText(
															"backup.database.error.message",
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
					logger.error("backup database failed", e);
				}
			} catch (InterruptedException e) {
				if (logger.isErrorEnabled()) {
					logger.error("backup database failed", e);
				}
			}
		}
	}

	private class StreamDrainer implements Runnable {

		private InputStream in;
		private File file;

		public StreamDrainer(InputStream in) {
			this.in = in;
		}

		public StreamDrainer(InputStream in, File file) {
			this.in = in;
			this.file = file;
		}

		public void run() {
			try {
				BufferedWriter writer = file != null ? new BufferedWriter(
						new FileWriter(file)) : null;
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));
				String line = null;
				while ((line = reader.readLine()) != null) {
					if (writer != null) {
						writer.write(line + "\n");
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug(line);
						}
					}
				}
				if (writer != null) {
					writer.flush();
					writer.close();
				}
			} catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error("IO error", e);
				}
			}
		}
	}
}
