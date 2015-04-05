package core;

import lib.PID;
import lib.Util;
import lib.Config;
import lib.Controller;
import lib.navX.NavX;
import lib.LimitSwitch;

import java.lang.Math;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;

public class Drive 
{
//	LimitSwitch ls2 = new LimitSwitch(13, false);
//	LimitSwitch ls3 = new LimitSwitch(14, false);
//	LimitSwitch ls4 = new LimitSwitch(19, false);
//	LimitSwitch ls5 = new LimitSwitch(20, false);
//	LimitSwitch ls6 = new LimitSwitch(21, false);
//	LimitSwitch ls7 = new LimitSwitch(22, false);
//	LimitSwitch ls8 = new LimitSwitch(23, false);
//	LimitSwitch ls9 = new LimitSwitch(24, false);
	
	// Encoders connected to drive base
	private Encoder encFront = new Encoder(Config.Drive.chnEncFrontA, Config.Drive.chnEncFrontB); 
	private Encoder encBack = new Encoder(Config.Drive.chnEncBackA, Config.Drive.chnEncBackB);
	private Encoder encLeft = new Encoder(Config.Drive.chnEncLeftA, Config.Drive.chnEncLeftB);
	private Encoder encRight = new Encoder(Config.Drive.chnEncRightA, Config.Drive.chnEncRightB);
		
	// CAN Talons for controlling drive base
	private CANTalon mtLeftCAN = new CANTalon(Config.Drive.idMtLeft);
	private CANTalon mtRightCAN = new CANTalon(Config.Drive.idMtRight);
	private CANTalon mtFrontCAN = new CANTalon(Config.Drive.idMtFront);
	private CANTalon mtBackCAN = new CANTalon(Config.Drive.idMtBack);
	
	// REG Talons for controlling drive base
	private Talon mtLeft = new Talon(Config.Drive.chnMtLeft);
	private Talon mtRight = new Talon(Config.Drive.chnMtRight);
	
	// NavX for gyro
	private NavX navX = new NavX();
	
	// Controller that controls driv base
	private Controller contr;
	
	// PID
	private PID pidStrafe = new PID(Config.Drive.kStrafeP, Config.Drive.kStrafeI, Config.Drive.kStrafeD);
	private PID pidRotate = new PID(Config.Drive.kRotateP, Config.Drive.kRotateI, Config.Drive.kRotateD);
	private PID pidLeft= new PID(Config.Drive.kLeftP, Config.Drive.kLeftI, Config.Drive.kLeftD);
	private PID pidRight = new PID(Config.Drive.kRightP, Config.Drive.kRightI, Config.Drive.kRightD);
//	private PID pidFront = new PID(Config.Drive.kFrontP, Config.Drive.kFrontI, Config.Drive.kFrontD);
//	private PID pidBack = new PID(Config.Drive.kBackP, Config.Drive.kBackI, Config.Drive.kBackD);
	
	// PID variables
	private double wantLeftPos = 0;
	private double wantRightPos = 0;
	private double wantFrontPos = 0;
	private double wantBackPos = 0;
	private double wantStrafeAng = 0;
	private double prevTurnSpeed = 0;
	private double wantAng = 0;
	
	// Field centric/Slow mode
	private boolean fieldCentricMode = false;
	private boolean isSlowMode = false;
	
	public Drive (Controller newContr) 
	{
        contr = newContr;
        encFront.setDistancePerPulse((4 * Math.PI) / 256);
        encBack.setDistancePerPulse((4 * Math.PI) / 256);	
        encLeft.setDistancePerPulse((4 * Math.PI) / 256);
        encRight.setDistancePerPulse((4 * Math.PI) / 256);
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
		System.out.println("NEW SET, FRONT BACK LEFT RIGHT");
		System.out.println(encFront.getRaw());
		System.out.println(encBack.getRaw());
		System.out.println(encLeft.getRaw());
		System.out.println(encRight.getRaw());
		// Enable/Disable field centric mode
        if(contr.getDpadLeft())
			fieldCentricMode = true;
		
		else if(contr.getDpadRight())
			fieldCentricMode = false;
		
        // Enable/Disable slow mode
        if(contr.getButton(Config.ContrDrive.btToggleSlowMode))
        	setSlowMode(!getSlowMode());
        
        // If field centric, get ang diff, else get joy angle
		double newAng = fieldCentricMode ? getJoyAng() - getAngle() : getJoyAng();
		double leftJoyMag = getJoyMag();
		double centerSpeed = leftJoyMag * Math.sin(Math.toRadians(newAng));
        double sideSpeed = leftJoyMag * Math.cos(Math.toRadians(newAng));
        double turnSpeed = Math.abs(contr.getRawRightX()) * contr.getRawRightX();    
        
//        System.out.println("newAng: " + newAng + " leftJoyMag: " + leftJoyMag + " || " + centerSpeed + " || " + sideSpeed + " || " + turnSpeed);
        
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
			turnSpeed = pidStrafe.getOutput();
		}
		
		setSpeed(sideSpeed, centerSpeed, turnSpeed, true);
	}
	
	public void setSpeed(double sideSpeed, double centerSpeed, double turnSpeed, boolean ramp)
	{
		turnSpeed = Util.limit(turnSpeed, Config.Drive.minTurnSpeed, Config.Drive.maxTurnSpeed);
		turnSpeed = Util.ramp(prevTurnSpeed, turnSpeed, Config.Drive.rampTurnRate);
		prevTurnSpeed = turnSpeed;
		
		double leftSpeed = sideSpeed + turnSpeed;
		double rightSpeed = sideSpeed - turnSpeed;
		double frontSpeed = centerSpeed + turnSpeed;
		double backSpeed = centerSpeed - turnSpeed;
		
		setSpeed(leftSpeed, rightSpeed, frontSpeed, backSpeed, ramp);
	}
	
	/**
	 * Sets the speed of all motors
	 * @param leftSpeed speed for the left motor
	 * @param rightSpeed speed for the right motor
	 * @param frontSpeed speed for the front motor
	 * @param backSpeed speed for the back motor
	 */
	public void setSpeed(double leftSpeed, double rightSpeed, double frontSpeed, double backSpeed, boolean ramp)
	{
		rightSpeed = -rightSpeed;
		frontSpeed = -frontSpeed;
		frontSpeed = 0;
		backSpeed = 0;
		
		double rampSideRate = Config.Drive.rampSideRate;
		double rampCenterRate = Config.Drive.rampCenterRate;
		
		if(getSlowMode())
		{
			leftSpeed *= Config.Drive.slowSideSpeedScalar;
			rightSpeed *= Config.Drive.slowSideSpeedScalar;
			frontSpeed *= Config.Drive.slowCenterSpeedScalar;
			backSpeed *= Config.Drive.slowCenterSpeedScalar;
		}
		
		if(ramp)
		{
			leftSpeed = Util.ramp(mtLeftCAN.get(), leftSpeed, rampSideRate);
			rightSpeed = Util.ramp(mtRightCAN.get(), rightSpeed, rampSideRate);
//			frontSpeed = Util.ramp(mtFrontCAN.get(), frontSpeed, rampCenterRate);
//			backSpeed = Util.ramp(mtBackCAN.get(), backSpeed, rampCenterRate); 
		}
		
		mtLeftCAN.set(leftSpeed);
		mtLeft.set(leftSpeed);
		mtRightCAN.set(rightSpeed);
		mtRight.set(rightSpeed);				
		mtFrontCAN.set(frontSpeed);		
		mtBackCAN.set(backSpeed);
	}
	
	
	/**
	 * Sets the drive base pid want position at heading at distance
	 * @param heading
	 * @param distance
	 */
	public void setDistance(double distance)
	{
        wantLeftPos = distance;
        wantRightPos = distance;

        
        if(Math.abs(wantLeftPos - getLeftEncDist()) > Config.Drive.maxDistanceDiff && !pidLeft.isRunning())
			pidLeft.start();
        
        if(Math.abs(wantRightPos - getRightEncDist()) > Config.Drive.maxDistanceDiff && !pidRight.isRunning())
			pidRight.start();        
        
//        System.out.println(wantLeftPos + " : " + wantRightPos + " : " + wantFrontPos + " : " + wantBackPos);

	}
	
	/**
	 * Updates the drive base pid based on wanted positions set by setHeading()
	 */
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
				
		setSpeed(leftSpeed, rightSpeed, frontSpeed, backSpeed, true);

	}
	
	public void updateAng()
	{
		double angleDiff = Util.absoluteAngToRelative(wantAng - getAngle());
		double turnSpeed = 0;
			
		if(Math.abs(getAngle() - wantAng) < Config.Drive.minAngleDiff)
		{
			pidRotate.stop();
			pidRotate.reset();
		}
			
		if(pidRotate.isRunning())
		{
			pidRotate.update(getAngle(), angleDiff);
			turnSpeed = pidRotate.getOutput();
		}
		
		setSpeed(0, 0, turnSpeed, true);
	}
	
	public void setAng(double heading)
	{
		wantAng = heading;
		
		if(Math.abs(getAngle() - heading) > Config.Drive.minAngleDiff)
		{
			if(!pidRotate.isRunning())
			{
				pidRotate.reset();
				pidRotate.start();
			}
		}
	}
	
	
	
	/**
	 * Checks if the drive is still working to get to a destination,
	 * true for still working to get there
	 * false for doing nothing currently
	 * @return
	 */
	public boolean isRunning()
	{
		return pidLeft.isRunning() || pidRight.isRunning() /*|| pidFront.isRunning() || pidBack.isRunning()*/;
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
		
		return Util.absoluteAngToRelative((450 - Math.toDegrees(Math.atan2(y, x))) % 360);
	}

	/**
	 * Gets the front encoder distance in inches
	 * @return
	 */
	public double getFrontEncDist()
	{
		return -encFront.getDistance();
	}

	/**
	 * Gets the back encoder distance in inches
	 * @return
	 */
	public double getBackEncDist()
	{
		return -encBack.getDistance();
	}

	/**
	 * Gets the left encoder distance in inches
	 * @return
	 */
	public double getLeftEncDist()
	{
		return -encLeft.getDistance();
	}

	/**
	 * Gets the right encoder distance in inches
	 * @return
	 */
	public double getRightEncDist() 
	{
		return -encRight.getDistance();
		//return encRight.getDistance();
	}
	
	/**
	 * Returns the angle the bot is facing relative to the field
	 * @return
	 */
	public double getAngle()
	{
		return navX.getAngle();
	}
	
	/**
	 * Resets all the drive base encoders to 0
	 */
	public void encReset()
	{
		encLeft.reset();
		encRight.reset();
		encFront.reset();
		encBack.reset();
	}
	
	public void setSlowMode(boolean slowMode)
	{
		isSlowMode = slowMode;
	}
	
	public boolean getSlowMode()
	{
		return isSlowMode;
	}
	
	public boolean isRotating()
	{
		return pidRotate.isRunning();
	}
	
	public void setLeftCAN()
	{
		mtLeftCAN.set(1);
		mtLeft.set(0);
		mtRightCAN.set(0);
		mtRight.set(0);
	}
	
	public void setLeft()
	{
		mtLeft.set(1);
		mtLeftCAN.set(0);
		mtRightCAN.set(0);
		mtRight.set(0);
	}
	
	public void setRightCAN()
	{
		mtRightCAN.set(-1);
		mtLeftCAN.set(0);
		mtLeft.set(0);
		mtRight.set(0);
	}
	
	public void setRight()
	{
		mtRight.set(-1);
		mtLeftCAN.set(0);
		mtLeft.set(0);
		mtRightCAN.set(0);
	}
} 