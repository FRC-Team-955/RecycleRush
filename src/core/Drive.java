package core;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SerialPort;
import util.navX.IMUAdvanced;
import util.Config;
import util.Controller;

import java.lang.Math;

public class Drive {
	private CANTalon mtRightOne = new CANTalon(Config.Drive.chnMtRightOne);
	private CANTalon mtRightTwo = new CANTalon(Config.Drive.chnMtRightTwo);
	private CANTalon mtLeftOne = new CANTalon(Config.Drive.chnMtLeftOne);
	private CANTalon mtLeftTwo = new CANTalon(Config.Drive.chnMtLeftTwo);
	private CANTalon mtFront = new CANTalon(Config.Drive.chnMtFront);
	private CANTalon mtBack = new CANTalon(Config.Drive.chnMtBack);
	
	private Encoder frontEnc = new Encoder(Config.Drive.chnFrontEncA, Config.Drive.chnFrontEncB); 
	private Encoder backEnc = new Encoder(Config.Drive.chnBackEncA, Config.Drive.chnBackEncB);
	private Encoder leftEnc = new Encoder(Config.Drive.chnLeftEncA, Config.Drive.chnLeftEncB);
	private Encoder rightEnc = new Encoder(Config.Drive.chnRightEncA, Config.Drive.chnRightEncB);
	
	private SerialPort serial = new SerialPort(Config.Drive.navXBaudRate, SerialPort.Port.kMXP); 
	private IMUAdvanced navX;
	
	private Controller contr;
	
	public Drive (Controller newContr) 
	{
        contr = newContr;
        navX = new IMUAdvanced(serial, (byte) 50);
    }
	
	/**
	 * Moves the robot in relation to the field instead of the robot
	 */
	public void fieldCentric() 
	{
		double rightJoyXPos = contr.getRightX();
		
		//Moving with left joy
		if(rightJoyXPos < Config.Drive.tolerance && rightJoyXPos > -Config.Drive.tolerance) {
			double joyX = contr.getLeftX();
			double joyY = contr.getLeftY();
			double joyMag = Math.sqrt((joyX * joyX) + (joyY * joyY));
			double joyAng = (450 - Math.toDegrees(Math.atan2(joyY, joyX))) % 360;
			double angDiff = navX.getYaw() - joyAng;
			double centerSpeed = joyMag * Math.sin(Math.toRadians(angDiff));
			double sideSpeed = joyMag * Math.cos(Math.toRadians(angDiff));
			
			setSpeed(sideSpeed, sideSpeed, centerSpeed, centerSpeed);
		}
		
		//Turning with right joy
		else
		{
			mtRightOne.set(rightJoyXPos);
			mtRightTwo.set(rightJoyXPos);
			mtLeftOne.set(-rightJoyXPos);
			mtLeftTwo.set(-rightJoyXPos);
			mtFront.set(rightJoyXPos);
			mtBack.set(-rightJoyXPos);
		}		
	} 

	/**
	 * Sets the speed of all motors
	 * @param leftSpeed speed for the left motor
	 * @param rightSpeed speed for the right motor
	 * @param frontSpeed speed for the front motor
	 * @param backSpeed speed for the back motor
	 */
	public void setSpeed(double leftSpeed, double rightSpeed, double frontSpeed, double backSpeed)
	{
		leftSpeed = -leftSpeed;
		frontSpeed = -frontSpeed;
		mtRightOne.set(rightSpeed);
		mtRightTwo.set(rightSpeed);		
		mtLeftOne.set(leftSpeed);
		mtLeftTwo.set(leftSpeed);		
		mtFront.set(frontSpeed);		
		mtBack.set(backSpeed);	
	}
	
	/**
	 * Returns Voltage of the asked talon
	 * @param talonNum Numbers :mtRightOne 1, mtRightTwo, mtLeftOne 3, mtLeftTwo 4, mtFront 5, mtBack 6
	 * @return Voltage of the talon
	 */
	public double getVoltage(int talonNum)
	{
		switch(talonNum)
		{
			case 1: return mtRightOne.getBusVoltage();
			case 2: return mtRightTwo.getBusVoltage();
			case 3: return mtLeftOne.getBusVoltage();
			case 4: return mtLeftTwo.getBusVoltage();
			case 5: return mtFront.getBusVoltage();
			case 6: return mtBack.getBusVoltage();
		}
		
		return 0;
	}
	
	public double getFrontEncDist() 
	{
		return frontEnc.getDistance();
	}
	
	public double getBackEncDist() 
	{
		return backEnc.getDistance();
	}
	
	public double getLeftEncDist() 
	{
		return leftEnc.getDistance();
	}
	
	public double getRightEncDist() 
	{
		return rightEnc.getDistance();
	}
	
	/**
	 * Return the position of the requested Talon
	 * @param talonNum Numbers :mtRightOne 1, mtRightTwo, mtLeftOne 3, mtLeftTwo 4, mtFront 5, mtBack 6
	 * @return Position of the Talon
	 */
	public double getPosition(int talonNum)
	{
		switch(talonNum)
		{
			case 1: return mtRightOne.getPosition();
			case 2: return mtRightTwo.getPosition();
			case 3: return mtLeftOne.getPosition();
			case 4: return mtLeftTwo.getPosition();
			case 5: return mtFront.getPosition();
			case 6: return mtBack.getPosition();
		}
		
		return 0;
	}
	
	public void encReset()
	{
		leftEnc.reset();
		rightEnc.reset();
		frontEnc.reset();
		backEnc.reset();
	}
}
