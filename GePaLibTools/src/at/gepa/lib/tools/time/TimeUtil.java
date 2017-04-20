package at.gepa.lib.tools.time;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {
	public static String [] WEEKDAYS = new String[]{"Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"};
	public static String [] WEEKDAYS2 = new String[]{"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
	public static String [] WEEKDAYS_E = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	public static String [] WEEKDAYS_SiS = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
	
	public static final String TIME_TEMPLATE = "HH:mm";
	public static final String TIME_TEMPLATE_SIMPLE = "HHmm";
	
	public static final String DELIM = ":";
	public static final String DATETIME_TEMPLATE = "dd.MM.yyyy HH:mm:ss";
	public static final String DATE_TEMPLATE = "dd.MM.yyyy";
	public static final String DATE_TEMPLATE_WEEKDAY = "EEE, dd.MM.yyyy";

	public static String convertFloatToTime(float fromSis) 
	{
		return TimeUtil.convertFloatToTime(fromSis, "00:00");
	}	
	public static String convertFloatToTime(float fromSis, String template) 
	{
		String sa[] = template.split(DELIM);
		String ret = "";
		for( int i=0; i < sa.length; i++ )
		{
			int v =  (int)fromSis;
			if( !ret.isEmpty() ) 
				ret += DELIM;
			ret += String.format("%02d", v);
			fromSis -= v; 
			v /= 60.0;
		}
		return ret;
	}

	public static float convertTimeToFloat(String d) {
		String [] sa = d.split(DELIM);
		float value = 0.0f;
		float div = 1f;
		for( int i=0; i < sa.length; i++ )
		{
			value += Integer.parseInt(sa[i]) / div;
			div *= 60.0f;
		}
		return value;
	}

	public static boolean compare(String _gesamt, String _siS, String _toleranz) 
	{
		float gesamt = TimeUtil.convertTimeToFloat(_gesamt);
		float siS = TimeUtil.convertTimeToFloat(_siS);
		float toleranz = TimeUtil.convertTimeToFloat(_toleranz);
		float dif = gesamt - siS;
		boolean ret = dif == 0.0f || Math.abs(dif) <= Math.abs(toleranz); 
		//System.out.println("siS: " + siS + " gesamt: " + gesamt + " toleranz: " + toleranz + " dif: " + dif);
		return ret;
	}
	public static java.util.Date convertToDate(String toleranz) 
	{
		Calendar cal = at.gepa.lib.tools.time.TimeTool.getInstance(Locale.GERMAN);
		String sa [] = toleranz.split(DELIM);
		int [] dt = new int []{Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND};
		for( int i=0; i < dt.length; i++ )
		{
			int v = (i >= sa.length ? 0 : Integer.parseInt(sa[i]));
			cal.set(dt[i], v);
		}
		return cal.getTime();
	}
	public static String convertTimeToString(Date date, int[] fields) 
	{
		if( date == null ) return "0:00";
		Calendar cal = at.gepa.lib.tools.time.TimeTool.getInstance(Locale.GERMAN);
		cal.setTime(date);
		String ret = "";
		for( int i=0; i < fields.length; i++ )
		{
			if( !ret.isEmpty() )
				ret += ":";
			ret += String.format("%02d", cal.get(fields[i]) );
		}
		return ret;
	}
	public static Float convertTimeToFloat(Date date, int[] fields) 
	{
		String ret = convertTimeToString(date, fields);
		return TimeUtil.convertTimeToFloat(ret);
	}
	public static Date convertFloatToDate(Float f) 
	{
		if( f == null ) return null;
		String d = convertFloatToTime(f);
		return convertToDate(d);
	}
	public static String convertFloatToDayTime(float hours) 
	{
		String ret = "";
		int divValues[] = new int [] {8, 60, 60};
		String [] s = new String[]{"Tag(e)", "Stunde(n)", "Minute(n)" };
		for( int divPos = 0; divPos < divValues.length; divPos++ )
		{
			int v = (int)(hours/divValues[divPos]);
			if( !ret.isEmpty() )
				ret += " ";
			ret += String.format("%2d %s", v, s[divPos] );
			hours -= (v * divValues[divPos]);
			if( hours == 0f )
				break;
			hours *= divValues[divPos+1];
		}
		return ret;
	}
	public static Date convertToSimpleDate(String strDate) {
		return convertToSimpleDate(strDate, "dd.MM.yyyy");
	}	
	public static Date convertToSimpleDate(String strDate, String template) {
		SimpleDateFormat sdf = new SimpleDateFormat(template);
		
		int addZeros = 0;
		if( strDate.contains(":") )
			addZeros =  5-strDate.length();
		else
			addZeros =  4-strDate.length();
		while( addZeros > 0 )
		{
			strDate = "0" + strDate;
			addZeros--;
		}
		Date date = null;
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
		}
		return date;
	}
	public static int calcDifDays(Date von, Date bis) 
	{
		long dif = bis.getTime() - von.getTime();
		dif = dif / 1000;
		dif = dif / 3600;
		dif = dif / 24;
		return (int)dif;
	}
	public static Date addToDate(Date d, int days) 
	{
		Calendar cal = at.gepa.lib.tools.time.TimeTool.getInstance(Locale.GERMAN);
		cal.setTime(d);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}
	
	public static void setWeekOfYear(Calendar cal, int week) {
		if( isYearWeekBug(cal.get(Calendar.YEAR)) )
		{
			if( week - 1 > 0 )
				week--;
		}
		cal.set(Calendar.WEEK_OF_YEAR, week);
	}
	
	public static int getWeekInYear(Date d) {
		Calendar cal = at.gepa.lib.tools.time.TimeTool.getInstance();
		cal.setTime(d);
		int week = cal.get(Calendar.WEEK_OF_YEAR);

		if( isYearWeekBug(cal.get(Calendar.YEAR)))
		{
			if( week == 53 )
				week = 1;
			else
				week++;
		}
		
		Calendar calus = at.gepa.lib.tools.time.TimeTool.getInstance(Locale.US);
		calus.setTime(d);
		int weekus = cal.get(Calendar.WEEK_OF_YEAR);
		
		// System.out.println( d + " -> " + week + " / " + weekus);
		
//		Calendar calTest = at.gepa.lib.tools.time.TimeTool.getInstance(Locale.GERMAN);
//		calTest.set( cal.get(Calendar.YEAR), 0, 1);
////		//Date dx = calTest.getTime();
//		if( calTest.get(Calendar.DAY_OF_WEEK) > Calendar.THURSDAY )
//		{
//			if( week == 53 )
//				week = 1;
//			else
//				week++;
//		}
//		System.out.println( "\t" + d + " -> " + week );

//		SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
////		System.out.println(sd.format(calTest.getTime()));
////		System.out.println(sd.format(cal.getTime()));
		return week;
	}
	public static boolean isYearWeekBug( int year )
	{
		Calendar calTest = at.gepa.lib.tools.time.TimeTool.getInstance(Locale.GERMAN);
		calTest.set( year, 0, 1);
		return ( calTest.get(Calendar.DAY_OF_WEEK) > Calendar.THURSDAY );
	}
	public static boolean isFriday(Date d) {
		Calendar cal = at.gepa.lib.tools.time.TimeTool.getInstance(Locale.GERMAN);
		cal.setTime(d);
		
		return cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;
	}
	
	public static boolean isDay(Date d, int checkDay) {
		Calendar cal = at.gepa.lib.tools.time.TimeTool.getInstance(Locale.GERMAN);
		cal.setTime(d);
		
		return cal.get(Calendar.DAY_OF_WEEK) == checkDay;
	}
	public static boolean isWeekend(Date d) {
		Calendar cal = at.gepa.lib.tools.time.TimeTool.getInstance(Locale.GERMAN);
		cal.setTime(d);
		
		return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
	}
	public static String toString(Date d) 
	{
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		return df.format(d);
	}
	
	public static Calendar resetTime( Calendar cal )
	{
		cal.set(Calendar.HOUR_OF_DAY, 0);
		//cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	public static Object convertToSimpleDate(Date time, String template) {
		SimpleDateFormat sd = new SimpleDateFormat(template);
		return sd.format(time);
	}
	public static boolean isSameDate(long timeInMillis, long time) 
	{
		//ignore hours per day
		timeInMillis /= 1000; 
		timeInMillis /= 60;
		timeInMillis /= 60;
		
		time /= 1000;
		time /= 60;
		time /= 60;
		
		return time == timeInMillis;
	}
	public static int convetToQuarters(Timestamp t) {
		if( t == null ) return -1;
		Calendar cal = at.gepa.lib.tools.time.TimeTool.getInstance(Locale.GERMAN);
		cal.setTime(t);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int quarters = hour * 4;
		quarters += min / 15;
		if( min % 15 > 0 )
			quarters++;
		return quarters;
	}
	public static String convertToSimpleDate(Timestamp t) {
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		return df.format(t);
	}
	public static String convertToSimpleDate(Date t) {
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		return df.format(t);
	}
	public static int mapWeekdayToColumnIndex(Calendar monthOrWeek) {
		int weekday = monthOrWeek.get(Calendar.DAY_OF_WEEK); //First day is sunday = 1
		int ret = -1;
		switch(weekday)
		{
		case Calendar.MONDAY:
			ret = 0;
			break;
		case Calendar.TUESDAY:
			ret = 1;
			break;
		case Calendar.WEDNESDAY:
			ret = 2;
			break;
		case Calendar.THURSDAY:
			ret = 3;
			break;
		case Calendar.FRIDAY:
			ret = 4;
			break;
		case Calendar.SATURDAY:
			ret = 5;
			break;
		case Calendar.SUNDAY:
			ret = 6;
			break;
		}
		//System.out.println( convertToSimpleDate(monthOrWeek.getTime()) + " = " + weekday + " maped = " + ret);
		return ret;
	}
	public static String getDayInWekk(Calendar t2) {
		int idx = mapWeekdayToColumnIndex(t2);
		if( idx < 0 ) return " UNKNOWN";
		return WEEKDAYS2[idx];
	}
	
	public static Timestamp convertFloatToTimestamp(float e) 
	{
		String s = convertFloatToTimeString( e );
		SimpleDateFormat df = new SimpleDateFormat(TIME_TEMPLATE);
		try {
			return new Timestamp( df.parse(s).getTime() );
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	public static String convertFloatToTimeString(float e) 
	{
		int hours = new Float(e).intValue();
		float x = e - hours;
		x *= 60f;
		int minutes = Math.round(x);
		return String.format("%02d:%02d", hours, minutes);
	}
	public static int convertTimeToQuarters(float startHour) 
	{
		int q = ((int)startHour) * 4;
		startHour -= (int)startHour;
		q += startHour / 0.25;
		if( startHour % 0.25 > 0 )
			q++;
		return q;
	}
	public static Date setTime(Date d, float hour) 
	{
		Calendar cal = at.gepa.lib.tools.time.TimeTool.getInstance(Locale.GERMAN);
		cal.setTime(d);
		cal.set(Calendar.HOUR_OF_DAY, (int)hour );
		hour -= (int)hour;
		int min = (int)(hour * 60);
		cal.set(Calendar.MINUTE, (int)min );
		cal.set(Calendar.SECOND, (int)0 );
		cal.set(Calendar.MILLISECOND, (int)0 );
		return cal.getTime();
	}
	public static int getDayOfWeek(Date d) {
		Calendar cal = at.gepa.lib.tools.time.TimeTool.getInstance(Locale.GERMAN);
		cal.setTime(d);
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	public static int calcDifMinutes(Timestamp von, Timestamp bis) 
	{
		if( von ==null ) return 0;
		long dif = bis.getTime() - von.getTime();
		dif = dif / 1000;
		dif = dif / 60;
		
		float f = ((float)dif) / 100f;
		
		return (int)((f - (int)f)*100);
	}
	public static int calcDifHours(Timestamp von, Timestamp bis) 
	{
		if( von ==null ) return 0;
		long dif = bis.getTime() - von.getTime();
		dif = dif / 1000;
		dif = dif / 60;
		dif = dif / 60;
		
		float f = ((float)dif) / 100f;
		
		return (int)((f - (int)f)*100);
	}
	public static int calcDifHours(Date von, Date bis) 
	{
		if( von ==null ) return 0;
		long dif = bis.getTime() - von.getTime();
		dif = dif / 1000;
		dif = dif / 60;
		dif = dif / 60;
		
		float f = ((float)dif) / 100f;
		
		return (int)((f - (int)f)*100);
	}
	public static Timestamp convertStringToTime(String t) {
		SimpleDateFormat df = new SimpleDateFormat(TimeUtil.TIME_TEMPLATE);
		Date d = null;
		Timestamp ts = null;
		try {
			d = df.parse(t);
			ts = new Timestamp(d.getTime());
		} catch (ParseException e) {}
		return ts;
	}
	public static float convertTimespanToFloat(String s) 
	{
		float f = 0f;
		if( s == null || s.isEmpty() ) return f;
		
		if( s.contains(DELIM) )
		{
			String sa [] = null;
			sa = s.split(DELIM);
			int h = Integer.parseInt(sa[0]);
			float m = ((float)Integer.parseInt(sa[1])) / 60.0f;
			f = h + m;
		}
		else
		{
			if( s.contains(",") )
				s = s.replace(',', '.');
			f = Float.parseFloat(s);
			if( f >= 100)
				return convertTimespanToFloat( convertToTimeString(s) );
		}
		return f;
	}
	public static Calendar moveToWeekday(Calendar t, int moveToDay, int i) 
	{
		Calendar cal = (Calendar)t.clone();
		while( cal.get(Calendar.DAY_OF_WEEK) != moveToDay )
		{
			cal.add(Calendar.DAY_OF_MONTH, i);
		}
		cal.add(Calendar.DAY_OF_MONTH, -i);
		return cal;
	}
	public static int getWeekInYear(Calendar today) {
		return TimeUtil.getWeekInYear(today.getTime());
	}
	public static boolean isNoon(Timestamp ende) {
		Calendar cal = at.gepa.lib.tools.time.TimeTool.getInstance(Locale.GERMAN);
		cal.setTime(ende);
		return cal.get(Calendar.HOUR_OF_DAY) == 12;
	}
	public static Calendar alignToQuarter(Calendar now) 
	{
		int origMinutes = now.get(Calendar.MINUTE);
		int minutes = now.get(Calendar.MINUTE) % 15;
		float halfOfQuarter = 15f / 2;
		int cnt = (origMinutes/15);
		if( minutes > halfOfQuarter)
			cnt++;
		minutes = cnt * 15;
		now.set(Calendar.MINUTE, minutes);
		return now;
	}
	public static String convertToSimpleDateTime(Date time) {
		SimpleDateFormat df = new SimpleDateFormat(DATETIME_TEMPLATE, Locale.GERMAN);
		return df.format(time);
	}
	public static float alignToQuarter(float sf) 
	{
		float alignValue = sf;
		alignValue -= (int)sf;
		if( alignValue >= 0 && alignValue < 1f/8f)
			alignValue = 0;
		else if( alignValue >= 1f/8f && alignValue <= 1f/4f)
			alignValue = 0.25f;
		else if( alignValue >= 1f/4f && alignValue <= 3f/8f)
			alignValue = 0.25f;
		else if( alignValue >= 3f/8f && alignValue <= 1f/2f)
			alignValue = 0.5f;
		else if( alignValue >= 1f/2f && alignValue <= 3f/4f)
			alignValue = 0.75f;
		else if( alignValue > 3f/4f )
			alignValue = 1f;
//		Date d = TimeUtil.convertFloatToDate(sf);
//		Calendar cal = at.gepa.lib.tools.time.TimeTool.getInstance(Locale.GERMAN);
//		cal.setTime(d);
//		cal = alignToQuarter(cal);
//		return convertTimeToFloat(cal.getTime(), new int []{Calendar.HOUR_OF_DAY, Calendar.MINUTE});
		return ((int)sf) + alignValue;
	}
	public static float convertMinutesToFloat(int timeMinutes) {
		float m = timeMinutes/60f;
		return m;
	}
	public static boolean equals(java.util.Date date, java.util.Date date2) 
	{
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		
		boolean year = c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
		boolean month = c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
		boolean day = c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
		
		return year && month && day;
	}
	public static int countWorkingDays(Date dvon, Date dbis) 
	{
		Calendar calVon = at.gepa.lib.tools.time.TimeTool.getInstance(Locale.GERMAN);
		calVon.setTime(dvon);
		Calendar calBis = at.gepa.lib.tools.time.TimeTool.getInstance(Locale.GERMAN);
		calBis.setTime(dbis);
		return countWorkingDays( calVon, calBis);
	}
	private static int countWorkingDays(Calendar calVon, Calendar calBis) 
	{
		int workingDays = 0;
		while( !calVon.after( calBis ) )
		{
			if( !isWeekend(calVon.getTime()) )
			{
				workingDays++;
			}
			calVon.add( Calendar.DAY_OF_MONTH, 1);
		}
		return workingDays;
	}
	public static int calcDifDays(Calendar c1, Calendar c2) {
		return calcDifDays( c1.getTime(), c2.getTime() );
	}
	public static String convertToTimeString(String string) 
	{
		if( string.length() == 0 ) return "0:00";
		if( string.length() <= 2 ) return string + ":00";
		String h = string.substring(0, string.length()-2);
		String m = string.substring( string.length()-2);
		return h + ":" + m;
	}
	public static boolean isToday(Calendar colcomp) {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR) == colcomp.get(Calendar.YEAR) && cal.get(Calendar.MONTH) == colcomp.get(Calendar.MONTH) && cal.get(Calendar.DAY_OF_MONTH) == colcomp.get(Calendar.DAY_OF_MONTH);
	}
	public static Date toUTCTime(Date dstart) {
		Date utc = getUTCTime();
		int hdif = TimeUtil.calcDifHours(utc, Calendar.getInstance().getTime());
		Calendar cal = Calendar.getInstance();
		cal.setTime(dstart);
		hdif += cal.get(Calendar.DST_OFFSET);
		cal.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		cal.add(Calendar.HOUR_OF_DAY, hdif);
		
		Date convertedDate = cal.getTime(); 
		return convertedDate;
	}
	public static Date getUTCTime() {
		SimpleDateFormat sdf = new SimpleDateFormat(TimeUtil.DATETIME_TEMPLATE);
	    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	    String utcTime = sdf.format(new Date());
		return TimeUtil.convertToSimpleDate(utcTime, DATETIME_TEMPLATE);
	}


}
