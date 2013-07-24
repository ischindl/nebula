package org.eclipse.nebula.widgets.datechooser.internal;

import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.internal.SWTEventListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.swt.widgets.Widget;

public class CompatibilityImpl implements Compatibility {

	public void copy(Text text) {
		text.copy();
	}

	public void cut(Text text) {
		text.cut();
	}

	public void paste(Text text) {
		text.paste();
	}

	public void removeModifyListener(Widget widget, ModifyListener listener) {
		removeListener(widget, SWT.Modify, listener);
	}
	
	public void removeListener(Widget widget, int type,SWTEventListener swtListen){
		Listener[] listeners = widget.getListeners(type);
		for (int i = 0; i < listeners.length; i++) {
			if(listeners[i] instanceof TypedListener){
				TypedListener typedListener = (TypedListener) listeners [i];
				if(typedListener.getEventListener().equals(swtListen)){
					widget.removeListener(type, typedListener);
					return;
				}
			}
		}
	}

	public void removeSelectionListener(Widget widget,
			SelectionListener listener) {
		removeListener(widget,SWT.Selection, listener);
		removeListener(widget,SWT.DefaultSelection,listener);	
	}

	public void removeVerifyListener(Widget widget, VerifyListener listener) {
		removeListener(widget,SWT.Verify, listener);
	}

	public Locale getLocale() {
		return Locale.getDefault();
	}

	public boolean traverse(Control control, int traversal) {
		return control.traverse(traversal);
	}
}
