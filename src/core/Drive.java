package core;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;
import lib.PID;
import lib.Util;
//import util.Config;
//import util.Controller;



import java.lang.Math;

import lib.Config;
import lib.Controller;
import lib.navX.NavX;

public class Drive 
{
	// TODO Fix joystick axis in field centric mode
	// Encoders
	private Encoder encFront = new Encoder(Config.Drive.chnEncFrontA, Config.Drive.chnEncFrontB); 
	private Encoder encBack = new Encoder(Config.Drive.chnEncBackA, Config.Drive.chnEncBackB);
	private Encoder encLeft = new Encoder(Config.Drive.chnEncLeftA, Config.Drive.chnEncLeftB);
	private Encoder encRight = new Encoder(Config.Drive.chnEncRightA, Config.Drive.chnEncRightB);
		
	// CAN Talons
	private CANTalon mtLeftCAN = new CANTalon(Config.Drive.idMtLeft);
	private CANTalon mtRightCAN = new CANTalon(Config.Drive.idMtRight);
	private CANTalon mtFrontCAN = new CANTalon(Config.Drive.idMtFront);
	private CANTalon mtBackCAN = new CANTalon(Config.Drive.idMtBack);
	
	// REG Talons
	private Talon mtLeft = new Talon(Config.Drive.chnMtLeft);
	private Talon mtRight = new Talon(Config.Drive.chnMtRight);
	
	private NavX navX = new NavX();
	private Controller contr;
	
	//PID
	private PID pidStrafe = new PID(Config.Drive.kStrafeP, Config.Drive.kStrafeI, Config.Drive.kStrafeD);
	private PID pidLeft= new PID(Config.Drive.kLeftP, Config.Drive.kLeftI, Config.Drive.kLeftD);
	private PID pidRight = new PID(Config.Drive.kRightP, Config.Drive.kRightI, Config.Drive.kRightD);
	private PID pidFront = new PID(Config.Drive.kFrontP, Config.Drive.kFrontI, Config.Drive.kFrontD);
	private PID pidBack = new PID(Config.Drive.kBackP, Config.Drive.kBackI, Config.Drive.kBackD);
	
	private double wantLeftPos = 0;
	private double wantRightPos = 0;
	private double wantFrontPos = 0;
	private double wantBackPos = 0;
	
	private boolean fieldCentricMode = false;
	private double wantStrafeAng = 0;
	private double turnSpeed = 0;
	private boolean slowMode = true;
	
	public Drive (Controller newContr) 
	{
        contr = newContr;
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
		System.out.println("Joy Angle " + getJoyAng() + " : Joy Left X " + contr.getRawLeftX() + " : Joy Left Y " + contr.getRawLeftY() );

		double joyAng = getJoyAng();
		double leftJoyMag = getJoyMag();  
		double centerSpeed = leftJoyMag * -Math.sin(Math.toRadians(joyAng));
        double sideSpeed = leftJoyMag * Math.cos(Math.toRadians(joyAng));
        turnSpeed = Util.limit(Util.ramp(turnSpeed, Math.abs(contr.getRawRightX()) * contr.getRawRightX(), Config.Drive.turnRampRate), Config.Drive.minTurnSpeed, Config.Drive.maxTurnSpeed);    
        
        // Enable/Disable field centric mode
        if(contr.getButton(Config.ContrDrive.btFieldCentricMode))
			fieldCentricMode = true;
		
		else if(contr.getButton(Config.ContrDrive.btRobotCentricMode))
			fieldCentricMode = false;
        
        if(contr.getButton(Config.ContrDrive.btToggleSlowMode))
        	setSlowMode(!slowMode);
        
        // Run field centric mode if enabled
		if(fieldCentricMode)
		{
			double angDiff = getAngle() - joyAng;
			centerSpeed = -leftJoyMag * Math.sin(Math.toRadians(angDiff));
			sideSpeed = leftJoyMag * Math.cos(Math.toRadians(angDiff));
		}
		
		// Set wantStrafe angle when bot is turning or not moving, stop strafe pid
		if(Math.abs(turnSpeed) > Config.Drive.minTurnJoyVal || Math.abs(leftJoyMag) <= Config.Drive.minLeftJoyMag)
		{
			wantStrafeAng = getAngle();
			
			if(pidStrafe.isRunning())
			{
				pidStrafe.stop();
				pidStrafe.reset();
			}
		}
		
		// Run strafe pid if only moving, not turning
		else if(Math.abs(leftJoyMag) > Config.Drive.minLeftJoyMag)
		{
			if(!pidStrafe.isRunning())
			{
				pidStrafe.reset();
				pidStrafe.start();
			}
			
			pidStrafe.update(getAngle(), wantStrafeAng);
			
			if(fieldCentricMode)
				centerSpeed *= -1;
			
			turnSpeed = pidStrafe.getOutput();
				
			System.out.println("PID " + pidStrafe.getOutput());
		}
		
//		System.out.println(sideSpeed + " : " + centerSpeed + " : " + turnSpeed);
		setSpeed(sideSpeed + turnSpeed, sideSpeed - turnSpeed, centerSpeed - turnSpeed, centerSpeed + turnSpeed);
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
		rightSpeed = -rightSpeed;
		backSpeed = -backSpeed;
		
		if(slowMode)
		{
			leftSpeed *= Config.Drive.slowSideSpeedScalar;
			rightSpeed *= Config.Drive.slowSideSpeedScalar;
			frontSpeed *= Config.Drive.slowCenterSpeedScalar;
			backSpeed *= Config.Drive.slowCenterSpeedScalar;
		}
		
		leftSpeed = Util.ramp(mtLeftCAN.get(), leftSpeed, Config.Drive.rampSideRate);
		rightSpeed = Util.ramp(mtRightCAN.get(), rightSpeed, Config.Drive.rampSideRate);
		frontSpeed = Util.ramp(mtFrontCAN.get(), frontSpeed, Config.Drive.rampCenterRate);
		backSpeed = Util.ramp(mtBackCAN.get(), backSpeed, Config.Drive.rampCenterRate);
		
//		System.out.println("Left " + leftSpeed + "  : Right " + rightSpeed + "  : Front " + frontSpeed + "  : Back " + backSpeed);
		
		mtLeftCAN.set(leftSpeed);
		mtLeft.set(leftSpeed);
		mtRightCAN.set(rightSpeed);
		mtRight.set(rightSpeed);				
		mtFrontCAN.set(frontSpeed);		
		mtBackCAN.set(backSpeed);
	}
	
	public void setSpeed(double leftSpeed, double rightSpeed, double frontSpeed, double backSpeed, boolean ramp)
	{
		rightSpeed = -rightSpeed;
		backSpeed = -backSpeed;
		
		if(ramp)
		{
			leftSpeed = Util.ramp(mtLeftCAN.get(), leftSpeed, Config.Drive.rampSideRate);
			rightSpeed = Util.ramp(mtRightCAN.get(), rightSpeed, Config.Drive.rampSideRate);
			frontSpeed = Util.ramp(mtFrontCAN.get(), frontSpeed, Config.Drive.rampCenterRate);
			backSpeed = Util.ramp(mtBackCAN.get(), backSpeed, Config.Drive.rampCenterRate);
		}
		
//		System.out.println("Left " + leftSpeed + "  : Right " + rightSpeed + "  : Front " + frontSpeed + "  : Back " + backSpeed);
		
		mtLeftCAN.set(leftSpeed);
		mtLeft.set(leftSpeed);
		mtRightCAN.set(rightSpeed);
		mtRight.set(rightSpeed);				
		mtFrontCAN.set(frontSpeed);		
		mtBackCAN.set(backSpeed);
	}
	
	public void setHeading(double heading, double distance)
	{
		double angDiff = Util.absoluteAngToRelative(heading - getAngle());
		double centerPosition = distance * Math.sin(Math.toRadians(angDiff));
        double sidePosition = distance * Math.cos(Math.toRadians(angDiff));
        wantLeftPos += sidePosition;
        wantRightPos += centerPosition;
        wantFrontPos += sidePosition;
        wantBackPos += centerPosition;
        
        if(Math.abs(wantLeftPos - getLeftEncDist()) > Config.Drive.maxDistanceDiff && !pidLeft.isRunning())
			pidLeft.start();
        
        if(Math.abs(wantRightPos - getRightEncDist()) > Config.Drive.maxDistanceDiff && !pidRight.isRunning())
			pidRight.start();
        
        if(Math.abs(wantFrontPos - getFrontEncDist()) > Config.Drive.maxDistanceDiff && !pidFront.isRunning())
			pidFront.start();
        
        if(Math.abs(wantBackPos - getBackEncDist()) > Config.Drive.maxDistanceDiff && !pidBack.isRunning())
			pidBack.start();
	}
	
	// TODO: Remove this or fix this if we're gonna use it, has not been looked over
	public void setHeading(double heading, double bearing, double distance)
	{
		return;
//		double centerPosition = distance * Math.sin(Math.toRadians(heading));
//        double sidePosition = distance * Math.cos(Math.toRadians(heading));
//		double angDiff = Util.absoluteAngToRelative(bearing - heading);
//		wantLeftPos += sidePosition + (Config.Drive.robotCircumfrence * angDiff);
//		wantRightPos += sidePosition - (Config.Drive.robotCircumfrence * angDiff);
//		wantFrontPos += centerPosition + (Config.Drive.robotCircumfrence * angDiff);
//		wantBackPos += centerPosition - (Config.Drive.robotCircumfrence * angDiff);
	}
	
	public void update()
	{
		double leftSpeed = 0;
		double rightSpeed = 0;
		double frontSpeed = 0;
		double backSpeed = 0;
		
		if(Math.abs(wantLeftPos - getLeftEncDist()) < Config.Drive.maxDistanceDiff)
		{
			pidLeft.stop();
			pidLeft.reset();
		}
		
		if(Math.abs(wantRightPos - getRightEncDist()) < Config.Drive.maxDistanceDiff)
		{
			pidRight.stop();
			pidRight.reset();
		}
		
		if(Math.abs(wantFrontPos - getFrontEncDist()) < Config.Drive.maxDistanceDiff)
		{
			pidFront.stop();
			pidFront.reset();
		}
		
		if(Math.abs(wantBackPos - getBackEncDist()) < Config.Drive.maxDistanceDiff)
		{
			pidBack.stop();
			pidBack.reset();
		}
			
		if(pidLeft.isRunning())
		{
			pidLeft.update(getLeftEncDist(), wantLeftPos);
			leftSpeed = pidLeft.getOutput();
		}
		
		if(pidRight.isRunning())
		{
			pidRight.update(getRightEncDist(), wantRightPos);
			rightSpeed = pidRight.getOutput();
		}
		
		if(pidFront.isRunning())
		{
			pidFront.update(getFrontEncDist(), wantFrontPos);
			frontSpeed = pidFront.getOutput();
		}
		
		if(pidBack.isRunning())
		{
			pidBack.update(getBackEncDist(), wantBackPos);
			backSpeed = pidBack.getOutput();
		}
		
		setSpeed(leftSpeed, rightSpeed, frontSpeed, backSpeed);
	}
	
	public boolean isRunning()
	{
		return pidLeft.isRunning() && pidRight.isRunning() && pidFront.isRunning() && pidBack.isRunning();
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
		
		System.out.println("Joy Angle before adjustment " + (450 - Math.toDegrees(Math.atan2(y, x))) % 360);
		return Util.absoluteAngToRelative((450 - Math.toDegrees(Math.atan2(y, x))) % 360);
	}

	/**
	 * Gets the front encoder distance in inches
	 * @return
	 */
	public double getFrontEncDist()
	{
		return encFront.getDistance();
	}

	/**
	 * Gets the back encoder distance in inches
	 * @return
	 */
	public double getBackEncDist()
	{
		return encBack.getDistance();
	}

	/**
	 * Gets the left encoder distance in inches
	 * @return
	 */
	public double getLeftEncDist()
	{
		return encLeft.getDistance();
	}

	/**
	 * Gets the right encoder distance in inches
	 * @return
	 */
	public double getRightEncDist() 
	{
		return encRight.getDistance();
	}
	
	/**
	 * Returns the angle the bot is facing relative to the field
	 * @return
	 */
	public double getAngle()
	{
		return navX.getAngle();
	}
	
	public void encReset()
	{
		encLeft.reset();
		encRight.reset();
		encFront.reset();
		encBack.reset();
	}
	
	public void setSlowMode(boolean wantSlowMode)
	{
		slowMode = wantSlowMode;
	}
} 