package org.mingy.jsfs.ui;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.mingy.jsfs.Activator;
import org.mingy.jsfs.facade.ISalesLogFacade;
import org.mingy.jsfs.model.Goods;
import org.mingy.jsfs.model.GoodsType;
import org.mingy.jsfs.model.SalesLog;
import org.mingy.jsfs.model.SalesLog.SalesLogDetail;
import org.mingy.jsfs.model.Staff;
import org.mingy.jsfs.ui.model.Catalogs;
import org.mingy.jsfs.ui.model.MergeDateConverter;
import org.mingy.jsfs.ui.model.MergeTimeConverter;
import org.mingy.jsfs.ui.util.IAggregateValidateListener;
import org.mingy.jsfs.ui.util.UIUtils;
import org.mingy.kernel.context.GlobalBeanContext;
import org.mingy.kernel.util.Langs;
import org.mingy.kernel.util.Strings;
import org.mingy.kernel.util.Validators;

public class SalesLogEditDialog extends TitleAreaDialog implements
		IAggregateValidateListener {

	private SalesLog salesLog;
	private DateTime dtSalesDate;
	private DateTime dtSalesTime;
	private ComboViewer cvStaff;
	private Map<Control, ControlDecoration> decoratorMap = new LinkedHashMap<Control, ControlDecoration>();
	private boolean editMode;
	private Table table;
	private Text txtMemo;
	private TableViewer tableViewer;
	private ComboBoxViewerCellEditor cvceGoods;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public SalesLogEditDialog(Shell parentShell, SalesLog salesLog) {
		super(parentShell);
		if (salesLog == null) {
			this.salesLog = new SalesLog();
			this.salesLog.setSalesTime(new Date());
			this.editMode = false;
		} else {
			this.salesLog = salesLog;
			this.editMode = true;
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setImage(Activator.getImageDescriptor(
				"/icons/edit_sales_log.gif").createImage());
		newShell.setText(editMode ? Langs.getText("edit_salesLog.dialog.title")
				: Langs.getText("new_salesLog.dialog.title"));
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitleImage(Activator.getImageDescriptor(
				"/icons/edit_sales_log_wiz.gif").createImage());
		if (editMode) {
			setTitle(Langs.getText("edit_salesLog.dialog.title"));
			setMessage(Langs.getText("edit_salesLog.dialog.message"));
		} else {
			setTitle(Langs.getText("new_salesLog.dialog.title"));
			setMessage(Langs.getText("new_salesLog.dialog.message"));
		}

		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		GridLayout layout = new GridLayout(3, false);
		layout.horizontalSpacing = 10;
		container.setLayout(layout);
		Label label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label.setText("销售时间：");
		dtSalesDate = new DateTime(container, SWT.BORDER | SWT.DROP_DOWN);
		dtSalesDate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		dtSalesTime = new DateTime(container, SWT.BORDER | SWT.DROP_DOWN
				| SWT.TIME | SWT.SHORT);
		dtSalesTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		Label label2 = new Label(container, SWT.NONE);
		label2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label2.setText("员工：");
		cvStaff = new ComboViewer(container, SWT.READ_ONLY);
		cvStaff.getCombo().setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label label_1 = new Label(container, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_1.setText("销售明细：");

		tableViewer = new TableViewer(container, SWT.BORDER
				| SWT.FULL_SELECTION);
		tableViewer.addOpenListener(new IOpenListener() {
			public void open(OpenEvent event) {
			}
		});
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
			}
		});
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_table.heightHint = 100;
		table.setLayoutData(gd_table);

		UIUtils.createControlDecoration(table, decoratorMap);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn = tableViewerColumn.getColumn();
		tableColumn.setWidth(160);
		tableColumn.setText("商品");

		cvceGoods = new ComboBoxViewerCellEditor(tableViewer.getTable(),
				SWT.READ_ONLY);
		cvceGoods
				.setActivationStyle(ComboBoxViewerCellEditor.DROP_DOWN_ON_MOUSE_ACTIVATION
						| ComboBoxViewerCellEditor.DROP_DOWN_ON_PROGRAMMATIC_ACTIVATION);
		cvceGoods.setValidator(new ICellEditorValidator() {
			@Override
			public String isValid(Object value) {
				return getErrorMessage();
			}
		});
		cvceGoods.getViewer().addSelectionChangedListener(
				new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						Goods goods = (Goods) ((IStructuredSelection) event
								.getSelection()).getFirstElement();
						String error = UIUtils.getErrorMessage(Validators
								.validateValue(SalesLogDetail.class, "goods",
										goods));
						if (error != null) {
							setErrorMessage(error);
							return;
						}
						for (SalesLogDetail detail : salesLog.getDetails()) {
							if (goods.equals(detail.getGoods())
									&& !detail
											.equals(((IStructuredSelection) tableViewer
													.getSelection())
													.getFirstElement())) {
								error = Langs
										.getText("error.input.goodsIsDuplicated");
								setErrorMessage(error);
								return;
							}
						}
						setErrorMessage(null);
					}
				});
		cvceGoods.addListener(new ICellEditorListener() {

			@Override
			public void editorValueChanged(boolean oldValidState,
					boolean newValidState) {
				// setErrorMessage(cvceGoods.getErrorMessage());
			}

			@Override
			public void cancelEditor() {
				if (getErrorMessage() != null) {
					SalesLogDetail detail = (SalesLogDetail) ((IStructuredSelection) tableViewer
							.getSelection()).getFirstElement();
					if (detail.getGoods() == null) {
						salesLog.getDetails().remove(detail);
						checkSalesLogDetails();
					}
					setErrorMessage(null);
				}
			}

			@Override
			public void applyEditorValue() {
				if (getErrorMessage() != null) {
					SalesLogDetail detail = (SalesLogDetail) ((IStructuredSelection) tableViewer
							.getSelection()).getFirstElement();
					if (detail.getGoods() == null) {
						salesLog.getDetails().remove(detail);
						checkSalesLogDetails();
					}
					setErrorMessage(null);
				}
			}
		});
		tableViewerColumn.setEditingSupport(new EditingSupport(tableViewer) {

			@Override
			protected void setValue(Object element, Object value) {
				SalesLogDetail detail = (SalesLogDetail) element;
				if (value != null) {
					detail.setGoods((Goods) value);
					if (detail.getCount() == null || detail.getCount() < 1) {
						detail.setCount(1);
					}
					detail.setTotalPrice(detail.getGoods().getSalesPrice()
							* detail.getCount());
					tableViewer.update(detail, new String[] { "goods", "count",
							"totalPrice" });
				} else {
					salesLog.getDetails().remove(detail);
					checkSalesLogDetails();
				}
			}

			@Override
			protected Object getValue(Object element) {
				return ((SalesLogDetail) element).getGoods();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return cvceGoods;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_1 = tableViewerColumn_1.getColumn();
		tableColumn_1.setAlignment(SWT.RIGHT);
		tableColumn_1.setWidth(80);
		tableColumn_1.setText("数量");

		final TextCellEditor tceCount = new TextCellEditor(
				tableViewer.getTable(), SWT.RIGHT);
		tceCount.setValidator(new ICellEditorValidator() {
			@Override
			public String isValid(Object value) {
				String error;
				String text = (String) value;
				if (text == null || text.length() == 0) {
					error = UIUtils.getErrorMessage(Validators.validateValue(
							SalesLogDetail.class, "count", null));
					if (error != null) {
						return error;
					}
				}
				Integer i = Strings.parseInt(text);
				if (i == null) {
					return Langs.getText("error.input.NaN", "数量");
				}
				return UIUtils.getErrorMessage(Validators.validateValue(
						SalesLogDetail.class, "count", i));
			}
		});
		tceCount.addListener(new ICellEditorListener() {

			@Override
			public void editorValueChanged(boolean oldValidState,
					boolean newValidState) {
				setErrorMessage(tceCount.getErrorMessage());
			}

			@Override
			public void cancelEditor() {
				setErrorMessage(null);
			}

			@Override
			public void applyEditorValue() {
				setErrorMessage(null);
			}
		});
		tableViewerColumn_1.setEditingSupport(new EditingSupport(tableViewer) {

			@Override
			protected void setValue(Object element, Object value) {
				SalesLogDetail detail = (SalesLogDetail) element;
				Integer count = Strings.parseInt((String) value);
				detail.setCount(count);
				if (detail.getGoods() != null && detail.getCount() != null) {
					detail.setTotalPrice(detail.getGoods().getSalesPrice()
							* count);
				} else {
					detail.setTotalPrice(null);
				}
				tableViewer.update(detail,
						new String[] { "count", "totalPrice" });
			}

			@Override
			protected Object getValue(Object element) {
				return Strings.objToString(((SalesLogDetail) element)
						.getCount());
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return tceCount;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_2 = tableViewerColumn_2.getColumn();
		tableColumn_2.setAlignment(SWT.RIGHT);
		tableColumn_2.setWidth(100);
		tableColumn_2.setText("总价");

		final TextCellEditor tcePrice = new TextCellEditor(
				tableViewer.getTable(), SWT.RIGHT);
		tcePrice.setValidator(new ICellEditorValidator() {
			@Override
			public String isValid(Object value) {
				String error;
				String text = (String) value;
				if (text == null || text.length() == 0) {
					error = UIUtils.getErrorMessage(Validators.validateValue(
							SalesLogDetail.class, "totalPrice", null));
					if (error != null) {
						return error;
					}
				}
				Double i = Strings.parseDouble(text);
				if (i == null) {
					return Langs.getText("error.input.NaN", "总价");
				}
				return UIUtils.getErrorMessage(Validators.validateValue(
						SalesLogDetail.class, "totalPrice", i));
			}
		});
		tcePrice.addListener(new ICellEditorListener() {

			@Override
			public void editorValueChanged(boolean oldValidState,
					boolean newValidState) {
				setErrorMessage(tcePrice.getErrorMessage());
			}

			@Override
			public void cancelEditor() {
				setErrorMessage(null);
			}

			@Override
			public void applyEditorValue() {
				setErrorMessage(null);
			}
		});
		tableViewerColumn_2.setEditingSupport(new EditingSupport(tableViewer) {

			@Override
			protected void setValue(Object element, Object value) {
				SalesLogDetail detail = (SalesLogDetail) element;
				detail.setTotalPrice(Strings.parseDouble((String) value));
				tableViewer.update(detail, new String[] { "totalPrice" });
			}

			@Override
			protected Object getValue(Object element) {
				return Strings.objToString(((SalesLogDetail) element)
						.getTotalPrice());
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return tcePrice;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});

		final Action addAction = new Action() {
			@Override
			public void run() {
				SalesLogDetail detail = salesLog.new SalesLogDetail();
				if (detail != null) {
					salesLog.getDetails().add(detail);
					checkSalesLogDetails();
					tableViewer.setSelection(new StructuredSelection(detail),
							true);
					tableViewer.editElement(detail, 0);
				}
			}
		};
		addAction.setText("新增行");
		addAction.setImageDescriptor(Activator
				.getImageDescriptor("/icons/add.gif"));

		final Action delAction = new Action() {
			@Override
			public void run() {
				ISelection selection = tableViewer.getSelection();
				if (!selection.isEmpty()
						&& selection instanceof IStructuredSelection) {
					for (Iterator<?> it = ((IStructuredSelection) selection)
							.iterator(); it.hasNext();) {
						Object element = it.next();
						salesLog.getDetails().remove(element);
					}
					checkSalesLogDetails();
				}
			}
		};
		delAction.setText("删除行");
		delAction.setImageDescriptor(Activator
				.getImageDescriptor("/icons/delete.gif"));

		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				manager.add(addAction);
				if (!tableViewer.getSelection().isEmpty()) {
					manager.add(delAction);
				}
			}
		});
		Menu menu = menuMgr.createContextMenu(table);
		table.setMenu(menu);

		Label label_2 = new Label(container, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_2.setText("备注：");

		txtMemo = new Text(container, SWT.BORDER | SWT.MULTI);
		GridData gd_txtMemo = new GridData(SWT.FILL, SWT.CENTER, true, false,
				2, 1);
		gd_txtMemo.heightHint = 60;
		txtMemo.setLayoutData(gd_txtMemo);

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "保存", true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		initDataBindings();
		return contents;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 410);
	}

	@Override
	public void onValidateFinish(boolean passed) {
		getButton(IDialogConstants.OK_ID).setEnabled(passed);
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		IObservableMap observeMap = BeansObservables.observeMap(
				listContentProvider.getKnownElements(), Staff.class, "name");
		cvStaff.setLabelProvider(new ObservableMapLabelProvider(observeMap));
		cvStaff.setContentProvider(listContentProvider);
		cvStaff.setInput(Catalogs.getStaffList());
		//
		IObservableValue observableValue = PojoProperties.value("salesTime")
				.observe(salesLog);
		UIUtils.bindSelection(bindingContext, dtSalesDate, salesLog,
				"salesTime", new MergeDateConverter(observableValue), null,
				decoratorMap, this);
		UIUtils.bindSelection(bindingContext, dtSalesTime, salesLog,
				"salesTime", new MergeTimeConverter(observableValue), null,
				decoratorMap, this);
		UIUtils.bindSelection(bindingContext, cvStaff, salesLog, "staff", null,
				null, decoratorMap, this);
		UIUtils.bindText(bindingContext, txtMemo, salesLog, "memo", null, null,
				decoratorMap, this);
		//
		ObservableListContentProvider tableContentProvider = new ObservableListContentProvider();
		IObservableMap[] observeMaps = PojoObservables.observeMaps(
				tableContentProvider.getKnownElements(), SalesLogDetail.class,
				new String[] { "goods", "count", "totalPrice" });
		tableViewer
				.setLabelProvider(new ObservableMapLabelProvider(observeMaps) {
					@Override
					public String getColumnText(Object element, int columnIndex) {
						if (columnIndex == 0) {
							Goods goods = (Goods) attributeMaps[0].get(element);
							return goods != null ? goods.getName() : null;
						} else {
							return super.getColumnText(element, columnIndex);
						}
					}
				});
		tableViewer.setContentProvider(tableContentProvider);
		tableViewer.setInput(salesLog.getObservableDetails());
		checkSalesLogDetails();
		//
		ObservableListContentProvider listContentProvider_1 = new ObservableListContentProvider();
		IObservableMap observeMap_1 = BeansObservables.observeMap(
				listContentProvider_1.getKnownElements(), Goods.class, "name");
		cvceGoods
				.setLabelProvider(new ObservableMapLabelProvider(observeMap_1));
		cvceGoods.setContentProvider(listContentProvider_1);
		cvceGoods.setInput(Catalogs.getGoodsList());

		return bindingContext;
	}

	private void checkSalesLogDetails() {
		ControlDecoration controlDecoration = decoratorMap.get(table);
		String error = UIUtils.getErrorMessage(Validators.validateProperty(
				salesLog, "details"));
		if (error != null) {
			controlDecoration.setDescriptionText(error);
			controlDecoration.show();
			controlDecoration.showHoverText(error);
			onValidateFinish(false);
		} else {
			controlDecoration.hideHover();
			controlDecoration.hide();
			controlDecoration.setDescriptionText(null);
			onValidateFinish(!UIUtils.hasError(decoratorMap));
		}
	}

	@SuppressWarnings("unused")
	private SalesLogDetail createSalesLogDetail() {
		SalesLogDetail detail = salesLog.new SalesLogDetail();
		for (GoodsType goodsType : Catalogs.getGoodsTypeList()) {
			boolean b = false;
			for (SalesLogDetail sld : salesLog.getDetails()) {
				if (sld.getGoods().getType().equals(goodsType)) {
					b = true;
					break;
				}
			}
			if (!b) {
				for (Goods goods : Catalogs.getGoodsList()) {
					if (goods.getType().equals(goodsType)) {
						detail.setGoods(goods);
						break;
					}
				}
				if (detail.getGoods() != null) {
					break;
				}
			}
		}
		if (detail.getGoods() == null) {
			for (Goods goods : Catalogs.getGoodsList()) {
				boolean b = false;
				for (SalesLogDetail sld : salesLog.getDetails()) {
					if (sld.getGoods().equals(goods)) {
						b = true;
						break;
					}
				}
				if (!b) {
					detail.setGoods(goods);
					break;
				}
			}
		}
		if (detail.getGoods() != null) {
			detail.setCount(1);
			detail.setTotalPrice(detail.getGoods().getSalesPrice());
			return detail;
		} else {
			return null;
		}
	}

	@Override
	protected void okPressed() {
		try {
			GlobalBeanContext.getInstance().getBean(ISalesLogFacade.class)
					.saveSalesLog(salesLog);
			super.okPressed();
		} catch (Exception e) {
			MessageDialog.openError(
					getShell(),
					Langs.getText("error.save.title"),
					Langs.getText("error.save.message", e.getClass().getName()
							+ ": " + e.getLocalizedMessage()));
		}
	}
}
