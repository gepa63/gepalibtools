package org.astronomie.info;

//05-025-W1601 / 120000
public class CalSky {
	public static class Coor
	{
		public Coor(){}
		
		public double lon;
		public double lat;

		public double ra;
		public double dec;
		public double raGeocentric;
		public double decGeocentric;

		public double az;
		public double alt;

		public double x;
		public double y;
		public double z;        

		public double radius;
		public double diameter;
		public double distance;
		public double distanceTopocentric;
		public double decTopocentric;
		public double raTopocentric;

		public double anomalyMean;
		public double parallax;
		public double orbitLon;

		public double moonAge;
		public double phase;

		public String moonPhase;
		public String sign;
	}

	/**
	 * set of variables for sunrise calculations 
	 */
	public static class Riseset
	{
		public Riseset(){}
		
		public double transit;
		public double rise;
		public double set;

		public double cicilTwilightMorning;
		public double cicilTwilightEvening;

		public double nauticalTwilightMorning;
		public double nauticalTwilightEvening;

		public double astronomicalTwilightMorning;
		public double astronomicalTwilightEvening;
	}

	/**
	 * time calculations
	 */
	public static class Time
	{
		public Time(){}
		
		public Time(double hhi)
		{
			 
			  double m 				= frac(hhi)*60;
			  int h 				= Int(hhi);
			  double s 				= frac(m)*60.;
			  m 					= Int(m);
			  if (s>=59.5) { m++; s -= 60.; }
			  if (m>=60)   { h++; m -= 60; }
			  s 					= Math.round(s);			  
			  
			  //create String HH:MM and HH:MM:SS
			  hhmmssString 			  = ""+h;
			  if (h<10) hhmmssString  = "0"+h;
			  hhmmssString 			 += ":";
			  if (m<10) hhmmssString += "0";
			  hhmmssString 			 += Int(m);
			  hhmmString 			  = hhmmssString;
			  hhmmssString 			 += ":";
			  if (s<10) hhmmssString += "0";
			  hhmmssString 			 += Int(s);

			  //create String HH:MM = dec and HH:MM:SS = dec
			  hhmmssStringdec = hhmmssString+" = "+hhi;
			  hhmmStringdec = hhmmString+" = "+hhi;
		}
		
		public int hh;
		public int mm;
		public int ss;

		public String hhmmString;
		public String hhmmStringdec;
		public String hhmmssString;
		public String hhmmssStringdec;
		
	}
	
	
	private static final double pi = Math.PI;
	private static final double 	DEG = pi/180.0;
	private static final double RAD = 180./pi;
	
	double sqr(double x)
	{
	  return x*x;
	}
	
	// return integer value, closer to 0
	static int Int(double x)
	{
	  if (x<0) 
	  { 
		  return (int)(Math.ceil(x)); 
	  } 
	  return (int)(Math.floor(x));
	}

	static double frac(double x) 
	{ 
		return(x-Math.floor(x)); 
	}

	static double Mod(double a, double b) 
	{
		return a % b;
	}
	// Modulo PI
	static double Mod2Pi(double x)
	{
	  x = Mod(x, 2.*pi);
	  return(x);
	}

	public double round1000000( double x) { return(Math.round(1000000.*x)/1000000.); }
	public double round100000( double x) { return(Math.round(100000.*x)/100000.); }
	public double round10000( double x) { return(Math.round(10000.*x)/10000.); }
	public double round1000( double x) { return(Math.round(1000.*x)/1000.); }
	public double round100( double x) { return(Math.round(100.*x)/100.); }
	public double round10( double x) { return(Math.round(10.*x)/10.); }
	
	private String empty = "--";

	private String HHMM(int hh) 
	{
	  if (hh==0) return(empty);
	  
	  double m = frac(hh)*60.;
	  int h = Int(hh);
	  
	  if (m>=59.5) { h++; m -=60.; }
	  
	  String sh = "";
	  m = Math.round(m);
	  if (h<10) 
		  sh = "0"+h;
	  sh = sh+":";
	  if (m<10) sh = sh+"0";
	  sh = sh + m;
	  return(sh+" = "+round1000(hh));
	}

	private String HHMMSS(int hh) 
	{
	  if (hh==0) return(empty);
	  
	  double m = frac(hh)*60;
	  int h = Int(hh);
	  double s = frac(m)*60.;
	  m = Int(m);
	  if (s>=59.5) { m++; s -=60.; }
	  if (m>=60)   { h++; m -=60; }
	  s = Math.round(s);
	  String sh="";
	  if (h<10) sh = "0"+h;
	  sh = sh+":";
	  if (m<10) sh = sh+"0";
	  sh = sh+m+":";
	  if (s<10) sh = sh+"0";
	  sh = sh+s;
	  return(sh+" = "+round10000(hh));
	}
	
	private String Sign(double lon)
	{ 
		String [] signs= new String[]{"Widder", "Stier", "Zwillinge", "Krebs", "Löwe", "Jungfrau", "Waage", "Skorpion", "Schütze", "Steinbock", "Wassermann", "Fische"};
		int idx = Math.abs((int)Math.floor(lon*RAD/30));
		if( idx >= signs.length ) return "unbekannt";
		return( signs[idx] );
	}

	// Calculate Julian date: valid only from 1.3.1901 to 28.2.2100
	static public double CalcJD(int day, int month, int year)
	{
		double jd = 2415020.5-64; // 1.1.1900 - correction of algorithm
		if (month<=2) { year--; month += 12; }
		jd += Int( (year-1900)*365.25 );
		jd += Int( 30.6001*(1+month) );
		return(jd + day);
	}

	// Julian Date to Greenwich Mean Sidereal Time
	static public double GMST(double JD)
	{
		double UT = frac(JD-0.5)*24.; // UT in hours
		JD = Math.floor(JD-0.5)+0.5;   // JD at 0 hours UT
		double T = (JD-2451545.0)/36525.0;
		double T0 = 6.697374558 + T*(2400.051336 + T*0.000025862);
		return(Mod(T0+UT*1.002737909, 24.));
	}


	// Convert Greenweek mean sidereal time to UT
	private double GMST2UT(double JD, double gmst)
	{
		JD = Math.floor(JD-0.5)+0.5;   // JD at 0 hours UT
		double T = (JD-2451545.0)/36525.0;
		double T0 = Mod(6.697374558 + T*(2400.051336 + T*0.000025862), 24.);
		//var UT = 0.9972695663*Mod((gmst-T0), 24.);
		double UT = 0.9972695663*((gmst-T0));
		return(UT);
	}

	// Local Mean Sidereal Time, geographical longitude in radians, East is positive
	private double GMST2LMST(double gmst, double lon)
	{
		double lmst = Mod(gmst+RAD*lon/15, 24.);
		return( lmst );
	}

	// Transform ecliptical coordinates (lon/lat) to equatorial coordinates (RA/dec)
	private Coor Ecl2Equ(Coor coor, double TDT)
	{
		double T = (TDT-2451545.0)/36525.; // Epoch 2000 January 1.5
		double eps = (23.+(26+21.45/60.)/60. + T*(-46.815 +T*(-0.0006 + T*0.00181) )/3600. )*DEG;
		double coseps = Math.cos(eps);
		double sineps = Math.sin(eps);
  
		double sinlon = Math.sin(coor.lon);
		coor.ra  = Mod2Pi( Math.atan2( (sinlon*coseps-Math.tan(coor.lat)*sineps), Math.cos(coor.lon) ) );
		coor.dec = Math.asin( Math.sin(coor.lat)*coseps + Math.cos(coor.lat)*sineps*sinlon );
		return coor;
	}

	// Transform equatorial coordinates (RA/Dec) to horizonal coordinates (azimuth/altitude)
	// Refraction is ignored
	private Coor Equ2Altaz(Coor coor, double TDT, double geolat, double lmst)
	{
		double cosdec = Math.cos(coor.dec);
		double sindec = Math.sin(coor.dec);
		double lha = lmst - coor.ra;
		double coslha = Math.cos(lha);
		double sinlha = Math.sin(lha);
		double coslat = Math.cos(geolat);
		double sinlat = Math.sin(geolat);
  
		double N = -cosdec * sinlha;
		double D = sindec * coslat - cosdec * coslha * sinlat;
		coor.az = Mod2Pi( Math.atan2(N, D) );
		coor.alt = Math.asin( sindec * sinlat + cosdec * coslha * coslat );
		return coor;
	}

	// Transform geocentric equatorial coordinates (RA/Dec) to topocentric equatorial coordinates
	private Coor GeoEqu2TopoEqu(Coor coor, Coor observer, double lmst)
	{
		double cosdec = Math.cos(coor.dec);
		double sindec = Math.sin(coor.dec);
		double coslst = Math.cos(lmst);
		double sinlst = Math.sin(lmst);
		double coslat = Math.cos(observer.lat); // we should use geocentric latitude, not geodetic latitude
		double sinlat = Math.sin(observer.lat);
		double rho = observer.radius; // observer-geocenter in Kilometer
  
		double x = coor.distance*cosdec*Math.cos(coor.ra) - rho*coslat*coslst;
		double y = coor.distance*cosdec*Math.sin(coor.ra) - rho*coslat*sinlst;
		double z = coor.distance*sindec - rho*sinlat;

		coor.distanceTopocentric = Math.sqrt(x*x + y*y + z*z);
		coor.decTopocentric = Math.asin(z/coor.distanceTopocentric);
		coor.raTopocentric = Mod2Pi( Math.atan2(y, x) );

		return coor;
	}

	// Calculate cartesian from polar coordinates
	private Coor EquPolar2Cart( double lon, double lat, double distance )
	{
		Coor cart = new Coor();
		double rcd = Math.cos(lat)*distance;
		cart.x = rcd*Math.cos(lon);
		cart.y = rcd*Math.sin(lon);
		cart.z = distance * Math.sin(lat);
		return(cart);
	}

	// Calculate observers cartesian equatorial coordinates (x,y,z in celestial frame) 
	// from geodetic coordinates (longitude, latitude, height above WGS84 ellipsoid)
	// Currently only used to calculate distance of a body from the observer
	private Coor Observer2EquCart( double lon, double lat, double height, double gmst )
	{
		double flat = 298.257223563;        // WGS84 flatening of earth
		double aearth = 6378.137;           // GRS80/WGS84 semi major axis of earth ellipsoid
		Coor cart = new Coor();
		// Calculate geocentric latitude from geodetic latitude
		double co = Math.cos (lat);
		double si = Math.sin (lat);
		double fl = 1.0 - 1.0 / flat;
		fl = fl * fl;
		si = si * si;
		double u = 1.0 / Math.sqrt (co * co + fl * si);
		double a = aearth * u + height;
		double b = aearth * fl * u + height;
		double radius = Math.sqrt (a * a * co * co + b * b * si); // geocentric distance from earth center
		cart.y = Math.acos (a * co / radius); // geocentric latitude, rad
		cart.x = lon; // longitude stays the same
		if (lat < 0.0) { cart.y = -cart.y; } // adjust sign
		cart = EquPolar2Cart( cart.x, cart.y, radius ); // convert from geocentric polar to geocentric cartesian, with regard to Greenwich
		// rotate around earth's polar axis to align coordinate system from Greenwich to vernal equinox
		double x=cart.x; 
		double y=cart.y;
		double rotangle = gmst/24*2*pi; // sideral time gmst given in hours. Convert to radians
		cart.x = x*Math.cos(rotangle)-y*Math.sin(rotangle);
		cart.y = x*Math.sin(rotangle)+y*Math.cos(rotangle);
		cart.radius = radius;
		cart.lon = lon;
		cart.lat = lat;
		return(cart);
	}

	// Calculate coordinates for Sun
	// Coordinates are accurate to about 10s (right ascension) 
	// and a few minutes of arc (declination)
	private Coor SunPosition( double TDT )
	{
		return SunPosition( TDT, 0, 0);
	}	
	private Coor SunPosition( double TDT, double geolat, double lmst)
	{
		double D = TDT-2447891.5;
  
		double eg = 279.403303*DEG;
		double wg = 282.768422*DEG;
		double e  = 0.016713;
		double a  = 149598500; // km
		double diameter0 = 0.533128*DEG; // angular diameter of Moon at a distance
  
		double MSun = 360*DEG/365.242191*D+eg-wg;
		double nu = MSun + 360.*DEG/pi*e*Math.sin(MSun);
  
		Coor sunCoor = new Coor();
		sunCoor.lon =  Mod2Pi(nu+wg);
		sunCoor.lat = 0;
		sunCoor.anomalyMean = MSun;
  
		sunCoor.distance = (1-sqr(e))/(1+e*Math.cos(nu)); // distance in astronomical units
		sunCoor.diameter = diameter0/sunCoor.distance; // angular diameter in radians
		sunCoor.distance *= a;                         // distance in km
		sunCoor.parallax = 6378.137/sunCoor.distance;  // horizonal parallax

		sunCoor = Ecl2Equ(sunCoor, TDT);
  
		// Calculate horizonal coordinates of sun, if geographic positions is given
		//if (geolat!=null && lmst!=null) {
		if (geolat!=0 && lmst!=0) 
		{
			sunCoor = Equ2Altaz(sunCoor, TDT, geolat, lmst);
		}
  
		sunCoor.sign = Sign(sunCoor.lon);
		return sunCoor;
	}

	// Calculate data and coordinates for the Moon
	// Coordinates are accurate to about 1/5 degree (in ecliptic coordinates)
	private Coor MoonPosition(Coor sunCoor, double TDT, Coor observer, double lmst)
	{
		double D = TDT-2447891.5;
		// Mean Moon orbit elements as of 1990.0
		double l0 = 318.351648*DEG;
		double P0 =  36.340410*DEG;
		double N0 = 318.510107*DEG;
		double i  = 5.145396*DEG;
		double e  = 0.054900;
		double a  = 384401; // km
		double diameter0 = 0.5181*DEG; // angular diameter of Moon at a distance
		double parallax0 = 0.9507*DEG; // parallax at distance a
  
		double l = 13.1763966*DEG*D+l0;
		double MMoon = l-0.1114041*DEG*D-P0; // Moon's mean anomaly M
		double N = N0-0.0529539*DEG*D;       // Moon's mean ascending node longitude
		double C = l-sunCoor.lon;
		double Ev = 1.2739*DEG*Math.sin(2*C-MMoon);
		double Ae = 0.1858*DEG*Math.sin(sunCoor.anomalyMean);
		double A3 = 0.37*DEG*Math.sin(sunCoor.anomalyMean);
		double MMoon2 = MMoon+Ev-Ae-A3;  // corrected Moon anomaly
		double Ec = 6.2886*DEG*Math.sin(MMoon2);  // equation of centre
		double A4 = 0.214*DEG*Math.sin(2*MMoon2);
		double l2 = l+Ev+Ec-Ae+A4; // corrected Moon's longitude
		double V = 0.6583*DEG*Math.sin(2*(l2-sunCoor.lon));
		double l3 = l2+V; // true orbital longitude;

		double N2 = N-0.16*DEG*Math.sin(sunCoor.anomalyMean);
  
		Coor moonCoor = new Coor();  
		moonCoor.lon = Mod2Pi( N2 + Math.atan2( Math.sin(l3-N2)*Math.cos(i), Math.cos(l3-N2) ) );
		moonCoor.lat = Math.asin( Math.sin(l3-N2)*Math.sin(i) );
		moonCoor.orbitLon = l3;
  
		moonCoor = Ecl2Equ(moonCoor, TDT);
		// relative distance to semi mayor axis of lunar oribt
		moonCoor.distance = (1-sqr(e)) / (1+e*Math.cos(MMoon2+Ec) );
		moonCoor.diameter = diameter0/moonCoor.distance; // angular diameter in radians
		moonCoor.parallax = parallax0/moonCoor.distance; // horizontal parallax in radians
		moonCoor.distance *= a; // distance in km

		// Calculate horizonal coordinates of sun, if geographic positions is given
		if (observer!=null && lmst!=0) {
			// transform geocentric coordinates into topocentric (==observer based) coordinates
			moonCoor = GeoEqu2TopoEqu(moonCoor, observer, lmst);
			moonCoor.raGeocentric = moonCoor.ra; // backup geocentric coordinates
			moonCoor.decGeocentric = moonCoor.dec;
			moonCoor.ra=moonCoor.raTopocentric;
			moonCoor.dec=moonCoor.decTopocentric;
			moonCoor = Equ2Altaz(moonCoor, TDT, observer.lat, lmst); // now ra and dec are topocentric
		}
  
		// Age of Moon in radians since New Moon (0) - Full Moon (pi)
		moonCoor.moonAge = Mod2Pi(l3-sunCoor.lon);   
		moonCoor.phase   = 0.5*(1-Math.cos(moonCoor.moonAge)); // Moon phase, 0-1
  
		String [] phases = new String[]{"Neumond", "Zunehmende Sichel", "Erstes Viertel", "Zunehmender Mond", 
				"Vollmond", "Abnehmender Mond", "Letztes Viertel", "Abnehmende Sichel", "Neumond"};
		double mainPhase = 1./29.53*360*DEG; // show 'Newmoon, 'Quarter' for +/-1 day arond the actual event
		int p = (int)Mod(moonCoor.moonAge, 90.*DEG);
		if (p < mainPhase || p > 90*DEG-mainPhase) 
			p = (int)(2*Math.round(moonCoor.moonAge / (90.*DEG)));
		else 
			p = (int)(2*Math.floor(moonCoor.moonAge / (90.*DEG))+1);
		moonCoor.moonPhase = phases[p];
  
		moonCoor.sign = Sign(moonCoor.lon);
		return(moonCoor);
	}

	// Rough refraction formula using standard atmosphere: 1015 mbar and 10°C
	// Input true altitude in radians, Output: increase in altitude in degrees
	private double Refraction(double alt)
	{
		double altdeg = alt*RAD;
		if (altdeg<-2 || altdeg>=90) return(0);
   
		double pressure    = 1015;
		double temperature = 10;
		if (altdeg>15) return( 0.00452*pressure/( (273+temperature)*Math.tan(alt)) );
  
		double y = alt;
		double D = 0.0;
		double P = (pressure-80.)/930.;
		double Q = 0.0048*(temperature-10.);
		double y0 = y;
		double D0 = D;

		for (int i=0; i<3; i++) 
		{
			double N = y+(7.31/(y+4.4));
			N = 1./Math.tan(N*DEG);
			D = N*P/(60.+Q*(N+39.));
			N = y-y0;
			y0 = D-D0-N;
			if ((N != 0.) && (y0 != 0.)) { N = y-N*(alt+D-y)/y0; }
			else { N = alt+D; }
			y0 = y;
			D0 = D;
			y = N;
		}
		return( D ); // Hebung durch Refraktion in radians
	}


	// returns Greenwich sidereal time (hours) of time of rise 
	// and set of object with coordinates coor.ra/coor.dec
	// at geographic position lon/lat (all values in radians)
	// Correction for refraction and semi-diameter/parallax of body is taken care of in function RiseSet
	// h is used to calculate the twilights. It gives the required elevation of the disk center of the sun
	private Riseset GMSTRiseSet(Coor coor, double lon, double lat, Double _h)
	{
		double h = (_h == null) ? 0. : _h; // set default value
		Riseset riseset = new Riseset();
		//  var tagbogen = Math.acos(-Math.tan(lat)*Math.tan(coor.dec)); // simple formula if twilight is not required
		double tagbogen = Math.acos((Math.sin(h) - Math.sin(lat)*Math.sin(coor.dec)) / (Math.cos(lat)*Math.cos(coor.dec)));

		riseset.transit =     RAD/15*(         +coor.ra-lon);
		riseset.rise    = 24.+RAD/15*(-tagbogen+coor.ra-lon); // calculate GMST of rise of object
		riseset.set     =     RAD/15*(+tagbogen+coor.ra-lon); // calculate GMST of set of object
	
		// using the modulo function Mod, the day number goes missing. This may get a problem for the moon
		riseset.transit = Mod(riseset.transit, 24);
		riseset.rise    = Mod(riseset.rise, 24);
		riseset.set     = Mod(riseset.set, 24);

		return(riseset);
	}


	// Find GMST of rise/set of object from the two calculates 
	// (start)points (day 1 and 2) and at midnight UT(0)
	private double InterpolateGMST(double gmst0, double gmst1, double gmst2, double timefactor)
	{
		return( (timefactor*24.07*gmst1- gmst0*(gmst2-gmst1)) / (timefactor*24.07+gmst1-gmst2) );
	}

	// JD is the Julian Date of 0h UTC time (midnight)
	private Riseset RiseSet(double jd0UT, Coor coor1, Coor coor2, double lon, double lat, double timeinterval, Double _altitude)
	{
		// altitude of sun center: semi-diameter, horizontal parallax and (standard) refraction of 34'
		double alt = 0.; // calculate 
		double altitude = (_altitude == null) ? 0. : _altitude; // set default value

		// true height of sun center for sunrise and set calculation. Is kept 0 for twilight (ie. altitude given):
		if (altitude == 0) altitude = alt = 0.5*coor1.diameter-coor1.parallax+34./60*DEG; 
  
		Riseset rise1 = GMSTRiseSet(coor1, lon, lat, altitude);
		Riseset rise2 = GMSTRiseSet(coor2, lon, lat, altitude);
  
		Riseset rise = new Riseset();
  
		// unwrap GMST in case we move across 24h -> 0h
		if (rise1.transit > rise2.transit && Math.abs(rise1.transit-rise2.transit)>18) rise2.transit += 24;
		if (rise1.rise    > rise2.rise    && Math.abs(rise1.rise   -rise2.rise)>18)    rise2.rise += 24;
		if (rise1.set     > rise2.set     && Math.abs(rise1.set    -rise2.set)>18)     rise2.set  += 24;
		double T0 = GMST(jd0UT);
		//  var T02 = T0-zone*1.002738; // Greenwich sidereal time at 0h time zone (zone: hours)

		// Greenwich sidereal time for 0h at selected longitude
		double T02 = T0-lon*RAD/15*1.002738; if (T02 < 0) T02 += 24; 

		if (rise1.transit < T02) { rise1.transit += 24; rise2.transit += 24; }
		if (rise1.rise    < T02) { rise1.rise    += 24; rise2.rise    += 24; }
		if (rise1.set     < T02) { rise1.set     += 24; rise2.set     += 24; }
  
		// Refraction and Parallax correction
		double decMean = 0.5*(coor1.dec+coor2.dec);
		double psi = Math.acos(Math.sin(lat)/Math.cos(decMean));
		double y = Math.asin(Math.sin(alt)/Math.sin(psi));
		double dt = 240*RAD*y/Math.cos(decMean)/3600; // time correction due to refraction, parallax

		rise.transit = GMST2UT( jd0UT, InterpolateGMST( T0, rise1.transit, rise2.transit, timeinterval) );
		rise.rise    = GMST2UT( jd0UT, InterpolateGMST( T0, rise1.rise,    rise2.rise,    timeinterval) -dt );
		rise.set     = GMST2UT( jd0UT, InterpolateGMST( T0, rise1.set,     rise2.set,     timeinterval) +dt );
  
		return(rise);  
	}

	// Find (local) time of sunrise and sunset, and twilights
	// JD is the Julian Date of 0h local time (midnight)
	// Accurate to about 1-2 minutes
	// recursive: 1 - calculate rise/set in UTC in a second run
	// recursive: 0 - find rise/set on the current local day. This is set when doing the first call to this function
	public Riseset SunRise(double JD, double deltaT, double lon, double lat, double zone, boolean recursive)
	{
		double jd0UT = Math.floor(JD-0.5)+0.5;   // JD at 0 hours UT
		Coor coor1 = SunPosition(jd0UT+  deltaT/24./3600.);
		Coor coor2 = SunPosition(jd0UT+1.+deltaT/24./3600.); // calculations for next day's UTC midnight
  
		Riseset risetemp = new Riseset();
		Riseset rise = new Riseset();
		// rise/set time in UTC. 
		rise = RiseSet(jd0UT, coor1, coor2, lon, lat, 1, null); 
		if (!recursive) 
		{ // check and adjust to have rise/set time on local calendar day
			if (zone>0) 
			{
				// rise time was yesterday local time -> calculate rise time for next UTC day
				if (rise.rise>=24-zone || rise.transit>=24-zone || rise.set>=24-zone) {
					risetemp = SunRise(JD+1, deltaT, lon, lat, zone, true);
					if (rise.rise>=24-zone) rise.rise = risetemp.rise;
					if (rise.transit >=24-zone) rise.transit = risetemp.transit;
					if (rise.set >=24-zone) rise.set  = risetemp.set;
				}
			}
			else if (zone<0) 
			{
				// rise time was yesterday local time -> calculate rise time for next UTC day
				if (rise.rise<-zone || rise.transit<-zone || rise.set<-zone) {
					risetemp = SunRise(JD-1, deltaT, lon, lat, zone, true);
					if (rise.rise<-zone) rise.rise = risetemp.rise;
					if (rise.transit<-zone) rise.transit = risetemp.transit;
					if (rise.set <-zone) rise.set  = risetemp.set;
				}
			}
	
			rise.transit = Mod(rise.transit+zone, 24.);
			rise.rise    = Mod(rise.rise   +zone, 24.);
			rise.set     = Mod(rise.set    +zone, 24.);

			// Twilight calculation
			// civil twilight time in UTC. 
			risetemp = RiseSet(jd0UT, coor1, coor2, lon, lat, 1, -6.*DEG);
			rise.cicilTwilightMorning = Mod(risetemp.rise +zone, 24.);
			rise.cicilTwilightEvening = Mod(risetemp.set  +zone, 24.);
		
			// nautical twilight time in UTC. 
			risetemp = RiseSet(jd0UT, coor1, coor2, lon, lat, 1, -12.*DEG);
			rise.nauticalTwilightMorning = Mod(risetemp.rise +zone, 24.);
			rise.nauticalTwilightEvening = Mod(risetemp.set  +zone, 24.);
		
			// astronomical twilight time in UTC. 
			risetemp = RiseSet(jd0UT, coor1, coor2, lon, lat, 1, -18.*DEG);
			rise.astronomicalTwilightMorning = Mod(risetemp.rise +zone, 24.);
			rise.astronomicalTwilightEvening = Mod(risetemp.set  +zone, 24.);
		}
		return( rise );  
	}


	// Find local time of moonrise and moonset
	// JD is the Julian Date of 0h local time (midnight)
	// Accurate to about 5 minutes or better
	// recursive: 1 - calculate rise/set in UTC
	// recursive: 0 - find rise/set on the current local day (set could also be first)
	// returns '' for moonrise/set does not occur on selected day
	public Riseset MoonRise(double JD, double deltaT, double lon, double lat, double zone, boolean recursive)
	{
		double timeinterval = 0.5;
  
		double jd0UT = Math.floor(JD-0.5)+0.5;   // JD at 0 hours UT
		Coor suncoor1 = SunPosition(jd0UT+ deltaT/24./3600.);
		Coor coor1 = MoonPosition(suncoor1, jd0UT+ deltaT/24./3600., null, 0);

		Coor suncoor2 = SunPosition(jd0UT +timeinterval + deltaT/24./3600., 0, 0); // calculations for noon
		// calculations for next day's midnight
		Coor coor2 = MoonPosition(suncoor2, jd0UT +timeinterval + deltaT/24./3600., null, 0); 
  
		Riseset risetemp = new Riseset();
		Riseset rise = new Riseset();
		Riseset riseprev = new Riseset();
  
		// rise/set time in UTC, time zone corrected later.
		// Taking into account refraction, semi-diameter and parallax
		rise = RiseSet(jd0UT, coor1, coor2, lon, lat, timeinterval, null); 
  
		if (!recursive) 
		{ // check and adjust to have rise/set time on local calendar day
			if (zone>0) 
			{
				// recursive call to MoonRise returns events in UTC
				riseprev = MoonRise(JD-1., deltaT, lon, lat, zone, true); 
      
	      // recursive call to MoonRise returns events in UTC
	      //risenext = MoonRise(JD+1, deltaT, lon, lat, zone, 1);
	        //alert("yesterday="+riseprev.transit+"  today="+rise.transit+" tomorrow="+risenext.transit);
	        //alert("yesterday="+riseprev.rise+"  today="+rise.rise+" tomorrow="+risenext.rise);
	        //alert("yesterday="+riseprev.set+"  today="+rise.set+" tomorrow="+risenext.set);
	
	      if (rise.transit >= 24.-zone || rise.transit < -zone) { // transit time is tomorrow local time
	        if (riseprev.transit < 24.-zone) rise.transit = 0; // there is no moontransit today
	        else rise.transit  = riseprev.transit;
	      }
	
	      if (rise.rise >= 24.-zone || rise.rise < -zone) { // rise time is tomorrow local time
	        if (riseprev.rise < 24.-zone) rise.rise = 0; // there is no moontransit today
	        else rise.rise  = riseprev.rise;
	      }
	
	      if (rise.set >= 24.-zone || rise.set < -zone) { // set time is tomorrow local time
	        if (riseprev.set < 24.-zone) rise.set = 0; // there is no moontransit today
	        else rise.set  = riseprev.set;
	      }
	
	    }
	    else if (zone<0) {
	      // rise/set time was tomorrow local time -> calculate rise time for former UTC day
	      if (rise.rise<-zone || rise.set<-zone || rise.transit<-zone) { 
	        risetemp = MoonRise(JD+1., deltaT, lon, lat, zone, true);
	        
	        if (rise.rise < -zone) {
	          if (risetemp.rise > -zone) rise.rise = 0; // there is no moonrise today
	          else rise.rise = risetemp.rise;
	        }
	        
	        if (rise.transit < -zone)
	        {
	          if (risetemp.transit > -zone)  rise.transit = 0; // there is no moonset today
	          else rise.transit  = risetemp.transit;
	        }
	        
	        if (rise.set < -zone)
	        {
	          if (risetemp.set > -zone)  rise.set = 0; // there is no moonset today
	          else rise.set  = risetemp.set;
	        }
	        
	      }
	    }
	    
	    if (rise.rise != 0)    rise.rise = Mod(rise.rise+zone, 24.);    // correct for time zone, if time is valid
	    if (rise.transit!= 0) rise.transit  = Mod(rise.transit +zone, 24.); // correct for time zone, if time is valid
	    if (rise.set!= 0)     rise.set  = Mod(rise.set +zone, 24.);    // correct for time zone, if time is valid
	  }
	  return( rise );  
	}

	
	/*
	private void Compute()
	{

  if (eval(form.Year.value)<=1900 || eval(form.Year.value)>=2100 ) {
    alert("Dies Script erlaubt nur Berechnungen"+
        "in der Zeitperiode 1901-2099. Angezeigte Resultat sind ungültig.");
    return;
  }

  JD0 = CalcJD( eval(form.Day.value), eval(form.Month.value), eval(form.Year.value) );
  JD  = JD0
       +( eval(form.Hour.value) -eval(form.Zone.value.replace(/,/,'.')) +eval(form.Minute.value)/60. 
       + eval(form.Second.value.replace(/,/,'.'))/3600.) /24.;
  TDT = JD+eval(form.DeltaT.value.replace(/,/,'.'))/24./3600.;

  lat      = eval(form.Lat.value.replace(/,/,'.'))*DEG; // geodetic latitude of observer on WGS84
  lon      = eval(form.Lon.value.replace(/,/,'.'))*DEG; // latitude of observer
  height   = 0 * 0.001; // altiude of observer in meters above WGS84 ellipsoid (and converted to kilometers)

  var gmst = GMST(JD);
  var lmst = GMST2LMST(gmst, lon);
  
  observerCart = Observer2EquCart(lon, lat, height, gmst); // geocentric cartesian coordinates of observer
 
  sunCoor  = SunPosition(TDT, lat, lmst*15.*DEG);   // Calculate data for the Sun at given time
  moonCoor = MoonPosition(sunCoor, TDT, observerCart, lmst*15.*DEG);    // Calculate data for the Moon at given time

  form.JD.value = round100000(JD);
  form.GMST.value = HHMMSS(gmst);
  form.LMST.value = HHMMSS(lmst);

  if (eval(form.Minute.value)<10) form.Minute.value = "0"+eval(form.Minute.value);
  if (eval(form.Month.value)<10) form.Month.value = "0"+eval(form.Month.value);

  form.SunLon.value  = round1000(sunCoor.lon*RAD);
  form.SunRA.value   = HHMM(sunCoor.ra*RAD/15);
  form.SunDec.value  = round1000(sunCoor.dec*RAD);
  form.SunAz.value   = round100(sunCoor.az*RAD);
  form.SunAlt.value  = round10(sunCoor.alt*RAD+Refraction(sunCoor.alt));  // including refraction

  form.SunSign.value = sunCoor.sign;
  form.SunDiameter.value = round100(sunCoor.diameter*RAD*60.); // angular diameter in arc seconds
  form.SunDistance.value = round10(sunCoor.distance);

  // Calculate distance from the observer (on the surface of earth) to the center of the sun
  sunCart      = EquPolar2Cart(sunCoor.ra, sunCoor.dec, sunCoor.distance);
  form.SunDistanceObserver.value = round10( Math.sqrt( sqr(sunCart.x-observerCart.x) + sqr(sunCart.y-observerCart.y) + sqr(sunCart.z-observerCart.z) ));

  // JD0: JD of 0h UTC time
  sunRise = SunRise(JD0, eval(form.DeltaT.value.replace(/,/,'.')), lon, lat, eval(form.Zone.value.replace(/,/,'.')), 0);

  form.SunTransit.value = HHMM(sunRise.transit);
  form.SunRise.value    = HHMM(sunRise.rise);
  form.SunSet.value     = HHMM(sunRise.set);

  form.SunCivilTwilightMorning.value    = HHMM(sunRise.cicilTwilightMorning);
  form.SunCivilTwilightEvening.value    = HHMM(sunRise.cicilTwilightEvening);
  form.SunNauticalTwilightMorning.value    = HHMM(sunRise.nauticalTwilightMorning);
  form.SunNauticalTwilightEvening.value    = HHMM(sunRise.nauticalTwilightEvening);
  form.SunAstronomicalTwilightMorning.value    = HHMM(sunRise.astronomicalTwilightMorning);
  form.SunAstronomicalTwilightEvening.value    = HHMM(sunRise.astronomicalTwilightEvening);

  form.MoonLon.value = round1000(moonCoor.lon*RAD);
  form.MoonLat.value = round1000(moonCoor.lat*RAD);
  form.MoonRA.value  = HHMM(moonCoor.ra*RAD/15.);
  form.MoonDec.value = round1000(moonCoor.dec*RAD);
  form.MoonAz.value   = round100(moonCoor.az*RAD);
  form.MoonAlt.value  = round10(moonCoor.alt*RAD+Refraction(moonCoor.alt));  // including refraction
  form.MoonAge.value = round1000(moonCoor.moonAge*RAD);
  form.MoonPhaseNumber.value = round1000(moonCoor.phase);
  form.MoonPhase.value    = moonCoor.moonPhase;

  form.MoonSign.value     = moonCoor.sign;
  form.MoonDistance.value = round10(moonCoor.distance);
  form.MoonDiameter.value = round100(moonCoor.diameter*RAD*60.); // angular diameter in arc seconds

  // Calculate distance from the observer (on the surface of earth) to the center of the moon
  moonCart      = EquPolar2Cart(moonCoor.raGeocentric, moonCoor.decGeocentric, moonCoor.distance);
  form.MoonDistanceObserver.value = round10( Math.sqrt( sqr(moonCart.x-observerCart.x) + sqr(moonCart.y-observerCart.y) + sqr(moonCart.z-observerCart.z) ));

  moonRise = MoonRise(JD0, eval(form.DeltaT.value.replace(/,/,'.')), lon, lat, eval(form.Zone.value.replace(/,/,'.')), 0);

  form.MoonTransit.value = HHMM(moonRise.transit);
  form.MoonRise.value    = HHMM(moonRise.rise);
  form.MoonSet.value     = HHMM(moonRise.set);
}
*/	
}
