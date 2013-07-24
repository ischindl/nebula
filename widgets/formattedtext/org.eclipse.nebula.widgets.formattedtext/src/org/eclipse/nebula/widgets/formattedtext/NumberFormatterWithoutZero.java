/*******************************************************************************
 * Copyright (c) 2005, 2007 Eric Wuillai.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eric Wuillai (eric@wdev91.com) - initial API and implementation
 *******************************************************************************/
package org.eclipse.nebula.widgets.formattedtext;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import org.eclipse.swt.SWT;

/**
 * This class provides formatting of <code>Number</code> values in a
 * <code>FormattedText</code>.
 * <p>
 *
 * Formatter is composed of an edit pattern and a display pattern.<br>
 * Display pattern uses the same syntax than <code>DecimalFormat</code>, and
 * uses it to compute the value to display.<br>
 * Edit pattern is more limited and composed of two part, the int part and the
 * decimal part. Formatting characters allow to specify number of digits,
 * minimal length, decimal position, grouping and negative sign.
 * <p>
 *
 * <h4>Patterns Characters</h4>
 * <table border=0 cellspacing=3 cellpadding=0 summary="Chart shows pattern letters, date/time component, presentation, and examples.">
 * <tr bgcolor="#ccccff">
 * <th align=left>Symbol</th> <th align=left>Meaning</th>
 * </tr>
 * <tr>
 * <td><code>0</code></td>
 * <td>Digit</td>
 * </tr>
 * <tr bgcolor="#eeeeff">
 * <td><code>#</code></td>
 * <td>Digit, zero shows as absent</td>
 * </tr>
 * <tr>
 * <td><code>.</code></td>
 * <td>Decimal separator</td>
 * </tr>
 * <tr bgcolor="#eeeeff">
 * <td><code>-</code></td>
 * <td>Minus sign</td>
 * </tr>
 * <tr>
 * <td><code>,</code></td>
 * <td>Grouping separator</td>
 * </tr>
 * </table>
 *
 * <h4>Examples</h4>
 * <ul>
 * <li><code>new NumberFormatter("#,##0.00")</code> - 1234.5 will edit and
 * display as "1,234.50".</li>
 * </ul>
 */
public class NumberFormatterWithoutZero extends NumberFormatter {

	private static final long serialVersionUID = 1L;
	public static final char P_DIGIT = '#';
	public static final char P_ZERODIGIT = '0';
	public static final char P_DECIMAL_SEP = '.';
	public static final char P_GROUP_SEP = ',';
	public static final char P_MINUS = '-';

	public NumberFormatterWithoutZero(String editPattern, String displayPattern) {
		super(editPattern, displayPattern, Locale.getDefault());
	}

	/**
	 * Formats the edit buffer. Inserts group separators to the right places,
	 * deletes excess decimal digits and add 0 to complete to the minimal length
	 * of int and decimal parts. The position of the cursor is preserved.
	 *
	 * @param curseur
	 *            Current position of the cursor
	 * @return New position of the cursor
	 */
	protected int format(int curseur) {
		int i = prefixLen + (negative ? 1 : 0);
		char c;

		// Inserts zeros in the int part
		if (value != null)
			while (intCount < zeroIntLen) {
				editValue.insert(i, '0');
				intCount++;
				curseur++;
			}
		while (intCount > zeroIntLen) {
			if (editValue.charAt(i) == '0' && intCount > 1) {
				// Upravene, aby nezmazal ked je jedna 0
				intCount--;
			} else if (editValue.charAt(i) != symbols.getGroupingSeparator()) {
				break;
			}
			editValue.deleteCharAt(i);
			if (curseur > i)
				curseur--;
		}

		// Recreates the groups in the int part
		if (groupLen > 0) {
			int n = intCount > groupLen ? groupLen - intCount % groupLen : 0;
			if (n == groupLen) {
				n = 0;
			}
			for (; i < editValue.length() - suffixLen; i++) {
				c = editValue.charAt(i);
				if (c >= '0' && c <= '9') {
					if (n == groupLen) {
						editValue.insert(i, symbols.getGroupingSeparator());
						if (curseur >= i) {
							curseur++;
						}
						n = 0;
					} else {
						n++;
					}
				} else if (c == symbols.getGroupingSeparator()) {
					if (n != groupLen) {
						editValue.deleteCharAt(i);
						if (curseur >= i) {
							curseur--;
						}
						i--;
					} else {
						n = 0;
					}
				} else if (c == symbols.getDecimalSeparator()) {
					if (i > 0 && editValue.charAt(i - 1) == symbols.getGroupingSeparator()) {
						editValue.deleteCharAt(i - 1);
						if (curseur >= i) {
							curseur--;
						}
						i--;
					}
					break;
				} else {
					break;
				}
			}
		}

		// Truncates / completes by zeros the decimal part
		i = editValue.indexOf(EMPTY + symbols.getDecimalSeparator());
		if (i < 0 && (zeroDecimalLen > 0 || alwaysShowDec)) {
			i = editValue.length() - suffixLen;
			editValue.insert(i, symbols.getDecimalSeparator());
		}
		if (i >= 0) {
			int j;
			for (j = i + 1; j < editValue.length() - suffixLen;) {
				c = editValue.charAt(j);
				if (c == symbols.getGroupingSeparator()) {
					editValue.deleteCharAt(j);
				} else if (c < '0' || c > '9') {
					break;
				} else {
					j++;
				}
			}
			if (fixedDec && (j - i - 1) > decimalLen) {
				editValue.delete(i + decimalLen + 1, j);
				if (curseur > i + decimalLen) {
					curseur = i + decimalLen;
				}
			} else {
				while ((j - i - 1) < zeroDecimalLen) {
					editValue.insert(j++, '0');
				}
			}
		}

		return curseur;
	}

	  public String getDisplayString() {
		    return editValue.substring(0, prefixLen)
		    			 + ((getValue() != null
		    			     ? nfDisplay.format(value) : ","))
		    			 + editValue.substring(editValue.length() - suffixLen);
	  }

	/**
	 * Returns the current value of the text control if it is a valid
	 * <code>Number</code>. If the buffer is flaged as modified, the value is
	 * recalculated by parsing with the <code>nfEdit</code> initialized with the
	 * edit pattern. If the number is not valid, returns <code>null</code>.
	 *
	 * @return current number value if valid, <code>null</code> else
	 * @see ITextFormatter#getValue()
	 */
	public Object getValue() {
		if (modified) {
			try {
				value = nfEdit.parse(editValue.substring(prefixLen, editValue.length() - suffixLen));
			} catch (ParseException e1) {
				if (zeroIntLen + zeroDecimalLen == 0 && (editValue.length() == 0 || editValue.charAt(0) == symbols.getDecimalSeparator())) {
					modified = false;
					// value = new Integer(0);
					value = null; // Upravene
				} else {
					return null;
				}
			}
			modified = false;
		}
		return value;
	}

	/**
	 * Sets the patterns and initializes the technical attributes used to manage
	 * the operations.
	 *
	 * @param edit
	 *            edit pattern
	 * @param display
	 *            display pattern
	 * @param loc
	 *            Locale to use
	 * @throws IllegalArgumentException
	 *             if a pattern is invalid
	 */
	protected void setPatterns(String edit, String display, Locale loc) {
		// Symbols
		symbols = new DecimalFormatSymbols(loc);
		nbspSeparator = symbols.getGroupingSeparator() == 0xA0;

		// Get default edit pattern if null
		if (edit == null) {
			edit = getDefaultPattern(loc);
		}

		// Analyze the edit pattern
		boolean grouping = false;
		boolean decimal = false;
		groupLen = intLen = decimalLen = zeroIntLen = zeroDecimalLen = 0;
		minus = false;
		int i;
		for (i = 0; i < edit.length(); i++) {
			switch (edit.charAt(i)) {
			case P_MINUS:
				if (i != 0) {
					SWT.error(SWT.ERROR_INVALID_ARGUMENT);
				}
				minus = true;
				break;
			case P_GROUP_SEP:
				if (!decimal) {
					grouping = true;
					groupLen = 0;
				} else {
					SWT.error(SWT.ERROR_INVALID_ARGUMENT);
				}
				break;
			case P_DECIMAL_SEP:
				grouping = false;
				decimal = true;
				break;
			case P_ZERODIGIT:
				if (value != null) // Upravene
					if (decimal) {
						zeroDecimalLen++;
					} else {
						zeroIntLen++;
					}
				// Continue on P_DIGIT...
			case P_DIGIT:
				if (decimal) {
					decimalLen++;
				} else {
					intLen++;
					if (grouping) {
						groupLen++;
					}
				}
				break;
			default:
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
		}
		editPattern = edit;
		nfEdit = new DecimalFormat(minus ? editPattern.substring(1) : editPattern, symbols);
		editValue = new StringBuffer();

		// Create the display formatter
		nfDisplay = display != null ? new DecimalFormat(display, symbols) : nfEdit;

		// Initialize the edit cache
		intCount = 0;
		for (i = 0; i < zeroIntLen; i++) {
			editValue.append('0');
			intCount++;
		}
		if (alwaysShowDec || zeroDecimalLen > 0) {
			editValue.append(symbols.getDecimalSeparator());
		}
		for (i = 0; i < zeroDecimalLen; i++) {
			editValue.append('0');
		}
		// value = new Long(0L);
		value = null;
	}
}
