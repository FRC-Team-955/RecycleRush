package core;

import lib.ChooserType;
import lib.Controller;
import lib.Config;
import lib.Config.ContrElevator;
import lib.FileSaver;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class Dashboard 
{
	private Drive drive;
	private Elevator elevator;
	private Claw claw;
	private SendableChooser autoChooser = new SendableChooser();
	private SendableChooser driveChooser = new SendableChooser();
	private PowerDistributionPanel pdp = new PowerDistributionPanel();
	private Controller contr;
	private FileSaver fileSaver;
	
	public Dashboard(Drive newDrive, Elevator newElevator, Claw newClaw, Controller newContr)
	{
		contr = newContr;
		drive = newDrive;
		elevator = newElevator;
		claw = newClaw;
		
		/** INPUT **/
//		// Autonomous Routines
		autoChooser.addDefault("Do Nothing", new ChooserType(Config.AutoPID.idDoNothing));
		autoChooser.addObject("To Auto Zone", new ChooserType(Config.AutoPID.idToAutoZone));
		autoChooser.addObject("Move Bin to Line Up", new ChooserType(Config.AutoPID.idMoveOneBinLineUp));
		autoChooser.addObject("Move Bin to Auto Zone", new ChooserType(Config.AutoPID.idMoveOneBinToAutoZone));
		autoChooser.addObject("To Auto Zone From Landfill", new ChooserType(Config.AutoPID.idToAutoZoneLandfill));
		
//		chooser.addDefault("Do Nothing", new ChooserType(Config.Auto.idDoNothing));
//		chooser.addObject("Drive Foward Timer", new ChooserType(Config.Auto.idDriveForwardTimer));
//		chooser.addObject("Drive Foward Encoder", new ChooserType(Config.Auto.idDriveForwardEnc));
//		chooser.addObject("Get One Tote Timer", new ChooserType(Config.Auto.idGetOneToteTimer));
//		chooser.addObject("Get One Tote Encoder", new ChooserType(Config.Auto.idGetOneToteEnc));
//		chooser.addObject("Get All Totes Left Timer", new ChooserType(Config.Auto.idGetAllTotesLeftTimer));
//		chooser.addObject("Get All Totes Center Timer", new ChooserType(Config.Auto.idGetAllTotesCenterTimer));
//		chooser.addObject("Get All Totes Right Timer", new ChooserType(Config.Auto.idGetAllTotesRightTimer));
//		chooser.addObject("Get All Totes Left Encoder", new ChooserType(Config.Auto.idGetAllTotesLeftEnc));
//		chooser.addObject("Get All Totes Center Encoder", new ChooserType(Config.Auto.idGetAllTotesCenterEnc));
//		chooser.addObject("Get All Totes Right Encoder", new ChooserType(Config.Auto.idGetAllTotesRightEnc));
//		chooser.addObject("Get Recycle Bin and Tote Encoder", new ChooserType(Config.Auto.idGetRecycleBinEnc));
//		chooser.addObject("Get Recycle Bin and Tote Timer", new ChooserType(Config.Auto.idGetRecycleBinTimer));
		SmartDashboard.putData("AutoMode", autoChooser);
		
		// Drive modes
		driveChooser.addDefault("Robo Centric Drive", new ChooserType(Config.Drive.idRobotCentric));
		driveChooser.addObject("Field Centric Drive", new ChooserType(Config.Drive.idFieldCentric));
		SmartDashboard.putData("DriveMode", driveChooser);
		
		// Bot Angle Offset for beginning of match
		///SmartDashboard.putNumber("Angle Offset", 0);
		// TODO: fix the writing before file is created error
		//fileSaver.write("Angle Offset: 0");
	}
	
	public void update()
	{
		/** OUTPUT **/
		// Drive
//		SmartDashboard.putNumber("Right Talon One", pdp.getCurrent(Config.Drive.pdpChnMtRight));
//		SmartDashboard.putNumber("Right Talon Two", pdp.getCurrent(Config.Drive.pdpChnMtRightCAN));
//		SmartDashboard.putNumber("Left Talon One", pdp.getCurrent(Config.Drive.pdpChnMtLeft));
//		SmartDashboard.putNumber("Left Talon Two", pdp.getCurrent(Config.Drive.pdpChnMtLeftCAN));
//		SmartDashboard.putNumber("Front Talon", pdp.getCurrent(Config.Drive.pdpChnMtFrontCAN));
//		SmartDashboard.putNumber("Back Talon", pdp.getCurrent(Config.Drive.pdpChnMtBackCAN));
		SmartDashboard.putNumber("Front Encoder", drive.getFrontEncDist());
		SmartDashboard.putNumber("Back Encoder", drive.getBackEncDist());
		SmartDashboard.putNumber("Left Encoder", drive.getLeftEncDist());
		SmartDashboard.putNumber("Right Encoder", drive.getRightEncDist());
        SmartDashboard.putBoolean("Slow Mode", drive.getSlowMode());
		SmartDashboard.putNumber("Gyro", drive.getAngle());
		fileSaver.write("Gyro Angle:" + String.valueOf(drive.getAngle()));
		
		// Elevator
//		SmartDashboard.putNumber("ELEVATOR 1", pdp.getCurrent(Config.Elevator.pdpChnMtElevatorOneCAN));		
//		SmartDashboard.putNumber("ELEVATOR 2", pdp.getCurrent(Config.Elevator.pdpChnMtElevatorTwoCAN));
		SmartDashboard.putNumber("Elevator Want Height", elevator.getWantHeight());
		SmartDashboard.putNumber("Elevator Curr Height", elevator.getHeight());
		SmartDashboard.putNumber("Elevator Speed", elevator.getSpeed());
		SmartDashboard.putNumber("Elevator Rate", elevator.getRate());
		SmartDashboard.putBoolean("Elevator Limit Switch Top", elevator.getLimitSwitchTop());
		SmartDashboard.putBoolean("Elevator Limit Switch Bot", elevator.getLimitSwitchBot());
		SmartDashboard.putBoolean("Dropoff Mode", elevator.getDropoffMode());
		SmartDashboard.putBoolean("Elevator Brake", elevator.getBrake());
//		fileSaver.write("Elevator 1 Current:" + String.valueOf(pdp.getCurrent(Config.Elevator.pdpChnMtElevatorOneCAN)));
//		fileSaver.write("Elevator 2 Current:" + String.valueOf(pdp.getCurrent(Config.Elevator.pdpChnMtElevatorTwoCAN)));
		
		// Claw
		SmartDashboard.putBoolean("Claw", claw.getBotClaw());
		fileSaver.write("Claw Closed" + String.valueOf(claw.getBotClaw())); 
	}
	
	/**
	 * Returns the drive type
	 * @return
	 */
	public int getDriveType()
	{
		return ((ChooserType) driveChooser.getSelected()).getId();
	}
	
	public int getAutoType()
	{
		return ((ChooserType) autoChooser.getSelected()).getId();
	}
	
	/**
	 * Returns the bots angle offset
	 * @return
	 */
	public double getBotAngleOffset()
	{
		try
		{
			return ((double)SmartDashboard.getNumber("Angle Offset"));
		}
		
		catch(Exception e)
		{
			System.out.println(e);
			return 0;
		}
	}
	
	/** 
	 * Opens a new log file if one is not already open
	 */
	public void openLogFile()
	{
		if(fileSaver == null || !fileSaver.isOpen())
			fileSaver = new FileSaver(System.currentTimeMillis() + ".txt");
	}
	
	/**
	 * Closed the log file
	 */
	public void closeLogFile()
	{
		fileSaver.close();
	}
	
	public void displayCurrent()
	{
		for(int i = 0; i < 16; i++)
			SmartDashboard.putNumber("Port " + i, pdp.getCurrent(i));
	}
	
	public void displayDpad()
	{
		SmartDashboard.putBoolean("D Pad Up ", contr.getDpadUp());
		SmartDashboard.putBoolean("D Pad Left ", contr.getDpadLeft());
		SmartDashboard.putBoolean("D Pad Right ", contr.getDpadRight());
		SmartDashboard.putBoolean("D Pad Down ", contr.getDpadDown());
	}
}