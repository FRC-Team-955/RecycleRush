package core;

import auto.AutoType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import util.Config;
import util.DriveType;
import util.LIDAR;
import util.navX.NavX;

public class Dashboard {
	private Elevator elevator;
	private Drive drive;
	private Claw claw;
	private NavX navX;
	private SendableChooser chooser = new SendableChooser();
	private SendableChooser driveChooser = new SendableChooser();
	private PowerDistributionPanel pdp = new PowerDistributionPanel();
	private LIDAR rangeFinder;
	
	public Dashboard(Drive newDrive, Elevator newElevator, Claw newClaw, NavX newNavX)
	{
		drive = newDrive;
		elevator = newElevator;
		claw = newClaw;
		navX = newNavX;
	}
	
	public void update()
	{
		
		// Elevator Data
		SmartDashboard.putString("Elevator Level", String.valueOf(elevator.getLevel()));
		SmartDashboard.putString("Elevator Distance From Base", String.valueOf(elevator.getDistanceFromBase()));
		
		//Gyrosope
		SmartDashboard.putString("Gyro", String.valueOf(navX.getYaw()));
		
		//Claw
		SmartDashboard.putString("Claw", String.valueOf(claw.getClawStatus())); //True if closed
		
		// Drive Data
		//		Currents
		SmartDashboard.putString("Right Talon One", String.valueOf(pdp.getCurrent(Config.Drive.chnMtRightOne)));
		SmartDashboard.putString("Right Talon Two", String.valueOf(pdp.getCurrent(Config.Drive.chnMtRightTwo)));
		SmartDashboard.putString("Left Talon One", String.valueOf(pdp.getCurrent(Config.Drive.chnMtLeftOne)));
		SmartDashboard.putString("Left Talon Two", String.valueOf(pdp.getCurrent(Config.Drive.chnMtLeftTwo)));
		SmartDashboard.putString("Front Talon", String.valueOf(pdp.getCurrent(Config.Drive.chnMtFront)));
		SmartDashboard.putString("Back Talon", String.valueOf(pdp.getCurrent(Config.Drive.chnMtBack)));
		
		//		Voltages
		SmartDashboard.putString("Right Talon One", String.valueOf(drive.getVoltage(1)));
		SmartDashboard.putString("Right Talon Two", String.valueOf(drive.getVoltage(2)));
		SmartDashboard.putString("Left Talon One", String.valueOf(drive.getVoltage(3)));
		SmartDashboard.putString("Left Talon Two", String.valueOf(drive.getVoltage(4)));
		SmartDashboard.putString("Front Talon", String.valueOf(drive.getVoltage(5)));
		SmartDashboard.putString("Back Talon", String.valueOf(drive.getVoltage(6)));
		
		// Autonomous Routines
		chooser.addDefault("Do Nothing", new AutoType(Config.Auto.idDoNothing));
		chooser.addObject("Drive Foward Timer", new AutoType(Config.Auto.idDriveForwardTimer));
		chooser.addObject("Drive Foward Encoder", new AutoType(Config.Auto.idDriveForwardEnc));
		chooser.addObject("Get One Tote Timer", new AutoType(Config.Auto.idGetOneToteTimer));
		chooser.addObject("Get One Tote Encoder", new AutoType(Config.Auto.idGetOneToteEnc));
		chooser.addObject("Get All Totes Timer", new AutoType(Config.Auto.idGetAllTotesLeftTimer));
		chooser.addObject("Get All Totes Timer", new AutoType(Config.Auto.idGetAllTotesCenterTimer));
		chooser.addObject("Get All Totes Timer", new AutoType(Config.Auto.idGetAllTotesRightTimer));
		chooser.addObject("Get All Totes Encoder", new AutoType(Config.Auto.idGetAllTotesLeftEnc));
		chooser.addObject("Get All Totes Encoder", new AutoType(Config.Auto.idGetAllTotesCenterEnc));
		chooser.addObject("Get All Totes Encoder", new AutoType(Config.Auto.idGetAllTotesRightEnc));
		SmartDashboard.putData("AutoMode", chooser);
		
		// Drive Mode
		driveChooser.addDefault("Field Centric Drive", new DriveType(Config.Drive.idFieldCentric));
		driveChooser.addObject("Robo Centric Drive", new DriveType(Config.Drive.idRoboCentric));
		SmartDashboard.putData("Drive Mode", driveChooser);
		
		// Angle Offset
		SmartDashboard.putNumber("Angle Offset", 0);
		
	}
	
	public int getAutoType()
	{
		System.out.println(((AutoType) chooser.getSelected()).getId() + " HALLA @ ME");
		return ((AutoType) chooser.getSelected()).getId();
	}
	
	public int getDriveType()
	{
		return ((DriveType) driveChooser.getSelected()).getId();
	}
	
	public double getAngelOffset()
	{
		return  ((double)SmartDashboard.getNumber("Angle Offset"));
	}
}
