/**
 * 
 */
package org.eclipse.nebula.widgets.datechooser.internal;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.nebula.widgets.datechooser.DateChooser;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

/**
 * RCP implementation
 * @author amergey
 *
 */
public class DateChooserComponentImpl extends DateChooserComponent {
	ResourceBundle resources;

	public int getComboSupportedStyle() {
		return super.getComboSupportedStyle() | SWT.RIGHT_TO_LEFT;
	}
	
	public void comboTextTraverseEvent(Composite composite, Event event) {
		switch ( event.detail ) {
			case SWT.TRAVERSE_ARROW_PREVIOUS:
			case SWT.TRAVERSE_ARROW_NEXT:
				// The enter causes default selection and
				// the arrow keys are used to manipulate the list contents so
				// do not use them for traversal.
				event.doit = false;
				break;
			case SWT.TRAVERSE_TAB_PREVIOUS:
				event.doit = composite.traverse(SWT.TRAVERSE_TAB_PREVIOUS);
				event.detail = SWT.TRAVERSE_NONE;
				return;
		}	
		super.comboTextTraverseEvent(composite, event);
	}
	
	public void chooserTraverseEvent(Event event) {
		switch (event.detail) {
		case SWT.TRAVERSE_ARROW_NEXT :
		case SWT.TRAVERSE_ARROW_PREVIOUS :
		case SWT.TRAVERSE_PAGE_NEXT :
		case SWT.TRAVERSE_PAGE_PREVIOUS :
			event.doit = false;
			break;
		default :
			event.doit = true;
		}
	}

	public NLSMessages getNLS(Locale locale) {
		if(resources == null) {
			resources = ResourceBundle.getBundle(DateChooser.BUNDLE_NAME, locale);
		}
		NLSMessages messages = new NLSMessages();
		messages.DateChooser_nextButton = resources.getString("DateChooser_nextButton");
		messages.DateChooser_previousButton = resources.getString("DateChooser_previousButton");
		messages.DateChooser_today = resources.getString("DateChooser_today");
		messages.minimalDaysInFirstWeek = resources.getString("minimalDaysInFirstWeek");
		
		return messages;
	}
}
