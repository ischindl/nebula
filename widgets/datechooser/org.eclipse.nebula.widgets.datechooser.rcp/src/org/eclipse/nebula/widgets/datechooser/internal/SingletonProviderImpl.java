/**
 * 
 */
package org.eclipse.nebula.widgets.datechooser.internal;

import org.eclipse.nebula.widgets.datechooser.DateChooserTheme;


/**
 * RCP implementation for <code>SingletonProvider</code> facade
 * @author amergey
 *
 */
public class SingletonProviderImpl implements SingletonProvider {
	private static DateChooserThemeProvider INSTANCE=null;

	/**
	 * @see org.eclipse.nebula.widgets.datechooser.internal.SingletonProvider#getThemeProviderInstance()
	 */
	public DateChooserThemeProvider getThemeProviderInstance() {
		if(INSTANCE==null){
			INSTANCE = new DateChooserThemeProvider();
			
			//set old constant for compatibility
			//TODO remove later
			DateChooserTheme.BLUE=INSTANCE.getBlue();
			DateChooserTheme.CLASSIC=INSTANCE.getClassic();
			DateChooserTheme.YELLOW=INSTANCE.getYellow();
			DateChooserTheme.SYSTEM=INSTANCE.getSystem();
			DateChooserTheme.GRAY=INSTANCE.getGray();
		}
		return INSTANCE;
	}

}
