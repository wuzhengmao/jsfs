package org.mingy.jsfs.ui;

import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
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

public class LoginDialog extends TitleAreaDialog {

	private static final Log logger = LogFactory.getLog(LoginDialog.class);

	private IWorkbenchWindow window;
	private ComboViewer cvRole;
	private Text txtPassword;

	public LoginDialog(IWorkbenchWindow window) {
		super(window.getShell());
		this.window = window;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Langs.getText("login.dialog.title"));
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle(Langs.getText("login.dialog.title"));
		setMessage(Langs.getText("login.dialog.message"));

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
		label.setText("身份：");
		cvRole = new ComboViewer(container, SWT.READ_ONLY);
		cvRole.getCombo().setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cvRole.setContentProvider(new ArrayContentProvider());
		cvRole.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return Langs.getLabel("role", ((Role) element).ordinal());
			}
		});
		cvRole.setInput(new Role[] { Role.ADMIN, Role.ACCOUNTING });
		cvRole.getCombo().select(
				RoleManager.getInstance().getRole() != Role.ADMIN ? 0 : 1);

		Label label1 = new Label(container, SWT.NONE);
		label1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label1.setText("口令：");
		txtPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		txtPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		txtPassword.addModifyListener(new ModifyListener() {
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
		createButton(parent, IDialogConstants.OK_ID, "登录", true);
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
		Role role = (Role) ((IStructuredSelection) cvRole.getSelection())
				.getFirstElement();
		String password;
		try {
			password = Signature.digestBase64("SHA", txtPassword.getText()
					.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		if (!password.equals(GlobalBeanContext.getInstance()
				.getBean(IConfigFacade.class)
				.getConfig("password." + role.name().toLowerCase()))) {
			setErrorMessage(Langs.getText("error.login.message"));
			txtPassword.selectAll();
			txtPassword.setFocus();
			return;
		}
		if (logger.isInfoEnabled()) {
			logger.info("Login: " + role.name());
		}
		RoleManager.getInstance().setRole(role);
		window.getActivePage().closeAllEditors(false);
		CatalogView view = (CatalogView) window.getActivePage().findView(
				CatalogView.ID);
		if (view != null) {
			view.syncToolbarStatus();
		}
		super.okPressed();
	}
}
