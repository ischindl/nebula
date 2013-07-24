package org.eclipse.nebula.widgets.cdatetime;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * @since 3.3
 *
 */
public class CDateTimeMod extends CDateTime {

	/**
	 * @param parent
	 * @param style
	 */
	public CDateTimeMod(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * change method to public
	 */
	public void addModifyListener(ModifyListener listener) {
		super.addModifyListener(listener);
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
