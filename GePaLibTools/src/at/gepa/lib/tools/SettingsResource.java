package at.gepa.lib.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class SettingsResource {
	private static final String delim = "=";
	private HashMap<String, Object> lines;
	
	public static final String KEY_VERSION = "Version";
	
	public SettingsResource(InputStream inputStream)
	{
		lines = new HashMap<String, Object>();
		if( inputStream == null ) return;
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String read;

		try {
			while((read=br.readLine()) != null) {
			    add( read );   
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			try {
				br.close();
			} catch (IOException e) {
			}
		}
		
		
	}

	private void add(String l) {
		String [] sa = l.split(delim);
		if( sa == null || sa.length < 2 )
			return;
		lines.put(sa[0].toUpperCase(), sa[1]);
	}

	public Object getValue(String key) {
		key = key.toUpperCase();
		if( lines.containsKey(key) )
			return lines.get(key);
		return null;
	}

}
