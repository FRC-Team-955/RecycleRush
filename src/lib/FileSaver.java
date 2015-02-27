package lib;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class FileSaver 
{
	// TODO Add comments
	private FileWriter writer;
	private boolean isOpen = false;
	
	
	public FileSaver(String fileName) 
	{
		isOpen = true;	
		
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
			isOpen = false;
		}

		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public boolean isOpen()
	{
		return isOpen;
	}
}