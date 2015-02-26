package lib;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class FileSaver 
{
	// TODO Add comments
	private FileWriter writer;
	private boolean wasWrittenTo = false;
	
	
	public FileSaver(String fileName) 
	{
		wasWrittenTo = false;	
		
		try 
		{
			File f = new File(Config.FileSaver.saveDir);
			f.mkdir();
			
			f = new File(Config.FileSaver.saveDir + "/" + fileName);
			writer = new FileWriter(f);
		}

		catch (IOException e) 
		{	
			e.printStackTrace();
		}
	}

	public void write(String data) 
	{
		try 
		{
			writer.write(data);
			writer.flush();
			wasWrittenTo = true;
		}

		catch (IOException e) 
		{			
			e.printStackTrace();
		}
	}

	public void close() 
	{
		try 
		{
			writer.flush();
			writer.close();
		}

		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public boolean getWrittenTo()
	{
		return wasWrittenTo;
	}
}