package org.eclipse.nebula.widgets.internal.singlesourcing;

import java.text.MessageFormat;

/**
 * Code inspired from snippet found in eclipsesource single sourcing guide.
 * It load implementation of abstract facade from fragment (RAP or RCP)
 * 
 * FIXME it should probably be moved in a common bundle to be shared across other single sourced bundles
 * @author amergey
 *
 */
public class ImplementationLoader {
	
	private static final String ERROR_MSG = "Could not load implementation for {0}";
	private static final String IMPL = "Impl";

	public static Object newInstance(final Class type){
		String name = type.getName();
		Object result = null;
		try{
			result = type.getClassLoader().loadClass(name + IMPL).newInstance();
		} catch (Throwable throwable){
			throw new RuntimeException(MessageFormat.format(ERROR_MSG, name), throwable);
		}
		return result;
	}
	
}
