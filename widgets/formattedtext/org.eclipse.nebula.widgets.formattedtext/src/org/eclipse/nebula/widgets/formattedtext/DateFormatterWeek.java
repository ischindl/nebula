package org.eclipse.nebula.widgets.formattedtext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class DateFormatterWeek extends DateFormatter {

	private static final long serialVersionUID = 1L;

	public DateFormatterWeek(Locale loc) {
		this(null, null, loc);
	}

	public DateFormatterWeek(String editPattern, Locale loc) {
		this(editPattern, null, loc);
	}

	/**
	 *
	 * @param editPattern
	 * @param displayPattern
	 * @param loc
	 */
	public DateFormatterWeek(String editPattern, String displayPattern, Locale loc) {
		super(null, displayPattern, loc);
		compile(editPattern);
		if (displayPattern == null) {
			displayPattern = editPattern;
		}
		sdfDisplay = new SimpleDateFormat(displayPattern, loc);

		// Set the default value
	    calendar = CalendarWeek.getInstance(loc);
		((CalendarWeek)calendar).setFormat(sdfDisplay);

	    if ( yearStart == -1 ) {
	    	calendar.setTime(sdfDisplay.get2DigitYearStart());
	    	yearStart = calendar.get(Calendar.YEAR) % 100;
	    }
	    calendar.setTimeInMillis(0);
	    sdfDisplay.setCalendar(calendar);
	}

	public DateFormatterWeek(String editPattern, String displayPattern) {
		this(editPattern, displayPattern, Locale.getDefault());
	}

	public DateFormatterWeek(String editPattern) {
		this(editPattern, null, Locale.getDefault());
	}

	/**
	 * Constructs a new instance with all defaults :
	 * <ul>
	 * <li>edit mask in SHORT date format for the default locale</li>
	 * <li>display mask identical to the edit mask</li>
	 * <li>default locale</li>
	 * </ul>
	 */
	public DateFormatterWeek() {
		this(null, null, Locale.getDefault());
	}

	/**
	 * Returns the default edit pattern for the given <code>Locale</code>.
	 * <p>
	 *
	 * A <code>DateFormat</code> object is instanciated with SHORT format for both the date part for the given locale. The corresponding pattern string is then retrieved by calling the
	 * <code>toPattern</code>.
	 * <p>
	 *
	 * Default patterns are stored in a cache with ISO3 language and country codes as key. So they are computed only once by locale.
	 *
	 * @param loc
	 *            locale
	 * @return edit pattern for the locale
	 */
	public String getDefaultEditPattern(Locale loc) {
		if (loc == null) {
			loc = Locale.getDefault();
		}
		String key = "DW" + loc.toString();
		String pattern = (String) cachedPatterns.get(key);
		if (pattern == null) {
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, loc);
			if (!(df instanceof SimpleDateFormat)) {
				throw new IllegalArgumentException("No default pattern for locale " + loc.getDisplayName());
			}
			StringBuffer buffer = new StringBuffer();
			buffer.append(((SimpleDateFormat) df).toPattern());
			int i;
			if (buffer.indexOf("yyy") < 0 && (i = buffer.indexOf("yy")) >= 0) {
				buffer.insert(i, "yy");
			}
			pattern = buffer.toString();
			cachedPatterns.put(key, pattern);
		}
		return pattern;
	}

	/**
	 * Compiles a given edit pattern, initializing <code>inputMask</code> and <code>inputCache</code>. The content of the edit pattern is analyzed char by char and the array of field descriptors is
	 * initialized. Pattern chars allowed are : y, M, d, H, h, s, S, a. The presence of other pattern chars defined in <code>SimpleDateFormat</code> will raised an
	 * <code>IllegalArgumentException</code>.
	 *
	 * @param editPattern
	 *            edit pattern
	 * @throws IllegalArgumentException
	 *             pattern is invalid
	 */
	private void compile(String editPattern) {
		inputMask = new StringBuffer();
		inputCache = new StringBuffer();
		fields = new FieldDesc[10];
		boolean apostrof = false;
		int fi = 0;
		int length = editPattern.length();
		for (int i = 0; i < length; i++) {
			char c = editPattern.charAt(i);
			int l = 1;
			// if text in apostrofs
			if (c == '\'') {
				apostrof = !apostrof;
				continue;
			}
			if (!apostrof) {
				while (i < length - 1 && editPattern.charAt(i + 1) == c) {
					i++;
					l++;
				}
				isValidCharPattern(c);
				switch (c) {
				case 'y': // Year
					fields[fi] = new FieldDesc();
					fields[fi].field = Calendar.YEAR;
					fields[fi].minLen = fields[fi].maxLen = l <= 2 ? 2 : 4;
					if (fields[fi].maxLen == 2) {
						yearStart = -1;
					}
					break;
				case 'M': // Month
					fields[fi] = new FieldDesc();
					fields[fi].field = Calendar.MONTH;
					fields[fi].minLen = Math.min(l, 2);
					fields[fi].maxLen = 2;
					break;
				case 'd': // Day in month
					fields[fi] = new FieldDesc();
					fields[fi].field = Calendar.DAY_OF_MONTH;
					fields[fi].minLen = Math.min(l, 2);
					fields[fi].maxLen = 2;
					break;
				case 'H': // Hour (0-23)
					fields[fi] = new FieldDesc();
					fields[fi].field = Calendar.HOUR_OF_DAY;
					fields[fi].minLen = Math.min(l, 2);
					fields[fi].maxLen = 2;
					break;
				case 'h': // Hour (1-12 AM-PM)
					fields[fi] = new FieldDesc();
					fields[fi].field = Calendar.HOUR;
					fields[fi].minLen = Math.min(l, 2);
					fields[fi].maxLen = 2;
					break;
				case 'm': // Minutes
					fields[fi] = new FieldDesc();
					fields[fi].field = Calendar.MINUTE;
					fields[fi].minLen = Math.min(l, 2);
					fields[fi].maxLen = 2;
					break;
				case 's': // Seconds
					fields[fi] = new FieldDesc();
					fields[fi].field = Calendar.SECOND;
					fields[fi].minLen = Math.min(l, 2);
					fields[fi].maxLen = 2;
					break;
				case 'S': // Milliseconds
					fields[fi] = new FieldDesc();
					fields[fi].field = Calendar.MILLISECOND;
					fields[fi].minLen = Math.min(l, 3);
					fields[fi].maxLen = 3;
					break;
				case 'a': // AM-PM marker
					fields[fi] = new FieldDesc();
					fields[fi].field = Calendar.AM_PM;
					fields[fi].minLen = fields[fi].maxLen = 2;
					break;
				case 'w': // Week of year
					fields[fi] = new FieldDesc();
					fields[fi].field = Calendar.WEEK_OF_YEAR;
					fields[fi].minLen = fields[fi].maxLen = 2;
					break;
				default:
					for (int j = 0; j < l; j++) {
						inputMask.append('*');
						inputCache.append(c);
					}
					continue;
				}
			} else {
				inputMask.append('*');
				inputCache.append(c);
				continue;
			}
			fields[fi].empty = true;
			fields[fi].valid = false;
			char k = (char) ('0' + fi);
			for (int j = 0; j < fields[fi].minLen; j++) {
				inputMask.append(k);
				inputCache.append(SPACE);
			}
			fields[fi].index = k;
			fi++;
		}
		fieldCount = fi;
	}

	public String getDisplayString() {
		return isValid() ? ((CalendarWeek)calendar).format() : getEditString();
	}
}
