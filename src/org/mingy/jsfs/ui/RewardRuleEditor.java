package org.mingy.jsfs.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.DecoratingObservableList;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.mingy.jsfs.facade.IRewardRuleFacade;
import org.mingy.jsfs.model.Position;
import org.mingy.jsfs.model.RewardRule;
import org.mingy.jsfs.ui.model.Catalog;
import org.mingy.jsfs.ui.model.CatalogToValueConverter;
import org.mingy.jsfs.ui.model.Catalogs;
import org.mingy.jsfs.ui.model.ValueToCatalogConverter;
import org.mingy.jsfs.ui.util.UIUtils;
import org.mingy.jsfs.ui.viewer.CTreeComboViewer;
import org.mingy.kernel.context.GlobalBeanContext;
import org.mingy.kernel.util.Validators;

public class RewardRuleEditor extends AbstractFormEditor<RewardRule> {
	public RewardRuleEditor() {
	}

	public static final String ID = "org.mingy.jsfs.ui.RewardRuleEditor"; //$NON-NLS-1$

	private Text txtName;
	private TableViewer tvPosition;
	private CTreeComboViewer cvGoodsOrType;
	private Text txtScript;
	private Text txtDescription;

	@Override
	protected RewardRule init() {
		Catalog catalog = (Catalog) getEditorInput().getAdapter(Catalog.class);
		RewardRule rule = (RewardRule) catalog.getValue();
		setPartName(rule != null ? "修改规则 - " + rule.getName() : "新增规则");
		RewardRule bean = new RewardRule();
		if (rule != null) {
			rule.copyTo(bean);
		}
		return bean;
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

		txtName = new Text(container, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		txtName.addModifyListener(defaultModifyListener);

		Label label_1 = new Label(container, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_1.setText("职位：");

		tvPosition = CheckboxTableViewer.newCheckList(container, SWT.BORDER
				| SWT.FULL_SELECTION | SWT.CHECK);
		GridData gd_table = new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1);
		gd_table.heightHint = 108;
		tvPosition.getTable().setLayoutData(gd_table);

		Label label_2 = new Label(container, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_2.setText("商品或分类：");

		cvGoodsOrType = new CTreeComboViewer(container, SWT.BORDER
				| SWT.READ_ONLY);
		cvGoodsOrType.getTree().setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cvGoodsOrType.addSelectionChangedListener(defaultModifyListener);

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblNewLabel.setText("脚本：");

		txtScript = new Text(container, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_txtScript = new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1);
		txtScript.setLayoutData(gd_txtScript);
		txtScript.addModifyListener(defaultModifyListener);

		Label label_3 = new Label(container, SWT.NONE);
		label_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_3.setText("描述：");

		txtDescription = new Text(container, SWT.BORDER | SWT.MULTI);
		GridData gd_txtDescription = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1);
		gd_txtDescription.heightHint = 108;
		txtDescription.setLayoutData(gd_txtDescription);
		txtDescription.addModifyListener(defaultModifyListener);

		ObservableListContentProvider tableContentProvider = new ObservableListContentProvider();
		tvPosition.setLabelProvider(new ObservableMapLabelProvider(
				PojoObservables.observeMap(
						tableContentProvider.getKnownElements(),
						Position.class, "name")));
		tvPosition.setContentProvider(tableContentProvider);
		tvPosition.setInput(Catalogs.getPositionList());
		ObservableListTreeContentProvider contentProvider = new ObservableListTreeContentProvider(
				new IObservableFactory() {
					@Override
					public IObservable createObservable(Object target) {
						return new DecoratingObservableList(
								((Catalog) target).getObservableChildren(),
								false);
					}
				}, null);
		cvGoodsOrType.setContentProvider(contentProvider);
		ObservableMapLabelProvider labelProvider = new ObservableMapLabelProvider(
				BeanProperties.value("label").observeDetail(
						contentProvider.getKnownElements()));
		cvGoodsOrType.setLabelProvider(labelProvider);
		cvGoodsOrType.setAutoExpandLevel(2);
		cvGoodsOrType.setInput(Catalogs.getCatalog(Catalog.TYPE_GOODS));
	}

	@Override
	protected void initDataBindings(final RewardRule bean) {
		bindText(txtName, bean, "name");
		final ControlDecoration controlDecoration = createControlDecoration(tvPosition
				.getTable());
		IObservableSet observeMultiSelectionTvPosition = ViewerProperties
				.checkedElements(Position.class).observe(tvPosition);
		IObservableSet positionsRuleObserveSet = BeanProperties
				.set("positions").observe(bean);
		bean.addPropertyChangeListener("positions", defaultModifyListener);
		bean.addPropertyChangeListener("positions",
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						checkPositions(bean, controlDecoration);
					}
				});
		checkPositions(bean, controlDecoration);
		dataBindingContext.bindSet(observeMultiSelectionTvPosition,
				positionsRuleObserveSet, null, null);
		bindSelection(cvGoodsOrType, bean, "goodsOrType",
				new CatalogToValueConverter(Object.class),
				new ValueToCatalogConverter(Object.class));
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
		GlobalBeanContext.getInstance().getBean(IRewardRuleFacade.class)
				.saveRule(bean);
	}

	@Override
	protected void postSave(RewardRule bean) {
		Catalog catalog = (Catalog) getEditorInput().getAdapter(Catalog.class);
		Catalogs.updateCatalog(catalog, bean);
	}

	@Override
	public void setFocus() {
		txtName.setFocus();
	}

	private void checkPositions(RewardRule bean,
			ControlDecoration controlDecoration) {
		String errorText = UIUtils.getErrorMessage(Validators.validateProperty(
				bean, "positions"));
		if (errorText != null) {
			controlDecoration.setDescriptionText(errorText);
			controlDecoration.show();
			controlDecoration.showHoverText(errorText);
		} else {
			controlDecoration.hideHover();
			controlDecoration.hide();
			controlDecoration.setDescriptionText(null);
		}
	}
}
