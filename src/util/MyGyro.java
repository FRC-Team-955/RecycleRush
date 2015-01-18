package util;

import libs.Matrix;
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

	public Matrix getDirectionalCosineMatrix(){
		
		double[][] a = new double[2][2];
		a[0][0] = Math.cos(super.getAngle());
		a[0][1] = -1 * Math.sin(super.getAngle());
		a[1][0] =   Math.sin(super.getAngle());
		a[1][1] =  Math.cos(super.getAngle());
		
		
		return new Matrix(a);
	}


}