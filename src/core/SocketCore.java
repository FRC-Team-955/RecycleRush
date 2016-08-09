package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import lib.Config;

public class SocketCore implements Runnable {
	public Socket visionSocket;
	public ServerSocket visionServer;
	Socket clientSocket;
	PrintWriter out;
	BufferedReader in;
	boolean lostConnection = false;
	int updateNumber = 0;
	private boolean init = false;
	
	double distance = 0;
	
	/**
	 * Runs in separate thread to handle communication efficiently 
	 */
	public void run() {
		if(!init){
			try {
				visionServer = new ServerSocket(Config.Sockets.port);
				clientSocket = visionServer.accept();
				init=true;
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				System.out.println("connected");
			} catch(Exception e) {
				System.out.println("visionServer initialization threw exception");
			}
		}
		
		try {
			while(true) {
				out.println(Integer.toString(updateNumber));
				distance = Double.parseDouble(in.readLine());
				System.out.println(distance);
				updateNumber++;
				Thread.sleep(50);
			} 
		} catch(Exception e) {
			
		}
		init = true;
	}
	
	public double getDistance(){
		return distance;
	}
	
	public void setInit(boolean init) {
		this.init = init;
	}
	
	public boolean getSocketStatus() {
		try {
			return lostConnection ? lostConnection : visionSocket.isConnected();			
		} catch (Exception e) {
			return false;
		}
	}
	
	public void initClient() {
		try {
			visionSocket = new Socket(Config.Sockets.hostName, Config.Sockets.port);
			out = new PrintWriter(visionSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(visionSocket.getInputStream()));	
			out.println("connected");
			
		} catch(UnknownHostException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initServer() {
		try {
			visionServer = new ServerSocket(Config.Sockets.port);
			Socket clientSocket = visionServer.accept();
			out = new PrintWriter(clientSocket.getOutputStream(), true);
		    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch(Exception e) {
			
		}
	}
	
	public void connectServer () {
		try {
			visionServer.close();
			visionServer = new ServerSocket(Config.Sockets.port);
			Socket clientSocket = visionServer.accept();
			out = new PrintWriter(clientSocket.getOutputStream(), true);
		    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch(Exception e) {
			
		}
	}
	
	public void update() {
		try{
			out.println(Integer.toString(updateNumber));
			distance = Double.parseDouble(in.readLine());
			updateNumber++;
		} catch (IOException e) {
			lostConnection = true;
			System.out.println("ioexception");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("exception");
		}
	}
}
