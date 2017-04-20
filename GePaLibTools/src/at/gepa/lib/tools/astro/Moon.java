package at.gepa.lib.tools.astro;

public class Moon {
	public static enum MoonPhases
    {
        /// <summary>
        /// Newmoon phase.
        /// </summary>
        newmoon,
        /// <summary>
        /// Waxing crescent moon phase.
        /// </summary>
        waxingcrescent,
        /// <summary>
        /// First quarter phase.
        /// </summary>
        //firstquarter,
        /// <summary>
        /// Waxing gibbous moon phase.
        /// </summary>
        //waxinggibbous,
        /// <summary>
        /// Fullmoon phase.
        /// </summary>
        fullmoon,
        /// <summary>
        /// Waning gibbous moon phase.
        /// </summary>
        //waninggibbous,
        /// <summary>
        /// Last quarter phase.
        /// </summary>
        lastquarter,
        /// <summary>
        /// Waning crescent moon phase.
        /// </summary>
        waningcrescent;
        
        public String toString()
        {
        	switch(this)
        	{
        	case newmoon:
        		return "Neumond";
        	case waxingcrescent:
        		return "Zunehmender Mond";
        	case fullmoon:
        		return "Vollmond";
        	case waningcrescent:
        		return "Abnehmender Mond";
        	}
        	return name();
        }
    }
	
    /// <summary>
    /// The moonphase of the given date.
    /// </summary>
    private MoonPhases Phase;

    /// <summary>
    /// Initializes a new instance of the <see cref="Moon"/> class.
    /// </summary>
    /// <param name="year">The year.</param>
    /// <param name="day">The day.</param>
    /// <param name="hour">The hour.</param>
    public Moon(int year, int month, int day)
    {
    	double P2 = 3.14159 * 2;
    	double YY = year - (int)((12 - month) / 10);
    	double MM = month + 9;
        if (MM >= 12) { MM = MM - 12; }
        double K1 = (int)(365.25 * (YY + 4712));
        double K2 = (int)(30.6 * MM + .5);
        double K3 = (int)((int)((YY / 100) + 49) * .75) - 38;
        double J = K1 + K2 + day + 59;
        if (J > 2299160) { J = J - K3; }
        double V = (J - 2451550.1) / 29.530588853;
        V = V - (int)(V);
        if (V < 0) { V = V + 1; }
        double AG = V * 29.53;
        if ((AG > 27.6849270496875) || (AG <= 1.8456618033125))
        {
            Phase = MoonPhases.newmoon;
        }
        else if ((AG > 1.8456618033125) && (AG <= 5.5369854099375))
        {
            Phase = MoonPhases.waxingcrescent;
        }
        else if ((AG > 5.5369854099375) && (AG <= 9.2283090165625))
        {
            Phase = MoonPhases.waxingcrescent;
        }
        else if ((AG > 9.2283090165625) && (AG <= 12.9196326231875))
        {
            Phase = MoonPhases.waxingcrescent;
        }
        else if ((AG > 12.9196326231875) && (AG <= 16.6109562298125))
        {
            Phase = MoonPhases.fullmoon;
        }
        else if ((AG > 16.6109562298125) && (AG <= 20.3022798364375))
        {
            Phase = MoonPhases.waningcrescent;
        }
        else if ((AG > 20.3022798364375) && (AG <= 23.9936034430625))
        {
            Phase = MoonPhases.lastquarter;
        }
        else if ((AG > 23.9936034430625) && (AG <= 27.6849270496875))
        {
            Phase = MoonPhases.waningcrescent;
        }
        else
        {
            Phase = MoonPhases.fullmoon;
        }
    }
    public MoonPhases getPhase()
    {
    	return Phase;
    }
}
