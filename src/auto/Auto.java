package auto;

import core.Claw;
import core.Drive;
import core.Elevator;

public class Auto
{
	// Subsystems for auto to control
	private Drive drive;
	private Elevator elevator;
	private Claw claw;
	
	public Auto(Drive newDrive, Elevator newElevator, Claw newClaw)
	{
		drive = newDrive;
		elevator = newElevator;
		claw = newClaw;
	}
}