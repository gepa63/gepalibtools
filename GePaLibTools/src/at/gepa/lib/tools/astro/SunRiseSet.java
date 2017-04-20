package at.gepa.lib.tools.astro;

import java.util.Calendar;

import at.gepa.lib.tools.time.TimeTool;

public class SunRiseSet {

    private double pi = Math.PI;
    private double rad = pi / 180.0;
    private double h = -(50.0 / 60.0) * rad;
    private double hBürgerlicheDämmerung = -6 * rad;
    private double breite = 47.5;
    private double b = breite * rad;
    private double laenge = 8.5;
    private int zeitzone = 2;
    private int tag;

    private String aufgangStr;
    private String untergangStr;
	private String aufgangBürgerlichStr;
	private String untergangBürgerlichStr;

    public SunRiseSet(double breite, double laenge, Calendar date) {
    	this( breite, laenge, date.get(Calendar.DAY_OF_YEAR), date.get(Calendar.ZONE_OFFSET) );
    }
    public SunRiseSet(double breite, double laenge, int tag, int zeitzone) {
        this.breite = breite;
        this.laenge = laenge;
        this.zeitzone = zeitzone;
        this.tag = tag;
        init();
    }

    private void init() {
        double aufgang = aufgang1(this.tag);
        double untergang = untergang1(this.tag);
        
        double aufgangBürgerlich = aufgangBürgerlich(this.tag);
        double untergangBürgerlich = untergangBürgerlich(this.tag);

        aufgang = aufgang - laenge / 15.0 + zeitzone;
        untergang = untergang - laenge / 15.0 + zeitzone;

        this.aufgangStr = toString(aufgang);
        this.untergangStr = toString(untergang);
        
        this.aufgangBürgerlichStr = toString(aufgangBürgerlich);
        this.untergangBürgerlichStr = toString(untergangBürgerlich);
        

//        int untergangh = (int) Math.floor(untergang);
//        String untergangH = ((untergangh < 10) ? "0" + String.valueOf(untergangh) : String.valueOf(untergangh));
//        int untergangm = (int) Math.floor(((untergang - untergangh) * 100) * 3 / 5);
//        String untergangM = ((untergangm < 10) ? "0" + String.valueOf(untergangm) : String.valueOf(untergangm));
//        int untergangs = (int) Math.floor(((((((untergang - untergangh) * 100) * 3 / 5) - untergangm) * 100) * 3 / 5));
//        String untergangS = ((untergangs < 10) ? "0" + String.valueOf(untergangs) : String.valueOf(untergangs));
//        
//        this.untergangStr = untergangH + ":" + untergangM + ":" + untergangS;

    }

    private String toString(double aufgang) {
        int aufgangh = (int) Math.floor(aufgang);
        String aufgangH = ((aufgangh < 10) ? "0" + String.valueOf(aufgangh) : String.valueOf(aufgangh));
        int aufgangm = (int) Math.floor(((aufgang - aufgangh) * 100) * 3 / 5);
        String aufgangM = ((aufgangm < 10) ? "0" + String.valueOf(aufgangm) : String.valueOf(aufgangm));
        int aufgangs = (int) Math.floor(((((((aufgang - aufgangh) * 100) * 3 / 5) - aufgangm) * 100) * 3 / 5));
        String aufgangS =((aufgangs < 10) ? "0" + String.valueOf(aufgangs) : String.valueOf(aufgangs));
        
		return aufgangH + ":" + aufgangM + ":" + aufgangS;
	}
	private double sonnendeklination(double t) {
        return 0.40954 * Math.sin(0.0172 * (t - 79.35));
    }

    private double zeitdifferenz(double deklination) {
        return 12.0 * Math.acos((Math.sin(h) - Math.sin(b) * Math.sin(deklination)) / (Math.cos(b) * Math.cos(deklination))) / pi;
    }
    private double zeitdifferenzBürgerlich(double deklination) {
        return 12.0 * Math.acos((Math.sin(hBürgerlicheDämmerung) - Math.sin(b) * Math.sin(deklination)) / (Math.cos(b) * Math.cos(deklination))) / pi;
    }

    private double zeitgleichung(double t) {
        return -0.1752 * Math.sin(0.033430 * t + 0.5474) - 0.1340 * Math.sin(0.018234 * t - 0.1939);
    }

    private double aufgang1(double t) {
        double deklination = sonnendeklination(t);
        return 12 - zeitdifferenz(deklination) - zeitgleichung(t);
    }
    private double aufgangBürgerlich(double t) {
        double deklination = sonnendeklination(t);
        return 12 - zeitdifferenzBürgerlich(deklination) - zeitgleichung(t);
    }

    private double untergangBürgerlich(double t) {
        double deklination = sonnendeklination(t);
        return 12 + zeitdifferenzBürgerlich(deklination) - zeitgleichung(t);
    }
    private double untergang1(double t) {
        double deklination = sonnendeklination(t);
        return 12 + zeitdifferenz(deklination) - zeitgleichung(t);
    }

    public String getAufgangStr() {
        return aufgangStr;
    }

    public String getUntergangStr() {
        return untergangStr;
    }
    public String getAufgangBürgerlichStr() {
        return aufgangBürgerlichStr;
    }

    public String getUntergangBürgerlichStr() {
        return untergangBürgerlichStr;
    }
    
    public Calendar getAufgang()
    {
    	Calendar cal = TimeTool.toCalendar( this.tag, this.zeitzone, aufgangStr);
    	return cal;
    }
    public Calendar getUntergang()
    {
    	Calendar cal = TimeTool.toCalendar( this.tag, this.zeitzone, untergangStr);
    	return cal;
    }
    public Calendar getAufgangBürgerlich()
    {
    	Calendar cal = TimeTool.toCalendar( this.tag, this.zeitzone, aufgangBürgerlichStr);
    	return cal;
    }
    public Calendar getUntergangBürgerlich()
    {
    	Calendar cal = TimeTool.toCalendar( this.tag, this.zeitzone, untergangBürgerlichStr);
    	return cal;
    }

//    public static void main(String... args) {
//        SunRiseSet sun = new SunRiseSet(52,9, 176, 2);
//
//        System.out.println("Sonnenaufgang : " + sun.getAufgangStr());
//        System.out.println("Sonnenuntergang : " + sun.getUntergangStr());
//    }
}
