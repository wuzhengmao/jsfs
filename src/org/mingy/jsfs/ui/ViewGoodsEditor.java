package org.mingy.jsfs.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.mingy.jsfs.model.Goods;
import org.mingy.jsfs.ui.model.Catalog;

public class ViewGoodsEditor extends AbstractFormEditor<Goods> {

	public static final String ID = "org.mingy.jsfs.ui.ViewGoodsEditor"; //$NON-NLS-1$

	private Text txtType;
	private Text txtName;
	private Text txtSalesPrice;
	private Text txtMemo;

	@Override
	protected Goods init() {
		Catalog catalog = (Catalog) getEditorInput().getAdapter(Catalog.class);
		Goods goods = (Goods) catalog.getValue();
		setPartName("查看商品 - " + goods.getName());
		return goods;
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

		txtType = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		txtType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label.setText("名称：");

		txtName = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label label_2 = new Label(container, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_2.setText("售价：");

		txtSalesPrice = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		txtSalesPrice.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label label_5 = new Label(container, SWT.NONE);
		label_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_5.setText("备注：");

		txtMemo = new Text(container, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);
		GridData gd_txtMemo = new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1);
		gd_txtMemo.heightHint = 108;
		txtMemo.setLayoutData(gd_txtMemo);
	}

	@Override
	protected void initDataBindings(Goods bean) {
		bindText(txtType, bean, "type.name");
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
		// do nothing
	}

	@Override
	protected void postSave(Goods bean) {
		// do nothing
	}

	@Override
	public void setFocus() {
		txtType.setFocus();
	}
}
