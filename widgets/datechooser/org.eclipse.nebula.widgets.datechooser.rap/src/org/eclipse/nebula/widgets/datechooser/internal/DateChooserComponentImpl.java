/**
 *
 */
package org.eclipse.nebula.widgets.datechooser.internal;

import java.util.Locale;

import org.eclipse.nebula.widgets.datechooser.DateChooser;
import org.eclipse.rap.rwt.RWT;

/**
 * RAP implementation
 * @author amergey
 *
 */
public class DateChooserComponentImpl extends DateChooserComponent {

	@Override
	public NLSMessages getNLS(Locale locale) {
		return (NLSMessages) RWT.NLS.getISO8859_1Encoded( DateChooser.BUNDLE_NAME, NLSMessages.class );
	}

}
