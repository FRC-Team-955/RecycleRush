package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.microedition.io.Connector;
import com.sun.squawk.microedition.io.FileConnection;

public class FileSystem {
	public String preface = "file:///"; //Useful if added to the beginnings of URLs to access the internal cRIO memory
	private String directory = "";
	
	/**
	 * Constructor
	 * Allows for reading and writing to files on the roboRIO
	 * @param URL The Directory of the file
	 */
	public FileSystem (String URL) {
		directory = URL;
	}
	
	
	/**
	 * Reads individual lines of text
	 * @param lineNumber The line to get text from
	 * @return The content of the line
	 */
	public String getLine(int lineNumber) {
		String text = "";
		try {
			FileConnection connection = (FileConnection) Connector.open(directory);
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.openInputStream()));
			
			for (int i = 0; i < (lineNumber - 1); i++)
				reader.readLine();
				
			text = reader.readLine();
			reader.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return text;
	}
	
	/**
	 * Writes text to files
	 * @param text Will write this text to the file
	 */
	public void writeAll(String text) {
		try {
			FileConnection connection = (FileConnection) Connector.open(directory);
			OutputStreamWriter writer = new OutputStreamWriter(connection.openOutputStream());
			writer.write(text);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Gets all of the text in a file
	 * @return All of the text
	 */
	public String readAll() {
		String text = "";
		try {
			FileConnection connection = (FileConnection) Connector.open(directory);
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.openInputStream()));
			
			String read = "DefaultText";
			while (read != null) {
				read = reader.readLine();
				text += read;
			}
			
			reader.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return text;
	}
	
	
	
}
