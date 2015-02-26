package core;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;
import lib.Util;
//import util.Config;
//import util.Controller;

import java.lang.Math;

import lib.Config;
import lib.Controller;
import lib.navX.NavX;

public class Drive 
{
	//Encoders
	private Encoder frontEnc = new Encoder(Config.Drive.chnFrontEncA, Config.Drive.chnFrontEncB); 
	private Encoder backEnc = new Encoder(Config.Drive.chnBackEncA, Config.Drive.chnBackEncB);
	private Encoder leftEnc = new Encoder(Config.Drive.chnLeftEncA, Config.Drive.chnLeftEncB);
	private Encoder rightEnc = new Encoder(Config.Drive.chnRightEncA, Config.Drive.chnRightEncB);
		
	
	// CAN Talons
	private CANTalon mtLeftCAN = new CANTalon(Config.Drive.idMtLeftCAN);
	private CANTalon mtRightCAN = new CANTalon(Config.Drive.idMtRightCAN);
	private CANTalon mtFrontCAN = new CANTalon(Config.Drive.idMtFrontCAN);
	private CANTalon mtBackCAN = new CANTalon(Config.Drive.idMtBackCAN);
	// REG Talons
	private Talon mtLeft = new Talon(Config.Drive.chnMtLeft);
	private Talon mtRight = new Talon(Config.Drive.chnMtRight);
	
	private NavX navX;
	private Controller contr;
	
	private boolean fieldCentricMode = true;
	private double prevGyroAng = 0;
	
	public Drive (Controller newContr, NavX newNavX) 
	{
        contr = newContr;
        navX = newNavX;
        
        // Param not used
        //setTalonMode(false);
    }
	
	public void init(int driveId, double offsetAng)
	{
		if(driveId == Config.Drive.idRobotCentric) 
			fieldCentricMode = false;
		
		else
			fieldCentricMode = true;
		
		navX.setOffsetAngle(offsetAng);
	}
	
	/**
	 * Moves the robot in relation to the field instead of the robot
	 */	
	public void run() 
	{
		if(contr.getButton(Config.Drive.btFieldCentricMode))
			fieldCentricMode = true;
		
		else if(contr.getButton(Config.Drive.btRobotCentricMode))
				fieldCentricMode = false;
		
		if(fieldCentricMode)
			fieldCentric();
		
		else
			robotCentric();
	}
	
//	/**
//	 * Moves the robot on the set course with a timer
//	 * @param encDistance distance needed to be traveled
//	 * @param angle Angle you want to travel with right from the driver station as 0
//	 * @param magnitude the speed you want to travel at
//	 */
//	public void autoMove(double time, double angle, double magnitude)
//	{
//		double angDiff = navX.getYaw() - angle;
//		double centerSpeed = magnitude * Math.sin(Math.toRadians(angDiff));
//		double sideSpeed = magnitude * Math.cos(Math.toRadians(angDiff));
//		double turnSpeed = Math.pow(contr.getRawRightX(), 2) * (contr.getRawRightX() > 0 ? 1 : -1);
//		
//		setSpeed(sideSpeed, sideSpeed, centerSpeed, centerSpeed);
//	}
	
	/**
	 * Moves the robot in relation to the field
	 */
	public void fieldCentric() 
	{		
		double joyMag = getJoyMag(); 
		double joyAng = getJoyAng(); 
		double angDiff = navX.getAngle() - joyAng;
		double centerSpeed = joyMag * Math.sin(Math.toRadians(angDiff));
		double sideSpeed = joyMag * Math.cos(Math.toRadians(angDiff));
		double turnSpeed = Math.abs(contr.getRawRightX()) * contr.getRawRightX();
		
//		if(Math.abs(contr.getRawRightX()) > Config.Drive.minRightJoyValue)
//			prevGyroAng = navX.getAngle();
		
		if(Math.abs(turnSpeed) > Config.Drive.minTurnSpeed)
			prevGyroAng = navX.getAngle();
		
		else if(Math.abs(joyMag) > Config.Drive.minLeftJoyVal)
			turnSpeed = strafeAdjust();
		
		System.out.println(sideSpeed + " : " + centerSpeed + " : " + turnSpeed);
		setSpeed(sideSpeed + turnSpeed, sideSpeed - turnSpeed, centerSpeed + turnSpeed, centerSpeed - turnSpeed);
	} 
	
	/**
	 * Moves the robot in relation to itself
	 */
	public void robotCentric() 
	{
		double centerSpeed = Math.abs(contr.getLeftX()) * contr.getLeftX();
        double sideSpeed = Math.abs(contr.getLeftY()) * contr.getLeftY();
        double turnSpeed = Math.abs(contr.getRawRightX()) * contr.getRawRightX();
        centerSpeed *= Config.Drive.robotCentricStrafingScalar;
        setSpeed(sideSpeed + turnSpeed, sideSpeed - turnSpeed , centerSpeed + turnSpeed, centerSpeed - turnSpeed);
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
		leftSpeed = Util.ramp(mtLeftCAN.get(), -leftSpeed, Config.Drive.maxRampRate);
		rightSpeed = Util.ramp(mtRightCAN.get(), rightSpeed, Config.Drive.maxRampRate);
		frontSpeed = Util.ramp(mtFrontCAN.get(), -frontSpeed, Config.Drive.maxRampRate);
		backSpeed = Util.ramp(mtBackCAN.get(), backSpeed, Config.Drive.maxRampRate);
		
		mtLeftCAN.set(leftSpeed);
		mtLeft.set(leftSpeed);
		mtRightCAN.set(rightSpeed);
		mtRight.set(rightSpeed);				
		mtFrontCAN.set(frontSpeed);		
		mtBackCAN.set(backSpeed);
		
//		leftSpeed = -leftSpeed;
//		frontSpeed = -frontSpeed;
//		
//		mtLeftCAN.set(leftSpeed);
//		mtLeft.set(leftSpeed);
//		mtRightCAN.set(rightSpeed);
//		mtRight.set(rightSpeed);				
//		mtFrontCAN.set(frontSpeed);		
//		mtBackCAN.set(backSpeed);
	}
	
	/**
	 * Sets the talons to work with standard or encoder values
	 * @param encMode whether you want to set the talon to take encoder value
	 */
	public void setTalonMode(boolean encMode)
	{
//		if(encMode)
//		{
//			mtLeftOne.changeControlMode(CANTalon.ControlMode.Position);
//			mtRightOne.changeControlMode(CANTalon.ControlMode.Position);
//			mtFront.changeControlMode(CANTalon.ControlMode.Position);
//			mtBack.changeControlMode(CANTalon.ControlMode.Position);
//		}
//		
//		else
//		{
			mtLeftCAN.changeControlMode(CANTalon.ControlMode.Voltage);
			mtRightCAN.changeControlMode(CANTalon.ControlMode.Voltage);
			mtFrontCAN.changeControlMode(CANTalon.ControlMode.Voltage);
			mtBackCAN.changeControlMode(CANTalon.ControlMode.Voltage);
//		}
	}
	
	/**
	 * Returns Voltage of the asked talon
	 * @param talonNum Numbers :mtRightOne 1, mtLeftOne 3, mtFront 5, mtBack 6
	 * @return Voltage of the talon
	 */
	public double getVoltage(int talonNum)
	{
		switch(talonNum)
		{
			case Config.Drive.idMtLeftCAN: return mtLeftCAN.getBusVoltage();
			
			case Config.Drive.idMtRightCAN: return mtRightCAN.getBusVoltage();
			
			case Config.Drive.idMtFrontCAN: return mtFrontCAN.getBusVoltage();
			
			case Config.Drive.idMtBackCAN: return mtBackCAN.getBusVoltage();
		}
		
		return 0;
	}
	
	/**
	 * Adjusts the robot angle for inconsistent strafing
	 * @return the adjusted angle
	 */
	public double strafeAdjust()
	{
		return convertAngBound(navX.getAngle() - prevGyroAng) * Config.Drive.strafeAdjustment;
	}
	
	/**
	 * gets the speed you want to travel at during field centric mode
	 * @return the speed component for the vector of travel
	 */
	public double getJoyMag()
	{
		return Math.sqrt(Math.pow(contr.getRawLeftX(), 2) + Math.pow(contr.getRawLeftY(), 2));
	}
	
	/**
	 * Gets the angle the joystick was at
	 * @return returns the joystick angle
	 */
	public double getJoyAng()
	{
		double x = contr.getRawLeftX();
		double y = contr.getRawLeftY();
		
		if(x == 0 && y == 0)
			return  0;
		
		return convertAngBound((450 - Math.toDegrees(Math.atan2(y, x))) % 360);
	}
	
	/**
	 * Converts angles greater than 180 to their opposite coterminal angle
	 * @param ang the angle to be converted
	 * @return adjusted angle
	 */
	public double convertAngBound(double ang)
	{
		if(ang > 180)
			return -(360 - ang);
		
		return ang;
	}

	public int getFrontEnc() {
		return frontEnc.get();
	}

	public int getBackEnc() {
		return backEnc.get();
	}

	public int getLeftEnc() {
		return leftEnc.get();
	}

	public int getRightEnc() {
		return rightEnc.get();
	}


	


} 