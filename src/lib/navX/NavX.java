package lib.navX;

import lib.Config;
import edu.wpi.first.wpilibj.SerialPort;

public class NavX extends IMUAdvanced
{
	private double offsetAngle = 0;
	private static SerialPort serial = new SerialPort(Config.NavX.baudRate, SerialPort.Port.kMXP); 
		
	public NavX()
	{
		super(serial, Config.NavX.updateRateHz);
	}

	public double getAngle()
	{
		return super.getYaw() +  offsetAngle;
	}
	
	public void setOffsetAngle(double ang)
	{
		offsetAngle = ang;
	}
}