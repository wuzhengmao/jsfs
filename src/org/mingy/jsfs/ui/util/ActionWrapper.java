package org.mingy.jsfs.ui.util;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Event;

public class ActionWrapper implements IAction {

	private IAction action;
	private String text;
	private String description;
	private String tooltipText;

	public ActionWrapper(IAction action, String text, String description,
			String tooltipText) {
		this.action = action;
		this.text = text;
		this.description = description;
		this.tooltipText = tooltipText;
	}

	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		action.addPropertyChangeListener(listener);
	}

	@Override
	public int getAccelerator() {
		return action.getAccelerator();
	}

	@Override
	public String getActionDefinitionId() {
		return action.getActionDefinitionId();
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public ImageDescriptor getDisabledImageDescriptor() {
		return action.getDisabledImageDescriptor();
	}

	@Override
	public HelpListener getHelpListener() {
		return action.getHelpListener();
	}

	@Override
	public ImageDescriptor getHoverImageDescriptor() {
		return action.getHoverImageDescriptor();
	}

	@Override
	public String getId() {
		return action.getId();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return action.getImageDescriptor();
	}

	@Override
	public IMenuCreator getMenuCreator() {
		return action.getMenuCreator();
	}

	@Override
	public int getStyle() {
		return action.getStyle();
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String getToolTipText() {
		return tooltipText;
	}

	@Override
	public boolean isChecked() {
		return action.isChecked();
	}

	@Override
	public boolean isEnabled() {
		return action.isEnabled();
	}

	@Override
	public boolean isHandled() {
		return action.isHandled();
	}

	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		action.removePropertyChangeListener(listener);
	}

	@Override
	public void run() {
		action.run();
	}

	@Override
	public void runWithEvent(Event event) {
		action.runWithEvent(event);
	}

	@Override
	public void setActionDefinitionId(String id) {
		action.setActionDefinitionId(id);
	}

	@Override
	public void setChecked(boolean checked) {
		action.setChecked(checked);
	}

	@Override
	public void setDescription(String text) {
		this.description = text;
	}

	@Override
	public void setDisabledImageDescriptor(ImageDescriptor newImage) {
		action.setDisabledImageDescriptor(newImage);
	}

	@Override
	public void setEnabled(boolean enabled) {
		action.setEnabled(enabled);
	}

	@Override
	public void setHelpListener(HelpListener listener) {
		action.setHelpListener(listener);
	}

	@Override
	public void setHoverImageDescriptor(ImageDescriptor newImage) {
		action.setHoverImageDescriptor(newImage);
	}

	@Override
	public void setId(String id) {
		action.setId(id);
	}

	@Override
	public void setImageDescriptor(ImageDescriptor newImage) {
		action.setImageDescriptor(newImage);
	}

	@Override
	public void setMenuCreator(IMenuCreator creator) {
		action.setMenuCreator(creator);
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void setToolTipText(String text) {
		this.tooltipText = text;
	}

	@Override
	public void setAccelerator(int keycode) {
		action.setAccelerator(keycode);
	}
}
