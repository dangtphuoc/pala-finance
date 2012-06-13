package pala.gui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static Date getFirstDay(Date d) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date dddd = calendar.getTime();
		return dddd;
	}

	public static Date getLastDay(Date d) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date dddd = calendar.getTime();
		return dddd;
	}
}