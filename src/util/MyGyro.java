package util;

import edu.wpi.first.wpilibj.Gyro;

public class MyGyro extends Gyro
{
	private double angOffset = 0;
	
	public MyGyro(int chn, double offsetAng)
	{
		super(chn);
		this.angOffset = offsetAng;
	}
	
	public double getAngle()
	{
		return (super.getAngle() % 360) - angOffset;
	}
}
