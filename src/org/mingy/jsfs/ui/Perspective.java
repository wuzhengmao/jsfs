package org.mingy.jsfs.ui;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

public class Perspective implements IPerspectiveFactory {

	public static final String ID = "org.mingy.jsfs.ui.perspective"; //$NON-NLS-1$

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.addView(CatalogView.ID, IPageLayout.LEFT, 0.3f, editorArea);
		layout.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW,
				IPageLayout.BOTTOM, 0.7f, editorArea);
		layout.addNewWizardShortcut(NewStaffWizard.ID);
		layout.addNewWizardShortcut(NewGoodsWizard.ID);
		layout.addNewWizardShortcut(NewRewardRuleWizard.ID);
	}
}
