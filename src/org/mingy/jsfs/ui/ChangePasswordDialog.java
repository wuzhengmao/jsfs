package org.mingy.jsfs.ui;

import java.security.NoSuchAlgorithmException;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.mingy.jsfs.facade.IConfigFacade;
import org.mingy.jsfs.model.Role;
import org.mingy.jsfs.model.RoleManager;
import org.mingy.kernel.context.GlobalBeanContext;
import org.mingy.kernel.crypto.Signature;
import org.mingy.kernel.util.Langs;

public class ChangePasswordDialog extends TitleAreaDialog {

	private Text txtOldPassword;
	private Text txtNewPassword;
	private Text txtRepeat;

	public ChangePasswordDialog(IWorkbenchWindow window) {
		super(window.getShell());
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Langs.getText("change_password.dialog.title"));
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle(Langs.getText("change_password.dialog.title"));
		setMessage(Langs.getText("change_password.dialog.message", RoleManager
				.getInstance().getRole().toString()));

		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 10;
		container.setLayout(layout);

		Label label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label.setText("旧口令：");
		txtOldPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		txtOldPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		txtOldPassword.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setErrorMessage(null);
			}
		});

		Label label1 = new Label(container, SWT.NONE);
		label1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label1.setText("新口令：");
		txtNewPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		txtNewPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		txtNewPassword.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setErrorMessage(null);
			}
		});

		Label label2 = new Label(container, SWT.NONE);
		label2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label2.setText("重复新口令：");
		txtRepeat = new Text(container, SWT.BORDER | SWT.PASSWORD);
		txtRepeat.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		txtRepeat.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setErrorMessage(null);
			}
		});

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

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	@Override
	protected void okPressed() {
		Role role = RoleManager.getInstance().getRole();
		String password;
		try {
			password = Signature.digestBase64("SHA", txtOldPassword.getText()
					.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		if (!password.equals(GlobalBeanContext.getInstance()
				.getBean(IConfigFacade.class)
				.getConfig("password." + role.name().toLowerCase()))) {
			setErrorMessage(Langs
					.getText("error.change_password.password_wrong"));
			txtOldPassword.selectAll();
			txtOldPassword.setFocus();
			return;
		}
		if (!txtNewPassword.getText().equals(txtRepeat.getText())) {
			setErrorMessage(Langs
					.getText("error.change_password.password_diff"));
			txtNewPassword.selectAll();
			txtNewPassword.setFocus();
			return;
		}
		try {
			password = Signature.digestBase64("SHA", txtNewPassword.getText()
					.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		GlobalBeanContext.getInstance().getBean(IConfigFacade.class)
				.saveConfig("password." + role.name().toLowerCase(), password);
		super.okPressed();
	}
}
