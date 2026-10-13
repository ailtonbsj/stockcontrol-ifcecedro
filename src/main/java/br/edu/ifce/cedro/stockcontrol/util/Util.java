package br.edu.ifce.cedro.stockcontrol.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {

	public static String dateToDateTimeLocal(Date date) {  
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		return df.format(date);
	}
	
	public static String dateToDateTimeLocal() {
		Date date = Calendar.getInstance().getTime();  
		return dateToDateTimeLocal(date);
	}
	
	public static Date dateTimeLocalToDate(String datetimeLocal) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(datetimeLocal);
		} catch (ParseException e) {
			return new Date();
		}
	}

}
