package org.eclipse.nebula.widgets.tablecombo;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TableItem;

public class CheckBoxTableCombo extends TableCombo {

	protected String title;

	public CheckBoxTableCombo(Composite parent, int style) {
		super(parent, style);
	}

	public CheckBoxTableCombo(Composite parent, int style, String title) {
		super(parent, style,SWT.READ_ONLY | SWT.CHECK | SWT.SINGLE | SWT.V_SCROLL);
		this.title = title;
		setText(title);
	}


	public List<TableItem> getChecked() {
		List<TableItem> checked = new ArrayList<TableItem>();
		for (TableItem ti : this.getTable().getItems()) {
			if (ti.getChecked()) {
				checked.add(ti);
			}
		}
		return checked;
	}

	public List<String> getCheckedText() {
		List<String> checked = new ArrayList<String>();
		for (TableItem ti : this.getTable().getItems()) {
			if (ti.getChecked()) {
				checked.add(ti.getText());
			}
		}
		return checked;
	}

	public void checkAll(boolean check) {
		for (TableItem ti : this.getTable().getItems()) {
			ti.setChecked(check);
		}
	}

	public void check(String text, boolean check) {
		for (TableItem ti : this.getTable().getItems()) {
			if (ti.getText().equals(text)) {
				ti.setChecked(check);
			}
		}
	}

	public void check(int index, boolean check) {
		this.getTable().getItem(index).setChecked(check);
	}

	@Override
	void handleTableMouseUp(Event event) {
	}

	@Override
	void handleTableSelection(Event event) {
		Event e = new Event();
		e.time = event.time;
		e.stateMask = event.stateMask;
		e.doit = event.doit;

		if (event.detail == SWT.CHECK) {
			e.detail = SWT.CHECK;
			e.item = (TableItem) event.item;
		}
		select(-1);
		setText(title);
		notifyListeners(SWT.Selection, e);
		event.doit = e.doit;
	}
}
