package org.mingy.jsfs.ui;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.mingy.jsfs.model.orm.Position;
import org.mingy.jsfs.model.orm.Staff;
import org.mingy.jsfs.ui.model.Catalog;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.core.databinding.beans.BeansObservables;

public class CopyOfStaffEditor extends EditorPart {
	private DataBindingContext m_bindingContext;

	public static final String ID = "org.mingy.jsfs.ui.StaffEditor"; //$NON-NLS-1$
	private Text text;
	private Text text_1;
	private Text txtAaa;
	private Staff staff;
	private Combo combo;
	private DateTime dateTime;
	private ComboViewer comboViewer_1;
	private List<Position> positions;
	private Catalog catalog;

	public CopyOfStaffEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 10;
		container.setLayout(layout);
		
		Label label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("姓名：");
		
		text = new Text(container, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_1 = new Label(container, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText("性别：");
		
		ComboViewer comboViewer = new ComboViewer(container, SWT.READ_ONLY);
		combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_2 = new Label(container, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_2.setText("出生年月：");
		
		dateTime = new DateTime(container, SWT.BORDER | SWT.DROP_DOWN | SWT.SHORT);
		dateTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_3 = new Label(container, SWT.NONE);
		label_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_3.setText("联系方式：");
		
		text_1 = new Text(container, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_4 = new Label(container, SWT.NONE);
		label_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_4.setText("职位：");
		
		comboViewer_1 = new ComboViewer(container, SWT.READ_ONLY);
		Combo combo_1 = comboViewer_1.getCombo();
		combo_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_5 = new Label(container, SWT.NONE);
		label_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_5.setText("备注：");
		
		txtAaa = new Text(container, SWT.BORDER | SWT.MULTI);
		GridData gd_txtAaa = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtAaa.heightHint = 108;
		txtAaa.setLayoutData(gd_txtAaa);
		m_bindingContext = initDataBindings();

	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// Do the Save operation
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		// Initialize the editor part
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTextObserveWidget = WidgetProperties.text(SWT.Modify).observe(text);
		IObservableValue nameStaffObserveValue = PojoProperties.value("name").observe(staff);
		bindingContext.bindValue(observeTextTextObserveWidget, nameStaffObserveValue, null, null);
		//
		IObservableValue observeSelectionComboObserveWidget = WidgetProperties.selection().observe(combo);
		IObservableValue sexStaffObserveValue = PojoProperties.value("sex").observe(staff);
		bindingContext.bindValue(observeSelectionComboObserveWidget, sexStaffObserveValue, null, null);
		//
		IObservableValue observeSelectionDateTimeObserveWidget = WidgetProperties.selection().observe(dateTime);
		IObservableValue birthdayStaffObserveValue = PojoProperties.value("birthday").observe(staff);
		bindingContext.bindValue(observeSelectionDateTimeObserveWidget, birthdayStaffObserveValue, null, null);
		//
		IObservableValue observeSingleSelectionComboViewer_1 = ViewerProperties.singleSelection().observe(comboViewer_1);
		IObservableValue positionStaffObserveValue = PojoProperties.value("position").observe(staff);
		bindingContext.bindValue(observeSingleSelectionComboViewer_1, positionStaffObserveValue, null, null);
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		IObservableMap observeMap = BeansObservables.observeMap(listContentProvider.getKnownElements(), Catalog.class, "value");
		comboViewer_1.setLabelProvider(new ObservableMapLabelProvider(observeMap));
		comboViewer_1.setContentProvider(listContentProvider);
		//
		IObservableList selfList = Properties.selfList(Catalog.class).observe(catalog.getChildren());
		comboViewer_1.setInput(selfList);
		//
		return bindingContext;
	}
}
