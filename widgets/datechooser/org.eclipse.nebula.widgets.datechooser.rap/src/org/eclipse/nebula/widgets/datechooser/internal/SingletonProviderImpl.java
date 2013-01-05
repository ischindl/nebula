/**
 *
 */
package org.eclipse.nebula.widgets.datechooser.internal;

import org.eclipse.rap.rwt.SingletonUtil;


/**
 * RAP implementation for <code>SingletonProvider</code> facade
 * @author amergey
 *
 */
public class SingletonProviderImpl implements SingletonProvider {

	/**
	 * @see org.eclipse.nebula.widgets.datechooser.internal.SingletonProvider#getThemeProviderInstance()
	 */
	@Override
	public DateChooserThemeProvider getThemeProviderInstance() {
		//return SessionSingletonBase.getInstance(DateChooserThemeProvider.class);
		return SingletonUtil.getSessionInstance(DateChooserThemeProvider.class);
	}

}
