package core;

import lib.Controller;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class Dashboard 
{
	private Drive drive;
	private Elevator elevator;
	private SendableChooser autoChooser = new SendableChooser();
	private SendableChooser driveChooser = new SendableChooser();
	private PowerDistributionPanel pdp = new PowerDistributionPanel();
	private Controller contr;
	
	public Dashboard(Drive newDrive, Elevator newElevator, Controller newContr)
	{
		contr = newContr;
		drive = newDrive;
		elevator = newElevator;
	}
	
	public void update()
	{		
		// Elevator
//		SmartDashboard.putNumber("ELEVATOR 1", pdp.getCurrent(Config.Elevator.pdpChnMtElevatorOneCAN));		
//		SmartDashboard.putNumber("ELEVATOR 2", pdp.getCurrent(Config.Elevator.pdpChnMtElevatorTwoCAN));
		SmartDashboard.putNumber("Elevator Want Height", elevator.getWantHeight());
		SmartDashboard.putNumber("Elevator Curr Height", elevator.getHeight());
		SmartDashboard.putNumber("Elevator Speed", elevator.getSpeed());
		SmartDashboard.putNumber("Elevator Rate", elevator.getRate());
//		SmartDashboard.putBoolean("Dropoff Mode", elevator.getDropoffMode());
		SmartDashboard.putBoolean("Elevator Brake", elevator.getBrake());
//		fileSaver.write("Elevator 1 Current:" + String.valueOf(pdp.getCurrent(Config.Elevator.pdpChnMtElevatorOneCAN)));
//		fileSaver.write("Elevator 2 Current:" + String.valueOf(pdp.getCurrent(Config.Elevator.pdpChnMtElevatorTwoCAN)));
		
	}
}