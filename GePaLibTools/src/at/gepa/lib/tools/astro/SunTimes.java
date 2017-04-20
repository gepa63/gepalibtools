package at.gepa.lib.tools.astro;

import java.util.Calendar;

public class SunTimes {

	private final double mDR = Math.PI / 180;
    private final double mK1 = 15 * mDR * 1.0027379;

    private int[] mRiseTimeArr = new int[] { 0, 0 };
    private int[] mSetTimeArr = new int[] { 0, 0 };
    private double mRizeAzimuth = 0.0;
    private double mSetAzimuth = 0.0;

    private double[] mSunPositionInSkyArr = new double[] { 0.0, 0.0 };
    private double[] mRightAscentionArr = new double[] { 0.0, 0.0, 0.0 };
    private double[] mDecensionArr = new double[] { 0.0, 0.0, 0.0 };
    private double[] mVHzArr = new double[] { 0.0, 0.0, 0.0 };

    private boolean mIsSunrise = false;
    private boolean mIsSunset = false;
    
    static abstract class Coords
    {
        protected int mDegrees = 0;
        protected int mMinutes = 0;
        protected int mSeconds = 0;

        public double ToDouble()
        {
            return Sign() * (mDegrees + ((double)mMinutes / 60) + ((double)mSeconds / 3600));
        }

        protected abstract int Sign();
    }
    
    static class LatitudeCoords extends Coords
    {
        public enum Direction
        {
            North,
            South
        }
        protected Direction mDirection = Direction.North;

        public LatitudeCoords(int degrees, int minutes, int seconds, Direction direction)
        {
            mDegrees = degrees;
            mMinutes = minutes;
            mSeconds = seconds;
            mDirection = direction;
        }

        @Override
        protected int Sign()
        {
            return (mDirection == Direction.North ? 1 : -1);
        }
    }
    
    
    static class LongitudeCoords extends Coords
    {
        public enum Direction
        {
            East,
            West
        }

        protected Direction mDirection = Direction.East;

        public LongitudeCoords(int degrees, int minutes, int seconds, Direction direction)
        {
            mDegrees = degrees;
            mMinutes = minutes;
            mSeconds = seconds;
            mDirection = direction;
        }

        @Override
        protected int Sign()
        {
            return (mDirection == Direction.East ? 1 : -1);
        }
    }

    /////////////////////////////////////////
    private int Sign(double value)
    {
        int rv = 0;

        if (value > 0.0) rv = 1;
        else if (value < 0.0) rv = -1;
        else rv = 0;

        return rv;
    }

    // Local Sidereal Time for zone
    private double LocalSiderealTimeForTimeZone(double lon, double jd, double z)
    {
        double s = 24110.5 + 8640184.812999999 * jd / 36525 + 86636.6 * z + 86400 * lon;
        s = s / 86400;
        s = s - Math.floor(s);
        return s * 360 * mDR;
    }

    // determine Julian day from calendar date
    // (Jean Meeus, "Astronomical Algorithms", Willmann-Bell, 1991)
    private double GetJulianDay(Calendar date)
    {
        int month = date.get(Calendar.MONTH)+1;
        int day = date.get(Calendar.DAY_OF_MONTH);
        int year = date.get(Calendar.YEAR);

        boolean gregorian = (year < 1583) ? false : true;

        if ((month == 1) || (month == 2))
        {
            year = year - 1;
            month = month + 12;
        }

        double a = Math.floor((double)year / 100);
        double b = 0;

        if (gregorian)
            b = 2 - a + Math.floor(a / 4);
        else
            b = 0.0;

        double jd = Math.floor(365.25 * (year + 4716))
                   + Math.floor(30.6001 * (month + 1))
                   + day + b - 1524.5;

        return jd;
    }

    // sun's position using fundamental arguments 
    // (Van Flandern & Pulkkinen, 1979)
    private void CalculateSunPosition(double jd, double ct)
    {
        double g, lo, s, u, v, w;

        lo = 0.779072 + 0.00273790931 * jd;
        lo = lo - Math.floor(lo);
        lo = lo * 2 * Math.PI;

        g = 0.993126 + 0.0027377785 * jd;
        g = g - Math.floor(g);
        g = g * 2 * Math.PI;

        v = 0.39785 * Math.sin(lo);
        v = v - 0.01 * Math.sin(lo - g);
        v = v + 0.00333 * Math.sin(lo + g);
        v = v - 0.00021 * ct * Math.sin(lo);

        u = 1 - 0.03349 * Math.cos(g);
        u = u - 0.00014 * Math.cos(2 * lo);
        u = u + 0.00008 * Math.cos(lo);

        w = -0.0001 - 0.04129 * Math.sin(2 * lo);
        w = w + 0.03211 * Math.sin(g);
        w = w + 0.00104 * Math.sin(2 * lo - g);
        w = w - 0.00035 * Math.sin(2 * lo + g);
        w = w - 0.00008 * ct * Math.sin(g);

        // compute sun's right ascension
        s = w / Math.sqrt(u - v * v);
        mSunPositionInSkyArr[0] = lo + Math.atan(s / Math.sqrt(1 - s * s));

        // ...and declination 
        s = v / Math.sqrt(u);
        mSunPositionInSkyArr[1] = Math.atan(s / Math.sqrt(1 - s * s));
    }

    // test an hour for an event
    private double TestHour(int k, double zone, double t0, double lat)
    {
        double[] ha = new double[3];
        double a, b, c, d, e, s, z;
        double time;
        int hr, min;
        double az, dz, hz, nz;

        ha[0] = t0 - mRightAscentionArr[0] + k * mK1;
        ha[2] = t0 - mRightAscentionArr[2] + k * mK1 + mK1;

        ha[1] = (ha[2] + ha[0]) / 2;    // hour angle at half hour
        mDecensionArr[1] = (mDecensionArr[2] + mDecensionArr[0]) / 2;  // declination at half hour

        s = Math.sin(lat * mDR);
        c = Math.cos(lat * mDR);
        z = Math.cos(90.833 * mDR);    // refraction + sun semidiameter at horizon

        if (k <= 0)
            mVHzArr[0] = s * Math.sin(mDecensionArr[0]) + c * Math.cos(mDecensionArr[0]) * Math.cos(ha[0]) - z;

        mVHzArr[2] = s * Math.sin(mDecensionArr[2]) + c * Math.cos(mDecensionArr[2]) * Math.cos(ha[2]) - z;

        if (Sign(mVHzArr[0]) == Sign(mVHzArr[2]))
            return mVHzArr[2];  // no event this hour

        mVHzArr[1] = s * Math.sin(mDecensionArr[1]) + c * Math.cos(mDecensionArr[1]) * Math.cos(ha[1]) - z;

        a = 2 * mVHzArr[0] - 4 * mVHzArr[1] + 2 * mVHzArr[2];
        b = -3 * mVHzArr[0] + 4 * mVHzArr[1] - mVHzArr[2];
        d = b * b - 4 * a * mVHzArr[0];

        if (d < 0)
            return mVHzArr[2];  // no event this hour

        d = Math.sqrt(d);
        e = (-b + d) / (2 * a);

        if ((e > 1) || (e < 0))
            e = (-b - d) / (2 * a);

        time = (double)k + e + (double)1 / (double)120; // time of an event

        hr = (int)Math.floor(time);
        min = (int)Math.floor((time - hr) * 60);

        hz = ha[0] + e * (ha[2] - ha[0]);                 // azimuth of the sun at the event
        nz = -Math.cos(mDecensionArr[1]) * Math.sin(hz);
        dz = c * Math.sin(mDecensionArr[1]) - s * Math.cos(mDecensionArr[1]) * Math.cos(hz);
        az = Math.atan2(nz, dz) / mDR;
        if (az < 0) az = az + 360;

        if ((mVHzArr[0] < 0) && (mVHzArr[2] > 0))
        {
            mRiseTimeArr[0] = hr;
            mRiseTimeArr[1] = min;
            mRizeAzimuth = az;
            mIsSunrise = true;
        }

        if ((mVHzArr[0] > 0) && (mVHzArr[2] < 0))
        {
            mSetTimeArr[0] = hr;
            mSetTimeArr[1] = min;
            mSetAzimuth = az;
            mIsSunset = true;
        }

        return mVHzArr[2];
    }
    
    
  /// Calculate sunrise and sunset times. Returns false if time zone and longitude are incompatible.
    /// </summary>
    /// <param name="lat">Latitude coordinates.</param>
    /// <param name="lon">Longitude coordinates.</param>
    /// <param name="date">Date for which to calculate.</param>
    /// <param name="riseTime">Sunrise time (output)</param>
    /// <param name="setTime">Sunset time (output)</param>
    /// <param name="isSunrise">Whether or not the sun rises at that day</param>
    /// <param name="isSunset">Whether or not the sun sets at that day</param>
    private Calendar riseTime = at.gepa.lib.tools.time.TimeTool.getInstance();
    private Calendar setTime = at.gepa.lib.tools.time.TimeTool.getInstance();
    public boolean CalculateSunRiseSetTimes(LatitudeCoords lat, LongitudeCoords lon, Calendar date ) throws Exception
    {
        return CalculateSunRiseSetTimes(lat.ToDouble(), lon.ToDouble(), date);
    }
    
    /// <summary>
    /// Calculate sunrise and sunset times. Returns false if time zone and longitude are incompatible.
    /// </summary>
    /// <param name="lat">Latitude in decimal notation.</param>
    /// <param name="lon">Longitude in decimal notation.</param>
    /// <param name="date">Date for which to calculate.</param>
    /// <param name="riseTime">Sunrise time (output)</param>
    /// <param name="setTime">Sunset time (output)</param>
    /// <param name="isSunrise">Whether or not the sun rises at that day</param>
    /// <param name="isSunset">Whether or not the sun sets at that day</param>
    public boolean CalculateSunRiseSetTimes(double lat, double lon, Calendar date ) throws Exception
    {
    	double zone = -(int)Math.round( (date.get(Calendar.ZONE_OFFSET)+date.get(Calendar.DST_OFFSET)) / (3600 * 1000));
    	//zone += date.get(Calendar.DST_OFFSET) / (3600);
    	//zone *= -1;
        //double zone = -(int)Math.Round(TimeZone.CurrentTimeZone.GetUtcOffset(date).TotalSeconds / 3600);
        double jd = GetJulianDay(date) - 2451545;  // Julian day relative to Jan 1.5, 2000

        if ((Sign(zone) == Sign(lon)) && (zone != 0))
        {
            throw new Exception("WARNING: time zone and longitude are incompatible!");
        }

        lon = lon / 360;
        double tz = zone / 24;
        double ct = jd / 36525 + 1;                                 // centuries since 1900.0
        double t0 = LocalSiderealTimeForTimeZone(lon, jd, tz);      // local sidereal time

        // get sun position at start of day
        jd += tz;
        CalculateSunPosition(jd, ct);
        double ra0 = mSunPositionInSkyArr[0];
        double dec0 = mSunPositionInSkyArr[1];

        // get sun position at end of day
        jd += 1;
        CalculateSunPosition(jd, ct);
        double ra1 = mSunPositionInSkyArr[0];
        double dec1 = mSunPositionInSkyArr[1];

        // make continuous 
        if (ra1 < ra0)
            ra1 += 2 * Math.PI;

        // initialize
        mIsSunrise = false;
        mIsSunset = false;

        mRightAscentionArr[0] = ra0;
        mDecensionArr[0] = dec0;

        // check each hour of this day
        for (int k = 0; k < 24; k++)
        {
            mRightAscentionArr[2] = ra0 + (k + 1) * (ra1 - ra0) / 24;
            mDecensionArr[2] = dec0 + (k + 1) * (dec1 - dec0) / 24;
            mVHzArr[2] = TestHour(k, zone, t0, lat);

            // advance to next hour
            mRightAscentionArr[0] = mRightAscentionArr[2];
            mDecensionArr[0] = mDecensionArr[2];
            mVHzArr[0] = mVHzArr[2];
        }

        //riseTime = new DateTime(date.Year, date.Month, date.Day, mRiseTimeArr[0], mRiseTimeArr[1], 0);
        //setTime = new DateTime(date.Year, date.Month, date.Day, mSetTimeArr[0], mSetTimeArr[1], 0);
        riseTime.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH), mRiseTimeArr[0], mRiseTimeArr[1]);
        setTime.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH), mSetTimeArr[0], mSetTimeArr[1]);

        // neither sunrise nor sunset
        if ((!mIsSunrise) && (!mIsSunset))
        {
            if (mVHzArr[2] < 0)
            	riseTime = null; // Sun down all day
            else
            	setTime = null; // Sun up all day
        }
        // sunrise or sunset
        else
        {
            if (!mIsSunrise)
                // No sunrise this date
            	riseTime = null;
            else if (!mIsSunset)
                // No sunset this date
            	setTime = null;
        }

        return true;
    }
    
    public Calendar getRise()
    {
    	return riseTime;
    }
    public Calendar getSet()
    {
    	return setTime;
    }
}
