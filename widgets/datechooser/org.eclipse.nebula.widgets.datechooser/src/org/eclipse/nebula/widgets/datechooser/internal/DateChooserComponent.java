package org.eclipse.nebula.widgets.datechooser.internal;

import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

/**
 * Facade used to code some specific behavior of Date Chooser components, depending of target type (RCP or RAP) 
 * @author amergey
 *
 */
public abstract class DateChooserComponent {
	/**
	 * @return <code>AbstractCombo</code> supported styles
	 */
	public int getComboSupportedStyle(){
		return SWT.BORDER | SWT.READ_ONLY | SWT.FLAT | SWT.LEFT_TO_RIGHT;
	}

	/**
	 * Manages traverse event on text component of <code>DateChooserCombo</code> 
	 *
	 * @param composite where event has been fired
	 * @param event event
	 */
	public void comboTextTraverseEvent(Composite composite,Event event){
		Event e = new Event ();
		e.time = event.time;
		e.detail = event.detail;
		e.doit = event.doit;
		e.character = event.character;
		e.keyCode = event.keyCode;
		composite.notifyListeners(SWT.Traverse, e);
		event.doit = e.doit;
		event.detail = e.detail;
	}
	
	/**
	 * Traverse event processing for <code>DateChooser</code>
	 * @param event event to process
	 */
	public void chooserTraverseEvent(Event event){
		
	}
	
	/**
	 * @param locale locale to use
	 * @return NLS messages
	 */
	public abstract NLSMessages getNLS(Locale locale);
}
