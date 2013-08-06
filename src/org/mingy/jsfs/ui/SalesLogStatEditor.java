package org.mingy.jsfs.ui;

import java.util.Date;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.mingy.jsfs.Activator;
import org.mingy.jsfs.facade.IConfigFacade;
import org.mingy.jsfs.model.SalesLogStat;
import org.mingy.jsfs.model.Staff;
import org.mingy.kernel.context.GlobalBeanContext;
import org.mingy.kernel.util.Calendars;
import org.mingy.kernel.util.Langs;
import org.mingy.kernel.util.Strings;

public class SalesLogStatEditor extends EditorPart {

	public static final String ID = "org.mingy.jsfs.ui.SalesLogStatEditor"; //$NON-NLS-1$

	private Composite container;
	private Table table;

	public SalesLogStatEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		init();
	}

	@SuppressWarnings("unchecked")
	public void init() {
		table.removeAll();
		for (TableColumn col : table.getColumns()) {
			col.dispose();
		}
		Color blue = new Color(getSite().getShell().getDisplay(), 0, 0, 255);
		Font font = getSite().getShell().getFont();
		FontData fd = font.getFontData()[0];
		fd.setStyle(SWT.BOLD);
		Font bold = new Font(font.getDevice(), fd);
		TableColumn staffCol = new TableColumn(table, SWT.NONE);
		staffCol.setWidth(110);
		staffCol.setText("日期");
		final Set<String> lockedDays = (Set<String>) getEditorInput()
				.getAdapter(Set.class);
		final Image lockImage = Activator.getImageDescriptor(
				"/icons/lock_sales_log.gif").createImage();
		SalesLogStat salesLogStat = (SalesLogStat) getEditorInput().getAdapter(
				SalesLogStat.class);
		Staff[] staffs = salesLogStat.getStaffs();
		for (Staff staff : staffs) {
			TableColumn col = new TableColumn(table, SWT.NONE);
			col.setAlignment(SWT.RIGHT);
			col.setWidth(80);
			col.setText(staff.getName());
		}
		TableColumn statCol = new TableColumn(table, SWT.NONE);
		statCol.setAlignment(SWT.RIGHT);
		statCol.setWidth(80);
		statCol.setText("总计");
		for (Date day : salesLogStat.getDays()) {
			TableItem item = new TableItem(table, SWT.NONE);
			String date = Calendars.get10Date(day);
			item.setData(date);
			item.setText(0, date);
			if (lockedDays.contains(date)) {
				item.setImage(0, lockImage);
			}
			for (int i = 0; i < staffs.length; i++) {
				item.setText(i + 1,
						Strings.format(salesLogStat.getStat(day, staffs[i]), 1));
			}
			item.setForeground(staffs.length + 1, blue);
			item.setText(staffs.length + 1,
					Strings.format(salesLogStat.getStat(day), 1));
		}
		TableItem item = new TableItem(table, SWT.NONE);
		item.setForeground(blue);
		item.setFont(bold);
		item.setText(0, "合计");
		for (int i = 0; i < staffs.length; i++) {
			item.setText(i + 1,
					Strings.format(salesLogStat.getStat(staffs[i]), 1));
		}
		item.setText(staffs.length + 1,
				Strings.format(salesLogStat.getStat(), 1));
		final Action lockAction = new Action() {
			@Override
			public void run() {
				TableItem item = table.getSelection()[0];
				String date = (String) item.getData();
				if (MessageDialog.openConfirm(getSite().getShell(),
						Langs.getText("confirm.lock.title"),
						Langs.getText("confirm.lock.message.date", date))) {
					GlobalBeanContext.getInstance()
							.getBean(IConfigFacade.class)
							.saveConfig(date, "LOCK");
					lockedDays.add(date);
					item.setImage(0, lockImage);
				}
			}
		};
		lockAction.setText("锁定");
		lockAction.setImageDescriptor(Activator
				.getImageDescriptor("/icons/lock_sales_log.gif"));
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				if (table.getSelectionCount() > 0) {
					String date = (String) table.getSelection()[0].getData();
					if (date != null && !lockedDays.contains(date)) {
						manager.add(lockAction);
					}
				}
			}
		});
		Menu menu = menuMgr.createContextMenu(table);
		table.setMenu(menu);
	}

	@Override
	public void setFocus() {
		table.setFocus();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// Do the Save operation
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());
	}

	public void init(IEditorInput input) {
		setInput(input);
		setPartName(input.getName());
		init();
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
}
