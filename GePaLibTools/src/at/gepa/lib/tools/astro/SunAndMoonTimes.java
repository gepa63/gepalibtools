package at.gepa.lib.tools.astro;

import java.util.Calendar;

import at.gepa.lib.tools.time.TimeTool;

//http://www.mondkalender-online.at/sonnenaufgang.asp?Monat=4&Jahr=2016&zeitzone=2&ort=Wien&Lat=48.13&Lon=16.22
	
	
public class SunAndMoonTimes 
{
	private Calendar Heute;

	double JD_Java(double JD)
	{
		return (JD - 2440587.5) * 86400000;
	}
	
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
	
	double Java_JD(Calendar Zeit)
	{
		return GetJulianDay(Zeit);
		//return Zeit / 86400000 + 2440587.5;
	}
	
	double CS(double x){
		return Math.cos(x * .0174532925199433);
	}
	double SN(double x)
	{
		return Math.sin(x * .0174532925199433);
	}
	
	String In_Tagen(double JDE)
	{
		double Diff = Tagesdifferenz(JDE);
	    return " int tagen: " + Math.floor(Diff) + "," + Math.round((Diff - Math.floor(Diff)) * 10) + "...Tagen am";
	}
	double Tagesdifferenz(double JDE){
		    // aktuelles Datum
		double JDH = Java_JD(Heute);
		return (Math.round((JDE - JDH) * 10)) /10;
	}
	double Var_o(double k, double t)
	{
		return 124.7746 - 1.5637558 * k + .0020691 * t * t + .00000215 * t * t * t;
	}
	double Var_f(double k, double t)
	{
		return 160.7108 + 390.67050274 * k - .0016341 * t * t - .00000227 * t * t * t + .000000011 * t * t * t * t;
	}
	double Var_m1(double k, double t)
	{
		return 201.5643 + 385.81693528 * k + .1017438 * t * t + .00001239 * t * t * t - .000000058 * t * t * t * t;
	}
	double Var_m(double k, double t){
		return 2.5534 + 29.10535669 * k - .0000218 * t * t - .00000011 * t * t * t;
	}
	double Var_e(double t)
	{
		return 1 - .002516 * t - .0000074 * t * t;
	}
	double Var_JDE(double k, double t)
	{
		return 2451550.09765 + 29.530588853 * k + .0001337 * t * t - .00000015 * t * t * t + .00000000073 * t * t * t * t;
	}
	double Var_k(double tz)
	{
		return (Heute.get(Calendar.YEAR) + (Heute.get((Calendar.MONTH)+1) * 30.4 + Heute.get(Calendar.DAY_OF_MONTH) + tz) / 365 - 2000) * 12.3685;
	}
	
	String Gradumrechnung(double Grad){
	    String n1 = "", n2 = "";
	    double G = Math.floor(Grad);
	    double M = Math.floor((Grad - G) * 60);
	    double S = Math.round(((Grad - G) * 60 - M) * 60);
	    if(M < 10) n1 = "0";
	    if(S < 10) n2 = "0";
	    return G + "°" + n1 + M + "'" + n2 + S + "&quot;";
	}
	double Neumond(double k)
	{
		double JDE, e, m, m1, f, o;
	    k = Math.floor(k);
	    double t = k / 1236.85;
	    e = Var_e(t);
	    m = Var_m(k, t);
	    m1 = Var_m1(k, t);
	    f = Var_f(k, t);
	    o = Var_o(k, t);
	    //Neumondkorrekturen
	    JDE = Var_JDE(k, t);
	    JDE += -.4072 * SN(m1) + .17241 * e * SN(m) + .01608 * SN(2 * m1) + .01039 * SN(2 * f) + .00739 * e * SN(m1 - m) - .00514 * e * SN(m1 + m) + .00208 * e * e * SN(2 * m) - .00111 * SN(m1 - 2 * f) - .00057 * SN(m1 + 2 * f);
	    JDE += .00056 * e * SN(2 * m1 + m) - .00042 * SN(3 * m1) + .00042 * e * SN(m + 2 * f) + .00038 * e * SN(m - 2 * f) - .00024 * e * SN(2 * m1 - m) - .00017 * SN(o) - .00007 * SN(m1 + 2 * m) + .00004 * SN(2 * m1 - 2 * f);
	    JDE += .00004 * SN(3 * m) + .00003 * SN(m1 + m - 2 * f) + .00003 * SN(2 * m1 + 2 * f) - .00003 * SN(m1 + m + 2 * f) + .00003 * SN(m1 - m + 2 * f) - .00002 * SN(m1 - m - 2 * f) - .00002 * SN(3 * m1 + m);
	    JDE += .00002 * SN(4 * m1);
	    return Korrektur(JDE, t, k);
	  }
	double Viertel(double k, double modus)
	{
	    // modus = .25 = Erstes Viertel, modus = .75 = Letztes Viertel
		double JDE, w, e, m, m1, f, o;
	    k = Math.floor(k) + modus;
	    double t = k / 1236.85;
	    e = Var_e(t);
	    m = Var_m(k, t);
	    m1 = Var_m1(k, t);
	    f = Var_f(k, t);
	    o = Var_o(k, t);
	    // Viertelmondkorrekturen
	    JDE = Var_JDE(k, t);
	    JDE += -.62801 * SN(m1) + .17172 * e * SN(m) - .01183 * e * SN(m1 + m) + .00862 * SN(2 * m1) + .00804 * SN(2 * f) + .00454 * e * SN(m1 - m) + .00204 * e * e * SN(2 * m) - .0018 * SN(m1 - 2 * f) - .0007 * SN(m1 + 2 * f);
	    JDE += -.0004 * SN(3 * m1) - .00034 * e * SN(2 * m1 - m) + .00032 * e * SN(m + 2 * f) + .00032 * e * SN(m - 2 * f) - .00028 * e * e * SN(m1 + 2 * m) + .00027 * e * SN(2 * m1 + m) - .00017 * SN(o);
	    JDE += -.00005 * SN(m1 - m - 2 * f) + .00004 * SN(2 * m1 + 2 * f) - .00004 * SN(m1 + m + 2 * f) + .00004 * SN(m1 - 2 * m) + .00003 * SN(m1 + m - 2 * f) + .00003 * SN(3 * m) + .00002 * SN(2 * m1 - 2 * f);
	    JDE += .00002 * SN(m1 - m + 2 * f) - .00002 * SN(3 * m1 + m);
	    w = .00306 - .00038 * e * CS(m) + .00026 * CS(m1) - .00002 * CS(m1 - m) + .00002 * CS(m1 + m) + .00002 * CS(2 * f);
	    JDE += (modus == .25) ? w : -w;
	    return Korrektur(JDE, t, k);
	  }
	  double Vollmond(double k)
	  {
		  double JDE, e, m, m1, f, o;
		  k = Math.floor(k) + .5;
		  double t = k / 1236.85;
		  e = Var_e(t);
		  m = Var_m(k, t);
		  m1 = Var_m1(k, t);
		  f = Var_f(k, t);
		  o = Var_o(k, t);
		  //Vollmondkorrekturen
		  JDE = Var_JDE(k, t);
		  JDE += -.40614 * SN(m1) + .17302 * e * SN(m) + .01614 * SN(2 * m1) + .01043 * SN(2 * f) + .00734 * e * SN(m1 - m) - .00515 * e * SN(m1 + m) + .00209 * e * e * SN(2 * m) - .00111 * SN(m1 - 2 * f) - .00057 * SN(m1 + 2 * f);
		  JDE += .00056 * e * SN(2 * m1 + m) - .00042 * SN(3 * m1) + .00042 * e * SN(m + 2 * f) + .00038 * e * SN(m - 2 * f) - .00024 * e * SN(2 * m1 - m) - .00017 * SN(o) - .00007 * SN(m1 + 2 * m) + .00004 * SN(2 * m1 - 2 * f);
		  JDE += .00004 * SN(3 * m) + .00003 * SN(m1 + m - 2 * f) + .00003 * SN(2 * m1 + 2 * f) - .00003 * SN(m1 + m + 2 * f) + .00003 * SN(m1 - m + 2 * f) - .00002 * SN(m1 - m - 2 * f) - .00002 * SN(3 * m1 + m);
		  JDE += .00002 * SN(4 * m1);
		  return Korrektur(JDE, t, k);
	  }
	  double Korrektur(double JDE, double t, double k)
	  {
	    //Zusätzlichen Korrekturen
	    JDE += .000325 * SN(299.77 + .107408 * k - .009173 * t * t) + .000165 * SN(251.88 + .016321 * k) + .000164 * SN(251.83 + 26.651886 * k) + .000126 * SN(349.42 + 36.412478 * k) + .00011 * SN(84.66 + 18.206239 * k);
	    JDE += .000062 * SN(141.74 + 53.303771 * k) + .00006 * SN(207.14 + 2.453732 * k) + .000056 * SN(154.84 + 7.30686 * k) + .000047 * SN(34.52 + 27.261239 * k) + .000042 * SN(207.19 + .121824 * k) + .00004 * SN(291.34 + 1.844379 * k);
	    JDE += .000037 * SN(161.72 + 24.198154 * k) + .000035 * SN(239.56 + 25.513099 * k) + .000023 * SN(331.55 + 3.592518 * k);
	    return JDE;
	  }
	  double Finsternis(double k, double Typ, double Modus){
	    // Typ = .5 = Mondfinsternis; Typ = 0 = Sonnenfinsternis
	    // Modus = 0 = Finsternis partiell, Modus = 1 = Finsternis total, Modus = 2 = Sonnenfinsternis ringförmig
		  double t, f, JDE, Ringtest;
		  k = Math.floor(k) + Typ;
		  t = k / 1236.85;
		  f = Var_f(k, t);
		  JDE = 0;
		  Ringtest = 0;
		  if(SN(Math.abs(f)) <= .36)
		  {
			  double var1, var2, o, f1, a1, e, m, m1, p, q, g, u;
		      o = Var_o(k, t);
		      f1 = f - .02665 * SN(o);
		      a1 = 299.77 + .107408 * k - .009173 * t * t;
		      e = Var_e(t);
		      m = Var_m(k, t);
		      m1 = Var_m1(k, t);
		      p = .207 * e * SN(m) + .0024 * e * SN(2 * m) - .0392 * SN(m1) + .0116 * SN(2 * m1) - .0073 * e * SN(m1 + m) + .0067 * e * SN(m1 - m) + .0118 * SN(2 * f1);
		      q = 5.2207 - .0048 * e * CS(m) + .002 * e * CS(2 * m) - .3299 * CS(m1) - .006 * e * CS(m1 + m) + .0041 * e * CS(m1 - m);
		      g = (p * CS(f1) + q * SN(f1)) * (1 - .0048 * CS(Math.abs(f1)));
		      u = .0059 + .0046 * e * CS(m) - .0182 * CS(m1) + .0004 * CS(2 * m1) - .0005 * CS(m + m1);
		      JDE = Var_JDE(k, t);
		      JDE += (Typ != 0) ? - .4065 * SN(m1) + .1727 * e * SN(m) : - .4075 * SN(m1) + .1721 * e * SN(m);
		      JDE += .0161 * SN(2 * m1) - .0097 * SN(2 * f1) + .0073 * e * SN(m1 - m) - .005 * e * SN(m1 + m) - .0023 * SN(m1 - 2 * f1) + .0021 * e * SN(2 * m);
		      JDE += .0012 * SN(m1 + 2 * f1) + .0006 * e * SN(2 * m1 + m) - .0004 * SN(3 * m1) - .0003 * e * SN(m + 2 * f1) + .0003 * SN(a1) - .0002 * e * SN(m - 2 * f1) - .0002 * e * SN(2 * m1 - m) - .0002 * SN(o);
		      if(Typ != 0){
		        if((1.0248 - u - Math.abs(g)) / .545 <= 0) JDE = 0; // keine Mf
		        if(Modus == 0 && (1.0128 - u - Math.abs(g)) / .545 > 0 && (.4678 - u) * (.4678 - u) - g * g > 0) JDE = 0; // keine partielle Mf
		        if(Modus == 1 && ((1.0128 - u - Math.abs(g)) / .545 <= 0 != (.4678 - u) * (.4678 - u) - g * g <= 0)) JDE = 0; // keine totale Mf
		      }
		      else{
		        if(Math.abs(g) > 1.5433 + u) JDE = 0; // keine SF
		        if(Modus == 0 && ((g >= -.9972 && g <= .9972) || (Math.abs(g) >= .9972 && Math.abs(g) < .9972 + Math.abs(u)))) JDE = 0; // keine partielle Sf
		        if(Modus > 0){
		           if((g < -.9972 || g > .9972) || (Math.abs(g) < .9972 && Math.abs(g) > .9972 + Math.abs(u))) JDE = 0; // keine ringförmige oder totale SF
		           if(u > .0047 || u >= .00464 * Math.sqrt(1 - g * g)) Ringtest = 1; // keine totale Sf
		           if(Ringtest == 1 && Modus == 1) JDE = 0;
		           if(Ringtest == 0 && Modus == 2) JDE = 0;
		        }
		      }
	    }
	    return JDE;
	  }
	  double Beleuchtung(double JDE){
		  double t, d, m, m1, i;
		  t = (JDE - 2451545) / 36525;
		  d = 297.8502042 + 445267.11151686 * t - .00163 * t * t + t * t * t / 545868 - t * t * t * t / 113065000;
		  m = 357.5291092 + 35999.0502909 * t - .0001536 * t * t + t * t * t / 24490000;
		  m1 = 134.9634114 + 477198.8676313 * t + .008997 * t * t + t * t * t / 69699 - t * t * t * t / 14712000;
		  i = 180 - d - 6.289 * SN(m1) + 2.1 * SN(m) - 1.274 * SN(2 * d - m1) - .658 * SN(2 * d) - .241 * SN(2 * m1) - .110 * SN(d);
		  return (1 + CS(i)) / 2 * 100;
	  }
	  double Deklination_Nord(double Modus){
	    double JDA = Java_JD(Heute);
	    double JDE = 0;
	    double Grad = 0;
	    double k = Math.floor((Dezimaljahr() - 2000.03) * 13.3686);
	    do{
	    	double t = k / 1336.86;
	    	double d = 152.2029 + 333.0705546 * k - .0004025 * t * t + .00000011 * t * t * t;
	    	double m = 14.8591 + 26.9281592 * k - .0000544 * t * t - .0000001 * t * t * t;
	    	double m1 = 4.6881 + 356.9562795 * k + .0103126 * t * t + .00001251 * t * t * t;
	    	double f = 325.8867 + 1.4467806 * k - .0020708 * t * t - .00000215 * t * t * t;
	    	double e = 1 - .002516 * t - .0000074 * t * t;
	    	JDE = 2451562.5897 + 27.321582241 * k + .000100695 * t * t - .000000141 * t * t * t;
	      JDE += .8975 * CS(f) - .4726 * SN(m1) - .103 * SN(2 * f) - .0976 * SN(2 * d - m1) - .0462 * CS(m1 - f) - .0461 * CS(m1 + f) - .0438 * SN(2 * d) + .0162 * SN(m) * e - .0157 * CS(3 * f) + .0145 * SN(m1 + 2 * f) + .0136 * CS(2 * d - f);
	      JDE += - .0095 * CS(2 * d - m1 - f) - .0091 * CS(2 * d - m1 + f) - .0089 * CS(2 * d + f) + .0075 * SN(2 * m1) - .0068 * SN(m1 - 2 * f) + .0061 * CS(2 * m1 - f) - .0047 * SN(m1 + 3 * f) - .0043 * SN(2 * d - m - m1) * e;
	      JDE += - .004 * CS(m1 - 2 * f)  - .0037 * SN(2 * d - 2 * m1) + .0031 * SN(f) + .003 * SN(2 * d + m1) - .0029 * CS(m1 + 2 * f) - .0029 * SN(2 * d - m) * e - .0027 * SN(m1 + f) + .0024 * SN(m - m1) * e - .0021 * SN(m1 - 3 * f);
	      JDE += .0019 * SN(2 * m1 + f) + .0018 * CS(2 * d - 2 * m1 - f) + .0018 * SN(3 * f) + .0017 * CS(m1 + 3 * f) + .0017 * CS(2 * m1) - .0014 * CS(2 * d - m1) + .0013 * CS(2 * d + m1 + f) + .0013 * CS(m1) + .0012 * SN(3 * m1 + f);
	      JDE += .0011 * SN(2 * d - m1 + f) - .0011 * CS(2 * d - 2 * m1) + .001 * CS(d + f) + .001 * SN(m + m1) * e - .0009 * SN(2 * d - 2 * f) + .0007 * CS(2 * m1 + f) - .0007 * CS(3 * m1 + f);
	      Grad = 23.6961 - .013004 * t + 5.1093 * SN(f) + .2658 * CS(2 * f) + .1448 * SN(2 * d - f) - .0322 * SN(3 * f) + .0133 * CS(2 * d - 2 * f) + .0125 * CS(2 * d) - .0124 * SN(m1 - f) - .0101 * SN(m1 + 2 * f) + .0097 * CS(f);
	      Grad += - .0087 * SN(2 * d + m - f) * e + .0074 * SN(m1 + 3 * f) + .0067 * SN(d + f) + .0063 * SN(m1 - 2 * f) + .006 * SN(2 * d - m - f) * e - .0057 * SN(2 * d - m1 - f) - .0056 * CS(m1 + f) + .0052 * CS(m1 + 2 * f) + .0041 * CS(2 * m1 + f);
	      Grad += - .004 * CS(m1 - 3 * f) + .0038 * CS(2 * m1 - f) - .0034 * CS(m1 - 2 * f) - .0029 * SN(2 * m1) + .0029 * SN(3 * m1 + f) - .0028 * CS(2 * d + m - f) * e - .0028 * CS(m1 - f) - .0023 * CS(3 * f) - .0021 * SN(2 * d + f) + .0019 * CS(m1 + 3 * f);
	      Grad += .0018 * CS(d + f) + .0017 * SN(2 * m1 - f) + .0015 * CS(3 * m1 + f) + .0014 * CS(2 * d + 2 * m1 + f) - .0012 * SN(2 * d - 2 * m1 - f) - .0012 * CS(2 * m1) - .001 * CS(m1) - .001 * SN(2 * f) + .0006 * SN(m1 + f);
	      k += 1;
	    }while (JDE < JDA);
	    if(Modus == 0) return JDE;
	    else return Grad;
	  }
	  double Deklination_Sued(double Modus)
	  {
	    double JDA = Java_JD(Heute);
	    double JDE, Grad;
	    double k = Math.floor((Dezimaljahr() - 2000.03) * 13.3686);
	    do{
	      double t = k / 1336.86;
	      double d = 345.6676 + 333.0705546 * k - .0004025 * t * t + .00000011 * t * t * t;
	      double m = 1.3951 + 26.9281592 * k - .0000544 * t * t - .0000001 * t * t * t;
	      double m1 = 186.21 + 356.9562795 * k + .0103126 * t * t + .00001251 * t * t * t;
	      double f = 145.1633 + 1.4467806 * k - .0020708 * t * t - .00000215 * t * t * t;
	      double e = 1 - .002516 * t - .0000074 * t * t;
	      JDE = 2451548.9289 + 27.321582241 * k + .000100695 * t * t - .000000141 * t * t * t;
	      JDE += -.8975 * CS(f) - .4726 * SN(m1) - .103 * SN(2 * f) - .0976 * SN(2 * d - m1) + .0541 * CS(m1 - f) + .0516 * CS(m1 + f) - .0438 * SN(2 * d) + .0112 * SN(m) * e + .0157 * CS(3 * f) + .0023 * SN(m1 + 2 * f) - .0136 * CS(2 * d - f);
	      JDE += .011 * CS(2 * d - m1 - f) + .0091 * CS(2 * d - m1 + f) + .0089 * CS(2 * d + f) + .0075 * SN(2 * m1) - .003 * SN(m1 - 2 * f) - .0061 * CS(2 * m1 - f) - .0047 * SN(m1 + 3 * f) - .0043 * SN(2 * d - m - m1) * e;
	      JDE += .004 * CS(m1 - 2 * f)  - .0037 * SN(2 * d - 2 * m1) - .0031 * SN(f) + .003 * SN(2 * d + m1) + .0029 * CS(m1 + 2 * f) - .0029 * SN(2 * d - m) * e - .0027 * SN(m1 + f) + .0024 * SN(m - m1) * e - .0021 * SN(m1 - 3 * f);
	      JDE += -.0019 * SN(2 * m1 + f) - .0006 * CS(2 * d - 2 * m1 - f) - .0018 * SN(3 * f) - .0017 * CS(m1 + 3 * f) + .0017 * CS(2 * m1) + .0014 * CS(2 * d - m1) - .0013 * CS(2 * d + m1 + f) - .0013 * CS(m1) + .0012 * SN(3 * m1 + f);
	      JDE += .0011 * SN(2 * d - m1 + f) + .0011 * CS(2 * d - 2 * m1) + .001 * CS(d + f) + .001 * SN(m + m1) * e - .0009 * SN(2 * d - 2 * f) - .0007 * CS(2 * m1 + f) - .0007 * CS(3 * m1 + f);
	      Grad = 23.6961 - .013004 * t - 5.1093 * SN(f) + .2658 * CS(2 * f) - .1448 * SN(2 * d - f) + .0322 * SN(3 * f) + .0133 * CS(2 * d - 2 * f) + .0125 * CS(2 * d) - .0015 * SN(m1 - f) + .0101 * SN(m1 + 2 * f) - .0097 * CS(f);
	      Grad += .0087 * SN(2 * d + m - f) * e + .0074 * SN(m1 + 3 * f) + .0067 * SN(d + f) - .0063 * SN(m1 - 2 * f) - .006 * SN(2 * d - m - f) * e + .0057 * SN(2 * d - m1 - f) - .0056 * CS(m1 + f) - .0052 * CS(m1 + 2 * f) - .0041 * CS(2 * m1 + f);
	      Grad += - .004 * CS(m1 - 3 * f) + .0038 * CS(2 * m1 - f) + .0034 * CS(m1 - 2 * f) - .0029 * SN(2 * m1) + .0029 * SN(3 * m1 + f) + .0028 * CS(2 * d + m - f) * e - .0028 * CS(m1 - f) + .0023 * CS(3 * f) + .0021 * SN(2 * d + f) + .0019 * CS(m1 + 3 * f);
	      Grad += .0018 * CS(d + f) - .0017 * SN(2 * m1 - f) + .0015 * CS(3 * m1 + f) + .0014 * CS(2 * d + 2 * m1 + f) + .0012 * SN(2 * d - 2 * m1 - f) - .0012 * CS(2 * m1) + .001 * CS(m1) - .001 * SN(2 * f) + .0037 * SN(m1 + f);
	      k += 1;
	    }
	    while (JDE < JDA);
	    if(Modus == 0) return JDE;
	    else return Grad;
	  }
	  double Mondknoten()
	  {
		  double k, d, m, m1, o, v, p, JDE;
		  double JDA = Java_JD(Heute);
		  k = Math.floor((Dezimaljahr()- 2000.05) * 13.4223);
	    do{
	      double t = k / 1342.23;
	      d = 183.638 + 331.73735691 * k + 0.0015057 * t * t + 0.00000209 * t * t * t - 0.00000001 * t * t * t * t;
	      double e = 1 - 0.002516 * t - 0.0000074 * t * t;
	      m = 17.4006 + 26.82037250 * k + 0.0000999 * t * t + 0.00000006 * t * t * t;
	      m1 = 38.3776 + 355.52747322 * k + 0.0123577 * t * t + 0.000014628 * t * t * t - 0.000000069 * t * t * t * t;
	      o = 123.9767 - 1.44098949 * k + 0.0020625 * t * t + 0.00000214 * t * t * t - 0.000000016 * t * t * t * t;
	      v = 299.75 + 132.85 * t - 0.009173 * t * t;
	      p = o + 272.75 - 2.3 * t;
	      JDE = 2451565.1619 + 27.212220817 * k + 0.0002572 * t * t + 0.000000021 * t * t * t - 0.000000000088 * t * t * t * t - .4721 * SN(m1) - 0.1649 * SN(2 * d) - .0868 * SN(2 * d - m1) + 0.0084 * SN(2 * d + m1) - 0.0083 * SN(2 * d - m) * e;
	      JDE += - 0.0039 * SN(2 * d - m - m1) * e + 0.0034 * SN(2 * m1) - 0.0031 * SN(2 * d - 2 * m1) + 0.003 * SN(2 * d + m) * e + 0.0028 * SN(m - m1) * e + 0.0026 * SN(m) + 0.0025 * SN(4 * d) + 0.0024 * SN(d) + 0.0022 * SN(m + m1) * e;
	      JDE += 0.0017 * SN(o) + 0.0014 * SN(4 * d - m1) + 0.0005 * SN(2 * d + m - m1) * e + 0.0004 * SN(2 * d - m + m1) * e - 0.0003 * SN(2 * d - 2 * m) * e + 0.0003 * SN(4 * d - m) * e + 0.0003 * SN(v) + 0.0003 * SN(p);
	      k += .5;
	    }
	    while (JDE < JDA);
	    if(k - Math.floor(k) == 0) JDE = JDE * -1; //absteigend
	    return JDE;
	  }
	  double Apogaeum(){
		  double  k, d, m, f, JDE;
		  double  JDA = Java_JD(Heute);
	    k = Math.floor((Dezimaljahr() - 1999.97) * 13.2555) + .5;
	    do{
	    	double t = k / 1325.55;
	      d = 171.9179 + 335.9106046 * k - .010025 * t * t - .00001156 * t * t * t + .000000055 * t * t * t * t;
	      m = 347.3477 + 27.1577721 * k - .0008323 * t * t - .000001 * t * t * t;
	      f = 316.6109 + 364.5287911 * k - .0125131 * t * t - .0000148 * t * t * t;
	      JDE = 2451534.6698 + 27.55454988 * k - .0006886 * t * t - .000001098 * t * t * t + .0000000052 * t * t + .4392 * SN(2 * d) + .0684 * SN(4 * d) + (.0456 - .00011 * t) * SN(m) + (.0426 - .00011 * t) * SN(2 * d - m) + .0212 * SN(2 * f);
	      JDE += - .0189 * SN(d) + .0144 * SN(6 * d) + .0113 * SN(4 * d - m) + .0047 * SN(2 * d + 2 * f) + .0036 * SN(d + m) + .0035 * SN(8 * d) + .0034 * SN(6 * d - m) - .0034 * SN(2 * d - 2 * f) + .0022 * SN(2 * d - 2 * m) - .0017 * SN(3 * d);
	      JDE += .0013 * SN(4 * d + 2 * f) + .0011 * SN(8 * d - m) + .001 * SN(4 * d - 2 * m) + .0009 * SN(10 * d) + .0007 * SN(3 * d + m) + .0006 * SN(2 * m) + .0005 * SN(2 * d + m) + .0005 * SN(2 * d + 2 * m) + .0004 * SN(6 * d + 2 * f);
	      JDE += .0004 * SN(6 * d - 2 * m) + .0004 * SN(10 * d - m) - .0004 * SN(5 * d) - .0004 * SN(4 * d - 2 * f) + .0003 * SN(2 * f + m) + .0003 * SN(12 * d) + .0003 * SN(2 * d + 2 * f - m) - .0003 * SN(d - m);
	      k += 1;
	    }
	    while(JDE < JDA);
	    return JDE;
	  }
	  double Perigaeum(){
		  double k, t, d, m, f, JDE;
	    double  JDA = Java_JD(Heute);
	    k = Math.floor((Dezimaljahr() - 1999.97) * 13.2555);
	    do{
	      t = k / 1325.55;
	      d = 171.9179 + 335.9106046 * k - .010025 * t * t - .00001156 * t * t * t + .000000055 * t * t * t * t;
	      m = 347.3477 + 27.1577721 * k - .0008323 * t * t - .000001 * t * t * t;
	      f = 316.6109 + 364.5287911 * k - .0125131 * t * t - .0000148 * t * t * t;
	      JDE = 2451534.6698 + 27.55454988 * k - .0006886 * t * t - .000001098 * t * t * t + .0000000052 * t * t - 1.6769 * SN(2 * d) + .4589 * SN(4 * d) - .1856 * SN(6 * d) + .0883 * SN(8 * d);
	      JDE += - (.0773 + .00019 * t) * SN(2 * d - m) + (.0502 - .00013 * t) * SN(m) - .046 * SN(10 * d) + (.0422 - .00011 * t) * SN(4 * d - m) - .0256 * SN(6 * d - m) + .0253 * SN(12 * d) + .0237 * SN(d);
	      JDE += .0162 * SN(8 * d - m) - .0145 * SN(14 * d) + .0129 * SN(2 * f) - .0112 * SN(3 * d) - .0104 * SN(10 * d - m) + .0086 * SN(16 * d) + .0069 * SN(12 * d - m) + .0066 * SN(5 * d) - .0053 * SN(2 * d + 2 * f);
	      JDE += - .0052 * SN(18 * d) - .0046 * SN(14 * d - m) - .0041 * SN(7 * d) + .004 * SN(2 * d + m) + .0032 * SN(20 * d) - .0032 * SN(d + m) + .0031 * SN(16 * d - m);
	      JDE += - .0029 * SN(4 * d + m) - .0027 * SN(2 * d - 2 * m) + .0024 * SN(4 * d - 2 * m) - .0021 * SN(6 * d - 2 * m) - .0021 * SN(22 * d) - .0021 * SN(18 * d - m);
	      JDE += .0019 * SN(6 * d + m) - .0018 * SN(11 * d) - .0014 * SN(8 * d + m) - .0014 * SN(4 * d - 2 * f) - .0014 * SN(6 * d - 2 * f) + .0014 * SN(3 * d + m) - .0014 * SN(5 * d + m) + .0013 * SN(13 * d);
	      JDE += .0013 * SN(20 * d - m) + .0011 * SN(3 * d + 2 * m) - .0011 * SN(4 * d + 2 * f - 2 * m) - .001 * SN(d + 2 * m) - .0009 * SN(22 * d - m) - .0008 * SN(4 * f) + .0008 * SN(6 * d - 2 * f) + .0008 * SN(2 * d - 2 * f + m);
	      JDE += .0007 * SN(2 * m) + .0007 * SN(2 * f - m) + .0007 * SN(2 * d + 4 * f) - .0006 * SN(2 * f - 2 * m) - .0006 * SN(2 * d - 2 * f + 2 * m) + .0006 * SN(24 * d) + .0005 * SN(4 * d - 4 * f) + .0005 * SN(2 * d + 2 * m) - .0004 * SN(d - m) + .0027 * SN(9 * d) + .0027 * SN(4 * d + 2 * f);
	      k += 1;
	   }
	    while(JDE < JDA);
	    return JDE;
	  }
	  void Ausgabe_des_Mondknotens()
	  {
		  String Minuten, AufAb, GradS;
		  int test, t;
		  Calendar Zeit = at.gepa.lib.tools.time.TimeTool.getInstance();
		  double [] JDE_dek = new double [3];
		  double [] Text_dek = new double [3];

		  //max. nördliche Deklination
		  JDE_dek[1] = Deklination_Nord(0);
		  String GradN = Gradumrechnung(Deklination_Nord(1));
		  
		  setJulianTime(Zeit, JD_Java(JDE_dek[1]));
		  
		  Minuten = (Zeit.get(Calendar.MINUTE) < 10) ? "0" + Zeit.get(Calendar.MINUTE) : ""+Zeit.get(Calendar.MINUTE);
		  //System.out.println("Die nächste nördliche Deklination " + In_Tagen(JDE_dek[1]) + " " + Zeit.getDate() + "." + (Zeit.getMonth() + 1) + "." + Zeit.getFullYear() + " um<\/td><td align=\"right\">" + Zeit.getHours() + "." + Minuten + " Uhr,<\/td><td> bei " + GradN + "<\/td><\/tr>";

	    //Knotendurchgang
	    JDE_dek[2] = Mondknoten();
	    AufAb = (JDE_dek[2] < 1) ? "absteigend" : "aufsteigend";
	    JDE_dek[2] = Math.abs(JDE_dek[2]);
	    setJulianTime(Zeit, JD_Java(JDE_dek[2]));
	    
//	    (Zeit.getMinutes() < 10) ? Minuten = "0" + Zeit.getMinutes() : Minuten = Zeit.getMinutes();
//	    Text_dek[2] = "<tr><td>Der nächste Knotendurchgang " + In_Tagen(JDE_dek[2]) + " <td align=\"right\">" + Zeit.getDate() + "." + (Zeit.getMonth() + 1) + "." + Zeit.getFullYear() + " um<\/td><td align=\"right\">" + Zeit.getHours() + "." + Minuten + " Uhr, <\/td><td>" + AufAb + "<\/td><\/tr>";

	    //max. düdliche Deklination
	    JDE_dek[3] = Deklination_Sued(0);
	    GradS = Gradumrechnung(Deklination_Sued(1));
	    setJulianTime(Zeit, JD_Java(JDE_dek[3]));
//	    Zeit.setTime(JD_Java(JDE_dek[3]));
//	    (Zeit.getMinutes() < 10) ? Minuten = "0" + Zeit.getMinutes() : Minuten = Zeit.getMinutes();
//	    Text_dek[3] = "<tr><td>Die nächste südliche Deklination " + In_Tagen(JDE_dek[3]) + " <td align=\"right\">" + Zeit.getDate() + "." + (Zeit.getMonth() + 1) + "." + Zeit.getFullYear() + " um<\/td><td align=\"right\">" + Zeit.getHours() + "." + Minuten + " Uhr,<\/td><td> bei " + GradS + "<\/td><\/tr>";


	    do{
	      test = 0;
	      for(t = 1; t < 3; t++)
	      {
	        if(JDE_dek[t] > JDE_dek[t + 1]){
	          JDE_dek[0] = JDE_dek[t];
	          JDE_dek[t] = JDE_dek[t + 1];
	          JDE_dek[t + 1] = JDE_dek[0];
	          Text_dek[0] = Text_dek[t];
	          Text_dek[t] = Text_dek[t + 1];
	          Text_dek[t + 1] = Text_dek[0];
	          test = 1;
	        }
	      }
	    }
	    while(test == 1);

	    System.out.println(Text_dek[1]);
	    System.out.println(Text_dek[2]);
	    System.out.println(Text_dek[3]);
	    // Tabellenanfang
//	    document.write("<h2 align=\"center\">Maximale Deklination und Knotendurchgang<\/h2>");
//	    document.write("<table summary=\"Rahmen\" align=\"center\" border=\"1\" bgcolor=\"#ffffff\"><tr><td>");
//	    document.write("<table summary=\"Maximale Deklination und Knotendurchgang\" border=\"0\">");
//
//
//	    document.write(Text_dek[1]);
//	    document.write(Text_dek[2]);
//	    document.write(Text_dek[3]);
//
//	     //Tabellenende
//	    document.write("<\/table>");
//	    document.write("<\/td><\/tr><\/table>");
	  }
	  void Ausgabe_der_Mondentfernung(){
	    String Minuten;
	    Calendar Zeit = at.gepa.lib.tools.time.TimeTool.getInstance();
	    // Tabellenanfang
//	    document.write("<h2 align=\"center\">Perigäum und Apogäum<\/h2>");
//	    document.write("<table summary=\"Rahmen\" align=\"center\" border=\"1\" bgcolor=\"#ffffff\"><tr><td>");
//	    document.write("<table summary=\"Perigäum und Apogäum\" border=\"0\">");
	    //Perigäum
	    double JDP = Perigaeum();
	    setJulianTime(Zeit, JD_Java(JDP));
	    double km = Math.round(Entfernung(JDP));
//	    var Peri = "<tr><td>Die nächste Mondnähe<\/td><td>" + In_Tagen(JDP) +" <td align=\"right\">" + Zeit.getDate() + "." + (Zeit.getMonth() + 1) + "." + Zeit.getFullYear() + " um<\/td><td align=\"right\">" + Zeit.getHours() + "." + Minuten + " Uhr<\/td><td>bei " + km + " km<\/td><\/tr>";
	    //Apogäum
	    double JDA = Apogaeum();
	    setJulianTime(Zeit, JD_Java(JDA));
	    km = Math.round(Entfernung(JDA));
	    //Minuten = (Zeit.getMinutes() < 10) ? "0" + Zeit.getMinutes() : Zeit.getMinutes();
	    //var Apog = "<tr><td>Die nächste Mondferne<\/td><td>" + In_Tagen(JDA) +" <td align=\"right\">" + Zeit.getDate() + "." + (Zeit.getMonth() + 1) + "." + Zeit.getFullYear() + " um<\/td><td align=\"right\">" + Zeit.getHours() + "." + Minuten + " Uhr<\/td><td>bei " + km + " km<\/td><\/tr>";
	    //Sortierung
//	    if(JDP < JDA)
//	      document.write(Peri + Apog);
//	    else
//	      document.write(Apog + Peri);
//	     //Tabellenende
//	    document.write("<\/table>");
//	    document.write("<\/td><\/tr><\/table>");
	  }
	  double Dezimaljahr(){
		  double Gesamt = 365;
		  double Summe = 0;
		  int [] Monatstage = new int []{31,28,31,30,31,30,31,31,30,31,30,31};
		  int Jahr = Heute.get(Calendar.YEAR);
		  int Monat = Heute.get(Calendar.MONTH);
		  int Tag = Heute.get(Calendar.DAY_OF_MONTH);
		  if(Jahr % 4 == 0)
		  {
			  Monatstage[1] = 29;
			  Gesamt = 366;
		  }
		  for (int t = 0; t < Monat ; t++ ) {
			  Summe += Monatstage[t];
		  }
	    return Jahr + (Summe + Tag) / Gesamt;
	  }
	  double NaechsterVM(double zeit){
		  double  tz = 0, k;
	    do{
	      k = Var_k(tz);
	      tz += 1;
	    }
	    while(Vollmond(k) < zeit);
	    return Vollmond(k);
	  }
	  double NaechstesLV(double zeit){
		  double  tz = 0, k;
	    do{
	      k = Var_k(tz);
	      tz += 1;
	    }
	    while(Viertel(k, .75) < zeit);
	    return Viertel(k, .75);
	  }
	  double NaechsterNM(double zeit){
		  double  tz = 0, k;
	    do{
	      k = Var_k(tz);
	      tz += 1;
	    }
	    while(Neumond(k) < zeit);
	    return Neumond(k);
	  }
	  double NaechstesEV(double zeit){
		  double  tz = 0, k;
	    do{
	      k = Var_k(tz);
	      tz += 1;
	    }
	    while(Viertel(k, .25) < zeit);
	    return Viertel(k, .25);
	  }
	  double NaechsteMF(double zeit, double Typ){
		  double tz = 0, k;
	    do{
	      k = Var_k(tz);
	      tz += 1;
	    }
	    while(Finsternis(k, .5, Typ) < zeit);
	    return Finsternis(k, .5, Typ);
	  }
	  double  NaechsteSF(double zeit, double Typ){
		  double tz = 0, k;
	    do{
	      k = Var_k(tz);
	      tz += 1;
	    }
	    while(Finsternis(k, 0, Typ) < zeit);
	    return Finsternis(k, 0, Typ);
	  }
	  double  Entfernung(double JD){
		  double t, d, m, m1, f, sr;
	    t = (JD - 2451545) / 36525;
	    d = 297.8502042 + 445267.11151686 * t - .00163 * t * t + t * t * t / 545868 - t * t * t * t / 113065000;
	    m = 357.5291092 + 35999.0502909 * t - .0001536 * t * t + t * t * t / 24490000;
	    m1 = 134.9634114 + 477198.8676313 * t + .008997 * t * t + t * t * t / 69699 - t * t * t * t / 14712000;
	    f = 93.27209929999999 + 483202.0175273 * t - .0034029 * t * t - t * t * t / 3526000 + t * t * t * t / 863310000;
	    sr = 385000.56 + Koeffizient(d, m, m1, f) / 1000;
	    return sr;
	  }
	  double Koeffizient(double d, double m, double m1, double f){
		  double sr = 0;
		  int t;
		  int [] kd = new int []{0,2,2,0,0,0,2,2,2,2,0,1,0,2,0,0,4,0,4,2,2,1,1,2,2,4,2,0,2,2,1,2,0,0,2,2,2,4,0,3,2,4,0,2,2,2,4,0,4,1,2,0,1,3,4,2,0,1,2,2};
		  int [] km = new int[] {0,0,0,0,1,0,0,-1,0,-1,1,0,1,0,0,0,0,0,0,1,1,0,1,-1,0,0,0,1,0,-1,0,-2,1,2,-2,0,0,-1,0,0,1,-1,2,2,1,-1,0,0,-1,0,1,0,1,0,0,-1,2,1,0,0};
		  int [] km1 = new int []{1,-1,0,2,0,0,-2,-1,1,0,-1,0,1,0,1,1,-1,3,-2,-1,0,-1,0,1,2,0,-3,-2,-1,-2,1,0,2,0,-1,1,0,-1,2,-1,1,-2,-1,-1,-2,0,1,4,0,-2,0,2,1,-2,-3,2,1,-1,3,-1};
		  int [] kf = new int [] {0,0,0,0,0,2,0,0,0,0,0,0,0,-2,2,-2,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,-2,2,0,2,0,0,0,0,0,0,-2,0,0,0,0,-2,-2,0,0,0,0,0,0,0,-2};
		  int [] kr = new int [] {-20905355,-3699111,-2955968,-569925,48888,-3149,246158,-152138,-170733,-204586,-129620,108743,104755,10321,0,79661,-34782,-23210,-21636,24208,30824,-8379,-16675,-12831,-10445,-11650,14403,-7003,0,10056,6322,-9884,5751,0,-4950,4130,0,-3958,0,3258,2616,-1897,-2117,2354,0,0,-1423,-1117,-1571,-1739,0,-4421,0,0,0,0,1165,0,0,8752};
		  for (t = 0; t < 60; t++)
		  {
			  sr += kr[t] * CS(kd[t] * d + km[t] * m + km1[t] * m1 + kf[t] * f);
		  }
		  return sr;
	  }
	  String Ausgabe_der_Mondphasen(String Text, double JDE)
	  {
		  String Minuten, SText;
		  Calendar julianDate = at.gepa.lib.tools.time.TimeTool.getInstance();
		  setJulianTime(julianDate, JD_Java(JDE));
		  Minuten = (julianDate.get(Calendar.MINUTE) < 10) ? "0" + julianDate.get(Calendar.MINUTE) : ""+ julianDate.get(Calendar.MINUTE);
		  return Text + In_Tagen(JDE)+ " - " + TimeTool.toDateTimeString(Heute.getTime()); 
//				  Heute.getDate() + "." + (Heute.getMonth() + 1) + "." + Heute.getFullYear() + " um<\/td><td align=\"right\">" + Heute.getHours() + "." + Minuten + " Uhr<\/td><\/tr>");
	  }

	  private void setJulianTime(Calendar julianDate, double jd_Java) {
		  String dateStr = "" + jd_Java;
		  julianDate.set(Integer.parseInt(dateStr.substring(0,3)), 1, 1, 0, 0, 0);
		  julianDate.set(Calendar.YEAR,Integer.parseInt(dateStr.substring(0,3)));
		  julianDate.set(Calendar.DAY_OF_YEAR,Integer.parseInt(dateStr.substring(4)));		
	}

	void NaechsteMondphasen(){
		double [] JDE_mondphase = new double[4];
		double [] JDE_finsternis = new double [5];
	    String [] Text_mondphase = new String[]{" ","Nächster Vollmond","Das nächste Letzte Viertel","Nächster Neumond","Das nächste Erste Viertel"};
	    String [] Text_finsternis = new String [] {" ","Die nächste partielle Mondfinsternis","Die nächste totale Mondfinsternis","Die nächste partielle Sonnenfinsternis","Die nächste totale Sonnenfinsternis","Die nächste ringförmige Sonnenfinsternis"};
	    int test, t;
	    Calendar Heute = at.gepa.lib.tools.time.TimeTool.getInstance();
	    double zeit =  Java_JD(Heute);

	    JDE_mondphase[0] = NaechsterVM(zeit);
	    JDE_mondphase[1] = NaechstesLV(zeit);
	    JDE_mondphase[2] = NaechsterNM(zeit);
	    JDE_mondphase[3] = NaechstesEV(zeit);
	    JDE_finsternis[0] = NaechsteMF(zeit, 0); //partiell
	    JDE_finsternis[1] = NaechsteMF(zeit, 1); //total
	    JDE_finsternis[2] = NaechsteSF(zeit, 0); //partiell
	    JDE_finsternis[3] = NaechsteSF(zeit, 1); //total
	    JDE_finsternis[4] = NaechsteSF(zeit, 2); //ring

	   do{
	      test = 0;
	      for(t = 1; t < 4; t++){
	        if(JDE_mondphase[t] > JDE_mondphase[t + 1]){
	          JDE_mondphase[0] = JDE_mondphase[t];
	          JDE_mondphase[t] = JDE_mondphase[t + 1];
	          JDE_mondphase[t + 1] = JDE_mondphase[0];
	          Text_mondphase[0] = Text_mondphase[t];
	          Text_mondphase[t] = Text_mondphase[t + 1];
	          Text_mondphase[t + 1] = Text_mondphase[0];
	          test = 1;
	        }
	      }
	    }
	    while(test == 1);

	     do{
	      test = 0;
	      for(t = 1; t < 5; t++){
	        if(JDE_finsternis[t] > JDE_finsternis[t + 1]){
	          JDE_finsternis[0] = JDE_finsternis[t];
	          JDE_finsternis[t] = JDE_finsternis[t + 1];
	          JDE_finsternis[t + 1] = JDE_finsternis[0];
	          Text_finsternis[0] = Text_finsternis[t];
	          Text_finsternis[t] = Text_finsternis[t + 1];
	          Text_finsternis[t + 1] = Text_finsternis[0];
	          test = 1;
	        }
	      }
	    }while(test > 0);
//	    document.write("<h2 align=\"center\">Die nächsten Mondphasen<\/h2>");
//	    document.write("<table summary=\"Rahmen\" align=\"center\" border=\"1\" bgcolor=\"#ffffff\"><tr><td>");
//	    document.write("<table summary=\"Mondphasen\" border=\"0\">");
	    for(t = 1; t < 5; t++){
	      Ausgabe_der_Mondphasen(Text_mondphase[t], JDE_mondphase[t]);
	    }
//	    document.write("<\/table>");
//	    document.write("<\/td><\/tr><\/table>");
//
//	    document.write("<h2 align=\"center\">Die nächsten Finsternisse<\/h2>");
//	    document.write("<table summary=\"Rahmen\" align=\"center\" border=\"1\" bgcolor=\"#ffffff\"><tr><td>");
//	    document.write("<table summary=\"Finsternisse\" border=\"0\">");
	    for(t = 1; t < 6; t++){
	      Ausgabe_der_Mondphasen(Text_finsternis[t], JDE_finsternis[t]);
	    }
//	    document.write("<\/table>");
//	    document.write("<\/td><\/tr><\/table>");
	  }
	  int Mondtag(){
	    //Bezug ist der Neumond am 6.1.2001
	    int Bild = 0;
	    int Bildmenge = 32; // Die Menge der einzelnen Mondbilder
	    double bk = 29.53059 / Bildmenge / 5 ;// Ein Fünftel der Bilder-Schrittweite, benötigt zur rechtzeitigen Anzeige der Viertel
	    double k = Var_k(0);
	    Calendar Heute = at.gepa.lib.tools.time.TimeTool.getInstance();
	    double JD = Java_JD(Heute);

	    double neumond = (Neumond(k) - JD);
	    double erstesviertel = (Viertel(k, .25) - JD);
	    double letztesviertel = (Viertel(k, .75) - JD);
	    double vollmond = (Vollmond(k) - JD);

	    BeleuchtungZeile(JD);
	    EntfernungZeile(JD);

	    if(Math.abs(neumond) < Math.abs(erstesviertel) && Math.abs(neumond) < Math.abs(letztesviertel) && Math.abs(neumond) < Math.abs(vollmond))
	    {
	      //Bild = 1 - Math.round(neumond * Bildmenge / 29.53059);
//	      if(neumond < bk && Bild > Math.round(Bildmenge / 4 * 3 + 1)) 
//	    	  Bild = 1; // Damit das Bild auf jeden Fall rechtzeitg gezeigt wird.
//	      if(Bild < 1) Bild += Bildmenge;
//	      (neumond > 0) ? document.mond.text.value = "Nächster Neumond ist in:" : document.mond.text.value = "Letzter Neumond war vor:";
	      ZeitZeile(Math.abs(neumond));
	    }
	    if(Math.abs(erstesviertel) < Math.abs(neumond) && Math.abs(erstesviertel) < Math.abs(letztesviertel) && Math.abs(erstesviertel) < Math.abs(vollmond))
	    {
	      //Bild = Math.round(Bildmenge / 4 + 1 - erstesviertel * Bildmenge / </table9);
//	      if(erstesviertel < bk && Bild < Math.round(Bildmenge / 4 + 1)) Bild =  Math.round(Bildmenge / 4 + 1);
//	      (erstesviertel > 0) ? document.mond.text.value = "Das nächste Erste Viertel ist in:" : document.mond.text.value = "Das letzte Erste Viertel war vor:";
	      ZeitZeile(Math.abs(erstesviertel));
	    }
	    if(Math.abs(vollmond) < Math.abs(neumond) && Math.abs(vollmond) < Math.abs(letztesviertel) && Math.abs(vollmond) < Math.abs(erstesviertel)){
	      //Bild = Math.round(Bildmenge / 2 + 1 - vollmond * Bildmenge / 29.53059);
	      //if(vollmond < bk && Bild < Math.round(Bildmenge / 2 + 1)) Bild =  Math.round(Bildmenge / 2 + 1);
	      //(vollmond > 0) ? document.mond.text.value = "Nächster Vollmond ist in:" : document.mond.text.value = "Letzter Vollmond war vor:";
	      ZeitZeile(Math.abs(vollmond));
	    }
	    if(Math.abs(letztesviertel) < Math.abs(neumond) && Math.abs(letztesviertel) < Math.abs(vollmond) && Math.abs(letztesviertel) < Math.abs(erstesviertel)){
	      //Bild = Bildmenge - Math.round(neumond * Bildmenge / 29.53059);
	      //if(Bild > Bildmenge) Bild += - Bildmenge;
	      //if(letztesviertel < bk && Bild < Math.round(Bildmenge / 4 * 3 + 1)) Bild =  Math.round(Bildmenge / 4 * 3 + 1);
	      //(letztesviertel > 0) ? document.mond.text.value = "Das nächste Letzte Viertel ist in:" : document.mond.text.value =  "Das letzte Letzte Viertel war vor:";
	      ZeitZeile(Math.abs(letztesviertel));
	    }

	    //window.setTimeout('Mondtag()',1000);
	    return Bild;
	  }
	  void MondAusgabe(){
		  System.out.println("Mondausgabe: " + Mondtag() );
//	     document.write("<table summary=\"Rahmen\" align=\"center\" border=\"1\"><tr><td>");
//	     document.write("<img src=\"bild/" + Mondtag() + ".png \" width=\"386\" height=\"386\" alt=\"Die aktuelle Mondphase berechnet mit JavaScript\" border=\"0\">");
//	     document.write("<\/td><\/tr><\/table>");
	  }
	  double JD_Stunde(double JDE){
	    return Math.floor((24 * (JDE - Math.floor(JDE))));
	  }
	  double JD_Minute(double JDE){
	    return Math.floor((JDE - Math.floor(JDE)) * 1440 - JD_Stunde(JDE) * 60);
	  }
	  double JD_Sekunde(double JDE){
	    return Math.floor((JDE - Math.floor(JDE)) * 86400 - JD_Stunde(JDE) * 3600 - JD_Minute(JDE) * 60);
	  }
	  void ZeitZeile(double JDE){
		  String x = "";
	    if(Math.floor(JDE) != 1){
	      x = Math.floor(JDE) + " Tagen";
	    }
	    else{
	      x = Math.floor(JDE) + " Tag";
	    }
	    x += JD_Stunde(JDE) + " Std.";
	    x += JD_Minute(JDE) + " Min.";
	    x += JD_Sekunde(JDE) + " Sek.";
	    System.out.println(x);
	  }
	  void BeleuchtungZeile(double zeit){
	    System.out.println( "Beleuchteter Teil der Mondscheibe:" );
	    System.out.println( Math.round(Beleuchtung(zeit) * 10000) / 10000 + "%" );
	   }
	  void EntfernungZeile(double zeit){
		  System.out.println( "Entfernung der Mittelpunkte:" );
		  System.out.println( Math.round(Entfernung(zeit) * 10) / 10 + " km" );
	    // document.mond.ent.value = Math.round(Entfernung(zeit) * 10) / 10;
	  }
	  String [] AufUntergang(int Modus, double lat, double lon){
	    //Modus% 1 = Mond, 2 = Sonne, 3 = Dämmerung bürgerlich, 4 = Dämmerung nautisch wird hier nicht verwendet

//	    Calendar Heute = at.gepa.lib.tools.time.TimeTool.getInstance(); 
//	    Heute.set(Calendar.HOUR_OF_DAY, 12);
//	    Heute.set(Calendar.MINUTE, 0);
	    double lambda = lon; 
//	    		document.koordinaten.laengengrad.value;   //Längengrad  -Ost/West
//	    if(document.koordinaten.laenge[0].checked == true){
//	      lambda = lambda * -1;
//	    }
	    double phi = lat; 
	    		//document.koordinaten.breitengrad.value;    //Breitengrad Nord/-Süd
//	    if(document.koordinaten.breite[1].checked == true){
//	      phi = phi * -1;
//	    }
	    double MJD = Math.floor(Java_JD(Heute)) - 2400000;
	    MJD += (Heute.get(Calendar.ZONE_OFFSET) + Heute.get(Calendar.DST_OFFSET)) / (3600 * 1000); 
//	    if(document.koordinaten.zeitzone.value == "auto"){
//	       MJD += Heute.getTimezoneOffset() / 1440;
//	    }
//	    else{
//	      var Zone = document.koordinaten.zeitzone.value;
//	      if(document.koordinaten.zone[0].checked == true){
//	      Zone = Zone * -1;
//	      }
//	      MJD += Zone / 24;
//	    }
	    double sphi = SN(phi);
	    double cphi = CS(phi);
	    String Auf;
	    String Unter;
	    double sinho = 0;
	    switch(Modus){
	      case 1:
	        sinho = -SN(8.0 / 60.0) * 100;  //Mond
	        sinho = -0.2327103568950269;
	        sinho = -0.1999903568950269;
	    	//sinho = -0.1902;//0.2327;//103568950269;
	    	System.out.println(sinho);
	        break;
	      case 2:
	        sinho = SN(-50 / 60); //Sonne
	        break;
	      case 3:
	        sinho = SN(-6.7);  //Dämmerung bürgerlich (-6)
	        break;
	      case 4:
	        sinho = SN(-12);   //Dämmerung nautische (-12))
	        break;
	    }
	    double [] RiseSet = AufUnterBerechnung(Modus, MJD, lambda, phi, sphi, cphi, sinho); //return [utrise, utset, above];
	    double AMinute = (RiseSet[0] - Math.floor(RiseSet[0])) * 60 / 100;
	    if(AMinute >= .595){
	      AMinute = 0;
	      RiseSet[0] += 1;
	    }
	    double Aufgang = Math.floor(RiseSet[0]) + AMinute;
	    if(Aufgang != 0){
	      Auf = ""+TimeTool.toTimeString(Aufgang);
	    }
	    else{
	      Auf = "--:-- ";
	    }
	    double UMinute = (RiseSet[1] - Math.floor(RiseSet[1])) * 60 / 100;
	    if(UMinute >= .595){
	      UMinute = 0;
	      RiseSet[1] += 1;
	    }
	    double Untergang = Math.floor(RiseSet[1]) + UMinute;
	    
	    if(Untergang != 0){
	      Unter = TimeTool.toTimeString(Untergang);
	     }
	    else{
	      Unter = "--:-- ";
	    }
	    if(Aufgang == 0 && Untergang == 0){
	      if(Modus == 3){
	        if(RiseSet[2] == 1){
	          Auf = "immer";
	          Unter = " hell";
	        }
	        else{
	          Auf = "immer";
	          Unter = " dunkel";
	        }
	      }
	      else{
	        Unter = " sichtbar";
	        if(RiseSet[2] == 1){
	          Auf = "immer";
	        }
	        else{
	          Auf = "nicht";
	        }
	      }
	    }
	    return new String []{Auf, Unter};
	   }
	  double [] AufUnterBerechnung(int Modus, double MJD, double lambda, double phi, double sphi, double cphi, double sinho){
	    /*
	    Modus=1 = Mond Modus>1 = Sonne
	    utrise = Aufgang Mod. Scalicer Dez.
	    utset = Untergang Mod. Scalicer Dez.
	    above=1 = immer sichtbar, above=0 immer dunkel
	    */
		  double Hour = 1;
		  double yminus, yo, yplus;
		  double [] QuatRet = new double[3];
		  double utrise = 0, utset = 0, above;
		  
	    do {
	      yminus = SINALT(Modus, MJD, Hour - 1, lambda, cphi, sphi) - sinho;
	      if(yminus > 0){
	        above = 1;
	      }
	      else{
	        above = 0;
	      }
	      yo = SINALT(Modus, MJD, Hour, lambda, cphi, sphi) - sinho;
	      yplus = SINALT(Modus, MJD, Hour + 1, lambda, cphi, sphi) - sinho;
	      QuatRet = QUAD(yminus, yo, yplus); // QuatRet 0 = ye, 1 = zero1, 2 = zero2, 3 = nz
	      if(QuatRet[3] == 1){
	        if(yminus < 0){
	          utrise = Hour + QuatRet[1];
	        }
	        else{
	          utset = Hour + QuatRet[1];
	        }
	      }
	      if(QuatRet[3] == 2){
	        if(QuatRet[0] < 0){
	          utrise = Hour + QuatRet[2];
	          utset = Hour + QuatRet[1];
	        }
	        else{
	          utrise = Hour + QuatRet[1];
	          utset = Hour + QuatRet[2];
	        }
	      }
	      yminus = yplus;
	      Hour = Hour + 1;//2;

	   } while(Hour < 25 && (utrise == 0 || utset == 0));
	   return new double []{utrise, utset, above};
	}
	  double SINALT(int Modus, double MJD, double Hour, double lambda, double cphi, double sphi){
		  double MJDO = MJD + Hour / 24;
		  double t = (MJDO - 51544.5) / 36525;
	    double[] decra;
		if(Modus == 1){
	      decra = Mondberechnung(t);
	    }
	    else{
	      decra = Sonnenberechnung(t);
	    }
	    double tau = 15 * (LMST(MJDO, lambda) - decra[1]);
	    return sphi * SN(decra[0]) + cphi * CS(decra[0]) * CS(tau);
	  }
	  double LMST(double MJD, double lambda){
		  double MJDO = Math.floor(MJD);
		  double ut = (MJD - MJDO) * 24;
		  double t = (MJDO - 51544.5) / 36525;
		  double gmst = 6.697374558 + 1.0027379093 * ut + (8640184.812866 + (.093104 - .0000062 * t) * t) * t / 3600;
		  return 24 * FRAK((gmst - lambda / 15) / 24);
	  }
	  double FRAK(double x){
	    x = x - (int)(x);
	    if(x < 0)
	      x += 1;
	    return x;
	  }
	  double [] Mondberechnung(double t){
		  double p2 = 6.283185307;
		  double arc = 206264.8062;
		  double coseps = .91748;
		  double sineps = .39778;
		  double lo = FRAK(.606433 + 1336.855225 * t);
		  double l = p2 * FRAK(.374897 + 1325.55241 * t);
		  double ls = p2 * FRAK(.993133 + 99.997361 * t);
		  double d = p2 * FRAK(.827361 + 1236.853086 * t);
		  double f = p2 * FRAK(.259086 + 1342.227825 * t);
		  double dl = 22640 * Math.sin(l) - 4586 * Math.sin(l - 2 * d) + 2370 * Math.sin(2 * d) + 769 * Math.sin(2 * l) - 668 * Math.sin(ls) - 412 * Math.sin(2 * f) - 212 * Math.sin(2 * l - 2 * d) - 206 * Math.sin(l + ls - 2 * d) + 192 * Math.sin(l + 2 * d) - 165 * Math.sin(ls - 2 * d) - 125 * Math.sin(d) - 110 * Math.sin(l + ls) + 148 * Math.sin(l - ls) - 55 * Math.sin(2 * f - 2 * d);
		  double s = f + (dl + 412 * Math.sin(2 * f) + 541 * Math.sin(ls)) / arc;
		  double h = f - 2 * d;
		  double n = -526 * Math.sin(h) + 44 * Math.sin(l + h) - 31 * Math.sin(-l + h) - 23 * Math.sin(ls + h) + 11 * Math.sin(-ls + h) - 25 * Math.sin(-2 * l + f) + 21 * Math.sin(-l + f);
		  double lmoon = p2 * FRAK(lo + dl / 1296000);
		  double bmoon = (18520 * Math.sin(s) + n) / arc;
		  double cb = Math.cos(bmoon);
		  double x = cb * Math.cos(lmoon);
		  double v = cb * Math.sin(lmoon);
		  double w = Math.sin(bmoon);
		  double y = coseps * v - sineps * w;
		  double z = sineps * v + coseps * w;
		  double rho = Math.sqrt(1 - z * z);
		  double dec = (360 / p2) * Math.atan(z / rho);
		  double ra = (48 / p2) * Math.atan(y / (x + rho));
		  if(ra < 0){
			  ra += 24;
		  }
	    return new double[] {dec, ra};
	  }
	  double [] Sonnenberechnung(double t){
		  double p2 = 6.283185307;
		  double coseps = .91748;
		  double sineps = .39778;
		  double m = p2 * FRAK(.993133 + 99.997361 * t);
		  double dl = 6893 * Math.sin(m) + 72 * Math.sin(2 * m);
		  double l = p2 * FRAK(.7859453 + m / p2 + (6191.2 * t + dl) / 1296000);
		  double sl = Math.sin(l);
		  double x = Math.cos(l);
		  double y = coseps * sl;
		  double z = sineps * sl;
		  double rho = Math.sqrt(1 - z * z);
		  double dec = (360 / p2) * Math.atan(z / rho);
		  double ra = (48 / p2) * Math.atan(y / (x + rho));
		  if(ra < 0)
			  ra += 24;
	    return new double[]{dec, ra};
	  }

	  double [] QUAD(double yminus, double yo, double yplus){
		  double nz = 0;
		  double a = .5 * (yminus + yplus) - yo;
		  double b = .5 * (yplus - yminus);
		  double c = yo;
		  double xe = -b / (2 * a);
		  double ye = (a * xe + b) * xe + c;
		  double dis = b * b - 4 * a * c;
		  double zero1 = 0;
		  double zero2 = 0;
		  if(dis >= 0){
			  double dx = .5 * Math.sqrt(dis) / Math.abs(a);
			  zero1 = xe - dx;
			  zero2 = xe + dx;
		      if(Math.abs(zero1) <= 1){
		        nz += 1;
		      }
		      if(Math.abs(zero2) <= 1){
		        nz += 1;
		      }
		      if(zero1 < -1){
		        zero1 = zero2;
		      }
		    }
		    return new double[] {ye, zero1, zero2, nz};
		  }
	  /*
	  double LeseCookie()
	  {
		    var Cookie = new Array();
		    var Eintraege = new Array();
		    var Inhalt = document.cookie;
		    if(Inhalt != ""){
		      Cookie = Inhalt.split(";");
		      for (var t = 0; t <= Cookie.length; t++){
		        if(Cookie[t].search(/Koordinaten.+/) != -1){
		          Eintraege = Cookie[t].split(":");
		          document.koordinaten.name.value = Eintraege[1];

		          document.koordinaten.breitengrad.value = Math.abs(Eintraege[2]);
		          if(Eintraege[2] < 0){
		            document.koordinaten.breite[1].checked == true
		          }
		          else{
		            document.koordinaten.breite[0].checked == true
		          }
		          document.koordinaten.laengengrad.value = Math.abs(Eintraege[3]);
		          if(Eintraege[3] < 0){
		            document.koordinaten.laenge[0].checked == true
		          }
		          else{
		            document.koordinaten.laenge[1].checked == true
		          }

		          if(Eintraege[4] == "auto"){
		            document.koordinaten.systemzeit.value = "Aus";
		            document.koordinaten.zeitzone.value = Eintraege[4];
		          }
		          else{
		            document.koordinaten.systemzeit.value = "Ein";
		            document.koordinaten.zeitzone.value = Math.abs(Eintraege[4]);
		            if(Eintraege[4] < 0){
		              document.koordinaten.zone[1].checked == true
		            }
		            else{
		              document.koordinaten.zone[0].checked == true
		            }
		          }
		          break;
		        }
		      }
		    }
		    else{
		      KoordinatenReset();
		    }
		  }
		  function KoordinatenReset(){
		      document.koordinaten.name.value = "München";
		      document.koordinaten.breitengrad.value = "48.08";
		      document.koordinaten.breite[0].checked = true;
		      document.koordinaten.laengengrad.value = "11.42";
		      document.koordinaten.laenge[0].checked = true;
		      document.koordinaten.zeitzone.value = "auto";
		      document.koordinaten.zone[0].checked = true
		      document.koordinaten.systemzeit.value = "Aus";
		  }
		  function SchreibeCookie(){
		    var Name = document.koordinaten.name.value;
		    var Breite = document.koordinaten.breitengrad.value;
		    if(document.koordinaten.breite[1].checked == true){
		      Breite = Breite * -1;
		    }
		    var Laenge = document.koordinaten.laengengrad.value;
		    if(document.koordinaten.laenge[0].checked == true){
		      Laenge = Laenge * -1;
		    }
		    var Zone = document.koordinaten.zeitzone.value;
		    if(document.koordinaten.systemzeit.value == "Aus"){
		      Zone = "auto";
		    }
		    else if(document.koordinaten.zone[1].checked == true){
		      Zone = Zone * -1;
		    }
		    var Ablauf = new Date(Heute.getTime() + 86400000 * 30) //Löschdatum = Eintrag + 30 Tage
		    document.cookie = "Koordinaten=:" + Name + ":" + Breite + ":" + Laenge + ":" + Zone + "; expires=" + Ablauf.toGMTString();
		    AufUnterAusgabe();
		    if(document.cookie.search(/Koordinaten.+/) == -1){
		      alert("Speichern fehlgeschlagen!");
		    }
		   }
		  function Trennzeichen(Text){
		    var Zahl = "", Test, Punkt = false, Minus = false, Nummer = false;
		    for(var t = 0; t < Text.length; t++){
		      Test = Text.substring(t, t + 1);
		      if(Test >= "0" && Test <= "9"){
		        Zahl += Test;
		        Nummer = true;
		      }
		      else if(t == 0 && Test == "-"){
		        Minus = true;
		      }
		      else if(Punkt == false && Nummer == true){
		        Zahl += ".";
		        Punkt = true;
		      }
		    }
		    if(Minus == true){
		      Zahl = Zahl * -1;
		    }
		    return Zahl;
		  }
		  function Verlaengern(){
		    if(document.cookie.search(/Koordinaten.+/) != -1){
		      SchreibeCookie();
		    }
		  }
		  function PruefeBreitengrad(){
		    var Breite = Trennzeichen(document.koordinaten.breitengrad.value);
		    if(Breite < 0){
		      document.koordinaten.breite[1].checked = true;
		    }
		    else{
		      document.koordinaten.breite[0].checked = true;
		    }
		    Breite = Math.abs(Breite);
		    if(isNaN(Breite)){
		      Breite = 0;
		    }
		    if(Breite - Math.floor(Breite) >= .599){
		      Breite = Math.floor(Breite) + (Breite - Math.floor(Breite)) / 1 * .6; // Ins 60er System
		    }
		    if(Breite > 90){
		      Breite = Math.floor(Breite) % 90 + (Breite - Math.floor(Breite)); //Ins 90er System
		    }
		    Breite = Math.round(Breite * 100) / 100;
		    document.koordinaten.breitengrad.value = Breite;
		  }
		  function PruefeLaengengrad(){
		    var Laenge = Trennzeichen(document.koordinaten.laengengrad.value);
		    if(Laenge < 0){
		      document.koordinaten.laenge[1].checked = true;
		    }
		    else{
		      document.koordinaten.laenge[0].checked = true;
		    }
		    Laenge = Math.abs(Laenge);
		    if(isNaN(Laenge)){
		      Laenge = 0;
		    }
		    if(Laenge - Math.floor(Laenge) >= .599){
		      Laenge = Math.floor(Laenge) + (Laenge - Math.floor(Laenge)) / 1 * .6; // Ins 60er System
		    }
		    if(Laenge > 180){
		      Laenge = Math.floor(Laenge) % 180 + (Laenge - Math.floor(Laenge)); //Ins 180er System
		    }
		    Laenge = Math.round(Laenge * 100) / 100;
		    document.koordinaten.laengengrad.value = Laenge;
		  }
		  function PruefeZeitzone(){
		    var Zone = Trennzeichen(document.koordinaten.zeitzone.value);
		    var Test = 12;
		    if(document.koordinaten.systemzeit.value == "Aus"){
		      document.koordinaten.zeitzone.value = "auto";
		    }
		    else{
		      if(Zone < 0){
		        document.koordinaten.zone[1].checked = true;
		      }
		      else{
		        document.koordinaten.zone[0].checked = true;
		      }
		      if(document.koordinaten.zone[0].checked == true){
		        Test = 14;
		      }
		      Zone = Math.abs(Zone);
		      if(isNaN(Zone)){
		        Zone = 0;
		      }
		      if(Zone > Test){
		        Zone = Math.floor(Zone) % Test + (Zone - Math.floor(Zone)); //Ins 90er System
		      }
		      Zone = Math.round(Zone * 100) / 100;
		      document.koordinaten.zeitzone.value = Zone;
		    }
		  }
		  function ZoneUmschalten(){
		    if(document.koordinaten.systemzeit.value == "Aus"){
		      document.koordinaten.systemzeit.value = "Ein";
		    }
		    else{
		      document.koordinaten.systemzeit.value = "Aus";
		    }
		    PruefeZeitzone();
		    AufUnterAusgabe();
		  }
		  */
		  public void AufUnterAusgabe(double lat, double lon){
		    String [] AufUnter = AufUntergang(1, lat, lon);
		    
	    	System.out.println( "Mondaufgang ist um " +AufUnter[0] + " Uhr" );
	    	System.out.println( "Monduntergang ist um " + AufUnter[1] + " Uhr" );
		    
		    AufUnter = AufUntergang(2, lat, lon);
		    System.out.println( "Sonnenaufgang ist um " + AufUnter[0] + " Uhr" );
		    System.out.println( "Sonnenuntergang ist um "+ AufUnter[1] + " Uhr" );
		    
		    AufUnter = AufUntergang(3, lat, lon);
		    System.out.println( "Morgengrauen ist um " + AufUnter[0] + " Uhr" );
		    System.out.println( "Dämmerungsende ist um " + AufUnter[1] + " Uhr" );
		  }
		  
		  public SunAndMoonTimes(Calendar cal)
		  {
			  this.Heute = cal;
		  }
}
