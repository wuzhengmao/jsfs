package org.mingy.jsfs.ui;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.mingy.jsfs.facade.IGoodsFacade;
import org.mingy.jsfs.model.Goods;
import org.mingy.jsfs.model.GoodsType;
import org.mingy.jsfs.ui.model.Catalog;
import org.mingy.jsfs.ui.model.CatalogToValueConverter;
import org.mingy.jsfs.ui.model.Catalogs;
import org.mingy.jsfs.ui.model.ValueToCatalogConverter;
import org.mingy.kernel.context.GlobalBeanContext;

public class GoodsEditor extends AbstractFormEditor<Goods> {

	public static final String ID = "org.mingy.jsfs.ui.GoodsEditor"; //$NON-NLS-1$

	private ComboViewer cvType;
	private Text txtName;
	private Text txtSalesPrice;
	private Text txtMemo;

	@Override
	protected Goods init() {
		Catalog catalog = (Catalog) getEditorInput().getAdapter(Catalog.class);
		Goods goods = (Goods) catalog.getValue();
		setPartName(goods != null ? "修改商品 - " + goods.getName() : "新增商品");
		Goods bean = new Goods();
		if (goods != null) {
			goods.copyTo(bean);
		} else {
			if (catalog.getParent().isSub()) {
				bean.setType((GoodsType) catalog.getParent().getValue());
			}
		}
		return bean;
	}

	@Override
	protected void createControls(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 10;
		container.setLayout(layout);

		Label label_4 = new Label(container, SWT.NONE);
		label_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_4.setText("类型：");

		cvType = new ComboViewer(container, SWT.READ_ONLY);
		cvType.getCombo().setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cvType.addSelectionChangedListener(defaultModifyListener);

		Label label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label.setText("名称：");

		txtName = new Text(container, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		txtName.addModifyListener(defaultModifyListener);

		Label label_2 = new Label(container, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_2.setText("售价：");

		txtSalesPrice = new Text(container, SWT.BORDER);
		txtSalesPrice.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		txtSalesPrice.addModifyListener(defaultModifyListener);

		Label label_5 = new Label(container, SWT.NONE);
		label_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_5.setText("备注：");

		txtMemo = new Text(container, SWT.BORDER | SWT.MULTI);
		GridData gd_txtMemo = new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1);
		gd_txtMemo.heightHint = 108;
		txtMemo.setLayoutData(gd_txtMemo);
		txtMemo.addModifyListener(defaultModifyListener);
	}

	@Override
	protected void initDataBindings(Goods bean) {
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		IObservableMap observeMap = BeansObservables.observeMap(
				listContentProvider.getKnownElements(), Catalog.class, "label");
		cvType.setLabelProvider(new ObservableMapLabelProvider(observeMap));
		cvType.setContentProvider(listContentProvider);
		cvType.setInput(Catalogs.getCatalog(Catalog.TYPE_GOODS).getChildren());
		bindSelection(cvType, bean, "type", new CatalogToValueConverter(
				GoodsType.class), new ValueToCatalogConverter(GoodsType.class));
		bindText(txtName, bean, "name");
		bindText(txtSalesPrice, bean, "salesPrice");
		bindText(txtMemo, bean, "memo");
	}

	@Override
	protected void fillForm(Goods bean) {
		// do nothing
	}

	@Override
	protected void fillData(Goods bean) {
		// do nothing
	}

	@Override
	protected void save(Goods bean) {
		GlobalBeanContext.getInstance().getBean(IGoodsFacade.class)
				.saveGoods(bean);
	}

	@Override
	protected void postSave(Goods bean) {
		Catalog catalog = (Catalog) getEditorInput().getAdapter(Catalog.class);
		Catalogs.updateCatalog(catalog, bean);
	}

	@Override
	public void setFocus() {
		if (cvType.getSelection().isEmpty()) {
			cvType.getCombo().setFocus();
		} else {
			txtName.setFocus();
		}
	}
}
