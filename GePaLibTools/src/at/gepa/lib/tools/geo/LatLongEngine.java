package at.gepa.lib.tools.geo;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


public class LatLongEngine
{
	public static enum LatLonType
	{
		Longitude,
		Latitude
	}
	public static final String[] LatitudeDir_Short	= new String[]{	"N", "S"};
	public static final String[] LongitudeDir_Short = new String[]{ "E", "W"};
	
	private Double degrees;
	private LatLonType	type;
	
	private double grad;
	private int minutes;
	private double seconds;
	private String direction;
	
	public LatLongEngine(Double degrees, LatLonType type)
	{
		this.degrees = degrees;
		this.type = type;
	}
	public LatLongEngine(Double degrees, LatLonType type, boolean extended)
	{
		this.degrees = degrees;
		grad = degrees.intValue();
		degrees -= grad;
		degrees *= 100;
		minutes = degrees.intValue();
		degrees -= minutes;
		seconds = degrees * 60;
		this.type = type;
	}
	
	public boolean convertToGradMinutes()
	{
		Double _degrees = Double.valueOf(Math.abs(degrees));
		int d = Math.abs(_degrees.intValue());
		double rest = Math.abs(_degrees.doubleValue()) - d;
		int m = (int)(rest * 60);
		rest -= (m) / 60.0;
		double sec = rest * 360000;
		sec /= 100;
		grad = d;
		minutes = m;
		seconds= sec;
		setDirection(degrees);
		return true;
	}
	public String getDirection()
	{
		return this.direction;
	}
	public void setDirection(String direction)
	{
		this.direction = direction;
	}
	public void setDirection(double value)
	{
		this.direction = evalDirection(value);
	}
	private String evalDirection(double value)
	{
		String [] sa = getDirectionArray();
		if( value < 0 )
			return sa[1];
		return sa[0];
	}
	private int reconvertSign()
	{
		String [] sa = getDirectionArray();
		String dir = this.getDirection();
		if( dir == null )
			return 0;
		if( dir.equals(sa[1]))
			return -1;
		return 1;
	}
	public Double getDegrees()
	{
		return degrees;
	}

	public void setDegrees(Double degrees)
	{
		this.degrees = degrees;
	}

	public double getGrad()
	{
		return grad;
	}

	public int getMinutes()
	{
		return minutes;
	}

	public double getSeconds()
	{
		return seconds;
	}
	public void setGrad(double grad)
	{
		this.grad = grad;
	}
	public void setMinutes(int minutes)
	{
		this.minutes = minutes;
	}
	public void setSeconds(double seconds)
	{
		this.seconds = seconds;
	}
	public void dump()
	{
		System.out.println("Degrees: " + this.degrees);
		System.out.println("\tGrad: " + this.grad);
		System.out.println("\tMinutes: " + this.minutes);
		System.out.println("\tSeconds: " + this.seconds);
		System.out.println("\tDirection: " + this.getDirection());
	}
	public void reconvert()
	{
		this.setDirection(degrees);
		double sec = this.seconds * 100;
		sec /= 360000.0;
		
		sec += (this.minutes) / 60.0;

		sec += this.grad;
		
		sec *= reconvertSign();
		
		this.degrees = new Double( sec );
	}
	public String [] getDirectionArray()
	{
		return type == LatLonType.Latitude ? LatitudeDir_Short : LongitudeDir_Short;		
	}
	public void calcDegrees()
	{
		degrees = (((getSeconds() / 60) + getMinutes()) / 60) + getGrad();
	}
	@Override
	public String toString()
	{
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator('.');
		decimalFormatSymbols.setGroupingSeparator(',');
		
		DecimalFormat decimalFormat = new DecimalFormat("#0.000", decimalFormatSymbols);
		decimalFormat.setMaximumFractionDigits(5);
		decimalFormat.setMaximumIntegerDigits(2);
		
		return String.format("%3d°%2d'%s\"%s", (int)grad, minutes, decimalFormat.format(seconds), getDirection() );
	}
}
