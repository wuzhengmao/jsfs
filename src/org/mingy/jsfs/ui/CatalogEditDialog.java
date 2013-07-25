package org.mingy.jsfs.ui;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.mingy.jsfs.Activator;
import org.mingy.jsfs.ui.util.IAggregateValidateListener;
import org.mingy.jsfs.ui.util.UIUtils;

public abstract class CatalogEditDialog extends TitleAreaDialog implements
		IAggregateValidateListener {

	protected DataBindingContext dataBindingContext;
	protected Text txtName;
	protected Text txtDescription;
	private Map<Control, ControlDecoration> decoratorMap = new LinkedHashMap<Control, ControlDecoration>();

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public CatalogEditDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setImage(Activator.getImageDescriptor("/icons/folder.gif")
				.createImage());
		newShell.setText(getDialogTitle());
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitleImage(Activator.getImageDescriptor("/icons/folder_wiz.gif")
				.createImage());
		setTitle(getDialogTitle());
		setMessage(getDialogMessage());
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 10;
		container.setLayout(layout);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label.setText("名称：");

		txtName = new Text(container, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label label_1 = new Label(container, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false,
				1, 1));
		label_1.setText("描述：");

		txtDescription = new Text(container, SWT.BORDER | SWT.MULTI);
		GridData gd_txtDescription = new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1);
		gd_txtDescription.heightHint = 108;
		txtDescription.setLayoutData(gd_txtDescription);

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		dataBindingContext = new DataBindingContext();
		initDataBindings();
		return contents;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	@Override
	public void onValidateFinish(boolean passed) {
		getButton(IDialogConstants.OK_ID).setEnabled(passed);
	}

	protected void bindText(Control control, Object bean, String propName) {
		UIUtils.bindText(dataBindingContext, control, bean, propName, null,
				null, decoratorMap, this);
	}

	protected abstract String getDialogTitle();

	protected abstract String getDialogMessage();

	protected abstract void initDataBindings();

	protected abstract boolean onOk();

	@Override
	protected void okPressed() {
		if (onOk()) {
			super.okPressed();
		}
	}
}
