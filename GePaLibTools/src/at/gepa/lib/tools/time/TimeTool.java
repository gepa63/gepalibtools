package at.gepa.lib.tools.time;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import at.gepa.lib.tools.Util;



public class TimeTool {
	public static final String TEMPLATE_UNFORMATED = "ddMMyyyy";
	public static final String TEMPLATE_DATETIME_SHORT = "yy.M.d H:m";
	public static final String TEMPLATE_DATE = "dd.MM.yyyy";
	public static final String TEMPLATE_DATE_US = "yyyy-MM-dd";
	public static final String TEMPLATE_DATE_EN = "yyyy.MM.dd";
	public static final String TEMPLATE_TIME = "HH:mm";
	public static final String TEMPLATE_TIME2 = "HH:mm:ss";
	public static final String TEMPLATE_DATETIME = TEMPLATE_DATE + " " + TEMPLATE_TIME;
	public static final String TEMPLATE_DATETIME2 = TEMPLATE_DATE_US + " " + TEMPLATE_TIME2;
	public static final String TEMPLATE_DATE_US2 = "dd-MM-yyyy";
	
	public static Date alignToQuarter( Date d )
	{
		Calendar cal = at.gepa.lib.tools.time.TimeTool.getInstance();
		cal.setTime(d);
		
		int min = cal.get(Calendar.MINUTE);
		int testValues [] = new int []{0, 15, 30, 45};
		for( int v : testValues )
		{
			if( min <= v + 7 )
			{
				min = v;
				break;
			}
		}
		if( min > 45 + 7 )
		{
			min = 0;
			cal.add(Calendar.HOUR_OF_DAY, 1);
		}
		cal.set(Calendar.MINUTE, min);
		return cal.getTime();
	}

	public static Date toDate(String sdate) 
	{
		return toDate( sdate, TimeTool.TEMPLATE_DATETIME);
	}
	public static Date toDate(String sdate, String template) 
	{
		SimpleDateFormat sdf = new SimpleDateFormat(template, Locale.GERMAN);
		try {
			return sdf.parse(sdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String toDateTimeString(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat(TEMPLATE_DATETIME, Locale.GERMAN);
		return sdf.format(d);
	}

	public static String toDateString(Calendar d) 
	{
		return toDateString(d.getTime());
	}

	public static String toTimeString(Date d) 
	{
		SimpleDateFormat sdf = new SimpleDateFormat(TEMPLATE_TIME, Locale.GERMAN);
		return sdf.format(d);
	}

	public static Date toDateTime(String sdate, String stime) 
	{
		return toDate( sdate + " " + stime );
	}

	public static String toDateString(Date d, String template) {
		SimpleDateFormat sdf = new SimpleDateFormat(template, Locale.GERMAN);
		return sdf.format(d);
	}
	public static String toDateString(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat(TEMPLATE_DATE, Locale.GERMAN);
		return sdf.format(d);
	}
	public static String toDateStringUS(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat(TEMPLATE_DATE_US, Locale.US);
		return sdf.format(d);
	}

	public static boolean check(Date evening, Date morning) 
	{
		String e = toDateString(evening);
		String m = toDateString(morning);
		if( !e.equals(m) ) return false;
		return true;
	}

	public static String toShortDateTimeString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(TEMPLATE_DATETIME_SHORT, Locale.GERMAN);
		
		return sdf.format(date);
	}

	public static boolean isNight(Date dateTime) 
	{
		Calendar cal = at.gepa.lib.tools.time.TimeTool.getInstance();
		cal.setTime(dateTime);
		return cal.get(Calendar.HOUR_OF_DAY) >= 13;
	}

	public static Calendar toCalendar(String[] sa, int i, int j) throws ParseException {
		String tmpl = "yy.MM.dd.hh.mm.s";
		SimpleDateFormat sd = new SimpleDateFormat(tmpl);
		Calendar cal = at.gepa.lib.tools.time.TimeTool.getInstance();
		String value = "";
		for( ; i <= j; i++ )
		{
			if( !value.isEmpty() )
				value += ".";
			value += sa[i];
		}
		cal.setTime( sd.parse(value) );
		return cal;
	}

	public static Calendar toCalendar(int tag, int zeitzone, String uhrzeit) 
	{
		Calendar cal = at.gepa.lib.tools.time.TimeTool.getInstance();
		cal.set(Calendar.DAY_OF_YEAR, tag);
		cal.set(Calendar.ZONE_OFFSET, zeitzone);
		String zeit[] = uhrzeit.split(":");
		int [] ts = new int []{Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND};
		for( int i=0; i < zeit.length; i++ )
		{
			if( i == 0 )
				cal.set(ts[i], Util.toInt(zeit[i]) );
		}
		return cal;
	}

	public static Calendar toCalendar(String d) 
	{
		return toCalendar(d, TEMPLATE_DATE);
	}

	private static Calendar toCalendar(String d, String templateDate) {
		SimpleDateFormat f = new SimpleDateFormat(templateDate);
		Calendar cal = at.gepa.lib.tools.time.TimeTool.getInstance();
		
		try {
			Date date = f.parse(d);
			cal.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
			cal = null;
		}
		
		return cal;
	}

	public static int dayOfYear(Calendar cal) {
		return cal.get(Calendar.DAY_OF_YEAR);
	}

	public static int timeZoneOffset(Calendar cal) {
		return cal.get(Calendar.ZONE_OFFSET) / (1000 * 3600);
	}

	public static int daylight(Calendar cal) {
		return cal.get(Calendar.DST_OFFSET) / (3600 * 1000);
	}

	public static int timedifToUTC(Calendar cal) {
		int offset = TimeTool.timeZoneOffset(cal);
		int daylight = TimeTool.daylight(cal);
		return offset + daylight;
	}

	public static double toJulianDate(Calendar cal) {
		
		//SimpleDateFormat sdf = new SimpleDateFormat(TEMPLATE_UNFORMATED, Locale.GERMAN);
		//String unformattedDate = sdf.format(cal.getTime());

	    /*Unformatted Date: ddmmyyyy*/
	    int resultJulian = 0;
	    /*Days of month*/
	    int[] monthValues = {31,28,31,30,31,30,31,31,30,31,30,31};

//	    String dayS, monthS, yearS;
//	    dayS = unformattedDate.substring(0,2);
//	    monthS = unformattedDate.substring(3, 5);
//	    yearS = unformattedDate.substring(6, 10);

	    /*Convert to Integer*/
//	    int day = Integer.valueOf(dayS);
//	    int month = Integer.valueOf(monthS);
//	    int year = Integer.valueOf(yearS); 
	    int day = cal.get(Calendar.DAY_OF_YEAR);
	    int month = cal.get(Calendar.MONTH);
	    int year = cal.get(Calendar.YEAR); 

	    //Leap year check
	    if(year % 4 == 0)
	    {
	    	monthValues[1] = 29;    
	    }
	    //Start building Julian date
	    String julianDate = "1";
	    //last two digit of year: 2012 ==> 12
	    julianDate += (""+year).substring(2,4);

	    int julianDays = 0;
	    for (int i=0; i < month-1; i++)
	    {
	    	julianDays += monthValues[i];
	    }
	    julianDays += day;
	    if(String.valueOf(julianDays).length() < 2)
	    {
	    	julianDate += "00";
	    }
	    if(String.valueOf(julianDays).length() < 3)
	    {
	    	julianDate += "0";
	    }

	    julianDate += String.valueOf(julianDays);
	    resultJulian =  Integer.valueOf(julianDate);    
	    return resultJulian;
	}
	
	public Calendar julianToGregorian(double injulian) 
	{
	    int JGREG= 15 + 31*(10+12*1582);     
	    int jalpha,ja,jb,jc,jd,je,year,month,day;
	    double julian = injulian + 0.5 / 86400.0;
	    ja = (int) julian;
	    if (ja>= JGREG) {
	        jalpha = (int) (((ja - 1867216) - 0.25) / 36524.25);
	        ja = ja + 1 + jalpha - jalpha / 4;
	    }

	    jb = ja + 1524;
	    jc = (int) (6680.0 + ((jb - 2439870) - 122.1) / 365.25);
	    jd = 365 * jc + jc / 4;
	    je = (int) ((jb - jd) / 30.6001);
	    day = jb - jd - (int) (30.6001 * je);
	    month = je - 1;
	    if (month > 12) month = month - 12;
	    year = jc - 4715;
	    if (month > 2) year--;
	    if (year <= 0) year--;

	    Calendar cal = at.gepa.lib.tools.time.TimeTool.getInstance();
	    cal.set(year, month, day);
	    return cal;
	}


	public static String toTimeString(double hm) {
		int h = (int)hm;
		int min = (int)((hm - h) * 60);
		return String.format("%02d:%02d", h, min );
	}
	public static String toTimeString2(long millisec) {
		millisec /= 1000;
		int h = (int)(millisec);
		h /= 60;
		h /= 60;
		millisec -= (h*60 * 60);
		
		int min = (int)millisec;
		min /= 60;
		millisec -= (min * 60);
		
		int sec = (int)(millisec);
		return String.format("%02d:%02d:%02d", h, min, sec );
	}

	public static Calendar convertToCalendar(String dateString, String template, boolean removeTimeZone) 
	{
		return convertToCalendar(dateString, template, removeTimeZone, Locale.getDefault() );
	}

	public static String toTimeString(Calendar cal) {
		return toTimeString(cal.getTime());
	}

	public static Calendar convertToCalendar(String dateString, String template, boolean removeTimeZone, Locale locale) 
	{
		Calendar ret = null;
		SimpleDateFormat sdf = new SimpleDateFormat(template, locale );
		try {
			String tzs = null;
			if( removeTimeZone )
			{
				int end = dateString.lastIndexOf(' ');
				tzs = dateString.substring(end).trim();
				dateString = dateString.substring(0, end).trim();
			}
			Date date = sdf.parse(dateString);
			if( tzs != null )
			{
				TimeZone tz = TimeZone.getTimeZone(tzs);
				ret = Calendar.getInstance(tz);
			}
			else
				ret = at.gepa.lib.tools.time.TimeTool.getInstance();
			ret.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return ret;
	}

	public static String toDateString(Calendar cal, String template, Locale locale) {
		String ret = "";
		SimpleDateFormat sdf = new SimpleDateFormat(template, locale );
		try {
			ret = sdf.format(cal.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			ret = e.getMessage();
		}
		return ret;
	}

	public static boolean isSameDay(Calendar cal, Calendar heute) {
		return cal.get(Calendar.YEAR) == heute.get(Calendar.YEAR) && cal.get(Calendar.MONTH) == heute.get(Calendar.MONTH) && cal.get(Calendar.DAY_OF_MONTH) == heute.get(Calendar.DAY_OF_MONTH);
	}

	public static int dayDif(Calendar heute, Calendar cal) {
		long dif = cal.getTimeInMillis() - heute.getTimeInMillis();
		dif /= 1000;
		dif /= 60;
		dif /= 60;
		int hoursOfLastDay = 0;
		if( dif % 24 > 0 ) hoursOfLastDay++;
		dif /= 24;
		return (int)dif + hoursOfLastDay;
	}

	public static Calendar setDatePart(Calendar cal, String dateString, String template) 
	{
		Date d = TimeTool.toDate(dateString, template);
		Calendar dummy = at.gepa.lib.tools.time.TimeTool.getInstance();
		dummy.setTime(d);
		cal.set(Calendar.YEAR, dummy.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, dummy.get(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, dummy.get(Calendar.DAY_OF_MONTH));
		return cal;
	}

	public static Calendar setTimePart(Calendar cal, String dateString, String template) {
		Date d = TimeTool.toDate(dateString, template);
		Calendar dummy = at.gepa.lib.tools.time.TimeTool.getInstance();
		dummy.setTime(d);
		cal.set(Calendar.HOUR_OF_DAY, dummy.get(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, dummy.get(Calendar.MINUTE));
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	public static Calendar toCalendar(Timestamp timestamp) {
		if( timestamp == null ) return null;
		Calendar cal = at.gepa.lib.tools.time.TimeTool.getInstance();
		cal.setTimeInMillis( timestamp.getTime() );
		return cal;
	}

	public static Date toDate(Timestamp o) 
	{
		Calendar cal = at.gepa.lib.tools.time.TimeTool.getInstance();
		cal.setTimeInMillis(o.getTime());
		return cal.getTime();
	}

	public static Calendar getInstance() {
		return getInstance(Locale.GERMAN);
	}

	public static Calendar getInstance(Locale l) {
		return Calendar.getInstance(l);
	}
	public static Date makeDate(Date d, int hourOfDay, int minute) 
	{
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, hourOfDay);
		c.set(Calendar.MINUTE, minute);
		return c.getTime();
	}

	public static Date makeDate(Date d, int year, int monthOfYear, int dayOfMonth) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, monthOfYear);
		c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		return c.getTime();
	}
	public static int getAge(Date d) 
	{
		Calendar today = Calendar.getInstance();
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int years = today.get(Calendar.YEAR) - c.get(Calendar.YEAR);
		if( today.get(Calendar.MONTH) < c.get(Calendar.MONTH) )
			years--;
		else if( today.get(Calendar.MONTH) == c.get(Calendar.MONTH) )
		{
			if( today.get(Calendar.DAY_OF_MONTH) < c.get(Calendar.DAY_OF_MONTH) )
				years--;
		}
		return years;
	}

	public static String toString(java.util.Date date, String dateFormat) {
		String ret = "";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat );
		try {
			ret = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			ret = e.getMessage();
		}
		return ret;
	}

}
