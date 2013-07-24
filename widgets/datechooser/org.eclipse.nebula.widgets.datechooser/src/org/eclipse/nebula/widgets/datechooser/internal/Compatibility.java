package org.eclipse.nebula.widgets.datechooser.internal;

import java.util.Locale;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

/**
 * compatibility layer managing incompatibilities between RAP and RCP API
 * @author amergey
 *
 */
public interface Compatibility {
	/**
	 * RWT.RAP_ACTIVE_KEYS constant
	 */
	public static final String RAP_ACTIVE_KEYS = "org.eclipse.rap.rwt.activeKeys";
	/**
	 * RWT.RAP_CANCEL_KEYS constant
	 */
	public static final String RAP_CANCEL_KEYS = "org.eclipse.rap.rwt.cancelKeys";

	/**
	 * SWT.Paint constant
	 *
	 * TODO remove it when available in RAP
	 */
	public final static int Paint = 9;

	/**
	 * Copies the selected text.
	 * <p>
	 * The current selection is copied to the clipboard.
	 * </p>
	 * @param text Text widget where to copy
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public void copy(Text text);

	/**
	 * Cuts the selected text.
	 * <p>
	 * The current selection is first copied to the clipboard and then deleted
	 * from the widget.
	 * </p>
	 * @param text Text widget where to cut
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public void cut(Text text);

	/**
	 * Pastes text from clipboard.
	 * <p>
	 * The selected text is deleted from the widget
	 * and new text inserted from the clipboard.
	 * </p>
	 * @param text Text widget where to cut
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public void paste(Text text);

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when the receiver's text is modified.
	 *
	 * @param widget the widget where listener must be removed
	 * @param listener the listener which should no longer be notified
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 * @see ModifyListener
	 */
	public void removeModifyListener(Widget widget,ModifyListener listener);

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when the receiver's selection changes.
	 *
	 * @param widget the widget where listener must be removed
	 * @param listener the listener which should no longer be notified
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 * @see SelectionListener
	 */
	public void removeSelectionListener(Widget widget,SelectionListener listener);

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when the control is verified.
	 *
	 * @param widget the widget where listener must be removed
	 * @param listener the listener which should no longer be notified
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 * @see VerifyListener
	 */
	public void removeVerifyListener(Widget widget,VerifyListener listener);

	/**
	 *
	 * @return locale to use
	 */
	public Locale getLocale();

	/**
	 * Based on the argument, perform one of the expected platform
	 * traversal action. The argument should be one of the constants:
	 * <code>SWT.TRAVERSE_ESCAPE</code>, <code>SWT.TRAVERSE_RETURN</code>,
	 * <code>SWT.TRAVERSE_TAB_NEXT</code>, <code>SWT.TRAVERSE_TAB_PREVIOUS</code>,
	 * <code>SWT.TRAVERSE_ARROW_NEXT</code>, <code>SWT.TRAVERSE_ARROW_PREVIOUS</code>,
	 * <code>SWT.TRAVERSE_PAGE_NEXT</code> and <code>SWT.TRAVERSE_PAGE_PREVIOUS</code>.
	 *
	 * @param control control to traverse
	 * @param traversal the type of traversal
	 * @return true if the traversal succeeded
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public boolean traverse (Control control, int traversal);

	/**
	 * Create an "arrow" button, SWT.ARROW is not supported in RAP, so this method provide an alternative
	 * @param composite the button parent
	 * @param left <code>true</code> if button is a left arrow, <code>false</code> if it is a right arrow
	 * @param style button style
	 * @return button instance
	 */
	public Button createArrowButton(Composite composite,boolean left,int style);
}
