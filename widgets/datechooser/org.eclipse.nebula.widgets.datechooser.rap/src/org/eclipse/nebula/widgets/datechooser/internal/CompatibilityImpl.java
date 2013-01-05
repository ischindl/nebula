package org.eclipse.nebula.widgets.datechooser.internal;

import java.util.Locale;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

public class CompatibilityImpl implements Compatibility {

	@Override
	public void copy(Text text) {
		// TODO Auto-generated method stub
	}

	@Override
	public void cut(Text text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paste(Text text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeModifyListener(Widget widget, ModifyListener listener) {
		widget.removeListener(SWT.Modify, (Listener) listener);
		// ModifyEvent.removeListener(widget, listener);
	}

	@Override
	public void removeSelectionListener(Widget widget, SelectionListener listener) {
		widget.removeListener(SWT.Selection,(Listener)listener);
//		SelectionEvent.removeListener(widget, listener);
	}

	@Override
	public void removeVerifyListener(Widget widget, VerifyListener listener) {
		widget.removeListener(SWT.Verify,(Listener)listener);
//		VerifyEvent.removeListener(widget, listener);
	}

	@Override
	public Locale getLocale() {
		return RWT.getLocale();
	}

	@Override
	public boolean traverse(Control control, int traversal) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Button createArrowButton(Composite composite, boolean left, int style) {
		Button button = new Button(composite, SWT.PUSH | style);
		button.setText(left ? "<" : ">");
		return button;
	}
}
