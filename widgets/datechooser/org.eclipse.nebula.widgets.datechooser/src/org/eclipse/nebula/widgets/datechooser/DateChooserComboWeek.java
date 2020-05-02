package org.eclipse.nebula.widgets.datechooser;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.nebula.widgets.formattedtext.CalendarWeek;
import org.eclipse.nebula.widgets.formattedtext.DateFormatter;
import org.eclipse.nebula.widgets.formattedtext.ITextFormatter;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class DateChooserComboWeek extends DateChooserCombo {

	private Date minDate = null;

	public DateChooserComboWeek(Composite parent, int style, Date minD) {
		this(parent, style);
		minDate = minD;
	}

	public DateChooserComboWeek(Composite parent, int style) {
		super(parent, style);
//		setTheme(DateChooserTheme.blue());
		formattedText.getControl().addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
				Text t = (Text) e.getSource();
				Point start = t.getSelection();
				if (start.x == t.getText().length())
					t.setSelection(0);

			}

			@Override
			public void mouseUp(MouseEvent e) {
			}
		});
	}

	@Override
	protected Control createPopupContent(Composite parent) {
		final Control c = super.createPopupContent(parent);
		Calendar cm = locale != null ? CalendarWeek.getInstance(locale) : CalendarWeek.getInstance();
		((DateChooser) c).setFirstDayOfWeek(cm.getFirstDayOfWeek());
		((DateChooser) c).setMinimalDaysInFirstWeek(cm.getMinimalDaysInFirstWeek());
		((DateChooser) c).prevMonth.setEnabled(true);
		if (minDate != null) {
			((DateChooser) c).prevMonth.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseDown(MouseEvent arg0) {
					Calendar cur_m = Calendar.getInstance();
					cur_m.setTime(((DateChooser) c).getCurrentMonth());
					cur_m.add(Calendar.MONTH, -1);
					if (minDate.before(cur_m.getTime())) {
						((DateChooser) c).prevMonth.setEnabled(true);
					} else {
						((DateChooser) c).setCurrentMonth(cur_m.getTime());
						((DateChooser) c).prevMonth.setEnabled(false);
					}

				}
			});

			((DateChooser) c).nextMonth.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseDown(MouseEvent arg0) {
					((DateChooser) c).prevMonth.setEnabled(true);
				}
			});
			Date cur_m = ((DateChooser) c).getCurrentMonth();
			((DateChooser) c).prevMonth.setEnabled(minDate.before(cur_m));

		}
		return c;
	}

	public Calendar getCalValue() {
		Date d = getValue();
		Calendar c = null;
		if (d != null) {
			c = Calendar.getInstance();
			c.setTime(d);
		}
		return c;
	}

	public void setFormatter(ITextFormatter formatter) {
		if (formatter instanceof DateFormatter)
			super.setFormatter((DateFormatter) formatter);
		else {
			checkWidget();
			formattedText.setFormatter(formatter);
		}
	}
}
