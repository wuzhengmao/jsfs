package org.mingy.jsfs.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
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
import org.mingy.jsfs.ui.util.Jsr303BeanValidator;
import org.mingy.kernel.context.GlobalBeanContext;
import org.mingy.kernel.facade.IEntityDaoFacade;
import org.mingy.kernel.util.Langs;
import org.mingy.kernel.util.Validators;

public abstract class AbstractFormEditor<T> extends EditorPart {

	protected DefaultModifyListener defaultModifyListener = new DefaultModifyListener();
	protected IEntityDaoFacade entityDao = GlobalBeanContext.getInstance()
			.getBean(IEntityDaoFacade.class);
	protected DataBindingContext dataBindingContext;
	private Map<Control, ControlDecoration> decoratorMap = new HashMap<Control, ControlDecoration>();
	private T bean;
	private boolean dirty = false;

	private class DefaultModifyListener implements ModifyListener,
			ISelectionChangedListener, SelectionListener {

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			setDirty(true);
		}

		@Override
		public void modifyText(ModifyEvent e) {
			setDirty(true);
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			setDirty(true);
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// do nothing
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		createControls(parent);
		dataBindingContext = new DataBindingContext();
		initDataBindings(bean);
		fillForm(bean);
		setDirty(false);
	}

	protected abstract T init();

	protected abstract void createControls(Composite parent);

	protected abstract void initDataBindings(T bean);

	protected abstract void fillForm(T bean);

	protected abstract void fillData(T bean);

	protected abstract void save(T bean);

	protected abstract void postSave(T bean);

	protected ControlDecoration createControlDecoration(Control control) {
		ControlDecoration controlDecoration = new ControlDecoration(control,
				SWT.LEFT | SWT.TOP);
		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
		controlDecoration.setImage(fieldDecoration.getImage());
		controlDecoration.hide();
		decoratorMap.put(control, controlDecoration);
		return controlDecoration;
	}

	protected void bindText(Control control, Object bean, String propName) {
		IObservableValue observeWidget = WidgetProperties.text(SWT.Modify)
				.observe(control);
		bind(observeWidget, control, bean, propName, null, null);
	}

	protected void bindSelection(Control control, Object bean, String propName) {
		IObservableValue observeWidget = WidgetProperties.selection().observe(
				control);
		bind(observeWidget, control, bean, propName, null, null);
	}

	protected void bindSelection(Viewer viewer, Object bean, String propName) {
		IObservableValue observeWidget = ViewerProperties.singleSelection()
				.observe(viewer);
		bind(observeWidget, viewer.getControl(), bean, propName, null, null);
	}

	protected void bindSelection(Viewer viewer, Object bean, String propName,
			Converter targetToModelConverter, Converter modelToTargetConverter) {
		IObservableValue observeWidget = ViewerProperties.singleSelection()
				.observe(viewer);
		bind(observeWidget, viewer.getControl(), bean, propName,
				targetToModelConverter, modelToTargetConverter);
	}

	private void bind(IObservableValue observeWidget, Control control,
			Object bean, String propName, Converter targetToModelConverter,
			Converter modelToTargetConverter) {
		IObservableValue observeValue = PojoProperties.value(propName).observe(
				bean);
		UpdateValueStrategy targetToModel = new UpdateValueStrategy();
		targetToModel.setConverter(targetToModelConverter);
		targetToModel.setAfterConvertValidator(new Jsr303BeanValidator(bean
				.getClass(), propName, createControlDecoration(control)));
		UpdateValueStrategy modelToTarget = new UpdateValueStrategy();
		modelToTarget.setConverter(modelToTargetConverter);
		dataBindingContext.bindValue(observeWidget, observeValue,
				targetToModel, modelToTarget);
	}

	protected boolean validate(T bean) {
		Set<ConstraintViolation<T>> violations = Validators.validate(bean);
		if (!violations.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (ConstraintViolation<T> violation : violations) {
				if (sb.length() > 0)
					sb.append("\n");
				sb.append(violation.getMessage());
			}
			MessageDialog.openError(getSite().getShell(),
					Langs.getText("error.input.title"),
					Langs.getText("error.input.message", sb.toString()));
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		fillData(bean);
		if (validate(bean)) {
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
			bean = init();
			initDataBindings(bean);
			fillForm(bean);
			setDirty(false);
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
