package util;

import edu.wpi.first.wpilibj.Gyro;

public class MyGyro extends Gyro
{
	private double angOffset = 0;

	public MyGyro(int chn, double offsetAng) {
		super(chn);
		this.angOffset = offsetAng;
	}
	
	/**
	 * Gets the adjusted angle of the gyro
	 * @return the amount of degrees after 0 * 360k
	 */
	public double getAngle()
	{
		return (super.getAngle() % 360) - angOffset;
	}
}