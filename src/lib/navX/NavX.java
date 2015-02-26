package lib.navX;

import edu.wpi.first.wpilibj.SerialPort;

public class NavX extends IMUAdvanced
{
	private double offsetAngle = 0;
		
	public NavX(SerialPort serial_port, byte update_rate_hz)
	{
		super(serial_port, update_rate_hz);
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