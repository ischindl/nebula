package org.eclipse.nebula.widgets.cdatetime;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * @since 3.3
 *
 */
public class CDateTimeMod extends CDateTime {

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#addListener(int, org.eclipse.swt.widgets.Listener)
	 */
	public void addListener(int eventType, Listener listener) {
		super.addListener(eventType, listener);
		if(eventType == SWT.Modify) {
			if (checkText()) {
				text.getControl().addListener(eventType, listener);
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#removeListener(int, org.eclipse.swt.widgets.Listener)
	 */
	public void removeListener(int eventType, Listener listener) {
		super.removeListener(eventType, listener);
		if(eventType == SWT.Modify) {
			if (checkText()) {
				text.getControl().removeListener(eventType, listener);
			}
		}
	}

	/**
	 * @param parent
	 * @param style
	 */
	public CDateTimeMod(Composite parent, int style) {
		super(parent, style);
	}

	protected void createPicker() {
		super.createPicker();
		super.pickerPanel.getWidget().addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event event) {
				event.doit = false;
				setOpen(false);
			}
		});
	}
}
