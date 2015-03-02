package lib;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class FileSaver 
{
	// TODO Add comments
	private FileWriter writer;
	private boolean isOpen = false;
	
	/**
	 * Creates a file object for writing to the roborio
	 * @param fileName
	 */
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

	/**
	 * Writes data to the file, with new line '\n' at the end
	 * @param data
	 */
	public void write(String data) 
	{
		try 
		{
			writer.write(data + "\n");
			writer.flush();
		}

		catch (IOException e) 
		{			
			e.printStackTrace();
		}
	}

	/**
	 * Closes the file
	 */
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
	
	/**
	 * Returns true if the file is open
	 * @return
	 */
	public boolean isOpen()
	{
		return isOpen;
	}
}