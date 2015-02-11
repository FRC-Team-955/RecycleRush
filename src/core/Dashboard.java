package core;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import util.Config;
import util.Type;
import util.LIDAR;
import util.navX.NavX;

public class Dashboard {
	private Elevator elevator;
	private Drive drive;
	private Claw claw;
	private NavX navX;
	private SendableChooser talonModeChooser = new SendableChooser();
	private SendableChooser chooser = new SendableChooser();
	private SendableChooser driveChooser = new SendableChooser();
	private PowerDistributionPanel pdp = new PowerDistributionPanel();
	
	public Dashboard(Drive newDrive, Elevator newElevator, Claw newClaw, NavX newNavX)
	{
		drive = newDrive;
		elevator = newElevator;
		claw = newClaw;
		navX = newNavX;
		SmartDashboard.putData("AutoMode", chooser);
		SmartDashboard.putData("Drive Mode", driveChooser);
	}
	
	public void update()
	{
		
		// Elevator Data
		SmartDashboard.putNumber("Elevator Level", elevator.getLevel());
		SmartDashboard.putNumber("Elevator Distance From Base", elevator.getDistanceFromBase());
		
		// Gyrosope
		SmartDashboard.putNumber("Gyro", navX.getYaw());
		
		// Claw
		SmartDashboard.putBoolean("Claw", claw.getClawStatus()); //True if closed
		
		// Drive Data
		// Currents
		SmartDashboard.putNumber("Right Talon One", pdp.getCurrent(Config.Drive.chnMtRightOne));
		SmartDashboard.putNumber("Right Talon Two", pdp.getCurrent(Config.Drive.chnMtRightTwo));
		SmartDashboard.putNumber("Left Talon One", pdp.getCurrent(Config.Drive.chnMtLeftOne));
		SmartDashboard.putNumber("Left Talon Two", pdp.getCurrent(Config.Drive.chnMtLeftTwo));
		SmartDashboard.putNumber("Front Talon", pdp.getCurrent(Config.Drive.chnMtFront));
		SmartDashboard.putNumber("Back Talon", pdp.getCurrent(Config.Drive.chnMtBack));
		
		// Voltages
		SmartDashboard.putNumber("Right Talon One", drive.getVoltage(1));
		SmartDashboard.putNumber("Right Talon Two", drive.getVoltage(2));
		SmartDashboard.putNumber("Left Talon One", drive.getVoltage(3));
		SmartDashboard.putNumber("Left Talon Two", drive.getVoltage(4));
		SmartDashboard.putNumber("Front Talon", drive.getVoltage(5));
		SmartDashboard.putNumber("Back Talon", drive.getVoltage(6));
		
		// Autonomous Routines
		chooser.addDefault("Do Nothing", new Type(Config.Auto.idDoNothing));
		chooser.addObject("Drive Foward Timer", new Type(Config.Auto.idDriveForwardTimer));
		chooser.addObject("Drive Foward Encoder", new Type(Config.Auto.idDriveForwardEnc));
		chooser.addObject("Get One Tote Timer", new Type(Config.Auto.idGetOneToteTimer));
		chooser.addObject("Get One Tote Encoder", new Type(Config.Auto.idGetOneToteEnc));
		chooser.addObject("Get All Totes Left Timer", new Type(Config.Auto.idGetAllTotesLeftTimer));
		chooser.addObject("Get All Totes Center Timer", new Type(Config.Auto.idGetAllTotesCenterTimer));
		chooser.addObject("Get All Totes Right Timer", new Type(Config.Auto.idGetAllTotesRightTimer));
		chooser.addObject("Get All Totes Left Encoder", new Type(Config.Auto.idGetAllTotesLeftEnc));
		chooser.addObject("Get All Totes Center Encoder", new Type(Config.Auto.idGetAllTotesCenterEnc));
		chooser.addObject("Get All Totes Right Encoder", new Type(Config.Auto.idGetAllTotesRightEnc));
		
		//Talon mode (SRX inclusion or not)
		talonModeChooser.addDefault("TalonSRX", new Type(Config.Drive.idTalonSRX));
		talonModeChooser.addObject("TalonOLD", new Type(Config.Drive.idTalon));
		
		// Drive Mode
		driveChooser.addDefault("Field Centric Drive", new Type(Config.Drive.idFieldCentric));
		driveChooser.addObject("Robo Centric Drive", new Type(Config.Drive.idRobotCentric));
		
		// Angle Offset
		SmartDashboard.putNumber("Angle Offset", 0);
		
	}
	
	public int getAutoType()
	{
		System.out.println(((Type) chooser.getSelected()).getId() + " HALLA @ ME");
		return ((Type) chooser.getSelected()).getId();
	}
	
	public int getTalonModeType()
	{
		return ((Type) talonModeChooser.getSelected()).getId();
	}
	
	public int getDriveType()
	{
		return ((Type) driveChooser.getSelected()).getId();
	}
	
	public double getAngelOffset()
	{
		return  ((double)SmartDashboard.getNumber("Angle Offset"));
	}
}
