package org.mingy.jsfs.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.mingy.jsfs.facade.IConfigFacade;
import org.mingy.jsfs.model.RoleManager;
import org.mingy.kernel.context.GlobalBeanContext;
import org.mingy.kernel.util.Langs;
import org.mingy.kernel.util.Strings;

public class PreferencePage extends org.eclipse.jface.preference.PreferencePage {

	private Text text;
	private IConfigFacade configFacade = GlobalBeanContext.getInstance()
			.getBean(IConfigFacade.class);

	/**
	 * Create the preference page.
	 */
	public PreferencePage() {
		setTitle("系统");
	}

	/**
	 * Create contents of the preference page.
	 * 
	 * @param parent
	 */
	@Override
	public Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));

		Label label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label.setText("系统标题：");

		text = new Text(container, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.setText(configFacade.getConfig("system.title"));
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setErrorMessage(null);
			}
		});

		return container;
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		// do nothing
	}

	@Override
	protected void performDefaults() {
		text.setText(Langs.getText("system.title"));
	}

	@Override
	public boolean performOk() {
		String title = text.getText();
		if (Strings.isBlank(title)) {
			setErrorMessage(Langs.getText("error.input.notNull", "系统标题"));
			return false;
		}
		configFacade.saveConfig("system.title", title);
		Application
				.getInstance()
				.getWindowConfigurer()
				.setTitle(
						title + " - [" + RoleManager.getInstance().getRole()
								+ "]");
		return true;
	}
}
