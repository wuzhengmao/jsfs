package org.mingy.jsfs.ui.util;

import java.util.Set;

import javax.validation.ConstraintViolation;

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
		Set<ConstraintViolation<?>> violations = Validators.validateValue(
				clazz, propName, value);
		if (!violations.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (ConstraintViolation<?> violation : violations) {
				if (sb.length() > 0)
					sb.append("\n");
				sb.append(violation.getMessage());
			}
			String errorText = sb.toString();
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
