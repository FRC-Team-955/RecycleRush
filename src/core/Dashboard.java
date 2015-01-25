package core;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import util.Config;

public class Dashboard {
	Elevator elevator;
	Drive drive;
	Claw claw;
	PowerDistributionPanel pdp = new PowerDistributionPanel();
	
	public Dashboard(Drive newDrive, Elevator newElevator, Claw newClaw)
	{
		drive = newDrive;
		elevator = newElevator;
		claw = newClaw;
	}
	
	public void update()
	{
		// Elevator Data
		SmartDashboard.putString("Elevator Level", String.valueOf(elevator.getLevel()));
		SmartDashboard.putString("Elevator Distance From Base", String.valueOf(elevator.getDistanceFromBase()));
		
		// Drive Data
		// Currents
		SmartDashboard.putString("Right Talon One", String.valueOf(pdp.getCurrent(Config.Drive.chnMtRightOne)));
		SmartDashboard.putString("Right Talon Two", String.valueOf(pdp.getCurrent(Config.Drive.chnMtRightTwo)));
		SmartDashboard.putString("Left Talon One", String.valueOf(pdp.getCurrent(Config.Drive.chnMtLeftOne)));
		SmartDashboard.putString("Left Talon Two", String.valueOf(pdp.getCurrent(Config.Drive.chnMtLeftTwo)));
		SmartDashboard.putString("Front Talon", String.valueOf(pdp.getCurrent(Config.Drive.chnMtFront)));
		SmartDashboard.putString("Back Talon", String.valueOf(pdp.getCurrent(Config.Drive.chnMtBack)));
		
		//Voltages
		SmartDashboard.putString("Right Talon One", String.valueOf(drive.getVoltage(1)));
		SmartDashboard.putString("Right Talon Two", String.valueOf(drive.getVoltage(2)));
		SmartDashboard.putString("Left Talon One", String.valueOf(drive.getVoltage(3)));
		SmartDashboard.putString("Left Talon Two", String.valueOf(drive.getVoltage(4)));
		SmartDashboard.putString("Front Talon", String.valueOf(drive.getVoltage(5)));
		SmartDashboard.putString("Back Talon", String.valueOf(drive.getVoltage(6)));
	}
}
