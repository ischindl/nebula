package org.eclipse.nebula.widgets.datechooser.internal;

import java.text.MessageFormat;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * Utility class allowing to get/store used colors in JFace {@link ColorRegistry}, so we let the registry managing 
 * resources for us either for RAP and RCP  
 * @author amergey
 *
 */
public class ColorUtil {
	private final static String COLOR_KEY="DC_{0}_{1}_{2}"; //$NON-NLS-1$

	public static Color getColor(int red, int green, int blue){
		String key = MessageFormat.format(COLOR_KEY, red,green,blue);
		ColorRegistry reg = JFaceResources.getColorRegistry();
		Color color = reg.get(key);
		if(color==null){
			reg.put(key, new RGB(red,green,blue));
			color = reg.get(key);
		}
		return color;
	}
}