/**
 * 
 */
package org.eclipse.nebula.widgets.datechooser.internal;

/**
 * Facade for singleton provider
 * @author amergey
 *
 */
public interface SingletonProvider {
	/**
	 * 
	 * @return {@link DateChooserThemeProvider} singleton instance
	 */
	public DateChooserThemeProvider getThemeProviderInstance();

}
