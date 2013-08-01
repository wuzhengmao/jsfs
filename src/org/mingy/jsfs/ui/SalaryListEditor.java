package org.mingy.jsfs.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.mingy.jsfs.model.Goods;
import org.mingy.jsfs.model.GoodsType;
import org.mingy.jsfs.model.Salary;
import org.mingy.jsfs.model.Salary.StatDetail;
import org.mingy.kernel.util.Strings;

public class SalaryListEditor extends EditorPart {

	public static final String ID = "org.mingy.jsfs.ui.SalaryListEditor"; //$NON-NLS-1$

	private Composite container;
	private Table table;

	public SalaryListEditor() {
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
		Color green = new Color(getSite().getShell().getDisplay(), 0, 255, 0);
		Font font = getSite().getShell().getFont();
		FontData fd = font.getFontData()[0];
		fd.setStyle(SWT.BOLD);
		Font bold = new Font(font.getDevice(), fd);
		TableColumn staffCol = new TableColumn(table, SWT.LEFT);
		staffCol.setWidth(80);
		staffCol.setText("员工");
		TableColumn salaryCol = new TableColumn(table, SWT.RIGHT);
		salaryCol.setWidth(80);
		salaryCol.setText("工资");
		TableColumn amountCol = new TableColumn(table, SWT.RIGHT);
		amountCol.setWidth(80);
		amountCol.setText("销售额");
		List<Salary> salaryList = (List<Salary>) getEditorInput().getAdapter(
				List.class);
		List<StatDetail> list = new ArrayList<StatDetail>();
		for (Salary salary : salaryList) {
			for (StatDetail detail : salary.getDetails()) {
				boolean b = false;
				for (StatDetail sd : list) {
					if (detail.getGoodsOrType().equals(sd.getGoodsOrType())) {
						b = true;
						break;
					}
				}
				if (!b) {
					StatDetail sd = new StatDetail();
					sd.setGoodsOrType(detail.getGoodsOrType());
					list.add(sd);
				}
			}
		}
		Collections.sort(list, new Comparator<StatDetail>() {
			@Override
			public int compare(StatDetail o1, StatDetail o2) {
				if (o1.getGoodsOrType().getClass() != o2.getGoodsOrType()
						.getClass()) {
					return o1.getGoodsOrType() instanceof GoodsType ? -1 : 1;
				} else if (o1.getGoodsOrType().getClass() == GoodsType.class) {
					return ((GoodsType) o1.getGoodsOrType()).getId().compareTo(
							((GoodsType) o2.getGoodsOrType()).getId());
				} else {
					return ((Goods) o1.getGoodsOrType()).getId().compareTo(
							((Goods) o2.getGoodsOrType()).getId());
				}
			}
		});
		for (StatDetail sd : list) {
			TableColumn col = new TableColumn(table, SWT.RIGHT);
			col.setWidth(80);
			col.setText(sd.getGoodsOrType() instanceof GoodsType ? "["
					+ sd.getGoodsOrType().getName() + "]" : sd.getGoodsOrType()
					.getName());
			TableColumn col1 = new TableColumn(table, SWT.RIGHT);
			col1.setWidth(80);
			TableColumn col2 = new TableColumn(table, SWT.RIGHT);
			col2.setWidth(80);
		}
		Double summary = 0d, salesSummary = 0d;
		for (Salary salary : salaryList) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, salary.getStaff().getName());
			item.setText(1, Strings.format(salary.getAmount(), 1));
			item.setForeground(1, blue);
			item.setText(2, Strings.format(salary.getSalesAmount(), 1));
			item.setForeground(2, blue);
			for (int i = 0; i < list.size(); i++) {
				for (StatDetail detail : salary.getDetails()) {
					StatDetail sd = list.get(i);
					if (detail.getGoodsOrType().equals(sd.getGoodsOrType())) {
						item.setText(i * 3 + 3,
								Strings.objToString(detail.getCount()));
						item.setText(i * 3 + 4,
								Strings.format(detail.getPrice(), 1));
						item.setText(i * 3 + 5,
								Strings.format(detail.getAmount(), 1));
						item.setForeground(i * 3 + 5, blue);
						sd.setCount(sd.getCount() + detail.getCount());
						sd.setPrice(sd.getPrice() + detail.getPrice());
						sd.setAmount(sd.getAmount() + detail.getAmount());
						break;
					}
				}
			}
			summary += salary.getAmount();
			salesSummary += salary.getSalesAmount();
		}
		{
			TableItem item = new TableItem(table, SWT.NONE);
			item.setForeground(blue);
			item.setFont(bold);
			item.setText(0, "合计");
			item.setText(1, Strings.format(summary, 1));
			item.setText(2, Strings.format(salesSummary, 1));
			for (int i = 0; i < list.size(); i++) {
				StatDetail sd = list.get(i);
				item.setText(i * 3 + 4, Strings.format(sd.getPrice(), 1));
				item.setText(i * 3 + 5, Strings.format(sd.getAmount(), 1));
			}
		}
		{
			TableItem item = new TableItem(table, SWT.NONE);
			item.setForeground(green);
			item.setFont(bold);
			item.setText(0, "盈利");
			item.setText(2, Strings.format(salesSummary - summary, 1));
			for (int i = 0; i < list.size(); i++) {
				StatDetail sd = list.get(i);
				item.setText(i * 3 + 4,
						Strings.format(sd.getPrice() - sd.getAmount(), 1));
			}
		}
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
