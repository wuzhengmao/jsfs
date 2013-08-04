package org.mingy.jsfs.ui;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ObservableSetContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.mingy.jsfs.model.Position;
import org.mingy.jsfs.model.RewardRule;
import org.mingy.jsfs.ui.model.Catalog;

public class ViewRewardRuleEditor extends AbstractFormEditor<RewardRule> {
	public ViewRewardRuleEditor() {
	}

	public static final String ID = "org.mingy.jsfs.ui.ViewRewardRuleEditor"; //$NON-NLS-1$

	private Text txtName;
	private TableViewer tvPosition;
	private Text txtGoodsOrType;
	private Text txtScript;
	private Text txtDescription;

	@Override
	protected RewardRule init() {
		Catalog catalog = (Catalog) getEditorInput().getAdapter(Catalog.class);
		RewardRule rule = (RewardRule) catalog.getValue();
		setPartName("查看规则 - " + rule.getName());
		return rule;
	}

	@Override
	protected void createControls(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 10;
		container.setLayout(layout);

		Label label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label.setText("规则名：");

		txtName = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label label_1 = new Label(container, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_1.setText("职位：");

		tvPosition = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_table = new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1);
		gd_table.heightHint = 108;
		tvPosition.getTable().setLayoutData(gd_table);
		ObservableSetContentProvider tableContentProvider = new ObservableSetContentProvider();
		tvPosition.setLabelProvider(new ObservableMapLabelProvider(
				PojoObservables.observeMap(
						tableContentProvider.getKnownElements(),
						Position.class, "name")));
		tvPosition.setContentProvider(tableContentProvider);

		Label label_2 = new Label(container, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_2.setText("商品或分类：");

		txtGoodsOrType = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		txtGoodsOrType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblNewLabel.setText("脚本：");

		txtScript = new Text(container, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI
				| SWT.READ_ONLY);
		GridData gd_txtScript = new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1);
		txtScript.setLayoutData(gd_txtScript);

		Label label_3 = new Label(container, SWT.NONE);
		label_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_3.setText("描述：");

		txtDescription = new Text(container, SWT.BORDER | SWT.MULTI
				| SWT.READ_ONLY);
		GridData gd_txtDescription = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1);
		gd_txtDescription.heightHint = 108;
		txtDescription.setLayoutData(gd_txtDescription);
	}

	@Override
	protected void initDataBindings(final RewardRule bean) {
		bindText(txtName, bean, "name");
		tvPosition.setInput(BeanProperties.set("positions").observe(bean));
		bindText(txtGoodsOrType, bean, "goodsOrType.name");
		bindText(txtScript, bean, "script");
		bindText(txtDescription, bean, "description");
	}

	@Override
	protected void fillForm(RewardRule bean) {
		// do nothing
	}

	@Override
	protected void fillData(RewardRule bean) {
		// do nothing
	}

	@Override
	protected void save(RewardRule bean) {
		// do nothing
	}

	@Override
	protected void postSave(RewardRule bean) {
		// do nothing
	}

	@Override
	public void setFocus() {
		txtName.setFocus();
	}
}
