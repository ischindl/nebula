package org.eclipse.nebula.widgets.formattedtext;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarWeek extends GregorianCalendar {

	public static Calendar getInstance() {
		return new CalendarWeek();
	}

	public static Calendar getInstance(Locale loc) {
		return new CalendarWeek(loc);
	}

	public static Calendar zeroTime(Calendar c) {
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

	private static final long serialVersionUID = -7333541897613042584L;

	public static final long MILLISECONDS_PER_SECOND = 1000;
	public static final long MILLISECONDS_PER_MINUTE = MILLISECONDS_PER_SECOND * 60;
	public static final long MILLISECONDS_PER_HOUR = MILLISECONDS_PER_MINUTE * 60;
	public static final long MILLISECONDS_PER_DAY = MILLISECONDS_PER_HOUR * 24;
	public static final long MILLISECONDS_PER_WEEK = MILLISECONDS_PER_DAY * 7;

	public static final String PATTERN_WEEK = "ww/yyyy";

	boolean correctWeek;

	SimpleDateFormat sdf;

	public CalendarWeek() {
		super();
		init();
	}

	public CalendarWeek(int year, int month, int dayOfMonth) {
		super(year, month, dayOfMonth);
		init();
	}

	public CalendarWeek(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
		super(year, month, dayOfMonth, hourOfDay, minute);
		init();
	}

	public CalendarWeek(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second) {
		super(year, month, dayOfMonth, hourOfDay, minute, second);
		init();
	}

	public CalendarWeek(Locale aLocale) {
		super(aLocale);
		init();
	}

	public CalendarWeek(TimeZone zone) {
		super(zone);
		init();
	}

	public CalendarWeek(TimeZone zone, Locale aLocale) {
		super(zone, aLocale);
		init();
	}

	public String format() {
		if (sdf.toPattern().contains(PATTERN_WEEK)) {
			correctWeek = true;
		}
		return sdf.format(this.getTime());
	}

	@Override
	public int get(int field) {
		if (field == Calendar.YEAR && correctWeek) {
			if (super.get(Calendar.MONTH) == Calendar.JANUARY && super.get(Calendar.WEEK_OF_YEAR) > 50) {
				return super.get(field) - 1;
			}
		}
		return super.get(field);
	}

	public SimpleDateFormat getFormat() {
		return sdf;
	}

	protected void init() {
		setMinimalDaysInFirstWeek(getMinimalDays());
		setFirstDayOfWeek(Calendar.MONDAY);
	}

	private int getMinimalDays() {
		int minimalDays = 7;
		
		String minimalDaysProperty = System.getProperty("minimal.days.in.first.week");
		if (minimalDaysProperty != null && !"".equals(minimalDaysProperty));
		try {
			minimalDays = Integer.parseInt(minimalDaysProperty);
		} catch (NumberFormatException e) {
		}
		
		return minimalDays;
	}

	public boolean isCorrectWeek() {
		return correctWeek;
	}

	public int differenceInDays(CalendarWeek cal2) {
		Date date1 = zeroTime(this).getTime();

		long time1 = date1.getTime();
		long day1 = time1 / MILLISECONDS_PER_DAY;
		if (time1 % MILLISECONDS_PER_DAY > 0)
			++day1;

		Date date2 = zeroTime(cal2).getTime();

		long time2 = date2.getTime();
		long day2 = time2 / MILLISECONDS_PER_DAY;
		if (time2 % MILLISECONDS_PER_DAY > 0)
			++day2;

		return (int) (day1 - day2);
	}

	public int differenceInWeeks(CalendarWeek cal2) {
		Date date1 = zeroTime(this).getTime();

		long time1 = date1.getTime();

		Date date2 = zeroTime(cal2).getTime();
		long time2 = date2.getTime();

		long dif = (time1 - time2) / MILLISECONDS_PER_WEEK;
		if (time2 < time1)
			dif++;

		return (int) dif;
	}

	public void setCorrectWeek(boolean correctWeek) {
		this.correctWeek = correctWeek;
	}

	public void setFormat(SimpleDateFormat format) {
		this.sdf = format;
	}

}