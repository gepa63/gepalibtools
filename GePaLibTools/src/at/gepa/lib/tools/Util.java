package at.gepa.lib.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

//Ver

public class Util 
{
	public static int toInt(String s) 
	{
		if( s == null || s.isEmpty() ) return 0;
		return Integer.parseInt(s);
	}
	public static String asciiDecode(String input) 
	{
		return Util.asciiDecode(new StringBuffer(input));
	}	
//	public static String asciiDecode(StringBuffer input) {
//        char c;
//        StringBuffer filtered = new StringBuffer(input.length());
//        for (int i = 0; i < input.length(); i++) {
//            c = input.charAt(i);
//            switch (c) {
//            // ä
//            case 8222:
//                filtered.append("ä");
//                break;
//            // Ä
//            case 381:
//                filtered.append("Ä");
//                break;
//
//            // ö
//            case 8221:
//                filtered.append("ö");
//                break;
//
//            // Ö
//            case 8482:
//                filtered.append("Ö");
//                break;
//
//            // ü
//            case 129:
//                filtered.append("ü");
//                break;
//
//            // Ü
//            case 353:
//                filtered.append("Ü");
//                break;
//
//            // ß
//            case 225:
//                filtered.append("ß");
//                break;
//
//            // ³
//            case 252:
//                filtered.append("³");
//                break;
//
//            // ²
//            case 253:
//                filtered.append("²");
//                break;
//
//            // µ
//            case 230:
//                filtered.append("µ");
//                break;
//
//            // ~
//            case 126:
//                filtered.append("~");
//                break;
//
//            default:
//            	//Log.i("asciidecode", "" + c + " = " + ((int)c));
//                filtered.append(c);
//                break;
//            }
//
//        }
//        // System.out.println (filtered.toString());
//        return (filtered.toString());
//    }
	
	public static String convertToString(ArrayList<String> list, String _defDelim) {

        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (String s : list)
        {
            sb.append(delim);
            sb.append(s);
            delim = _defDelim;
        }
        return sb.toString();
    }

	public static  ArrayList<String> convertToArray(String string) {

        return convertToArray(string, ",");
    }
	public static  ArrayList<String> convertToArray(String string, String delim) {

        ArrayList<String> list = new ArrayList<String>(Arrays.asList(string.split(delim)));
        return list;
    }
	public static Integer[] buildIntArray(int min, int max, int jumps) 
	{
		Integer ret[] = new Integer[2+max-min];
		int cur=min;
		for( int i=0; i < ret.length; i++ )
		{
			ret[i] = cur;
			cur += jumps;
		}
		return ret;
	}
	public static float objectToFloat(Object value) 
	{
		if( value == null || value.toString().isEmpty() ) return 0f;
		if( value instanceof String )
			return Util.toFloat((String)value);
		if( value instanceof Float )
			return (Float)value;
		return Util.toFloat(value.toString());
	}
	public static float toFloat(String s)
	{
		if( s.contains(",") )
			s = s.replace(',', '.');
		return Float.parseFloat(s);
	}
	//////////////////////////////////////////////////////////////////////////////////////////
	
//	public static int toInt(String s) 
//	{
//		if( s == null || s.isEmpty() ) return 0;
//		return Integer.parseInt(s);
//	}
//	public static Integer toInt(Object value) {
//		if( value == null || value.toString().isEmpty() ) return 0;
//		if( value instanceof Integer ) return (Integer)value;
//		return Integer.parseInt(value.toString());
//	}
	
	public static String asciiDecode(StringBuffer input) {
        char c;
        StringBuffer filtered = new StringBuffer(input.length());
        for (int i = 0; i < input.length(); i++) {
            c = input.charAt(i);
            switch (c) {
            // ä
            case 8222:
                filtered.append("ä");
                break;
            // Ä
            case 381:
                filtered.append("Ä");
                break;

            // ö
            case 8221:
                filtered.append("ö");
                break;

            // Ö
            case 8482:
                filtered.append("Ö");
                break;

            // ü
            case 129:
                filtered.append("ü");
                break;

            // Ü
            case 353:
                filtered.append("Ü");
                break;

            // ß
            case 225:
                filtered.append("ß");
                break;

            // ³
            case 252:
                filtered.append("³");
                break;

            // ²
            case 253:
                filtered.append("²");
                break;

            // µ
            case 230:
                filtered.append("µ");
                break;

            // ~
            case 126:
                filtered.append("~");
                break;

            default:
            	//Log.i("asciidecode", "" + c + " = " + ((int)c));
                filtered.append(c);
                break;
            }

        }
        // System.out.println (filtered.toString());
        return (filtered.toString());
    }
	public static Integer toInt(Object value) {
		if( value == null ) return 0;
		if( value instanceof Integer ) return (Integer)value;
		if( value.toString().isEmpty() ) return 0;
		return Integer.parseInt(value.toString());
	}
	public static Long toLong(Object value) {
		if( value == null ) return 0L;
		if( value instanceof Long ) return (Long)value;
		if( value.toString().isEmpty() ) return 0L;
		return Long.parseLong(value.toString());
	}
	public static Float toFloat(Object value) {
		if( value == null ) return 0f;
		if( value instanceof Float ) return (Float)value;
		String s = value.toString(); 
		if( s.isEmpty() ) return 0f;
		if( s.contains(",") )
			s = s.replace(',', '.');
		return Float.parseFloat(s);
	}
	public static double toDouble(Object value) {
		if( value == null ) return 0f;
		if( value instanceof Double ) return (Double)value;
		String s = value.toString(); 
		if( s.isEmpty() ) return 0f;
		if( s.contains(",") )
			s = s.replace(',', '.');
		return Double.parseDouble(s);
	}
	public static String convertDegreeToWindDirection(int windDeg) 
	{
		return convertDegreeToWindDirection(windDeg, Locale.GERMAN);
	}	
	public static String convertDegreeToWindDirection(int windDeg, Locale l) 
	{
//		int div = 8*45;
//		int cakePart = windDeg / div;
//		
//		if( cakePart % div  > 0 )
//			cakePart++;
		String [] wd = null;
		if( l != Locale.GERMAN )
			wd = new String[]{"N", "NNE", "N", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
		else
			wd = new String[]{"N", "NNO", "NO", "ONO", "O", "OSO", "SO", "SSO", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};

//			wd = new String[]{"E", "ENE", "NNE", "N", "NNW", "WNW", "W", "WSW", "SSW", "S", "SSE", "ESE"};
//		else
//			wd = new String[]{"O", "ONO", "NNO", "N", "NNW", "WNW", "W", "WSW", "SSW", "S", "SSO", "OSO"};
		
		float step = 360 / wd.length;
		float b = (float)Math.floor((windDeg + (step/2)) / step);
		
		//if( cakePart < 0 || cakePart >= wd.length ) return "unknown";
		int cakePart = (int)(b % wd.length);
		return wd[cakePart];
	}
	public static float fromKelvinToCelsius(float tempKelvin, boolean withCheck) 
	{
		float degree = tempKelvin - 273.15f;
		float deepestCelsiusDegrees = -90f;
		if( withCheck && degree < deepestCelsiusDegrees )
			degree = tempKelvin;
		
		return degree;
	}
//	public static int turnFtoC(int tempF) {
//		return (int) ((tempF - 32) * 5.0f / 9);
//	}
//	
//	public static int turnCtoF(int tempC) {
//	    return (int) (tempC * 9.0f / 5 + 32);
//	}
	
	public static float fromFtoC(float farenheit) 
	{
		return (farenheit - 32f) * 0.5f / 9f;
	}
	public static float fromCtoF(float gradCelsius) 
	{
		return ((gradCelsius * 9f) / 5f) + 32f;
	}
	
	public static boolean isDigit(String v) 
	{
		boolean ret = true;
		try
		{
			Integer.parseInt(v);
		}
		catch( Exception ex )
		{
			ret = false;
		}
		return ret;
	}

	public static float toFloat2(String waz) 
	{
		float ret = 0f;
		int multiplier = 1;
		String [] value = new String[]{ "0", "0" };
		if( waz.contains(".") )
		{
			value = waz.split(".");
		}
		else if( waz.contains(",") )
		{
			value = waz.split(",");
		}
		else
			value[0] = waz;
		
		for( int i= value[0].length()-1; i >= 0; i-- )
		{
			char c = value[0].charAt(i);
			if( Character.isDigit( c ) )
			{
				int v = (c - 48) * multiplier;
				ret += v;
				multiplier *= 10;
			}
			else
				break;
		}
		float divisor = 10;
		for( int i= 0; i < value[1].length(); i++ )
		{
			char c = value[1].charAt(i);
			if( Character.isDigit( c ) )
			{
				float v = (c - 48) / divisor;
				ret += v;
				divisor *= 10;
			}
			else
				break;
		}
		return ret;
	}
	public static String toHtml(String string) {
		return "<html>"+string.replaceAll("\n", "<br>")+"</html>";
	}
	public static String toHtml(String string, boolean withmonospacedFont) {
		return "<html>" + (withmonospacedFont ? "<tt>" : "") + string.replaceAll("\n", "<br>")+(withmonospacedFont ? "</tt>" : "") + "</html>";
	}
	public static String clearFromHTML(String tt) {
		return clearFromHTML(tt, null);
	}
	public static String clearFromHTML(String tt, String retIfNull) {
		if( tt == null || tt.isEmpty() ) return retIfNull;
		tt = tt.replaceAll("<br>", " -> ");
		tt = tt.replaceAll("<html>", "");
		tt = tt.replaceAll("</html>", "");
		return tt;
	}

	//////////////////////////////////////////////////////////////////////////
}
