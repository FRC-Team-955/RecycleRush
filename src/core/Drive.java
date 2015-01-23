package core;

import edu.wpi.first.wpilibj.CANTalon;
import util.NegInertia;
import util.Config;
import util.MyGyro;
import java.lang.Math;
import util.Controller;

public class Drive {
	private CANTalon mtRightOne = new CANTalon(Config.Drive.chnMtRightOne);
	private CANTalon mtRightTwo = new CANTalon(Config.Drive.chnMtRightTwo);
	private CANTalon mtLeftOne = new CANTalon(Config.Drive.chnMtLeftOne);
	private CANTalon mtLeftTwo = new CANTalon(Config.Drive.chnMtLeftTwo);
	private CANTalon mtFront = new CANTalon(Config.Drive.chnMtFront);
	private CANTalon mtBack = new CANTalon(Config.Drive.chnMtBack);
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
	
	/**
	 * Returns Voltage of the asked talon
	 * @param talonNum Numbers :mtRightOne 1, mtRightTwo, mtLeftOne 3, mtLeftTwo 4, mtFront 5, mtBack 6
	 * @return Voltage of the talon
	 */
	public double getVoltage(int talonNum)
	{
		switch(talonNum)
		{
			case 1: return mtRightOne.getBusVoltage();
			case 2: return mtRightTwo.getBusVoltage();
			case 3: return mtLeftOne.getBusVoltage();
			case 4: return mtLeftTwo.getBusVoltage();
			case 5: return mtFront.getBusVoltage();
			case 6: return mtBack.getBusVoltage();
		}
		
		return 0;
	}
}
