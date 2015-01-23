package core;

import edu.wpi.first.wpilibj.Talon;
import util.NegInertia;
import util.Config;
import util.MyGyro;
import java.lang.Math;
import util.Controller;

public class Drive {
	private Talon mtRightOne = new Talon(Config.Drive.chnMtRightOne);
	private Talon mtRightTwo = new Talon(Config.Drive.chnMtRightTwo);
	private Talon mtLeftOne = new Talon(Config.Drive.chnMtLeftOne);
	private Talon mtLeftTwo = new Talon(Config.Drive.chnMtLeftTwo);
	private Talon mtFront = new Talon(Config.Drive.chnMtFront);
	private Talon mtBack = new Talon(Config.Drive.chnMtBack);
	private Controller contr;
	private MyGyro gyro = new MyGyro(1, 0.0);
	private NegInertia negInertia;
	
	public Drive (Controller newContr) 
	{
        contr = newContr;
        gyro.reset();
    }
	
	/**
	 * Moves the robot in relation to the field instead of the robot
	 */
	public void fieldCentric() 
	{
		double rightJoyXPos = contr.getRightX();
		
		//Moving with left joy
		if(rightJoyXPos < Config.Drive.tolerance && rightJoyXPos > -Config.Drive.tolerance) {
			double joyX = contr.getLeftX();
			double joyY = contr.getLeftY();
			double joyMag = Math.sqrt((joyX * joyX) + (joyY * joyY));
			double joyAng = (450 - Math.toDegrees(Math.atan2(joyY, joyX))) % 360;
			double angDiff = gyro.getAngle() - joyAng;
			double centerSpeed = joyMag * Math.sin(Math.toRadians(angDiff));
			double sideSpeed = joyMag * Math.cos(Math.toRadians(angDiff));
			
			setSpeed(sideSpeed, sideSpeed, centerSpeed, centerSpeed);
		}
		
		//Turning with right joy
		else
		{
			mtRightOne.set(negInertia.getTurn(rightJoyXPos));
			mtRightTwo.set(negInertia.getTurn(rightJoyXPos));
			mtLeftOne.set(negInertia.getTurn(-rightJoyXPos));
			mtLeftTwo.set(negInertia.getTurn(-rightJoyXPos));
			mtFront.set(negInertia.getTurn(rightJoyXPos));
			mtBack.set(negInertia.getTurn(-rightJoyXPos));
		}		
	} 

	/**
	 * Sets the speed of all motors
	 * @param leftSpeed speed for the left motor
	 * @param rightSpeed speed for the right motor
	 * @param frontSpeed speed for the front motor
	 * @param backSpeed speed for the back motor
	 */
	public void setSpeed(double leftSpeed, double rightSpeed, double frontSpeed, double backSpeed)
	{
		leftSpeed = -leftSpeed;
		frontSpeed = -frontSpeed;
		mtRightOne.set(rightSpeed);
		mtRightTwo.set(rightSpeed);		
		mtLeftOne.set(leftSpeed);
		mtLeftTwo.set(leftSpeed);		
		mtFront.set(frontSpeed);		
		mtBack.set(backSpeed);	
	}
	
}
