package lib;

import lib.Config;

public class PDP extends edu.wpi.first.wpilibj.PowerDistributionPanel
{
	public double getElevatorPowerUsage()
	{
		return super.getCurrent(Config.Elevator.idMtElevatorOne) + super.getCurrent(Config.Elevator.idMtElevatorTwo);
	}
	
}

