package org.mingy.jsfs.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.mingy.jsfs.ui.util.UIUtils;
import org.mingy.kernel.util.Langs;

public abstract class AbstractFormEditor<T> extends EditorPart {

	protected DefaultModifyListener defaultModifyListener = new DefaultModifyListener();
	protected DataBindingContext dataBindingContext;
	private Map<Control, ControlDecoration> decoratorMap = new LinkedHashMap<Control, ControlDecoration>();
	private T bean;
	private boolean dirty = false;
	private boolean ignoreChange = false;

	private class DefaultModifyListener implements ModifyListener,
			ISelectionChangedListener, SelectionListener,
			PropertyChangeListener {

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			if (!ignoreChange) {
				setDirty(true);
			}
		}

		@Override
		public void modifyText(ModifyEvent e) {
			if (!ignoreChange) {
				setDirty(true);
			}
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (!ignoreChange) {
				setDirty(true);
			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// do nothing
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (!ignoreChange) {
				setDirty(true);
			}
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		createControls(parent);
		setDirty(false);
		ignoreChange = true;
		dataBindingContext = new DataBindingContext();
		initDataBindings(bean);
		fillForm(bean);
		ignoreChange = false;
	}

	protected abstract T init();

	protected abstract void createControls(Composite parent);

	protected abstract void initDataBindings(T bean);

	protected abstract void fillForm(T bean);

	protected abstract void fillData(T bean);

	protected abstract void save(T bean);

	protected abstract void postSave(T bean);

	protected ControlDecoration createControlDecoration(Control control) {
		return UIUtils.createControlDecoration(control, decoratorMap);
	}

	protected void bindText(Control control, Object bean, String propName) {
		UIUtils.bindText(dataBindingContext, control, bean, propName,
				decoratorMap);
	}

	protected void bindSelection(Control control, Object bean, String propName) {
		UIUtils.bindSelection(dataBindingContext, control, bean, propName,
				decoratorMap);
	}

	protected void bindSelection(Viewer viewer, Object bean, String propName) {
		UIUtils.bindSelection(dataBindingContext, viewer, bean, propName,
				decoratorMap);
	}

	protected void bindSelection(Viewer viewer, Object bean, String propName,
			Converter targetToModelConverter, Converter modelToTargetConverter) {
		UIUtils.bindSelection(dataBindingContext, viewer, bean, propName,
				targetToModelConverter, modelToTargetConverter, decoratorMap);
	}

	private boolean showError() {
		String error = UIUtils.getErrorMessage(decoratorMap);
		if (error != null) {
			getSite().getPage().activate(this);
			MessageDialog.openError(getSite().getShell(),
					Langs.getText("error.input.title"),
					Langs.getText("error.input.message", error));
			return true;
		} else {
			return false;
		}
	}

	protected boolean validate(T bean) {
		String error = UIUtils.validate(bean);
		if (error != null) {
			getSite().getPage().activate(this);
			MessageDialog.openError(getSite().getShell(),
					Langs.getText("error.input.title"),
					Langs.getText("error.input.message", error));
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		fillData(bean);
		if (!showError() && validate(bean)) {
			try {
				save(bean);
			} catch (Exception e) {
				MessageDialog.openError(
						getSite().getShell(),
						Langs.getText("error.save.title"),
						Langs.getText("error.save.message", e.getClass()
								.getName() + ": " + e.getLocalizedMessage()));
				return;
			}
			setDirty(false);
			postSave(bean);
			reset();
		} else {
			monitor.setCanceled(true);
		}
	}

	@Override
	public void doSaveAs() {
		// do nothing
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		bean = init();
	}

	public void init(IEditorInput input) {
		setInput(input);
		reset();
	}

	protected void reset() {
		for (Object binding : dataBindingContext.getBindings().toArray()) {
			((Binding) binding).dispose();
		}
		bean = init();
		setDirty(false);
		ignoreChange = true;
		initDataBindings(bean);
		fillForm(bean);
		ignoreChange = false;
	}

	protected T getBean() {
		return bean;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	protected void setDirty(boolean dirty) {
		this.dirty = dirty;
		firePropertyChange(EditorPart.PROP_DIRTY);
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
}
