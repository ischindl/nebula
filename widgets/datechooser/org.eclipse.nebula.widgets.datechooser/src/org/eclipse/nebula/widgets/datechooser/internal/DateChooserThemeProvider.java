package org.eclipse.nebula.widgets.datechooser.internal;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.nebula.widgets.datechooser.DateChooser;
import org.eclipse.nebula.widgets.datechooser.DateChooserCombo;
import org.eclipse.nebula.widgets.datechooser.DateChooserTheme;
import org.eclipse.nebula.widgets.internal.singlesourcing.ImplementationLoader;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Singleton provider for <code>DateChooserTheme</code> 
 * 
 * TODO As improvement, investigate if in RAP, default theme can be created according to RAP current theme 
 * @author amergey
 *
 */
public class DateChooserThemeProvider {
	/** Default image filename */
	private static final String IMAGE = "/org/eclipse/nebula/widgets/datechooser/DateChooserCombo.png";
	
	private static final SingletonProvider PROVIDER;
	static {
		PROVIDER = (SingletonProvider) ImplementationLoader.newInstance(SingletonProvider.class);
	}
	
	public static DateChooserThemeProvider getInstance(){
		return PROVIDER.getThemeProviderInstance();
	}

	public DateChooserThemeProvider(){
		//we register combo image here because it is done once for RCP and for each session in RAP
		JFaceResources.getImageRegistry().put(IMAGE, new Image(Display.getCurrent(),
		                        DateChooserCombo.class.getResourceAsStream(IMAGE)));
	}
	
	
	private DateChooserTheme gray;
	private DateChooserTheme blue;
	private DateChooserTheme yellow;
	private DateChooserTheme classic;
	private DateChooserTheme system;
	private DateChooserTheme defaultTheme;
	
	/** @return GRAY theme. Default */
	public DateChooserTheme getGray() {
		if(gray==null){
			gray=new DateChooserTheme();
		}
		return gray;
	}
	/** @return BLUE theme */
	public DateChooserTheme getBlue() {
		if(blue==null){
			blue=createBlueTheme();
		}
		return blue;
	}
	/** @return YELLOW theme */
	public DateChooserTheme getYellow() {
		if(yellow==null){
			yellow=createYellowTheme();
		}
		return yellow;
	}
	/**@return CLASSIC theme */
	public DateChooserTheme getClassic() {
		if(classic==null){
			classic=createClassicTheme();
		}
		return classic;
	}
	/**@return SYSTEM theme */
	public DateChooserTheme getSystem() {
		if(system==null){
			system=createSystemTheme();
		}
		return system;
	}
	/** @return Default theme */
	public DateChooserTheme getDefaultTheme() {
		if(defaultTheme==null){
			defaultTheme=getGray();
		}
		return defaultTheme;
	}
	/**
	 * set default theme
	 * @param defTheme
	 */
	public void setDefaultTheme(DateChooserTheme defTheme) {
		if ( defTheme == null ){
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		defaultTheme = defTheme;
	}
	
	/**
	 * TODO it could be good to provide an API helping t customize this image
	 * @return image to use as combo button 
	 */
	public Image getChooserImage(){
		return JFaceResources.getImage(IMAGE);
	}
	
	/**
	 * Creates the BLUE theme.
	 * 
	 * @return BLUE theme
	 */
	private static DateChooserTheme createBlueTheme() {
		Display display = Display.getCurrent();
		DateChooserTheme theme = new DateChooserTheme();

		Color headerBackround = ColorUtil.getColor(170, 190, 220);
		theme.setHeaderBack(headerBackround);
		theme.setGridHeaderBackground(headerBackround);
		theme.setDayCellBackground(ColorUtil.getColor( 190, 220, 240));
		theme.setExtraMonthForeground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
		theme.setWeekendForeground(display.getSystemColor(SWT.COLOR_RED));
		theme.setTodayBackground(display.getSystemColor(SWT.COLOR_WHITE));

		return theme;
	}

	/**
	 * Creates the CLASSIC theme.
	 * 
	 * @return CLASSIC theme
	 */
	private static DateChooserTheme createClassicTheme() {
		Display display = Display.getCurrent();
		DateChooserTheme theme = new DateChooserTheme();

		Color borderBackground = display.getSystemColor(SWT.COLOR_WHITE);
		theme.setBorderBackground(borderBackground);
		theme.setGridHeaderBackground(borderBackground);
		theme.setBorderSize(3);
		theme.setCellPadding(3);
		theme.setGridVisible(DateChooser.GRID_LINES);

		return theme;
	}

	/**
	 * Creates the SYSTEM theme.
	 * 
	 * @return SYSTEM theme
	 */
	private static DateChooserTheme createSystemTheme() {
		Display display = Display.getCurrent();
		DateChooserTheme theme = new DateChooserTheme();

		Color borderBackground = display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
		Color gridHeaderForeground = display.getSystemColor(SWT.COLOR_LIST_FOREGROUND);
		Color headerForeground = display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
		Color gridLinesColor = display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
		theme.setBorderBackground(borderBackground);
		theme.setHeaderBack(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		theme.setHeaderForg(headerForeground);
		theme.setGridHeaderBackground(borderBackground);
		theme.setGridHeaderForeground(gridHeaderForeground);
		theme.setGridLinesColor(gridLinesColor);
		theme.setDayCellBackground(borderBackground);
		theme.setDayCellForeground(gridHeaderForeground);
		theme.setSelectedBackground(display.getSystemColor(SWT.COLOR_LIST_SELECTION));
		theme.setSelectedForeground(display.getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
		theme.setTodayBackground(borderBackground);
		theme.setTodayForeground(gridHeaderForeground);
		theme.setExtraMonthForeground(gridLinesColor);
		theme.setWeekendForeground(headerForeground);
		theme.setFocusColor(display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND));
		theme.setBorderSize(3);
		theme.setCellPadding(3);
		theme.setGridVisible(DateChooser.GRID_LINES);

		return theme;
	}

	/**
	 * Creates the YELLOW theme.
	 * 
	 * @return YELLOW theme
	 */
	private static DateChooserTheme createYellowTheme() {
		Display display = Display.getCurrent();
		DateChooserTheme theme = new DateChooserTheme();

		Color headerBackground		 = ColorUtil.getColor(190, 180, 60);
		theme.setHeaderBack(headerBackground);
		theme.setGridHeaderBackground(headerBackground);
		theme.setDayCellBackground(ColorUtil.getColor(255, 255, 170));
		theme.setExtraMonthForeground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
		theme.setWeekendForeground(display.getSystemColor(SWT.COLOR_RED));
		theme.setTodayBackground(display.getSystemColor(SWT.COLOR_GRAY));
		theme.setSelectedBackground(display.getSystemColor(SWT.COLOR_DARK_GREEN));
		theme.setSelectedForeground(display.getSystemColor(SWT.COLOR_WHITE));

		return theme;
	}
	
	
}
