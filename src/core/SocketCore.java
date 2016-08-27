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
	
	double distance = 1000;
	
	/**
	 * Runs in separate thread to handle communication efficiently 
	 */
	public void run() {
		if(!init){
			try {
				System.out.println("starting init");
				visionSocket = new Socket(Config.Sockets.hostName, Config.Sockets.port);
				System.out.println("socket created");
				out = new PrintWriter(visionSocket.getOutputStream(), true);
				System.out.println("out created");
				in = new BufferedReader(new InputStreamReader(visionSocket.getInputStream()));	
				System.out.println("in created.connected");
				out.println("connected");
			
				/*
				System.out.println("starting init");
				visionServer = new ServerSocket(Config.Sockets.port);
				System.out.println("server started");
				clientSocket = visionServer.accept();
				System.out.println("server accepted");
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));*/
				init=true;
				System.out.println("connected");
			} catch(Exception e) {
				System.out.println("visionServer initialization threw exception");
			}
		}
		
		try {
			while(true) {
				out.println(Integer.toString(updateNumber));
				try {
					String input = in.readLine();
					if(!input.contains("Info")) {
						distance = Double.parseDouble(input);						
					}
					//String test = in.readLine();					
				} catch (Exception e) {
					//System.out.println("not a double");
				}
				//System.out.println(test);
				//System.out.println(distance);
				updateNumber++;
				//Thread.sleep(50);
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
