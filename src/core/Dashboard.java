package core;

import lib.ChooserType;
import lib.Config;
import lib.FileSaver;
import lib.navX.NavX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class Dashboard 
{
	private Elevator elevator;
	private Drive drive;
	private Claw claw;
	private NavX navX;
//	private SendableChooser talonModeChooser = new SendableChooser();
//	private SendableChooser autoChooser = new SendableChooser();
	private SendableChooser driveChooser = new SendableChooser();
	private PowerDistributionPanel pdp = new PowerDistributionPanel();
	private FileSaver fileSaver;
	
	public Dashboard(Drive newDrive, Elevator newElevator, Claw newClaw, NavX newNavX)
	{
		drive = newDrive;
		elevator = newElevator;
		claw = newClaw;
		navX = newNavX;
//		// Autonomous Routines
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
//		
//		//Talon mode (SRX inclusion or not)
//		talonModeChooser.addDefault("TalonSRX", new ChooserType(Config.Drive.idTalonSRX));
//		talonModeChooser.addObject("Talon", new ChooserType(Config.Drive.idTalon));
//		SmartDashboard.putData("AutoMode", autoChooser);
		driveChooser.addDefault("Field Centric Drive", new ChooserType(Config.Drive.idFieldCentric));
		driveChooser.addObject("Robo Centric Drive", new ChooserType(Config.Drive.idRobotCentric));
		SmartDashboard.putData("DriveMode", driveChooser);
		fileSaver = new FileSaver(String.valueOf(System.currentTimeMillis()));
	}
	
	public void update()
	{
		// Gyro
		SmartDashboard.putNumber("Gyro", navX.getAngle());
		fileSaver.write("Gyro Angle:" + String.valueOf(navX.getAngle()));
		
		// Claw
		SmartDashboard.putBoolean("Claw", claw.getClawStatus());
		fileSaver.write("Claw Open" + String.valueOf(claw.getClawStatus()));
		
		/** Drive Data **/
		// Currents
		SmartDashboard.putNumber("Right Talon One", pdp.getCurrent(Config.Drive.pdpChnMtRight));
		SmartDashboard.putNumber("Right Talon Two", pdp.getCurrent(Config.Drive.pdpChnMtRightCAN));
		SmartDashboard.putNumber("Left Talon One", pdp.getCurrent(Config.Drive.pdpChnMtLeft));
		SmartDashboard.putNumber("Left Talon Two", pdp.getCurrent(Config.Drive.pdpChnMtLeftCAN));
		SmartDashboard.putNumber("Front Talon", pdp.getCurrent(Config.Drive.pdpChnMtFrontCAN));
		SmartDashboard.putNumber("Back Talon", pdp.getCurrent(Config.Drive.pdpChnMtBackCAN));
		
		SmartDashboard.putNumber("ELEVATOR 1", pdp.getCurrent(14));
		fileSaver.write("Elevator 1 Current:" + String.valueOf(pdp.getCurrent(14)));
		
		SmartDashboard.putNumber("ELEVATOR 2", pdp.getCurrent(15));
		fileSaver.write("Elevator 2 Current:" + String.valueOf(pdp.getCurrent(15)));
		
		SmartDashboard.putNumber("HALP NO CURRENT PLS", pdp.getCurrent(5));
		//fileSaver.write("HALP NO CURRENT PLS:" + String.valueOf(pdp.getCurrent(5)));
		
		// Voltages
//		SmartDashboard.putNumber("Right Talon One", drive.getVoltage(1));
//		fileSaver.write("Right Talon One Voltage:" + String.valueOf(drive.getVoltage(1)));
//		
//		SmartDashboard.putNumber("Right Talon Two", drive.getVoltage(2));
//		fileSaver.write("Right Talon Two Voltage:" + String.valueOf(drive.getVoltage(2)));
//		
//		SmartDashboard.putNumber("Left Talon One", drive.getVoltage(3));
//		fileSaver.write("Left Talon One Voltage:" + String.valueOf(drive.getVoltage(3)));
//		
//		
//		SmartDashboard.putNumber("Left Talon Two", drive.getVoltage(4));
//		fileSaver.write("Left Talon Two Voltage:" + String.valueOf(drive.getVoltage(4)));
//		
//		SmartDashboard.putNumber("Front Talon", drive.getVoltage(5));
//		fileSaver.write("Front Talon:" + String.valueOf(drive.getVoltage(5)));
//		
//		SmartDashboard.putNumber("Back Talon", drive.getVoltage(6));
//		fileSaver.write("Back Talon:" + String.valueOf(drive.getVoltage(6)));
		
		// Angle Offset
		SmartDashboard.putNumber("Angle Offset", 0);
		
		fileSaver.write("Angle Offset: 0"); 
	}
	
//	public int getAutoType()
//	{
//		return ((ChooserType) chooser.getSelected()).getId();
//	}
//	
//	public int getTalonModeType()
//	{
//		return ((ChooserType) talonModeChooser.getSelected()).getId();
//	}
//	
	public int getDriveType()
	{
		return ((ChooserType) driveChooser.getSelected()).getId();
	}
	
	public double getAngleOffset()
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
	
	public void closeLogFile()
	{
		if(fileSaver.getWrittenTo())
			fileSaver.close();
	}
}