package org.mingy.jsfs.ui.util;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.mingy.kernel.util.Validators;

public class Jsr303BeanValidator implements IValidator {

	@SuppressWarnings("rawtypes")
	private Class clazz;
	private String propName;
	private ControlDecoration controlDecoration;

	public Jsr303BeanValidator(Class<?> clazz, String propName) {
		this.clazz = clazz;
		this.propName = propName;
	}

	public Jsr303BeanValidator(Class<?> clazz, String propName,
			ControlDecoration controlDecoration) {
		this(clazz, propName);
		this.controlDecoration = controlDecoration;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IStatus validate(Object value) {
		String errorText = UIUtils.getErrorMessage(Validators.validateValue(
				clazz, propName, value));
		if (errorText != null) {
			if (controlDecoration != null) {
				controlDecoration.setDescriptionText(errorText);
				controlDecoration.show();
				controlDecoration.showHoverText(errorText);
			}
			return ValidationStatus.error(errorText);
		} else {
			if (controlDecoration != null) {
				controlDecoration.hideHover();
				controlDecoration.hide();
				controlDecoration.setDescriptionText(null);
			}
			return ValidationStatus.ok();
		}
	}
}
