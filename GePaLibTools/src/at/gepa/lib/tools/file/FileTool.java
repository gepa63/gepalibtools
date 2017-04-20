package at.gepa.lib.tools.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class FileTool {

	public static void copyFolder(File source, File destination)
	{
		if (source.isDirectory())
		{
			if (!destination.exists())
			{
				destination.mkdirs();
			}
		
			String files[] = source.list();
		
			for (String file : files)
			{
				File srcFile = new File(source, file);
				File destFile = new File(destination, file);
				copyFolder(srcFile, destFile);
			}
		}
		else
		{
			java.io.InputStream in = null;
			java.io.OutputStream out = null;
		
			try
			{
				in = new java.io.FileInputStream(source);
				out = new java.io.FileOutputStream(destination);
		
				byte[] buffer = new byte[1024];
				int length;
				while ((length = in.read(buffer)) > 0)
				{
					out.write(buffer, 0, length);
				}
				in.close(); out.close(); in = null; out = null;
			}
			catch (Exception e)
			{
				try { if( in != null ) in.close(); } catch (Exception e1) { e1.printStackTrace(); }
				try { if( out != null ) out.close(); } catch (Exception e1) { e1.printStackTrace(); }
			}
		}
	}
	
	public static void removeDirectory( File dir )
	{
		File [] files = dir.listFiles();
		if( files != null ) 
		{
			for( File f : files )
			{
				if( f.isDirectory() )
					removeDirectory(f);
				f.delete();
			}
		}
		dir.delete();
	}

	public static String santinize(String name) {
		return name.replaceAll("[^\\w.-]", "_");
	}
	public static ArrayList<String> readScript( File f )
	{
		return readScript( f, "GO" );
	}
	public static ArrayList<String> readScript( File f, String lineDelim )
	{
		ArrayList<String> ret = new ArrayList<String>();
		BufferedReader bfr = null;
		try
		{
			bfr = new BufferedReader(new FileReader(f) );
			String line;
			String batch = "";
			while( (line = bfr.readLine()) != null)
			{
				if( line.equalsIgnoreCase(lineDelim) )
				{
					ret.add(batch);
					batch = "";
				}
				else
				{
					if( !batch.isEmpty() )
						batch += "\n";
					batch += line;
				}
			}
			if( !batch.isEmpty() )
				ret.add(batch);
		}
		catch (Exception e)
		{
			try { if( bfr != null ) bfr.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		return ret;
	}
}
