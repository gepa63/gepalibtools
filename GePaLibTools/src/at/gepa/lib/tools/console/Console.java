package at.gepa.lib.tools.console;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Console {
	public static String readString(String prefix)
	{
		String ret = null;
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader( new InputStreamReader(System.in ));
			
			System.out.print(prefix);
			ret = reader.readLine();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			ret = null;
		}
		return ret;
	}
}
