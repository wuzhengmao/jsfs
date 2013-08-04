package org.mingy.jsfs.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.mingy.jsfs.model.Staff;
import org.mingy.jsfs.ui.model.Catalog;
import org.mingy.jsfs.ui.model.DateFormatConverter;
import org.mingy.jsfs.ui.model.ValueToLabelConverter;

public class ViewStaffEditor extends AbstractFormEditor<Staff> {

	public static final String ID = "org.mingy.jsfs.ui.ViewStaffEditor"; //$NON-NLS-1$

	private Text txtName;
	private Text txtSex;
	private Text txtBirthday;
	private Text txtContacts;
	private Text txtPosition;
	private Text txtMemo;

	@Override
	protected Staff init() {
		Catalog catalog = (Catalog) getEditorInput().getAdapter(Catalog.class);
		Staff staff = (Staff) catalog.getValue();
		setPartName("查看员工 - " + staff.getName());
		return staff;
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

		txtName = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label label_1 = new Label(container, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_1.setText("性别：");

		txtSex = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		txtSex.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));

		Label label_2 = new Label(container, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_2.setText("出生年月：");

		txtBirthday = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		txtBirthday.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label label_3 = new Label(container, SWT.NONE);
		label_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_3.setText("联系方式：");

		txtContacts = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		txtContacts.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label label_4 = new Label(container, SWT.NONE);
		label_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_4.setText("职位：");

		txtPosition = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		txtPosition.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
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
	protected void initDataBindings(Staff bean) {
		bindText(txtName, bean, "name");
		bindText(txtSex, bean, "sex", null, new ValueToLabelConverter("sex"));
		bindText(txtBirthday, bean, "birthday", null, new DateFormatConverter(
				"yyyy年M月"));
		bindText(txtContacts, bean, "contacts");
		bindText(txtPosition, bean, "position.name");
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
		// do nothing
	}

	@Override
	protected void postSave(Staff bean) {
		// do nothing
	}

	@Override
	public void setFocus() {
		txtName.setFocus();
	}
}
