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
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.mingy.jsfs.facade.IStaffFacade;
import org.mingy.jsfs.model.Position;
import org.mingy.jsfs.model.Staff;
import org.mingy.jsfs.ui.model.Catalog;
import org.mingy.jsfs.ui.model.CatalogToValueConverter;
import org.mingy.jsfs.ui.model.Catalogs;
import org.mingy.jsfs.ui.model.ValueToCatalogConverter;
import org.mingy.jsfs.ui.util.UIUtils;
import org.mingy.kernel.context.GlobalBeanContext;

public class StaffEditor extends AbstractFormEditor<Staff> {

	public static final String ID = "org.mingy.jsfs.ui.StaffEditor"; //$NON-NLS-1$

	private Text txtName;
	private ComboViewer cvSex;
	private DateTime dtBirthday;
	private Text txtContacts;
	private ComboViewer cvPosition;
	private Text txtMemo;

	@Override
	protected Staff init() {
		Catalog catalog = (Catalog) getEditorInput().getAdapter(Catalog.class);
		Staff staff = (Staff) catalog.getValue();
		setPartName(staff != null ? "修改员工 - " + staff.getName() : "新增员工");
		Staff bean = new Staff();
		if (staff != null) {
			staff.copyTo(bean);
		} else {
			if (catalog.getParent().isSub()) {
				bean.setPosition((Position) catalog.getParent().getValue());
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

		Label label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label.setText("姓名：");

		txtName = new Text(container, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		txtName.addModifyListener(defaultModifyListener);

		Label label_1 = new Label(container, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_1.setText("性别：");

		cvSex = new ComboViewer(container, SWT.READ_ONLY);
		cvSex.getCombo().setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		UIUtils.initCombo(cvSex, "sex", Integer.class);
		cvSex.addSelectionChangedListener(defaultModifyListener);

		Label label_2 = new Label(container, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_2.setText("出生年月：");

		dtBirthday = new DateTime(container, SWT.BORDER | SWT.DROP_DOWN
				| SWT.SHORT);
		dtBirthday.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		dtBirthday.addSelectionListener(defaultModifyListener);

		Label label_3 = new Label(container, SWT.NONE);
		label_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_3.setText("联系方式：");

		txtContacts = new Text(container, SWT.BORDER);
		txtContacts.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		txtContacts.addModifyListener(defaultModifyListener);

		Label label_4 = new Label(container, SWT.NONE);
		label_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_4.setText("职位：");

		cvPosition = new ComboViewer(container, SWT.READ_ONLY);
		cvPosition.getCombo().setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cvPosition.addSelectionChangedListener(defaultModifyListener);

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
	protected void initDataBindings(Staff bean) {
		bindText(txtName, bean, "name");
		bindSelection(cvSex, bean, "sex");
		bindSelection(dtBirthday, bean, "birthday");
		bindText(txtContacts, bean, "contacts");
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		IObservableMap observeMap = BeansObservables.observeMap(
				listContentProvider.getKnownElements(), Catalog.class, "label");
		cvPosition.setLabelProvider(new ObservableMapLabelProvider(observeMap));
		cvPosition.setContentProvider(listContentProvider);
		cvPosition.setInput(Catalogs.getCatalog(Catalog.TYPE_STAFF)
				.getChildren());
		bindSelection(cvPosition, bean, "position",
				new CatalogToValueConverter(Position.class),
				new ValueToCatalogConverter(Position.class));
		bindText(txtMemo, bean, "memo");
	}

	@Override
	protected void fillForm(Staff bean) {
		// do nothing
	}

	@Override
	protected void fillData(Staff bean) {
		// do nothing
	}

	@Override
	protected void save(Staff bean) {
		GlobalBeanContext.getInstance().getBean(IStaffFacade.class)
				.saveStaff(bean);
	}

	@Override
	protected void postSave(Staff bean) {
		Catalog catalog = (Catalog) getEditorInput().getAdapter(Catalog.class);
		Catalogs.updateCatalog(catalog, bean);
	}

	@Override
	public void setFocus() {
		txtName.setFocus();
	}
}
