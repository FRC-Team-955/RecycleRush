package core;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;
import util.Config;
import util.Controller;
import util.navX.NavX;

import java.lang.Math;

public class Drive 
{
	
	//CAN Talons
	private CANTalon mtLeftOne = new CANTalon(Config.Drive.chnMtLeftOne);
	private Talon mtLeftTwo = new Talon(Config.Drive.chnMtLeftTwo);
	private CANTalon mtRightOne = new CANTalon(Config.Drive.chnMtRightOne);
	private Talon mtRightTwo = new Talon(Config.Drive.chnMtRightTwo);
	private CANTalon mtFront = new CANTalon(Config.Drive.chnMtFront);
	private CANTalon mtBack = new CANTalon(Config.Drive.chnMtBack);;
	
	//Old Talons (For practice bot)
//	private Talon mtLeftOne = new Talon(Config.Drive.chnMtLeftOne);
//	private Talon mtRightOne = new Talon(Config.Drive.chnMtRightOne);
//	private Talon mtFront = new Talon(Config.Drive.chnMtFront);
//	private Talon mtBack = new Talon(Config.Drive.chnMtBack);
	
	private Timer timer = new Timer();
	
	private Encoder frontEnc = new Encoder(Config.Drive.chnFrontEncA, Config.Drive.chnFrontEncB); 
	private Encoder backEnc = new Encoder(Config.Drive.chnBackEncA, Config.Drive.chnBackEncB);
	private Encoder leftEnc = new Encoder(Config.Drive.chnLeftEncA, Config.Drive.chnLeftEncB);
	private Encoder rightEnc = new Encoder(Config.Drive.chnRightEncA, Config.Drive.chnRightEncB);
	
	private double prevGyroAng = 0;
	
	private NavX navX;
	
	private Controller contr;
	
	private Dashboard dash;
	
	public Drive (Controller newContr, double angleOffset, NavX newNavX) 
	{
        contr = newContr;
        navX = newNavX;
    }
	
	/**
	 * Moves the robot in relation to the field instead of the robot
	 */	
	public void run(int driveId) 
	{
		if(driveId == Config.Drive.idRobotCentric) 
			robotCentric();
		else
			fieldCentric();
	}
	
	/**
	 * Moves the robot on the set course with encoders
	 * @param encDistance distance needed to be traveled
	 * @param angle Angle you want to travel with Straight ahead from the driver station as 0
	 */
	public void autoMove(double encDistance, double angle) 
	{
		// North is 0 degrees
		setTalonMode(true);
		
		double centerSpeed = (encDistance * Math.sin(angle));
		double sideSpeed = (encDistance * Math.cos(angle));

		setPos(sideSpeed, sideSpeed, centerSpeed, centerSpeed);
	}
	
	/**
	 * Moves the robot on the set course with a timer
	 * @param encDistance distance needed to be traveled
	 * @param angle Angle you want to travel with right from the driver station as 0
	 * @param magnitude the speed you want to travel at
	 */
	public void autoMove(double time, double angle, double magnitude)
	{
		
		timer.start();
		
		double angDiff = navX.getYaw() - angle;
		double centerSpeed = magnitude * Math.sin(Math.toRadians(angDiff));
		double sideSpeed = magnitude * Math.cos(Math.toRadians(angDiff));
		double turnSpeed = Math.pow(contr.getRawRightX(), 2) * (contr.getRawRightX() > 0 ? 1 : -1);
		
		setSpeed(sideSpeed, sideSpeed, centerSpeed, centerSpeed);
	}
	
	/**
	 * Moves the robot in relation to the field
	 */
	public void fieldCentric() 
	{		
		double joyMag = getJoyMag(); 
		double joyAng = getJoyAng(); 
		double angDiff = navX.getYaw() - joyAng;
		double centerSpeed = joyMag * Math.sin(Math.toRadians(angDiff));
		double sideSpeed = joyMag * Math.cos(Math.toRadians(angDiff));
		double turnSpeed = Math.pow(contr.getRawRightX(), 2) * (contr.getRawRightX() > 0 ? 1 : -1);
		
		if(Math.abs(contr.getRawRightX()) > Config.Drive.minRightJoyValue)
		{
			prevGyroAng = navX.getAngle();
			setSpeed(sideSpeed + turnSpeed, sideSpeed - turnSpeed, centerSpeed + turnSpeed, centerSpeed - turnSpeed);
		} 
		
		else
		{
			setSpeed(sideSpeed, sideSpeed, centerSpeed + strafeAdjust(), centerSpeed - strafeAdjust());
		}
	} 
	
	/**
	 * Moves the robot in relation to itself
	 */
	public void robotCentric() 
	{
		double joyX = contr.getLeftX();
		double joyY = contr.getLeftY();
		double x = Math.abs(joyX) * joyX;
        double y = Math.abs(joyY) * joyY;
        
        x *= Config.Drive.robotCentricTurningScalar;
        setSpeed(-x + y, x + y, 0, 0);
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
		if (dash.getTalonModeType() == 0)
		{
			leftSpeed = -leftSpeed;
			frontSpeed = -frontSpeed;
			mtLeftOne.set(leftSpeed);
			mtLeftTwo.set(leftSpeed);
			mtRightOne.set(rightSpeed);
			mtRightTwo.set(rightSpeed);				
			mtFront.set(frontSpeed);		
			mtBack.set(backSpeed);
		}
		else
		{
			leftSpeed = -leftSpeed;
			frontSpeed = -frontSpeed;
			mtLeftOne.set(leftSpeed);
			mtLeftTwo.set(leftSpeed);
			mtRightOne.set(rightSpeed);
			mtRightTwo.set(rightSpeed);				
			mtFront.set(frontSpeed);		
			mtBack.set(backSpeed);
		}
	}
	
	/**
	 * Sets the talons to work with standard or encoder values
	 * @param encMode whether you want to set the talon to take encoder value
	 */
	public void setTalonMode(boolean encMode)
	{
		if(encMode)
		{
			mtLeftOne.changeControlMode(CANTalon.ControlMode.Position);
			mtRightOne.changeControlMode(CANTalon.ControlMode.Position);
			mtFront.changeControlMode(CANTalon.ControlMode.Position);
			mtBack.changeControlMode(CANTalon.ControlMode.Position);
		}
		
		else
		{
			mtLeftOne.changeControlMode(CANTalon.ControlMode.Speed);
			mtRightOne.changeControlMode(CANTalon.ControlMode.Speed);
			mtFront.changeControlMode(CANTalon.ControlMode.Speed);
			mtBack.changeControlMode(CANTalon.ControlMode.Speed);
		}
	}
	
	/**
	 * Sets the talons with encoder values
	 * @param leftPos left encoder value
	 * @param rightPos right encoder value
	 * @param frontPos front encoder value
	 * @param backPos back encoder value
	 */
	public void setPos(double leftPos, double rightPos, double frontPos, double backPos)
	{
		if (dash.getTalonModeType() == 0)
		{
			mtLeftOne.setPosition(leftPos);
			mtLeftTwo.set(mtLeftOne.getBusVoltage());
			mtRightOne.setPosition(rightPos);
			mtRightTwo.set(mtRightOne.getBusVoltage());
			mtFront.setPosition(frontPos);
			mtBack.setPosition(backPos);
		}
		else
		{
			mtLeftOne.setPosition(leftPos);
			mtLeftTwo.set(mtLeftOne.get());
			mtRightOne.setPosition(rightPos);
			mtRightTwo.set(mtRightOne.get());
			mtFront.setPosition(frontPos);
			mtBack.setPosition(backPos);
		}
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
			case 1: return mtRightOne.getBusVoltage();
			case 3: return mtLeftOne.getBusVoltage();
			case 5: return mtFront.getBusVoltage();
			case 6: return mtBack.getBusVoltage();
		}
		
		return 0;
	}
	
	/**
	 * Adjusts the robot angle for inconsistent strafing
	 * @return the adjusted angle
	 */
	public double strafeAdjust()
	{
			return convertAngBound(navX.getAngle() - prevGyroAng) * Config.Drive.turnAdjustment;
	}
	
	/**
	 * Gets the front encoder value
	 * @return the front encoder value
	 */
	public double getFrontEncDist() 
	{
		return frontEnc.getDistance();
	}
	
	/**
	 * Gets the back encoder value
	 * @return the back encoder value
	 */
	public double getBackEncDist() 
	{
		return backEnc.getDistance();
	}
	
	/**
	 * Gets the left encoder value
	 * @return the left encoder value
	 */
	public double getLeftEncDist() 
	{
		return leftEnc.getDistance();
	}

	/**
	 * Gets the right encoder value
	 * @return the right encoder value
	 */
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
		if (dash.getTalonModeType() == 0)
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
		}
		else
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
		}
		
		return 0;
	}
	
	/**
	 * Resets all the encoders
	 */
	public void encReset()
	{
		leftEnc.reset();
		rightEnc.reset();
		frontEnc.reset();
		backEnc.reset();
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
}
	
