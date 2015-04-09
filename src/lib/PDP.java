package lib;

import lib.Config;

public class PDP extends edu.wpi.first.wpilibj.PowerDistributionPanel
{
	public double getElevatorPowerUsage()
	{
		return super.getCurrent(Config.Elevator.idMtElevatorOne) + super.getCurrent(Config.Elevator.idMtElevatorTwo);
	}
	public void displayCurrent()
	{
		for(int i = 0; i < 16; i++)
			SmartDashboard.putNumber("Port " + i, pdp.getCurrent(i));
	}
}

